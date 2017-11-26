document.addEventListener('DOMContentLoaded', onProfileLoadPage);

function onProfileLoadPage() {
    if (location.href.indexOf('?') < 0) return;

    const { name, category } = parseProfileQueryParams();
    if (name && category) {
        sendGenerateProfile(name, category)
            .then(parseProfileData);
    }

    document.getElementById('close-dialog')
        .addEventListener('click', (e) => {
            e.preventDefault();
            closeDialog();
        });
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
    let { name, profileDescription, profilePicture, publications } = data;
    const publicationsNumber = publications.length;
    profilePicture = profilePicture || (publications && publications[publicationsNumber - 1].imageUrl);

    setImage('profile-image', profilePicture);
    setImage('dialog-profile-img', profilePicture);
    setText('subscribers', Math.ceil((Math.random() * 123) + 1));
    setText('profile-name', name);
    setText('description', profileDescription);
    setText('publications-number', publications.length);

    const publicationsElement = document.getElementById('publications');
    const rowsNumber = Math.ceil(publicationsNumber / 3);

    for(let i = 0; i < rowsNumber; i++) {
        let rowNumber = i * 3;
        publicationsElement.appendChild(
            createPublicationsRow(name, publications[rowNumber], publications[rowNumber+1], publications[rowNumber+2])
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
        `<a href="publication.html?owner=${ownerName}&location=${location}" class="open-dialog">` +
            `<div class="_e3il2">` +
                `<div class="_4rbun">` +
                    `<img class="_2di5p publ-image"
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

    publicationElement
        .getElementsByClassName('open-dialog')[0]
        .addEventListener('click', (e) => {
            e.preventDefault();
            openDialog(publication, ownerName);
        });

    return publicationElement;
}

function openDialog(publication, ownerName) {
    const { description = '', imageUrl = '', location = '', tags = []} = publication;
    const dialog = document.getElementById('dialog');

    setImage('dialog-image', imageUrl);
    setText('location', location);
    setText('publication-description', description);
    eachClassElement('ownername', e => e.innerText = ownerName, dialog);
    addTags(tags);

    dialog.classList.remove('hidden');
}

function closeDialog() {
    document.getElementById('dialog').classList.add('hidden');
}

function addTags(tags) {
    const tagsElement = document.getElementById('tags');
    const lastIdx = tags.length - 1;

    tags.forEach((tag, idx) => {
        const tagElement = document.createElement('A');
        tagElement.innerText = '#'+tag;
        tagsElement.appendChild(tagElement);

        if (idx < lastIdx) {
            const span = document.createElement('SPAN');
            span.innerText = ' ';
            tagsElement.appendChild(span);
        }
    });
}