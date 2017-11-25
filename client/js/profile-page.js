document.addEventListener('DOMContentLoaded', onProfileLoadPage);

function onProfileLoadPage() {
    if (location.href.indexOf('?') < 0) return;

    const { name, category } = parseProfileQueryParams();
    if (name && category) {
        sendGenerateProfile(name, category)
            .then(parseProfileData);
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

function parseProfileData(data) {
    const { name, profileDescription, profilePicture, publications } = data;
    setImage('profile-image', profilePicture);
    setText('profile-name', name);
    setText('description', profileDescription);
    setText('publications-number', publications.length);

    const publicationsElement = document.getElementById('publications');
    const rowsNumber = Math.ceil(publications.length / 3);

    for(let i = 0; i < rowsNumber; i++) {
        publicationsElement.appendChild(
            createPublicationsRow(name, publications[i], publications[i+1], publications[i+2])
        );
    }
}

function createPublicationsRow(ownerName, ...publications) {
    const row = document.createElement('div');
    row.classList.add('publications-row');
    publications.forEach(publication => row.appendChild(createPublication(publication, ownerName)));
    return row;
}
function createPublication(publication = {}, ownerName) {
    const { description = '', imageUrl = '', location = '', tags = []} = publication;
    const publicationElement = document.createElement('div');
    publicationElement.classList.add('_mck9w', '_gvoze', 'row-element');
    publicationElement.innerHTML =
        `<a href="publication.html?owner=${ownerName}&location=${location}">` +
            `<div class="_e3il2">` +
                `<div class="_4rbun">` +
                    `<img
                        class="_2di5p publ-image"
                        alt="${tags.join(' #')}"
                        src="${imageUrl}">` +
                `</div>` +
                `<div class="_si7dy"></div>` +
            `</div>` +
            `<div class="_lxd52">` +
                `<div class="_jnyq2">` +
                    `<span class="_c3aqk _8scx2 coreSpriteSidecarIconLarge">Post</span>` +
                `</div>` +
            `</div>` +
        `</a>`;
    return publicationElement;
}