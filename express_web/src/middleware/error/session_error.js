/**
 * Catches an error occurring with user sessions
 * @param err
 * @param req
 * @param res
 * @param next
 */
module.exports = function(err,req,res,next){
    if(err && err.type == 'session'){
        res.status(500).send('Session error: ' + err.message);
    }else {
        next(err);
    }
}