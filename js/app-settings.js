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
    html[data-theme="dark"] .stat-card {
      background: var(--surface);
      border-color: var(--border);
      color: var(--text);
    }
    html[data-theme="dark"] .topbar *,
    html[data-theme="dark"] .table-card *,
    html[data-theme="dark"] .card *,
    html[data-theme="dark"] .chart-card *,
    html[data-theme="dark"] .settings-card *,
    html[data-theme="dark"] .modal *,
    html[data-theme="dark"] .drawer *,
    html[data-theme="dark"] .summary-card *,
    html[data-theme="dark"] .empty-state * {
      border-color: var(--border);
    }
    html[data-theme="dark"] .logo-mark,
    html[data-theme="dark"] .avatar {
      background: #f3f4f0;
      color: #1f1f1f;
    }
    html[data-theme="dark"] .nav-item.active {
      background: #f3f4f0 !important;
      color: #1f1f1f !important;
    }
    html[data-theme="dark"] .nav-item.active svg {
      stroke: #1f1f1f !important;
    }
    html[data-theme="dark"] .nav-item.active .nav-badge {
      background: rgba(31,31,31,0.12);
      color: #1f1f1f;
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
    html[data-theme="dark"] .hint,
    html[data-theme="dark"] small {
      color: var(--muted) !important;
    }
    html[data-theme="dark"] .stat-value,
    html[data-theme="dark"] .page-title,
    html[data-theme="dark"] .drawer-title,
    html[data-theme="dark"] .summary-value,
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
      background: #f3f4f0 !important;
      color: #1f1f1f !important;
      border-color: #f3f4f0 !important;
    }
    html[data-theme="dark"] .btn.ghost,
    html[data-theme="dark"] button.btn.ghost,
    html[data-theme="dark"] .btn,
    html[data-theme="dark"] .btn-icon,
    html[data-theme="dark"] .btn-danger-ghost,
    html[data-theme="dark"] .icon-btn,
    html[data-theme="dark"] .search-box,
    html[data-theme="dark"] .lang-btn,
    html[data-theme="dark"] .quick-btn {
      background: #1f1f1f;
      color: var(--text);
      border-color: var(--border);
    }
    html[data-theme="dark"] .btn svg,
    html[data-theme="dark"] .btn-icon svg,
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
    html[data-theme="dark"] .suggestion-item:hover {
      background: #2a2a2a;
    }
    html[data-theme="dark"] .tbl-books tr.customer-tier-row td {
      background: linear-gradient(90deg, rgba(255,255,255,0.035), transparent 70%);
    }
    html[data-theme="dark"] .customer-tier-bronze { --tier-bg: rgba(183,138,92,.08); }
    html[data-theme="dark"] .customer-tier-silver { --tier-bg: rgba(145,160,173,.08); }
    html[data-theme="dark"] .customer-tier-gold { --tier-bg: rgba(214,169,0,.12); }
    html[data-theme="dark"] .customer-tier-platinum { --tier-bg: rgba(86,183,207,.10); }
    html[data-theme="dark"] .tbl-books tr.customer-tier-vip td {
      background-image:
        linear-gradient(110deg, transparent 0%, rgba(255,255,255,.16) 18%, transparent 34%),
        linear-gradient(90deg, rgba(255,215,92,.11), rgba(86,183,207,.09) 55%, transparent) !important;
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
    html[data-theme="dark"] .modal-overlay {
      background: rgba(0,0,0,.62);
    }
    html[data-theme="dark"] [style*="color:#595959"],
    html[data-theme="dark"] [style*="color: #595959"],
    html[data-theme="dark"] [style*="color:#6b7280"],
    html[data-theme="dark"] [style*="color: #6b7280"],
    html[data-theme="dark"] [style*="color:#1f1f1f"],
    html[data-theme="dark"] [style*="color: #1f1f1f"],
    html[data-theme="dark"] [style*="color: black"],
    html[data-theme="dark"] [style*="color:black"] {
      color: var(--text) !important;
    }
    html[data-theme="dark"] [style*="background:#fff"],
    html[data-theme="dark"] [style*="background: #fff"],
    html[data-theme="dark"] [style*="background:white"],
    html[data-theme="dark"] [style*="background: white"] {
      background: var(--surface) !important;
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

  const translationPairs = [
    ['Tổng quan', 'Dashboard'], ['Sách', 'Books'], ['Danh mục', 'Categories'],
    ['Khách Hàng', 'Customers'], ['Khách hàng', 'Customers'], ['Nhân viên', 'Staff'],
    ['Nghiệp vụ', 'Operations'], ['Bán hàng', 'Sales'], ['Hóa đơn', 'Invoices'],
    ['Nhập hàng', 'Purchasing'], ['Phiếu giữ', 'Reservations'], ['Báo cáo', 'Reports'],
    ['Doanh thu', 'Revenue'], ['Tồn kho', 'Inventory'], ['Hệ thống', 'System'],
    ['Cài đặt', 'Settings'], ['Đăng xuất', 'Logout'], ['Quản lý', 'Management'],
    ['Tìm kiếm sách, hóa đơn, khách hàng...', 'Search books, invoices, customers...'],
    ['Tìm kiếm sách, hóa đơn, khách hàng…', 'Search books, invoices, customers...'],
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
    ['Đang tải', 'Loading'], ['Đang tải...', 'Loading...'], ['Chưa gọi API', 'API not called'],
    ['Chưa đặt KPI', 'KPI not set'], ['Đã đạt KPI', 'KPI reached'], ['Chưa đạt KPI', 'KPI not reached'],
    ['7 ngày qua', 'Last 7 days'], ['30 ngày qua', 'Last 30 days'],
    ['Xem tất cả', 'View all'], ['Xem tất cả ›', 'View all ›'],
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
    ['Hóa đơn trong ngày', 'Invoices today'], ['Tổng hóa đơn', 'Total invoices'],
    ['Doanh thu trong ngày', 'Revenue today'], ['Tổng doanh thu', 'Total revenue'],
    ['Tìm ID hóa đơn (số)...', 'Search invoice ID (number)...'],
    ['Mã HĐ', 'Invoice code'], ['Ngày bán', 'Sale date'], ['Tổng tiền', 'Total amount'],
    ['Khách hàng:', 'Customer:'], ['Mã KH:', 'Customer ID:'], ['Nhân viên lập:', 'Created by:'],
    ['Mã NV:', 'Staff ID:'], ['Ngày bán:', 'Sale date:'], ['Trạng thái:', 'Status:'],
    ['Không tìm thấy hóa đơn tương thích', 'No matching invoices found'],
    ['Hủy hóa đơn', 'Cancel invoice'], ['Hoàn thành', 'Completed'], ['Đã hủy', 'Canceled'],
    ['Hóa đơn đã đóng', 'Invoice closed'],
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
    ['Tìm mã phiếu giữ (ID)...', 'Search reservation ID...'], ['Ngày lập', 'Created date'],
    ['Số lượng sách', 'Book quantity'], ['Lập phiếu giữ sách mới', 'Create new book reservation'],
    ['Nhập họ tên hoặc số điện thoại khách hàng...', 'Enter customer name or phone...'],
    ['Tìm tên sách nhanh...', 'Quickly search book title...'], ['Mã khách hàng:', 'Customer ID:'],
    ['Trạng thái phiếu:', 'Reservation status:'], ['Ngày lập phiếu:', 'Created date:'],
    ['Hạn lấy sách:', 'Pickup deadline:'], ['Không tìm thấy phiếu đặt giữ sách nào thích hợp', 'No matching reservations found'],
    ['Chưa có sản phẩm trong giỏ hàng giữ sách', 'No products in reservation cart'],
    ['Chờ lấy', 'Waiting for pickup'], ['Xác nhận lấy sách', 'Confirm pickup'],
    ['Hủy phiếu giữ', 'Cancel reservation'], ['Phiếu đã đóng quy trình', 'Reservation closed'],
    ['Giao diện', 'Appearance'], ['Chế độ tối', 'Dark mode'],
    ['Chọn màu nền và chế độ hiển thị', 'Choose colors and display mode'],
    ['Chuyển giao diện sang nền đen, giảm mỏi mắt ban đêm', 'Use a dark interface to reduce eye strain at night'],
    ['Âm nhạc nền', 'Background music'], ['Nhạc nền nhẹ khi sử dụng hệ thống', 'Light background music while using the system'],
    ['Bật nhạc nền', 'Enable background music'], ['Phát nhạc nền khi bạn đang làm việc', 'Play background music while you work'],
    ['Âm lượng', 'Volume'], ['Chọn ngôn ngữ hiển thị của hệ thống', 'Choose the display language'],
    ['Tải lại trang để áp dụng ngôn ngữ mới', 'Reload the page to apply the new language'],
    ['Chuyển chế độ sáng/tối', 'Toggle light/dark mode']
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
    map[normalizeText(pair[0])] = pair[1];
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
      audio.preload = 'metadata';
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
    const hasMusicControls = document.getElementById('musicToggle') || document.getElementById('volumeSlider');
    if (!enabled && !hasMusicControls) return;
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
    const exact = translations[trimmed] || normalizedTranslations[normalizeText(trimmed)];
    if (exact) return exact;

    for (const pair of translationPairs) {
      const vi = pair[0];
      const en = pair[1];
      if (trimmed.startsWith(vi + ':')) return trimmed.replace(vi, en);
      if (trimmed.startsWith(vi + ' ')) return trimmed.replace(vi, en);
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

  function init() {
    applyTheme();
    restoreMusic();
    applyLanguage();
    window.addEventListener('pagehide', saveMusicTime);
    window.addEventListener('beforeunload', saveMusicTime);
    let languageFrame = 0;
    const observer = new MutationObserver(() => {
      if (currentLanguage() !== 'en' || languageFrame) return;
      languageFrame = requestAnimationFrame(() => {
        languageFrame = 0;
        applyLanguage('en');
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
