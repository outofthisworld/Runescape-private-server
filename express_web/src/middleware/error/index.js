

module.exports = function(app){
    app.use(require('./session_error'));
    app.use(require('./default_error'));
}