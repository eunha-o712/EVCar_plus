'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const wishlistGrid = document.getElementById('wishlistGrid');
    const wishlistEmpty = document.getElementById('wishlistEmpty');

    if (!wishlistGrid || !wishlistEmpty) {
        return;
    }

    const updateEmptyState = () => {
        const cards = wishlistGrid.querySelectorAll('.ev-wishlist-card');
        const isEmpty = cards.length === 0;

        wishlistGrid.style.display = isEmpty ? 'none' : 'grid';
        wishlistEmpty.classList.toggle('ev-wishlist-empty--hidden', !isEmpty);
    };

    const deleteButtons = wishlistGrid.querySelectorAll('.ev-delete-btn');

    deleteButtons.forEach((button) => {
        button.addEventListener('click', (event) => {
            const card = event.target.closest('.ev-wishlist-card');

            if (!card) {
                return;
            }

            const confirmed = window.confirm('관심 차량을 삭제하시겠습니까?');

            if (!confirmed) {
                return;
            }

            card.remove();
            updateEmptyState();
        });
    });

    updateEmptyState();
});