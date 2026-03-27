'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const header = document.querySelector('.ev-header');

    if (!header) {
        return;
    }

    const updateHeaderState = () => {
        if (window.scrollY > 24) {
            header.classList.remove('ev-header--transparent');
            header.classList.add('ev-header--scrolled');
            return;
        }

        header.classList.remove('ev-header--scrolled');
        header.classList.add('ev-header--transparent');
    };

    updateHeaderState();
    window.addEventListener('scroll', updateHeaderState, { passive: true });
});