'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const wishlistGrid = document.getElementById('wishlistGrid');
    const wishlistEmpty = document.getElementById('wishlistEmpty');
    const addSampleVehicleButton = document.getElementById('addSampleVehicleButton');
    const resetWishlistButton = document.getElementById('resetWishlistButton');

    if (!wishlistGrid || !wishlistEmpty) {
        return;
    }

    const STORAGE_KEY = 'evcar-wishlist';

    /*
     * TODO: 팀플 백엔드 연동 시 아래 값을 false 로 변경하거나,
     * 테스트 버튼 영역 자체를 제거한 뒤 서버 조회/삭제만 사용
     */
    const USE_TEST_DATA = true;

    /*
     * TODO: 차량 목록/상세 페이지 연동 전 테스트용 샘플 데이터
     * 팀원 API 또는 Thymeleaf 모델 연동 후 제거 가능
     */
    const defaultWishlist = [
        {
            wishlistId: 1,
            brand: '현대',
            category: '중형 SUV',
            model: '아이오닉 5',
            price: '5,200만원',
            imageUrl: '/images/no-image.png',
            detailUrl: '#'
        },
        {
            wishlistId: 2,
            brand: '기아',
            category: '중형 SUV',
            model: 'EV6',
            price: '4,870만원',
            imageUrl: '/images/no-image.png',
            detailUrl: '#'
        }
    ];

    const createCardHtml = (item) => {
        return `
            <article class="ev-wishlist-card" data-wishlist-id="${item.wishlistId}">
                <button type="button" class="ev-wishlist-favorite" aria-label="관심차량">♥</button>
                <div class="ev-wishlist-image-wrap">
                    <img class="ev-wishlist-image" src="${item.imageUrl}" alt="${item.model}">
                </div>
                <div class="ev-wishlist-body">
                    <p class="ev-wishlist-brand">${item.brand} | ${item.category}</p>
                    <h2 class="ev-wishlist-model">${item.model}</h2>
                    <p class="ev-wishlist-price">${item.price}</p>
                    <div class="ev-wishlist-actions">
                        <a href="${item.detailUrl}" class="btn btn-ev-secondary">상세보기</a>
                        <button type="button" class="btn btn-ev-primary ev-delete-btn">삭제</button>
                    </div>
                </div>
            </article>
        `;
    };

    const renderEmptyState = (wishlist) => {
        const isEmpty = wishlist.length === 0;
        wishlistGrid.style.display = isEmpty ? 'none' : 'grid';
        wishlistEmpty.classList.toggle('ev-wishlist-empty--hidden', !isEmpty);
    };

    const bindEvents = () => {
        const deleteButtons = wishlistGrid.querySelectorAll('.ev-delete-btn');
        deleteButtons.forEach((button) => {
            button.addEventListener('click', async (event) => {
                const card = event.target.closest('.ev-wishlist-card');
                await handleDelete(card);
            });
        });

        const favoriteButtons = wishlistGrid.querySelectorAll('.ev-wishlist-favorite');
        favoriteButtons.forEach((button) => {
            button.addEventListener('click', async (event) => {
                const card = event.target.closest('.ev-wishlist-card');
                await handleDelete(card);
            });
        });
    };

    const renderWishlist = async () => {
        const wishlist = await getWishlist();
        wishlistGrid.innerHTML = wishlist.map(createCardHtml).join('');
        renderEmptyState(wishlist);
        bindEvents();
    };

    const handleDelete = async (card) => {
        if (!card) {
            return;
        }

        const wishlistId = Number(card.dataset.wishlistId);

        if (!wishlistId) {
            window.alert('관심 차량 식별값이 없습니다.');
            return;
        }

        const confirmed = window.confirm('관심차량에서 삭제하시겠습니까?');

        if (!confirmed) {
            return;
        }

        await removeWishlistItem(wishlistId);
        await renderWishlist();
    };

    /*
     * =========================
     * 테스트용 localStorage 영역
     * =========================
     */

    const getWishlistFromLocal = () => {
        const stored = localStorage.getItem(STORAGE_KEY);

        if (!stored) {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultWishlist));
            return defaultWishlist;
        }

        try {
            return JSON.parse(stored);
        } catch (error) {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultWishlist));
            return defaultWishlist;
        }
    };

    const saveWishlistToLocal = (wishlist) => {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(wishlist));
    };

    const addWishlistToLocal = (item) => {
        const wishlist = getWishlistFromLocal();
        const exists = wishlist.some((wishlistItem) => wishlistItem.model === item.model);

        if (exists) {
            window.alert('이미 관심차량에 등록된 차량입니다.');
            return;
        }

        wishlist.push(item);
        saveWishlistToLocal(wishlist);
    };

    const removeWishlistFromLocal = (wishlistId) => {
        const wishlist = getWishlistFromLocal();
        const filteredWishlist = wishlist.filter((item) => item.wishlistId !== wishlistId);
        saveWishlistToLocal(filteredWishlist);
    };

    /*
     * =========================
     * 팀플용 서버 연동 영역
     * =========================
     * TODO:
     * 1. 팀원 API 확정 시 조회 URL 연결
     * 2. 삭제 API는 /mypage/wishlist/delete 사용 가능
     * 3. 추가 API 확정 시 addWishlistToServer 구현
     */

    const getWishlistFromServer = async () => {
        const response = await fetch('/mypage/wishlist/api', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('관심 차량 목록 조회에 실패했습니다.');
        }

        return await response.json();
    };

    const removeWishlistFromServer = async (wishlistId) => {
        const response = await fetch('/mypage/wishlist/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                wishlistId: String(wishlistId)
            })
        });

        if (!response.ok) {
            throw new Error('관심 차량 삭제에 실패했습니다.');
        }
    };

    const addWishlistToServer = async () => {
        /*
         * TODO: 차량 상세/목록 하트 연동 후 구현
         * 예시:
         * await fetch('/wishlist/add', { method: 'POST', body: ... })
         */
    };

    /*
     * =========================
     * 공통 분기 영역
     * =========================
     */

    const getWishlist = async () => {
        if (USE_TEST_DATA) {
            return getWishlistFromLocal();
        }

        return await getWishlistFromServer();
    };

    const removeWishlistItem = async (wishlistId) => {
        if (USE_TEST_DATA) {
            removeWishlistFromLocal(wishlistId);
            return;
        }

        await removeWishlistFromServer(wishlistId);
    };

    const addSampleWishlistItem = async () => {
        if (!USE_TEST_DATA) {
            window.alert('테스트 모드가 아닙니다.');
            return;
        }

        addWishlistToLocal({
            wishlistId: Date.now(),
            brand: '기아',
            category: '대형 SUV',
            model: 'EV9',
            price: '7,337만원',
            imageUrl: '/images/no-image.png',
            detailUrl: '#'
        });

        await renderWishlist();
    };

    if (addSampleVehicleButton) {
        addSampleVehicleButton.addEventListener('click', async () => {
            await addSampleWishlistItem();
        });
    }

    if (resetWishlistButton) {
        resetWishlistButton.addEventListener('click', async () => {
            if (!USE_TEST_DATA) {
                window.alert('테스트 모드에서만 초기화할 수 있습니다.');
                return;
            }

            localStorage.removeItem(STORAGE_KEY);
            await renderWishlist();
        });
    }

    renderWishlist().catch(() => {
        window.alert('관심 차량 정보를 불러오는 중 오류가 발생했습니다.');
    });
});