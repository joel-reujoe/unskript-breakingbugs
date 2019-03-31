const project_model=require('../model/project_model')
const MasterFunctions4=require('../dependencies/masterfunctions')
const multiparty=require('multiparty');
const multer1any = require('multer');
const download = require('image-downloader')
var amqp = require('amqplib/callback_api');
var config=require('../../config/environment')
var MasterFunctions9=require('../dependencies/masterfunctions')
var uuid=require('uuid')
// const upload1any = multer1any();

var upload = multer1any({ dest: 'upload/'});
var type = upload.single('file');

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
        const options = {
            url:  'https://firebasestorage.googleapis.com/v0/b/unskript-d89f8.appspot.com/o/testImage%2F'+req.query.name+'?alt=media&token='+req.query.token,
            dest: 'C:/xampp/htdocs/Unskript/backend/functions/lib/v1/python-runtime/upload'                  // Save to /path/to/dest/image.jpg
          }
          download.image(options).then(({ filename, image }) => {
                console.log('File saved to', filename)
            }).catch((err) => {
                console.error(err)
            })

  var self=this
        amqp.connect(config.AMQPServer.url, function(err, conn) {
            conn.createChannel(function(err, ch) {
                var q = 'ImageQueue';    
                ch.assertQueue('', {exclusive: true}, function(err, q) {
                    var corr = uuid();
                    
                    ch.consume(q.queue, async(msg) =>{
                        console.log(msg.properties.correlationId)
                        if (msg.properties.correlationId == corr) {
                            var data=JSON.parse(msg.content.toString())
                             setTimeout(function() { conn.close();}, 500);
                            // MasterFunctions9.logacesstoFbase(req,res,next,200,data,self.hrtime,0,0)
                        }
                      }, {noAck: true});
                      ch.sendToQueue('ImageQueue',Buffer.from('testImage%2f'+req.query.name),
                        { correlationId: corr, replyTo: q.queue });
                });
            });
        });
  res.send({data:"Saved",status:"true"})
}
}

module.exports=ctrl_project