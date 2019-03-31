var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var config = require('../../config/environment');
var exec = require("child_process").exec;
var MasterFunctions9 = require('../dependencies/masterfunctions');
var amqp = require('amqplib/callback_api');
class python_runtime {
    constructor() {
        this.python_runtime_switch = (child) => __awaiter(this, void 0, void 0, function* () {
            return new Promise(function (resolve, reject) {
                child.addListener("error", reject);
                child.addListener("exit", resolve);
            });
        });
    }
}
amqp.connect(config.AMQPServer.url, function (err, conn) {
    if (err)
        throw err;
    conn.createChannel(function (err, ch) {
        var q = 'ImageQueue';
        ch.assertQueue(q, { durable: false });
        ch.prefetch(1);
        console.log(' [x] Awaiting RPC requests');
        ch.consume(q, (msg) => __awaiter(this, void 0, void 0, function* () {
            var x = msg.content.toString();
            var dataToSend = {};
            var child = exec(`python skinprocess.py ./upload/${x}`);
            var ctrl_python_runtime_object = new python_runtime();
            ctrl_python_runtime_object.python_runtime_switch(child)
                .then(function (result) {
                console.log(result);
                dataToSend = MasterFunctions9.formatResponse(result, "true", "");
                ch.sendToQueue(msg.properties.replyTo, Buffer.from(JSON.stringify(dataToSend)), { correlationId: msg.properties.correlationId });
                ch.ack(msg);
            }, function (error) {
                dataToSend = MasterFunctions9.formatResponse(error, "false", "");
                ch.sendToQueue(msg.properties.replyTo, Buffer.from(JSON.stringify(dataToSend)), { correlationId: msg.properties.correlationId });
                ch.ack(msg);
            });
        }));
    });
});
//# sourceMappingURL=python_runtime.js.map