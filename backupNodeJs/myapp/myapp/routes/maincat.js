var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var connection = require('../connect');
var tokenn = require('../routes/token');
var multer = require('multer');
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './uploads/main');
    },
    filename: function (req, file, cb) {
        cb(null, file.originalname);
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

    var qry = "SELECT * FROM `tbl_main_cat`";
    var finalqry = mysql.format(qry);
    totRecords = await getData(finalqry);

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_main_cat` order by main_cat_id desc limit ?,?";
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



router.post('/saveorupdate', upload.single("main_cat_img"), tokenn.accessToken, function (req, res) {

    var name = req.body.main_cat_name;
    if (req.file)
        var file_path = req.file.path;

    if (req.body.main_cat_id) {
        var id = req.body.main_cat_id;
        if (req.file) {
            var qry = "UPDATE tbl_main_cat SET main_cat_name = ? , main_cat_img = ? WHERE main_cat_id  = ?";
            var finalqry = mysql.format(qry, [name, file_path, id]);
        } else {
            var qry = "UPDATE tbl_main_cat SET main_cat_name = ? WHERE main_cat_id  = ?";
            var finalqry = mysql.format(qry, [name, id]);
        }
    } else {

        var qry = "INSERT INTO tbl_main_cat(main_cat_name,main_cat_img) VALUES(?,?)";
        var finalqry = mysql.format(qry, [name, file_path]);
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
    var id = req.body.main_cat_id;
    var qry = " DELETE FROM tbl_main_cat WHERE main_cat_id=?";
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
