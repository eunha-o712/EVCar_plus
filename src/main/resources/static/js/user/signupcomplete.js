'use strict';

document.addEventListener('DOMContentLoaded', () => {
    preventBackToSignupComplete();
    redirectOnReload();
});

function preventBackToSignupComplete() {
    history.pushState(null, '', location.href);

    window.addEventListener('popstate', () => {
        window.location.href = '/login';
    });
}

function redirectOnReload() {
    const navigationEntries = performance.getEntriesByType('navigation');

    if (!navigationEntries.length) {
        return;
    }

    if (navigationEntries[0].type === 'reload') {
        window.location.href = '/login';
    }
}