'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initFaqAccordion();
    initDisabledPagination();
});

function initFaqAccordion() {
    const faqItems = document.querySelectorAll('.ev-faq-item');

    faqItems.forEach((item) => {
        const button = item.querySelector('.ev-faq-item__question');

        if (!button) {
            return;
        }

        button.addEventListener('click', () => {
            item.classList.toggle('is-open');
        });
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