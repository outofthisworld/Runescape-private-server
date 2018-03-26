const path = require('path');
const express = require('express');
const exphbs  = require('express-handlebars');
const middleware = require('./src/middleware');
const routes = require('./src/routes');


const app = express();

app.set('port', (process.env.PORT || 5000));
app.set('views',path.join(__dirname,'views'));
app.engine('handlebars', exphbs());
app.set('view engine', 'handlebars');

middleware(app);

app.get('/',function(req,res){
    req.session.homeViews = req.session.homeViews || 0;
    req.session.homeViews++;
    console.log(req.session.homeViews);
    res.render('index');
});


routes(app);
app.listen(3000);