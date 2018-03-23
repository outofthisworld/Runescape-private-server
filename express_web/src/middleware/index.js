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


/*
    Require express module
 */
module.exports = function(app){
    console.log(path.join(__dirname,'../','../','/public'))
    app.use(express.static(path.join(__dirname,'../','../','/public')));
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(cookieParser());
}