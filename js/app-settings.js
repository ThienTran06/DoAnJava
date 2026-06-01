(function () {
  const THEME_KEY = 'darkMode';
  const MUSIC_ENABLED_KEY = 'musicEnabled';
  const MUSIC_VOLUME_KEY = 'musicVolume';
  const MUSIC_TIME_KEY = 'musicCurrentTime';
  const LANGUAGE_KEY = 'language';
  const AUDIO_ID = 'bookhouseSharedAudio';
  const AUDIO_SRC = 'music/Come My Way.mp3';

  const darkCss = `
    html[data-theme="dark"] {
      --bg: #1a1a1a;
      --surface: #242424;
      --border: #333333;
      --text: #f0f0f0;
      --muted: #aaaaaa;
      --subtle: #666666;
      --text-muted: #aaaaaa;
      --text-subtle: #666666;
      --green-light: #1a3320;
      --red-light: #3a1515;
      --shadow-md: 0 4px 16px rgba(0,0,0,0.3);
    }
    html[data-theme="dark"] input,
    html[data-theme="dark"] textarea,
    html[data-theme="dark"] select {
      background: var(--surface);
      color: var(--text);
      border-color: var(--border);
    }
  `;

  const translations = {
    'Tổng quan': 'Dashboard',
    'Sách': 'Books',
    'Danh mục': 'Categories',
    'Khách Hàng': 'Customers',
    'Khách hàng': 'Customers',
    'Nhân viên': 'Staff',
    'Nghiệp vụ': 'Operations',
    'Bán hàng': 'Sales',
    'Hóa đơn': 'Invoices',
    'Nhập hàng': 'Purchasing',
    'Phiếu giữ': 'Reservations',
    'Báo cáo': 'Reports',
    'Doanh thu': 'Revenue',
    'Tồn kho': 'Inventory',
    'Hệ thống': 'System',
    'Cài đặt': 'Settings',
    'Đăng xuất': 'Logout',
    'Quản lý': 'Management',
    'Giao diện': 'Appearance',
    'Chế độ tối': 'Dark mode',
    'Âm nhạc nền': 'Background music',
    'Bật nhạc nền': 'Enable background music',
    'Âm lượng': 'Volume',
    'Ngôn ngữ': 'Language',
    'Tiếng Việt': 'Vietnamese',
    'Tuỳ chỉnh trải nghiệm của bạn': 'Customize your experience',
    'Chọn màu nền và chế độ hiển thị': 'Choose colors and display mode',
    'Chuyển giao diện sang nền đen, giảm mỏi mắt ban đêm': 'Use a dark interface to reduce eye strain at night',
    'Nhạc nền nhẹ khi sử dụng hệ thống': 'Light background music while using the system',
    'Phát nhạc nền khi bạn đang làm việc': 'Play background music while you work',
    'Chọn ngôn ngữ hiển thị của hệ thống': 'Choose the display language',
    'Tải lại trang để áp dụng ngôn ngữ mới': 'Reload the page to apply the new language',
    'Tri thức trong tầm tay': 'Knowledge within reach',
    'Chào mừng bạn trở lại, Admin!': 'Welcome back, Admin!',
    'Quản lý toàn bộ đầu sách trong kho': 'Manage all books in stock',
    'Quản lý thông tin khách hàng': 'Manage customer information',
    'Quản lý nhân sự cửa hàng': 'Manage store staff',
    'Báo cáo doanh thu từ backend': 'Revenue reports from backend',
    'Báo cáo tồn kho từ backend': 'Inventory reports from backend'
  };

  function isDarkMode() {
    return localStorage.getItem(THEME_KEY) === '1';
  }

  function injectDarkCss() {
    if (document.getElementById('bookhouse-dark-css')) return;
    const style = document.createElement('style');
    style.id = 'bookhouse-dark-css';
    style.textContent = darkCss;
    document.head.appendChild(style);
  }

  function applyTheme(isDark = isDarkMode()) {
    injectDarkCss();
    document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
    const toggle = document.getElementById('darkModeToggle');
    if (toggle) toggle.checked = isDark;
    const icon = document.getElementById('topbarThemeIcon');
    if (icon) {
      icon.innerHTML = isDark
        ? '<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>'
        : '<circle cx="12" cy="12" r="5"/><line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/><line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>';
    }
  }

  function setDarkMode(isDark) {
    localStorage.setItem(THEME_KEY, isDark ? '1' : '0');
    applyTheme(isDark);
  }

  function getAudio() {
    let audio = document.getElementById(AUDIO_ID) || document.getElementById('bgAudio');
    if (!audio) {
      audio = document.createElement('audio');
      audio.id = AUDIO_ID;
      audio.loop = true;
      audio.preload = 'auto';
      const source = document.createElement('source');
      source.src = AUDIO_SRC;
      source.type = 'audio/mpeg';
      audio.appendChild(source);
      audio.style.display = 'none';
      document.body.appendChild(audio);
    }
    return audio;
  }

  function saveMusicTime() {
    const audio = document.getElementById(AUDIO_ID) || document.getElementById('bgAudio');
    if (audio && Number.isFinite(audio.currentTime)) {
      localStorage.setItem(MUSIC_TIME_KEY, String(audio.currentTime));
    }
  }

  function syncMusicControls() {
    const enabled = localStorage.getItem(MUSIC_ENABLED_KEY) === '1';
    const volume = parseInt(localStorage.getItem(MUSIC_VOLUME_KEY) || '50', 10);
    const toggle = document.getElementById('musicToggle');
    const slider = document.getElementById('volumeSlider');
    const label = document.getElementById('volumeVal');
    if (toggle) toggle.checked = enabled;
    if (slider) slider.value = String(volume);
    if (label) label.textContent = volume + '%';
  }

  function restoreMusic() {
    syncMusicControls();
    const enabled = localStorage.getItem(MUSIC_ENABLED_KEY) === '1';
    const volume = parseInt(localStorage.getItem(MUSIC_VOLUME_KEY) || '50', 10);
    const audio = getAudio();
    audio.volume = Math.max(0, Math.min(100, volume)) / 100;
    const savedTime = parseFloat(localStorage.getItem(MUSIC_TIME_KEY) || '0');
    if (Number.isFinite(savedTime) && savedTime > 0) {
      const applyTime = () => {
        if (audio.duration && savedTime < audio.duration) audio.currentTime = savedTime;
      };
      if (audio.readyState >= 1) applyTime();
      else audio.addEventListener('loadedmetadata', applyTime, { once: true });
    }
    audio.addEventListener('timeupdate', saveMusicTime);
    if (enabled) audio.play().catch(() => {});
  }

  function setMusicEnabled(enabled) {
    const audio = getAudio();
    localStorage.setItem(MUSIC_ENABLED_KEY, enabled ? '1' : '0');
    syncMusicControls();
    if (enabled) audio.play().catch(() => {});
    else {
      saveMusicTime();
      audio.pause();
    }
  }

  function setMusicVolume(value) {
    const volume = Math.max(0, Math.min(100, Number(value) || 0));
    localStorage.setItem(MUSIC_VOLUME_KEY, String(volume));
    const audio = getAudio();
    audio.volume = volume / 100;
    syncMusicControls();
  }

  function currentLanguage() {
    return localStorage.getItem(LANGUAGE_KEY) || 'vi';
  }

  function translateText(text) {
    const trimmed = text.replace(/\s+/g, ' ').trim();
    return translations[trimmed] || null;
  }

  function applyLanguage(lang = currentLanguage()) {
    document.documentElement.lang = lang === 'en' ? 'en' : 'vi';
    const vi = document.getElementById('langVi');
    const en = document.getElementById('langEn');
    if (vi) vi.classList.toggle('active', lang === 'vi');
    if (en) en.classList.toggle('active', lang === 'en');
    if (lang !== 'en') return;

    const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, {
      acceptNode(node) {
        if (!node.nodeValue || !node.nodeValue.trim()) return NodeFilter.FILTER_REJECT;
        const parent = node.parentElement;
        if (!parent || ['SCRIPT', 'STYLE', 'TEXTAREA'].includes(parent.tagName)) return NodeFilter.FILTER_REJECT;
        return translateText(node.nodeValue) ? NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_REJECT;
      }
    });

    const nodes = [];
    while (walker.nextNode()) nodes.push(walker.currentNode);
    nodes.forEach(node => {
      const translated = translateText(node.nodeValue);
      if (translated) node.nodeValue = node.nodeValue.replace(node.nodeValue.trim(), translated);
    });
  }

  function setLanguage(lang) {
    localStorage.setItem(LANGUAGE_KEY, lang);
    applyLanguage(lang);
    const notice = document.getElementById('langNotice');
    if (notice) notice.classList.add('show');
  }

  function init() {
    applyTheme();
    restoreMusic();
    applyLanguage();
    window.addEventListener('pagehide', saveMusicTime);
    window.addEventListener('beforeunload', saveMusicTime);
    const observer = new MutationObserver(() => {
      if (currentLanguage() === 'en') applyLanguage('en');
    });
    observer.observe(document.body, { childList: true, subtree: true });
  }

  injectDarkCss();
  applyTheme();

  window.BookHouseSettings = {
    applyTheme,
    setDarkMode,
    isDarkMode,
    restoreMusic,
    setMusicEnabled,
    setMusicVolume,
    saveMusicTime,
    applyLanguage,
    setLanguage
  };

  window.toggleDarkMode = function () {
    const checkbox = document.getElementById('darkModeToggle');
    const next = checkbox ? checkbox.checked : !isDarkMode();
    setDarkMode(next);
  };
  window.toggleMusic = function () {
    const checkbox = document.getElementById('musicToggle');
    setMusicEnabled(checkbox ? checkbox.checked : localStorage.getItem(MUSIC_ENABLED_KEY) !== '1');
  };
  window.changeVolume = function (value) {
    setMusicVolume(value);
  };
  window.setLanguage = setLanguage;

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
