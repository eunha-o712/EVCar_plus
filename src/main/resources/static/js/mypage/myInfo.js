'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initMyInfoValidation();
});

function initMyInfoValidation() {
    const form = document.getElementById('myInfoForm');

    if (!form) {
        return;
    }

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
    const vehicleModel = form.querySelector('[name="vehicleModel"]');
    const vehicleYear = form.querySelector('[name="vehicleYear"]');
    const drivingDistance = form.querySelector('[name="drivingDistance"]');

    if (!currentPassword || currentPassword.value.trim() === '') {
        showFieldError(currentPassword, '회원정보 수정을 위해 현재 비밀번호를 입력해주세요.');
        isValid = false;
    }

    if (phone && !/^(\+?\d{10,15}|01[016789]-?\d{3,4}-?\d{4})$/.test(phone.value.trim())) {
        showFieldError(phone, '전화번호 형식이 올바르지 않습니다.');
        isValid = false;
    }

    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
        showFieldError(email, '이메일 형식이 올바르지 않습니다.');
        isValid = false;
    }

    const hasNewPassword = newPassword && newPassword.value.trim() !== '';
    const hasNewPasswordConfirm = newPasswordConfirm && newPasswordConfirm.value.trim() !== '';

    if (hasNewPassword || hasNewPasswordConfirm) {
        if (!newPassword || !/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(newPassword.value.trim())) {
            showFieldError(newPassword, '비밀번호는 영문/숫자를 포함해 8~20자로 입력해주세요.');
            isValid = false;
        }

        if (!newPasswordConfirm || newPassword.value.trim() !== newPasswordConfirm.value.trim()) {
            showFieldError(newPasswordConfirm, '비밀번호가 일치하지 않습니다.');
            isValid = false;
        }
    }

    if (vehicleModel && vehicleModel.value.trim() !== '') {
        if (vehicleYear && vehicleYear.value.trim() !== '' && !/^\d{4}-(0[1-9]|1[0-2])$/.test(vehicleYear.value.trim())) {
            showFieldError(vehicleYear, '연식은 YYYY-MM 형식으로 입력해주세요.');
            isValid = false;
        }

        if (drivingDistance && drivingDistance.value.trim() !== '' && !/^\d{1,10}$/.test(drivingDistance.value.trim())) {
            showFieldError(drivingDistance, '주행거리는 숫자만 입력해주세요.');
            isValid = false;
        }
    }

    return isValid;
}

function showFieldError(input, message) {
    if (!input) {
        return;
    }

    input.classList.add('is-invalid');

    const error = input.closest('.ev-form-group')?.querySelector('.ev-field-error');

    if (!error) {
        return;
    }

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

    window.setTimeout(() => {
        toast.classList.remove('is-show');
    }, 2200);
}