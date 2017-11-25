const express = require('express');
const app = express();

const HTML_LOCATION = '/static/html/';

app.get('/*', function(req, res){
    console.log(__dirname + req.path);

    if (req.path.indexOf('index.html') >= 0) {
        return res.sendFile(__dirname + req.path);
    }
    if (req.path.indexOf('.html') > 0) {
        return res.sendFile(__dirname + HTML_LOCATION + req.path);
    }

    return res.sendFile(__dirname + req.path);
});

app.listen(3000);