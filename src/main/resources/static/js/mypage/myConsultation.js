'use strict';

document.addEventListener('DOMContentLoaded', () => {

    const list = document.getElementById('consultationList');
    const emptyBox = document.getElementById('emptyBox');

    const updateEmpty = () => {
        const items = list.querySelectorAll('.ev-consultation-card');
        emptyBox.style.display = items.length === 0 ? 'block' : 'none';
    };

    document.querySelectorAll('.ev-cancel-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const card = e.target.closest('.ev-consultation-card');

            if (!confirm('상담을 취소하시겠습니까?')) return;

            card.remove();
            updateEmpty();
        });
    });

    updateEmpty();
});