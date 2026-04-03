<<<<<<< HEAD
'use strict';

document.addEventListener('DOMContentLoaded', () => {
    bindDeleteConfirm();
    bindImageUploader();
    bindAutoHideAlert();
    bindVehicleIdPreview();
});

function bindDeleteConfirm() {
    const deleteForms = document.querySelectorAll('.ev-admin-delete-form');

    deleteForms.forEach((formElement) => {
        formElement.addEventListener('submit', (event) => {
            const isConfirmed = window.confirm('선택한 차량을 삭제하시겠습니까?');
            if (!isConfirmed) {
                event.preventDefault();
            }
        });
    });
}

function bindImageUploader() {
    const dropzone = document.querySelector('.js-vehicle-dropzone');
    const fileInput = document.querySelector('.js-vehicle-file-input');
    const previewBox = document.getElementById('vehicleImagePreview');
    const currentImageUrlInput = document.getElementById('currentImageUrl');
    const fileNameText = document.querySelector('.js-vehicle-file-name');

    if (!dropzone || !fileInput || !previewBox || !fileNameText) {
        return;
    }

    const renderExistingPreview = () => {
        const currentImageUrl = currentImageUrlInput ? currentImageUrlInput.value.trim() : '';

        if (!currentImageUrl) {
            previewBox.innerHTML = '<span>이미지 파일을 선택하면 미리보기가 표시됩니다.</span>';
            fileNameText.textContent = '선택된 파일 없음';
            return;
        }

        previewBox.innerHTML = '<img src="' + escapeHtml(currentImageUrl) + '" alt="차량 이미지 미리보기" class="ev-admin-vehicle-preview-image">';
        fileNameText.textContent = '현재 이미지 유지 중';
    };

    const renderPreview = (file) => {
        if (!file) {
            renderExistingPreview();
            return;
        }

        if (!file.type.startsWith('image/')) {
            window.alert('이미지 파일만 선택할 수 있습니다.');
            fileInput.value = '';
            renderExistingPreview();
            return;
        }

        const reader = new FileReader();

        reader.onload = (event) => {
            previewBox.innerHTML = '<img src="' + event.target.result + '" alt="차량 이미지 미리보기" class="ev-admin-vehicle-preview-image">';
            fileNameText.textContent = file.name;
        };

        reader.readAsDataURL(file);
    };

    fileInput.addEventListener('change', () => {
        const file = fileInput.files && fileInput.files.length > 0 ? fileInput.files[0] : null;
        renderPreview(file);
    });

    ['dragenter', 'dragover'].forEach((eventName) => {
        dropzone.addEventListener(eventName, (event) => {
            event.preventDefault();
            dropzone.classList.add('is-dragover');
        });
    });

    ['dragleave', 'dragend', 'drop'].forEach((eventName) => {
        dropzone.addEventListener(eventName, (event) => {
            event.preventDefault();
            dropzone.classList.remove('is-dragover');
        });
    });

    dropzone.addEventListener('drop', (event) => {
        const files = event.dataTransfer.files;
        if (!files || files.length === 0) {
            return;
        }

        const transfer = new DataTransfer();
        transfer.items.add(files[0]);
        fileInput.files = transfer.files;
        renderPreview(files[0]);
    });

    renderExistingPreview();
}

function bindAutoHideAlert() {
    const alertElement = document.querySelector('.js-admin-alert');

    if (!alertElement) {
        return;
    }

    window.setTimeout(() => {
        alertElement.classList.add('is-hiding');

        window.setTimeout(() => {
            alertElement.remove();
        }, 400);
    }, 1000);
}

function bindVehicleIdPreview() {
    const brandInput = document.getElementById('brand');
    const vehicleIdPreviewInput = document.getElementById('vehicleIdPreview');
    const hiddenVehicleIdInput = document.querySelector('input[name="vehicleId"]');

    if (!brandInput || !vehicleIdPreviewInput) {
        return;
    }

    if (hiddenVehicleIdInput && hiddenVehicleIdInput.value.trim() !== '') {
        return;
    }

    const requestPreviewVehicleId = async () => {
        const brand = brandInput.value.trim();

        try {
            const response = await fetch('/admin/vehicle/next-id?brand=' + encodeURIComponent(brand));
            if (!response.ok) {
                return;
            }

            const nextVehicleId = await response.text();
            vehicleIdPreviewInput.value = nextVehicleId;
        } catch (error) {
            console.error(error);
        }
    };

    brandInput.addEventListener('input', requestPreviewVehicleId);
    requestPreviewVehicleId();
}

function escapeHtml(value) {
    return value
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll('\'', '&#39;');
}
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
