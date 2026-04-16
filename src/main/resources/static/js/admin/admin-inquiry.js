'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const rows = document.querySelectorAll('.ev-inquiry-table tbody tr');

    rows.forEach((row) => {
        const detailUrl = row.dataset.detailUrl;
        const link = row.querySelector('.ev-inquiry-detail-link');

        if (!detailUrl || !link) {
            return;
        }

        row.addEventListener('click', (event) => {
            if (event.target.closest('a, button, textarea, input')) {
                return;
            }

            window.location.href = detailUrl;
        });
    });
});