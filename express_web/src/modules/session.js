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


class Session{
    constructor(options){
        this._priv = {};
        this._priv._sessionObj = options.sessionObj;
        this._priv._req = options.req;
        this._priv._res = options.res;
        this._priv._options = options.options;
    }

    init(callback){

        const sessionId = this._generateId();
        const _this = this;
        //Create a new session
        Session._store.create(sessionId,function(err,createdSession) {
            if(err) {
                _this._priv._res.clearCookie(Session.sessionCookieName);
                return callback(err,createdSession);
            }else {
                _this._priv._res.cookie(Session.sessionCookieName, sessionId, _this._priv._options);
                _this._priv._req.sessionId = sessionId;
                Object.assign(_this, createdSession);
                _this._priv._req.session = _this;
                return callback(null);
            }
        });
    }

    load(callback){
        const _this = this;
        try {
            this._verify();
            Session._store.get(_this._priv._req.cookies[Session.sessionCookieName], function (err, session) {
                if (err) return callback(err);

                console.dir(session);
                Object.assign(_this, session);
                _this._priv._req.sessionId = _this._priv._req.cookies[Session.sessionCookieName];
                _this._priv._req.session = _this;
                return callback(null);
            })
        }catch(err){
            return callback(err);
        }
    }

    destroy(callback){
        const _this = this;
       //destroy session obj
        Session._store.destroy(this._priv._req.cookies[Session.sessionCookieName],function(err){
            if(err){
                return callback(err);
            }else{
                delete _this._priv._req.sessionId;
                delete _this._priv._req.session;
                res.clearCookie(Session.sessionCookieName);
            }
        })
    }

    save(callback){
        const _this = this;
        const priv = this._priv;
        delete this._priv;

        if(priv._requiresNewCookie){
            const newSessionId = _this._generateId();
            Session._store.save(newSessionId,_this,function(err){
                _this._priv = priv;
                if(err){
                    return callback(err);
                }else{
                    Session._store.destroy(priv._req.cookies[Session.sessionCookieName],function(err){})
                    res.clearCookie(Session.sessionCookieName);
                    res.cookie(Session.sessionCookieName,newSessionId, _this._priv._options);
                    priv__requiresNewCookie = false;
                    return callback(null);
                }
            })

        }else {
            console.log('saving session')
            Session._store.save(priv._req.cookies[Session.sessionCookieName], _this, function (err) {
                _this._priv = priv;
                if (err) {
                    return callback(err);
                }else{
                    return callback(null);
                }
            })
        }
    }

    addHeader(key,val,callback){
        this._priv._sessionObj[key] = val;
        this._priv._requiresNewCookie = true;

    }

    removeHeader(key){
        delete this._priv._sessionObj[key];
        this._priv._requiresNewCookie = true;
    }

    _verify(){
        const sessId = this._priv._req.cookies[Session.sessionCookieName];
        const session_id_parts = sessId.split(':');
        if(session_id_parts.length == 2){
            const sessionIdJson = Buffer.from(session_id_parts[0], 'base64').toString('ascii');
            const hmac = session_id_parts[1];
            const reHmac = crypto.createHmac('sha1', this._priv._options.hmacSecret).update(sessionIdJson).digest('hex');

            //Verify hmac
            if(reHmac == hmac){
                const sessionIdObj = JSON.parse(sessionIdJson);
                this._priv._sessionObj = sessionIdObj;
                const {sessionIdHeader,sessionIdRandomPart} = sessionIdObj;

                if(sessionIdHeader && sessionIdRandomPart != undefined){
                    const userIp = sessionIdHeader.ip;
                    const userAgent = sessionIdHeader.user_agent;

                    if(userIp == this._priv._req.ip && (this._priv._req.headers['user-agent'] || '') == userAgent){
                        return true;
                    }else{
                        throw new Error('Session key validation error');
                    }
                }
            }
        }
    }

    _generateId(){
        const sessionId = this._priv._sessionObj;

        const sessionIdJson = JSON.stringify(sessionId);
        console.log(this._priv.options)
        const sessionIdMac =  crypto.createHmac('sha1', this._priv._options.hmacSecret).update(sessionIdJson).digest('hex');
        const sessionIdEncoded = Buffer.from(sessionIdJson).toString('base64');
        const base64SessionId = `${sessionIdEncoded}:${sessionIdMac}`;

        return base64SessionId;
    }
}

module.exports = function(options){
    if(!options || !options.hmacSecret || !options.secret || !options.store){
        throw new Error('Invalid options arguments')
    }

    Session._store = options.store;
    Session.sessionCookieName = options.sessionCookieName || 'sessionId';

    return function(req,res,next){
        const err = new Error();
        err.type = 'session';

        if(req.cookies[Session.sessionCookieName]){

            const session = new Session({
                req,
                res,
                options
            });

            session.load(function(err){
                if(err){
                    return next(err);
                }
                req.on('end', function () {
                    session.save(function (err) {
                        if (err) {

                        }
                    })
                });
                return next();
            });


        }else {
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
                req,
                res,
                options
            });

            session.init(function (err) {
                if (err) {
                    return next(err);
                }

                req.on('end', function () {
                    session.save(function (err) {
                        if (err) {
                        }

                    })
                });
                return next();
            });
        }
    }
}