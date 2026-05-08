'use strict';

document.addEventListener('DOMContentLoaded', function () {

    if (window.evChatbotInitialized) {
        return;
    }

    const fab = document.getElementById('evChatbotFab');
    const panel = document.getElementById('evChatbotPanel');
    const closeBtn = document.getElementById('evChatbotClose');

    const form = document.getElementById('evChatbotForm');
    const input = document.getElementById('evChatbotInput');

    const messages = document.getElementById('evChatbotMessages');

    const userIdInput = document.getElementById('evChatbotUserId');

    const quickButtons = document.querySelectorAll(
        '.ev-floating-chatbot__quick-btn'
    );

    const tooltip = document.getElementById(
        'evChatbotTooltip'
    );

    if (
        !fab ||
        !panel ||
        !closeBtn ||
        !form ||
        !input ||
        !messages ||
        !userIdInput
    ) {
        return;
    }

    window.evChatbotInitialized = true;

    let isSending = false;

    const tooltipMessages = [
        'HI~ AI 이카봇이에요 ⚡',
        '차량 간편 상담 맡겨주세요 💌'
    ];

    const shortcutLinks = [
        {
            url: '/consultation',
            text: '상담게시판 바로가기'
        },
        {
            url: '/faq',
            text: 'FAQ 바로가기'
        },
        {
            url: '/charging/map',
            text: '충전소 지도 바로가기'
        }
    ];

    let tooltipMessageIndex = 0;
    let tooltipTimerId = null;

    function escapeHtml(text) {

        return String(text)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    function nl2br(text) {

        return escapeHtml(text)
            .replace(/\n/g, '<br>');
    }

    function cleanBotText(text) {

        return String(text || '')
            .replace(/\*\*/g, '')
            .replace(/###/g, '')
            .replace(/##/g, '')
            .replace(/# /g, '')
            .replace(/^- /gm, '')
            .replace(/^\* /gm, '')
            .replace(/`/g, '')
            .replace(/\|---\|---\|---\|/g, '')
            .replace(/\|/g, ' ')
            .replace(/\s{2,}/g, ' ')
            .trim();
    }

    function showWaitingTooltip() {

        if (
            !tooltip ||
            panel.style.display === 'flex'
        ) {
            return;
        }

        tooltip.textContent =
            tooltipMessages[tooltipMessageIndex];

        tooltip.classList.add('is-active');

        window.setTimeout(function () {

            tooltip.classList.remove('is-active');

        }, 2600);

        tooltipMessageIndex =
            (tooltipMessageIndex + 1)
            % tooltipMessages.length;
    }

    function startWaitingTooltip() {

        if (
            !tooltip ||
            tooltipTimerId
        ) {
            return;
        }

        showWaitingTooltip();

        tooltipTimerId =
            window.setInterval(function () {

                showWaitingTooltip();

            }, 5000);
    }

    function stopWaitingTooltip() {

        if (tooltipTimerId) {

            window.clearInterval(
                tooltipTimerId
            );

            tooltipTimerId = null;
        }

        if (tooltip) {

            tooltip.classList.remove(
                'is-active'
            );
        }
    }

    function openPanel() {

        stopWaitingTooltip();

        panel.style.display = 'flex';

        setTimeout(function () {

            input.focus();

        }, 50);
    }

    function closePanel() {

        panel.style.display = 'none';

        startWaitingTooltip();
    }

    function scrollToBottom() {

        messages.scrollTop =
            messages.scrollHeight;
    }

    function createMessageElement(
        role,
        htmlContent
    ) {

        const messageDiv =
            document.createElement('div');

        messageDiv.className =
            'ev-floating-chatbot__message ' +
            (
                role === 'user'
                    ? 'ev-floating-chatbot__message--user'
                    : 'ev-floating-chatbot__message--bot'
            );

        if (role === 'bot') {

            messageDiv.classList.add(
                'ev-floating-chatbot__message--animated'
            );
        }

        messageDiv.innerHTML =
            htmlContent;

        return messageDiv;
    }

    function appendUserMessage(text) {

        const messageEl =
            createMessageElement(
                'user',
                nl2br(text)
            );

        messages.appendChild(
            messageEl
        );

        scrollToBottom();
    }

    function createLoadingMessage() {

        const loadingDiv =
            createMessageElement(
                'bot',
                `
                <div class="ev-chatbot-loading">
                    <span class="ev-chatbot-loading__dot"></span>
                    <span class="ev-chatbot-loading__dot"></span>
                    <span class="ev-chatbot-loading__dot"></span>
                </div>
                `
            );

        messages.appendChild(
            loadingDiv
        );

        scrollToBottom();

        return loadingDiv;
    }

    function buildShortcutLinkCard(shortcut) {

        return `
            <div class="ev-chatbot-consult-link-wrap">

                <a href="${shortcut.url}"
                   class="ev-chatbot-consult-link">

                    <img src="/images/ev_chat_1.png"
                         alt="${shortcut.text}"
                         class="ev-chatbot-consult-link__icon">

                    <span class="ev-chatbot-consult-link__text">
                        ${shortcut.text}
                    </span>

                </a>

            </div>
        `;
    }

    function formatBotMessage(text) {

        const cleanedText =
            cleanBotText(text);

        return cleanedText
            .split('\n')
            .filter(function (line) {

                return line.trim() !== '';

            })
            .map(function (line) {

                return `<p>${line}</p>`;

            })
            .join('');
    }

    function renderBotMessage(message) {

        const safeMessage =
            escapeHtml(message);

        const matchedShortcuts =
            shortcutLinks.filter(
                function (shortcut) {

                    return safeMessage.includes(
                        shortcut.url
                    );
                }
            );

        let cleaned =
            safeMessage;

        matchedShortcuts.forEach(
            function (shortcut) {

                cleaned = cleaned
                    .split(shortcut.url)
                    .join('');
            }
        );

        cleaned = cleaned.trim();

        return `
            <div class="ev-chatbot-bot-content">

                ${formatBotMessage(cleaned)}

                ${
                    matchedShortcuts
                        .map(buildShortcutLinkCard)
                        .join('')
                }

            </div>
        `;
    }

    function appendBotMessage(text) {

        const html =
            renderBotMessage(text);

        const messageEl =
            createMessageElement(
                'bot',
                html
            );

        messages.appendChild(
            messageEl
        );

        scrollToBottom();
    }

    async function sendMessage(messageText) {

        const userMessage =
            String(messageText || '')
                .trim();

        const userId =
            String(
                userIdInput.value || ''
            ).trim();

        if (
            !userId ||
            !userMessage ||
            isSending
        ) {
            return;
        }

        isSending = true;

        input.disabled = true;

        appendUserMessage(
            userMessage
        );

        input.value = '';

        const loadingEl =
            createLoadingMessage();

        try {

            const response =
                await fetch(
                    '/api/chatbot/send',
                    {
                        method: 'POST',

                        headers: {
                            'Content-Type':
                                'application/json'
                        },

                        body: JSON.stringify({
                            userId: userId,
                            userMessage: userMessage
                        })
                    }
                );

            if (!response.ok) {

                throw new Error(
                    '챗봇 요청 실패'
                );
            }

            const data =
                await response.json();

            loadingEl.remove();

            appendBotMessage(
                data.reply ||
                '응답을 가져오지 못했습니다.'
            );

        } catch (error) {

            console.error(error);

            loadingEl.remove();

            appendBotMessage(
                '일시적인 오류가 발생했습니다.'
            );

        } finally {

            isSending = false;

            input.disabled = false;

            input.focus();
        }
    }

    startWaitingTooltip();

    fab.addEventListener(
        'click',
        function () {

            if (
                panel.style.display === 'none' ||
                panel.style.display === ''
            ) {

                openPanel();

                return;
            }

            closePanel();
        }
    );

    closeBtn.addEventListener(
        'click',
        closePanel
    );

    form.addEventListener(
        'submit',
        function (event) {

            event.preventDefault();

            sendMessage(
                input.value
            );
        }
    );

    quickButtons.forEach(function (button) {

        button.addEventListener(
            'click',
            function () {

                const shortcut =
                    button.getAttribute(
                        'data-shortcut'
                    );

                if (shortcut === 'charging') {

                    appendUserMessage(
                        '충전소 찾기'
                    );

                    appendBotMessage(
                        '충전소 위치와 이용 가능 여부는 충전소 지도에서 확인할 수 있습니다.\n/charging/map'
                    );

                    return;
                }

                if (shortcut === 'faq') {

                    appendUserMessage(
                        'FAQ 상담'
                    );

                    appendBotMessage(
                        '자주 묻는 질문은 FAQ 페이지에서 확인할 수 있습니다.\n/faq'
                    );

                    return;
                }

                if (shortcut === 'consultation') {

                    appendUserMessage(
                        '상담 예약'
                    );

                    appendBotMessage(
                        '상담 예약은 고객센터 상담게시판에서 진행할 수 있습니다.\n/consultation'
                    );

                    return;
                }

                sendMessage(
                    '전기차 추천해줘'
                );
            }
        );
    });
});