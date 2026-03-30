'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const withdrawForm = document.getElementById('withdrawForm');
    const withdrawPassword = document.getElementById('withdrawPassword');
    const withdrawReason = document.getElementById('withdrawReason');
    const withdrawAgree = document.getElementById('withdrawAgree');
    const withdrawErrorMessage = document.getElementById('withdrawErrorMessage');
    const withdrawSubmitButton = document.getElementById('withdrawSubmitButton');

    if (!withdrawForm || !withdrawPassword || !withdrawReason || !withdrawAgree || !withdrawErrorMessage || !withdrawSubmitButton) {
        return;
    }

    const showError = (message) => {
        withdrawErrorMessage.textContent = message;
    };

    const clearError = () => {
        withdrawErrorMessage.textContent = '';
    };

    const validateWithdrawForm = () => {
        const password = withdrawPassword.value.trim();
        const reason = withdrawReason.value.trim();
        const agreed = withdrawAgree.checked;

        if (password === '') {
            showError('비밀번호를 입력해주세요.');
            withdrawPassword.focus();
            return false;
        }

        if (reason === '') {
            showError('탈퇴 사유를 입력해주세요.');
            withdrawReason.focus();
            return false;
        }

        if (!agreed) {
            showError('회원탈퇴 안내사항 동의가 필요합니다.');
            withdrawAgree.focus();
            return false;
        }

        clearError();
        return true;
    };

    withdrawPassword.addEventListener('input', clearError);
    withdrawReason.addEventListener('input', clearError);
    withdrawAgree.addEventListener('change', clearError);

    withdrawForm.addEventListener('submit', (event) => {
        event.preventDefault();

        if (!validateWithdrawForm()) {
            return;
        }

        const confirmed = window.confirm(
            '정말 회원탈퇴를 진행하시겠습니까?\n탈퇴 후 계정 정보는 복구할 수 없습니다.'
        );

        if (!confirmed) {
            return;
        }

        withdrawSubmitButton.disabled = true;
        withdrawForm.submit();
    });
});