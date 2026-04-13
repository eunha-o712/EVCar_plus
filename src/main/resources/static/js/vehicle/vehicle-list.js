'use strict';

function filterByData(button) {
    const brand = button.getAttribute('data-brand') || '전체';
    const vehicleClass = button.getAttribute('data-class') || '전체';

    location.href = '/vehicle/list?brand=' + encodeURIComponent(brand)
        + '&vehicleClass=' + encodeURIComponent(vehicleClass);
}

function goDetail(card) {
    const vehicleId = card.getAttribute('data-id');

    if (!vehicleId) {
        return;
    }

    location.href = '/vehicle/' + vehicleId;
}

function goPage(button) {
    const brand = button.getAttribute('data-brand') || '전체';
    const vehicleClass = button.getAttribute('data-class') || '전체';
    const page = button.getAttribute('data-page') || '0';

    location.href = '/vehicle/list?brand=' + encodeURIComponent(brand)
        + '&vehicleClass=' + encodeURIComponent(vehicleClass)
        + '&page=' + encodeURIComponent(page);
}