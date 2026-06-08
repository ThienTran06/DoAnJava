(function () {
  const THEME_KEY = 'darkMode';
  const MUSIC_ENABLED_KEY = 'musicEnabled';
  const MUSIC_VOLUME_KEY = 'musicVolume';
  const MUSIC_TIME_KEY = 'musicCurrentTime';
  const MUSIC_TRACK_KEY = 'musicTrackIndex';
  const LANGUAGE_KEY = 'language';
  const AUDIO_ID = 'bookhouseSharedAudio';
  const AUDIO_PLAYLIST = [
    { title: 'Come My Way', src: 'music/Come My Way.mp3' },
    { title: 'Ai Ngoài Anh', src: 'music/Ai Ngoài Anh.mp3' },
    { title: 'Haru Haru', src: 'music/Haru Haru.mp3' },
    { title: 'Pickleball', src: 'music/Pickleball.mp3' },
    { title: 'Sóng Gió', src: 'music/Sóng Gió.mp3' },
    { title: 'Tình Em Là Đại Dương', src: 'music/Tình Em Là Đại Dương.mp3' },
    { title: 'Độ Mixi - Nhạc Tết 2026', src: 'music/Độ Mixi (Nà ná na na anh Độ Mixi) - Nhạc Tết 2026.mp3' }
  ];

  const darkCss = `
    html[data-theme="dark"] {
      --bg: #12161d;
      --surface: #1b222c;
      --surface-soft: #232c38;
      --border: #303a47;
      --border-strong: #445164;
      --text: #eef2f7;
      --muted: #a8b3c2;
      --subtle: #788394;
      --text-muted: #aaaaaa;
      --text-subtle: #666666;
      --accent: #8bb8ff;
      --accent-soft: rgba(80, 139, 220, .16);
      --green-light: #1a3320;
      --red-light: #3a1515;
      --shadow-md: 0 4px 16px rgba(0,0,0,0.3);
    }
    html[data-theme="dark"] body,
    html[data-theme="dark"] .main,
    html[data-theme="dark"] .content {
      background: var(--bg);
      color: var(--text);
    }
    html[data-theme="dark"] .sidebar,
    html[data-theme="dark"] .topbar,
    html[data-theme="dark"] .table-card,
    html[data-theme="dark"] .card,
    html[data-theme="dark"] .chart-card,
    html[data-theme="dark"] .settings-card,
    html[data-theme="dark"] .modal,
    html[data-theme="dark"] .drawer,
    html[data-theme="dark"] .drawer-panel,
    html[data-theme="dark"] .summary-card,
    html[data-theme="dark"] .empty-state,
    html[data-theme="dark"] .suggestion-dropdown,
    html[data-theme="dark"] .modal-box,
    html[data-theme="dark"] .drawer-box,
    html[data-theme="dark"] .cart-container,
    html[data-theme="dark"] .cart-items-list,
    html[data-theme="dark"] .section-card,
    html[data-theme="dark"] .table-wrap,
    html[data-theme="dark"] .detail-summary-grid,
    html[data-theme="dark"] .mini-card,
    html[data-theme="dark"] .bar-list,
    html[data-theme="dark"] .donut-canvas-wrap,
    html[data-theme="dark"] .list-modal,
    html[data-theme="dark"] .stat-card {
      background: var(--surface) !important;
      border-color: var(--border) !important;
      color: var(--text) !important;
    }
    html[data-theme="dark"] .topbar *,
    html[data-theme="dark"] .table-card *,
    html[data-theme="dark"] .card *,
    html[data-theme="dark"] .chart-card *,
    html[data-theme="dark"] .settings-card *,
    html[data-theme="dark"] .modal *,
    html[data-theme="dark"] .drawer *,
    html[data-theme="dark"] .summary-card *,
    html[data-theme="dark"] .empty-state *,
    html[data-theme="dark"] .drawer-box *,
    html[data-theme="dark"] .modal-box *,
    html[data-theme="dark"] .cart-container *,
    html[data-theme="dark"] .section-card *,
    html[data-theme="dark"] .list-modal * {
      border-color: var(--border);
    }
    html[data-theme="dark"] .logo-mark,
    html[data-theme="dark"] .avatar {
      background: #263242;
      color: var(--text);
    }
    html[data-theme="dark"] .nav-item.active {
      background: #2d3a4c !important;
      color: var(--text) !important;
    }
    html[data-theme="dark"] .nav-item.active svg {
      stroke: currentColor !important;
    }
    html[data-theme="dark"] .nav-item.active .nav-badge {
      background: rgba(255,255,255,0.12);
      color: var(--text);
    }
    html[data-theme="dark"] .stat-card.dark {
      background: #2f2f2f !important;
      border-color: #444 !important;
      color: var(--text) !important;
    }
    html[data-theme="dark"] .stat-card.dark .stat-label,
    html[data-theme="dark"] .stat-card.dark .stat-value,
    html[data-theme="dark"] .stat-card.dark .stat-delta,
    html[data-theme="dark"] .stat-card.dark * {
      color: inherit;
    }
    html[data-theme="dark"] .stat-label,
    html[data-theme="dark"] .card-title,
    html[data-theme="dark"] .section-title,
    html[data-theme="dark"] .muted,
    html[data-theme="dark"] .text-muted,
    html[data-theme="dark"] .summary-label,
    html[data-theme="dark"] .pagination-info,
    html[data-theme="dark"] .state-text,
    html[data-theme="dark"] .donut-center .lbl,
    html[data-theme="dark"] .legend-item,
    html[data-theme="dark"] .bar-name,
    html[data-theme="dark"] label,
    html[data-theme="dark"] .label,
    html[data-theme="dark"] .input-grid-mini label,
    html[data-theme="dark"] .hint,
    html[data-theme="dark"] small {
      color: var(--muted) !important;
    }
    html[data-theme="dark"] .stat-value,
    html[data-theme="dark"] .page-title,
    html[data-theme="dark"] .drawer-title,
    html[data-theme="dark"] .summary-value,
    html[data-theme="dark"] .modal-title,
    html[data-theme="dark"] .section-title,
    html[data-theme="dark"] .donut-center .val,
    html[data-theme="dark"] .legend-pct,
    html[data-theme="dark"] .bar-value,
    html[data-theme="dark"] .mini-card-title,
    html[data-theme="dark"] h1,
    html[data-theme="dark"] h2,
    html[data-theme="dark"] h3,
    html[data-theme="dark"] h4,
    html[data-theme="dark"] strong {
      color: var(--text) !important;
    }
    html[data-theme="dark"] .btn.primary,
    html[data-theme="dark"] button.btn.primary,
    html[data-theme="dark"] .btn-primary,
    html[data-theme="dark"] .report-btn,
    html[data-theme="dark"] .kpi-save-btn {
      background: #2d3a4c !important;
      color: var(--text) !important;
      border-color: #40516a !important;
    }
    html[data-theme="dark"] .btn.ghost,
    html[data-theme="dark"] button.btn.ghost,
    html[data-theme="dark"] .btn,
    html[data-theme="dark"] .btn-danger-ghost,
    html[data-theme="dark"] .icon-btn,
    html[data-theme="dark"] .search-box,
    html[data-theme="dark"] .lang-btn,
    html[data-theme="dark"] .quick-btn {
      background: #1f1f1f;
      color: var(--text);
      border-color: var(--border);
    }
    html[data-theme="dark"] .btn:disabled,
    html[data-theme="dark"] button:disabled {
      background: #2a2a2a !important;
      color: #777 !important;
      border-color: var(--border) !important;
    }
    html[data-theme="dark"] .btn svg,
    html[data-theme="dark"] .icon-btn svg {
      stroke: currentColor;
    }
    html[data-theme="dark"] .btn-danger-ghost,
    html[data-theme="dark"] .danger,
    html[data-theme="dark"] .text-danger {
      color: #ff8177 !important;
    }
    html[data-theme="dark"] input,
    html[data-theme="dark"] textarea,
    html[data-theme="dark"] select,
    html[data-theme="dark"] .input,
    html[data-theme="dark"] .input-text,
    html[data-theme="dark"] .input-search,
    html[data-theme="dark"] .kpi-input {
      background: #1f1f1f;
      color: var(--text);
      border-color: var(--border);
    }
    html[data-theme="dark"] input::placeholder,
    html[data-theme="dark"] textarea::placeholder {
      color: #777;
    }
    html[data-theme="dark"] .tbl-books th,
    html[data-theme="dark"] .tbl-books td,
    html[data-theme="dark"] .tbl th,
    html[data-theme="dark"] .tbl td {
      border-color: var(--border);
      color: var(--text);
    }
    html[data-theme="dark"] .tbl-books th,
    html[data-theme="dark"] .tbl th {
      color: #7b7b7b;
    }
    html[data-theme="dark"] .tbl-books tr:hover td,
    html[data-theme="dark"] .tbl tr:hover td,
    html[data-theme="dark"] .suggestion-item:hover,
    html[data-theme="dark"] .mini-card,
    html[data-theme="dark"] .modal-list-row,
    html[data-theme="dark"] .bar-row,
    html[data-theme="dark"] .rank-item:hover,
    html[data-theme="dark"] .inv-item:hover {
      background: var(--surface-soft) !important;
    }
    html[data-theme="dark"] .bar-track,
    html[data-theme="dark"] .kpi-progress {
      background: #1f1f1f !important;
      border-color: var(--border) !important;
    }
    html[data-theme="dark"] .bar-fill,
    html[data-theme="dark"] .kpi-progress-fill {
      background: #d7e7a8 !important;
    }
    html[data-theme="dark"] .drawer-overlay,
    html[data-theme="dark"] .modal-overlay {
      background: rgba(0,0,0,.72) !important;
    }
    html[data-theme="dark"] .modal-header,
    html[data-theme="dark"] .modal-footer,
    html[data-theme="dark"] .drawer-footer,
    html[data-theme="dark"] .list-modal-header,
    html[data-theme="dark"] .pagination-container {
      background: var(--surface) !important;
      border-color: var(--border) !important;
      color: var(--text) !important;
    }
    html[data-theme="dark"] .toast,
    html[data-theme="dark"] .notice,
    html[data-theme="dark"] .app-toast {
      background: #253044 !important;
      color: var(--text) !important;
      border-color: #3a4960 !important;
      box-shadow: 0 14px 34px rgba(0,0,0,.34) !important;
    }
    html[data-theme="dark"] .tbl-books tr.customer-tier-row td {
      background: var(--tier-bg, rgba(255,255,255,0.035));
    }
    html[data-theme="dark"] .customer-tier-bronze { --tier-bg: rgba(183,138,92,.08); }
    html[data-theme="dark"] .customer-tier-silver { --tier-bg: rgba(145,160,173,.08); }
    html[data-theme="dark"] .customer-tier-gold { --tier-bg: rgba(214,169,0,.12); }
    html[data-theme="dark"] .customer-tier-platinum { --tier-bg: rgba(86,183,207,.10); }
    html[data-theme="dark"] .tbl-books tr.customer-tier-vip td {
      background: rgba(86,183,207,.11) !important;
    }
    html[data-theme="dark"] .link {
      color: #e7e7e7 !important;
    }
    html[data-theme="dark"] .link.danger {
      color: #ff8177 !important;
    }
    html[data-theme="dark"] .badge.paid,
    html[data-theme="dark"] .badge.ok {
      background: #12351b;
      color: #62d071;
    }
    html[data-theme="dark"] .badge.bad {
      background: #3d1717;
      color: #ff8177;
    }
    html[data-theme="dark"] .quick-btn,
    html[data-theme="dark"] .quick-icon-wrap,
    html[data-theme="dark"] .settings-card-icon,
    html[data-theme="dark"] .toggle-track,
    html[data-theme="dark"] .volume-row,
    html[data-theme="dark"] .kbd {
      background: #1f1f1f;
      border-color: var(--border);
      color: var(--text);
    }
    html[data-theme="dark"] .quick-btn {
      background: #202936 !important;
      border-color: var(--border) !important;
      color: var(--text) !important;
    }
    html[data-theme="dark"] .quick-btn:hover {
      background: #283344 !important;
      border-color: var(--border-strong) !important;
    }
    html[data-theme="dark"] .nav-item:hover,
    html[data-theme="dark"] .user-card:hover,
    html[data-theme="dark"] .icon-btn:hover,
    html[data-theme="dark"] .settings-row:hover,
    html[data-theme="dark"] .card-action:hover,
    html[data-theme="dark"] .link:hover,
    html[data-theme="dark"] .music-skip-btn:hover,
    html[data-theme="dark"] .password-toggle:hover,
    html[data-theme="dark"] .lang-btn:hover {
      background: var(--surface-soft) !important;
      border-color: var(--border-strong) !important;
      color: var(--text) !important;
    }
    html[data-theme="dark"] .link.danger:hover,
    html[data-theme="dark"] .btn-danger-ghost:hover {
      background: rgba(255, 129, 119, .12) !important;
      border-color: rgba(255, 129, 119, .38) !important;
      color: #ff9b93 !important;
    }
    html[data-theme="dark"] .card:hover,
    html[data-theme="dark"] .stat-card:hover,
    html[data-theme="dark"] .table-card:hover {
      border-color: var(--border-strong) !important;
      box-shadow: 0 12px 28px rgba(0, 0, 0, .22) !important;
    }
    html[data-theme="dark"] .quick-icon-wrap {
      background: rgba(244,234,212,.10) !important;
    }
    html[data-theme="dark"] .modal-overlay {
      background: rgba(0,0,0,.62);
    }
    html[data-theme="dark"] [style*="color:#595959"],
    html[data-theme="dark"] [style*="color: #595959"],
    html[data-theme="dark"] [style*="color:#6b7280"],
    html[data-theme="dark"] [style*="color: #6b7280"],
    html[data-theme="dark"] [style*="color:#999"],
    html[data-theme="dark"] [style*="color: #999"],
    html[data-theme="dark"] [style*="color:#111"],
    html[data-theme="dark"] [style*="color: #111"],
    html[data-theme="dark"] [style*="color:#1f1f1f"],
    html[data-theme="dark"] [style*="color: #1f1f1f"],
    html[data-theme="dark"] [style*="color: black"],
    html[data-theme="dark"] [style*="color:black"] {
      color: var(--text) !important;
    }
    html[data-theme="dark"] [style*="background:#fff"],
    html[data-theme="dark"] [style*="background: #fff"],
    html[data-theme="dark"] [style*="background:#f5f5f5"],
    html[data-theme="dark"] [style*="background: #f5f5f5"],
    html[data-theme="dark"] [style*="background:white"],
    html[data-theme="dark"] [style*="background: white"],
    html[data-theme="dark"] [style*="background: rgb(255"],
    html[data-theme="dark"] [style*="background-color: rgb(255"] {
      background: var(--surface) !important;
    }
    html[data-theme="dark"] [style*="background:#f0f0f0"],
    html[data-theme="dark"] [style*="background: #f0f0f0"],
    html[data-theme="dark"] [style*="background: rgb(240"] {
      background: var(--border) !important;
    }
  `;

  const iconCss = `
    .app-nav-icon {
      width: 18px;
      height: 18px;
      flex: 0 0 18px;
      stroke: currentColor;
      stroke-width: 2;
      fill: none;
      stroke-linecap: round;
      stroke-linejoin: round;
    }
    .app-ui-icon {
      width: 20px;
      height: 20px;
      stroke: currentColor;
      stroke-width: 2;
      fill: none;
      stroke-linecap: round;
      stroke-linejoin: round;
    }
    .stat-icon-wrap .app-ui-icon,
    .settings-card-icon .app-ui-icon,
    .quick-icon-wrap .app-ui-icon {
      width: 22px;
      height: 22px;
    }
    .icon-btn .app-ui-icon {
      width: 18px;
      height: 18px;
    }
    .settings-card-header {
      gap: 12px !important;
    }
    .settings-card-icon {
      display: grid !important;
      place-items: center;
      flex: 0 0 38px;
    }
    .nav-item {
      gap: 10px !important;
    }
    .logo-mark {
      display: grid !important;
      place-items: center;
      color: var(--surface, #fff) !important;
    }
    .logo-mark .app-logo-icon {
      width: 20px;
      height: 20px;
      stroke: currentColor;
      stroke-width: 2;
      fill: none;
      stroke-linecap: round;
      stroke-linejoin: round;
    }
    .btn,
    button.btn,
    .btn-primary,
    .report-btn,
    .kpi-save-btn,
    .logout-btn,
    .card-action,
    .link {
      border-radius: 8px !important;
      box-shadow: none !important;
      transition: background .15s ease, border-color .15s ease, color .15s ease, transform .15s ease !important;
    }
    .btn:not(.btn-danger-ghost):not(.danger),
    button.btn:not(.btn-danger-ghost):not(.danger),
    .btn-primary,
    .report-btn,
    .kpi-save-btn {
      background: #f4ead4 !important;
      border-color: #ddd0b5 !important;
      color: #2f2b22 !important;
    }
    .btn:not(.btn-danger-ghost):not(.danger):hover,
    button.btn:not(.btn-danger-ghost):not(.danger):hover,
    .btn-primary:hover,
    .report-btn:hover,
    .kpi-save-btn:hover {
      background: #eadbbd !important;
      border-color: #cdbb95 !important;
      color: #241f18 !important;
    }
    .btn.ghost,
    button.btn.ghost {
      background: transparent !important;
      color: var(--text) !important;
      border-color: var(--border) !important;
    }
    html[data-theme="dark"] .btn:not(.btn-danger-ghost):not(.danger),
    html[data-theme="dark"] button.btn:not(.btn-danger-ghost):not(.danger),
    html[data-theme="dark"] .btn-primary,
    html[data-theme="dark"] .report-btn,
    html[data-theme="dark"] .kpi-save-btn {
      background: #253044 !important;
      border-color: #3a4960 !important;
      color: #eef2f7 !important;
    }
    html[data-theme="dark"] .btn:not(.btn-danger-ghost):not(.danger):hover,
    html[data-theme="dark"] button.btn:not(.btn-danger-ghost):not(.danger):hover,
    html[data-theme="dark"] .btn-primary:hover,
    html[data-theme="dark"] .report-btn:hover,
    html[data-theme="dark"] .kpi-save-btn:hover {
      background: #2f3d54 !important;
      border-color: #50627c !important;
      color: #ffffff !important;
    }
    html[data-theme="dark"] .nav-item:hover,
    html[data-theme="dark"] .user-card:hover,
    html[data-theme="dark"] .icon-btn:hover,
    html[data-theme="dark"] .settings-row:hover,
    html[data-theme="dark"] .card-action:hover,
    html[data-theme="dark"] .link:hover,
    html[data-theme="dark"] .music-skip-btn:hover,
    html[data-theme="dark"] .password-toggle:hover,
    html[data-theme="dark"] .lang-btn:hover {
      background: var(--surface-soft) !important;
      border-color: var(--border-strong) !important;
      color: var(--text) !important;
    }
    html[data-theme="dark"] .logo-mark {
      color: var(--text) !important;
    }
    .app-close-x,
    .close.app-close-x,
    .list-modal-close.app-close-x {
      width: 34px !important;
      height: 34px !important;
      min-width: 34px !important;
      padding: 0 !important;
      display: inline-flex !important;
      align-items: center !important;
      justify-content: center !important;
      border: 1px solid rgba(216, 51, 51, .24) !important;
      border-radius: 8px !important;
      background: rgba(216, 51, 51, .08) !important;
      color: #d83333 !important;
      font-size: 22px !important;
      font-weight: 900 !important;
      line-height: 1 !important;
    }
    .app-close-x:hover,
    .close.app-close-x:hover,
    .list-modal-close.app-close-x:hover {
      background: rgba(216, 51, 51, .14) !important;
      border-color: rgba(216, 51, 51, .38) !important;
    }
    html[data-theme="dark"] .app-close-x {
      background: rgba(255, 98, 98, .12) !important;
      border-color: rgba(255, 98, 98, .28) !important;
      color: #ff6b6b !important;
    }
    input[type="range"] {
      --range-value: 50%;
      background: linear-gradient(90deg, var(--text, #1f1f1f) var(--range-value), var(--border, #e5e5dc) 0) !important;
    }
    input[type="range"]::-webkit-slider-thumb {
      border: 2px solid var(--surface, #fff);
    }
    html[data-theme="dark"] input[type="range"] {
      background: linear-gradient(90deg, #d7e7a8 var(--range-value), #3a3a3a 0) !important;
      border: 1px solid #444 !important;
    }
    html[data-theme="dark"] input[type="range"]::-webkit-slider-thumb {
      background: #8bb8ff !important;
      border: 2px solid #253044 !important;
      box-shadow: 0 0 0 4px rgba(139,184,255,.14) !important;
    }
  `;

  const appCss = `
    .app-toast {
      position: fixed;
      right: 24px;
      bottom: 24px;
      z-index: 3000;
      max-width: min(360px, calc(100vw - 32px));
      padding: 12px 14px;
      border: 1px solid var(--border, #e6e6e0);
      border-radius: 10px;
      background: var(--text, #1f1f1f);
      color: #fff;
      box-shadow: var(--shadow-md, 0 4px 16px rgba(0,0,0,.14));
      font-weight: 700;
      font-size: 13px;
      opacity: 0;
      transform: translateY(10px);
      pointer-events: none;
      transition: .2s ease;
    }
    .app-toast.show {
      opacity: 1;
      transform: translateY(0);
    }
    :is(a, button, input, select, textarea, .nav-item, .icon-btn, .quick-btn, .card-action, .link):focus-visible {
      outline: 3px solid rgba(58, 125, 68, .32) !important;
      outline-offset: 2px !important;
    }
    :is(button, .btn, .btn-primary, .icon-btn, .nav-item, .quick-btn, .card-action, .link) {
      touch-action: manipulation;
    }
    :is(.btn, button.btn, .btn-primary, .report-btn, .kpi-save-btn, .logout-btn, .icon-btn, .nav-item, .quick-btn) {
      min-height: 44px;
    }
    .card-action {
      min-height: 36px;
      padding: 0 6px;
      border-radius: 8px;
    }
    .table-wrap {
      -webkit-overflow-scrolling: touch;
    }
    @media (prefers-reduced-motion: reduce) {
      *, *::before, *::after {
        animation-duration: .01ms !important;
        animation-iteration-count: 1 !important;
        scroll-behavior: auto !important;
        transition-duration: .01ms !important;
      }
    }
    @media (max-width: 1100px) {
      body {
        display: block !important;
        min-height: 100dvh !important;
        height: auto !important;
        overflow: auto !important;
      }
      .sidebar {
        position: sticky !important;
        top: 0 !important;
        z-index: 80 !important;
        display: flex !important;
        width: 100% !important;
        min-width: 0 !important;
        max-height: none !important;
        padding: 0 !important;
        border-right: 0 !important;
        border-bottom: 1px solid var(--border) !important;
        overflow-x: auto !important;
        overflow-y: hidden !important;
        flex-direction: row !important;
        align-items: stretch !important;
      }
      .sidebar-logo {
        flex: 0 0 auto !important;
        min-width: 176px !important;
        padding: 12px 14px !important;
        border-bottom: 0 !important;
        border-right: 1px solid var(--border) !important;
      }
      .logo-sub,
      .sidebar-footer,
      .nav-label {
        display: none !important;
      }
      .nav-section {
        display: flex !important;
        flex: 0 0 auto !important;
        gap: 6px !important;
        padding: 10px 8px !important;
        align-items: center !important;
      }
      .nav-item {
        flex: 0 0 auto !important;
        min-height: 44px !important;
        padding: 9px 12px !important;
        white-space: nowrap !important;
      }
      .nav-item .app-nav-icon {
        width: 17px !important;
        height: 17px !important;
      }
      .main {
        min-height: calc(100dvh - 66px) !important;
        overflow: visible !important;
      }
      .topbar {
        position: sticky !important;
        top: 65px !important;
        min-height: 60px !important;
        height: auto !important;
        padding: 10px 18px !important;
        align-items: flex-start !important;
      }
      .topbar-title {
        font-size: 17px !important;
        line-height: 1.25 !important;
      }
      .topbar-subtitle {
        line-height: 1.35 !important;
      }
      .content {
        padding: 18px !important;
        overflow: visible !important;
      }
      .stats-grid,
      [style*="grid-template-columns:repeat(4,1fr)"],
      [style*="grid-template-columns: repeat(4, 1fr)"] {
        grid-template-columns: repeat(2, minmax(0, 1fr)) !important;
      }
      .mid-row,
      .bot-row,
      .create-grid,
      [style*="grid-template-columns:1fr 1fr"],
      [style*="grid-template-columns: 1fr 1fr"],
      [style*="grid-template-columns:1.3fr 1fr"],
      [style*="grid-template-columns: 1.3fr 1fr"] {
        grid-template-columns: 1fr !important;
      }
      .create-grid {
        height: auto !important;
        min-height: 0 !important;
      }
      .cart-container {
        min-height: 320px !important;
        height: auto !important;
      }
      .drawer-box {
        width: min(100vw, 620px) !important;
      }
      .modal-box,
      .modal,
      .list-modal {
        width: min(100%, calc(100vw - 24px)) !important;
        max-height: calc(100dvh - 24px) !important;
      }
    }
    @media (max-width: 640px) {
      body {
        font-size: 14px !important;
      }
      .sidebar-logo {
        min-width: 68px !important;
        justify-content: center !important;
      }
      .sidebar-logo > div:last-child {
        display: none !important;
      }
      .nav-section {
        gap: 4px !important;
        padding: 8px 6px !important;
      }
      .nav-item {
        padding: 9px 10px !important;
        gap: 7px !important;
      }
      .topbar {
        top: 61px !important;
        padding: 10px 14px !important;
        gap: 10px !important;
      }
      .topbar-actions {
        flex-shrink: 0 !important;
      }
      .content {
        padding: 14px !important;
      }
      .stats-grid,
      [style*="grid-template-columns:repeat(4,1fr)"],
      [style*="grid-template-columns: repeat(4, 1fr)"] {
        grid-template-columns: 1fr !important;
      }
      .card,
      .table-card,
      .settings-card,
      .section-card {
        padding: 16px !important;
      }
      .header-row,
      .card-header,
      .report-toolbar,
      .modal-footer,
      .footer-actions {
        align-items: stretch !important;
        flex-wrap: wrap !important;
      }
      .modal-body {
        padding: 16px !important;
      }
      .drawer-box {
        width: 100vw !important;
      }
      .detail-summary-grid,
      .grid {
        grid-template-columns: 1fr !important;
      }
      .quick-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr)) !important;
      }
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
    'Báo cáo tồn kho từ backend': 'Inventory reports from backend',
    'Bài tiếp theo': 'Next Play'
  };

  const translationPairs = [
    ['Tổng quan', 'Dashboard'], ['Sách', 'Books'], ['Danh mục', 'Categories'],
    ['Khách Hàng', 'Customers'], ['Khách hàng', 'Customers'], ['Nhân viên', 'Staff'],
    ['Nghiệp vụ', 'Operations'], ['Bán hàng', 'Sales'], ['Hóa đơn', 'Invoices'],
    ['Nhập hàng', 'Purchasing'], ['Phiếu giữ', 'Reservations'], ['Báo cáo', 'Reports'],
    ['Doanh thu', 'Revenue'], ['Tồn kho', 'Inventory'], ['Hệ thống', 'System'],
    ['Cài đặt', 'Settings'], ['Đăng xuất', 'Logout'], ['Quản lý', 'Management'],
    ['Tìm kiếm sách, hóa đơn, khách hàng...', 'Search books, invoices, customers...'],
    ['Tìm kiếm sách, hóa đơn, khách hàng…', 'Search books, invoices, customers...'],
    ['BookHouse – Tổng quan', 'BookHouse - Dashboard'],
    ['Chào mừng bạn trở lại, Admin!', 'Welcome back, Admin!'],
    ['Tri thức trong tầm tay', 'Knowledge within reach'],
    ['Quản lý toàn bộ đầu sách trong kho', 'Manage all books in stock'],
    ['Quản lý thể loại sách', 'Manage book categories'],
    ['Quản lý thông tin khách hàng', 'Manage customer information'],
    ['Quản lý nhân sự cửa hàng', 'Manage store staff'],
    ['Báo cáo doanh thu từ backend', 'Revenue reports from backend'],
    ['Báo cáo tồn kho từ backend', 'Inventory reports from backend'],
    ['Doanh thu hôm nay', 'Today revenue'], ['Hóa đơn hôm nay', 'Today invoices'],
    ['Sách đã bán', 'Books sold'], ['Tồn kho hiện tại', 'Current inventory'],
    ['KPI doanh thu hôm nay', 'Today revenue KPI'], ['KPI hôm nay', 'Today KPI'],
    ['Lưu', 'Save'], ['Lỗi', 'Error'], ['Lỗi API', 'API error'], ['Từ backend', 'From backend'],
    ['Quản trị viên', 'Administrator'], ['Hôm nay:', 'Today:'], ['Hôm nay', 'Today'],
    ['Cần quyền XEM BÁO CÁO để đặt KPI.', 'REPORT_VIEW permission is required to set KPI.'],
    ['Doanh thu 7 ngày qua', 'Revenue over the last 7 days'], ['Theo danh mục', 'By category'],
    ['Tổng', 'Total'], ['Khác', 'Other'], ['Thời gian', 'Time'], ['Khách lẻ', 'Walk-in customer'],
    ['Đã thanh toán', 'Paid'], ['Thao tác nhanh', 'Quick actions'], ['Thông tin hệ thống', 'System information'],
    ['Phiên bản', 'Version'], ['Cơ sở dữ liệu', 'Database'],
    ['Dữ liệu lấy từ /api/bao-cao để test trên Vercel.', 'Data is loaded from /api/bao-cao for Vercel testing.'],
    ['7 ngày gần nhất', 'Last 7 days'], ['30 ngày gần nhất', 'Last 30 days'],
    ['Bộ lọc đang xem', 'Current filter'], ['Biểu đồ doanh thu 7 ngày', '7-day revenue chart'],
    ['Biểu đồ doanh thu 30 ngày', '30-day revenue chart'], ['Doanh thu theo ngày', 'Revenue by day'],
    ['Ngày', 'Date'], ['Chưa tải dữ liệu', 'Data not loaded'], ['Chưa tải dữ liệu.', 'Data not loaded.'],
    ['Doanh thu theo thể loại', 'Revenue by category'],
    ['Kiểm tra tổng tồn kho, sách tồn nhiều và sách sắp hết hàng.', 'Review total inventory, highest-stock books, and low-stock books.'],
    ['Tổng tồn kho', 'Total inventory'], ['Trạng thái API', 'API status'], ['Sách sắp hết hàng', 'Low-stock books'],
    ['Danh sách', 'List'], ['Dữ liệu từ dashboard', 'Dashboard data'],
    ['Đang tải', 'Loading'], ['Đang tải...', 'Loading...'], ['Chưa gọi API', 'API not called'],
    ['Chưa đặt KPI', 'KPI not set'], ['Đã đạt KPI', 'KPI reached'], ['Chưa đạt KPI', 'KPI not reached'],
    ['7 ngày qua', 'Last 7 days'], ['30 ngày qua', 'Last 30 days'],
    ['Xem tất cả', 'View all'],
    ['Thêm sách', 'Add book'], ['Tạo hóa đơn', 'Create invoice'],
    ['Năm', 'Year'], ['Tháng', 'Month'], ['Theo tháng', 'By month'],
    ['Từ ngày', 'From date'], ['Đến ngày', 'To date'], ['Theo khoảng', 'By range'],
    ['Tổng doanh thu hôm nay', 'Total revenue today'],
    ['Tổng theo endpoint bảy ngày trước', 'Total from seven-day endpoint'],
    ['Tổng theo endpoint ba mươi ngày trước', 'Total from thirty-day endpoint'],
    ['Tổng theo khoảng đã chọn', 'Total for selected range'],
    ['Tải lại', 'Reload'], ['Số lượng sách hiện còn', 'Books currently in stock'],
    ['Kết quả gọi endpoint tồn kho', 'Inventory endpoint result'], ['Đóng', 'Close'],
    ['Xem báo cáo doanh thu', 'View revenue report'], ['Xem báo cáo tồn kho', 'View inventory report'],
    ['Đặt KPI doanh thu hôm nay', 'Set today revenue KPI'],
    ['Sách bán chạy', 'Best-selling books'],
    ['Danh sách đầy đủ theo số lượng đã bán', 'Full list by sold quantity'],
    ['Tồn kho nhiều nhất', 'Highest inventory'],
    ['Danh sách đầy đủ theo số lượng tồn', 'Full list by stock quantity'],
    ['Hóa đơn mới nhất', 'Latest invoices'],
    ['Danh sách hóa đơn gần đây', 'Recent invoice list'],
    ['Danh sách đầu sách', 'Book list'], ['Tìm kiếm sách...', 'Search books...'],
    ['Tất cả thể loại', 'All categories'], ['Kinh tế', 'Business'], ['Tâm lý', 'Psychology'],
    ['Văn học', 'Literature'], ['Thiếu nhi', 'Children'], ['Kỹ năng', 'Skills'],
    ['Tổng sách', 'Total books'], ['Bán chạy nhất', 'Best seller'], ['Hết hàng', 'Out of stock'],
    ['Giá trị kho', 'Inventory value'], ['STT', 'No.'], ['Ảnh', 'Image'], ['Tên', 'Name'],
    ['Mục', 'Category'], ['Giá', 'Price'], ['Số lượng', 'Quantity'], ['Trạng thái', 'Status'],
    ['Thao tác', 'Actions'], ['Còn hàng', 'In stock'], ['Sửa', 'Edit'], ['Xóa', 'Delete'],
    ['Tên sách', 'Book title'], ['Giá bán', 'Sale price'], ['Số lượng tồn', 'Stock quantity'],
    ['Năm xuất bản', 'Publication year'], ['Ảnh sách', 'Book image'], ['Thể loại', 'Category'],
    ['Nhà xuất bản', 'Publisher'], ['Tác giả', 'Author'], ['Mô tả sách', 'Book description'],
    ['Ngôn ngữ', 'Language'], ['Tiếng Việt', 'Vietnamese'], ['Số trang', 'Pages'],
    ['Không để trống tên sách', 'Book title cannot be empty'], ['Giá phải lớn hơn 0', 'Price must be greater than 0'],
    ['Preview ảnh sẽ hiển thị ở đây', 'Image preview will appear here'],
    ['-- Chọn thể loại --', '-- Select category --'], ['-- Chọn NXB --', '-- Select publisher --'],
    ['-- Chọn tác giả --', '-- Select author --'],
    ['Thêm thể loại', 'Add category'], ['Tìm kiếm thể loại...', 'Search categories...'],
    ['Tên thể loại', 'Category name'], ['Mô tả', 'Description'], ['Hủy', 'Cancel'],
    ['Lưu thể loại', 'Save category'], ['Cập nhật thể loại', 'Update category'], ['Sửa thể loại', 'Edit category'],
    ['Nhập tên thể loại', 'Enter category name'], ['Tên thể loại không được để trống', 'Category name cannot be empty'],
    ['Đang tải thể loại...', 'Loading categories...'],
    ['Thêm khách hàng', 'Add customer'], ['Tìm kiếm khách hàng...', 'Search customers...'], ['Lọc', 'Filter'],
    ['Tổng khách hàng', 'Total customers'], ['Khách hàng VIP', 'VIP customers'],
    ['Khách hàng mới tháng này', 'New customers this month'], ['Tổng đơn hàng', 'Total orders'],
    ['Họ tên', 'Full name'], ['Số điện thoại', 'Phone number'], ['Ngày sinh', 'Date of birth'],
    ['Địa chỉ', 'Address'], ['Điểm tích lũy', 'Loyalty points'], ['Hạng thành viên', 'Membership tier'],
    ['Avatar khách hàng', 'Customer avatar'], ['Ghi chú', 'Notes'], ['Lưu khách hàng', 'Save customer'],
    ['Cập nhật', 'Update'], ['Chi tiết khách hàng', 'Customer details'], ['Xem chi tiết', 'View details'],
    ['Không được để trống tên', 'Name cannot be empty'], ['SĐT hợp lệ, ví dụ 09xxxxxxxx', 'Valid phone, e.g. 09xxxxxxxx'],
    ['Email hợp lệ', 'Valid email'], ['Tên không được để trống', 'Name cannot be empty'],
    ['SĐT phải đúng định dạng', 'Phone number has invalid format'], ['Email không hợp lệ', 'Invalid email'],
    ['Preview avatar sẽ hiển thị ở đây', 'Avatar preview will appear here'],
    ['Không', 'No'], ['Có', 'Yes'], ['Hoạt động', 'Active'], ['Ngừng hoạt động', 'Inactive'],
    ['Đồng', 'Bronze'], ['Bạc', 'Silver'], ['Vàng', 'Gold'], ['Bạch Kim', 'Platinum'], ['Kim Cương', 'Diamond'],
    ['Đang tải danh sách khách hàng...', 'Loading customer list...'],
    ['Không thể tải danh sách khách hàng từ backend.', 'Could not load customers from backend.'],
    ['Thêm nhân viên', 'Add staff'], ['Tìm kiếm nhân viên...', 'Search staff...'],
    ['Tổng nhân viên', 'Total staff'], ['Đang làm việc', 'Working'],
    ['Quản lý cửa hàng', 'Store managers'], ['Nhân viên xuất sắc', 'Top staff'],
    ['Mã nhân viên', 'Staff code'], ['Chức vụ', 'Role'], ['Ca làm việc', 'Shift'],
    ['Ngày vào làm', 'Start date'], ['Lương cơ bản', 'Base salary'], ['Lưu nhân viên', 'Save staff'],
    ['Sửa nhân viên', 'Edit staff'], ['Xem hồ sơ', 'View profile'],
    ['Đang tải danh sách nhân viên...', 'Loading staff list...'],
    ['Không thể tải danh sách nhân viên từ backend:', 'Could not load staff from backend:'],
    ['Đang tải nhân viên xuất sắc...', 'Loading top staff...'],
    ['Không thể tải nhân viên xuất sắc:', 'Could not load top staff:'],
    ['Nhân viên bán hàng', 'Sales staff'], ['Quản lý', 'Manager'], ['Thu ngân', 'Cashier'],
    ['Kho', 'Warehouse'], ['Sáng', 'Morning'], ['Chiều', 'Afternoon'], ['Tối', 'Evening'], ['Cả ngày', 'Full day'],
    ['Nhập tên khách hàng để tìm kiếm...', 'Enter customer name to search...'],
    ['Tìm nhanh tên sách...', 'Quickly search book title...'],
    ['Không tìm thấy sách phù hợp', 'No matching books found'],
    ['Không tìm thấy khách hàng', 'No customers found'],
    ['Chưa có sản phẩm trong giỏ hàng', 'No products in cart'],
    ['Thêm', 'Add'], ['Chọn', 'Select'], ['Tạo tài khoản khách hàng mới', 'Create new customer account'],
    ['Họ tên *', 'Full name *'], ['Số điện thoại *', 'Phone number *'],
    ['BookHouse – Bán hàng', 'BookHouse - Sales'], ['Đặt giữ sách', 'Book reservation'],
    ['Quầy Bán Hàng (POS)', 'Point of Sale (POS)'],
    ['Hệ thống tạo đơn hàng và tính tiền nhanh tại quầy', 'Create orders and checkout quickly at the counter'],
    ['Thông tin khách hàng', 'Customer information'], ['Tạo KH', 'Create customer'], ['+ Tạo KH', 'Create customer'],
    ['Danh mục sản phẩm', 'Product catalog'], ['Hành động', 'Action'],
    ['Giỏ hàng lập đơn (', 'Order cart ('], ['Tổng tiền thanh toán', 'Payment total'],
    ['XÁC NHẬN LẬP HÓA ĐƠN', 'CONFIRM INVOICE CREATION'], ['Tạo mới', 'Create new'],
    ['Hóa đơn trong ngày', 'Invoices today'], ['Tổng hóa đơn', 'Total invoices'],
    ['Doanh thu trong ngày', 'Revenue today'], ['Tổng doanh thu', 'Total revenue'],
    ['Tìm ID hóa đơn (số)...', 'Search invoice ID (number)...'],
    ['Mã HĐ', 'Invoice code'], ['Ngày bán', 'Sale date'], ['Tổng tiền', 'Total amount'],
    ['Khách hàng:', 'Customer:'], ['Mã KH:', 'Customer ID:'], ['Nhân viên lập:', 'Created by:'],
    ['Mã NV:', 'Staff ID:'], ['Ngày bán:', 'Sale date:'], ['Trạng thái:', 'Status:'],
    ['Không tìm thấy hóa đơn tương thích', 'No matching invoices found'],
    ['Hủy hóa đơn', 'Cancel invoice'], ['Hoàn thành', 'Completed'], ['Đã hủy', 'Canceled'],
    ['Hóa đơn đã đóng', 'Invoice closed'],
    ['BookHouse – Quản lý hóa đơn', 'BookHouse - Invoice management'],
    ['Hệ thống quản lý quy trình tra cứu bán sách', 'Manage book sale lookup workflow'],
    ['Quản lý hóa đơn', 'Invoice management'], ['Tạo hóa đơn mới', 'Create new invoice'],
    ['Hiển thị 0-0 dòng trong tổng số 0 dòng', 'Showing 0-0 rows out of 0 rows'],
    ['Trước', 'Previous'], ['Sau', 'Next'],
    ['Chi tiết hóa đơn', 'Invoice details'], ['Danh sách sản phẩm', 'Product list'],
    ['Thành tiền cần thanh toán', 'Amount due'],
    ['Phiếu nhập trong ngày', 'Purchase orders today'],
    ['Tổng phiếu nhập kho', 'Total purchase orders'], ['Vốn nhập trong ngày', 'Purchase cost today'],
    ['Tổng vốn đầu tư kho', 'Total inventory investment'],
    ['Tìm ID phiếu nhập (số)...', 'Search purchase order ID (number)...'],
    ['Mã Phiếu', 'Order code'], ['Nhà cung cấp', 'Supplier'], ['Ngày nhập kho', 'Stock-in date'],
    ['Tổng tiền nhập', 'Total purchase amount'], ['Lập phiếu nhập kho hàng', 'Create purchase order'],
    ['Nhập tên nhà cung cấp cần tìm...', 'Enter supplier name to search...'],
    ['Giá niêm yết', 'List price'], ['Hủy bỏ', 'Cancel'], ['Thêm mới Nhà Cung Cấp đối tác', 'Add new supplier partner'],
    ['Tên nhà cung cấp *', 'Supplier name *'], ['Số điện thoại liên hệ *', 'Contact phone *'],
    ['Địa chỉ trụ sở chính', 'Head office address'], ['Nhập địa chỉ nhà cung cấp...', 'Enter supplier address...'],
    ['Nhà cung cấp đối tác:', 'Supplier partner:'], ['Mã NCC:', 'Supplier ID:'],
    ['Nhân viên kho:', 'Warehouse staff:'], ['Thời gian nhập:', 'Stock-in time:'],
    ['Không tìm thấy phiếu nhập kho tương thích', 'No matching purchase orders found'],
    ['Không tìm thấy nhà cung cấp nào', 'No suppliers found'], ['Chưa chọn mặt hàng nào để nhập kho', 'No items selected for stock-in'],
    ['Giá Nhập (đ)', 'Import price (VND)'], ['S.Lượng', 'Qty'], ['Xóa phiếu nhập', 'Delete purchase order'],
    ['BookHouse – Quản lý phiếu nhập', 'BookHouse - Purchase order management'],
    ['Quản kho', 'Warehouse management'], ['Kho quản trị viên', 'Warehouse administrator'],
    ['Nhập hàng kho', 'Stock receiving'],
    ['Hệ thống xử lý lưu trữ phiếu nhập đầu sách', 'Process and store book purchase orders'],
    ['Quản lý nhập kho (Phiếu Nhập)', 'Stock receiving management'],
    ['Thêm NCC', 'Add supplier'], ['Tìm sách cần nhập kho', 'Search books to receive'],
    ['Danh sách hàng nhập (', 'Receiving list ('], ['Tổng vốn đầu tư', 'Total investment'],
    ['Hoàn tất nhập kho', 'Complete stock-in'], ['Tạo đối tác', 'Create partner'],
    ['Chi tiết phiếu nhập kho', 'Purchase order details'],
    ['Danh sách mặt hàng chi tiết', 'Detailed item list'], ['Tổng dòng vốn thanh toán', 'Total payment amount'],
    ['Ví dụ: Nhà sách Tuổi Trẻ', 'Example: Tuoi Tre Bookstore'], ['Ví dụ: 0912345678', 'Example: 0912345678'],
    ['Tìm mã phiếu giữ (ID)...', 'Search reservation ID...'], ['Ngày lập', 'Created date'],
    ['Số lượng sách', 'Book quantity'], ['Lập phiếu giữ sách mới', 'Create new book reservation'],
    ['Nhập họ tên hoặc số điện thoại khách hàng...', 'Enter customer name or phone...'],
    ['Tìm tên sách nhanh...', 'Quickly search book title...'], ['Mã khách hàng:', 'Customer ID:'],
    ['Trạng thái phiếu:', 'Reservation status:'], ['Ngày lập phiếu:', 'Created date:'],
    ['Hạn lấy sách:', 'Pickup deadline:'], ['Không tìm thấy phiếu đặt giữ sách nào thích hợp', 'No matching reservations found'],
    ['Chưa có sản phẩm trong giỏ hàng giữ sách', 'No products in reservation cart'],
    ['Chờ lấy', 'Waiting for pickup'], ['Xác nhận lấy sách', 'Confirm pickup'],
    ['Hủy phiếu giữ', 'Cancel reservation'], ['Phiếu đã đóng quy trình', 'Reservation closed'],
    ['BookHouse – Quản lý phiếu giữ sách', 'BookHouse - Reservation management'],
    ['Phiếu giữ sách', 'Book reservations'],
    ['Hệ thống xử lý đặt giữ sách trước cho khách hàng', 'Manage advance book reservations for customers'],
    ['Quản lý phiếu giữ sách', 'Reservation management'], ['Tạo phiếu giữ sách', 'Create reservation'],
    ['Tìm kiếm sách giữ', 'Search reserved books'], ['Giỏ hàng đặt giữ (', 'Reservation cart ('],
    ['Tổng số lượng giữ', 'Total reserved quantity'], ['0 quyển', '0 books'],
    ['XÁC NHẬN LẬP PHIẾU GIỮ', 'CONFIRM RESERVATION CREATION'],
    ['Tạo khách hàng', 'Create customer'], ['Chi tiết phiếu giữ sách', 'Reservation details'],
    ['Danh sách sản phẩm giữ', 'Reserved product list'],
    ['Giao diện', 'Appearance'], ['Chế độ tối', 'Dark mode'],
    ['Chọn màu nền và chế độ hiển thị', 'Choose colors and display mode'],
    ['Chuyển giao diện sang nền đen, giảm mỏi mắt ban đêm', 'Use a dark interface to reduce eye strain at night'],
    ['Âm nhạc nền', 'Background music'], ['Nhạc nền nhẹ khi sử dụng hệ thống', 'Light background music while using the system'],
    ['Bật nhạc nền', 'Enable background music'], ['Phát nhạc nền khi bạn đang làm việc', 'Play background music while you work'],
    ['Âm lượng', 'Volume'], ['Chọn ngôn ngữ hiển thị của hệ thống', 'Choose the display language'],
    ['Tải lại trang để áp dụng ngôn ngữ mới', 'Reload the page to apply the new language'],
    ['Chuyển chế độ sáng/tối', 'Toggle light/dark mode'],
    ['BookHouse – Cài đặt', 'BookHouse - Settings'], ['Tuỳ chỉnh trải nghiệm của bạn', 'Customize your experience'],
    ['BookHouse – Đăng nhập', 'BookHouse - Login'], ['hiệu sách', 'bookstore'], ['thông minh', 'smart'],
    ['Theo dõi doanh thu, quản lý kho hàng, và xử lý hóa đơn nhanh chóng — tất cả trong một giao diện.', 'Track revenue, manage inventory, and process invoices quickly in one interface.'],
    ['Đầu sách', 'Book titles'], ['Bán hôm nay', 'Sold today'], ['Chào mừng trở lại', 'Welcome back'],
    ['Đăng nhập để tiếp tục quản lý cửa hàng', 'Log in to continue managing the store'],
    ['Sai tài khoản hoặc mật khẩu', 'Incorrect username or password'],
    ['Tên đăng nhập', 'Username'], ['Mật khẩu', 'Password'], ['Ghi nhớ đăng nhập', 'Remember me'],
    ['Quên mật khẩu?', 'Forgot password?'], ['Đăng nhập', 'Log in'],
    ['Đang đăng nhập...', 'Logging in...'], ['Hiện', 'Show'], ['Ẩn', 'Hide'],
    ['Hệ thống quản lý nội bộ', 'Internal management system'],
    ['Nhập tên đăng nhập', 'Enter username'], ['Nhập mật khẩu', 'Enter password'],
    ['Hiện/ẩn mật khẩu', 'Show/hide password'],
    ['Chưa có khách hàng nào.', 'No customers yet.'],
    ['Quản lý sách – Danh sách đầu sách và kho hàng', 'Book management - Book list and inventory'],
    ['Tất cả chức vụ', 'All roles'], ['Biên mục', 'Cataloging'], ['Kế toán', 'Accounting'], ['Nhân sự', 'Human resources'],
    ['Hóa đơn của NV xuất sắc', 'Top staff invoices'], ['Quyền lợi theo chức vụ', 'Benefits by role'],
    ['Quyền lợi / quyền truy cập', 'Benefits / access rights'], ['Chưa có nhân viên nào.', 'No staff yet.'],
    ['Không được để trống', 'Required'], ['Ví dụ: NV001', 'Example: NV001'], ['SĐT hợp lệ', 'Valid phone number'],
    ['Avatar nhân viên', 'Staff avatar'], ['Hồ sơ nhân viên', 'Staff profile'], ['Bài tiếp theo', 'Next Play']

  ];

  function normalizeText(value) {
    return String(value || '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/đ/g, 'd')
      .replace(/Đ/g, 'D')
      .replace(/[^\w%]+/g, ' ')
      .replace(/\s+/g, ' ')
      .trim()
      .toLowerCase();
  }

  const normalizedTranslations = translationPairs.reduce((map, pair) => {
    const key = normalizeText(pair[0]);
    if (!map[key]) map[key] = pair[1];
    return map;
  }, {});
  const originalTextNodes = new WeakMap();
  const originalAttributes = new WeakMap();

  function isDarkMode() {
    return localStorage.getItem(THEME_KEY) === '1';
  }

  function injectDarkCss() {
    if (document.getElementById('bookhouse-dark-css')) return;
    const style = document.createElement('style');
    style.id = 'bookhouse-dark-css';
    style.textContent = darkCss + iconCss + appCss;
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
    const themeButtonIcon = document.querySelector('#topbarThemeBtn .app-ui-icon');
    if (themeButtonIcon) themeButtonIcon.innerHTML = isDark ? uiIconPaths.moon : uiIconPaths.sun;
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
      audio.preload = 'metadata';
      const source = document.createElement('source');
      source.type = 'audio/mpeg';
      audio.appendChild(source);
      audio.style.display = 'none';
      document.body.appendChild(audio);
    }
    audio.loop = false;
    audio.preload = 'metadata';
    preparePlaylistAudio(audio);
    return audio;
  }

  function getTrackIndex() {
    const saved = parseInt(localStorage.getItem(MUSIC_TRACK_KEY) || '0', 10);
    return Number.isFinite(saved) && saved >= 0 && saved < AUDIO_PLAYLIST.length ? saved : 0;
  }

  function setTrackIndex(index) {
    const next = ((Number(index) || 0) + AUDIO_PLAYLIST.length) % AUDIO_PLAYLIST.length;
    localStorage.setItem(MUSIC_TRACK_KEY, String(next));
    return next;
  }

  function setAudioSource(audio, index, keepTime = false) {
    const track = AUDIO_PLAYLIST[index] || AUDIO_PLAYLIST[0];
    if (!track) return;
    const currentFile = decodeURI((audio.getAttribute('src') || audio.currentSrc || '').split('/').pop() || '');
    const nextFile = decodeURI(track.src.split('/').pop());
    if (currentFile !== nextFile) {
      audio.src = track.src;
      const source = audio.querySelector('source');
      if (source) source.src = track.src;
      if (!keepTime) audio.currentTime = 0;
      audio.load();
    }
    syncMusicProgress();
  }

  function preparePlaylistAudio(audio) {
    if (!audio) return;
    setAudioSource(audio, getTrackIndex(), true);
    if (audio.dataset.playlistReady === '1') return;
    audio.dataset.playlistReady = '1';
    audio.addEventListener('ended', () => {
      playMusicTrack(getTrackIndex() + 1, true);
    });
  }

  function saveMusicTime() {
    const audio = document.getElementById(AUDIO_ID) || document.getElementById('bgAudio');
    if (audio && Number.isFinite(audio.currentTime)) {
      localStorage.setItem(MUSIC_TIME_KEY, String(audio.currentTime));
    }
  }

  function formatMusicTime(seconds) {
    const safeSeconds = Number.isFinite(seconds) && seconds > 0 ? Math.floor(seconds) : 0;
    const mins = Math.floor(safeSeconds / 60);
    const secs = String(safeSeconds % 60).padStart(2, '0');
    return `${mins}:${secs}`;
  }

  function syncMusicProgress() {
    const audio = document.getElementById(AUDIO_ID) || document.getElementById('bgAudio');
    const seek = document.getElementById('musicSeekSlider');
    const current = document.getElementById('musicCurrentTime');
    const duration = document.getElementById('musicDuration');
    const title = document.getElementById('musicTrackTitle');
    const track = AUDIO_PLAYLIST[getTrackIndex()] || AUDIO_PLAYLIST[0];
    if (title && track) title.textContent = track.title;
    const total = audio && Number.isFinite(audio.duration) ? audio.duration : 0;
    const now = audio && Number.isFinite(audio.currentTime) ? audio.currentTime : 0;
    if (seek) {
      seek.disabled = !total;
      seek.value = total ? String(Math.round((now / total) * 1000)) : '0';
      updateRangeSlider(seek);
    }
    if (current) current.textContent = formatMusicTime(now);
    if (duration) duration.textContent = formatMusicTime(total);
  }

  function syncMusicControls() {
    const enabled = localStorage.getItem(MUSIC_ENABLED_KEY) === '1';
    const volume = parseInt(localStorage.getItem(MUSIC_VOLUME_KEY) || '50', 10);
    const toggle = document.getElementById('musicToggle');
    const slider = document.getElementById('volumeSlider');
    const label = document.getElementById('volumeVal');
    if (toggle) toggle.checked = enabled;
    if (slider) {
      slider.value = String(volume);
      updateRangeSlider(slider);
    }
    if (label) label.textContent = volume + '%';
    syncMusicProgress();
  }

  function restoreMusic() {
    syncMusicControls();
    const enabled = localStorage.getItem(MUSIC_ENABLED_KEY) === '1';
    const hasMusicControls = document.getElementById('musicToggle') || document.getElementById('volumeSlider');
    if (!enabled && !hasMusicControls) return;
    const volume = parseInt(localStorage.getItem(MUSIC_VOLUME_KEY) || '50', 10);
    const audio = getAudio();
    audio.volume = Math.max(0, Math.min(100, volume)) / 100;
    setAudioSource(audio, getTrackIndex(), true);
    const savedTime = parseFloat(localStorage.getItem(MUSIC_TIME_KEY) || '0');
    if (Number.isFinite(savedTime) && savedTime > 0) {
      const applyTime = () => {
        if (audio.duration && savedTime < audio.duration) audio.currentTime = savedTime;
      };
      if (audio.readyState >= 1) applyTime();
      else audio.addEventListener('loadedmetadata', applyTime, { once: true });
    }
    if (audio.dataset.timeSaveReady !== '1') {
      audio.dataset.timeSaveReady = '1';
      audio.addEventListener('timeupdate', saveMusicTime);
      audio.addEventListener('timeupdate', syncMusicProgress);
      audio.addEventListener('durationchange', syncMusicProgress);
      audio.addEventListener('loadedmetadata', syncMusicProgress);
    }
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

  function playMusicTrack(index, autoplay = localStorage.getItem(MUSIC_ENABLED_KEY) === '1') {
    const audio = getAudio();
    const nextIndex = setTrackIndex(index);
    localStorage.setItem(MUSIC_TIME_KEY, '0');
    setAudioSource(audio, nextIndex, false);
    if (autoplay) audio.play().catch(() => {});
  }

  function skipMusicTrack() {
    localStorage.setItem(MUSIC_ENABLED_KEY, '1');
    playMusicTrack(getTrackIndex() + 1, true);
    syncMusicControls();
  }

  function previousMusicTrack() {
    localStorage.setItem(MUSIC_ENABLED_KEY, '1');
    playMusicTrack(getTrackIndex() - 1, true);
    syncMusicControls();
  }

  function seekMusicTrack(value) {
    const audio = getAudio();
    const total = Number.isFinite(audio.duration) ? audio.duration : 0;
    if (!total) return;
    const pct = Math.max(0, Math.min(1000, Number(value) || 0)) / 1000;
    audio.currentTime = total * pct;
    syncMusicProgress();
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
    const tierTranslation = {
      'Đồng': 'Bronze',
      'Bạc': 'Silver',
      'Vàng': 'Gold',
      'Bạch Kim': 'Platinum',
      'Kim Cương': 'Diamond',
      'Đồng + VIP': 'Bronze + VIP',
      'Bạc + VIP': 'Silver + VIP',
      'Vàng + VIP': 'Gold + VIP',
      'Bạch Kim + VIP': 'Platinum + VIP',
      'Kim Cương + VIP': 'Diamond + VIP',
    }[trimmed];
    if (tierTranslation) return tierTranslation;
    const exact = translations[trimmed] || normalizedTranslations[normalizeText(trimmed)];
    if (exact) return exact;

    for (const pair of translationPairs) {
      const vi = pair[0];
      const en = pair[1];
      if (trimmed.startsWith(vi + ':')) return trimmed.replace(vi, en);
    }
    return null;
  }

  function translateValue(value) {
    if (!value || !String(value).trim()) return null;
    return translateText(String(value));
  }

  function translateAttributes(lang) {
    const attrs = ['placeholder', 'title', 'aria-label'];
    document.querySelectorAll('*').forEach(element => {
      let saved = originalAttributes.get(element);
      if (!saved) {
        saved = {};
        originalAttributes.set(element, saved);
      }
      attrs.forEach(attr => {
        if (!element.hasAttribute(attr)) return;
        const current = element.getAttribute(attr);
        const currentTranslation = translateValue(current);
        if (!saved[attr] || currentTranslation) saved[attr] = current;
        if (lang === 'en') {
          const translated = translateValue(saved[attr]);
          if (translated && element.getAttribute(attr) !== translated) element.setAttribute(attr, translated);
        } else {
          if (element.getAttribute(attr) !== saved[attr]) element.setAttribute(attr, saved[attr]);
        }
      });
    });
  }

  function restoreVietnameseText() {
    const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, {
      acceptNode(node) {
        return originalTextNodes.has(node) ? NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_REJECT;
      }
    });
    const nodes = [];
    while (walker.nextNode()) nodes.push(walker.currentNode);
    nodes.forEach(node => {
      const original = originalTextNodes.get(node);
      if (node.nodeValue !== original) node.nodeValue = original;
    });
  }

  function applyLanguage(lang = currentLanguage()) {
    document.documentElement.lang = lang === 'en' ? 'en' : 'vi';
    const vi = document.getElementById('langVi');
    const en = document.getElementById('langEn');
    if (vi) vi.classList.toggle('active', lang === 'vi');
    if (en) en.classList.toggle('active', lang === 'en');
    translateAttributes(lang);
    if (lang !== 'en') {
      restoreVietnameseText();
      return;
    }

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
      const currentTranslation = translateText(node.nodeValue);
      if (!originalTextNodes.has(node) || currentTranslation) originalTextNodes.set(node, node.nodeValue);
      const original = originalTextNodes.get(node);
      const translated = currentTranslation || translateText(original);
      const nextValue = translated ? original.replace(original.trim(), translated) : node.nodeValue;
      if (node.nodeValue !== nextValue) node.nodeValue = nextValue;
    });
  }

  function setLanguage(lang) {
    localStorage.setItem(LANGUAGE_KEY, lang);
    applyLanguage(lang);
    const notice = document.getElementById('langNotice');
    if (notice) notice.classList.add('show');
  }

  function ensureToast() {
    let toast = document.getElementById('appToast') || document.querySelector('.app-toast');
    if (!toast) {
      toast = document.createElement('div');
      toast.id = 'appToast';
      toast.className = 'app-toast';
      document.body.appendChild(toast);
    }
    toast.classList.add('app-toast');
    return toast;
  }

  function showToast(message) {
    const toast = ensureToast();
    toast.textContent = message;
    toast.classList.add('show');
    clearTimeout(showToast.timer);
    showToast.timer = setTimeout(() => toast.classList.remove('show'), 2600);
  }

  const iconPaths = {
    dashboard: '<rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/>',
    books: '<path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M4 4.5A2.5 2.5 0 0 1 6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5z"/>',
    categories: '<path d="M4 4h6v6H4z"/><path d="M14 4h6v6h-6z"/><path d="M4 14h6v6H4z"/><path d="M14 14h6v6h-6z"/>',
    customers: '<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>',
    staff: '<path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>',
    sales: '<circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/><path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h8.85a2 2 0 0 0 2-1.58L21 8H5.12"/>',
    invoices: '<path d="M14 2H6a2 2 0 0 0-2 2v16l4-2 4 2 4-2 4 2V8z"/><path d="M14 2v6h6"/><path d="M8 13h8"/><path d="M8 17h5"/>',
    purchasing: '<path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><path d="M3.3 7 12 12l8.7-5"/><path d="M12 22V12"/>',
    reservations: '<path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>',
    revenue: '<path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/>',
    inventory: '<path d="M3 7h18"/><path d="M5 7l1 13h12l1-13"/><path d="M9 7V4h6v3"/><path d="M9 12h6"/>',
    settings: '<path d="M12 15.5A3.5 3.5 0 1 0 12 8a3.5 3.5 0 0 0 0 7.5z"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06A1.65 1.65 0 0 0 15 19.4a1.65 1.65 0 0 0-1 .6 1.65 1.65 0 0 0-.33 1.82V22a2 2 0 1 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.6 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06A2 2 0 1 1 7.04 4.3l.06.06A1.65 1.65 0 0 0 9 4.6a1.65 1.65 0 0 0 1-.6V4a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1 .51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9c.36.3.87.46 1.51.46H21a2 2 0 1 1 0 4h-.09c-.64 0-1.15.16-1.51.54z"/>'
  };

  const uiIconPaths = {
    revenue: '<path d="M12 1v22"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7H14a3.5 3.5 0 0 1 0 7H6"/>',
    stock: '<path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><path d="M3.3 7 12 12l8.7-5"/><path d="M12 22V12"/>',
    chart: '<path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/>',
    target: '<circle cx="12" cy="12" r="9"/><circle cx="12" cy="12" r="5"/><circle cx="12" cy="12" r="1"/>',
    sun: '<circle cx="12" cy="12" r="4"/><path d="M12 2v2"/><path d="M12 20v2"/><path d="m4.93 4.93 1.41 1.41"/><path d="m17.66 17.66 1.41 1.41"/><path d="M2 12h2"/><path d="M20 12h2"/><path d="m6.34 17.66-1.41 1.41"/><path d="m19.07 4.93-1.41 1.41"/>',
    moon: '<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>',
    appearance: '<path d="M12 3a9 9 0 1 0 9 9 4 4 0 0 1-4 4h-1a4 4 0 0 1-4-4v-1a4 4 0 0 0-4-4 4 4 0 0 1 4-4z"/><circle cx="7.5" cy="10.5" r=".5"/><circle cx="10.5" cy="7.5" r=".5"/><circle cx="14.5" cy="7.5" r=".5"/>',
    music: '<path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/>',
    language: '<path d="m5 8 6 6"/><path d="m4 14 6-6 2-3"/><path d="M2 5h12"/><path d="M7 2h1"/><path d="M22 22l-5-10-5 10"/><path d="M14 18h6"/>',
    bell: '<path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/>',
    user: '<path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>',
    addBook: '<path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M4 4.5A2.5 2.5 0 0 1 6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5z"/><path d="M12 7v6"/><path d="M9 10h6"/>',
    import: '<path d="M12 3v12"/><path d="m7 10 5 5 5-5"/><path d="M4 21h16"/>',
    invoice: '<path d="M14 2H6a2 2 0 0 0-2 2v16l4-2 4 2 4-2 4 2V8z"/><path d="M14 2v6h6"/><path d="M8 13h8"/>',
    report: '<path d="M3 3v18h18"/><path d="M8 17v-5"/><path d="M13 17V7"/><path d="M18 17v-8"/>',
    store: '<path d="M4 10h16"/><path d="M5 10l1-5h12l1 5"/><path d="M6 10v10h12V10"/><path d="M9 20v-6h6v6"/><path d="M8 5V3h8v2"/>'
  };

  function navIconKey(text) {
    const value = (text || '').toLowerCase();
    if (value.includes('tổng quan') || value.includes('dashboard')) return 'dashboard';
    if (value.includes('sách') && !value.includes('giữ')) return 'books';
    if (value.includes('danh mục') || value.includes('categories')) return 'categories';
    if (value.includes('khách') || value.includes('customer')) return 'customers';
    if (value.includes('nhân viên') || value.includes('staff')) return 'staff';
    if (value.includes('bán hàng') || value.includes('sales')) return 'sales';
    if (value.includes('hóa đơn') || value.includes('invoice')) return 'invoices';
    if (value.includes('nhập hàng') || value.includes('purchasing')) return 'purchasing';
    if (value.includes('phiếu giữ') || value.includes('đặt giữ') || value.includes('reservation')) return 'reservations';
    if (value.includes('doanh thu') || value.includes('revenue')) return 'revenue';
    if (value.includes('tồn kho') || value.includes('inventory')) return 'inventory';
    if (value.includes('cài đặt') || value.includes('setting')) return 'settings';
    return '';
  }

  function createNavIcon(key) {
    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('class', 'app-nav-icon');
    svg.setAttribute('viewBox', '0 0 24 24');
    svg.setAttribute('aria-hidden', 'true');
    svg.innerHTML = iconPaths[key] || iconPaths.dashboard;
    return svg;
  }

  function createUiIcon(key) {
    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('class', 'app-ui-icon');
    svg.setAttribute('viewBox', '0 0 24 24');
    svg.setAttribute('aria-hidden', 'true');
    svg.innerHTML = uiIconPaths[key] || uiIconPaths.chart;
    return svg;
  }

  function createLogoIcon() {
    const svg = createUiIcon('store');
    svg.setAttribute('class', 'app-logo-icon');
    return svg;
  }

  function statIconKey(card, index) {
    const text = (card.textContent || '').toLowerCase();
    if (text.includes('tồn kho') || text.includes('stock')) return 'stock';
    if (text.includes('kpi')) return 'target';
    if (text.includes('hóa đơn') || text.includes('invoice')) return 'invoices';
    if (text.includes('doanh thu') || text.includes('revenue')) return 'revenue';
    return ['revenue', 'stock', 'chart', 'revenue', 'target'][index % 5];
  }

  function settingsIconKey(card) {
    const text = (card.textContent || '').toLowerCase();
    if (text.includes('giao diện') || text.includes('appearance')) return 'appearance';
    if (text.includes('âm nhạc') || text.includes('music')) return 'music';
    if (text.includes('ngôn ngữ') || text.includes('language')) return 'language';
    return 'settings';
  }

  function quickIconKey(button) {
    const text = (button.textContent || '').toLowerCase();
    if (text.includes('thêm sách') || text.includes('add book')) return 'addBook';
    if (text.includes('bán hàng') || text.includes('sales')) return 'sales';
    if (text.includes('nhập hàng') || text.includes('import')) return 'import';
    if (text.includes('hóa đơn') || text.includes('invoice')) return 'invoice';
    if (text.includes('báo cáo') || text.includes('report')) return 'report';
    return 'chart';
  }

  function updateRangeSlider(slider) {
    if (!slider) return;
    const min = Number(slider.min || 0);
    const max = Number(slider.max || 100);
    const value = Number(slider.value || 0);
    const pct = max > min ? ((value - min) / (max - min)) * 100 : 0;
    slider.style.setProperty('--range-value', `${Math.max(0, Math.min(100, pct))}%`);
  }

  function enhanceUiIcons() {
    document.querySelectorAll('.logo-mark').forEach(mark => {
      if (!mark.querySelector('.app-logo-icon')) {
        mark.textContent = '';
        mark.appendChild(createLogoIcon());
      }
    });

    document.querySelectorAll('.nav-item').forEach(item => {
      if (item.querySelector('.app-nav-icon')) return;
      const key = navIconKey(item.textContent);
      if (key) item.prepend(createNavIcon(key));
    });

    document.querySelectorAll('.stat-card').forEach((card, index) => {
      const wrap = card.querySelector('.stat-icon-wrap');
      if (wrap && !wrap.querySelector('.app-ui-icon')) {
        wrap.appendChild(createUiIcon(statIconKey(card, index)));
      }
    });

    document.querySelectorAll('.settings-card').forEach(card => {
      const header = card.querySelector('.settings-card-header');
      if (!header || header.querySelector('.settings-card-icon')) return;
      const icon = document.createElement('div');
      icon.className = 'settings-card-icon';
      icon.appendChild(createUiIcon(settingsIconKey(card)));
      header.prepend(icon);
    });

    document.querySelectorAll('.icon-btn').forEach((button, index) => {
      if (button.querySelector('.app-ui-icon')) return;
      button.appendChild(createUiIcon(button.id === 'topbarThemeBtn' ? (isDarkMode() ? 'moon' : 'sun') : (index % 2 ? 'user' : 'bell')));
    });

    document.querySelectorAll('.quick-btn').forEach(button => {
      if (button.querySelector('.quick-icon-wrap')) return;
      const wrap = document.createElement('div');
      wrap.className = 'quick-icon-wrap';
      wrap.appendChild(createUiIcon(quickIconKey(button)));
      button.prepend(wrap);
    });

    document.querySelectorAll('input[type="range"]').forEach(slider => {
      updateRangeSlider(slider);
      if (!slider.dataset.rangeEnhanced) {
        slider.dataset.rangeEnhanced = '1';
        slider.addEventListener('input', () => updateRangeSlider(slider));
      }
    });

    const closeSelectors = [
      '.modal-head .close',
      '.modal-header button[onclick*="close"]',
      '.list-modal-close',
      '#closeCategoryModal',
      '#closeCustomerModal',
      '#closeDetailModal',
      '#modalClose',
      '#closeStaffModal',
      '#closeProfileModal',
      '#btnCloseCreateModal',
      '#btnCloseCustModal',
      '#btnCloseDrawer'
    ];
    document.querySelectorAll(closeSelectors.join(',')).forEach(button => {
      button.classList.add('app-close-x');
      button.setAttribute('aria-label', button.getAttribute('aria-label') || 'Đóng');
      if (button.textContent.trim() !== 'X') button.textContent = 'X';
    });
  }

  function init() {
    applyTheme();
    enhanceUiIcons();
    restoreMusic();
    applyLanguage();
    window.addEventListener('pagehide', saveMusicTime);
    window.addEventListener('beforeunload', saveMusicTime);
    let languageFrame = 0;
    const observer = new MutationObserver(() => {
      if (languageFrame) return;
      languageFrame = requestAnimationFrame(() => {
        languageFrame = 0;
        enhanceUiIcons();
        if (currentLanguage() === 'en') applyLanguage('en');
      });
    });
    observer.observe(document.body, { childList: true, characterData: true, subtree: true });
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
    skipMusicTrack,
    previousMusicTrack,
    seekMusicTrack,
    saveMusicTime,
    applyLanguage,
    setLanguage,
    showToast,
    enhanceUiIcons
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
  window.skipMusicTrack = skipMusicTrack;
  window.previousMusicTrack = previousMusicTrack;
  window.seekMusicTrack = seekMusicTrack;
  window.setLanguage = setLanguage;
  window.showToast = window.showToast || showToast;

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
