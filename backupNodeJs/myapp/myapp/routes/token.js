var express = require('express');
var jwt = require('jsonwebtoken');
var expressSession = require('express-session');
const e = require('express');

function accessToken(req, res, next) {

    var token = "";
    if (req.method == "POST") {
        token = req.body.token;
    } else {
        token = req.query.token;
    }
    jwt.verify(token, expressSession.token, function (err, data) {
         
        if (err) {
            // var object1 = {}   
    
            // object1['status'] = 0;
            // object1['error_code'] = 401;
            // object1['response'] = "invalid auth token";
            // object1['result'] = []
 
            // res.json(object1);

            
            next();
        } else {
            next();
        }
    });
};

module.exports = { accessToken };