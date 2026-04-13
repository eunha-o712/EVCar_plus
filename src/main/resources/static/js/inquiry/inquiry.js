'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initDisabledPagination();
});

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