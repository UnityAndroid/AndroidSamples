var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var connection = require('../connect');
var tokenn = require('../routes/token')

function getData(finalquery) {
    return new Promise((resolve, reject) => {
        connection.query(finalquery, (err, ress) => {
            return err ? reject(0) : resolve(ress.length);
        })
    })
}

router.get('/', tokenn.accessToken,async function (req, res) {

    var page = parseInt(req.query.page);
    var totalRecords = parseInt(req.query.noOfRecords);
    var totRecords = 0;

    var qry = "SELECT * FROM `tbl_sub_cat`";
    var finalqry = mysql.format(qry);
    totRecords = await getData(finalqry);

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_sub_cat` order by sub_cat_id desc limit ?,?";
    var finalqry = mysql.format(qry, [startindex, totalRecords]);

    connection.query(finalqry,async function (err, ress) {
        var object1 = {}

        if (err) {
            object1['status'] = 0;
            object1['error_code'] = res.statusCode;
            object1['response'] = "Records Fetched Failed";
            object1['result'] = [];
 
        } else {
            object1['status'] = 1;
            object1['error_code'] = res.statusCode;
            object1['response'] = "Records Fetched Successfully";


            object1['result'] = {
                "totalRecords": totRecords,
                "pageNumber": page,
                "noOfRecords": ress.length,
                "data": []
            };

            for (var i = 0; i < ress.length; i++) {
                object1['result']['data'].push(ress[i]);
            }
        }

        res.json(object1);
    });
})


router.get('/filterbymaincat', tokenn.accessToken, function (req, res) {

    var page = parseInt(req.query.page);
    var totalRecords = parseInt(req.query.noOfRecords);
    var totRecords = 0;
    var maincatid = parseInt(req.query.main_cat_id);

    var qry = "SELECT * FROM `tbl_sub_cat`  where main_cat_id = ?";
    var finalqry = mysql.format(qry,[maincatid]);
    connection.query(finalqry, function (err, ress) {
        if (err) {
            totRecords = 0;
        } else {
            totRecords = ress.length;
        }
    })

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_sub_cat` where main_cat_id = ? order by sub_cat_id desc limit ?,?";
    var finalqry = mysql.format(qry, [maincatid, startindex, totalRecords]);

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


            object1['result'] = {
                "totalRecords": totRecords,
                "pageNumber": page,
                "noOfRecords": ress.length,
                "data": []
            };

            for (var i = 0; i < ress.length; i++) {
                object1['result']['data'].push(ress[i]);
            }
        }

        res.json(object1);
    });
})


router.post('/saveorupdate', tokenn.accessToken, function (req, res) {
    var catid = req.body.main_cat_id;
    var name = req.body.sub_cat_name;
    if (req.body.sub_cat_id) {
        var id = req.body.sub_cat_id;
        var qry = "UPDATE tbl_sub_cat SET sub_cat_name = ? , main_cat_id = ? WHERE sub_cat_id = ?";
        var finalqry = mysql.format(qry, [name, catid, id]);
    } else {
        var qry = "INSERT INTO tbl_sub_cat(main_cat_id,sub_cat_name) VALUES(?,?)";
        var finalqry = mysql.format(qry, [catid, name]);
    }
    connection.query(finalqry, function (err, ress) {
        var object1 = {}
        if (err) {
            object1['status'] = 0;
            object1['error_code'] = res.statusCode;
            object1['response'] = "saveorUpdate Failed";
        }
        else {
            object1['status'] = 1;
            object1['error_code'] = res.statusCode;
            object1['response'] = "saveorUpdate Successfully";   
        }
        res.json(object1);
    });
})



router.post('/delete', tokenn.accessToken, function (req, res) {
    var id = req.body.sub_cat_id;
    var qry = " DELETE FROM tbl_sub_cat WHERE sub_cat_id=?";
    var finalqry = mysql.format(qry, [id]);

    connection.query(finalqry, function (err, ress) {
        var object1 = {}
        if (err) {
            object1['status'] = 0;
            object1['error_code'] = res.statusCode;
            object1['response'] = "delete Failed";
            
        }
        else {
            object1['status'] = 1;
            object1['error_code'] = res.statusCode;
            object1['response'] = "delete Successfully";   
        }
        res.json(object1);
    });

})


module.exports = router;