'use strict';

const DEFAULT_ZCODE = '11';
const DEFAULT_ZSCODE = '11680';

const DEFAULT_CENTER = {
    lat: 37.4979,
    lng: 127.0276
};

const DEFAULT_RADIUS_KM = 1;

const REGION_VIEW = {
    '11': { lat: 37.5665, lng: 126.9780, level: 7 },
    '26': { lat: 35.1796, lng: 129.0756, level: 7 },
    '27': { lat: 35.8714, lng: 128.6014, level: 7 },
    '28': { lat: 37.4563, lng: 126.7052, level: 7 },
    '29': { lat: 35.1595, lng: 126.8526, level: 7 },
    '30': { lat: 36.3504, lng: 127.3845, level: 7 },
    '31': { lat: 35.5384, lng: 129.3114, level: 7 },
    '36': { lat: 36.4800, lng: 127.2890, level: 7 },
    '41': { lat: 37.4138, lng: 127.5183, level: 8 },
    '43': { lat: 36.6357, lng: 127.4917, level: 8 },
    '44': { lat: 36.5184, lng: 126.8000, level: 8 },
    '46': { lat: 34.8161, lng: 126.4630, level: 8 },
    '47': { lat: 36.4919, lng: 128.8889, level: 8 },
    '48': { lat: 35.4606, lng: 128.2132, level: 8 },
    '50': { lat: 33.4996, lng: 126.5312, level: 8 },
    '51': { lat: 37.8228, lng: 128.1555, level: 8 },
    '52': { lat: 35.7175, lng: 127.1530, level: 8 }
};

const DETAIL_VIEW = {
    '11-11680': { lat: 37.4979, lng: 127.0276, level: 5 }
};

let map;
let clusterer = null;
let markers = [];
let currentOverlay = null;
let currentLocationMarker = null;
let selectedStation = null;
let regions = [];
let initialized = false;

window.onload = function () {
    const mapElement = document.getElementById('map');
    if (!mapElement) {
        return;
    }

    map = new kakao.maps.Map(mapElement, {
        center: new kakao.maps.LatLng(DEFAULT_CENTER.lat, DEFAULT_CENTER.lng),
        level: 5
    });

    clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 6,
        disableClickZoom: false
    });

    bindEvents();
    loadRegions();
};

function bindEvents() {
    const sidoSelect = document.getElementById('sidoSelect');
    const searchBtn = document.getElementById('searchBtn');
    const myLocationBtn = document.getElementById('myLocationBtn');
    const detailPanelCloseBtn = document.getElementById('detailPanelCloseBtn');

    if (sidoSelect) {
        sidoSelect.addEventListener('change', onSidoChange);
    }

    if (searchBtn) {
        searchBtn.addEventListener('click', function () {
            searchByRegion(false);
        });
    }

    if (myLocationBtn) {
        myLocationBtn.addEventListener('click', moveToMyLocation);
    }

    if (detailPanelCloseBtn) {
        detailPanelCloseBtn.addEventListener('click', closeDetailPanel);
    }

    kakao.maps.event.addListener(map, 'click', function () {
        closeOverlay();
    });
}

async function loadRegions() {
    showLoading();

    try {
        const response = await fetch('/charging/regions');
        if (!response.ok) {
            throw new Error('지역 목록 조회 실패');
        }

        const rawData = await response.json();
        regions = normalizeRegions(rawData);

        renderSidoOptions(regions);
        applyDefaultRegion();

        if (!initialized) {
            initialized = true;
            await searchByRegion(true);
        }
    } catch (error) {
        console.error('loadRegions error =', error);
        alert('지역 목록을 불러오지 못했습니다.');
    } finally {
        hideLoading();
    }
}

function normalizeRegions(rawData) {
    if (!rawData) {
        return [];
    }

    if (Array.isArray(rawData)) {
        return rawData.map(normalizeRegionItem).filter(Boolean);
    }

    if (typeof rawData === 'string') {
        try {
            return normalizeRegions(JSON.parse(rawData));
        } catch (error) {
            console.error('regions parse error =', error);
            return [];
        }
    }

    return [];
}

function normalizeRegionItem(region) {
    if (!region) {
        return null;
    }

    const sido = normalizeText(region.sido);
    const zcode = normalizeText(region.zcode);

    if (!sido || !zcode) {
        return null;
    }

    const sigunguList = Array.isArray(region.sigunguList)
        ? region.sigunguList.map(normalizeSigunguItem).filter(Boolean)
        : [];

    return {
        sido: sido,
        zcode: zcode,
        sigunguList: sigunguList
    };
}

function normalizeSigunguItem(sigungu) {
    if (!sigungu || typeof sigungu !== 'object') {
        return null;
    }

    const zscode = normalizeText(sigungu.zscode);
    const sigunguName = normalizeText(sigungu.sigungu);

    if (!sigunguName) {
        return null;
    }

    return {
        zscode: zscode,
        sigungu: sigunguName
    };
}

function renderSidoOptions(regionList) {
    const sidoSelect = document.getElementById('sidoSelect');
    if (!sidoSelect) {
        return;
    }

    sidoSelect.innerHTML = '<option value="">시/도 선택</option>';

    regionList.forEach(function (region) {
        const option = document.createElement('option');
        option.value = region.zcode;
        option.textContent = region.sido;
        sidoSelect.appendChild(option);
    });

    resetSigunguOptions();
}

function applyDefaultRegion() {
    const sidoSelect = document.getElementById('sidoSelect');
    const sigunguSelect = document.getElementById('sigunguSelect');

    if (!sidoSelect || !sigunguSelect) {
        return;
    }

    sidoSelect.value = DEFAULT_ZCODE;
    onSidoChange();
    sigunguSelect.value = DEFAULT_ZSCODE;
    moveMapToRegion(DEFAULT_ZCODE, DEFAULT_ZSCODE);
}

function onSidoChange() {
    const sidoSelect = document.getElementById('sidoSelect');
    const sigunguSelect = document.getElementById('sigunguSelect');

    if (!sidoSelect || !sigunguSelect) {
        return;
    }

    const selectedZcode = sidoSelect.value;
    resetSigunguOptions();

    if (!selectedZcode) {
        return;
    }

    const selectedRegion = regions.find(function (region) {
        return region.zcode === selectedZcode;
    });

    if (!selectedRegion || !Array.isArray(selectedRegion.sigunguList)) {
        moveMapToRegion(selectedZcode, '');
        return;
    }

    sigunguSelect.disabled = false;

    selectedRegion.sigunguList.forEach(function (sigungu) {
        const option = document.createElement('option');
        option.value = sigungu.zscode || '';
        option.textContent = sigungu.sigungu || '-';
        sigunguSelect.appendChild(option);
    });

    moveMapToRegion(selectedZcode, '');
}

function resetSigunguOptions() {
    const sigunguSelect = document.getElementById('sigunguSelect');
    if (!sigunguSelect) {
        return;
    }

    sigunguSelect.innerHTML = '<option value="">시/군/구 선택</option>';
    sigunguSelect.disabled = true;
}

async function searchByRegion(isInitialLoad) {
    const sidoSelect = document.getElementById('sidoSelect');
    const sigunguSelect = document.getElementById('sigunguSelect');

    if (sidoSelect) {
        sidoSelect.blur();
    }

    if (sigunguSelect) {
        sigunguSelect.blur();
    }

    const zcode = sidoSelect ? sidoSelect.value : '';
    const zscode = sigunguSelect ? sigunguSelect.value : '';

    if (!zcode) {
        if (!isInitialLoad) {
            alert('시/도를 선택해주세요.');
        }
        hideLoading();
        return;
    }

    showLoading();
    moveMapToRegion(zcode, zscode);

    try {
        let url = '/charging/stations?zcode=' + encodeURIComponent(zcode);

        if (zscode) {
            url += '&zscode=' + encodeURIComponent(zscode);
        }

        console.log('stations request url =', url);

        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('충전소 조회 실패');
        }

        const stations = await response.json();
        console.log('stations data =', stations);

        if (stations.length > 0) {
            console.log('[디버그] 첫 번째 충전소 전체 키:', Object.keys(stations[0]));
            console.log('[디버그] 첫 번째 충전소 데이터:', stations[0]);
        }

        renderStations(stations, zcode, zscode, isInitialLoad);
    } catch (error) {
        console.error('searchByRegion error =', error);
        alert('충전소 정보를 불러오지 못했습니다.');
    } finally {
        hideLoading();
    }
}

function renderStations(stations, zcode, zscode, isInitialLoad) {
    clearMarkers();
    closeOverlay();
    closeDetailPanel();
    selectedStation = null;

    if (!Array.isArray(stations) || stations.length === 0) {
        alert('해당 지역의 충전소가 없습니다.');
        return;
    }

    let targetStations = stations;

    if (isInitialLoad) {
        const filteredStations = stations.filter(function (station) {
            const lat = toNumber(station.lat);
            const lng = toNumber(station.lng);

            if (!isValidCoordinate(lat, lng)) {
                return false;
            }

            const distanceKm = getDistanceKm(
                DEFAULT_CENTER.lat,
                DEFAULT_CENTER.lng,
                lat,
                lng
            );

            return distanceKm <= DEFAULT_RADIUS_KM;
        });

        if (filteredStations.length > 0) {
            targetStations = filteredStations;
        }
    }

    const newMarkers = [];

    targetStations.forEach(function (station) {
        const lat = toNumber(station.lat);
        const lng = toNumber(station.lng);

        if (!isValidCoordinate(lat, lng)) {
            return;
        }

        const normalizedStation = {
            ...station,
            lat: lat,
            lng: lng
        };

        const position = new kakao.maps.LatLng(lat, lng);

        const marker = new kakao.maps.Marker({
            position: position
        });

        kakao.maps.event.addListener(marker, 'click', function () {
            selectedStation = normalizedStation;
            showOverlay(normalizedStation, position);
        });

        newMarkers.push(marker);
    });

    console.log('표시 가능한 충전소 수 =', newMarkers.length);

    if (newMarkers.length === 0) {
        alert('표시 가능한 충전소 좌표가 없습니다.');
        return;
    }

    markers = newMarkers;

    if (clusterer) {
        clusterer.clear();
        clusterer.addMarkers(markers);
    }

    moveMapToRegion(zcode, zscode);
}

function moveMapToRegion(zcode, zscode) {
    const detailKey = zcode + '-' + zscode;
    const detailView = DETAIL_VIEW[detailKey];

    if (detailView) {
        map.setCenter(new kakao.maps.LatLng(detailView.lat, detailView.lng));
        map.setLevel(detailView.level);
        return;
    }

    const regionView = REGION_VIEW[zcode];
    if (regionView) {
        map.setCenter(new kakao.maps.LatLng(regionView.lat, regionView.lng));
        map.setLevel(regionView.level);
    }
}

function showOverlay(station, position) {
    closeOverlay();

    const content = document.createElement('div');
    content.className = 'ev-overlay';
    content.innerHTML =
        '<button type="button" class="ev-overlay__close" data-action="close">&times;</button>' +
        '<p class="ev-overlay__name">' + escapeHtml(station.stationName || '-') + '</p>' +
        '<p class="ev-overlay__address">' + escapeHtml(station.address || '-') + '</p>' +
        '<button type="button" class="ev-overlay__detail-btn" data-action="detail">상세정보</button>';

    content.addEventListener('click', function (event) {
        event.stopPropagation();

        const actionEl = event.target.closest('[data-action]');
        if (!actionEl) {
            return;
        }

        const action = actionEl.dataset.action;

        if (action === 'close') {
            closeOverlay();
            return;
        }

        if (action === 'detail') {
            console.log('[상세정보 클릭] selectedStation =', selectedStation);
            openDetailPanel();
            closeOverlay();
        }
    });

    currentOverlay = new kakao.maps.CustomOverlay({
        map: map,
        position: position,
        content: content,
        yAnchor: 1.35,
        clickable: true
    });
}

function closeOverlay() {
    if (currentOverlay) {
        currentOverlay.setMap(null);
        currentOverlay = null;
    }
}

function getStationId(station) {
    if (!station) {
        return null;
    }

    if (station.stationId) {
        return station.stationId;
    }

    if (station.statId) {
        return station.statId;
    }

    if (station.station_id) {
        return station.station_id;
    }

    if (station.stat_id) {
        return station.stat_id;
    }

    console.warn('[getStationId] stationId 필드를 찾을 수 없습니다. 전체 키:', Object.keys(station));
    return null;
}

async function openDetailPanel() {
    if (!selectedStation) {
        console.warn('[openDetailPanel] selectedStation이 null입니다.');
        return;
    }

    const panel = document.getElementById('mapDetailPanel');
    if (!panel) {
        console.warn('[openDetailPanel] mapDetailPanel을 찾을 수 없습니다.');
        return;
    }

    const stationId = getStationId(selectedStation);
    console.log('[openDetailPanel] stationId =', stationId);

    panel.classList.add('is-open');
    showLoading();

    try {
        if (!stationId) {
            console.warn('[openDetailPanel] stationId가 없어서 충전기 조회를 건너뜁니다.');
            renderDetailPanel(selectedStation, []);
            return;
        }

        const chargers = await loadChargers(stationId);
        renderDetailPanel(selectedStation, chargers);
    } catch (error) {
        console.error('[openDetailPanel] error =', error);
        renderDetailPanel(selectedStation, []);
    } finally {
        hideLoading();
    }
}

function closeDetailPanel() {
    const panel = document.getElementById('mapDetailPanel');
    if (!panel) {
        return;
    }

    panel.classList.remove('is-open');
}

async function loadChargers(stationId) {
    const url = '/charging/chargers?stationId=' + encodeURIComponent(stationId);
    console.log('[loadChargers] url =', url);

    const response = await fetch(url);
    if (!response.ok) {
        throw new Error('충전기 조회 실패: ' + response.status);
    }

    return await response.json();
}

function renderDetailPanel(station, chargers) {
    const nameEl = document.getElementById('detailStationName');
    const addressEl = document.getElementById('detailStationAddress');
    const chargerRowsEl = document.getElementById('detailChargerRows');
    const infoRowsEl = document.getElementById('detailInfoRows');

    if (nameEl) {
        nameEl.textContent = station.stationName || '-';
    }

    if (addressEl) {
        addressEl.textContent = station.address || '-';
    }

    if (chargerRowsEl) {
        chargerRowsEl.innerHTML = buildChargerRows(chargers);
    }

    if (infoRowsEl) {
        infoRowsEl.innerHTML = buildInfoRows(station);
    }
}

function buildChargerRows(chargers) {
    if (!Array.isArray(chargers) || chargers.length === 0) {
        return (
            '<div class="ev-map-detail-table__row">' +
                '<div class="ev-map-detail-table__cell">충전기 정보 없음</div>' +
                '<div class="ev-map-detail-table__cell">-</div>' +
                '<div class="ev-map-detail-table__cell">-</div>' +
            '</div>'
        );
    }

    return chargers.map(function (charger, index) {
        const statusText = charger.status || '-';
        const typeText = charger.chargerType || '-';
        const statusClass = getStatusClass(statusText);

        return (
            '<div class="ev-map-detail-table__row">' +
                '<div class="ev-map-detail-table__cell">' +
                    '<span class="ev-map-detail-table__name">충전기 ' + String(index + 1).padStart(2, '0') + '</span>' +
                '</div>' +
                '<div class="ev-map-detail-table__cell">' +
                    '<span class="ev-map-detail-table__status ' + statusClass + '">' + escapeHtml(statusText) + '</span>' +
                '</div>' +
                '<div class="ev-map-detail-table__cell">' +
                    '<span class="ev-map-detail-table__badge">' + escapeHtml(typeText) + '</span>' +
                '</div>' +
            '</div>'
        );
    }).join('');
}

function buildInfoRows(station) {
    const rows = [
        ['이용시간', normalizeValue(station.useTime)],
        ['운영기관', normalizeValue(station.operatorName)],
        ['연락처', normalizeValue(station.operatorCall)],
        ['주차요금', formatParkingFree(station.parkingFree)]
    ];

    const note = normalizeNote(station.note);
    if (note) {
        rows.push(['비고', note]);
    }

    return rows.map(function (item) {
        return (
            '<div class="ev-map-info-box__row">' +
                '<div class="ev-map-info-box__label">' + escapeHtml(item[0]) + '</div>' +
                '<div class="ev-map-info-box__value">' + escapeHtml(item[1]) + '</div>' +
            '</div>'
        );
    }).join('');
}

function clearMarkers() {
    if (clusterer) {
        clusterer.clear();
    }

    markers.forEach(function (marker) {
        marker.setMap(null);
    });

    markers = [];
}

function moveToMyLocation() {
    if (!navigator.geolocation) {
        alert('이 브라우저는 위치 정보를 지원하지 않습니다.');
        return;
    }

    showLoading();

    navigator.geolocation.getCurrentPosition(
        function (position) {
            const current = new kakao.maps.LatLng(
                position.coords.latitude,
                position.coords.longitude
            );

            map.setCenter(current);
            map.setLevel(4);

            if (currentLocationMarker) {
                currentLocationMarker.setMap(null);
            }

            currentLocationMarker = new kakao.maps.Marker({
                map: map,
                position: current
            });

            hideLoading();
        },
        function () {
            hideLoading();
            alert('현재 위치를 가져오지 못했습니다.');
        }
    );
}

function showLoading() {
    const loadingEl = document.getElementById('mapLoading');
    if (!loadingEl) {
        return;
    }

    loadingEl.classList.add('is-active');
}

function hideLoading() {
    const loadingEl = document.getElementById('mapLoading');
    if (!loadingEl) {
        return;
    }

    loadingEl.classList.remove('is-active');
}

function getStatusClass(statusText) {
    if (statusText === '사용가능') {
        return 'ev-map-detail-table__status--available';
    }

    if (statusText === '충전중') {
        return 'ev-map-detail-table__status--charging';
    }

    return 'ev-map-detail-table__status--inactive';
}

function normalizeValue(value) {
    if (value == null) {
        return '-';
    }

    const text = String(value).trim();
    if (!text || text === 'null') {
        return '-';
    }

    return text;
}

function normalizeNote(note) {
    const text = normalizeValue(note);

    if (text === '-' || text === '이용 안내 없음') {
        return '';
    }

    return text;
}

function formatParkingFree(value) {
    const text = normalizeValue(value);

    if (text === 'Y' || text === '무료') {
        return '무료';
    }

    if (text === 'N' || text === '유료') {
        return '유료';
    }

    return text;
}

function toNumber(value) {
    if (typeof value === 'number') {
        return value;
    }

    if (value == null) {
        return NaN;
    }

    const parsed = Number(String(value).trim());
    return Number.isNaN(parsed) ? NaN : parsed;
}

function isValidCoordinate(lat, lng) {
    return Number.isFinite(lat)
        && Number.isFinite(lng)
        && lat >= 33
        && lat <= 39
        && lng >= 124
        && lng <= 132;
}

function normalizeText(value) {
    if (value == null) {
        return '';
    }

    const text = String(value).trim();
    if (!text || text === 'null') {
        return '';
    }

    return text;
}

function escapeHtml(value) {
    return String(value != null ? value : '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function getDistanceKm(lat1, lng1, lat2, lng2) {
    const earthRadiusKm = 6371;
    const dLat = toRadians(lat2 - lat1);
    const dLng = toRadians(lng2 - lng1);

    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(toRadians(lat1)) *
        Math.cos(toRadians(lat2)) *
        Math.sin(dLng / 2) *
        Math.sin(dLng / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return earthRadiusKm * c;
}

function toRadians(degree) {
    return degree * Math.PI / 180;
}