/* BookHouse shared notification center */
(function () {
  "use strict";

  const STORAGE_KEY = "bookhouse_notifications_v1";
  const MAX_ITEMS = 80;
  const LOW_STOCK_THRESHOLD = 5;

  const typeMeta = {
    invoice: { icon: "🧾", label: "Hóa đơn" },
    stock: { icon: "📦", label: "Tồn kho" },
    reservation: { icon: "📌", label: "Phiếu giữ" },
    import: { icon: "📥", label: "Phiếu nhập" },
    review: { icon: "💬", label: "Nhận xét" },
    kpi: { icon: "🎯", label: "KPI" },
    system: { icon: "🔔", label: "Hệ thống" }
  };

  function nowIso() {
    return new Date().toISOString();
  }

  function todayKey() {
    return new Date().toISOString().slice(0, 10);
  }

  function readItems() {
    try {
      const parsed = JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]");
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  function normalizeAuthValue(value) {
    return String(value || "")
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "")
      .replace(/[\s-]+/g, "_")
      .toUpperCase();
  }

  function canUseImportFlow() {
    var payload = decodeJwtPayload();
    var role = normalizeAuthValue(payload && payload.role);
    if (role === "ADMIN" || role === "KHO") return true;
    var permissions = Array.isArray(payload && payload.permissions) ? payload.permissions : [];
    return permissions.some(function (p) {
      return normalizeAuthValue(p) === "QUAN_LY_PHIEU_NHAP";
    });
  }

  function canSeeNotification(item) {
    return !item || item.type !== "stock" || canUseImportFlow();
  }

  function getVisibleItems() {
    return readItems().filter(canSeeNotification);
  }

  function writeItems(items) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items.slice(0, MAX_ITEMS)));
  }

  function formatTime(value) {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return "";
    return date.toLocaleString("vi-VN", {
      hour: "2-digit",
      minute: "2-digit",
      day: "2-digit",
      month: "2-digit"
    });
  }

  function escapeHtml(value) {
    return String(value || "")
      .replaceAll("&", "&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll('"', "&quot;")
      .replaceAll("'", "&#039;");
  }

  function injectStyles() {
    if (document.getElementById("bhNotificationStyles")) return;
    const style = document.createElement("style");
    style.id = "bhNotificationStyles";
    style.textContent = `
      .bh-notification-anchor{position:relative}
      .bh-notification-bell{position:relative;display:inline-flex;align-items:center;justify-content:center;width:40px;height:40px;border:1px solid var(--border,#e5e7eb);background:var(--surface,#fff);border-radius:10px;cursor:pointer;color:var(--text,#111827);font-size:18px;box-shadow:0 4px 14px rgba(15,23,42,.06)}
      .icon-btn.bh-notification-anchor{overflow:visible}
      .icon-btn.bh-notification-anchor .bh-notification-bell{width:100%;height:100%;border:0;background:transparent;border-radius:inherit;box-shadow:none;padding:0}
      .bh-notification-bell svg{width:18px;height:18px;display:block;stroke:currentColor}
      .bh-notification-anchor .notif-dot,.bh-notification-anchor .nav-badge,.bh-notification-anchor .badge:not(.bh-notification-badge){display:none!important}
      .bh-notification-bell:hover{background:var(--surface-2,#f8fafc)}
      .bh-notification-badge{position:absolute;top:-5px;right:-5px;min-width:18px;height:18px;padding:0 5px;border-radius:999px;background:#dc2626;color:#fff;font-size:11px;font-weight:800;line-height:18px;text-align:center;display:none}
      .bh-notification-badge.show{display:block}
      .bh-notification-panel{position:absolute;top:48px;right:0;width:min(360px,calc(100vw - 28px));max-height:440px;background:var(--surface,#fff);color:var(--text,#111827);border:1px solid var(--border,#e5e7eb);border-radius:12px;box-shadow:0 22px 60px rgba(15,23,42,.22);z-index:10000;display:none;overflow:hidden}
      .bh-notification-panel.open{display:block}
      .bh-notification-head{display:flex;align-items:center;justify-content:space-between;padding:13px 14px;border-bottom:1px solid var(--border,#e5e7eb)}
      .bh-notification-title{font-size:14px;font-weight:800}
      .bh-notification-clear{border:0;background:transparent;color:var(--muted,#64748b);font-weight:700;font-size:12px;cursor:pointer}
      .bh-notification-list{max-height:350px;overflow-y:auto}
      .bh-notification-item{display:flex;gap:10px;padding:12px 14px;border-bottom:1px solid var(--border,#eef2f7);text-decoration:none;color:inherit;background:transparent}
      .bh-notification-item.unread{background:rgba(245,158,11,.09)}
      .bh-notification-item:hover{background:rgba(15,23,42,.04)}
      .bh-notification-icon{width:30px;height:30px;display:flex;align-items:center;justify-content:center;border-radius:9px;background:#f8ecd2;flex:0 0 auto}
      .bh-notification-copy{min-width:0;flex:1}
      .bh-notification-row{display:flex;align-items:center;justify-content:space-between;gap:8px}
      .bh-notification-name{font-size:13px;font-weight:800;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}
      .bh-notification-time{font-size:11px;color:var(--muted,#64748b);white-space:nowrap}
      .bh-notification-message{margin-top:3px;font-size:12px;color:var(--muted,#64748b);line-height:1.35}
      .bh-notification-empty{padding:28px 16px;text-align:center;color:var(--muted,#64748b);font-size:13px}
      .bh-notification-toast-wrap{position:fixed;right:22px;bottom:22px;z-index:12000;display:flex;flex-direction:column;gap:10px;pointer-events:none}
      .bh-notification-toast{width:min(360px,calc(100vw - 44px));border-radius:12px;background:#111827;color:#fff;box-shadow:0 18px 48px rgba(15,23,42,.28);padding:12px 14px;display:flex;gap:10px;align-items:flex-start;pointer-events:auto;opacity:0;transform:translateY(12px);transition:opacity .2s ease,transform .2s ease}
      .bh-notification-toast.show{opacity:1;transform:translateY(0)}
      .bh-notification-toast strong{display:block;font-size:13px;margin-bottom:2px}
      .bh-notification-toast span{font-size:12px;line-height:1.35;color:#d1d5db}
    `;
    document.head.appendChild(style);
  }

  function ensureToastWrap() {
    let wrap = document.querySelector(".bh-notification-toast-wrap");
    if (!wrap) {
      wrap = document.createElement("div");
      wrap.className = "bh-notification-toast-wrap";
      document.body.appendChild(wrap);
    }
    return wrap;
  }

  function showFallbackToast(item) {
    if (!document.body) return;
    const wrap = ensureToastWrap();
    const meta = typeMeta[item.type] || typeMeta.system;
    const toast = document.createElement("div");
    toast.className = "bh-notification-toast";
    toast.innerHTML = `
      <div>${meta.icon}</div>
      <div><strong>${escapeHtml(item.title)}</strong><span>${escapeHtml(item.message)}</span></div>
    `;
    wrap.appendChild(toast);
    requestAnimationFrame(() => toast.classList.add("show"));
    setTimeout(() => {
      toast.classList.remove("show");
      setTimeout(() => toast.remove(), 220);
    }, 4200);
  }

  function showToastFor(item) {
    if (typeof window.showToast === "function") {
      window.showToast(`${item.title}: ${item.message}`, "info");
      return;
    }
    showFallbackToast(item);
  }

  function pushNotification(payload) {
    if (payload && payload.type === "stock" && !canUseImportFlow()) {
      return null;
    }
    const item = {
      id: payload.id || `${Date.now()}-${Math.random().toString(16).slice(2)}`,
      type: payload.type || "system",
      title: payload.title || "Thông báo mới",
      message: payload.message || "",
      href: payload.href || "",
      createdAt: payload.createdAt || nowIso(),
      read: false,
      dedupeKey: payload.dedupeKey || ""
    };
    const items = readItems();
    if (item.dedupeKey && items.some(existing => existing.dedupeKey === item.dedupeKey)) {
      return null;
    }
    items.unshift(item);
    writeItems(items);
    renderNotificationCenter();
    showToastFor(item);
    return item;
  }

  function markAllRead() {
    const items = readItems().map(item => ({ ...item, read: true }));
    writeItems(items);
    renderNotificationCenter();
  }

  function markOneRead(id) {
    const items = readItems().map(item => item.id === id ? { ...item, read: true } : item);
    writeItems(items);
    renderNotificationCenter();
  }

  function getUnreadCount() {
    return getVisibleItems().filter(item => !item.read).length;
  }

  function renderList(panel) {
    const list = panel.querySelector(".bh-notification-list");
    const items = getVisibleItems();
    if (!items.length) {
      list.innerHTML = '<div class="bh-notification-empty">Chưa có thông báo nào</div>';
      return;
    }
    list.innerHTML = items.map(item => {
      const meta = typeMeta[item.type] || typeMeta.system;
      const tag = item.href ? "a" : "div";
      const href = item.href ? ` href="${escapeHtml(item.href)}"` : "";
      return `
        <${tag}${href} class="bh-notification-item ${item.read ? "" : "unread"}" data-notification-id="${escapeHtml(item.id)}">
          <div class="bh-notification-icon">${meta.icon}</div>
          <div class="bh-notification-copy">
            <div class="bh-notification-row">
              <div class="bh-notification-name">${escapeHtml(item.title)}</div>
              <div class="bh-notification-time">${formatTime(item.createdAt)}</div>
            </div>
            <div class="bh-notification-message">${escapeHtml(item.message)}</div>
          </div>
        </${tag}>
      `;
    }).join("");
  }

  function renderNotificationCenter() {
    const badge = document.querySelector(".bh-notification-badge");
    const panel = document.querySelector(".bh-notification-panel");
    const count = getUnreadCount();
    if (badge) {
      badge.textContent = count > 9 ? "9+" : String(count);
      badge.classList.toggle("show", count > 0);
    }
    document.querySelectorAll("[data-bh-notification-host] > .app-ui-icon").forEach(icon => icon.remove());
    document.querySelectorAll(".notif-dot").forEach(dot => {
      if (dot.closest("[data-bh-notification-host]")) {
        dot.remove();
        return;
      }
      dot.style.display = count > 0 ? "" : "none";
    });
    if (panel) renderList(panel);
  }

  function findBellHost() {
    const existing = document.querySelector("[data-bh-notification-host]");
    if (existing) return existing;
    const appNotificationButton = document.querySelector(".topbar-actions .app-notification-btn");
    if (appNotificationButton) return appNotificationButton;
    const dotButton = document.querySelector(".topbar-actions .icon-btn:has(.notif-dot)");
    if (dotButton) return dotButton;
    const iconBtns = Array.from(document.querySelectorAll(".topbar-actions .icon-btn"));
    return iconBtns.find(button => !button.id || button.id !== "topbarThemeBtn") || null;
  }

  function cleanupDuplicateNotificationButtons(host) {
    document.querySelectorAll(".topbar-actions .app-notification-btn").forEach(button => {
      if (button !== host) button.remove();
    });
    document.querySelectorAll("#appNotificationMenu").forEach(menu => menu.remove());
  }

  function initCenter() {
    if (!document.body) return;
    injectStyles();
    const host = findBellHost();
    if (!host || host.dataset.bhNotificationReady === "1") {
      renderNotificationCenter();
      return;
    }
    host.dataset.bhNotificationReady = "1";
    host.dataset.bhNotificationHost = "1";
    host.classList.add("bh-notification-anchor", "app-notification-btn");
    host.querySelectorAll(".notif-dot,.app-notification-badge,.app-ui-icon").forEach(el => el.remove());
    document.querySelector("#appNotificationMenu")?.remove();
    cleanupDuplicateNotificationButtons(host);
    host.innerHTML = `
      <button class="bh-notification-bell" type="button" aria-label="Thong bao">
        <svg class="app-ui-icon" viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"></path>
          <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
        </svg>
        <span class="bh-notification-badge"></span>
      </button>
      <div class="bh-notification-panel" role="dialog" aria-label="Thông báo">
        <div class="bh-notification-head">
          <div class="bh-notification-title">Thông báo</div>
          <button class="bh-notification-clear" type="button">Đã đọc hết</button>
        </div>
        <div class="bh-notification-list"></div>
      </div>
    `;
    const bell = host.querySelector(".bh-notification-bell");
    const panel = host.querySelector(".bh-notification-panel");
    bell.addEventListener("click", event => {
      event.stopPropagation();
      panel.classList.toggle("open");
      renderList(panel);
    });
    panel.querySelector(".bh-notification-clear").addEventListener("click", event => {
      event.stopPropagation();
      markAllRead();
    });
    panel.addEventListener("click", event => {
      const item = event.target.closest("[data-notification-id]");
      if (item) markOneRead(item.dataset.notificationId);
    });
    document.addEventListener("click", event => {
      if (!host.contains(event.target)) panel.classList.remove("open");
    });
    renderNotificationCenter();
  }

  async function fetchJson(path) {
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    if (!token) return null;
    const response = await fetch(window.location.origin + path, {
      headers: { Authorization: `Bearer ${token}` }
    });
    if (!response.ok) return null;
    return response.json();
  }

  function decodeJwtPayload() {
    var token = localStorage.getItem("token") || sessionStorage.getItem("token");
    if (!token) return {};
    var parts = token.split(".");
    if (parts.length < 2) return {};
    try {
      var base64 = parts[1].replace(/-/g, "+").replace(/_/g, "/");
      while (base64.length % 4 !== 0) base64 += "=";
      var json = decodeURIComponent(
        atob(base64).split("").map(function (c) {
          return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
        }).join("")
      );
      return JSON.parse(json);
    } catch (e) {
      return {};
    }
  }

  function canSeeStockNotifications() {
    return canUseImportFlow();
  }

  async function checkLowStock() {
    try {
      const rows = await fetchJson("/api/bao-cao/ton-kho-it");
      if (!Array.isArray(rows)) return;
      rows.slice(0, 5).forEach(row => {
        const qty = Number(row.soLuongTonKho ?? row.soLuong ?? row.tonKho ?? 0);
        if (qty > LOW_STOCK_THRESHOLD) return;
        const name = row.tenSach || row.sach?.tenSach || "Một đầu sách";
        const id = row.id || row.sachId || name;
        pushNotification({
          type: "stock",
          title: "Sách tồn kho thấp",
          message: `${name} chỉ còn ${qty} cuốn trong kho.`,
          href: `NhapHang.html?quick=create&bookId=${encodeURIComponent(id)}&bookName=${encodeURIComponent(name)}`,
          dedupeKey: `low-stock-${todayKey()}-${id}-${qty}`
        });
      });
    } catch (error) {
      console.warn("Không kiểm tra được tồn kho thấp:", error);
    }
  }

  function checkKpi(revenue, target) {
    const current = Number(revenue || 0);
    const goal = Number(target || 0);
    if (!goal || current < goal) return;
    pushNotification({
      type: "kpi",
      title: "Đã hoàn thành KPI hôm nay",
      message: `Doanh thu đạt ${current.toLocaleString("vi-VN")} đ / ${goal.toLocaleString("vi-VN")} đ.`,
      href: "index.html?view=dashboard",
      dedupeKey: `kpi-reached-${todayKey()}-${goal}`
    });
  }

  window.BookHouseNotify = {
    push: pushNotification,
    render: renderNotificationCenter,
    markAllRead,
    checkLowStock,
    checkKpi
  };

  /* ---- Real-time notifications via WebSocket ---- */
  function loadScript(src) {
    return new Promise(function (resolve, reject) {
      if (document.querySelector('script[src="' + src + '"]')) { resolve(); return; }
      var s = document.createElement("script");
      s.src = src;
      s.onload = resolve;
      s.onerror = reject;
      document.head.appendChild(s);
    });
  }

  function connectNotificationWs() {
    Promise.all([
      loadScript("https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"),
      loadScript("https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js")
    ]).then(function () {
      var sock = new SockJS(window.location.origin + "/ws");
      var client = Stomp.over(sock);
      client.debug = null;
      client.connect({}, function () {
        client.subscribe("/topic/notifications", function (msg) {
          try {
            var data = JSON.parse(msg.body);
            if (data.type === "stock" && !canSeeStockNotifications()) {
              return;
            }
            pushNotification({
              type: data.type || "system",
              title: data.title || "Thông báo",
              message: data.message || "",
              href: data.href || ""
            });
          } catch (e) {
            console.warn("Notification parse error:", e);
          }
        });
      });
    }).catch(function (e) {
      console.warn("Could not load WebSocket libs:", e);
    });
  }

  window.addEventListener("storage", event => {
    if (event.key === STORAGE_KEY) renderNotificationCenter();
  });

  document.addEventListener("DOMContentLoaded", () => {
    initCenter();
    if (canSeeStockNotifications()) {
      setTimeout(checkLowStock, 1200);
      setInterval(checkLowStock, 90000);
    }
    connectNotificationWs();
  });
})();
