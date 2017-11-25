document.addEventListener('DOMContentLoaded', onProfileLoadPage);

function onProfileLoadPage() {
    if (location.href.indexOf('?') < 0) return;

    const { name, category } = parseProfileQueryParams();
    if (name && category) {
        sendGenerateProfile(name, category).then((data) => {
            debugger;
        });
    }
}

function parseProfileQueryParams() {
    const queryParamsString = location.href.match(/^.*\?(.*)$/)[1];
    const queryParams = decodeURIComponent(queryParamsString).split('&');
    let name;
    let category;
    queryParams.forEach((param) => {
        const parsedName = param.match(/^username\=(.*)$/);
        if(parsedName && !name) {
            name = parsedName[1];
        } else if (!category) {
            const parsedCategory = param.match(/^category\=(.*)$/);
            category = parsedCategory && parsedCategory[1];
        }
    });
    return { name, category };
}

function sendGenerateProfile(name, category) {
    return sendRequest(`/generateProfile?name=${name}&blogCategory=${category}`, 'GET');
}