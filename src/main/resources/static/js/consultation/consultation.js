'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initPreferredDatetimePlaceholder();
    initDisabledPagination();
});

function initPreferredDatetimePlaceholder() {
    const preferredDatetimeInput = document.getElementById('preferredDatetime');

    if (!preferredDatetimeInput) {
        return;
    }

    preferredDatetimeInput.addEventListener('focus', () => {
        preferredDatetimeInput.placeholder = '예: 2026-04-11 14:00';
    });
}

function initDisabledPagination() {
    const disabledLinks = document.querySelectorAll('.ev-pagination__link.is-disabled');

    disabledLinks.forEach((link) => {
        link.setAttribute('tabindex', '-1');
        link.setAttribute('aria-disabled', 'true');

        link.addEventListener('click', (event) => {
            event.preventDefault();
        });
    });
}