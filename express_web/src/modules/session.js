/**
 *   let options = {
            secret:cookie_secret,
            hmacSecret:'',
            maxAge: 1000 * 60 * 90, // would expire after 15 minutes
            httpOnly: true, // The cookie only accessible by the web server
            // signed: true // Indicates if the cookie should be signed
        }
 * @param options
 * @returns {Function}
 */

const crypto = require('crypto');


class Session {
    constructor(options) {
        this._priv = {};
        this._priv._sessionObj = options.sessionObj;
        this._priv._options = options.options;
    }

    init(req, res, callback) {
        const sessionId = this._generateId();
        const _this = this;
        //Create a new session
        Session._store.save(sessionId, {}, _this._getTTLSeconds(), function (err, createdSession) {
            res.clearCookie(Session.sessionCookieName);
            if (err) {
                return callback(err, createdSession);
            } else {
                res.cookie(Session.sessionCookieName, sessionId, _this._priv._options);
                req.sessionId = sessionId;
                Object.assign(_this, createdSession);
                req.session = _this;
                return callback(null);
            }
        });
    }

    load(req, res, callback) {
        const _this = this;
        try {
            this._verify(req, res);
            Session._store.get(req.cookies[Session.sessionCookieName], function (err, session) {
                if (err) return callback(err);

                Object.assign(_this, session);
                console.log('ttl: ')
                console.log(_this._getTTLSeconds())
                if (_this._priv._options.renewSession &&
                    _this._getTTLSeconds() <=  60 * 5) {
                   console.log('calling renew')
                    _this._renew(req, res);
                }
                req.sessionId = req.cookies[Session.sessionCookieName] || -1;
                req.session = _this || {};
                return callback(null);
            })
        } catch (err) {
            return callback(err);
        }
    }

    destroy(req, res, callback) {
        const _this = this;
        //destroy session obj
        Session._store.destroy(req.cookies[Session.sessionCookieName], function (err) {
            if (err) {
                return callback(err);
            } else {
                delete req.sessionId;
                delete req.session;
                res.clearCookie(Session.sessionCookieName);
                return callback(null);
            }
        })
    }

    save(req, res, callback) {
        const _this = this;
        const ttl = _this._getTTLSeconds();
        const priv = _this._priv;
        delete _this._priv;

        if (priv._requiresNewCookie) {
            const newSessionId = _this._generateId();
            Session._store.save(newSessionId, _this, ttl, function (err) {
                _this._priv = priv;
                if (err) {
                    return callback(err);
                } else {
                    //Attempt to destroy the current session, if we can't ignore the error because
                    //it will expire automatically after ttl mins.
                    Session._store.destroy(req.cookies[Session.sessionCookieName], function (err) {
                    });
                    res.clearCookie(Session.sessionCookieName);
                    res.cookie(Session.sessionCookieName, newSessionId, _this._priv._options);
                    _this._priv._requiresNewCookie = false;
                    return callback(null);
                }
            })

        } else {
            Session._store.save(req.cookies[Session.sessionCookieName], _this, ttl, function (err) {
                _this._priv = priv;
                if (err) {
                    return callback(err);
                } else {
                    return callback(null);
                }
            })
        }
    }

    addHeader(key, val, callback) {
        this._priv._sessionObj[key] = val;
        this._priv._requiresNewCookie = true;

    }

    removeHeader(key) {
        delete this._priv._sessionObj[key];
        this._priv._requiresNewCookie = true;
    }

    _getTTLSeconds() {
        const _this = this;
        const time_issued = this._priv._sessionObj.sessionIdHeader.time_issued;
        const date_issued = new Date(time_issued);
        const dateNow = new Date();
        const timePassed = new Date(dateNow - date_issued);
        const minutes = timePassed.getSeconds();
        const ttl = _this._priv._options.maxAge || (1000 * 60 * 90);
        const ttlMinutes = ttl / 1000;
        return ttlMinutes - minutes;
    }

    _renew(req, res) {
        console.log('renewed')
        const _this = this;
        Session._store.destroy(req.cookies[Session.sessionCookieName], function (err) {
            if (err) {
                console.log(err);
                return;
            }
            res.clearCookie(Session.sessionCookieName);
            _this._priv._sessionObj.sessionIdHeader.time_issued = new Date().getTime();
            const sessionId = _this._generateId();
            const priv = _this._priv;
            delete _this._priv;
            console.log('Saving this under id ' + sessionId);
            console.dir(_this);
            Session._store.save(sessionId, _this, priv._options.maxAge / 1000 / 60, function (err) {
                _this._priv = priv;
                if (err) {
                    console.log(err);
                    return;
                }

                console.log('setting new cookie');
                res.cookie(Session.sessionCookieName, sessionId, _this._priv._options);
            });
        });
    }

    _verify(req, res) {
        const sessId = req.cookies[Session.sessionCookieName];
        const session_id_parts = sessId.split(':');
        if (session_id_parts.length == 2) {
            const sessionIdJson = Buffer.from(session_id_parts[0], 'base64').toString('ascii');
            const hmac = session_id_parts[1];
            const reHmac = crypto.createHmac('sha1', this._priv._options.hmacSecret).update(sessionIdJson).digest('hex');

            //Verify hmac
            if (reHmac == hmac) {
                const sessionIdObj = JSON.parse(sessionIdJson);
                this._priv._sessionObj = sessionIdObj;
                const {sessionIdHeader, sessionIdRandomPart} = sessionIdObj;

                if (sessionIdHeader && sessionIdRandomPart != undefined) {
                    const userIp = sessionIdHeader.ip;
                    const userAgent = sessionIdHeader.user_agent;

                    if (userIp == req.ip && (req.headers['user-agent'] || '') == userAgent) {
                        return true;
                    } else {
                        throw new Error('Session key validation error');
                    }
                }
            }
        }
    }

    _generateId() {
        const sessionId = this._priv._sessionObj;
        const sessionIdJson = JSON.stringify(sessionId);
        const sessionIdMac = crypto.createHmac('sha1', this._priv._options.hmacSecret).update(sessionIdJson).digest('hex');
        const sessionIdEncoded = Buffer.from(sessionIdJson).toString('base64');
        const base64SessionId = `${sessionIdEncoded}:${sessionIdMac}`;
        return base64SessionId;
    }
}

/**
 * Potential updates:
 *
 * @param options
 * @returns {Function}
 */
module.exports = function (options) {
    if (!options || !options.hmacSecret || !options.secret || !options.store) {
        throw new Error('Invalid options arguments')
    }

    Session._store = options.store;
    Session.sessionCookieName = options.sessionCookieName || 'sessionId';

    //Clean the session store before we start handling sessions
    Session._store.clearAll();

    function patchResponse(req, res, session, next) {
        const _send = res.send;
        res.send = function () {
            const _this = this;
            const args = Array.prototype.slice.call(arguments);
            session.save(req, res, function (error) {
                if (error) {
                    error.type = 'session';
                    console.log('error saving session')
                    return next(error);
                }
                _send.apply(_this, args);
            })
        }
    }

    return function (req, res, next) {
        if (req.cookies[Session.sessionCookieName]) {
            const session = new Session({
                options
            });

            session.load(req, res, function (err) {
                if (err) {
                    return next(err);
                }

                console.log(req.session)
                console.log(req.sessionId)

                patchResponse(req, res, session, next);

                return next();
            });


        } else {
            console.log('new sess')
            const sessionObj = {
                sessionIdHeader: {
                    ip: req.ip,
                    user_agent: req.headers['user-agent'] || '',
                    time_issued: new Date().getTime()
                },
                sessionIdRandomPart: 3213123
            }

            const session = new Session({
                sessionObj,
                options
            });

            session.init(req, res, function (err) {
                if (err) {
                    return next(err);
                }

                patchResponse(req, res, session, next);
                return next();
            });
        }
    }
}