var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var connection = require('../connect');
var tokenn = require('../routes/token');
var multer = require('multer');

var jsonQuery = require('json-query')
const path = require('path');
// var storage = multer.diskStorage({
//     destination: function (req, file, cb) {
//         cb(null, './uploads/product');
//     },
//     filename: function (req, file, cb) {
//         cb(null, file.originalname);
//     }
// });

var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        if (file.fieldname === "product_image") {
            cb(null, './uploads/product/main');
        } else if (file.fieldname === "product_video") {
            cb(null, './uploads/product/video');
        } else if (file.fieldname === "other_images") {
            cb(null, './uploads/product/other');
        }
    },
    filename: function (req, file, cb) {
        var filename = file.originalname;
        filename = filename.replace(path.extname(file.originalname), "");
        cb(null, file.fieldname + "_" + filename + "_" + Date.now() + path.extname(file.originalname));
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

function getProductData(finalquery) {
    return new Promise((resolve, reject) => {
        connection.query(finalquery, (err, ress) => {
            return err ? reject(0) : resolve(ress);
        })
    })
}

router.get('/', tokenn.accessToken, async function (req, res) {

    var page = parseInt(req.query.page);
    var totalRecords = parseInt(req.query.noOfRecords);
    var totRecords = 0;

    var qry = "SELECT * FROM `tbl_product`";
    var finalqry = mysql.format(qry);
    totRecords = await getData(finalqry);


    var qry1 = "SELECT * FROM `tbl_product_imgs`";
    var finalqry1 = mysql.format(qry1);
    var results = await getProductData(finalqry1);

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_product` order by product_id desc limit ?,?";
    var finalqry = mysql.format(qry, [startindex, totalRecords]);

    connection.query(finalqry, async function (err, ress) {
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

                s = parse('[**][*product_id=%s]', ress[i]['product_id']);
                var result = jsonQuery(s, { data: results }).value
                ress[i]['images'] = result;

                object1['result']['data'].push(ress[i]);
            }
        }

        res.json(object1);
    });
})

function parse(str) {
    var args = [].slice.call(arguments, 1),
        i = 0;

    return str.replace(/%s/g, () => args[i++]);
}


router.get('/filterbysubcat', tokenn.accessToken, async function (req, res) {

    var page = parseInt(req.query.page);
    var totalRecords = parseInt(req.query.noOfRecords);
    var totRecords = 0;
    var subcatid = parseInt(req.query.sub_cat_id);

    var qry = "SELECT * FROM `tbl_product` where sub_cat_id = ?";
    var finalqry = mysql.format(qry, [subcatid]);
    totRecords = await getData(finalqry);

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_product` where sub_cat_id = ? order by product_id desc limit ?,?";
    var finalqry = mysql.format(qry, [subcatid, startindex, totalRecords]);

    connection.query(finalqry, async function (err, ress) {
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


router.get('/filterbymaincat', tokenn.accessToken, async function (req, res) {

    var page = parseInt(req.query.page);
    var totalRecords = parseInt(req.query.noOfRecords);
    var totRecords = 0;
    var maincatid = parseInt(req.query.main_cat_id);

    var qry = "SELECT * FROM `tbl_product` where sub_cat_id in ( select sub_cat_id from tbl_sub_cat where main_cat_id = ?)";
    var finalqry = mysql.format(qry, [maincatid]);
    totRecords = await getData(finalqry);

    if (page > 0) {
        var startindex = page * totalRecords;
    } else {
        var startindex = 0;
    }

    var qry = "SELECT * FROM `tbl_product` where sub_cat_id in ( select sub_cat_id from tbl_sub_cat where main_cat_id = ?) order by product_id desc limit ?,?";
    var finalqry = mysql.format(qry, [maincatid, startindex, totalRecords]);

    connection.query(finalqry, async function (err, ress) {
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

router.get('/filterby', tokenn.accessToken, async function (req, res) {

    var page = parseInt(req.query.page);
    var totalRecords = parseInt(req.query.noOfRecords);
    var totRecords = 0;

    if (req.query.sub_cat_id) {
        var subcatid = parseInt(req.query.sub_cat_id);
        var qry = "SELECT * FROM `tbl_product` where sub_cat_id = ?";
        var finalqry = mysql.format(qry, [subcatid]);
        totRecords = await getData(finalqry);

        if (page > 0) {
            var startindex = page * totalRecords;
        } else {
            var startindex = 0;
        }

        var qry = "SELECT tbl_product.*,tbl_metal_color.metal_color_name,tbl_rodium_color.rodium_color_name,tbl_tone.tone_name,tbl_purity.purity,tbl_shape.shape,tbl_brand.brand,tbl_collection.collection,tbl_pro_type.pro_type,tbl_main_cat.main_cat_name,tbl_sub_cat.sub_cat_name FROM `tbl_product` INNER JOIN tbl_metal_color ON tbl_product.metal_color_id = tbl_metal_color.metal_color_id INNER JOIN tbl_rodium_color ON tbl_product.rodium_color_id = tbl_rodium_color.rodium_color_id INNER JOIN tbl_sub_cat ON tbl_product.sub_cat_id = tbl_sub_cat.sub_cat_id INNER JOIN tbl_main_cat ON tbl_sub_cat.main_cat_id = tbl_main_cat.main_cat_id INNER JOIN tbl_tone ON tbl_product.tone_id = tbl_tone.tone_id INNER JOIN tbl_purity ON tbl_product.purity_id = tbl_purity.purity_id INNER JOIN tbl_shape ON tbl_product.shape_id = tbl_shape.shape_id INNER JOIN tbl_brand ON tbl_product.brand_id = tbl_brand.brand_id INNER JOIN tbl_collection ON tbl_product.collection_id = tbl_collection.collection_id INNER JOIN tbl_pro_type ON tbl_product.pro_type_id = tbl_pro_type.pro_type_id WHERE tbl_product.sub_cat_id = ? order by product_id desc limit ?,?";


        //   var qry = "SELECT * FROM `tbl_product` where sub_cat_id = ? order by product_id desc limit ?,?";
        var finalqry = mysql.format(qry, [subcatid, startindex, totalRecords]);
    } else if (!req.query.sub_cat_id && req.query.main_cat_id) {
        var maincatid = parseInt(req.query.main_cat_id);
        var qry = "SELECT * FROM `tbl_product` where sub_cat_id in ( select sub_cat_id from tbl_sub_cat where main_cat_id = ?)";
        var finalqry = mysql.format(qry, [maincatid]);
        totRecords = await getData(finalqry);

        if (page > 0) {
            var startindex = page * totalRecords;
        } else {
            var startindex = 0;
        }

        var qry = "SELECT tbl_product.*,tbl_metal_color.metal_color_name,tbl_rodium_color.rodium_color_name,tbl_tone.tone_name,tbl_purity.purity,tbl_shape.shape,tbl_brand.brand,tbl_collection.collection,tbl_pro_type.pro_type,tbl_main_cat.main_cat_name,tbl_sub_cat.sub_cat_name FROM `tbl_product` INNER JOIN tbl_metal_color ON tbl_product.metal_color_id = tbl_metal_color.metal_color_id INNER JOIN tbl_rodium_color ON tbl_product.rodium_color_id = tbl_rodium_color.rodium_color_id INNER JOIN tbl_sub_cat ON tbl_product.sub_cat_id = tbl_sub_cat.sub_cat_id INNER JOIN tbl_main_cat ON tbl_sub_cat.main_cat_id = tbl_main_cat.main_cat_id INNER JOIN tbl_tone ON tbl_product.tone_id = tbl_tone.tone_id INNER JOIN tbl_purity ON tbl_product.purity_id = tbl_purity.purity_id INNER JOIN tbl_shape ON tbl_product.shape_id = tbl_shape.shape_id INNER JOIN tbl_brand ON tbl_product.brand_id = tbl_brand.brand_id INNER JOIN tbl_collection ON tbl_product.collection_id = tbl_collection.collection_id INNER JOIN tbl_pro_type ON tbl_product.pro_type_id = tbl_pro_type.pro_type_id where tbl_product.sub_cat_id in ( select tbl_sub_cat.sub_cat_id from tbl_sub_cat where tbl_sub_cat.main_cat_id = ?) order by product_id desc limit ?,?";

        //  var qry = "SELECT * FROM `tbl_product` where sub_cat_id in ( select sub_cat_id from tbl_sub_cat where main_cat_id = ?) order by product_id desc limit ?,?";
        var finalqry = mysql.format(qry, [maincatid, startindex, totalRecords]);
    }
    connection.query(finalqry, async function (err, ress) {
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


router.get('/filterbymainsub', tokenn.accessToken, async function (req, res) {

    var page = parseInt(req.query.page);
    var totalRecords = parseInt(req.query.noOfRecords);
    var totRecords = 0;

    if (req.query.sub_cat_id) {
        var subcatid = req.query.sub_cat_id;
        globArr = [];
        var answ = subcatid.split(',');
        for (var p = 0; p < answ.length; p++) {
            globArr.push(parseInt(answ[p]));
        }
        var queryData = [globArr];
        var qry = "SELECT * FROM `tbl_product` where sub_cat_id in (?)";
        var finalqry = mysql.format(qry, queryData);
        totRecords = await getData(finalqry);

        if (page > 0) {
            var startindex = page * totalRecords;
        } else {
            var startindex = 0;
        }
        var queryData = [globArr, startindex, totRecords];

        var qry = "SELECT tbl_product.*,tbl_metal_color.metal_color_name,tbl_rodium_color.rodium_color_name,tbl_tone.tone_name,tbl_purity.purity,tbl_shape.shape,tbl_brand.brand,tbl_collection.collection,tbl_pro_type.pro_type,tbl_main_cat.main_cat_name,tbl_sub_cat.sub_cat_name FROM `tbl_product` INNER JOIN tbl_metal_color ON tbl_product.metal_color_id = tbl_metal_color.metal_color_id INNER JOIN tbl_rodium_color ON tbl_product.rodium_color_id = tbl_rodium_color.rodium_color_id INNER JOIN tbl_sub_cat ON tbl_product.sub_cat_id = tbl_sub_cat.sub_cat_id INNER JOIN tbl_main_cat ON tbl_sub_cat.main_cat_id = tbl_main_cat.main_cat_id INNER JOIN tbl_tone ON tbl_product.tone_id = tbl_tone.tone_id INNER JOIN tbl_purity ON tbl_product.purity_id = tbl_purity.purity_id INNER JOIN tbl_shape ON tbl_product.shape_id = tbl_shape.shape_id INNER JOIN tbl_brand ON tbl_product.brand_id = tbl_brand.brand_id INNER JOIN tbl_collection ON tbl_product.collection_id = tbl_collection.collection_id INNER JOIN tbl_pro_type ON tbl_product.pro_type_id = tbl_pro_type.pro_type_id WHERE tbl_product.sub_cat_id in (?) order by product_id desc limit ?,?";
        //   var qry = "SELECT * FROM `tbl_product` where sub_cat_id = ? order by product_id desc limit ?,?";
        var finalqry = mysql.format(qry, queryData);
    } else if (!req.query.sub_cat_id && req.query.main_cat_id) {
        var maincatid = req.query.main_cat_id;
        globArr = [];
        var answ = maincatid.split(',');
        for (var p = 0; p < answ.length; p++) {
            globArr.push(parseInt(answ[p]));
        }
        var queryData = [globArr];
        var qry = "SELECT * FROM `tbl_product` where sub_cat_id in ( select sub_cat_id from tbl_sub_cat where main_cat_id in (?))";
        var finalqry = mysql.format(qry, queryData);
        totRecords = await getData(finalqry);

        if (page > 0) {
            var startindex = page * totalRecords;
        } else {
            var startindex = 0;
        }
        var queryData = [globArr, startindex, totRecords];

        var qry = "SELECT tbl_product.*,tbl_metal_color.metal_color_name,tbl_rodium_color.rodium_color_name,tbl_tone.tone_name,tbl_purity.purity,tbl_shape.shape,tbl_brand.brand,tbl_collection.collection,tbl_pro_type.pro_type,tbl_main_cat.main_cat_name,tbl_sub_cat.sub_cat_name FROM `tbl_product` INNER JOIN tbl_metal_color ON tbl_product.metal_color_id = tbl_metal_color.metal_color_id INNER JOIN tbl_rodium_color ON tbl_product.rodium_color_id = tbl_rodium_color.rodium_color_id INNER JOIN tbl_sub_cat ON tbl_product.sub_cat_id = tbl_sub_cat.sub_cat_id INNER JOIN tbl_main_cat ON tbl_sub_cat.main_cat_id = tbl_main_cat.main_cat_id INNER JOIN tbl_tone ON tbl_product.tone_id = tbl_tone.tone_id INNER JOIN tbl_purity ON tbl_product.purity_id = tbl_purity.purity_id INNER JOIN tbl_shape ON tbl_product.shape_id = tbl_shape.shape_id INNER JOIN tbl_brand ON tbl_product.brand_id = tbl_brand.brand_id INNER JOIN tbl_collection ON tbl_product.collection_id = tbl_collection.collection_id INNER JOIN tbl_pro_type ON tbl_product.pro_type_id = tbl_pro_type.pro_type_id where tbl_product.sub_cat_id in ( select tbl_sub_cat.sub_cat_id from tbl_sub_cat where tbl_sub_cat.main_cat_id in (?)) order by product_id desc limit ?,?";

        //  var qry = "SELECT * FROM `tbl_product` where sub_cat_id in ( select sub_cat_id from tbl_sub_cat where main_cat_id = ?) order by product_id desc limit ?,?";
        var finalqry = mysql.format(qry, queryData);
    }
    connection.query(finalqry, async function (err, ress) {
        var object1 = {}

        if (err) {
            console.log(err);
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


router.get('/productdetails', tokenn.accessToken, async function (req, res) {


    var object1 = {}
    var query = "select * from tbl_metal_color;select * from tbl_rodium_color;select * from tbl_tone;select * from tbl_purity;select * from tbl_shape;select * from tbl_brand;select * from tbl_collection;select * from tbl_pro_type;"
    connection.query(query, [1, 2, 3, 4, 5, 6, 7, 8], function (error, results) {
        if (error) {
            object1['status'] = 0;
            object1['error_code'] = res.statusCode;
            object1['response'] = "Records Fetched Failed";
            object1['data'] = {};
        } else {
            object1['status'] = 1;
            object1['error_code'] = res.statusCode;
            object1['response'] = "Records Fetched Successfully";
            object1['data'] = {};
            object1['data']['metal_color'] = results[0];
            object1['data']['rodium_color'] = results[1];
            object1['data']['tone'] = results[2];
            object1['data']['purity'] = results[3];
            object1['data']['shape'] = results[4];
            object1['data']['brand'] = results[5];
            object1['data']['collection'] = results[6];
            object1['data']['pro_type'] = results[7];
        }
        res.json(object1);
    })

})




function insertImage(finalquery) {
    return new Promise((resolve, reject) => {
        connection.query(finalquery, (err, ress) => {
            return err ? reject(0) : resolve(1);
        })
    })
}

function getProductId(finalquery) {
    return new Promise((resolve, reject) => {
        connection.query(finalquery, (err, ress) => {
            return err ? reject(0) : resolve(ress[0].product_id);
        })
    })
}


router.post('/saveorupdate', upload.fields([
    {
        name: 'product_image',
        maxCount: 1
    },
    {
        name: 'product_video', maxCount: 1
    },
    {
        name: 'other_images', maxCount: 10
    }
]), tokenn.accessToken, async function (req, res) {

    var sub_cat_id = req.body.sub_cat_id;
    var design_number = req.body.design_number;
    var quantity = req.body.quantity;
    var size = req.body.size;
    var size_info = req.body.size_info;
    var weight = req.body.weight;
    var gross_weight = req.body.gross_weight;
    var less_weight = req.body.less_weight;
    var net_weight = req.body.net_weight;
    var metal_color_id = req.body.metal_color_id;
    var rodium_color_id = req.body.rodium_color_id;
    var tone_id = req.body.tone_id;
    var purity_id = req.body.purity_id;
    var shape_id = req.body.shape_id;
    var brand_id = req.body.brand_id;
    var remarks = req.body.remarks;
    var collection_id = req.body.collection_id;
    var pro_type_id = req.body.pro_type_id;
    var price = req.body.price;
    var mrp = req.body.mrp;
    var group_id = req.body.group_id;
    var file_path = req.files.product_image[0].path;
    var video_path = req.files.product_video[0].path;

    if (req.body.product_id) {
        var id = req.body.product_id;
        var qry = "UPDATE tbl_product SET sub_cat_id = ? , design_number = ? , quantity = ? , size = ? , size_info = ? , weight = ? , gross_weight = ? , less_weight = ? , net_weight = ? , metal_color_id = ? , rodium_color_id = ? , tone_id = ? , purity_id = ? , shape_id = ? , brand_id = ? , remarks = ? , collection_id = ? , pro_type_id = ? , price = ? , mrp = ? , product_image = ? , product_video = ? , group_id = ? WHERE product_id  = ?";
        var finalqry = mysql.format(qry, [sub_cat_id, design_number, quantity, size, size_info, weight, gross_weight, less_weight, net_weight, metal_color_id, rodium_color_id, tone_id, purity_id, shape_id, brand_id, remarks, collection_id, pro_type_id, price, mrp, file_path, video_path, group_id, id]);
    } else {
        var qry = "INSERT INTO tbl_product(sub_cat_id,design_number,quantity,size,size_info,weight,gross_weight,less_weight,net_weight,metal_color_id,rodium_color_id,tone_id,purity_id,shape_id,brand_id,remarks,collection_id,pro_type_id,price,mrp,product_image,product_video,group_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        var finalqry = mysql.format(qry, [sub_cat_id, design_number, quantity, size, size_info, weight, gross_weight, less_weight, net_weight, metal_color_id, rodium_color_id, tone_id, purity_id, shape_id, brand_id, remarks, collection_id, pro_type_id, price, mrp, file_path, video_path, group_id]);
    }

    connection.query(finalqry, async function (err, ress) {
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

            var proid = 0;
            var qry = "SELECT * FROM `tbl_product` order by product_id desc limit 1";
            var finalqry = mysql.format(qry, []);
            proid = await getProductId(finalqry);

            for (var k = 0; k < req.files.other_images.length; k++) {
                var tot = 0;
                var qry = "INSERT INTO tbl_product_imgs(product_id,product_image) VALUES(?,?)";
                var finalqry = mysql.format(qry, [proid, req.files.other_images[k].path]);
                tot = await insertImage(finalqry);
            }
        }
        res.json(object1);
    });
})


router.post('/delete', tokenn.accessToken, function (req, res) {
    var id = req.body.product_id;
    var qry = " DELETE FROM tbl_product WHERE product_id=?";
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
