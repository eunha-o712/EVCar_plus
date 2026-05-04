'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initDomainSelect();
    initResetPwCheckForm();
    showResetCheckMessage();
});

function initDomainSelect() {
    const select = document.getElementById('resetDomainSelect');
    const domainInput = document.getElementById('resetEmailDomain');

    if (!select || !domainInput) {
        return;
    }

    select.addEventListener('change', () => {
        domainInput.value = select.value;
        domainInput.readOnly = select.value !== '';
    });
}

function initResetPwCheckForm() {
    const form = document.getElementById('resetPwCheckForm');
    const emailIdInput = document.getElementById('resetEmailId');
    const emailDomainInput = document.getElementById('resetEmailDomain');
    const emailFullInput = document.getElementById('resetEmailFull');

    if (!form || !emailIdInput || !emailDomainInput || !emailFullInput) {
        return;
    }

    form.addEventListener('submit', (event) => {
        const emailId = emailIdInput.value.trim();
        const emailDomain = emailDomainInput.value.trim();

        if (!emailId || !emailDomain) {
            alert('이메일을 입력해주세요.');
            event.preventDefault();
            return;
        }

        emailFullInput.value = `${emailId}@${emailDomain}`;
    });
}

function showResetCheckMessage() {
    const message = window.evResetCheckMessage;

    if (!message || !message.errorMessage) {
        return;
    }

    alert(message.errorMessage);
}