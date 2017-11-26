document.addEventListener('DOMContentLoaded', onLoadPage);

function onLoadPage() {
    fetchCategories().then(updateCategories);
    disableClicks();
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
