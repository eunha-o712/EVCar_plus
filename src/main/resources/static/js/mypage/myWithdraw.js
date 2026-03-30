'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const withdrawForm = document.getElementById('withdrawForm');
    const passwordInput = document.getElementById('password');
    const withdrawReasonSelect = document.getElementById('withdrawReason');
    const agreeWithdrawCheckbox = document.getElementById('agreeWithdraw');

    if (!withdrawForm) {
        return;
    }

    withdrawForm.addEventListener('submit', (event) => {
        if (!passwordInput.value.trim()) {
            event.preventDefault();
            alert('현재 비밀번호를 입력해주세요.');
            passwordInput.focus();
            return;
        }

        if (!withdrawReasonSelect.value) {
            event.preventDefault();
            alert('탈퇴 사유를 선택해주세요.');
            withdrawReasonSelect.focus();
            return;
        }

        if (!agreeWithdrawCheckbox.checked) {
            event.preventDefault();
            alert('회원탈퇴 동의가 필요합니다.');
            agreeWithdrawCheckbox.focus();
            return;
        }

        const confirmed = window.confirm('정말 탈퇴를 진행하시겠습니까? 탈퇴 후 계정 복구는 불가능합니다.');

        if (!confirmed) {
            event.preventDefault();
        }
    });
});