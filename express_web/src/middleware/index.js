const express = require('express');
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
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(cookieParser());
    app.use(express.static(path.join(__dirname, 'public')));
}