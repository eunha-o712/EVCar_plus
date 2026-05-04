'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initDomainSelect();
    showFindIdMessage();
});

function initDomainSelect() {
    const select = document.getElementById('findDomainSelect');
    const domainInput = document.getElementById('findEmailDomain');

    if (!select || !domainInput) {
        return;
    }

    select.addEventListener('change', () => {
        domainInput.value = select.value;
        domainInput.readOnly = select.value !== '';
    });
}

function showFindIdMessage() {
    const message = window.evFindIdMessage;

    if (!message) {
        return;
    }

    if (message.errorMessage) {
        alert(message.errorMessage);
    }

    if (message.foundId) {
        alert(`아이디는 '${message.foundId}' 입니다.`);
    }
}