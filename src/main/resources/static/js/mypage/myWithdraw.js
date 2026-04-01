'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('withdrawForm');
    const reasonRadios = document.querySelectorAll('input[name="withdrawReason"]');
    const reasonDetailBox = document.getElementById('withdrawReasonDetailBox');
    const reasonDetailInput = document.getElementById('withdrawReasonDetail');
    const passwordInput = document.getElementById('withdrawPassword');
    const agreeCheckbox = document.getElementById('withdrawAgree');
    const errorMessage = document.getElementById('withdrawErrorMessage');

    const toggleOtherReason = () => {
        const selectedReason = document.querySelector('input[name="withdrawReason"]:checked');
        const isOtherSelected = selectedReason && selectedReason.value === '기타';

        if (isOtherSelected) {
            reasonDetailBox.classList.remove('ev-withdraw-hidden');
            reasonDetailInput.disabled = false;
        } else {
            reasonDetailBox.classList.add('ev-withdraw-hidden');
            reasonDetailInput.value = '';
            reasonDetailInput.disabled = true;
        }
    };

    const showError = (message) => {
        errorMessage.textContent = message;
    };

    const clearError = () => {
        errorMessage.textContent = '';
    };

    reasonRadios.forEach((radio) => {
        radio.addEventListener('change', () => {
            toggleOtherReason();
            clearError();
        });
    });

    toggleOtherReason();

    form.addEventListener('submit', (event) => {
        const selectedReason = document.querySelector('input[name="withdrawReason"]:checked');

        if (!passwordInput.value.trim()) {
            event.preventDefault();
            showError('현재 비밀번호를 입력해주세요.');
            passwordInput.focus();
            return;
        }

        if (!selectedReason) {
            event.preventDefault();
            showError('탈퇴 사유를 선택해주세요.');
            return;
        }

        if (selectedReason.value === '기타' && !reasonDetailInput.value.trim()) {
            event.preventDefault();
            showError('기타 사유를 입력해주세요.');
            reasonDetailInput.focus();
            return;
        }

        if (!agreeCheckbox.checked) {
            event.preventDefault();
            showError('회원탈퇴 안내사항 동의가 필요합니다.');
            agreeCheckbox.focus();
            return;
        }

        clearError();
    });
});