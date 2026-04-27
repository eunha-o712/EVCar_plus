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
    const quickButtons = document.querySelectorAll('.ev-floating-chatbot__quick-btn');

    if (!fab || !panel || !closeBtn || !form || !input || !messages || !userIdInput) {
        return;
    }

    window.evChatbotInitialized = true;

    let isSending = false;

    function escapeHtml(text) {
        return String(text)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    function nl2br(text) {
        return escapeHtml(text).replace(/\n/g, '<br>');
    }

    function openPanel() {
        panel.style.display = 'flex';
        setTimeout(function () {
            input.focus();
        }, 50);
    }

    function closePanel() {
        panel.style.display = 'none';
    }

    function scrollToBottom() {
        messages.scrollTop = messages.scrollHeight;
    }

    function createMessageElement(role, htmlContent) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'ev-floating-chatbot__message ' +
            (role === 'user'
                ? 'ev-floating-chatbot__message--user'
                : 'ev-floating-chatbot__message--bot');

        if (role === 'bot') {
            messageDiv.classList.add('ev-floating-chatbot__message--animated');
        }

        messageDiv.innerHTML = htmlContent;
        return messageDiv;
    }

    function appendUserMessage(text) {
        const messageEl = createMessageElement('user', nl2br(text));
        messages.appendChild(messageEl);
        scrollToBottom();
    }

    function createLoadingMessage() {
        const loadingDiv = createMessageElement('bot', `
            <div class="ev-chatbot-loading">
                <span class="ev-chatbot-loading__dot"></span>
                <span class="ev-chatbot-loading__dot"></span>
                <span class="ev-chatbot-loading__dot"></span>
            </div>
        `);

        messages.appendChild(loadingDiv);
        scrollToBottom();

        return loadingDiv;
    }

    function buildConsultLinkCard() {
        return `
            <div class="ev-chatbot-consult-link-wrap">
                <a href="/consultation" class="ev-chatbot-consult-link">
                    <img src="/images/ev_chat_1.png"
                         alt="상담게시판 바로가기"
                         class="ev-chatbot-consult-link__icon">
                    <span class="ev-chatbot-consult-link__text">상담게시판 바로가기</span>
                </a>
            </div>
        `;
    }

    function renderBotMessage(message) {
        const safeMessage = escapeHtml(message);

        if (safeMessage.includes('/consultation')) {
            const cleaned = safeMessage
                .replace(/\/consultation/g, '')
                .replace(/\n{2,}/g, '\n')
                .trim();

            return `
                <div class="ev-chatbot-bot-content">
                    ${cleaned ? `<div>${cleaned.replace(/\n/g, '<br>')}</div>` : ''}
                    ${buildConsultLinkCard()}
                </div>
            `;
        }

        return nl2br(message);
    }

    function appendBotMessage(text) {
        const html = renderBotMessage(text);
        const messageEl = createMessageElement('bot', html);
        messages.appendChild(messageEl);
        scrollToBottom();
    }

    async function sendMessage(messageText) {
        const userMessage = String(messageText || '').trim();
        const userId = String(userIdInput.value || '').trim();

        if (!userId || !userMessage || isSending) {
            return;
        }

        isSending = true;
        input.disabled = true;

        appendUserMessage(userMessage);
        input.value = '';

        const loadingEl = createLoadingMessage();

        try {
            const response = await fetch('/api/chatbot/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    userMessage: userMessage
                })
            });

            if (!response.ok) {
                throw new Error('챗봇 요청에 실패했습니다.');
            }

            const data = await response.json();
            loadingEl.remove();

            appendBotMessage(data.reply || '응답을 가져오지 못했습니다.');
        } catch (error) {
            console.error(error);
            loadingEl.remove();
            appendBotMessage('일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        } finally {
            isSending = false;
            input.disabled = false;
            input.focus();
        }
    }

    fab.addEventListener('click', function () {
        if (panel.style.display === 'none' || panel.style.display === '') {
            openPanel();
            return;
        }

        closePanel();
    });

    closeBtn.addEventListener('click', closePanel);

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        sendMessage(input.value);
    });

    quickButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            sendMessage(button.getAttribute('data-message'));
        });
    });
});