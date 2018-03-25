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

    options = Object.create(options);

    return function(req,res,next){
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
                            req.sessionId = session_id_parts[0];
                            req.session = (options.store && options.store[req.sessionId]) || {};
                            return next();
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
        req.sessionId = sessionIdEncoded;
        options.store[sessionId] = {}
        req.session = options.store[sessionId];
        next();
    }
}