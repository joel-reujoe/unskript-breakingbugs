var MasterFunctions=require('../dependencies/masterfunctions')
var fs=require('fs')
const https = require('https');
const request = require('request');
const gcs = require('@google-cloud/storage');

class model_project{
    public connection;
    constructor(connection){
        this.connection=connection
    }

    public testFunctions=async(req,res,next)=>{
        return new Promise(async(resolve,reject)=>{
            console.log("hi");
            var data={}
            data=MasterFunctions.formatResponse({name:"joel"},"true","All OK")
            resolve(data)
        })
    }

    public getUrl=async(req,res,next,url)=>{
        return new Promise(async(resolve,reject)=>{
            resolve({data:"joel",status:"true",message:"got url"})
        })
    }
}

module.exports=model_project;