/* ============================================================
   BookHouse – User Profile Module
   Self-contained IIFE: JWT decode, API fetch with cache,
   sidebar population, and profile popup modal.
   ============================================================ */
(function () {
  'use strict';

  var CACHE_KEY = 'bh_user_profile';
  var CACHE_TTL = 5 * 60 * 1000; // 5 minutes

  /* ── JWT Decode ── */
  function decodeJwt() {
    var token = localStorage.getItem('token');
    if (!token) return null;
    var parts = token.split('.');
    if (parts.length < 2) return null;
    try {
      var base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      var padded = base64 + '===='.slice(base64.length % 4 || 4);
      var bytes = Uint8Array.from(atob(padded), function (c) { return c.charCodeAt(0); });
      return JSON.parse(new TextDecoder('utf-8').decode(bytes));
    } catch (e) { return null; }
  }

  /* ── Cached API Fetch ── */
  function getCachedProfile() {
    try {
      var raw = JSON.parse(localStorage.getItem(CACHE_KEY));
      var ownerId = getCurrentProfileOwnerId();
      var sameUser = ownerId && String(raw && raw.userId) === String(ownerId);
      if (raw && raw.ts && sameUser && (Date.now() - raw.ts < CACHE_TTL)) return raw.data;
    } catch (e) { /* ignore */ }
    return null;
  }

  function setCachedProfile(data) {
    try {
      localStorage.setItem(CACHE_KEY, JSON.stringify({
        data: data,
        ts: Date.now(),
        userId: data && (data.id || data.userId || data.maNguoiDung || getCurrentProfileOwnerId())
      }));
    } catch (e) { /* ignore */ }
  }

  function clearCachedProfile() {
    try {
      localStorage.removeItem(CACHE_KEY);
    } catch (e) { /* ignore */ }
  }

  function getCurrentProfileOwnerId() {
    var jwt = decodeJwt() || {};
    var stored = readStoredUser ? readStoredUser() || {} : {};
    return jwt.id || jwt.userId || stored.id || stored.userId || stored.maNguoiDung || '';
  }

  function fetchProfile(userId) {
    var token = localStorage.getItem('token');
    var base = window.API_BASE_URL || window.location.origin;
    return fetch(base + '/api/nguoi-dung/' + userId, {
      headers: { Authorization: 'Bearer ' + token, 'Content-Type': 'application/json' }
    }).then(function (res) {
      if (!res.ok) throw new Error(res.status);
      return res.json();
    });
  }

  /* ── Sidebar Population ── */
  function findEl(ids, fallbackSelector) {
    for (var i = 0; i < ids.length; i++) {
      var el = document.getElementById(ids[i]);
      if (el) return el;
    }
    return fallbackSelector ? document.querySelector(fallbackSelector) : null;
  }

  function setAvatar(el, profile) {
    if (!el) return;
    if (profile.avatar) {
      var img = document.createElement('img');
      img.src = profile.avatar;
      img.alt = 'Avatar';
      img.style.cssText = 'width:100%;height:100%;object-fit:cover;border-radius:50%';
      img.onerror = function () {
        el.innerHTML = '';
        el.textContent = getInitial(profile);
      };
      el.innerHTML = '';
      el.appendChild(img);
    } else {
      el.innerHTML = '';
      el.textContent = getInitial(profile);
    }
  }

  function getInitial(profile) {
    return String(profile.hoTen || profile.username || 'A').charAt(0).toUpperCase();
  }

  function escAttr(s) {
    return String(s).replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  }

  function getRoleLabel(profile) {
    var r = profile.role || (profile.nhom && profile.nhom.tenNhom) || '';
    if (typeof r === 'string') return r;
    if (Array.isArray(r)) return r[0] || '';
    return '';
  }

  function populateSidebar(profile) {
    var avatarEl = findEl(['userAvatar', 'avatar-box'], '.sidebar-footer .avatar');
    var nameEl = findEl(['userName', 'user-display-name'], '.sidebar-footer .user-name');
    var roleEl = findEl([], '.sidebar-footer .user-role');

    setAvatar(avatarEl, profile);
    if (nameEl) nameEl.textContent = profile.hoTen || profile.username || 'Admin';
    if (roleEl) {
      var label = getRoleLabel(profile);
      if (label) roleEl.textContent = label;
    }
  }

  /* ── Avatar Upload (Cloudinary + API) ── */
  var CLOUD_NAME = 'ddjcg7c8y';
  var UPLOAD_PRESET = 'ml_default';

  function uploadAvatarToCloudinary(file) {
    var fd = new FormData();
    fd.append('file', file);
    fd.append('upload_preset', UPLOAD_PRESET);
    fd.append('cloud_name', CLOUD_NAME);
    return fetch('https://api.cloudinary.com/v1_1/' + CLOUD_NAME + '/image/upload', {
      method: 'POST',
      body: fd
    }).then(function (res) {
      if (!res.ok) throw new Error('Upload thất bại');
      return res.json();
    }).then(function (data) {
      if (!data.secure_url) throw new Error('Không nhận được URL ảnh');
      return data.secure_url;
    });
  }

  function saveAvatarToApi(profile, avatarUrl) {
    var token = localStorage.getItem('token');
    var base = window.API_BASE_URL || window.location.origin;
    var payload = {
      tenDangNhap: profile.username || profile.tenDangNhap,
      hoTen: profile.hoTen || profile.username || 'Admin',
      sdt: profile.sdt || profile.SDT || '0000000000',
      tenNhom: (profile.nhom && profile.nhom.tenNhom) || profile.role || 'Nhân viên',
      permissionIds: [],
      email: profile.email || '',
      avatar: avatarUrl,
      caLamViec: profile.caLamViec || '',
      diaChi: profile.diaChi || '',
      luongCoBan: profile.luongCoBan || 0,
      ghiChu: profile.ghiChu || ''
    };
    return fetch(base + '/api/nguoi-dung/' + profile.id, {
      method: 'PUT',
      headers: { Authorization: 'Bearer ' + token, 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    }).then(function (res) {
      if (!res.ok) throw new Error('Lưu avatar thất bại (' + res.status + ')');
      return res.json();
    });
  }

  /* ── Profile Popup ── */
  function injectPopupCSS() {
    if (document.getElementById('bh-profile-css')) return;
    var style = document.createElement('style');
    style.id = 'bh-profile-css';
    style.textContent =
      '.bh-profile-overlay{position:fixed;inset:0;z-index:9990;background:rgba(0,0,0,.35);backdrop-filter:blur(4px);display:flex;align-items:center;justify-content:center;opacity:0;transition:opacity .2s ease}' +
      '.bh-profile-overlay.open{opacity:1}' +
      '.bh-profile-popup{background:var(--surface,#fff);border-radius:20px;padding:32px 28px 24px;width:380px;max-width:calc(100vw - 32px);box-shadow:0 16px 48px rgba(0,0,0,.18);position:relative;transform:scale(.95);transition:transform .2s ease}' +
      '.bh-profile-overlay.open .bh-profile-popup{transform:scale(1)}' +
      '.bh-profile-close{position:absolute;top:14px;right:14px;width:28px;height:28px;border:none;background:var(--bg,#f5f5f0);border-radius:8px;cursor:pointer;display:flex;align-items:center;justify-content:center;color:var(--text,#1f1f1f);transition:background .15s}' +
      '.bh-profile-close:hover{background:var(--border,#ddd)}' +
      '.bh-profile-avatar-wrap{position:relative;width:72px;height:72px;margin:0 auto 16px;cursor:pointer}' +
      '.bh-profile-avatar{width:72px;height:72px;border-radius:50%;background:var(--text,#1f1f1f);color:var(--bg,#fff);display:flex;align-items:center;justify-content:center;font-size:28px;font-weight:800;overflow:hidden;border:3px solid var(--border,#e0e0e0)}' +
      '.bh-profile-avatar img{width:100%;height:100%;object-fit:cover}' +
      '.bh-profile-avatar-edit{position:absolute;bottom:0;right:0;width:24px;height:24px;border-radius:50%;background:var(--text,#1f1f1f);color:var(--bg,#fff);display:flex;align-items:center;justify-content:center;border:2px solid var(--surface,#fff);cursor:pointer;transition:transform .15s}' +
      '.bh-profile-avatar-edit:hover{transform:scale(1.1)}' +
      '.bh-profile-avatar-wrap.uploading .bh-profile-avatar{opacity:.5}' +
      '.bh-profile-avatar-wrap.uploading .bh-profile-avatar-edit{display:none}' +
      '.bh-profile-upload-spinner{position:absolute;inset:0;display:none;align-items:center;justify-content:center;font-size:11px;font-weight:700;color:var(--text,#1f1f1f)}' +
      '.bh-profile-avatar-wrap.uploading .bh-profile-upload-spinner{display:flex}' +
      '.bh-profile-name{text-align:center;font-size:18px;font-weight:800;margin-bottom:2px}' +
      '.bh-profile-role{text-align:center;font-size:12px;color:var(--text-muted,#888);font-weight:500;margin-bottom:20px}' +
      '.bh-profile-info{display:flex;flex-direction:column;gap:10px;margin-bottom:20px}' +
      '.bh-profile-row{display:flex;align-items:center;gap:10px;font-size:13px}' +
      '.bh-profile-row-label{color:var(--text-muted,#888);font-weight:500;min-width:90px;flex-shrink:0}' +
      '.bh-profile-row-value{font-weight:600;word-break:break-word}' +
      '.bh-profile-actions{display:flex;gap:8px;justify-content:center}' +
      '.bh-profile-btn{padding:8px 20px;border-radius:10px;font-size:13px;font-weight:700;cursor:pointer;border:1.5px solid var(--border,#ddd);background:var(--surface,#fff);color:var(--text,#1f1f1f);font-family:inherit;transition:background .15s,border-color .15s}' +
      '.bh-profile-btn:hover{background:var(--bg,#f5f5f0);border-color:var(--text,#1f1f1f)}' +
      '.bh-profile-btn.primary{background:var(--text,#1f1f1f);color:var(--bg,#fff);border-color:var(--text,#1f1f1f)}' +
      '.bh-profile-btn.primary:hover{opacity:.88}';
    document.head.appendChild(style);
  }

  function showProfilePopup(profile) {
    injectPopupCSS();

    /* Remove existing overlay if any */
    var old = document.getElementById('bhProfileOverlay');
    if (old) old.remove();

    var overlay = document.createElement('div');
    overlay.className = 'bh-profile-overlay';
    overlay.id = 'bhProfileOverlay';

    var rows = [];
    function valueOrEmpty(value) {
      if (value === 0) return '0';
      return value == null ? '' : String(value).trim();
    }
    function money(value) {
      if (value == null || value === '') return '';
      var number = Number(value);
      if (Number.isNaN(number)) return value;
      return number.toLocaleString('vi-VN') + ' đ';
    }
    function dateText(value) {
      if (!value) return '';
      try {
        var d = new Date(value);
        if (!Number.isNaN(d.getTime())) return d.toLocaleDateString('vi-VN');
      } catch (e) { /* keep original */ }
      return value;
    }
    function addRow(label, value, fallback) {
      var display = valueOrEmpty(value) || fallback || 'Chưa cập nhật';
      rows.push('<div class="bh-profile-row"><span class="bh-profile-row-label">' + escAttr(label) + '</span><span class="bh-profile-row-value">' + escAttr(display) + '</span></div>');
    }
    var groupName = profile.tenNhom || (profile.nhom && profile.nhom.tenNhom) || getRoleLabel(profile);
    var status = profile.trangThai || profile.status || (profile.enabled === false ? 'Đã khóa' : 'Đang hoạt động');
    addRow('Mã tài khoản', profile.id || profile.userId || profile.maNguoiDung);
    addRow('Họ tên', profile.hoTen || profile.fullName || profile.name || profile.username);
    addRow('Tài khoản', profile.username || profile.tenDangNhap);
    addRow('Vai trò', groupName);
    addRow('Email', profile.email);
    addRow('SĐT', profile.sdt || profile.SDT || profile.soDienThoai);
    addRow('Địa chỉ', profile.diaChi || profile.address);
    addRow('Ca làm việc', profile.caLamViec);
    addRow('Ngày vào làm', dateText(profile.ngayVaoLam));
    addRow('Lương cơ bản', money(profile.luongCoBan));
    addRow('Trạng thái', status);
    addRow('Ghi chú', profile.ghiChu || profile.note, 'Không có ghi chú');

    var cameraSvg = '<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/><circle cx="12" cy="13" r="4"/></svg>';

    overlay.innerHTML =
      '<div class="bh-profile-popup">' +
        '<button class="bh-profile-close" title="Đóng"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg></button>' +
        '<div class="bh-profile-avatar-wrap">' +
          '<div class="bh-profile-avatar"></div>' +
          '<div class="bh-profile-avatar-edit" title="Đổi avatar">' + cameraSvg + '</div>' +
          '<div class="bh-profile-upload-spinner">Đang tải...</div>' +
        '</div>' +
        '<input type="file" accept="image/*" style="display:none" class="bh-avatar-file-input">' +
        '<div class="bh-profile-name">' + escAttr(profile.hoTen || profile.username || 'Admin') + '</div>' +
        '<div class="bh-profile-role">' + escAttr(getRoleLabel(profile) || 'Nhân viên') + '</div>' +
        '<div class="bh-profile-info">' + rows.join('') + '</div>' +
        '<div class="bh-profile-actions">' +
          '<button class="bh-profile-btn bh-profile-close-btn">Đóng</button>' +
          '<button class="bh-profile-btn primary bh-profile-settings-btn">Cài đặt</button>' +
        '</div>' +
      '</div>';

    /* Set avatar safely via DOM (no inline handlers) */
    var avatarEl = overlay.querySelector('.bh-profile-avatar');
    function renderPopupAvatar(p) {
      avatarEl.innerHTML = '';
      if (p.avatar) {
        var img = document.createElement('img');
        img.src = p.avatar;
        img.alt = 'Avatar';
        img.onerror = function () {
          avatarEl.innerHTML = '';
          avatarEl.textContent = getInitial(p);
        };
        avatarEl.appendChild(img);
      } else {
        avatarEl.textContent = getInitial(p);
      }
    }
    renderPopupAvatar(profile);

    /* Avatar upload handler */
    var avatarWrap = overlay.querySelector('.bh-profile-avatar-wrap');
    var fileInput = overlay.querySelector('.bh-avatar-file-input');

    avatarWrap.addEventListener('click', function () {
      if (!avatarWrap.classList.contains('uploading')) fileInput.click();
    });

    fileInput.addEventListener('change', function () {
      var file = fileInput.files && fileInput.files[0];
      if (!file) return;
      avatarWrap.classList.add('uploading');

      uploadAvatarToCloudinary(file).then(function (url) {
        return saveAvatarToApi(profile, url);
      }).then(function (updatedProfile) {
        _profile = updatedProfile;
        setCachedProfile(updatedProfile);
        populateSidebar(updatedProfile);
        profile.avatar = updatedProfile.avatar;
        renderPopupAvatar(updatedProfile);
        avatarWrap.classList.remove('uploading');
        if (window.showToast) window.showToast('Đã cập nhật avatar', 'success');
      }).catch(function (err) {
        avatarWrap.classList.remove('uploading');
        if (window.showToast) window.showToast('Không thể cập nhật avatar: ' + err.message, 'error');
      });
    });

    document.body.appendChild(overlay);

    /* Trigger animation */
    requestAnimationFrame(function () { overlay.classList.add('open'); });

    /* Close handlers — single cleanup function */
    function close() {
      document.removeEventListener('keydown', escHandler);
      overlay.classList.remove('open');
      setTimeout(function () { overlay.remove(); }, 220);
    }

    function escHandler(e) {
      if (e.key === 'Escape') close();
    }

    overlay.querySelector('.bh-profile-close').addEventListener('click', close);
    overlay.querySelector('.bh-profile-close-btn').addEventListener('click', close);
    overlay.querySelector('.bh-profile-settings-btn').addEventListener('click', function () {
      window.location.href = 'settings.html';
    });
    overlay.addEventListener('click', function (e) {
      if (e.target === overlay) close();
    });
    document.addEventListener('keydown', escHandler);
  }

  /* ── Init ── */
  var _profile = null;
  var _initDone = false;

  function readStoredUser() {
    try {
      return JSON.parse(localStorage.getItem('user') || localStorage.getItem('currentUser') || localStorage.getItem('account') || 'null');
    } catch (e) {
      return null;
    }
  }

  function normalizePermission(value) {
    return String(value || '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/[\s-]+/g, '_')
      .toUpperCase();
  }

  function getJwtPermissions() {
    var jwt = decodeJwt() || {};
    return Array.isArray(jwt.permissions) ? jwt.permissions : [];
  }

  function getJwtRole() {
    var jwt = decodeJwt() || {};
    return normalizePermission(jwt.role);
  }

  function hasPermission(permission) {
    var expected = normalizePermission(permission);
    return getJwtPermissions().some(function (item) {
      return normalizePermission(item) === expected;
    });
  }

  function isAdmin() {
    return getJwtRole() === 'ADMIN' || hasPermission('ADMIN');
  }

  function canAccessRule(rule) {
    if (!rule) return true;
    if (isAdmin()) return true;
    if (rule.pendingInvoicePayment && hasPermission('QUAN_LY_HOA_DON') && localStorage.getItem('RESERVATION_INVOICE')) return true;
    if (rule.roles && rule.roles.map(normalizePermission).indexOf(getJwtRole()) >= 0) return true;
    return (rule.permissions || []).some(hasPermission);
  }

  var ACCESS_RULES = {
    dashboard: { permissions: ['XEM_BAO_CAO'] },
    books: { permissions: ['QUAN_LY_SACH'] },
    categories: { permissions: ['QUAN_LY_THE_LOAI', 'QUAN_LY_TAC_GIA', 'QUAN_LY_NHA_XUAT_BAN'] },
    customers: { permissions: ['QUAN_LY_KHACH_HANG'] },
    cskh: { permissions: ['QUAN_LY_KHACH_HANG'] },
    staff: { permissions: ['QUAN_LY_NGUOI_DUNG'] },
    sales: { roles: ['NHAN_VIEN_BAN_HANG'], pendingInvoicePayment: true },
    invoices: { permissions: ['QUAN_LY_HOA_DON'] },
    imports: { permissions: ['QUAN_LY_PHIEU_NHAP'] },
    reservations: { permissions: ['QUAN_LY_PHIEU_GIU', 'QUAN_LY_HOA_DON'] },
    reviews: { permissions: ['QUAN_LY_KHACH_HANG'] },
    revenue: { permissions: ['XEM_BAO_CAO'] },
    inventory: { permissions: ['XEM_BAO_CAO'] }
  };

  var PAGE_TO_KEY = {
    'tongquan.html': 'dashboard',
    'sach.html': 'books',
    'danhmuc.html': 'categories',
    'khachhang.html': 'customers',
    'cskh.html': 'cskh',
    'nhanvien.html': 'staff',
    'banhang.html': 'sales',
    'hoadon.html': 'invoices',
    'nhaphang.html': 'imports',
    'phieugiu.html': 'reservations',
    'danh-gia-cong-cong.html': 'reviews',
    'settings.html': 'settings'
  };

  var VIEW_TO_KEY = {
    dashboard: 'dashboard',
    books: 'books',
    categories: 'categories',
    customers: 'customers',
    cskh: 'cskh',
    staff: 'staff',
    revenue: 'revenue',
    inventory: 'inventory'
  };

  var DEFAULT_ROUTES = [
    ['dashboard', 'TongQuan.html'],
    ['customers', 'KhachHang.html'],
    ['invoices', 'HoaDon.html'],
    ['reservations', 'PhieuGiu.html'],
    ['books', 'Sach.html'],
    ['imports', 'NhapHang.html'],
    ['categories', 'DanhMuc.html'],
    ['staff', 'NhanVien.html'],
    ['revenue', 'index.html?view=revenue'],
    ['inventory', 'index.html?view=inventory'],
    ['settings', 'settings.html']
  ];

  function getPageFileName(url) {
    var path = (url || window.location.pathname).split('/').pop() || 'index.html';
    return path.toLowerCase();
  }

  function accessKeyFromUrl(url) {
    var parsed;
    try {
      parsed = new URL(url, window.location.href);
    } catch (e) {
      parsed = new URL(window.location.href);
    }
    var file = getPageFileName(parsed.pathname);
    if (file === 'index.html') {
      return VIEW_TO_KEY[parsed.searchParams.get('view') || 'dashboard'] || 'dashboard';
    }
    return PAGE_TO_KEY[file] || null;
  }

  function firstAllowedRoute() {
    for (var i = 0; i < DEFAULT_ROUTES.length; i++) {
      if (canAccessRule(ACCESS_RULES[DEFAULT_ROUTES[i][0]])) return DEFAULT_ROUTES[i][1];
    }
    return 'login.html';
  }

  function routeKeyFromNavItem(item) {
    var href = item.getAttribute('href');
    if (href) return accessKeyFromUrl(href);

    var onclick = item.getAttribute('onclick') || '';
    var match = onclick.match(/['"]([^'"]+\.html(?:\?[^'"]*)?)['"]/i);
    if (match) return accessKeyFromUrl(match[1]);

    var label = normalizePermission(item.textContent);
    if (label.indexOf('TONG_QUAN') >= 0) return 'dashboard';
    if (label.indexOf('SACH') >= 0 && label.indexOf('PHIEU') < 0) return 'books';
    if (label.indexOf('DANH_MUC') >= 0) return 'categories';
    if (label.indexOf('KHACH_HANG') >= 0) return 'customers';
    if (label.indexOf('CHAM_SOC') >= 0 || label.indexOf('CSKH') >= 0) return 'cskh';
    if (label.indexOf('NHAN_VIEN') >= 0) return 'staff';
    if (label.indexOf('BAN_HANG') >= 0) return 'sales';
    if (label.indexOf('HOA_DON') >= 0) return 'invoices';
    if (label.indexOf('NHAP_HANG') >= 0) return 'imports';
    if (label.indexOf('PHIEU_GIU') >= 0) return 'reservations';
    if (label.indexOf('DANH_GIA') >= 0) return 'reviews';
    if (label.indexOf('DOANH_THU') >= 0) return 'revenue';
    if (label.indexOf('TON_KHO') >= 0) return 'inventory';
    if (label.indexOf('CAI_DAT') >= 0) return 'settings';
    return null;
  }

  function applyNavigationAccess() {
    var items = document.querySelectorAll('.sidebar .nav-item');
    items.forEach(function (item) {
      var key = routeKeyFromNavItem(item);
      if (!key) return;
      var allowed = canAccessRule(ACCESS_RULES[key]);
      item.hidden = !allowed;
      item.style.display = allowed ? '' : 'none';
      if (!allowed) {
        item.classList.remove('active');
        item.addEventListener('click', function (event) {
          event.preventDefault();
          event.stopImmediatePropagation();
          if (window.showToast) window.showToast('Tai khoan nay khong co quyen truy cap chuc nang nay');
        }, true);
      }
    });

    document.querySelectorAll('.sidebar .nav-section').forEach(function (section) {
      var visible = Array.from(section.querySelectorAll('.nav-item')).some(function (item) {
        return !item.hidden && item.style.display !== 'none';
      });
      section.style.display = visible ? '' : 'none';
    });
  }

  function enforceCurrentPageAccess() {
    var token = localStorage.getItem('token');
    if (!token || getPageFileName() === 'login.html') return;
    var key = accessKeyFromUrl(window.location.href);
    if (!key) return;
    if (canAccessRule(ACCESS_RULES[key])) return;
    window.location.replace(firstAllowedRoute());
  }

  function fallbackProfile() {
    var jwt = decodeJwt() || {};
    var stored = readStoredUser() || {};
    return {
      id: stored.id || stored.userId || jwt.id || jwt.userId || '',
      username: stored.username || stored.tenDangNhap || jwt.username || jwt.sub || '',
      hoTen: stored.hoTen || stored.fullName || stored.name || jwt.hoTen || jwt.name || jwt.sub || 'Tài khoản BookHouse',
      email: stored.email || jwt.email || '',
      sdt: stored.sdt || stored.SDT || stored.soDienThoai || jwt.sdt || '',
      diaChi: stored.diaChi || stored.address || '',
      caLamViec: stored.caLamViec || '',
      ngayVaoLam: stored.ngayVaoLam || '',
      luongCoBan: stored.luongCoBan || '',
      ghiChu: stored.ghiChu || '',
      avatar: stored.avatar || stored.avatarUrl || jwt.avatar || '',
      role: stored.role || stored.chucVu || jwt.role || 'Người dùng',
      nhom: stored.nhom
    };
  }

  function init() {
    if (_initDone) return;
    _initDone = true;

    var jwt = decodeJwt();
    applyNavigationAccess();
    enforceCurrentPageAccess();

    /* 1. Try cache first for instant sidebar */
    var cached = getCachedProfile();
    if (cached) {
      _profile = cached;
      populateSidebar(cached);
    } else {
      /* Populate from JWT immediately as fallback */
      var fallback = fallbackProfile();
      _profile = fallback;
      populateSidebar(fallback);
    }

    /* 2. Fetch fresh data from API in background */
    if (jwt && jwt.id) {
      fetchProfile(jwt.id).then(function (data) {
        _profile = data;
        setCachedProfile(data);
        populateSidebar(data);
      }).catch(function () {
        /* API failed (403, network) — keep JWT/cache data, no error shown */
      });
    }

    /* 3. Attach click handler to user card */
    var userCard = document.querySelector('.sidebar-footer .user-card');
    if (userCard) {
      userCard.style.cursor = 'pointer';
      userCard.addEventListener('click', function () {
        if (_profile) showProfilePopup(_profile);
      });
    }
  }

  /* Expose for external use */
  window.BookHouseProfile = {
    getProfile: function () { return _profile || fallbackProfile(); },
    showPopup: function () { showProfilePopup(_profile || fallbackProfile()); },
    clearCache: clearCachedProfile,
    refresh: function () {
      var jwt = decodeJwt();
      if (!jwt || !jwt.id) return;
      fetchProfile(jwt.id).then(function (data) {
        _profile = data;
        setCachedProfile(data);
        populateSidebar(data);
      });
    }
  };

  window.BookHouseAccess = {
    hasPermission: hasPermission,
    canAccess: function (key) { return canAccessRule(ACCESS_RULES[key]); },
    keyFromUrl: accessKeyFromUrl,
    firstAllowedRoute: firstAllowedRoute,
    applyNavigation: applyNavigationAccess,
    enforceCurrentPage: enforceCurrentPageAccess
  };

  /* Run on DOM ready */
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
