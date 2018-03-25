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

const session = require('./../modules/session');


/*
    Require express module
 */
module.exports = function(app){
    console.log(path.join(__dirname,'../','../','/public'))
    app.use(express.static(path.join(__dirname,'../','../','/public')));
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(cookieParser());
    app.use(session({
        secret:'cookie_secret',
        hmacSecret:'ilikepancakes',
        maxAge: 1000 * 60 * 90, // would expire after 15 minutes
        httpOnly: true, // The cookie only accessible by the web server
        signed: true // Indicates if the cookie should be signed
    }));
}