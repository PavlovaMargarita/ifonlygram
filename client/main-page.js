const SERVER = 'http://10.168.0.160:8080';

document.addEventListener('DOMContentLoaded', () => {
    fetchCategories().then(updateCategories);
});

function fetchCategories() {
    return sendRequest('/blogCategories', 'GET');
}

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

/**
 * @param {Array} categories
 */
function updateCategories(categories) {
    const categoriesElement = document.getElementById('categories');

    removeChildren(categoriesElement);

    categories.forEach((category) => {
        const optionElement = document.createElement('OPTION');
        optionElement.appendChild(document.createTextNode(category));

        categoriesElement.appendChild(optionElement);
    });
}

function removeChildren(domElement) {
    while (domElement.firstChild) {
        domElement.removeChild(domElement.firstChild);
    }
}