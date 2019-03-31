"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.environment = {
    production: false,
    cloud_bucket: `istats-dev.appspot.com`,
    realtime_database_url: `https://smarthealthcare-160f0.firebaseio.com/`,
    serviceaccountkey: require(`./serviceAccountKey`),
    // serviceaccountkeyDriver:require(`./foodiloo-driver-firebase-adminsdk.json`),
    // serviceaccountkeyRestaurant:require(`./foodiloo-restaurant-firebase-adminsdk.json`),
    firebaseConfig: {
        apiKey: "AIzaSyDSF6e5vYkMSUqZQE4mACuILCY_-gA_6bg",
        authDomain: "smarthealthcare-160f0.firebaseapp.com",
        databaseURL: "https://istats-dev.firebaseio.com",
        projectId: "unskript-d89f8",
        storageBucket: "unskript-d89f8.appspot.com",
        messagingSenderId: "488465131798"
    }
};
exports.firebase_storage = {
    admin_images: `Istats/admin/images/`,
    admin_csv: 'Istats/admin/csv/',
    android_images: 'Istats/android/images/',
    android_core_images: 'IstatsAndroidCore/'
};
exports.DBUsServer = {
    host: "localhost",
    user: "root",
    password: "",
    database: "beproject"
};
exports.AMQPServer = {
    host: "spider.rmq.cloudamqp.com",
    user: "rvalxhgd",
    password: "aPjK9uqKN6Ib182jHd0uAovEWGB59o2x",
    url: "amqp://rvalxhgd:aPjK9uqKN6Ib182jHd0uAovEWGB59o2x@spider.rmq.cloudamqp.com/rvalxhgd"
};
exports.ElasticSearchServer = {
    host: 'https://site:6e1047462052ea699d24e21983a18796@gloin-eu-west-1.searchly.com'
};
exports.DBIndiaServer = {
    host: `localhost`,
    user: 'id8937355_joel_reujoe',
    password: '7709776820',
    database: 'id8937355_beproject',
};
//# sourceMappingURL=environment.js.map