'use strict';

document.addEventListener('DOMContentLoaded', async () => {
    await loadSummarySafe();
    await loadMonthlyConsultationChartSafe();
    await loadVehicleDemandChartSafe();
    await loadRegionConsultationChartSafe();
    await loadConsultationResultChartSafe();
    await loadComparisonSafe();
});

const VEHICLE_NAME_MAP = {
    IONIQ_5: '아이오닉 5',
    IONIQ_5_N: '아이오닉 5 N',
    IONIQ_6: '아이오닉 6',
    EV3: 'EV3',
    EV4: 'EV4',
    EV5: 'EV5',
    EV6: 'EV6',
    EV9: 'EV9',
    KONA_ELECTRIC: '코나 일렉트릭',
    CASPER_EV: '캐스퍼 EV',
    RAY_EV: '레이 EV',
    NIRO_EV: '니로 EV',
    GV60: 'GV60',
    ELECTRIFIED_G80: '일렉트리파이드 G80',
    ELECTRIFIED_GV70: '일렉트리파이드 GV70',
    PORTER2_EV: '포터2 EV',
    BONGO3_EV: '봉고3 EV'
};

async function fetchJson(url) {
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error('요청 실패: ' + url);
    }

    return response.json();
}

async function loadSummarySafe() {
    try {
        await loadSummary();
    } catch (error) {
        console.error('요약 통계 로딩 실패', error);
    }
}

async function loadMonthlyConsultationChartSafe() {
    try {
        await loadMonthlyConsultationChart();
    } catch (error) {
        console.error('월간 상담 추이 차트 로딩 실패', error);
    }
}

async function loadVehicleDemandChartSafe() {
    try {
        await loadVehicleDemandChart();
    } catch (error) {
        console.error('차량 모델별 수요 차트 로딩 실패', error);
    }
}

async function loadRegionConsultationChartSafe() {
    try {
        await loadRegionConsultationChart();
    } catch (error) {
        console.error('지역별 상담 분포 차트 로딩 실패', error);
    }
}

async function loadConsultationResultChartSafe() {
    try {
        await loadConsultationResultChart();
    } catch (error) {
        console.error('상담 결과 차트 로딩 실패', error);
    }
}

async function loadComparisonSafe() {
    try {
        await loadComparison();
    } catch (error) {
        console.error('전년 동기 비교 로딩 실패', error);
    }
}

async function loadSummary() {
    const data = await fetchJson('/admin/analytics/api/summary');

    setText('totalConsultationCount', `${data.totalConsultationCount}건`);
    setText('monthlyConsultationCount', `${data.monthlyConsultationCount}건`);
    setText('contractConversionRate', `${data.contractConversionRate}%`);
}

async function loadMonthlyConsultationChart() {
    const data = await fetchJson('/admin/analytics/api/monthly');

    const labels = Array.isArray(data)
        ? data.map(item => formatMonthLabel(item.monthLabel))
        : [];

    const values = Array.isArray(data)
        ? data.map(item => Number(item.consultationCount) || 0)
        : [];

    renderLineChart('monthlyConsultationChart', labels, values, '상담 건수', 50);
}

async function loadVehicleDemandChart() {
    const data = await fetchJson('/admin/analytics/api/vehicle-demand');

    const labels = Array.isArray(data) ? data.map(item => convertVehicleLabel(item.modelName)) : [];
    const values = Array.isArray(data) ? data.map(item => Number(item.consultationCount) || 0) : [];

    renderBarChart('vehicleDemandChart', labels, values, '상담 건수', 50);
}

async function loadRegionConsultationChart() {
    const data = await fetchJson('/admin/analytics/api/region');

    const labels = Array.isArray(data) ? data.map(item => convertRegionLabel(item.regionName)) : [];
    const values = Array.isArray(data) ? data.map(item => Number(item.consultationCount) || 0) : [];

    renderBarChart('regionConsultationChart', labels, values, '상담 건수', 50);
}

async function loadConsultationResultChart() {
    const data = await fetchJson('/admin/analytics/api/result');

    const labels = Array.isArray(data) ? data.map(item => convertResultLabel(item.resultName)) : [];
    const values = Array.isArray(data) ? data.map(item => Number(item.consultationCount) || 0) : [];

    renderDoughnutChart('consultationResultChart', labels, values, '상담 결과');
}

async function loadComparison() {
    const data = await fetchJson('/admin/analytics/api/comparison');

    setText('comparisonConsultationChangeRate', formatSignedPercent(data.consultationChangeRate));
    setText(
        'comparisonConsultationCountText',
        `최근 1년 ${data.currentConsultationCount}건 / 전년 ${data.previousConsultationCount}건`
    );

    setText('comparisonConversionRateGap', formatSignedPoint(data.conversionRateGap));
    setText(
        'comparisonConversionRateText',
        `최근 1년 ${data.currentConversionRate}% / 전년 ${data.previousConversionRate}%`
    );

    setText('comparisonTopModelName', convertVehicleLabel(data.topModelName));
    setText('comparisonTopRegionName', data.topRegionName || '-');
}

function setText(elementId, value) {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = value;
    }
}

function formatSignedPercent(value) {
    const numberValue = Number(value) || 0;
    return `${numberValue > 0 ? '+' : ''}${numberValue}%`;
}

function formatSignedPoint(value) {
    const numberValue = Number(value) || 0;
    return `${numberValue > 0 ? '+' : ''}${numberValue}%p`;
}

function formatMonthLabel(monthLabel) {
    if (!monthLabel || monthLabel.length < 7) {
        return monthLabel || '';
    }

    const year = monthLabel.substring(2, 4);
    const month = monthLabel.substring(5, 7);
    return `${year}.${month}`;
}

function convertVehicleLabel(modelName) {
    if (!modelName) {
        return '-';
    }

    return VEHICLE_NAME_MAP[modelName] || modelName.replace(/_/g, ' ');
}

function convertRegionLabel(regionName) {
    if (!regionName) {
        return '';
    }

    const parts = regionName.trim().split(/\s+/);

    if (parts.length <= 1) {
        return regionName;
    }

    if (parts.length === 2) {
        return [parts[0], parts[1]];
    }

    return [parts[0], parts.slice(1).join(' ')];
}

function convertResultLabel(resultName) {
    if (resultName === 'PURCHASE') {
        return '구매';
    }
    if (resultName === 'NOT_PURCHASE') {
        return '미구매';
    }
    if (resultName === 'UNDECIDED') {
        return '보류';
    }
    if (resultName === 'CONTRACT') {
        return '계약';
    }
    if (resultName === '미정' || resultName === null || resultName === '') {
        return '미정';
    }
    return resultName;
}

function renderLineChart(canvasId, labels, values, label, maxY) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) {
        return;
    }

    new Chart(canvas, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: label,
                    data: values,
                    borderWidth: 3,
                    tension: 0.35,
                    fill: true,
                    pointRadius: 4,
                    pointHoverRadius: 5
                }
            ]
        },
        options: getCartesianChartOptions(maxY)
    });
}

function renderBarChart(canvasId, labels, values, label, maxY) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) {
        return;
    }

    new Chart(canvas, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: label,
                    data: values,
                    borderWidth: 2
                }
            ]
        },
        options: getCartesianChartOptions(maxY)
    });
}

function renderDoughnutChart(canvasId, labels, values, label) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) {
        return;
    }

    new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [
                {
                    label: label,
                    data: values,
                    borderWidth: 2,
                    radius: '74%'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '62%',
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        color: '#e5eefc',
                        boxWidth: 18,
                        padding: 12
                    }
                }
            }
        }
    });
}

function getCartesianChartOptions(maxY) {
    const yOptions = {
        beginAtZero: true,
        ticks: {
            color: '#b8c7e0',
            precision: 0,
            stepSize: 5
        },
        grid: {
            color: 'rgba(255,255,255,0.06)'
        }
    };

    if (typeof maxY === 'number') {
        yOptions.max = maxY;
    }

    return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                labels: {
                    color: '#e5eefc'
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: '#b8c7e0',
                    maxRotation: 0,
                    minRotation: 0,
                    autoSkip: false,
                    padding: 8
                },
                grid: {
                    color: 'rgba(255,255,255,0.06)'
                }
            },
            y: yOptions
        }
    };
}