'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const replyForm = document.querySelector('form[action*="/reply"]');
    const consultStatus = document.getElementById('consultStatus');
    const adminReply = document.getElementById('adminReply');

    if (!replyForm) {
        return;
    }

    replyForm.addEventListener('submit', (event) => {
        if (!consultStatus.value) {
            event.preventDefault();
            alert('상담 상태를 선택해 주세요.');
            consultStatus.focus();
            return;
        }

        if (!adminReply.value.trim()) {
            event.preventDefault();
            alert('관리자 답변을 입력해 주세요.');
            adminReply.focus();
        }
    });
});