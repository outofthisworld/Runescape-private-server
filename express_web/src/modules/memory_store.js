module.exports = {
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