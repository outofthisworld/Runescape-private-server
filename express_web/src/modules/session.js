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


module.exports = function(options){

    if(!options || !options.hmacSecret || !options.secret || !options.store){
        throw new Error('Invalid options arguments')
    }


    return function(req,res,next){
        const err = new Error();
        err.type = 'session';

        if(req.cookies.sessionId){
            const sessId = req.cookies.sessionId;
            const session_id_parts = sessId.split(':');
            if(session_id_parts.length == 2){
                const sessionIdJson = Buffer.from(session_id_parts[0], 'base64').toString('ascii');
                const hmac = session_id_parts[1];
                const reHmac = crypto.createHmac('sha1', options.hmacSecret).update(sessionIdJson).digest('hex');

                //Verify hmac
                if(reHmac == hmac){
                    console.log('hmac verified')
                    const sessionIdObj = JSON.parse(sessionIdJson);
                    const {sessionIdHeader,sessionIdRandomPart} = sessionIdObj;

                    if(sessionIdHeader && sessionIdRandomPart != undefined){
                        const userIp = sessionIdHeader.ip;
                        const userAgent = sessionIdHeader.user_agent;

                        if(userIp == req.ip && (req.headers['user-agent'] || '') == userAgent){
                            console.log('Setting session')
                            console.log(req.session)

                            try {
                                req.session = options.store.get(session_id_parts[0]);
                                req.sessionId = session_id_parts[0];
                                req.on('end',function(){
                                    try {
                                        options.store.put(req.sessionId, req.session);
                                    }catch(error){
                                        err.message = 'Failed to store session object using store.put() session id:' + session_id_parts[0];
                                        err.method = 'put';
                                        options.onerror(err,session_id_parts[0],req,res);
                                    }
                                })
                                return next();
                            }catch(error){
                                //Error retrieving session send server error
                                err.message = 'Failed to retrieve session using store.get() for session id: ' + session_id_parts[0];
                                err.method = 'get';
                                options.onerror(err,session_id_parts[0],req,res);

                            }
                        }
                    }
                }
            }
        }

        console.log('reassigning session')
        res.clearCookie('sessionId');
        const sessionId = {
            sessionIdHeader:{
                ip:req.ip,
                user_agent:req.headers['user-agent'] || '',
                time_issued:new Date().getTime()
            },
            sessionIdRandomPart:require('uuid').v4()
        }

        const sessionIdJson = JSON.stringify(sessionId);
        const sessionIdMac =  crypto.createHmac('sha1', options.hmacSecret).update(sessionIdJson).digest('hex');
        const sessionIdEncoded = Buffer.from(sessionIdJson).toString('base64');
        const base64SessionId = `${sessionIdEncoded}:${sessionIdMac}`;

        //Generate random id
        res.cookie('sessionId',base64SessionId,options);

        if(options.store.create(sessionIdEncoded)){
            try {
                req.session = options.store.get(sessionIdEncoded);
                req.sessionId = sessionIdEncoded;
                next();
            }catch(error){
                res.clearCookie('sessionId');
                err.method = 'get';
                err.message = 'Failed to get session session id:' + sessionIdEncoded;
                options.onerror(err,sessionIdEncoded,req,res);
            }
        }else{
             res.clearCookie('sessionId');
             err.method = 'create';
             err.message = 'Failed to create session session id:' + sessionIdEncoded;
             options.onerror(err,sessionIdEncoded,req,res);
        }
    }
}