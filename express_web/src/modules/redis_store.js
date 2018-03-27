const redis = require('redis');


module.exports = function (options) {
    options = options || {};
    options.host = options.host || '127.0.0.1';
    options.port = options.port || 6379;
    const client = redis.createClient(options);

    return {
        _temp_session_cache: {},
        save(sessionId, obj, callback) {
            //this._sessions[sessionId] = obj;
            const lastSessionObj = this._temp_session_cache[sessionId];

            let update = false || !lastSessionObj;

            if (!update) {

                if (Object.keys(lastSessionObj).length === Object.keys(obj).length) {
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
                } else {
                    update = true;
                }
            }

            if (update) {
                //Save to db
                this._temp_session_cache[sessionId] = undefined;
                try {
                    client.set(sessionId, JSON.stringify(obj), function (err, reply) {
                        if (err) return callback(err);
                        console.log('reply from redis: ')
                        console.dir(reply);
                        return callback(null);
                    });
                } catch (err) {
                    return callback(err);
                }
            }

        },
        clearAll(callback) {
            client.flushall(function (err, succeeded) {
                if (typeof callback === 'function') {
                    if (err) return callback(err);
                    return callback(null);
                }
            });
        },
        create(sessionId, ttl, callback) {
            const obj = {};
            this.renew(sessionId, obj, ttl, callback);
        },
        renew(sessionId, obj, ttl, callback) {
            const _this = this;
            try {
                client.set(sessionId, JSON.stringify(obj), function (err, reply) {
                    if (err) {
                        return callback(err);
                    }
                    client.expire(sessionId,ttl/1000,function(err){
                        if(err) return callback(err);
                        _this._temp_session_cache[sessionId] = obj;
                        return callback(null, obj);
                    })
                });
            } catch (err) {
                return callback(err, null);
            }
        },
        destroy(sessionId,callback){
            client.del(sessionId,function(err){
                if(err) return callback(err);
                return callback(null);
            })
        },
        get(sessionId, callback) {
            const _this = this;
            client.get(sessionId, function (err, reply) {
                if (err) return callback(err);

                try {
                    const obj = JSON.parse(reply)
                    _this._temp_session_cache[sessionId] = obj;
                    return callback(null, obj);
                } catch (err) {
                    return callback(err);
                }
            });
        }
    }
}