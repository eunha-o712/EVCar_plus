'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('myInfoForm');
    const editToggleBtn = document.getElementById('editToggleBtn');
    const cancelEditBtn = document.getElementById('cancelEditBtn');
    const editActionButtons = document.getElementById('editActionButtons');
    const editableInputs = form.querySelectorAll('.ev-editable');
    const editableRadios = form.querySelectorAll('.ev-editable-radio');
    const vehicleFields = document.getElementById('vehicleFields');
    const vehicleInputs = form.querySelectorAll('.ev-vehicle-input');
    const hasVehicleRadios = form.querySelectorAll('input[name="hasVehicle"]');

    const initialValues = new Map();
    let editMode = false;

    const saveInitialValues = () => {
        editableInputs.forEach((input) => {
            initialValues.set(input.id, input.value);
        });

        const checkedRadio = form.querySelector('input[name="hasVehicle"]:checked');
        initialValues.set('hasVehicle', checkedRadio ? checkedRadio.value : '');
    };

    const getSelectedHasVehicleValue = () => {
        const checkedRadio = form.querySelector('input[name="hasVehicle"]:checked');
        return checkedRadio ? checkedRadio.value.toLowerCase() : '';
    };

    const clearVehicleInputs = () => {
        vehicleInputs.forEach((input) => {
            input.value = '';
        });
    };

    const updateVehicleFieldsState = () => {
        const hasVehicle = getSelectedHasVehicleValue();
        const vehicleEnabled = editMode && hasVehicle === 'yes';

        vehicleFields.classList.toggle('ev-vehicle-disabled', !vehicleEnabled);

        vehicleInputs.forEach((input) => {
            input.readOnly = !vehicleEnabled;
            input.classList.toggle('ev-edit-mode', vehicleEnabled);

            if (!vehicleEnabled) {
                input.value = '';
            }
        });
    };

    const setEditMode = (enabled) => {
        editMode = enabled;

        editableInputs.forEach((input) => {
            if (input.tagName === 'SELECT') {
                input.disabled = !enabled;
            } else {
                input.readOnly = !enabled;
            }

            input.classList.toggle('ev-edit-mode', enabled);
        });

        editableRadios.forEach((radio) => {
            radio.disabled = !enabled;
        });

        editActionButtons.classList.toggle('ev-form-actions--hidden', !enabled);
        editToggleBtn.style.display = enabled ? 'none' : 'inline-flex';

        updateVehicleFieldsState();
    };

    const restoreInitialValues = () => {
        editableInputs.forEach((input) => {
            const initialValue = initialValues.get(input.id);

            if (initialValue !== undefined) {
                input.value = initialValue;
            }
        });

        const initialHasVehicle = initialValues.get('hasVehicle');
        hasVehicleRadios.forEach((radio) => {
            radio.checked = radio.value === initialHasVehicle;
        });

        updateVehicleFieldsState();
    };

    editToggleBtn.addEventListener('click', () => {
        saveInitialValues();
        setEditMode(true);
    });

    cancelEditBtn.addEventListener('click', () => {
        restoreInitialValues();
        setEditMode(false);
    });

    hasVehicleRadios.forEach((radio) => {
        radio.addEventListener('change', () => {
            const hasVehicle = getSelectedHasVehicleValue();

            if (hasVehicle !== 'yes') {
                clearVehicleInputs();
            }

            updateVehicleFieldsState();
        });
    });

    form.addEventListener('submit', (event) => {
        const requiredTargets = [
            { id: 'name', message: '이름을 입력해주세요.' },
            { id: 'birthDate', message: '생년월일을 입력해주세요.' },
            { id: 'gender', message: '성별을 선택해주세요.' },
            { id: 'phone', message: '전화번호를 입력해주세요.' },
            { id: 'address', message: '주소를 입력해주세요.' },
            { id: 'addressDetail', message: '상세 주소를 입력해주세요.' },
            { id: 'email', message: '이메일을 입력해주세요.' }
        ];

        for (const target of requiredTargets) {
            const input = document.getElementById(target.id);

            if (!input || !String(input.value).trim()) {
                event.preventDefault();
                alert(target.message);
                input.focus();
                return;
            }
        }

        const hasVehicle = getSelectedHasVehicleValue();

        if (!hasVehicle) {
            event.preventDefault();
            alert('차량소유여부를 선택해주세요.');
            return;
        }

        if (hasVehicle !== 'yes') {
            clearVehicleInputs();
        }
    });

    saveInitialValues();
    setEditMode(false);
});