'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initVehicleRecommend();
});

function initVehicleRecommend() {
    const form = document.getElementById('vehicleRecommendForm');
    const userMessageInput = document.getElementById('userMessage');
    const resetButton = document.getElementById('recommendResetBtn');
    const optionButtons = document.querySelectorAll('.ev-recommend-option');

    if (!form || !userMessageInput || !optionButtons.length) {
        return;
    }

    const selectedValues = new Map();

    optionButtons.forEach((button) => {
        button.addEventListener('click', () => {
            const group = button.dataset.group;
            const value = button.dataset.value;

            if (!group || !value) {
                return;
            }

            document.querySelectorAll(`.ev-recommend-option[data-group="${group}"]`).forEach((groupButton) => {
                groupButton.classList.remove('is-selected');
            });

            button.classList.add('is-selected');
            selectedValues.set(group, value);
        });
    });

    if (resetButton) {
        resetButton.addEventListener('click', () => {
            selectedValues.clear();

            optionButtons.forEach((button) => {
                button.classList.remove('is-selected');
            });

            userMessageInput.value = '';
        });
    }

    form.addEventListener('submit', (event) => {
        const requiredGroups = ['budget', 'purpose', 'vehicleClass', 'priority'];
        const isComplete = requiredGroups.every((group) => selectedValues.has(group));

        if (!isComplete) {
            event.preventDefault();
            alert('모든 조건을 선택해 주세요.');
            return;
        }

        const message = [
            selectedValues.get('budget'),
            selectedValues.get('purpose'),
            selectedValues.get('vehicleClass'),
            selectedValues.get('priority')
        ].join(', ');

        userMessageInput.value = `${message} 조건에 맞는 전기차를 추천해 주세요.`;
    });
}