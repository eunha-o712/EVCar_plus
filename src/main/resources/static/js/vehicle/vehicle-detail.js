'use strict';

document.addEventListener('DOMContentLoaded', () => {
    initVehicleGallery();
    initWishlistButton();
});

function initVehicleGallery() {
    const slides = document.querySelectorAll('.ev-vehicle-gallery__slide');
    const thumbs = document.querySelectorAll('.ev-vehicle-gallery__thumb');
    const prevButton = document.getElementById('vehicleGalleryPrev');
    const nextButton = document.getElementById('vehicleGalleryNext');

    if (!slides.length || !thumbs.length) {
        return;
    }

    let currentIndex = 0;

    const showSlide = (index) => {
        if (index < 0) {
            currentIndex = slides.length - 1;
        } else if (index >= slides.length) {
            currentIndex = 0;
        } else {
            currentIndex = index;
        }

        slides.forEach((slide, slideIndex) => {
            slide.classList.toggle('is-active', slideIndex === currentIndex);
        });

        thumbs.forEach((thumb, thumbIndex) => {
            thumb.classList.toggle('is-active', thumbIndex === currentIndex);
        });
    };

    thumbs.forEach((thumb, index) => {
        thumb.addEventListener('click', () => {
            showSlide(index);
        });
    });

    if (prevButton) {
        prevButton.addEventListener('click', () => {
            showSlide(currentIndex - 1);
        });
    }

    if (nextButton) {
        nextButton.addEventListener('click', () => {
            showSlide(currentIndex + 1);
        });
    }

    showSlide(0);
}

function initWishlistButton() {
    const wishlistButton = document.getElementById('wishlistBtn');

    if (!wishlistButton) {
        return;
    }

    wishlistButton.addEventListener('click', async function () {
        const vehicleId = this.dataset.id;
        const isWished = this.textContent.trim() === '❤️';
        const requestUrl = isWished ? '/wishlist/delete' : '/wishlist/add';

        try {
            const response = await fetch(requestUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: 'vehicleId=' + encodeURIComponent(vehicleId)
            });

            if (!response.ok) {
                throw new Error('wishlist request failed');
            }

            this.innerHTML = isWished ? '<span>🤍</span>' : '<span>❤️</span>';
        } catch (error) {
            alert('처리 중 오류가 발생했습니다.');
        }
    });
}