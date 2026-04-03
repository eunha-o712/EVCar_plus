'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initBoardMenu();
    initMobileMenu();
    initModal();
});

function initBoardMenu() {
    const boardItems = document.querySelectorAll('.ev-gnb__item--board');
    const triggers = document.querySelectorAll('.ev-gnb__trigger');
    const backdrop = document.getElementById('evBoardBackdrop');
    const desktopMedia = window.matchMedia('(min-width: 1024px)');

    const closeAllBoards = () => {
        boardItems.forEach((item) => item.classList.remove('is-active'));
        if (backdrop) {
            backdrop.classList.remove('is-open');
        }
    };

    const openBoard = (triggerElement) => {
        boardItems.forEach((item) => {
            item.classList.toggle('is-active', item.contains(triggerElement));
        });

        if (backdrop) {
            backdrop.classList.add('is-open');
        }
    };

    triggers.forEach((trigger) => {
        trigger.addEventListener('click', (event) => {
            event.stopPropagation();

            const parentItem = trigger.closest('.ev-gnb__item--board');
            if (!parentItem) {
                return;
            }

            const isAlreadyOpen = parentItem.classList.contains('is-active');
            if (isAlreadyOpen) {
                closeAllBoards();
                return;
            }

            openBoard(trigger);
        });

        trigger.addEventListener('mouseenter', () => {
            if (!desktopMedia.matches) {
                return;
            }

            openBoard(trigger);
        });
    });

    boardItems.forEach((item) => {
        item.addEventListener('mouseleave', () => {
            if (!desktopMedia.matches) {
                return;
            }

            closeAllBoards();
        });
    });

    if (backdrop) {
        backdrop.addEventListener('click', closeAllBoards);
    }

    document.addEventListener('click', (event) => {
        if (!event.target.closest('.ev-gnb__item--board')) {
            closeAllBoards();
        }
    });

    window.addEventListener('resize', () => {
        closeAllBoards();
    });
}

function initMobileMenu() {
    const menuToggle = document.getElementById('evMobileMenuToggle');
    const menu = document.getElementById('evNavbarMenu');

    if (!menuToggle || !menu) {
        return;
    }

    menuToggle.addEventListener('click', () => {
        menu.classList.toggle('is-open');
    });
}

function initModal() {
    const openButtons = document.querySelectorAll('[data-ev-modal-open]');
    const closeButtons = document.querySelectorAll('[data-ev-modal-close]');
    const modals = document.querySelectorAll('.ev-modal');

    const closeAllModals = () => {
        modals.forEach((modal) => {
            modal.classList.remove('is-open');
            modal.setAttribute('aria-hidden', 'true');
        });
        document.body.classList.remove('ev-modal-open');
    };

    const openModal = (modalId) => {
        const targetModal = document.getElementById(modalId);
        if (!targetModal) {
            return;
        }

        targetModal.classList.add('is-open');
        targetModal.setAttribute('aria-hidden', 'false');
        document.body.classList.add('ev-modal-open');
    };

    openButtons.forEach((button) => {
        button.addEventListener('click', () => {
            openModal(button.dataset.evModalOpen);
        });
    });

    closeButtons.forEach((button) => {
        button.addEventListener('click', closeAllModals);
    });

    modals.forEach((modal) => {
        modal.addEventListener('click', (event) => {
            if (!event.target.closest('.ev-modal__dialog')) {
                closeAllModals();
            }
        });
    });

    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape') {
            closeAllModals();
        }
    });
}