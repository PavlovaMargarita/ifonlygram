const express = require('express');
const app = express();

app.get('/*', function(req, res){
    res.sendFile(__dirname + req.path);
    console.log(__dirname + req.path);
});

app.listen(3000);