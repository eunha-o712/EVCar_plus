'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const filterButtons = document.querySelectorAll('.ev-admin-chip');

    filterButtons.forEach((button) => {
        button.addEventListener('click', () => {
            filterButtons.forEach((item) => item.classList.remove('is-active'));
            button.classList.add('is-active');
        });
    });
});