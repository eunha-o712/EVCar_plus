@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.min.css');

:root {
    --ev-header-gradient: linear-gradient(to right, #080045, #0E3A5F);
    --ev-header-height: 96px;
    --ev-layout-width: 1440px;

    --ev-white: #FFFFFF;
    --ev-dark: #050816;
    --ev-footer-bg: #07111F;
    --ev-footer-panel: #F8FAFC;
    --ev-footer-border: #D8E1EA;
    --ev-text: #111827;
    --ev-muted: #667085;
    --ev-accent: #0E3A5F;
    --ev-accent-light: #EAF2FF;
}

* {
    box-sizing: border-box;
}

html {
    font-size: 16px;
}

body {
    margin: 0;
    font-family: 'Pretendard', -apple-system, BlinkMacSystemFont, sans-serif;
    color: var(--ev-text);
    background-color: #FFFFFF;
    letter-spacing: -0.01em;
}

img {
    display: block;
    max-width: 100%;
}

a {
    color: inherit;
    text-decoration: none;
}

button {
    font: inherit;
}

ul,
ol {
    margin: 0;
    padding: 0;
    list-style: none;
}

p,
h1,
h2,
h3,
h4,
h5,
h6 {
    margin: 0;
}

.ev-container {
    width: min(var(--ev-layout-width), calc(100% - 80px));
    margin: 0 auto;
}

.ev-main {
    min-height: 100vh;
}

.ev-header {
    position: sticky;
    top: 0;
    z-index: 1200;
    width: 100%;
    background: var(--ev-header-gradient);
    box-shadow: 0 12px 28px rgba(5, 12, 29, 0.22);
}

.ev-header__inner {
    display: flex;
    align-items: center;
    justify-content: space-between