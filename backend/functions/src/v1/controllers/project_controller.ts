const project_model=require('../model/project_model')
const MasterFunctions4=require('../dependencies/masterfunctions')


class ctrl_project{
    public hrtime;
    constructor()
    {
        this.hrtime=process.hrtime()
    }

    public testFunctions=async(req,res,next)=>{
        var project_model_object=new project_model(req.connection)
        var result=await project_model_object.testFunctions(req,res,next)
        res.send(result)
        // MasterFunctions4.logacesstoFbase(req,res,next,200,result,this.hrtime,0,0)        
    }

    public getUrl=async(req,res,next)=>{
        var project_model_object=new project_model(req.connection)
        var url=req.query.url
        var result=await project_model_object.getUrl(req,res,next,url)
        res.send(result)
    }
}

module.exports=ctrl_project