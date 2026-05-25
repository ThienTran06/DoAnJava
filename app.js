/**
 * BookHouse Admin Dashboard - SPA Navigation
 * Quản lý navigation, fetch, inject content, history management
 */

const app = {
  // Configuration pages
  pages: {
    dashboard: { title: 'Tổng quan', subtitle: 'Chào mừng bạn trở lại, Admin!', file: 'pages/dashboard.html' },
    books: { title: 'Sách', subtitle: 'Quản lý toàn bộ đầu sách trong kho', file: 'pages/inventory.html' },
    categories: { title: 'Thể loại', subtitle: 'Quản lý thể loại sách', file: 'pages/categories.html' },
    customers: { title: 'Khách hàng', subtitle: 'Quản lý thông tin khách hàng', file: 'pages/customers.html' },
    staff: { title: 'Nhân viên', subtitle: 'Quản lý nhân sự cửa hàng', file: 'pages/staff.html' },
    // Placeholder pages (coming soon)
    sales: { title: 'Bán hàng', subtitle: 'Giao diện bán hàng trực tiếp', content: 'Trang Bán hàng đang được xây dựng.' },
    invoices: { title: 'Hóa đơn', subtitle: 'Danh sách hóa đơn và thanh toán', content: 'Trang Hóa đơn đang được xây dựng.' },
    imports: { title: 'Nhập hàng', subtitle: 'Quản lý phiếu nhập và hàng về kho', content: 'Trang Nhập hàng đang được xây dựng.' },
    revenue: { title: 'Doanh thu', subtitle: 'Báo cáo doanh thu và phân tích', content: 'Trang Doanh thu đang được xây dựng.' },
    inventory: { title: 'Tồn kho', subtitle: 'Theo dõi trạng thái tồn kho', content: 'Trang Tồn kho đang được xây dựng.' },
    settings: { title: 'Cài đặt', subtitle: 'Thiết lập hệ thống và phân quyền', content: 'Trang Cài đặt đang được xây dựng.' }
  },

  // Cache for loaded pages
  cache: {},

  // Current page state
  currentPage: 'dashboard',

  // DOM elements
  elements: {
    mainContent: null,
    topbarTitle: null,
    topbarSubtitle: null,
    navItems: null
  },

  /**
   * Initialize app
   */
  init() {
    // Get DOM elements
    this.elements.mainContent = document.getElementById('mainContent');
    this.elements.topbarTitle = document.getElementById('topbarTitle');
    this.elements.topbarSubtitle = document.getElementById('topbarSubtitle');

    if (!this.elements.mainContent) {
      console.error('Missing mainContent element');
      return;
    }

    // Setup event listeners
    this.setupNavigation();
    this.setupHistoryListener();

    // Load initial page from URL or default
    const initialPage = this.getPageFromUrl() || 'dashboard';
    this.navigateTo(initialPage, false); // false = don't push to history on init
  },

  /**
   * Setup sidebar navigation
   */
  setupNavigation() {
    const sidebar = document.querySelector('.sidebar');
    if (!sidebar) return;

    sidebar.addEventListener('click', (e) => {
      const navItem = e.target.closest('.nav-item[data-page]');
      if (!navItem) return;
      
      e.preventDefault();
      const pageName = navItem.dataset.page;
      this.navigateTo(pageName);
    });
  },

  /**
   * Handle browser back/forward
   */
  setupHistoryListener() {
    window.addEventListener('popstate', (e) => {
      const page = e.state?.page || 'dashboard';
      this.navigateTo(page, false); // false = don't push again
    });
  },

  /**
   * Get page name from URL hash
   */
  getPageFromUrl() {
    const hash = window.location.hash.slice(1);
    return hash && this.pages[hash] ? hash : null;
  },

  /**
   * Navigate to page
   */
  async navigateTo(pageName, pushHistory = true) {
    // Validate page
    if (!this.pages[pageName]) {
      console.warn(`Page "${pageName}" not found`);
      return;
    }

    // Clean up old event listeners and modals
    this.cleanupPage();

    // Update current page
    this.currentPage = pageName;
    const pageConfig = this.pages[pageName];

    // Update sidebar active state
    this.updateActiveNavItem(pageName);

    // Update topbar
    this.elements.topbarTitle.textContent = pageConfig.title;
    this.elements.topbarSubtitle.textContent = pageConfig.subtitle;

    // Add fade out animation
    this.elements.mainContent.style.opacity = '0';

    // Wait for animation
    await new Promise(r => setTimeout(r, 150));

    // Load content
    if (pageConfig.file) {
      // Fetch from file
      await this.loadPageFromFile(pageName, pageConfig.file);
    } else {
      // Placeholder content
      this.elements.mainContent.innerHTML = `
        <div class="page-shell active">
          <div class="empty-state">${pageConfig.content}</div>
        </div>
      `;
    }

    // Fade in animation
    this.elements.mainContent.style.opacity = '1';

    // Push to history
    if (pushHistory) {
      window.history.pushState({ page: pageName }, pageConfig.title, `#${pageName}`);
    }

    // Re-initialize page-specific scripts after content is loaded
    // Use requestAnimationFrame to ensure DOM is fully rendered
    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        this.initPageScripts(pageName);
      });
    });
  },

  /**
   * Clean up old page (remove modals, detach listeners)
   */
  cleanupPage() {
    // Remove old modal overlays to avoid duplicates
    const oldModals = this.elements.mainContent.querySelectorAll('.modal-overlay');
    oldModals.forEach(modal => modal.remove());
  },

  /**
   * Load page from file
   */
  async loadPageFromFile(pageName, filePath) {
    try {
      // Check cache
      if (this.cache[pageName]) {
        this.elements.mainContent.innerHTML = this.cache[pageName];
        return;
      }

      // Fetch file
      const response = await fetch(filePath);
      if (!response.ok) throw new Error(`HTTP ${response.status}`);

      const html = await response.text();

      // Cache
      this.cache[pageName] = html;

      // Inject into DOM
      this.elements.mainContent.innerHTML = html;

      // Find and activate page-shell
      const pageShell = this.elements.mainContent.querySelector('.page-shell');
      if (pageShell) {
        pageShell.classList.add('active');
      }

      // Extract and execute any scripts in the loaded content
      const scripts = this.elements.mainContent.querySelectorAll('script');
      scripts.forEach(script => {
        const newScript = document.createElement('script');
        newScript.textContent = script.textContent;
        script.parentNode.replaceChild(newScript, script);
      });
    } catch (err) {
      console.error(`Error loading page "${pageName}":`, err);
      this.elements.mainContent.innerHTML = `
        <div class="page-shell active">
          <div class="empty-state">❌ Lỗi tải trang. Vui lòng thử lại.</div>
        </div>
      `;
    }
  },

  /**
   * Initialize page-specific scripts
   */
  initPageScripts(pageName) {
    // Remove old transition class
    const oldShell = this.elements.mainContent.querySelector('.page-shell');
    if (oldShell) {
      oldShell.classList.remove('page-transition');
    }

    // Call page init function if exists
    if (typeof window[`init${this.capitalize(pageName)}`] === 'function') {
      window[`init${this.capitalize(pageName)}`]();
    }
  },

  /**
   * Update active nav item
   */
  updateActiveNavItem(pageName) {
    document.querySelectorAll('.nav-item[data-page]').forEach(item => {
      item.classList.toggle('active', item.dataset.page === pageName);
    });
  },

  /**
   * Utility: capitalize string
   */
  capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }
};

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', () => app.init());
