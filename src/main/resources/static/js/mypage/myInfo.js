'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('myInfoForm');
    const editModeButton = document.getElementById('editModeButton');
    const saveButton = document.getElementById('saveButton');
    const cancelButton = document.getElementById('cancelButton');

    if (!form || !editModeButton || !saveButton || !cancelButton) {
        return;
    }

    const editableInputs = Array.from(form.querySelectorAll('[data-editable="true"]'));
    const genderInputs = Array.from(form.querySelectorAll('input[name="gender"]'));
    const hasVehicleInputs = Array.from(form.querySelectorAll('input[name="hasVehicle"]'));
    const ownedVehicleFields = Array.from(form.querySelectorAll('.ev-vehicle-owned-field'));

    const initialValues = new Map();
    const MIN_VEHICLE_YEAR = 1900;

    const saveInitialValues = () => {
        form.querySelectorAll('input').forEach((input) => {
            if (input.type === 'radio') {
                initialValues.set(`${input.name}:${input.value}`, input.checked);
            } else {
                initialValues.set(input.name, input.value ?? '');
            }
        });
    };

    const restoreInitialValues = () => {
        form.querySelectorAll('input').forEach((input) => {
            if (input.type === 'radio') {
                input.checked = Boolean(initialValues.get(`${input.name}:${input.value}`));
            } else {
                input.value = initialValues.get(input.name) ?? '';
            }
        });

        updateOwnedVehicleFieldsState();
    };

    const getHasVehicleValue = () => {
        const checked = form.querySelector('input[name="hasVehicle"]:checked');
        return checked ? checked.value : '';
    };

    const isVehicleOwned = () => {
        const value = getHasVehicleValue();
        return value === 'yes' || value === 'Y' || value === 'y';
    };

    const clearOwnedVehicleFields = () => {
        ownedVehicleFields.forEach((field) => {
            field.value = '';
        });
    };

    const updateOwnedVehicleFieldsState = () => {
        const editable = isVehicleOwned();
        const isEditing = !saveButton.classList.contains('ev-myinfo-hidden');

        ownedVehicleFields.forEach((field) => {
            if (!isEditing) {
                field.readOnly = true;
                field.disabled = false;
                return;
            }

            if (!editable) {
                field.value = '';
                field.readOnly = true;
                field.disabled = true;
                return;
            }

            field.readOnly = false;
            field.disabled = false;
        });
    };

    const setEditMode = (editing) => {
        editableInputs.forEach((input) => {
            if (ownedVehicleFields.includes(input)) {
                return;
            }

            input.readOnly = !editing;
            input.disabled = false;
        });

        genderInputs.forEach((input) => {
            input.disabled = true;
        });

        hasVehicleInputs.forEach((input) => {
            input.disabled = false;
        });

        editModeButton.classList.toggle('ev-myinfo-hidden', editing);
        saveButton.classList.toggle('ev-myinfo-hidden', !editing);
        cancelButton.classList.toggle('ev-myinfo-hidden', !editing);

        updateOwnedVehicleFieldsState();
    };

    const validateBasicFields = () => {
        const name = form.querySelector('input[name="name"]');
        const birthDate = form.querySelector('input[name="birthDate"]');
        const phone = form.querySelector('input[name="phone"]');
        const address = form.querySelector('input[name="address"]');
        const email = form.querySelector('input[name="email"]');

        if (name && !name.value.trim()) {
            window.alert('이름을 입력해주세요.');
            name.focus();
            return false;
        }

        if (birthDate && !birthDate.value.trim()) {
            window.alert('생년월일을 입력해주세요.');
            birthDate.focus();
            return false;
        }

        if (phone && !phone.value.trim()) {
            window.alert('전화번호를 입력해주세요.');
            phone.focus();
            return false;
        }

        if (address && !address.value.trim()) {
            window.alert('주소를 입력해주세요.');
            address.focus();
            return false;
        }

        if (email && !email.value.trim()) {
            window.alert('이메일을 입력해주세요.');
            email.focus();
            return false;
        }

        return true;
    };

    const validateOwnedVehicleFields = () => {
        if (!isVehicleOwned()) {
            return true;
        }

        const vehicleModel = form.querySelector('input[name="vehicleModel"]');
        const vehicleYear = form.querySelector('input[name="vehicleYear"]');
        const drivingDistance = form.querySelector('input[name="drivingDistance"]');
        const currentYear = new Date().getFullYear();

        if (vehicleModel && !vehicleModel.value.trim()) {
            window.alert('보유 차량명을 입력해주세요.');
            vehicleModel.focus();
            return false;
        }

        if (vehicleYear && !vehicleYear.value.trim()) {
            window.alert('보유차량 연식을 입력해주세요.');
            vehicleYear.focus();
            return false;
        }

        if (vehicleYear && !/^\d{4}$/.test(vehicleYear.value.trim())) {
            window.alert('보유차량 연식은 4자리 숫자(YYYY)로 입력해주세요.');
            vehicleYear.focus();
            return false;
        }

        if (vehicleYear) {
            const parsedYear = Number(vehicleYear.value.trim());

            if (Number.isNaN(parsedYear)) {
                window.alert('보유차량 연식은 숫자로 입력해주세요.');
                vehicleYear.focus();
                return false;
            }

            if (parsedYear < MIN_VEHICLE_YEAR) {
                window.alert('보유차량 연식이 올바르지 않습니다.');
                vehicleYear.focus();
                return false;
            }

            if (parsedYear > currentYear) {
                window.alert('보유차량 연식은 현재 연도보다 클 수 없습니다.');
                vehicleYear.focus();
                return false;
            }
        }

        if (drivingDistance && !drivingDistance.value.trim()) {
            window.alert('주행거리를 입력해주세요.');
            drivingDistance.focus();
            return false;
        }

        if (drivingDistance) {
            const parsedDistance = Number(drivingDistance.value);

            if (Number.isNaN(parsedDistance)) {
                window.alert('주행거리는 숫자로 입력해주세요.');
                drivingDistance.focus();
                return false;
            }

            if (parsedDistance < 0) {
                window.alert('주행거리는 0 이상이어야 합니다.');
                drivingDistance.focus();
                return false;
            }
        }

        return true;
    };

    const validatePasswordChange = () => {
        const currentPassword = form.querySelector('input[name="currentPassword"]');
        const newPassword = form.querySelector('input[name="newPassword"]');
        const newPasswordConfirm = form.querySelector('input[name="newPasswordConfirm"]');

        const hasAnyPasswordValue =
            (currentPassword && currentPassword.value.trim()) ||
            (newPassword && newPassword.value.trim()) ||
            (newPasswordConfirm && newPasswordConfirm.value.trim());

        if (!hasAnyPasswordValue) {
            return true;
        }

        if (!currentPassword.value.trim()) {
            window.alert('현재 비밀번호를 입력해주세요.');
            currentPassword.focus();
            return false;
        }

        if (!newPassword.value.trim()) {
            window.alert('새 비밀번호를 입력해주세요.');
            newPassword.focus();
            return false;
        }

        if (!newPasswordConfirm.value.trim()) {
            window.alert('새 비밀번호 확인을 입력해주세요.');
            newPasswordConfirm.focus();
            return false;
        }

        if (newPassword.value !== newPasswordConfirm.value) {
            window.alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
            newPasswordConfirm.focus();
            return false;
        }

        return true;
    };

    const prepareSubmit = () => {
        form.querySelectorAll('input').forEach((input) => {
            if (input.name === 'gender') {
                input.disabled = true;
                return;
            }

            input.disabled = false;
        });

        if (!isVehicleOwned()) {
            clearOwnedVehicleFields();
        }
    };

    hasVehicleInputs.forEach((input) => {
        input.addEventListener('change', () => {
            if (!isVehicleOwned()) {
                clearOwnedVehicleFields();
            }

            updateOwnedVehicleFieldsState();
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
        if (!validateBasicFields()) {
            event.preventDefault();
            return;
        }

        if (!validateOwnedVehicleFields()) {
            event.preventDefault();
            return;
        }

        if (!validatePasswordChange()) {
            event.preventDefault();
            return;
        }

        prepareSubmit();
    });

    saveInitialValues();
    setEditMode(false);
});