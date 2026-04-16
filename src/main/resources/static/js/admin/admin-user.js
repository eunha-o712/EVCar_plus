'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const filterButtons = document.querySelectorAll('.ev-admin-chip');
    const detailButtons = document.querySelectorAll('.js-user-detail-btn');
    const searchButton = document.querySelector('.js-user-search-btn');
    const searchInput = document.querySelector('.js-user-search-input');

	const formatPhoneNumber = (phone) => {
	    if (!phone) {
	        return '';
	    }

	    let numbers = phone.replace(/[^0-9]/g, '');

	    if (numbers.startsWith('82')) {
	        numbers = numbers.substring(2);
	    }

	    if (!numbers.startsWith('0')) {
	        numbers = `0${numbers}`;
	    }

	    if (numbers.length === 11) {
	        return `+82 ${numbers.substring(1, 3)}-${numbers.substring(3, 7)}-${numbers.substring(7)}`;
	    }

	    if (numbers.length === 10) {
	        return `+82 ${numbers.substring(1, 3)}-${numbers.substring(3, 6)}-${numbers.substring(6)}`;
	    }

	    return phone;
	};

    const applyPhoneFormat = () => {
        const phoneCells = document.querySelectorAll('.js-user-phone');
        phoneCells.forEach((cell) => {
            const original = cell.textContent.trim();
            cell.textContent = formatPhoneNumber(original);
        });

        const detailPhone = document.querySelector('.js-user-detail-phone');
        if (detailPhone) {
            const original = detailPhone.textContent.trim();
            detailPhone.textContent = formatPhoneNumber(original);
        }
    };

    const moveToUserList = (params) => {
        const url = new URL(`${window.location.origin}/admin/user`);

        Object.entries(params).forEach(([key, value]) => {
            if (value === null || value === undefined || value === '') {
                url.searchParams.delete(key);
            } else {
                url.searchParams.set(key, value);
            }
        });

        window.location.href = url.toString();
    };

    filterButtons.forEach((button) => {
        button.addEventListener('click', () => {
            const status = button.dataset.status;
            const keyword = searchInput ? searchInput.value.trim() : '';

            moveToUserList({
                status: status,
                keyword: keyword,
                page: 1
            });
        });
    });

    detailButtons.forEach((button) => {
        button.addEventListener('click', () => {
            const detailUrl = button.dataset.detailUrl;
            if (detailUrl) {
                window.location.href = detailUrl;
            }
        });
    });

    if (searchButton && searchInput) {
        searchButton.addEventListener('click', () => {
            const activeFilter = document.querySelector('.ev-admin-chip.is-active');
            const status = activeFilter ? activeFilter.dataset.status : 'ALL';
            const keyword = searchInput.value.trim();

            moveToUserList({
                status: status,
                keyword: keyword,
                page: 1
            });
        });

        searchInput.addEventListener('keydown', (event) => {
            if (event.key === 'Enter') {
                event.preventDefault();
                searchButton.click();
            }
        });
    }

    applyPhoneFormat();
});