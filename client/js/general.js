const IRA_SERVER = 'http://172.20.10.4:8090';
const MARGARITA_SERVER = 'http://10.168.0.160:8090';

const SERVER = MARGARITA_SERVER;
const IFILTERS = [
    '_1977', 'moon', 'nashville',
    'aden', 'amaro', 'brannan',
    'brooklyn', 'toaster', 'reyes'
];

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

function eachClassElement(className, callback, root = document) {
    Array.prototype.forEach.call(root.getElementsByClassName(className), callback);
}

function relocateTo(url) {
    window.location.href = url;
}

function disableClicks(root = document) {
    const allLinksElements = root.querySelectorAll('a:not(enable)');
    const allButtonsElements = root.querySelectorAll('buttons:not(enable)');

    Array.prototype.forEach.call(
        allLinksElements,
        (elem) => elem.addEventListener('click', (e) => e.preventDefault())
    );
    Array.prototype.forEach.call(
        allButtonsElements,
        (elem) => elem.addEventListener('click', (e) => e.preventDefault())
    );
}