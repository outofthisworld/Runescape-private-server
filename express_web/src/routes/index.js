

const routes = [
    {routePath:'',router:{}}
]

module.exports = function(app){
    for(let obj of routes){
        app.use(obj.routePath,obj.router);
    }
}