var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var async = require('async');
var jsonQuery = require('json-query')
var connection = require('../connect');
var tokenn = require('../routes/token')

var jwt = require('jsonwebtoken');
var expressSession = require('express-session');


function getData(finalquery) {
    return new Promise((resolve, reject) => {
        connection.query(finalquery, (err, ress) => {
            return err ? reject(0) : resolve(ress.length);
        })
    })
}

router.get('/mainsubcat', tokenn.accessToken, function (req, res) {

    var subcat = [];
    var sqry = "SELECT * FROM `tbl_sub_cat`order by sub_cat_id desc";
    var sfinalqry = mysql.format(sqry);


    connection.query(sfinalqry, function (err, ress) {
        if (err) {
        } else {
            for (var i = 0; i < ress.length; i++) {
                subcat.push(ress[i]);
            }

            var qry = "SELECT * FROM `tbl_main_cat` order by main_cat_id desc";
            var finalqry = mysql.format(qry);
            connection.query(finalqry, function (err, ress) {

                var object1 = {}
                if (err) {

                    object1['status'] = 0;
                    object1['error_code'] = res.statusCode;
                    object1['response'] = "Records Fetched Failed";
                    object1['result'] = []
                } else {

                    object1['status'] = 1;
                    object1['error_code'] = res.statusCode;
                    object1['response'] = "Records Fetched Successfully";

                    object1['result'] = [];

                    for (var i = 0; i < ress.length; i++) {

                        object = {
                            "main_cat_id": ress[i]['main_cat_id'],
                            "main_cat_name": ress[i]['main_cat_name'],
                            "main_cat_img": ress[i]['main_cat_img'],
                            "sub_cat_data": []
                        };

                        s = parse('[**][*main_cat_id=%s]', ress[i]['main_cat_id']);
                        var result = jsonQuery(s, { data: subcat }).value

                        object2 = {
                            "sub_cat_id": 0,
                            "main_cat_id": ress[i]['main_cat_id'],
                            "sub_cat_name": "View All Designs",
                        };

                        object["sub_cat_data"].push(object2);
                        if (result.length > 0) {
                            for (var k = 0; k < result.length; k++) {
                                object["sub_cat_data"].push(result[k]);
                            }
                        }

                        object1['result'].push(object);
                    }
                    res.json(object1);
                }
            });

        }
    });

})

function parse(str) {
    var args = [].slice.call(arguments, 1),
        i = 0;

    return str.replace(/%s/g, () => args[i++]);
}

router.get('/allproductcategories', tokenn.accessToken, function (req, res) {

    var subcat = [];
    var sqry = "SELECT * FROM `tbl_sub_cat`order by sub_cat_id desc";
    var sfinalqry = mysql.format(sqry);


    connection.query(sfinalqry, function (err, ress) {
        if (err) {
        } else {
            for (var i = 0; i < ress.length; i++) {
                subcat.push(ress[i]);
            }

            var qry = "SELECT * FROM `tbl_main_cat` order by main_cat_id desc";
            var finalqry = mysql.format(qry);
            connection.query(finalqry, function (err, ress) {

                var object1 = {}
                if (err) {

                    object1['status'] = 0;
                    object1['error_code'] = res.statusCode;
                    object1['response'] = "Records Fetched Failed";
                    object1['result'] = []
                } else {

                    object1['status'] = 1;
                    object1['error_code'] = res.statusCode;
                    object1['response'] = "Records Fetched Successfully";

                    object1['result'] = [];

                    for (var i = 0; i < ress.length; i++) {

                        object = {
                            "main_cat_id": ress[i]['main_cat_id'],
                            "main_cat_name": ress[i]['main_cat_name'],
                            "main_cat_img": ress[i]['main_cat_img'],
                            "sub_cat_data": []
                        };

                        s = parse('[**][*main_cat_id=%s]', ress[i]['main_cat_id']);
                        var result = jsonQuery(s, { data: subcat }).value

                        if (result.length > 0) {
                            for (var k = 0; k < result.length; k++) {
                                object["sub_cat_data"].push(result[k]);
                            }
                        }

                        object1['result'].push(object);
                    }
                    res.json(object1);
                }
            });

        }
    });

})

function parse(str) {
    var args = [].slice.call(arguments, 1),
        i = 0;

    return str.replace(/%s/g, () => args[i++]);
}




router.post('/login', function (req, res) {

    var email = req.body.email_id;
    var pass = req.body.password;

    var qry = " SELECT * FROM tbl_party WHERE email_id like ? AND password like ? AND party_type = 1";

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
                var data = {
                    expireat: 0,
                    token: token,
                };
                object1['result'].push(data);

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