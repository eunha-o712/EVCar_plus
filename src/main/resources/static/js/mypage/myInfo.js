'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('myInfoForm');
    const editModeButton = document.getElementById('editModeButton');
    const saveButton = document.getElementById('saveButton');
    const cancelButton = document.getElementById('cancelButton');

    const birthDateDisplay = document.getElementById('birthDateDisplay');
    const birthDateInput = document.getElementById('birthDateInput');

    const vehicleYearDisplay = document.getElementById('vehicleYearDisplay');
    const vehicleYearInput = document.getElementById('vehicleYearInput');

    if (
        !form ||
        !editModeButton ||
        !saveButton ||
        !cancelButton ||
        !birthDateDisplay ||
        !birthDateInput ||
        !vehicleYearDisplay ||
        !vehicleYearInput
    ) {
        return;
    }

    const editableInputs = Array.from(form.querySelectorAll('[data-editable="true"]'));
    const hasVehicleInputs = Array.from(form.querySelectorAll('input[name="hasVehicle"]'));
    const ownedVehicleFields = Array.from(form.querySelectorAll('.ev-vehicle-owned-field'));
    const editableGroups = Array.from(form.querySelectorAll('[data-editable-group]'));
    const fixedRadioGroups = Array.from(form.querySelectorAll('.ev-myinfo-radio-group--fixed'));

    const initialValues = new Map();
    const initialPlaceholders = new Map();

    const isOwnedVehicleField = (input) => input.classList.contains('ev-vehicle-owned-field');

    const pad2 = (value) => String(value).padStart(2, '0');

    const normalizeDateValue = (value) => {
        if (!value) {
            return '';
        }

        const trimmed = String(value).trim();

        if (/^\d{4}-\d{2}-\d{2}$/.test(trimmed)) {
            return trimmed;
        }

        const numericParts = trimmed.match(/\d+/g);

        if (!numericParts || numericParts.length < 3) {
            return '';
        }

        let [year, month, day] = numericParts;

        if (year.length === 2) {
            year = Number(year) >= 30 ? `19${year}` : `20${year}`;
        }

        return `${year}-${pad2(month)}-${pad2(day)}`;
    };

    const normalizeMonthValue = (value) => {
        if (!value) {
            return '';
        }

        const trimmed = String(value).trim();

        if (/^\d{4}-\d{2}$/.test(trimmed)) {
            return trimmed;
        }

        const numericParts = trimmed.match(/\d+/g);

        if (!numericParts || numericParts.length < 2) {
            return '';
        }

        let [year, month] = numericParts;

        if (year.length === 2) {
            year = Number(year) >= 30 ? `19${year}` : `20${year}`;
        }

        return `${year}-${pad2(month)}`;
    };

    const formatDateDisplayValue = (value) => {
        return normalizeDateValue(value);
    };

    const formatMonthDisplayValue = (value) => {
        const normalized = normalizeMonthValue(value);

        if (!normalized) {
            return '';
        }

        const [year, month] = normalized.split('-');
        return `${year}-${month}`;
    };

    const syncBirthDateInputFromDisplay = () => {
        const normalized = normalizeDateValue(birthDateDisplay.value);
        birthDateInput.value = normalized;
        birthDateDisplay.value = normalized;
    };

    const syncBirthDateDisplayFromInput = () => {
        const normalized = normalizeDateValue(birthDateInput.value);
        birthDateInput.value = normalized;
        birthDateDisplay.value = normalized;
    };

    const syncVehicleYearInputFromDisplay = () => {
        const normalized = normalizeMonthValue(vehicleYearDisplay.value);
        vehicleYearInput.value = normalized;
        vehicleYearDisplay.value = formatMonthDisplayValue(normalized);
    };

    const syncVehicleYearDisplayFromInput = () => {
        const normalized = normalizeMonthValue(vehicleYearInput.value);
        vehicleYearInput.value = normalized;
        vehicleYearDisplay.value = formatMonthDisplayValue(normalized);
    };

    const saveInitialValues = () => {
        editableInputs.forEach((input) => {
            initialValues.set(input.name, input.value ?? '');
            initialPlaceholders.set(input.name, input.placeholder || '');
        });

        hasVehicleInputs.forEach((input) => {
            initialValues.set(`hasVehicle:${input.value}`, input.checked);
        });

        initialValues.set('birthDateDisplay', birthDateDisplay.value ?? '');
        initialValues.set('vehicleYearDisplay', vehicleYearDisplay.value ?? '');
    };

    const restoreInitialValues = () => {
        editableInputs.forEach((input) => {
            input.value = initialValues.get(input.name) ?? '';
            input.placeholder = initialPlaceholders.get(input.name) ?? '';
        });

        hasVehicleInputs.forEach((input) => {
            input.checked = Boolean(initialValues.get(`hasVehicle:${input.value}`));
        });

        birthDateDisplay.value = initialValues.get('birthDateDisplay') ?? '';
        vehicleYearDisplay.value = initialValues.get('vehicleYearDisplay') ?? '';

        syncBirthDateInputFromDisplay();
        syncVehicleYearInputFromDisplay();
    };

    const setBirthDateEditMode = (editing) => {
        if (editing) {
            syncBirthDateInputFromDisplay();
            birthDateDisplay.classList.add('ev-myinfo-hidden');
            birthDateInput.classList.remove('ev-myinfo-hidden');
            birthDateInput.disabled = false;
            birthDateInput.readOnly = false;
            return;
        }

        syncBirthDateDisplayFromInput();
        birthDateDisplay.classList.remove('ev-myinfo-hidden');
        birthDateInput.classList.add('ev-myinfo-hidden');
        birthDateInput.disabled = true;
        birthDateInput.readOnly = true;
    };

    const setVehicleYearEditMode = (editing) => {
        if (editing) {
            syncVehicleYearInputFromDisplay();
            vehicleYearDisplay.classList.add('ev-myinfo-hidden');
            vehicleYearInput.classList.remove('ev-myinfo-hidden');
            vehicleYearInput.disabled = false;
            vehicleYearInput.readOnly = false;
            return;
        }

        syncVehicleYearDisplayFromInput();
        vehicleYearDisplay.classList.remove('ev-myinfo-hidden');
        vehicleYearInput.classList.add('ev-myinfo-hidden');
        vehicleYearInput.disabled = true;
        vehicleYearInput.readOnly = true;
    };

    const getHasVehicleValue = () => {
        const checkedInput = form.querySelector('input[name="hasVehicle"]:checked');
        return checkedInput ? checkedInput.value : '';
    };

    const hasVehicleSelected = () => {
        const value = getHasVehicleValue();
        return value === 'yes' || value === 'Y';
    };

    const clearOwnedVehicleFields = () => {
        ownedVehicleFields.forEach((field) => {
            field.value = '';
        });

        vehicleYearDisplay.value = '';
        vehicleYearInput.value = '';
    };

    const restoreOwnedVehicleFields = () => {
        ownedVehicleFields.forEach((field) => {
            field.value = initialValues.get(field.name) ?? '';
            field.placeholder = initialPlaceholders.get(field.name) ?? '';
        });

        vehicleYearDisplay.value = initialValues.get('vehicleYearDisplay') ?? '';
        syncVehicleYearInputFromDisplay();
    };

    const updateOwnedVehicleFields = (editing) => {
        if (!editing) {
            ownedVehicleFields.forEach((field) => {
                if (field === vehicleYearInput) {
                    return;
                }

                field.readOnly = true;
                field.disabled = false;
            });

            setVehicleYearEditMode(false);
            return;
        }

        if (!hasVehicleSelected()) {
            clearOwnedVehicleFields();

            ownedVehicleFields.forEach((field) => {
                if (field === vehicleYearInput) {
                    return;
                }

                field.readOnly = true;
                field.disabled = true;
            });

            vehicleYearDisplay.classList.remove('ev-myinfo-hidden');
            vehicleYearInput.classList.add('ev-myinfo-hidden');
            vehicleYearInput.disabled = true;
            vehicleYearInput.readOnly = true;
            return;
        }

        restoreOwnedVehicleFields();

        ownedVehicleFields.forEach((field) => {
            if (field === vehicleYearInput) {
                return;
            }

            field.readOnly = false;
            field.disabled = false;
        });

        setVehicleYearEditMode(true);
    };

    const setEditableInputState = (editing) => {
        editableInputs.forEach((input) => {
            if (
                isOwnedVehicleField(input) ||
                input === birthDateInput ||
                input === vehicleYearInput
            ) {
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

        setBirthDateEditMode(editing);
        updateOwnedVehicleFields(editing);
    };

    const setButtonState = (editing) => {
        editModeButton.classList.toggle('ev-myinfo-hidden', editing);
        saveButton.classList.toggle('ev-myinfo-hidden', !editing);
        cancelButton.classList.toggle('ev-myinfo-hidden', !editing);
    };

    const setEditMode = (editing) => {
        setEditableInputState(editing);
        setButtonState(editing);
    };

    const validateRequiredFields = () => {
        const phoneInput = form.querySelector('input[name="phone"]');
        const addressInput = form.querySelector('input[name="address"]');
        const emailInput = form.querySelector('input[name="email"]');

        syncBirthDateInputFromDisplay();

        if (!birthDateInput.value) {
            window.alert('생년월일을 입력해주세요.');
            birthDateInput.focus();
            return false;
        }

        if (phoneInput && !phoneInput.value.trim()) {
            window.alert('전화번호를 입력해주세요.');
            phoneInput.focus();
            return false;
        }

        if (addressInput && !addressInput.value.trim()) {
            window.alert('주소를 입력해주세요.');
            addressInput.focus();
            return false;
        }

        if (emailInput && !emailInput.value.trim()) {
            window.alert('이메일을 입력해주세요.');
            emailInput.focus();
            return false;
        }

        return true;
    };

    const validatePasswordChange = () => {
        const currentPasswordInput = form.querySelector('input[name="currentPassword"]');
        const newPasswordInput = form.querySelector('input[name="newPassword"]');
        const newPasswordConfirmInput = form.querySelector('input[name="newPasswordConfirm"]');

        const hasPasswordInput =
            (currentPasswordInput && currentPasswordInput.value.trim()) ||
            (newPasswordInput && newPasswordInput.value.trim()) ||
            (newPasswordConfirmInput && newPasswordConfirmInput.value.trim());

        if (!hasPasswordInput) {
            return true;
        }

        if (!currentPasswordInput.value.trim()) {
            window.alert('현재 비밀번호를 입력해주세요.');
            currentPasswordInput.focus();
            return false;
        }

        if (!newPasswordInput.value.trim()) {
            window.alert('새 비밀번호를 입력해주세요.');
            newPasswordInput.focus();
            return false;
        }

        if (!newPasswordConfirmInput.value.trim()) {
            window.alert('새 비밀번호 확인을 입력해주세요.');
            newPasswordConfirmInput.focus();
            return false;
        }

        if (newPasswordInput.value !== newPasswordConfirmInput.value) {
            window.alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
            newPasswordConfirmInput.focus();
            return false;
        }

        return true;
    };

    const prepareOwnedVehicleValuesForSubmit = () => {
        if (!hasVehicleSelected()) {
            clearOwnedVehicleFields();
        }

        syncVehicleYearInputFromDisplay();

        ownedVehicleFields.forEach((field) => {
            field.disabled = false;
            field.readOnly = false;
        });
    };

    const enableAllInputsBeforeSubmit = () => {
        form.querySelectorAll('input').forEach((input) => {
            input.disabled = false;
        });
    };

    hasVehicleInputs.forEach((input) => {
        input.addEventListener('change', () => {
            if (!input.disabled) {
                updateOwnedVehicleFields(true);
            }
        });
    });

    birthDateInput.addEventListener('change', syncBirthDateDisplayFromInput);
    vehicleYearInput.addEventListener('change', syncVehicleYearDisplayFromInput);

    editModeButton.addEventListener('click', () => {
        saveInitialValues();
        setEditMode(true);
    });

    cancelButton.addEventListener('click', () => {
        restoreInitialValues();
        setEditMode(false);
    });

    form.addEventListener('submit', (event) => {
        if (!validateRequiredFields()) {
            event.preventDefault();
            return;
        }

        if (!validatePasswordChange()) {
            event.preventDefault();
            return;
        }

        prepareOwnedVehicleValuesForSubmit();
        syncBirthDateInputFromDisplay();
        syncBirthDateDisplayFromInput();
        syncVehicleYearInputFromDisplay();
        syncVehicleYearDisplayFromInput();
        enableAllInputsBeforeSubmit();
    });

    birthDateDisplay.value = formatDateDisplayValue(birthDateDisplay.value);
    syncBirthDateInputFromDisplay();

    vehicleYearDisplay.value = formatMonthDisplayValue(vehicleYearDisplay.value);
    syncVehicleYearInputFromDisplay();

    saveInitialValues();
    setEditMode(false);
});