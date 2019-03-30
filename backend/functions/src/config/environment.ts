export const environment = {
    production: false,
    cloud_bucket:`istats-dev.appspot.com`,
    realtime_database_url:`https://smarthealthcare-160f0.firebaseio.com/`,
    serviceaccountkey:require(`./serviceAccountKey`),
    // serviceaccountkeyDriver:require(`./foodiloo-driver-firebase-adminsdk.json`),
    // serviceaccountkeyRestaurant:require(`./foodiloo-restaurant-firebase-adminsdk.json`),
    firebaseConfig: {
        apiKey: "AIzaSyB0ga6ikjDvGADhhd80r2Xeiaep3K22H-M",
        authDomain: "smarthealthcare-160f0.firebaseapp.com",
        databaseURL: "https://istats-dev.firebaseio.com",
        projectId: "smarthealthcare-160f0",
        storageBucket: "smarthealthcare-160f0.appspot.com",
        messagingSenderId: "488465131798"
    }   
};

export const firebase_storage = {
    admin_images: `Istats/admin/images/`,
    admin_csv: 'Istats/admin/csv/',
    android_images: 'Istats/android/images/',
    android_core_images: 'IstatsAndroidCore/'
}

export const DBUsServer={
    host: "localhost",
    user: "root",
    password: "",
    database: "beproject" 
}

export const AMQPServer={
    host:"dinosaur.rmq.cloudamqp.com",
    user:"ddqilcod",
    password:"VjIbCQIPbY7oMV1DQzrUkrIiuvaQj1BI",
    url:"amqp://ddqilcod:VjIbCQIPbY7oMV1DQzrUkrIiuvaQj1BI@dinosaur.rmq.cloudamqp.com/ddqilcod"
}

export const ElasticSearchServer={
    host:'https://site:6e1047462052ea699d24e21983a18796@gloin-eu-west-1.searchly.com'
}

export const DBIndiaServer = {
    host: `localhost`,
    user: 'id8937355_joel_reujoe',
    password: '7709776820',
    database: 'id8937355_beproject',
    
}
