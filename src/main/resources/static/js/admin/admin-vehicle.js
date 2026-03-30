'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const imageInput = document.getElementById('vehicleImage');
    const previewBox = document.getElementById('vehiclePreview');
    const statusInput = document.getElementById('vehicleStatus');
    const statusButtons = document.querySelectorAll('.ev-admin-inline .ev-admin-chip');

    if (imageInput && previewBox) {
        imageInput.addEventListener('change', (event) => {
            const file = event.target.files && event.target.files[0];

            if (!file) {
                previewBox.textContent = '등록된 이미지가 없습니다.';
                return;
            }

            const reader = new FileReader();
            reader.onload = (loadEvent) => {
                previewBox.innerHTML = '';
                const image = document.createElement('img');
                image.src = loadEvent.target.result;
                image.alt = '차량 이미지 미리보기';
                image.style.maxWidth = '100%';
                image.style.borderRadius = '16px';
                image.style.display = 'block';
                previewBox.appendChild(image);
            };
            reader.readAsDataURL(file);
        });
    }

    if (statusButtons.length > 0 && statusInput) {
        statusButtons.forEach((button) => {
            button.addEventListener('click', () => {
                statusButtons.forEach((item) => item.classList.remove('is-active'));
                button.classList.add('is-active');
                statusInput.value = button.textContent.trim() === '품절' ? 'SOLD_OUT' : 'ACTIVE';
            });
        });
    }
});