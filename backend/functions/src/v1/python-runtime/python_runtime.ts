var config=require('../../config/environment')
var exec = require("child_process").exec;
var MasterFunctions9=require('../dependencies/masterfunctions')
var amqp = require('amqplib/callback_api');


class python_runtime{
    public python_runtime_switch=async(child)=>{
            return new Promise(function (resolve, reject) {
              child.addListener("error", reject);
              child.addListener("exit", resolve);
            });
        }
      }
  
  
  amqp.connect(config.AMQPServer.url, function(err, conn) {
    if(err)throw err
              conn.createChannel(function(err, ch) {
                var q = 'ImageQueue';
                ch.assertQueue(q, {durable: false});
                ch.prefetch(1);
                console.log(' [x] Awaiting RPC requests');
                  ch.consume(q, async(msg)=> {
                    
                      var x=msg.content.toString();
                      var dataToSend={}
                      var child=exec(`python skinprocess.py ./upload/${x}`)
                      
                      var ctrl_python_runtime_object=new python_runtime()
                      ctrl_python_runtime_object.python_runtime_switch(child)
                      .then(function(result){
                        console.log(result)
                        dataToSend=MasterFunctions9.formatResponse(result,"true","")
  
                        ch.sendToQueue(msg.properties.replyTo,Buffer.from(JSON.stringify(dataToSend)),
                        {correlationId: msg.properties.correlationId});
                        
                        ch.ack(msg);
                      },
                      function(error){
                        dataToSend=MasterFunctions9.formatResponse(error,"false","")
  
                        ch.sendToQueue(msg.properties.replyTo,Buffer.from(JSON.stringify(dataToSend)),
                        {correlationId: msg.properties.correlationId});
                        
                        ch.ack(msg);
                      })
                });
              });
            });