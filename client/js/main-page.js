document.addEventListener('DOMContentLoaded', onLoadPage);

function onLoadPage() {
    fetchCategories().then(updateCategories);
    initListeners();
}

function initListeners() {
    document.getElementById('run').addEventListener('click', () => {
        const name = document.getElementById('username').value;
        const category = document.getElementById('categories').value;
        const indexLocation = window.location.href.match(/^(.*)\/[a-z]*\.html\?+.*$/)[1];
        window.location.replace(`${indexLocation}/html/profile.html?name=${name}&category=${category}`);
    });
}

function fetchCategories() {
    return sendRequest('/blogCategories', 'GET');
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
