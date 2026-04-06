'use strict';

document.addEventListener('DOMContentLoaded', async () => {
    try {
        await Promise.all([
            loadSummary(),
            loadMonthlyConsultationChart(),
            loadVehicleDemandChart(),
            loadRegionConsultationChart(),
            loadConsultationResultChart()
        ]);
    } catch (error) {
        console.error('통계 데이터를 불러오지 못했습니다.', error);
    }
});

async function fetchJson(url) {
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`요청 실패: ${url}`);
    }

    return response.json();
}

async function loadSummary() {
    const data = await fetchJson('/admin/analytics/api/summary');

    document.getElementById('totalConsultationCount').textContent = `${data.totalConsultationCount}건`;
    document.getElementById('monthlyConsultationCount').textContent = `${data.monthlyConsultationCount}건`;
    document.getElementById('contractConversionRate').textContent = `${data.contractConversionRate}%`;
}

async function loadMonthlyConsultationChart() {
    const data = await fetchJson('/admin/analytics/api/monthly');
    const labels = data.map(item => item.monthLabel);
    const values = data.map(item => item.consultationCount);

    renderChart('monthlyConsultationChart', 'line', labels, values, '상담 건수');
}

async function loadVehicleDemandChart() {
    const data = await fetchJson('/admin/analytics/api/vehicle-demand');
    const labels = data.map(item => item.modelName);
    const values = data.map(item => item.consultationCount);

    renderChart('vehicleDemandChart', 'bar', labels, values, '상담 건수');
}

async function loadRegionConsultationChart() {
    const data = await fetchJson('/admin/analytics/api/region');
    const labels = data.map(item => item.regionName);
    const values = data.map(item => item.consultationCount);

    renderChart('regionConsultationChart', 'bar', labels, values, '상담 건수');
}

async function loadConsultationResultChart() {
    const data = await fetchJson('/admin/analytics/api/result');
    const labels = data.map(item => convertResultLabel(item.resultName));
    const values = data.map(item => item.consultationCount);

    renderChart('consultationResultChart', 'doughnut', labels, values, '상담 결과');
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
    if (resultName === '미정' || resultName === null || resultName === '') {
        return '미정';
    }
    return resultName;
}

function renderChart(canvasId, type, labels, values, label) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) {
        return;
    }

    new Chart(canvas, {
        type: type,
        data: {
            labels: labels,
            datasets: [
                {
                    label: label,
                    data: values,
                    borderWidth: 2,
                    tension: 0.3
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    labels: {
                        color: '#e5eefc'
                    }
                }
            },
            scales: type === 'doughnut'
                    ? {}
                    : {
                        x: {
                            ticks: {
                                color: '#b8c7e0'
                            },
                            grid: {
                                color: 'rgba(255,255,255,0.06)'
                            }
                        },
                        y: {
                            beginAtZero: true,
                            ticks: {
                                color: '#b8c7e0',
                                precision: 0
                            },
                            grid: {
                                color: 'rgba(255,255,255,0.06)'
                            }
                        }
                    }
        }
    });
}