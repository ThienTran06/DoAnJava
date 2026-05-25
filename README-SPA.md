# 📚 BookHouse Admin Dashboard - SPA Refactor

## ✨ Tính năng

✅ **Single Page Application (SPA)** - Không reload toàn trang  
✅ **Sidebar cố định** - Luôn hiển thị  
✅ **Dynamic Content Loading** - Fetch HTML + inject vào content area  
✅ **History Navigation** - Hỗ trợ Back/Forward browser  
✅ **Smooth Transitions** - Animation fade in/out  
✅ **Page Caching** - Tối ưu hiệu năng  

## 📁 Cấu trúc Dự án

```
DoAnJava/
├── index.html              # Layout chính (Sidebar + Topbar + Content)
├── app.js                  # Core navigation engine (SPA)
├── styles.css              # CSS chung
├── pages/
│   ├── dashboard.html      # Trang Tổng quan (charts + stats)
│   ├── inventory.html      # Trang Sách (Books)
│   ├── categories.html     # Trang Thể loại
│   ├── customers.html      # Trang Khách hàng
│   └── staff.html          # Trang Nhân viên
└── ...
```

## 🚀 Cách hoạt động

### 1. **Navigation Flow**
```
User clicks sidebar → app.js intercepts click
                   ↓
                   Check page in config
                   ↓
                   Fetch HTML from pages/
                   ↓
                   Inject vào #mainContent
                   ↓
                   Call init function (e.g., initDashboard)
                   ↓
                   Push state to history
```

### 2. **Page Structure**

Mỗi file page chỉ chứa **content** (không html, head, body):

```html
<!-- pages/dashboard.html -->
<div class="page-shell page-transition active">
  <!-- Nội dung page -->
</div>

<script>
// Init function gọi tự động khi page load
window.initDashboard = function() {
  // Logic khởi tạo (event listeners, render, v.v)
};
</script>
```

### 3. **Page Configuration** (app.js)

```javascript
pages: {
  dashboard: { 
    title: 'Tổng quan', 
    subtitle: '...', 
    file: 'pages/dashboard.html'  // ← fetch file này
  },
  books: { 
    title: 'Sách', 
    subtitle: '...', 
    file: 'pages/inventory.html'
  },
  // ... other pages
}
```

## 🎯 Các tác vụ chính

### Thêm Page Mới

1. **Tạo file HTML** (`pages/newpage.html`):
```html
<div class="page-shell page-transition active">
  <!-- Content -->
</div>
<script>
window.initNewpage = function() {
  // Init logic
};
</script>
```

2. **Cấu hình app.js**:
```javascript
newpage: { 
  title: 'Trang Mới', 
  subtitle: 'Mô tả...', 
  file: 'pages/newpage.html' 
}
```

3. **Thêm nav item** (index.html):
```html
<div class="nav-item" data-page="newpage">
  <svg><!-- Icon --></svg>
  Trang Mới
</div>
```

### Sửa Modal/Form

Tất cả modal được load động khi page load. Chỉnh sửa trực tiếp trong file page:

```html
<!-- pages/inventory.html -->
<div class="modal-overlay" id="modalOverlay">
  <div class="modal">
    <!-- Form -->
  </div>
</div>
<script>
window.initBooks = function() {
  const form = document.getElementById('bookForm');
  // Event listeners...
};
</script>
```

### Global Notification (Toast)

Sử dụng `window.showToast()` từ bất cứ đâu:

```javascript
window.showToast('Đã lưu thành công!');
```

## ⚙️ API & Configuration

### app.navigateTo(pageName, pushHistory)
```javascript
app.navigateTo('dashboard');        // Navigate + push history
app.navigateTo('books', false);     // Navigate without history
```

### Page Cache
```javascript
app.cache['dashboard']  // Cached HTML content
```

### Current Page State
```javascript
app.currentPage         // Current page name
app.elements            // DOM element references
```

## 🔧 Troubleshooting

### Page không load
- Kiểm tra file path trong `app.js` `pages` config
- Mở DevTools Console xem lỗi fetch

### Event listeners mất sau khi chuyển page
- Đảm bảo gọi init function (`window.initPageName`)
- Function init phải được đăng ký trước khi file load

### CSS/Animation không hoạt động
- Import styles.css trong index.html ✓
- Kiểm tra `.page-shell` class có class `active`

## 📊 Performance

- **Caching**: Pages được cache sau lần đầu load
- **Lazy Loading**: Fetch chỉ khi user click
- **Smooth Transitions**: 150ms fade effect
- **No Full Reload**: SPA = speed ⚡

## 🎨 Customization

### Thay đổi Transition Time
```javascript
// app.js, line ~90
await new Promise(r => setTimeout(r, 150));  // Thay đổi giá trị
```

### Custom Page Init
```html
<script>
window.initCustompage = function() {
  console.log('Page initialized!');
  // Your code
};
</script>
```

### Add Global Event Listeners
```javascript
// app.js, init() method
document.addEventListener('your-event', () => { /* ... */ });
```

## 📝 Notes

- Sidebar không reload, luôn cố định ✓
- History button (Back/Forward) hoạt động ✓
- Active menu item tự động cập nhật ✓
- Modal không bị mất khi chuyển page ✓
- Smooth UX giống React/Vue app ✓

---

**Happy coding!** 🚀
