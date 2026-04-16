'use strict';

function filterByData(button) {
    if (!button) {
        return;
    }

    const brand = button.dataset.brand || '전체';
    const vehicleClass = button.dataset.class || '전체';

    moveVehicleList(brand, vehicleClass, 0);
}

function goPage(button) {
    if (!button) {
        return;
    }

    const brand = button.dataset.brand || '전체';
    const vehicleClass = button.dataset.class || '전체';
    const page = Number(button.dataset.page || 0);

    moveVehicleList(brand, vehicleClass, page);
}

function goDetail(element) {
    if (!element) {
        return;
    }

    const vehicleId = element.dataset.id;
    if (!vehicleId) {
        return;
    }

    window.location.href = `/vehicle/${encodeURIComponent(vehicleId)}`;
}

function moveVehicleList(brand, vehicleClass, page) {
    const params = new URLSearchParams();

    params.set('brand', brand || '전체');
    params.set('vehicleClass', vehicleClass || '전체');

    if (page && page > 0) {
        params.set('page', String(page));
    }

    window.location.href = `/vehicle?${params.toString()}`;
}