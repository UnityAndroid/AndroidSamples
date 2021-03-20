var express = require('express');
var router = express.Router();
var mysql = require('mysql');
const path = require('path');

var connection = require('../connect');
var tokenn = require('../routes/token');
var multer = require('multer');
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        if (file.fieldname === "party_logo") {
            cb(null, './uploads/party/logo');
        } else if (file.fieldname === "card_front") {
            cb(null, './uploads/party/front');
        } else if (file.fieldname === "card_back") {
            cb(null, './uploads/party/back');
        }
    },
    filename: function (req, file, cb) {
        cb(null, file.fieldname + "_" + Date.now() + path.extname(file.originalname));
    }
});

var upload = multer({ storage: storage })


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

    var qry = "SELECT * FROM `tbl_party`";
    var finalqry = mysql.format(qry);
    totRecords = await getData(finalqry);

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_party` order by party_id desc limit ?,?";
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


router.post('/saveorupdate', upload.fields([
    {
        name: 'party_logo',
        maxCount: 1
    },
    {
        name: 'card_front', maxCount: 1
    },
    {
        name: 'card_back', maxCount: 1
    }
]), function (req, res) {

    var party_code = req.body.party_code;
    var party_name = req.body.party_name;
    var owner_id = req.body.owner_id;
    var party_address = req.body.party_address;
    var pincode = req.body.pincode;
    var area = req.body.area;
    var city = req.body.city;
    var state = req.body.state;
    var reference_by = req.body.reference_by;
    var email_id = req.body.email_id;
    var pan_no = req.body.pan_no;
    var gst_no = req.body.gst_no;

    var mobile_number = req.body.mobile_number;
    var party_type = req.body.party_type;

    var password = req.body.password;

    var party_logo = req.files.party_logo[0].path;
    var card_front = req.files.card_front[0].path;
    var card_back = req.files.card_back[0].path;

    if (req.body.party_id) {
        var id = req.body.party_id;

        var qry = "UPDATE tbl_party SET party_code = ? , party_name = ? , owner_id = ? , party_address = ? , pincode = ? , area = ? , city = ? , state = ? , reference_by = ? , mobile_number = ? , email_id = ? , pan_no = ? , gst_no = ? , party_logo = ? , card_front = ? , card_back = ? , party_type = ? , password = ? WHERE party_id  = ?";
        var finalqry = mysql.format(qry, [party_code, party_name, owner_id, party_address, pincode, area, city, state, reference_by, mobile_number, email_id, pan_no, gst_no, party_logo, card_front, card_back, party_type, password, id]);

    } else {

        var qry = "INSERT INTO tbl_party(party_code,party_name,owner_id,party_address,pincode,area,city,state,reference_by,mobile_number,email_id,pan_no,gst_no,party_logo,card_front,card_back,party_type,password) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        var finalqry = mysql.format(qry, [party_code, party_name, owner_id, party_address, pincode, area, city, state, reference_by, mobile_number, email_id, pan_no, gst_no, party_logo, card_front, card_back, party_type, password]);
        console.log(finalqry);
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
    var id = req.body.party_id;
    var qry = " DELETE FROM tbl_party WHERE party_id=?";
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
