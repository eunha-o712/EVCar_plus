'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const agreementForm = document.getElementById('agreementForm');
    const agreeAll = document.getElementById('agreeAll');
    const agreePrivacy = document.getElementById('agreePrivacy');
    const agreeTerms = document.getElementById('agreeTerms');
    const alertMsg = document.getElementById('alertMsg');
    const backButton = document.getElementById('agreementBackBtn');

    if (!agreementForm || !agreeAll || !agreePrivacy || !agreeTerms || !alertMsg || !backButton) {
        return;
    }

    agreeAll.addEventListener('change', () => {
        agreePrivacy.checked = agreeAll.checked;
        agreeTerms.checked = agreeAll.checked;
        alertMsg.textContent = '';
    });

    agreePrivacy.addEventListener('change', () => {
        syncAllAgreement();
        alertMsg.textContent = '';
    });

    agreeTerms.addEventListener('change', () => {
        syncAllAgreement();
        alertMsg.textContent = '';
    });

    agreementForm.addEventListener('submit', (event) => {
        if (!agreePrivacy.checked || !agreeTerms.checked) {
            event.preventDefault();
            alertMsg.textContent = '필수 약관에 모두 동의해야 합니다.';
        }
    });

    backButton.addEventListener('click', () => {
        window.location.href = '/login';
    });

    function syncAllAgreement() {
        agreeAll.checked = agreePrivacy.checked && agreeTerms.checked;
    }
});