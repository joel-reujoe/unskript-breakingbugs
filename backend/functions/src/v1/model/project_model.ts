var MasterFunctions=require('../dependencies/masterfunctions')


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
            console.log(url)
            var data={}
            data=MasterFunctions.formatResponse(url,"true","Got url");
            resolve(data)
        })
    }
}

module.exports=model_project;