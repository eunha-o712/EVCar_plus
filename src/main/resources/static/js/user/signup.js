/* 비밀번호 확인 */
const pw = document.getElementById("password");
const pwConfirm = document.getElementById("passwordConfirm");
const msg = document.getElementById("pwMsg");

pwConfirm.addEventListener("keyup", function () {
    if (pw.value === pwConfirm.value) {
        msg.textContent = "비밀번호가 일치합니다.";
        msg.style.color = "green";
    } else {
        msg.textContent = "비밀번호가 일치하지 않습니다.";
        msg.style.color = "red";
    }
});

/* 전화번호 */
const phone = document.getElementById("phone");
const country = document.getElementById("countryCode");
const fullPhone = document.getElementById("fullPhone");

phone.addEventListener("input", function () {
    let numbers = this.value.replace(/[^0-9]/g, "");
    if (numbers.length > 11) numbers = numbers.substring(0, 11);
    let result = "";
    if (numbers.length <= 3) {
        result = numbers;
    } else if (numbers.length <= 7) {
        result = numbers.slice(0, 3) + "-" + numbers.slice(3);
    } else {
        result = numbers.slice(0, 3) + "-" + numbers.slice(3, 7) + "-" + numbers.slice(7, 11);
    }
    this.value = result;
    fullPhone.value = country.value + "-" + result;
});

country.addEventListener("change", function () {
    fullPhone.value = this.value + "-" + phone.value;
});

const countrySelect = document.getElementById("countryCode");
countryCodes.forEach(c => {
    const option = document.createElement("option");
    option.value = c.code;
    option.textContent = `${c.name} (+${c.code})`;
    countrySelect.appendChild(option);
});
countrySelect.value = "82";

/* 카카오 주소 검색 */
function searchAddress() {
    new daum.Postcode({
        oncomplete: function (data) {
            document.getElementById("address").value = data.address;
        }
    }).open();
}

/* 이메일 선택 */
const domainSelect = document.getElementById("domainSelect");
const domainInput = document.getElementById("emailDomain");

domainSelect.addEventListener("change", function () {
    if (this.value !== "") {
        domainInput.value = this.value;
        domainInput.readOnly = true;
    } else {
        domainInput.value = "";
        domainInput.readOnly = false;
    }
});

/* 이메일 합치기 */
document.querySelector("form").onsubmit = function () {
    const id = document.getElementById("emailId").value;
    const domain = document.getElementById("emailDomain").value;
    if (!id || !domain) {
        alert("이메일을 입력해주세요.");
        return false;
    }
    document.getElementById("emailFull").value = id + "@" + domain;
    return true;
};

/* 차량 활성화 */
const carName = document.getElementById("carName");
const carYear = document.getElementById("carYear");
const mileage = document.getElementById("carMileage");

carYear.disabled = true;
mileage.disabled = true;

carName.addEventListener("input", function () {
    const hasValue = this.value.trim() !== "";
    carYear.disabled = !hasValue;
    mileage.disabled = !hasValue;
    if (!hasValue) {
        carYear.value = "";
        mileage.value = "";
    }
});

/* 아이디 중복 체크 */
async function checkId() {
    const loginId = document.getElementById("loginId").value;
    const msg = document.getElementById("idMsg");
    if (!loginId) {
        msg.textContent = "아이디를 입력하세요.";
        msg.style.color = "red";
        return;
    }
    const res = await fetch(`/signup/check-id?loginId=${loginId}`);
    const text = await res.text();
    const isDuplicate = text.trim() === "true";
    if (isDuplicate) {
        msg.innerText = "이미 사용중인 아이디입니다.";
        msg.style.color = "red";
    } else {
        msg.innerText = "사용 가능한 아이디입니다.";
        msg.style.color = "green";
    }
}

/* 생년월일 */
const birth = document.getElementById("birthDate");
const picker = document.getElementById("birthPicker");

birth.addEventListener("input", function () {
    let numbers = this.value.replace(/[^0-9]/g, "");
    if (numbers.length > 8) numbers = numbers.substring(0, 8);
    let result = "";
    if (numbers.length <= 4) {
        result = numbers;
    } else if (numbers.length <= 6) {
        result = numbers.slice(0, 4) + "-" + numbers.slice(4);
    } else {
        result = numbers.slice(0, 4) + "-" + numbers.slice(4, 6) + "-" + numbers.slice(6, 8);
    }
    this.value = result;
    if (result.length === 10) picker.value = result;
});

/* 이메일 중복 체크 */
async function checkEmail() {
    const id = document.getElementById("emailId").value;
    const domain = document.getElementById("emailDomain").value;
    const msg = document.getElementById("emailMsg");
    if (!id || !domain) {
        msg.innerText = "이메일을 입력하세요.";
        msg.style.color = "red";
        return;
    }
    const email = id + "@" + domain;
    const res = await fetch(`/signup/check-email?email=${encodeURIComponent(email)}`);
    const text = await res.text();
    const isDuplicate = text.trim() === "true";
    if (isDuplicate) {
        msg.innerText = "이미 사용중인 이메일입니다.";
        msg.style.color = "red";
    } else {
        msg.innerText = "사용 가능한 이메일입니다.";
        msg.style.color = "green";
    }
}

/* 차량 연식 YYYY-MM 포맷 */
const carYearInput = document.getElementById("carYear");

carYearInput.addEventListener("input", function () {
    let numbers = this.value.replace(/[^0-9]/g, "");
    if (numbers.length > 6) numbers = numbers.substring(0, 6);
    let result = "";
    if (numbers.length <= 4) {
        result = numbers;
    } else {
        result = numbers.slice(0, 4) + "-" + numbers.slice(4, 6);
    }
    this.value = result;
});