const path = require('path');
const express = require('express');
const middleware = require('./src/middleware');
const routes = require('./src/routes');

const app = express();

app.set('views', path.join(__dirname, 'public','html'));
app.set('view engine', 'pug');

middleware(app);
app.use('/',function(req,res){
    res.render(path.join(app.settings.views,'index.html'));
})
routes(app);

app.listen(80);