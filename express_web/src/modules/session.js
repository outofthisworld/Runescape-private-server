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
        Session._store.create(sessionId, function (err, createdSession) {
            if (err) {
                res.clearCookie(Session.sessionCookieName);
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

                try {
                    req.sessionId = req.cookies[Session.sessionCookieName] || -1;
                    req.session = _this || {};
                    console.log('set session')
                    console.log(req)
                    return callback(null);
                } catch (err) {
                    console.log(err);
                }
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
            }
        })
    }

    save(req, res, callback) {
        const _this = this;
        const priv = _this._priv;
        delete _this._priv;

        if (priv._requiresNewCookie) {
            const newSessionId = _this._generateId();
            Session._store.save(newSessionId, _this, function (err) {
                _this._priv = priv;
                if (err) {
                    return callback(err);
                } else {
                    Session._store.destroy(req.cookies[Session.sessionCookieName], function (err) {
                    })
                    res.clearCookie(Session.sessionCookieName);
                    res.cookie(Session.sessionCookieName, newSessionId, _this._priv._options);
                    _this._priv._requiresNewCookie = false;
                    return callback(null);
                }
            })

        } else {
            console.log('saving session')
            Session._store.save(req.cookies[Session.sessionCookieName], _this, function (err) {
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
        console.log(this._priv.options)
        const sessionIdMac = crypto.createHmac('sha1', this._priv._options.hmacSecret).update(sessionIdJson).digest('hex');
        const sessionIdEncoded = Buffer.from(sessionIdJson).toString('base64');
        const base64SessionId = `${sessionIdEncoded}:${sessionIdMac}`;

        return base64SessionId;
    }
}

module.exports = function (options) {
    if (!options || !options.hmacSecret || !options.secret || !options.store) {
        throw new Error('Invalid options arguments')
    }

    Session._store = options.store;
    Session.sessionCookieName = options.sessionCookieName || 'sessionId';

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
                console.log(' saved session')
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