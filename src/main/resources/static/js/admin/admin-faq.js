'use strict';

document.addEventListener('DOMContentLoaded', () => {
    bindDeleteConfirm();
    bindFormValidation();
    bindTextareaAutoResize();
});

function bindDeleteConfirm() {
    const deleteForms = document.querySelectorAll('.ev-admin-faq-actions form');

    if (!deleteForms.length) {
        return;
    }

    deleteForms.forEach((form) => {
        form.addEventListener('submit', (event) => {
            const confirmed = window.confirm('해당 FAQ를 삭제하시겠습니까?');

            if (!confirmed) {
                event.preventDefault();
            }
        });
    });
}

function bindFormValidation() {
    const form = document.getElementById('adminFaqForm');
    const questionInput = document.getElementById('question');
    const answerInput = document.getElementById('answer');

    if (!form || !questionInput || !answerInput) {
        return;
    }

    form.addEventListener('submit', (event) => {
        const question = questionInput.value.trim();
        const answer = answerInput.value.trim();

        if (!question) {
            event.preventDefault();
            window.alert('질문을 입력해주세요.');
            questionInput.focus();
            return;
        }

        if (!answer) {
            event.preventDefault();
            window.alert('답변을 입력해주세요.');
            answerInput.focus();
        }
    });
}

function bindTextareaAutoResize() {
    const textarea = document.getElementById('answer');

    if (!textarea) {
        return;
    }

    const resize = () => {
        textarea.style.height = 'auto';
        textarea.style.height = `${textarea.scrollHeight}px`;
    };

    resize();
    textarea.addEventListener('input', resize);
}