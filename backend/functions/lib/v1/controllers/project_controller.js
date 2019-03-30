var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
const project_model = require('../model/project_model');
const MasterFunctions4 = require('../dependencies/masterfunctions');
class ctrl_project {
    constructor() {
        this.testFunctions = (req, res, next) => __awaiter(this, void 0, void 0, function* () {
            var project_model_object = new project_model(req.connection);
            var result = yield project_model_object.testFunctions(req, res, next);
            res.send(result);
            // MasterFunctions4.logacesstoFbase(req,res,next,200,result,this.hrtime,0,0)        
        });
        this.getUrl = (req, res, next) => __awaiter(this, void 0, void 0, function* () {
            var project_model_object = new project_model(req.connection);
            var url = req.query.url;
            var result = yield project_model_object.getUrl(req, res, next, url);
            res.send(result);
        });
        this.hrtime = process.hrtime();
    }
}
module.exports = ctrl_project;
//# sourceMappingURL=project_controller.js.map