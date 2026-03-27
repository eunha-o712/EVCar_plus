'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('myInfoForm');
    const editModeButton = document.getElementById('editModeButton');
    const saveButton = document.getElementById('saveButton');
    const cancelButton = document.getElementById('cancelButton');

    if (!form || !editModeButton || !saveButton || !cancelButton) {
        return;
    }

    const editableInputs = form.querySelectorAll('[data-editable="true"]');
    const hasVehicleInputs = form.querySelectorAll('input[name="hasVehicle"]');
    const ownedVehicleFields = form.querySelectorAll('.ev-vehicle-owned-field');
    const editableGroups = form.querySelectorAll('[data-editable-group]');
    const fixedRadioGroups = form.querySelectorAll('.ev-myinfo-radio-group--fixed');

    const initialValues = new Map();
    const initialPlaceholders = new Map();

    const saveInitialValues = () => {
        editableInputs.forEach((input) => {
            initialValues.set(input.name, input.value);
            initialPlaceholders.set(input.name, input.placeholder || '');
        });

        hasVehicleInputs.forEach((input) => {
            initialValues.set(`hasVehicle:${input.value}`, input.checked);
        });
    };

    const restoreInitialValues = () => {
        editableInputs.forEach((input) => {
            input.value = initialValues.get(input.name) ?? '';
            input.placeholder = initialPlaceholders.get(input.name) ?? '';
        });

        hasVehicleInputs.forEach((input) => {
            input.checked = Boolean(initialValues.get(`hasVehicle:${input.value}`));
        });
    };

    const getHasVehicleValue = () => {
        const checkedInput = form.querySelector('input[name="hasVehicle"]:checked');
        return checkedInput ? checkedInput.value : '';
    };

    const clearOwnedVehicleFields = () => {
        ownedVehicleFields.forEach((field) => {
            field.value = '';
            field.placeholder = '';
        });
    };

    const restoreOwnedVehiclePlaceholders = () => {
        ownedVehicleFields.forEach((field) => {
            field.placeholder = initialPlaceholders.get(field.name) ?? '';
        });
    };

    const updateOwnedVehicleFields = (editing) => {
        const hasVehicleValue = getHasVehicleValue();
        const hasVehicle = hasVehicleValue === 'yes' || hasVehicleValue === 'Y';

        if (!hasVehicle) {
            clearOwnedVehicleFields();

            ownedVehicleFields.forEach((field) => {
                field.readOnly = true;
                field.disabled = true;
            });
            return;
        }

        restoreOwnedVehiclePlaceholders();

        ownedVehicleFields.forEach((field) => {
            field.readOnly = !editing;
            field.disabled = !editing;
        });
    };

    const setEditMode = (editing) => {
        editableInputs.forEach((input) => {
            if (input.classList.contains('ev-vehicle-owned-field')) {
                return;
            }

            input.readOnly = !editing;
            input.disabled = false;
        });

        hasVehicleInputs.forEach((input) => {
            input.disabled = !editing;
        });

        editableGroups.forEach((group) => {
            group.classList.toggle('is-disabled', !editing);
        });

        fixedRadioGroups.forEach((group) => {
            group.classList.add('is-disabled');
        });

        updateOwnedVehicleFields(editing);

        editModeButton.classList.toggle('ev-myinfo-hidden', editing);
        saveButton.classList.toggle('ev-myinfo-hidden', !editing);
        cancelButton.classList.toggle('ev-myinfo-hidden', !editing);
    };

    hasVehicleInputs.forEach((input) => {
        input.addEventListener('change', () => {
            if (!input.disabled) {
                updateOwnedVehicleFields(true);
            }
        });
    });

    editModeButton.addEventListener('click', () => {
        saveInitialValues();
        setEditMode(true);
    });

    cancelButton.addEventListener('click', () => {
        restoreInitialValues();
        setEditMode(false);
    });

    form.addEventListener('submit', (event) => {
        const phoneInput = form.querySelector('input[name="phone"]');
        const addressInput = form.querySelector('input[name="address"]');
        const emailInput = form.querySelector('input[name="email"]');
        const currentPasswordInput = form.querySelector('input[name="currentPassword"]');
        const newPasswordInput = form.querySelector('input[name="newPassword"]');
        const newPasswordConfirmInput = form.querySelector('input[name="newPasswordConfirm"]');

        if (phoneInput && !phoneInput.value.trim()) {
            event.preventDefault();
            window.alert('전화번호를 입력해주세요.');
            phoneInput.focus();
            return;
        }

        if (addressInput && !addressInput.value.trim()) {
            event.preventDefault();
            window.alert('주소를 입력해주세요.');
            addressInput.focus();
            return;
        }

        if (emailInput && !emailInput.value.trim()) {
            event.preventDefault();
            window.alert('이메일을 입력해주세요.');
            emailInput.focus();
            return;
        }

        const hasPasswordInput =
            (currentPasswordInput && currentPasswordInput.value.trim()) ||
            (newPasswordInput && newPasswordInput.value.trim()) ||
            (newPasswordConfirmInput && newPasswordConfirmInput.value.trim());

        if (hasPasswordInput) {
            if (!currentPasswordInput.value.trim()) {
                event.preventDefault();
                window.alert('현재 비밀번호를 입력해주세요.');
                currentPasswordInput.focus();
                return;
            }

            if (!newPasswordInput.value.trim()) {
                event.preventDefault();
                window.alert('새 비밀번호를 입력해주세요.');
                newPasswordInput.focus();
                return;
            }

            if (!newPasswordConfirmInput.value.trim()) {
                event.preventDefault();
                window.alert('새 비밀번호 확인을 입력해주세요.');
                newPasswordConfirmInput.focus();
                return;
            }

            if (newPasswordInput.value !== newPasswordConfirmInput.value) {
                event.preventDefault();
                window.alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
                newPasswordConfirmInput.focus();
                return;
            }
        }

        const hasVehicleValue = getHasVehicleValue();

        if (hasVehicleValue === 'no' || hasVehicleValue === 'N') {
            clearOwnedVehicleFields();

            ownedVehicleFields.forEach((field) => {
                field.disabled = false;
                field.readOnly = false;
            });
        }
    });

    saveInitialValues();
    setEditMode(false);
});