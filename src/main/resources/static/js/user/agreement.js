'use strict';

// 전체 동의
document.getElementById("agreeAll").addEventListener("change", function(){
    document.getElementById("agreePrivacy").checked = this.checked;
    document.getElementById("agreeTerms").checked = this.checked;
});

// 개별 체크 → 전체 동의 반영
document.getElementById("agreePrivacy").addEventListener("change", checkAll);
document.getElementById("agreeTerms").addEventListener("change", checkAll);

function checkAll(){
    const p = document.getElementById("agreePrivacy").checked;
    const t = document.getElementById("agreeTerms").checked;
    document.getElementById("agreeAll").checked = (p && t);
}



//기존 코드 그대로 유지

function validateAgree() {
 const p = document.getElementById("agreePrivacy").checked;
 const t = document.getElementById("agreeTerms").checked;

 if (!p || !t) {
     document.getElementById("alertMsg").innerText = "필수 약관에 모두 동의해야 합니다.";
     return false;
 }

 return true;
}