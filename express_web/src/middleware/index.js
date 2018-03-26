const express = require('express');

const path = require('path');
/*
    Obtain cookie parser module
 */
const cookieParser = require('cookie-parser');
/*
    Obtain body parser module
 */
const bodyParser = require('body-parser');

const session = require('./../modules/session');


/*
    Require express module
 */
module.exports = function(app){
    console.log(path.join(__dirname,'../','../','/public'))
    app.use(express.static(path.join(__dirname,'../','../','/public')));
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(cookieParser());
    app.use(session({
        secret:'cookie_secret',
        hmacSecret:'ilikepancakes',
        maxAge: 1000 * 60 * 90, // would expire after 15 minutes
        httpOnly: true, // The cookie only accessible by the web server
        //signed: true,// Indicates if the cookie should be signed
        store:{
            _sessions:{},
            _temp_session_cache:{},
            save(sessionId,obj,callback){
                //this._sessions[sessionId] = obj;
                const lastSessionObj =  this._temp_session_cache[sessionId];

                let update = false || !lastSessionObj;

                if(!update){

                    if(Object.keys(lastSessionObj).length === Object.keys(obj).length) {
                        for (key in obj) {
                            if (!(key in lastSessionObj)) {
                                update = true;
                                break;
                            }

                            if (obj[key] !== lastSessionObj[key]) {
                                update = true;
                                break;
                            }

                        }
                    }else{
                        update = true;
                    }
                }

                if(update){
                    //Save to db
                    this._sessions[sessionId] = obj;
                    callback(null);
                }

            },
            create(sessionId,callback){
                if(this._sessions[sessionId]){
                    return callback(new Error('duplicate session id'))
                }
                this._sessions[sessionId] = {};
                return callback(null,this._sessions[sessionId]);
            },
            get(sessionId,callback){
                let session = this._sessions[sessionId]


                if(!session){
                    return callback(new Error('Invalid id'));
                }

                session = this._sessions[sessionId]
                this._temp_session_cache[sessionId] = session;
                return callback(null,session);
            }
        }
    }));
}