/**
 * shared.js — BookHouse shared utilities
 * Include this file in every page to get cross-page dark mode and music.
 *
 * Each page's <head> must also have the FOUC-prevention inline script:
 *   <script>(function(){if(localStorage.getItem('darkMode')==='1')document.documentElement.setAttribute('data-theme','dark');})();</script>
 */
(function () {

  /* ────────────────────────────────────────────
     DARK MODE
  ──────────────────────────────────────────── */

  /**
   * Toggle dark/light mode, save to localStorage, and sync any
   * settings-page checkbox or topbar icon that is present.
   */
  window._toggleDarkMode = function () {
    var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    var next   = !isDark;
    document.documentElement.setAttribute('data-theme', next ? 'dark' : 'light');
    localStorage.setItem('darkMode', next ? '1' : '0');

    // Sync settings-page checkbox (only present on settings.html)
    var cb = document.getElementById('darkModeToggle');
    if (cb) cb.checked = next;

    // Swap topbar icon between sun and moon
    _updateThemeIcon(next);
  };

  function _updateThemeIcon(isDark) {
    var icon = document.getElementById('topbarThemeIcon');
    if (!icon) return;
    if (isDark) {
      icon.setAttribute('viewBox', '0 0 24 24');
      icon.innerHTML = '<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>';
    } else {
      icon.setAttribute('viewBox', '0 0 24 24');
      icon.innerHTML =
        '<circle cx="12" cy="12" r="5"/>' +
        '<line x1="12" y1="1" x2="12" y2="3"/>' +
        '<line x1="12" y1="21" x2="12" y2="23"/>' +
        '<line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/>' +
        '<line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>' +
        '<line x1="1" y1="12" x2="3" y2="12"/>' +
        '<line x1="21" y1="12" x2="23" y2="12"/>' +
        '<line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/>' +
        '<line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>';
    }
  }

  /* ────────────────────────────────────────────
     MUSIC
  ──────────────────────────────────────────── */

  function _initMusic() {
    // settings.html manages its own <audio id="bgAudio"> element.
    // For every other page we inject one here if music is enabled.
    if (document.getElementById('bgAudio')) {
      // settings.html: just expose the existing element globally
      window._bgAudio = document.getElementById('bgAudio');
      return;
    }

    if (localStorage.getItem('musicEnabled') !== '1') return;

    var audio       = document.createElement('audio');
    audio.id        = 'bgAudio';
    audio.loop      = true;
    audio.style.display = 'none';
    audio.innerHTML = '<source src="music/Come My Way.mp3" type="audio/mpeg">';
    audio.volume    = parseFloat(localStorage.getItem('musicVolume') || '0.5');
    document.body.appendChild(audio);
    window._bgAudio = audio;

    audio.play().catch(function () {
      // Autoplay blocked by browser — start on first user click
      document.addEventListener('click', function handler() {
        audio.play();
        document.removeEventListener('click', handler);
      });
    });
  }

  /* ────────────────────────────────────────────
     INIT
  ──────────────────────────────────────────── */

  document.addEventListener('DOMContentLoaded', function () {
    // Sync topbar icon with current theme on every page
    var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    _updateThemeIcon(isDark);

    _initMusic();
  });

})();
