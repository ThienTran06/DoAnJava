/* ============================================================
   BookHouse – Shared Toast Notification System
   Self-contained: injects its own CSS + container element.
   Usage: showToast('Message', 'success' | 'error' | 'info')
   ============================================================ */
(function () {
  'use strict';

  /* ── CSS ── */
  var style = document.createElement('style');
  style.textContent =
    '.bh-toast-container{position:fixed;bottom:24px;right:24px;z-index:9999;display:flex;flex-direction:column-reverse;gap:10px;pointer-events:none}' +
    '.bh-toast{display:flex;align-items:center;gap:10px;min-width:260px;max-width:min(400px,calc(100vw - 48px));padding:12px 16px;border-radius:12px;' +
    'font-family:inherit;font-size:13.5px;font-weight:600;line-height:1.35;pointer-events:auto;' +
    'box-shadow:0 6px 24px rgba(0,0,0,.12);opacity:0;transform:translateY(12px);transition:opacity .25s ease,transform .25s ease}' +
    '.bh-toast.show{opacity:1;transform:translateY(0)}' +
    '.bh-toast.hide{opacity:0;transform:translateY(-8px)}' +
    '.bh-toast-icon{flex-shrink:0;width:20px;height:20px}' +
    '.bh-toast-msg{flex:1;word-break:break-word}' +
    '.bh-toast-close{flex-shrink:0;width:18px;height:18px;cursor:pointer;opacity:.55;transition:opacity .15s}' +
    '.bh-toast-close:hover{opacity:1}' +
    /* Type-specific colours */
    '.bh-toast--success{background:var(--green,#3a7d44);color:#fff}' +
    '.bh-toast--error{background:var(--red,#c0392b);color:#fff}' +
    '.bh-toast--info{background:var(--surface,#fff);color:var(--text,#1f1f1f);border:1.5px solid var(--border,#e0e0e0)}';
  document.head.appendChild(style);

  /* ── Container (deferred until body exists) ── */
  var container = document.createElement('div');
  container.className = 'bh-toast-container';

  function ensureContainer() {
    if (!container.parentNode && document.body) {
      document.body.appendChild(container);
    }
  }
  if (document.body) {
    ensureContainer();
  } else {
    document.addEventListener('DOMContentLoaded', ensureContainer);
  }

  /* ── SVG Icons ── */
  var icons = {
    success: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>',
    error: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>',
    info: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>'
  };

  var closeSvg = '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>';

  /* ── Public API ── */
  function showToast(message, type) {
    ensureContainer();
    type = type || 'info';
    if (!icons[type]) type = 'info';

    var el = document.createElement('div');
    el.className = 'bh-toast bh-toast--' + type;

    var icon = document.createElement('span');
    icon.className = 'bh-toast-icon';
    icon.innerHTML = icons[type];

    var msg = document.createElement('span');
    msg.className = 'bh-toast-msg';
    msg.textContent = message;

    var close = document.createElement('span');
    close.className = 'bh-toast-close';
    close.innerHTML = closeSvg;
    close.onclick = function () { dismiss(el); };

    el.appendChild(icon);
    el.appendChild(msg);
    el.appendChild(close);
    container.appendChild(el);

    /* trigger reflow then show */
    el.offsetHeight; // eslint-disable-line no-unused-expressions
    el.classList.add('show');

    var timer = setTimeout(function () { dismiss(el); }, 3500);
    el._timer = timer;
  }

  function dismiss(el) {
    if (el._dismissed) return;
    el._dismissed = true;
    clearTimeout(el._timer);
    el.classList.remove('show');
    el.classList.add('hide');
    setTimeout(function () { if (el.parentNode) el.parentNode.removeChild(el); }, 300);
  }

  /* Expose globally */
  window.showToast = showToast;
})();
