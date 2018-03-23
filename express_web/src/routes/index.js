

const routes = [
    {routePath:'/store',router:require('./store')}
]

module.exports = function(app){
    for(let obj of routes){
        app.use(obj.routePath,obj.router);
    }
}