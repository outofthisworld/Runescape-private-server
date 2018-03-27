/**
 * Catch all error case.
 * @param err
 * @param req
 * @param res
 * @param next
 */
module.exports = function(err,req,res,next){
    return res.status(500).send('Default error: ' + err.message);
}