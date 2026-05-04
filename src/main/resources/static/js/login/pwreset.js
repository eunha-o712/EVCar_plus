'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initPasswordForm();
    initBackButton();
    showServerMessage();
});

function initPasswordForm() {
    const form = document.getElementById('pwForm');

    if (!form) {
        return;
    }

    form.addEventListener('submit', validatePasswordForm);
}

function validatePasswordForm(event) {
    const form = event.currentTarget;
    const password = form.newPassword.value.trim();
    const passwordConfirm = form.newPasswordConfirm.value.trim();
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$/;

    if (!password || !passwordConfirm) {
        alert('비밀번호를 입력해주세요.');
        event.preventDefault();
        return;
    }

    if (!passwordRegex.test(password)) {
        alert('비밀번호는 영문과 숫자를 반드시 포함한 8~20자여야 합니다.');
        form.newPassword.focus();
        event.preventDefault();
        return;
    }

    if (password !== passwordConfirm) {
        alert('비밀번호가 일치하지 않습니다.');
        form.newPasswordConfirm.focus();
        event.preventDefault();
    }
}

function initBackButton() {
    const backButton = document.querySelector('.ev-pwreset-back');

    if (!backButton) {
        return;
    }

    backButton.addEventListener('click', () => {
        window.location.href = backButton.dataset.loginUrl;
    });
}

function showServerMessage() {
    const message = window.evPwresetMessage;

    if (!message) {
        return;
    }

    if (message.errorMessage) {
        alert(message.errorMessage);
    }

    if (message.successMessage) {
        alert(message.successMessage);
        window.location.href = '/login';
    }
}