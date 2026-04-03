'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const cancelForms = document.querySelectorAll('.ev-consultation-cancel-form');

    if (!cancelForms.length) {
        return;
    }

    cancelForms.forEach((form) => {
        form.addEventListener('submit', (event) => {
            const confirmed = window.confirm('상담을 취소하시겠습니까?');

            if (!confirmed) {
                event.preventDefault();
            }
        });
    });
});