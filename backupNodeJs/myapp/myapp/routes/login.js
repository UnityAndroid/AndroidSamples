var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var connection = require('../connect');
var jwt = require('jsonwebtoken');
var expressSession = require('express-session');

router.post('/', function (req, res) {

    var email = req.body.email_id;
    var pass = req.body.password;

    var qry = " SELECT * FROM tbl_party WHERE email_id like ? AND password like ? AND party_type = 0";

    var finalqry = mysql.format(qry, [email, pass]);
    connection.query(finalqry, function (err, ress) {
        var object1 = {}
        if (err) {

            object1['status'] = 0;
            object1['error_code'] = res.statusCode;
            object1['response'] = "login failed";
            object1['result'] = []

        } else {
            if (ress[0] != null) {
                const user = { id: ress[0].party_id };
                expressSession.token = ress[0].email_id;
                const token = jwt.sign({ user }, ress[0].email_id , {});


                object1['status'] = 1;
                object1['error_code'] = res.statusCode;
                object1['response'] = "login success";

                object1['result'] = []
                object1['result'].push(ress[0]);
                object1['result'][0]['token'] = token;

            } else {

                object1['status'] = 0;
                object1['error_code'] = res.statusCode;
                object1['response'] = "login failed";
                object1['result'] = []
            }
        }

        res.json(object1);
    });

});




module.exports = router;
