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

    var qry = "SELECT * FROM `tbl_owner`";
    var finalqry = mysql.format(qry);
    totRecords = await getData(finalqry);

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }
    
    var qry = "SELECT * FROM `tbl_owner` order by owner_id desc limit ?,?";
    var finalqry = mysql.format(qry, [startindex, totalRecords]);

    connection.query(finalqry,async function (err, ress) {
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
    var name = req.body.owner_name;
    var mobile_number = req.body.mobile_number;
    var birth_date = req.body.birth_date;
    var blood_group = req.body.blood_group;
    var anniversary_date = req.body.anniversary_date;
    if (req.body.owner_id) {
        var id = req.body.owner_id;
        var qry = "UPDATE tbl_owner SET owner_name = ? , mobile_number = ? , birth_date = ? , blood_group = ? , anniversary_date = ? WHERE owner_id = ?";
        var finalqry = mysql.format(qry, [name,mobile_number,birth_date,blood_group,anniversary_date,id]);
    } else {
        var qry = "INSERT INTO tbl_owner(owner_name,mobile_number,birth_date,blood_group,anniversary_date) VALUES(?,?,?,?,?)";
        var finalqry = mysql.format(qry, [name,mobile_number,birth_date,blood_group,anniversary_date]);
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
    var id = req.body.owner_id;
    var qry = " DELETE FROM tbl_owner WHERE owner_id=?";
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