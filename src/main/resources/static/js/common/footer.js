'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const policyButtons = document.querySelectorAll('[data-policy-tab]');
    const policyPanels = document.querySelectorAll('[data-policy-panel]');

    if (!policyButtons.length || !policyPanels.length) {
        return;
    }

    const closeAllPanels = () => {
        policyButtons.forEach((button) => {
            button.classList.remove('is-active');
        });

        policyPanels.forEach((panel) => {
            panel.hidden = true;
        });
    };

    policyButtons.forEach((button) => {
        button.addEventListener('click', () => {
            const target = button.getAttribute('data-policy-tab');
            const targetPanel = document.querySelector(`[data-policy-panel="${target}"]`);
            const isActive = button.classList.contains('is-active');

            closeAllPanels();

            if (isActive || !targetPanel) {
                return;
            }

            button.classList.add('is-active');
            targetPanel.hidden = false;
        });
    });
});