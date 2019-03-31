var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var MasterFunctions = require('../dependencies/masterfunctions');
var fs = require('fs');
const https = require('https');
const request = require('request');
const gcs = require('@google-cloud/storage');
class model_project {
    constructor(connection) {
        this.testFunctions = (req, res, next) => __awaiter(this, void 0, void 0, function* () {
            return new Promise((resolve, reject) => __awaiter(this, void 0, void 0, function* () {
                console.log("hi");
                var data = {};
                data = MasterFunctions.formatResponse({ name: "joel" }, "true", "All OK");
                resolve(data);
            }));
        });
        this.getUrl = (req, res, next, url) => __awaiter(this, void 0, void 0, function* () {
            return new Promise((resolve, reject) => __awaiter(this, void 0, void 0, function* () {
                resolve({ data: "joel", status: "true", message: "got url" });
            }));
        });
        this.connection = connection;
    }
}
module.exports = model_project;
//# sourceMappingURL=project_model.js.map