const IRA_SERVER = 'http://10.168.1.21:8080';
const MARGARITA_SERVER = 'http://10.168.0.160:8090';

const SERVER = MARGARITA_SERVER;

function sendRequest(url, method = 'GET') {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();

        xhr.open(method, SERVER + url, true);
        xhr.send();

        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4) {
                if(xhr.status === 200) {
                    resolve(JSON.parse(xhr.responseText));
                } else {
                    reject(xhr.statusText);
                }
            }
        };
    });
}

function removeChildren(domElement) {
    while (domElement.firstChild) {
        domElement.removeChild(domElement.firstChild);
    }
}

function setImage(id, url) {
    document.getElementById(id).src = url;
}

function setText(id, text) {
    document.getElementById(id).innerText = text;
}

function relocateTo(url) {
    window.location.href = url;
}