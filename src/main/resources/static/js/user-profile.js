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

  /* ── Avatar Upload (via Backend) ── */

  function uploadAvatarToCloudinary(file) {
    var token = localStorage.getItem('token');
    var base = window.API_BASE_URL || window.location.origin;
    var fd = new FormData();
    fd.append('file', file);
    return fetch(base + '/api/nguoi-dung/upload-avatar', {
      method: 'POST',
      headers: { Authorization: 'Bearer ' + token },
      body: fd
    }).then(function (res) {
      if (!res.ok) throw new Error('Upload thất bại');
      return res.json();
    }).then(function (data) {
      if (!data.url) throw new Error('Không nhận được URL ảnh');
      return data.url;
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
      '.bh-profile-popup{background:var(--surface,#fff);border-radius:20px;padding:32px 32px 24px;width:480px;max-width:calc(100vw - 32px);max-height:calc(100vh - 48px);overflow-y:auto;box-shadow:0 16px 48px rgba(0,0,0,.18);position:relative;transform:scale(.95);transition:transform .2s ease}' +
      '.bh-profile-overlay.open .bh-profile-popup{transform:scale(1)}' +
      '.bh-profile-close{position:absolute;top:14px;right:14px;width:28px;height:28px;border:none;background:var(--bg,#f5f5f0);border-radius:8px;cursor:pointer;display:flex;align-items:center;justify-content:center;color:var(--text,#1f1f1f);transition:background .15s;z-index:1}' +
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
      '.bh-profile-row-label{color:var(--text-muted,#888);font-weight:500;min-width:100px;flex-shrink:0}' +
      '.bh-profile-row-value{font-weight:600;word-break:break-word}' +
      '.bh-profile-actions{display:flex;gap:8px;justify-content:center;flex-wrap:wrap}' +
      '.bh-profile-btn{padding:8px 20px;border-radius:10px;font-size:13px;font-weight:700;cursor:pointer;border:1.5px solid var(--border,#ddd);background:var(--surface,#fff);color:var(--text,#1f1f1f);font-family:inherit;transition:background .15s,border-color .15s}' +
      '.bh-profile-btn:hover{background:var(--bg,#f5f5f0);border-color:var(--text,#1f1f1f)}' +
      '.bh-profile-btn.primary{background:var(--text,#1f1f1f);color:var(--bg,#fff);border-color:var(--text,#1f1f1f)}' +
      '.bh-profile-btn.primary:hover{opacity:.88}' +
      '.bh-profile-btn:disabled{opacity:.5;cursor:not-allowed}' +
      /* ── Edit form styles ── */
      '.bh-edit-section{margin-bottom:16px;padding:14px 16px;border-radius:12px;background:var(--bg,#f5f5f0);border:1px solid var(--border,#e0e0e0)}' +
      '.bh-edit-title{font-size:13px;font-weight:700;margin-bottom:12px;color:var(--text,#1f1f1f)}' +
      '.bh-edit-field{display:flex;flex-direction:column;gap:4px;margin-bottom:10px}' +
      '.bh-edit-field:last-of-type{margin-bottom:0}' +
      '.bh-edit-field label{font-size:11px;font-weight:600;color:var(--text-muted,#888)}' +
      '.bh-edit-field input,.bh-edit-field textarea{padding:7px 10px;border:1.5px solid var(--border,#ddd);border-radius:8px;font-size:13px;font-family:inherit;background:var(--surface,#fff);color:var(--text,#1f1f1f);outline:none;transition:border-color .15s}' +
      '.bh-edit-field input:focus,.bh-edit-field textarea:focus{border-color:var(--text,#1f1f1f)}' +
      '.bh-edit-field textarea{resize:vertical;min-height:48px}' +
      '.bh-edit-field .bh-field-error{color:#dc2626;font-size:11px;font-weight:600;margin-top:2px;display:none}' +
      '.bh-edit-actions{display:flex;gap:8px;margin-top:14px}' +
      '.bh-edit-actions button{padding:7px 18px;border-radius:8px;font-size:12px;font-weight:700;cursor:pointer;border:1.5px solid var(--border,#ddd);font-family:inherit;transition:background .15s,opacity .15s}' +
      '.bh-edit-save{background:var(--text,#1f1f1f);color:var(--bg,#fff);border-color:var(--text,#1f1f1f)!important}' +
      '.bh-edit-save:hover{opacity:.88}' +
      '.bh-edit-save:disabled{opacity:.5;cursor:not-allowed}' +
      '.bh-edit-cancel{background:var(--surface,#fff);color:var(--text,#1f1f1f)}' +
      '.bh-edit-cancel:hover{border-color:var(--text,#1f1f1f)}' +
      '.bh-edit-msg{font-size:11px;margin-top:8px;font-weight:600}' +
      '.bh-edit-msg.success{color:#16a34a}' +
      '.bh-edit-msg.error{color:#dc2626}';
    document.head.appendChild(style);
  }

  /* Build payload from profile for PUT request */
  function buildPayload(profile, overrides) {
    var p = {
      tenDangNhap: profile.username || profile.tenDangNhap,
      hoTen: profile.hoTen || profile.username || 'Admin',
      sdt: profile.sdt || profile.SDT || '0000000000',
      tenNhom: (profile.nhom && profile.nhom.tenNhom) || profile.role || 'Nhân viên',
      permissionIds: [],
      email: profile.email || '',
      avatar: profile.avatar || '',
      caLamViec: profile.caLamViec || '',
      diaChi: profile.diaChi || '',
      luongCoBan: profile.luongCoBan || 0,
      ghiChu: profile.ghiChu || ''
    };
    if (overrides) {
      for (var k in overrides) {
        if (overrides.hasOwnProperty(k)) p[k] = overrides[k];
      }
    }
    return p;
  }

  function sendProfileUpdate(profileId, payload) {
    var token = localStorage.getItem('token');
    var base = window.API_BASE_URL || window.location.origin;
    return fetch(base + '/api/nguoi-dung/' + profileId, {
      method: 'PUT',
      headers: { Authorization: 'Bearer ' + token, 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    }).then(function (res) {
      if (!res.ok) return res.text().then(function (t) {
        var msg = 'Cập nhật thất bại (' + res.status + ')';
        try { var j = JSON.parse(t); msg = j.message || j.error || msg; } catch (e) { /* ignore */ }
        throw new Error(msg);
      });
      return res.json();
    });
  }

  function applyProfileUpdate(profile, updated) {
    Object.assign(profile, updated);
    _profile = updated;
    setCachedProfile(updated);
    populateSidebar(updated);
  }

  function showProfilePopup(profile) {
    injectPopupCSS();

    var old = document.getElementById('bhProfileOverlay');
    if (old) old.remove();

    var overlay = document.createElement('div');
    overlay.className = 'bh-profile-overlay';
    overlay.id = 'bhProfileOverlay';

    /* ── Helpers ── */
    function val(v) { return v == null ? '' : String(v).trim(); }
    function money(v) {
      if (v == null || v === '') return '';
      var n = Number(v);
      return Number.isNaN(n) ? v : n.toLocaleString('vi-VN') + ' đ';
    }
    function dateText(v) {
      if (!v) return '';
      try { var d = new Date(v); if (!Number.isNaN(d.getTime())) return d.toLocaleDateString('vi-VN'); } catch (e) { /* ignore */ }
      return v;
    }
    var groupName = profile.tenNhom || (profile.nhom && profile.nhom.tenNhom) || getRoleLabel(profile);
    var status = profile.trangThai || profile.status || (profile.enabled === false ? 'Đã khóa' : 'Đang hoạt động');

    var cameraSvg = '<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/><circle cx="12" cy="13" r="4"/></svg>';
    var editSvg = '<svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>';

    /* ── Render View Mode ── */
    function renderViewMode() {
      var rows = '';
      var fields = [
        ['Mã tài khoản', val(profile.id || profile.userId || profile.maNguoiDung) || 'Chưa cập nhật'],
        ['Họ tên', val(profile.hoTen || profile.fullName || profile.name || profile.username) || 'Chưa cập nhật'],
        ['Tài khoản', val(profile.username || profile.tenDangNhap) || 'Chưa cập nhật'],
        ['Vai trò', val(groupName) || 'Chưa cập nhật'],
        ['Email', val(profile.email) || 'Chưa cập nhật'],
        ['SĐT', val(profile.sdt || profile.SDT || profile.soDienThoai) || 'Chưa cập nhật'],
        ['Địa chỉ', val(profile.diaChi || profile.address) || 'Chưa cập nhật'],
        ['Ca làm việc', val(profile.caLamViec) || 'Chưa cập nhật'],
        ['Ngày vào làm', dateText(profile.ngayVaoLam) || 'Chưa cập nhật'],
        ['Lương cơ bản', money(profile.luongCoBan) || 'Chưa cập nhật'],
        ['Trạng thái', val(status)],
        ['Ghi chú', val(profile.ghiChu || profile.note) || 'Không có ghi chú']
      ];
      for (var i = 0; i < fields.length; i++) {
        rows += '<div class="bh-profile-row"><span class="bh-profile-row-label">' + escAttr(fields[i][0]) + '</span><span class="bh-profile-row-value">' + escAttr(fields[i][1]) + '</span></div>';
      }

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
          '<div class="bh-profile-info">' + rows + '</div>' +
          '<div class="bh-profile-actions">' +
            '<button class="bh-profile-btn bh-profile-close-btn">Đóng</button>' +
            '<button class="bh-profile-btn" id="bhEditBtn">Chỉnh sửa</button>' +
            '<button class="bh-profile-btn" id="bhPwBtn">Đổi mật khẩu</button>' +
            '<button class="bh-profile-btn primary bh-profile-settings-btn">Cài đặt</button>' +
          '</div>' +
        '</div>';

      bindCommon();
    }

    /* ── Render Edit Mode ── */
    function renderEditMode() {
      overlay.innerHTML =
        '<div class="bh-profile-popup">' +
          '<button class="bh-profile-close" title="Đóng"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg></button>' +
          '<div class="bh-profile-avatar-wrap">' +
            '<div class="bh-profile-avatar"></div>' +
            '<div class="bh-profile-avatar-edit" title="Đổi avatar">' + cameraSvg + '</div>' +
            '<div class="bh-profile-upload-spinner">Đang tải...</div>' +
          '</div>' +
          '<input type="file" accept="image/*" style="display:none" class="bh-avatar-file-input">' +
          '<div class="bh-edit-section">' +
            '<div class="bh-edit-title">Chỉnh sửa thông tin</div>' +
            '<div class="bh-edit-field"><label>Họ tên *</label><input type="text" id="bhEdName" value="' + escAttr(profile.hoTen || '') + '" maxlength="50" placeholder="Nhập họ tên"><div class="bh-field-error" id="bhEdNameErr"></div></div>' +
            '<div class="bh-edit-field"><label>Email</label><input type="email" id="bhEdEmail" value="' + escAttr(profile.email || '') + '" placeholder="example@email.com"><div class="bh-field-error" id="bhEdEmailErr"></div></div>' +
            '<div class="bh-edit-field"><label>SĐT *</label><input type="text" id="bhEdPhone" value="' + escAttr(profile.sdt || profile.SDT || '') + '" maxlength="10" placeholder="0xxxxxxxxx"><div class="bh-field-error" id="bhEdPhoneErr"></div></div>' +
            '<div class="bh-edit-field"><label>Địa chỉ</label><input type="text" id="bhEdAddr" value="' + escAttr(profile.diaChi || '') + '" placeholder="Nhập địa chỉ"></div>' +
            '<div class="bh-edit-field"><label>Ca làm việc</label><input type="text" id="bhEdShift" value="' + escAttr(profile.caLamViec || '') + '" placeholder="VD: Ca sáng"></div>' +
            '<div class="bh-edit-field"><label>Ghi chú</label><textarea id="bhEdNote" rows="2" placeholder="Ghi chú thêm">' + escAttr(profile.ghiChu || '') + '</textarea></div>' +
            '<div class="bh-edit-actions">' +
              '<button class="bh-edit-save" id="bhEdSave">Lưu thay đổi</button>' +
              '<button class="bh-edit-cancel" id="bhEdCancel">Hủy</button>' +
            '</div>' +
            '<div class="bh-edit-msg" id="bhEdMsg"></div>' +
          '</div>' +
          '<div class="bh-profile-actions">' +
            '<button class="bh-profile-btn bh-profile-close-btn">Đóng</button>' +
            '<button class="bh-profile-btn primary bh-profile-settings-btn">Cài đặt</button>' +
          '</div>' +
        '</div>';

      bindCommon();

      /* ── Validate & Save ── */
      overlay.querySelector('#bhEdCancel').addEventListener('click', function () {
        renderViewMode();
      });

      overlay.querySelector('#bhEdSave').addEventListener('click', function () {
        var nameVal = overlay.querySelector('#bhEdName').value.trim();
        var emailVal = overlay.querySelector('#bhEdEmail').value.trim();
        var phoneVal = overlay.querySelector('#bhEdPhone').value.trim();
        var addrVal = overlay.querySelector('#bhEdAddr').value.trim();
        var shiftVal = overlay.querySelector('#bhEdShift').value.trim();
        var noteVal = overlay.querySelector('#bhEdNote').value.trim();
        var msgEl = overlay.querySelector('#bhEdMsg');

        /* Reset errors */
        var errEls = overlay.querySelectorAll('.bh-field-error');
        errEls.forEach(function (el) { el.style.display = 'none'; el.textContent = ''; });
        msgEl.textContent = '';

        /* Validate */
        var hasError = false;
        if (!nameVal) {
          showFieldError('bhEdNameErr', 'Họ tên không được để trống');
          hasError = true;
        }
        if (!phoneVal) {
          showFieldError('bhEdPhoneErr', 'SĐT không được để trống');
          hasError = true;
        } else if (!/^[0-9]{10}$/.test(phoneVal)) {
          showFieldError('bhEdPhoneErr', 'SĐT phải gồm đúng 10 chữ số');
          hasError = true;
        }
        if (emailVal && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailVal)) {
          showFieldError('bhEdEmailErr', 'Email không đúng định dạng');
          hasError = true;
        }
        if (hasError) return;

        var saveBtn = overlay.querySelector('#bhEdSave');
        saveBtn.disabled = true;
        saveBtn.textContent = 'Đang lưu...';

        var payload = buildPayload(profile, {
          hoTen: nameVal,
          email: emailVal,
          sdt: phoneVal,
          diaChi: addrVal,
          caLamViec: shiftVal,
          ghiChu: noteVal
        });

        sendProfileUpdate(profile.id, payload).then(function (updated) {
          applyProfileUpdate(profile, updated);
          if (window.showToast) window.showToast('Đã cập nhật thông tin', 'success');
          renderViewMode();
        }).catch(function (err) {
          saveBtn.disabled = false;
          saveBtn.textContent = 'Lưu thay đổi';
          msgEl.className = 'bh-edit-msg error';
          msgEl.textContent = err.message;
        });
      });

      /* Focus first field */
      var firstInput = overlay.querySelector('#bhEdName');
      if (firstInput) firstInput.focus();
    }

    /* ── Render Password Mode ── */
    function renderPasswordMode() {
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
          '<div class="bh-edit-section">' +
            '<div class="bh-edit-title">Đổi mật khẩu</div>' +
            '<div class="bh-edit-field"><label>Mật khẩu mới *</label><input type="password" id="bhPwNew" placeholder="Tối thiểu 4 ký tự"><div class="bh-field-error" id="bhPwNewErr"></div></div>' +
            '<div class="bh-edit-field"><label>Xác nhận mật khẩu *</label><input type="password" id="bhPwConfirm" placeholder="Nhập lại mật khẩu mới"><div class="bh-field-error" id="bhPwConfirmErr"></div></div>' +
            '<div class="bh-edit-actions">' +
              '<button class="bh-edit-save" id="bhPwSave">Lưu mật khẩu</button>' +
              '<button class="bh-edit-cancel" id="bhPwCancel">Hủy</button>' +
            '</div>' +
            '<div class="bh-edit-msg" id="bhPwMsg"></div>' +
          '</div>' +
          '<div class="bh-profile-actions">' +
            '<button class="bh-profile-btn bh-profile-close-btn">Đóng</button>' +
            '<button class="bh-profile-btn primary bh-profile-settings-btn">Cài đặt</button>' +
          '</div>' +
        '</div>';

      bindCommon();

      overlay.querySelector('#bhPwCancel').addEventListener('click', function () {
        renderViewMode();
      });

      overlay.querySelector('#bhPwSave').addEventListener('click', function () {
        var newPw = overlay.querySelector('#bhPwNew').value;
        var confirmPw = overlay.querySelector('#bhPwConfirm').value;
        var msgEl = overlay.querySelector('#bhPwMsg');

        var errEls = overlay.querySelectorAll('.bh-field-error');
        errEls.forEach(function (el) { el.style.display = 'none'; });
        msgEl.textContent = '';

        var hasError = false;
        if (!newPw || newPw.length < 4) {
          showFieldError('bhPwNewErr', 'Mật khẩu tối thiểu 4 ký tự');
          hasError = true;
        }
        if (newPw !== confirmPw) {
          showFieldError('bhPwConfirmErr', 'Mật khẩu xác nhận không khớp');
          hasError = true;
        }
        if (hasError) return;

        var saveBtn = overlay.querySelector('#bhPwSave');
        saveBtn.disabled = true;
        saveBtn.textContent = 'Đang lưu...';

        var payload = buildPayload(profile, { matKhau: newPw });
        sendProfileUpdate(profile.id, payload).then(function () {
          msgEl.className = 'bh-edit-msg success';
          msgEl.textContent = 'Đổi mật khẩu thành công!';
          if (window.showToast) window.showToast('Đã đổi mật khẩu thành công', 'success');
          setTimeout(function () { renderViewMode(); }, 1500);
        }).catch(function (err) {
          saveBtn.disabled = false;
          saveBtn.textContent = 'Lưu mật khẩu';
          msgEl.className = 'bh-edit-msg error';
          msgEl.textContent = err.message;
        });
      });

      var firstInput = overlay.querySelector('#bhPwNew');
      if (firstInput) firstInput.focus();
    }

    /* ── Show field error ── */
    function showFieldError(id, msg) {
      var el = overlay.querySelector('#' + id);
      if (el) { el.textContent = msg; el.style.display = 'block'; }
    }

    /* ── Common bindings (avatar, close, etc.) — called after each render ── */
    function bindCommon() {
      /* Avatar */
      var avatarEl = overlay.querySelector('.bh-profile-avatar');
      if (avatarEl) {
        avatarEl.innerHTML = '';
        if (profile.avatar) {
          var img = document.createElement('img');
          img.src = profile.avatar;
          img.alt = 'Avatar';
          img.onerror = function () { avatarEl.innerHTML = ''; avatarEl.textContent = getInitial(profile); };
          avatarEl.appendChild(img);
        } else {
          avatarEl.textContent = getInitial(profile);
        }
      }

      var avatarWrap = overlay.querySelector('.bh-profile-avatar-wrap');
      var fileInput = overlay.querySelector('.bh-avatar-file-input');
      if (avatarWrap && fileInput) {
        avatarWrap.addEventListener('click', function () {
          if (!avatarWrap.classList.contains('uploading')) fileInput.click();
        });
        fileInput.addEventListener('change', function () {
          var file = fileInput.files && fileInput.files[0];
          if (!file) return;
          avatarWrap.classList.add('uploading');
          uploadAvatarToCloudinary(file).then(function (url) {
            return saveAvatarToApi(profile, url);
          }).then(function (updated) {
            applyProfileUpdate(profile, updated);
            /* Re-render avatar inline */
            var av = overlay.querySelector('.bh-profile-avatar');
            if (av) {
              av.innerHTML = '';
              var im = document.createElement('img');
              im.src = updated.avatar;
              im.alt = 'Avatar';
              im.onerror = function () { av.innerHTML = ''; av.textContent = getInitial(updated); };
              av.appendChild(im);
            }
            avatarWrap.classList.remove('uploading');
            if (window.showToast) window.showToast('Đã cập nhật avatar', 'success');
          }).catch(function (err) {
            avatarWrap.classList.remove('uploading');
            if (window.showToast) window.showToast('Không thể cập nhật avatar: ' + err.message, 'error');
          });
        });
      }

      /* Close */
      var closeBtns = overlay.querySelectorAll('.bh-profile-close, .bh-profile-close-btn');
      closeBtns.forEach(function (btn) { btn.addEventListener('click', close); });

      /* Settings */
      var settingsBtn = overlay.querySelector('.bh-profile-settings-btn');
      if (settingsBtn) settingsBtn.addEventListener('click', function () { window.location.href = 'settings.html'; });

      /* Edit button */
      var editBtn = overlay.querySelector('#bhEditBtn');
      if (editBtn) editBtn.addEventListener('click', function () { renderEditMode(); });

      /* Password button */
      var pwBtn = overlay.querySelector('#bhPwBtn');
      if (pwBtn) pwBtn.addEventListener('click', function () { renderPasswordMode(); });

      /* Click overlay to close */
      overlay.onclick = function (e) { if (e.target === overlay) close(); };
    }

    /* ── Close ── */
    function close() {
      document.removeEventListener('keydown', escHandler);
      overlay.classList.remove('open');
      setTimeout(function () { overlay.remove(); }, 220);
    }
    function escHandler(e) { if (e.key === 'Escape') close(); }
    document.addEventListener('keydown', escHandler);

    /* ── Init ── */
    document.body.appendChild(overlay);
    renderViewMode();
    requestAnimationFrame(function () { overlay.classList.add('open'); });
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
