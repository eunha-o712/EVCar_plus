'use strict';

let isIdChecked = false;
let isEmailChecked = false;

document.addEventListener('DOMContentLoaded', () => {
    initCountryCode();
    initDateLimit();
    initPasswordConfirm();
    initPhoneFormat();
    initEmailDomainSelect();
    initVehicleInputs();
    initDuplicateReset();
    initButtons();
});

function initCountryCode() {
    const countrySelect = document.getElementById('countryCode');

    if (!countrySelect || typeof countryCodes === 'undefined') {
        return;
    }

    countryCodes.forEach((country) => {
        const option = document.createElement('option');
        option.value = country.code;
        option.textContent = `${country.name} (+${country.code})`;
        countrySelect.appendChild(option);
    });

    countrySelect.value = '82';
}

function initDateLimit() {
    const birthDate = document.getElementById('birthDate');
    const carYear = document.getElementById('carYear');

    const today = new Date().toISOString().split('T')[0];
    const currentMonth = new Date().toISOString().slice(0, 7);

    if (birthDate) {
        birthDate.setAttribute('max', today);
    }

    if (carYear) {
        carYear.setAttribute('max', currentMonth);
    }
}

function initPasswordConfirm() {
    const password = document.getElementById('password');
    const passwordConfirm = document.getElementById('passwordConfirm');
    const message = document.getElementById('pwMsg');

    if (!password || !passwordConfirm || !message) {
        return;
    }

    passwordConfirm.addEventListener('keyup', () => {
        if (!passwordConfirm.value) {
            message.textContent = '';
            return;
        }

        if (password.value === passwordConfirm.value) {
            message.textContent = '비밀번호가 일치합니다.';
            message.style.color = '#047857';
        } else {
            message.textContent = '비밀번호가 일치하지 않습니다.';
            message.style.color = '#ef4444';
        }
    });
}

function initPhoneFormat() {
    const phoneInput = document.getElementById('phone');
    const countryCode = document.getElementById('countryCode');
    const fullPhone = document.getElementById('fullPhone');

    if (!phoneInput || !countryCode || !fullPhone) {
        return;
    }

    phoneInput.addEventListener('input', () => {
        const formattedPhone = formatPhone(phoneInput.value);
        phoneInput.value = formattedPhone;
        fullPhone.value = `${countryCode.value}-${formattedPhone}`;
    });

    countryCode.addEventListener('change', () => {
        fullPhone.value = `${countryCode.value}-${phoneInput.value}`;
    });
}

function formatPhone(value) {
    let numbers = value.replace(/[^0-9]/g, '');

    if (numbers.length > 11) {
        numbers = numbers.substring(0, 11);
    }

    if (numbers.length <= 3) {
        return numbers;
    }

    if (numbers.length <= 7) {
        return `${numbers.slice(0, 3)}-${numbers.slice(3)}`;
    }

    return `${numbers.slice(0, 3)}-${numbers.slice(3, 7)}-${numbers.slice(7, 11)}`;
}

function initEmailDomainSelect() {
    const domainSelect = document.getElementById('domainSelect');
    const domainInput = document.getElementById('emailDomain');

    if (!domainSelect || !domainInput) {
        return;
    }

    domainSelect.addEventListener('change', () => {
        domainInput.value = domainSelect.value;
        domainInput.readOnly = domainSelect.value !== '';
        isEmailChecked = false;
    });
}

function initVehicleInputs() {
    const carName = document.getElementById('carName');
    const carYear = document.getElementById('carYear');
    const carMileage = document.getElementById('carMileage');

    if (!carName || !carYear || !carMileage) {
        return;
    }

    carYear.disabled = true;
    carMileage.disabled = true;

    carName.addEventListener('input', () => {
        const hasValue = carName.value.trim() !== '';

        carYear.disabled = !hasValue;
        carMileage.disabled = !hasValue;

        if (!hasValue) {
            carYear.value = '';
            carMileage.value = '';
        }
    });
}

function initDuplicateReset() {
    const loginId = document.getElementById('loginId');
    const emailId = document.getElementById('emailId');
    const emailDomain = document.getElementById('emailDomain');

    if (loginId) {
        loginId.addEventListener('input', () => {
            isIdChecked = false;
            clearMessage('idMsg');
        });
    }

    if (emailId) {
        emailId.addEventListener('input', () => {
            isEmailChecked = false;
            clearMessage('emailMsg');
        });
    }

    if (emailDomain) {
        emailDomain.addEventListener('input', () => {
            isEmailChecked = false;
            clearMessage('emailMsg');
        });
    }
}

function initButtons() {
    const checkIdButton = document.getElementById('checkIdBtn');
    const checkEmailButton = document.getElementById('checkEmailBtn');
    const addressSearchButton = document.getElementById('addressSearchBtn');
    const submitButton = document.getElementById('signupSubmitBtn');
    const backButton = document.getElementById('signupBackBtn');

    if (checkIdButton) {
        checkIdButton.addEventListener('click', checkId);
    }

    if (checkEmailButton) {
        checkEmailButton.addEventListener('click', checkEmail);
    }

    if (addressSearchButton) {
        addressSearchButton.addEventListener('click', searchAddress);
    }

    if (submitButton) {
        submitButton.addEventListener('click', submitSignupForm);
    }

    if (backButton) {
        backButton.addEventListener('click', () => {
            history.back();
        });
    }
}

function searchAddress() {
    if (typeof daum === 'undefined') {
        alert('주소 검색 API를 불러오지 못했습니다.');
        return;
    }

    new daum.Postcode({
        oncomplete: (data) => {
            document.getElementById('address').value = data.address;
        }
    }).open();
}

async function checkId() {
    const loginIdInput = document.getElementById('loginId');
    const message = document.getElementById('idMsg');

    if (!loginIdInput || !message) {
        return;
    }

    const loginId = loginIdInput.value.trim();
    const idRegex = /^[a-zA-Z0-9]{8,20}$/;

    if (!loginId) {
        setMessage(message, '아이디를 입력하세요.', false);
        return;
    }

    if (!idRegex.test(loginId)) {
        setMessage(message, '아이디는 영문/숫자 8~20자로 입력하세요.', false);
        return;
    }

    try {
        const response = await fetch(`/signup/check-id?loginId=${encodeURIComponent(loginId)}`);
        const text = await response.text();
        const isDuplicate = text.trim() === 'true';

        if (isDuplicate) {
            setMessage(message, '이미 사용중인 아이디입니다.', false);
            isIdChecked = false;
            return;
        }

        setMessage(message, '사용 가능한 아이디입니다.', true);
        isIdChecked = true;
    } catch (error) {
        setMessage(message, '아이디 중복확인 중 오류가 발생했습니다.', false);
        isIdChecked = false;
    }
}

async function checkEmail() {
    const emailIdInput = document.getElementById('emailId');
    const emailDomainInput = document.getElementById('emailDomain');
    const message = document.getElementById('emailMsg');

    if (!emailIdInput || !emailDomainInput || !message) {
        return;
    }

    const emailId = emailIdInput.value.trim();
    const emailDomain = emailDomainInput.value.trim();
    const email = `${emailId}@${emailDomain}`;

    if (!emailId || !emailDomain) {
        setMessage(message, '이메일을 입력하세요.', false);
        return;
    }

    if (!validateEmailFormat(emailId, emailDomain)) {
        setMessage(message, '이메일 형식을 확인하세요.', false);
        return;
    }

    try {
        const response = await fetch(`/signup/check-email?email=${encodeURIComponent(email)}`);
        const text = await response.text();
        const isDuplicate = text.trim() === 'true';

        if (isDuplicate) {
            setMessage(message, '이미 사용중인 이메일입니다.', false);
            isEmailChecked = false;
            return;
        }

        setMessage(message, '사용 가능한 이메일입니다.', true);
        isEmailChecked = true;
    } catch (error) {
        setMessage(message, '이메일 중복확인 중 오류가 발생했습니다.', false);
        isEmailChecked = false;
    }
}

function submitSignupForm() {
    const form = document.getElementById('signupForm');

    if (!form) {
        return;
    }

    if (!validateSignupForm()) {
        return;
    }

    form.submit();
}

function validateSignupForm() {
    const loginId = getValue('loginId');
    const password = getValue('password');
    const passwordConfirm = getValue('passwordConfirm');
    const name = getValue('name');
    const birthDate = getValue('birthDate');
    const gender = document.querySelector('input[name="gender"]:checked');
    const phone = getValue('phone');
    const address = getValue('address');
    const emailId = getValue('emailId');
    const emailDomain = getValue('emailDomain');
    const carYear = getValue('carYear');

    const idRegex = /^[a-zA-Z0-9]{8,20}$/;
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$/;
    const nameRegex = /^[가-힣a-zA-Z0-9]{1,50}$/;

    if (!loginId) return alertAndFocus('아이디를 입력하세요.', 'loginId');
    if (!idRegex.test(loginId)) return alertAndFocus('아이디는 영문/숫자 8~20자로 입력하세요.', 'loginId');

    if (!password) return alertAndFocus('비밀번호를 입력하세요.', 'password');
    if (!passwordRegex.test(password)) return alertAndFocus('비밀번호는 영문, 숫자를 포함한 8~20자로 입력하세요.', 'password');

    if (!passwordConfirm) return alertAndFocus('비밀번호 확인을 입력하세요.', 'passwordConfirm');
    if (password !== passwordConfirm) return alertAndFocus('비밀번호가 일치하지 않습니다.', 'passwordConfirm');

    if (!name) return alertAndFocus('이름을 입력하세요.', 'name');
    if (!nameRegex.test(name)) return alertAndFocus('이름은 한글, 영문, 숫자 1~50자로 입력하세요.', 'name');

    if (!birthDate) return alertAndFocus('생년월일을 입력하세요.', 'birthDate');

    const today = new Date().toISOString().split('T')[0];
    if (birthDate > today) return alertAndFocus('생년월일은 미래 날짜를 선택할 수 없습니다.', 'birthDate');

    if (!gender) {
        alert('성별을 선택하세요.');
        return false;
    }

    if (!phone) return alertAndFocus('전화번호를 입력하세요.', 'phone');
    if (!address) return alertAndFocus('주소를 입력하세요.', 'address');

    if (!emailId || !emailDomain) return alertAndFocus('이메일을 입력하세요.', 'emailId');
    if (!validateEmailFormat(emailId, emailDomain)) return alertAndFocus('이메일 형식을 확인하세요.', 'emailId');

    if (!isIdChecked) {
        alert('아이디 중복확인을 해주세요.');
        return false;
    }

    if (!isEmailChecked) {
        alert('이메일 중복확인을 해주세요.');
        return false;
    }

    if (carYear) {
        const currentMonth = new Date().toISOString().slice(0, 7);

        if (carYear > currentMonth) {
            return alertAndFocus('보유차종 연식은 미래 날짜를 선택할 수 없습니다.', 'carYear');
        }
    }

    document.getElementById('emailFull').value = `${emailId}@${emailDomain}`;
    document.getElementById('fullPhone').value = `${getValue('countryCode')}-${phone}`;

    return true;
}

function validateEmailFormat(emailId, emailDomain) {
    const emailIdRegex = /^[a-zA-Z0-9]+$/;
    const emailDomainRegex = /^[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[a-zA-Z]+(\.[a-zA-Z]+)*$/;
    const email = `${emailId}@${emailDomain}`;

    return emailIdRegex.test(emailId) && emailDomainRegex.test(emailDomain) && emailRegex.test(email);
}

function getValue(id) {
    const element = document.getElementById(id);

    if (!element) {
        return '';
    }

    return element.value.trim();
}

function alertAndFocus(message, id) {
    alert(message);

    const element = document.getElementById(id);

    if (element) {
        element.focus();
    }

    return false;
}

function setMessage(element, message, success) {
    element.textContent = message;
    element.style.color = success ? '#047857' : '#ef4444';
}

function clearMessage(id) {
    const message = document.getElementById(id);

    if (message) {
        message.textContent = '';
    }
}