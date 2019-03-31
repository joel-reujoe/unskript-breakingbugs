var express = require('express');
var bodyParser = require('body-parser');
var MasterFunctions = require('../dependencies/masterfunctions');
var project_controller = require('../controllers/project_controller');
var project_controller_object = new project_controller();
var router = express.Router();
router.get('/getUrl', MasterFunctions.catchErrors(project_controller_object.getUrl));
router.post('/getUrl', MasterFunctions.catchErrors(project_controller_object.getUrl));
module.exports = router;
//# sourceMappingURL=project_router.js.map