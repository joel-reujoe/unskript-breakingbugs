var express=require('express')
var admin=require('firebase-admin')
var url = require('url');
var path = require('path');
var sql = require('mysql');
var dbservice=require('./dependencies/db')
var bodyParser = require('body-parser');
var router=require('./routers/project_router')


const app=express()
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

app.use(async(req, res, next) =>{
    try{
        // var service = await new dbservice();
        // req.connection =await service.connectdb();
        // req.connection =myconn1;
        var fullUrl = req.protocol + '://' + req.get('host') + req.originalUrl;
        console.log("called API is: "+fullUrl);
        console.log("ReqBody Is:"+JSON.stringify(req.body));
        next();
      } catch(error) {//here goes if someAsyncPromise() rejected}
        next(error) //this will result in a resolved promise.
      };
});


app.use('/controllers/ctrl-project',router)

app.get('*',(req,res,next)=>{
    // console.log("We couldn't find anything you are looking for")
    res.send("We couldn't find anything you are looking for")
})
app.listen(9000,()=>{
    console.log("Server Active At 8000");  
})
