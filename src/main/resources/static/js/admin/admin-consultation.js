<<<<<<< HEAD
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
=======
'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const filterButtons = document.querySelectorAll('.ev-admin-chip');

    filterButtons.forEach((button) => {
        button.addEventListener('click', () => {
            filterButtons.forEach((item) => item.classList.remove('is-active'));
            button.classList.add('is-active');
        });
    });
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
});