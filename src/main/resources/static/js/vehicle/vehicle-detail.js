'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initVehicleGallery();
    initWishlistButton();
});

function initVehicleGallery() {
    const track = document.getElementById('vehicleGalleryTrack');
    const prevButton = document.getElementById('vehicleGalleryPrev');
    const nextButton = document.getElementById('vehicleGalleryNext');
    const thumbButtons = document.querySelectorAll('.ev-vehicle-gallery__thumb');
    const slides = document.querySelectorAll('.ev-vehicle-gallery__slide');

    if (!track || !slides.length) {
        return;
    }

    let currentIndex = 0;

    const updateGallery = (index) => {
        const maxIndex = slides.length - 1;
        currentIndex = Math.max(0, Math.min(index, maxIndex));

        slides.forEach((slide, slideIndex) => {
            slide.classList.toggle('is-active', slideIndex === currentIndex);
        });

        thumbButtons.forEach((thumb, thumbIndex) => {
            thumb.classList.toggle('is-active', thumbIndex === currentIndex);
        });

        track.style.transform = `translateX(-${currentIndex * 100}%)`;

        if (prevButton) {
            prevButton.disabled = currentIndex === 0;
        }

        if (nextButton) {
            nextButton.disabled = currentIndex === maxIndex;
        }
    };

    if (prevButton) {
        prevButton.addEventListener('click', () => {
            updateGallery(currentIndex - 1);
        });
    }

    if (nextButton) {
        nextButton.addEventListener('click', () => {
            updateGallery(currentIndex + 1);
        });
    }

    thumbButtons.forEach((thumb, index) => {
        thumb.addEventListener('click', () => {
            updateGallery(index);
        });
    });

    updateGallery(0);
}

function initWishlistButton() {
    const wishlistButton = document.getElementById('wishlistBtn');
    if (!wishlistButton) {
        return;
    }

    wishlistButton.addEventListener('click', async () => {
        const vehicleId = wishlistButton.dataset.id;
        if (!vehicleId) {
            return;
        }

        const wished = isWished(wishlistButton);

        try {
            wishlistButton.disabled = true;

            if (wished) {
                await requestWishlistDelete(vehicleId);
                renderWishlistState(wishlistButton, false);
            } else {
                await requestWishlistAdd(vehicleId);
                renderWishlistState(wishlistButton, true);
            }
        } catch (error) {
            console.error(error);
            alert('관심 차량 처리 중 오류가 발생했습니다.');
        } finally {
            wishlistButton.disabled = false;
        }
    });
}

function isWished(button) {
    return button.textContent.includes('❤️');
}

function renderWishlistState(button, wished) {
    button.innerHTML = wished ? '<span>❤️</span>' : '<span>🤍</span>';
}

async function requestWishlistAdd(vehicleId) {
    const response = await fetch('/wishlist/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        body: new URLSearchParams({
            vehicleId: vehicleId
        }).toString()
    });

    if (response.redirected) {
        window.location.href = response.url;
        return;
    }

    if (!response.ok) {
        throw new Error('관심 차량 등록에 실패했습니다.');
    }
}

async function requestWishlistDelete(vehicleId) {
    const response = await fetch('/wishlist/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        body: new URLSearchParams({
            vehicleId: vehicleId
        }).toString()
    });

    if (response.redirected) {
        window.location.href = response.url;
        return;
    }

    if (!response.ok) {
        throw new Error('관심 차량 삭제에 실패했습니다.');
    }
}