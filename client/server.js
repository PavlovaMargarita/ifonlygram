const express = require('express');
const app = express();

const HTML_LOCATION = '';
const CSS_LOCATION = '';

app.get('/*', function(req, res){
    console.log(__dirname + req.path);

    if (req.path.indexOf('index.html') >= 0) {
        return res.sendFile(__dirname + req.path);
    }
    if (req.path.indexOf('.html') > 0) {
        return res.sendFile(__dirname + HTML_LOCATION + req.path);
    }
    if (req.path.indexOf('.css') > 0) {
        return res.sendFile(__dirname + CSS_LOCATION + req.path);
    }

    return res.sendFile(__dirname + req.path);
});

// print process.argv
process.argv.forEach(function (val, index, array) {
    const parsedServer = val.match(/^server\=([\w\d\.]*)/);
    if (parsedServer) {
        server = parsedServer[1];
        console.log(server);
    }
});

app.listen(3000);