document.addEventListener('DOMContentLoaded', onProfileLoadPage);

function onProfileLoadPage() {
    if (location.href.indexOf('?') < 0) return;

    const { name, category} = parseProfileQueryParams();
    sendGenerateProfile(name, category).then((data) => {
        debugger;
    });
}

function parseProfileQueryParams() {
    const queryParamsString = location.href.match(/^.*(\?.*)$/)[1];
    const queryParams = queryParamsString.split('&');
    let name;
    let category;
    queryParams.forEach((param) => {
        name = param.match(/^name\=(\w*)$/)[1];
        if(!name) {
            category = param.match(/^blogCategory=\=(\w*)$/)[1];
        }
    });
    return { name, category };
}

function sendGenerateProfile(name, category) {
    return sendRequest(`/generateProfile?name=${name}&blogCategory=${category}`, 'GET');
}