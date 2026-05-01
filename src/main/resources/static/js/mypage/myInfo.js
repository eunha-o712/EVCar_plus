'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initMyInfoEditMode();
    initMyInfoValidation();
    initPhoneFormatter();
});

/**
 * 수정 모드
 */
function initMyInfoEditMode() {
    const editButton = document.getElementById('editModeButton');
    const saveButton = document.getElementById('saveButton');
    const cancelButton = document.getElementById('cancelButton');
    const editableInputs = document.querySelectorAll('[data-editable="true"]');
    const radioGroups = document.querySelectorAll('.ev-myinfo-radio-group');

    if (!editButton || !saveButton || !cancelButton) return;

    editButton.addEventListener('click', () => {
        editableInputs.forEach((input) => {
            input.removeAttribute('readonly');
            input.removeAttribute('disabled');
        });

        document.querySelectorAll('input[type="radio"]').forEach((radio) => {
            radio.removeAttribute('disabled');
        });

        radioGroups.forEach((group) => {
            group.classList.remove('is-disabled');
        });

        editButton.classList.add('ev-myinfo-hidden');
        saveButton.classList.remove('ev-myinfo-hidden');
        cancelButton.classList.remove('ev-myinfo-hidden');
    });

    cancelButton.addEventListener('click', () => {
        window.location.reload();
    });
}

/**
 * 유효성 검사
 */
function initMyInfoValidation() {
    const form = document.getElementById('myInfoForm');

    if (!form) return;

    form.addEventListener('submit', (event) => {
        clearValidation();

        const isValid = validateMyInfoForm(form);

        if (!isValid) {
            event.preventDefault();
            showToast('입력값을 다시 확인해주세요.', 'error');
            return;
        }

        showToast('회원정보 수정 요청을 처리하고 있습니다.', 'success');
    });
}

function validateMyInfoForm(form) {
    let isValid = true;

    const currentPassword = form.querySelector('[name="currentPassword"]');
    const newPassword = form.querySelector('[name="newPassword"]');
    const newPasswordConfirm = form.querySelector('[name="newPasswordConfirm"]');
    const phone = form.querySelector('[name="phone"]');
    const email = form.querySelector('[name="email"]');

    if (!currentPassword || currentPassword.value.trim() === '') {
        showFieldError(currentPassword, '현재 비밀번호를 입력해주세요.');
        isValid = false;
    }

    if (phone && !/^010-\d{4}-\d{4}$/.test(phone.value.trim())) {
        showFieldError(phone, '전화번호는 010-0000-0000 형식으로 입력해주세요.');
        isValid = false;
    }

    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
        showFieldError(email, '이메일 형식이 올바르지 않습니다.');
        isValid = false;
    }

    if (newPassword && newPassword.value.trim() !== '') {
        if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(newPassword.value.trim())) {
            showFieldError(newPassword, '비밀번호는 영문/숫자 포함 8~20자');
            isValid = false;
        }

        if (!newPasswordConfirm || newPassword.value.trim() !== newPasswordConfirm.value.trim()) {
            showFieldError(newPasswordConfirm, '비밀번호가 일치하지 않습니다.');
            isValid = false;
        }
    }

    return isValid;
}

function initPhoneFormatter() {
    const phoneInput = document.getElementById('phone');

    if (!phoneInput) {
        return;
    }

    const formatPhone = () => {
        let value = phoneInput.value.replace(/\D/g, '');

        if (!value.startsWith('010')) {
            value = '010' + value.replace(/^0+/, '').replace(/^10/, '');
        }

        value = value.substring(0, 11);

        if (value.length <= 3) {
            phoneInput.value = value;
        } else if (value.length <= 7) {
            phoneInput.value = value.replace(/(\d{3})(\d+)/, '$1-$2');
        } else {
            phoneInput.value = value.replace(/(\d{3})(\d{4})(\d+)/, '$1-$2-$3');
        }
    };

    formatPhone();

    phoneInput.addEventListener('input', formatPhone);
}


function showFieldError(input, message) {
    if (!input) return;

    input.classList.add('is-invalid');

    const error = input.closest('.ev-myinfo-field')?.querySelector('.ev-field-error');

    if (!error) return;

    error.textContent = message;
    error.classList.add('is-show');
}


function clearValidation() {
    document.querySelectorAll('.ev-input.is-invalid').forEach((input) => {
        input.classList.remove('is-invalid');
    });

    document.querySelectorAll('.ev-field-error.is-show').forEach((error) => {
        error.textContent = '';
        error.classList.remove('is-show');
    });
}


function showToast(message, type) {
    let toast = document.getElementById('evToast');

    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'evToast';
        toast.className = 'ev-toast';
        document.body.appendChild(toast);
    }

    toast.textContent = message;
    toast.className = `ev-toast ev-toast--${type}`;
    toast.classList.add('is-show');

    setTimeout(() => {
        toast.classList.remove('is-show');
    }, 2200);
}