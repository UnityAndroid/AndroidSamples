var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cors = require('cors');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var loginRouter = require('./routes/login');
var indexRouter = require('./routes/index');
var mainRouter = require('./routes/maincat');
var subRouter = require('./routes/subcat');
var bannerRouter = require('./routes/banner');
var colorsRouter = require('./routes/colors');
var metalRouter = require('./routes/metalcolor');
var rodiumRouter = require('./routes/rodiumcolor');
var toneRouter = require('./routes/tone');
var purityRouter = require('./routes/purity');
var shapeRouter = require('./routes/shape');
var brandRouter = require('./routes/brand');
var collectionRouter = require('./routes/collection');
var protypeRouter = require('./routes/protype');
var productRouter = require('./routes/product');

var ownerRouter = require('./routes/owner');
var partyRouter = require('./routes/party');
var orderRouter = require('./routes/order');
var cartRouter = require('./routes/cart');


var panelRouter = require('./routes/panel');

var expressSession = require('express-session');
var app = express();

app.use(cors());
// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');


app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.disable('etag');
app.use('/', indexRouter);
app.use('/login', loginRouter);
app.use('/maincat', mainRouter);
app.use('/subcat', subRouter);
app.use('/banner', bannerRouter);
app.use('/colors', colorsRouter);
app.use('/metalcolor', metalRouter);
app.use('/rodiumcolor', rodiumRouter);
app.use('/tone', toneRouter);
app.use('/purity', purityRouter);
app.use('/shape', shapeRouter);
app.use('/brand', brandRouter);
app.use('/collection', collectionRouter);
app.use('/protype', protypeRouter);
app.use('/product', productRouter);


app.use('/owner', ownerRouter);
app.use('/party', partyRouter);
app.use('/order', orderRouter);
app.use('/cart', cartRouter);


app.use('/panel', panelRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});


// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development

  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  //res.render('error');

  var object1 = {}
  object1['errorResult'] = {
    status: 1,
    response: err.message,
    error_code: err.statusCode
  };

  res.json(object1);
});

app.use(expressSession({
  secret: "stone12",
  token: "token",
  resave: true,
  saveUninitialized: true
}));


module.exports = app;
