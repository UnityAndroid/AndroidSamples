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

    var qry = "SELECT * FROM `tbl_order`";
    var finalqry = mysql.format(qry);
    totRecords = await getData(finalqry);


    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_order` order by order_id desc limit ?,?";
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
    var product_id = req.body.product_id;
    var quantity = req.body.quantity;
    var party_id = req.body.party_id;
    var order_status = req.body.order_status;
    if (req.body.order_id) {

        var id = req.body.order_id;
        var qry = "UPDATE tbl_order SET product_id = ? , quantity = ? , party_id = ? , order_status = ? WHERE order_id = ?";
        var finalqry = mysql.format(qry, [product_id, quantity, party_id, order_status, id]);
   
    } else {

        var qry = "delete FROM `tbl_cart` where party_id = ?";
        var finalqry = mysql.format(qry,[ party_id ]);
        connection.query(finalqry, function (err, ress) {
            if (err) {
            } else {
            }
        })

        var qry = "SELECT * FROM `tbl_order`";
        var finalqry = mysql.format(qry);
        connection.query(finalqry, function (err, ress) {
            if (err) {
                totRecords = 0;
            } else {
                totRecords = ress.length;
            }
        })


        var qry = "INSERT INTO tbl_order(product_id,quantity,party_id,order_status) VALUES(?,?,?,?)";
        var finalqry = mysql.format(qry, [product_id, quantity, party_id, order_status]);
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
    var id = req.body.order_id;
    var qry = " DELETE FROM tbl_order WHERE order_id=?";
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