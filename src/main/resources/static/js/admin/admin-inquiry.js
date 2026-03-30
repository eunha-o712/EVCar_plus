'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const detailLinks = document.querySelectorAll('.ev-inquiry-detail-link');
    const rows = document.querySelectorAll('.ev-inquiry-table tbody tr');

    rows.forEach((row) => {
        const link = row.querySelector('.ev-inquiry-detail-link');
        if (!link) {
            return;
        }

        row.addEventListener('click', (event) => {
            const target = event.target;
            if (target instanceof HTMLElement && target.closest('a')) {
                return;
            }
            window.location.href = link.href;
        });
    });

    detailLinks.forEach((link) => {
        link.addEventListener('click', (event) => {
            event.stopPropagation();
        });
    });
});