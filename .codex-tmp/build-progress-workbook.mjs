import fs from "node:fs/promises";
import path from "node:path";
import { FileBlob, SpreadsheetFile } from "@oai/artifact-tool";

const inputPath = "C:\\Users\\dangt\\Downloads\\tiendo_quanly_nhasach.xlsx";
const backupPath = "C:\\Users\\dangt\\Downloads\\tiendo_quanly_nhasach_backup_truoc_cap_nhat.xlsx";
const outputPath = "C:\\Users\\dangt\\Downloads\\tiendo_quanly_nhasach_capnhat.xlsx";

const input = await FileBlob.load(inputPath);
const workbook = await SpreadsheetFile.importXlsx(input);

try {
  await fs.copyFile(inputPath, backupPath);
} catch {
  // Backup is helpful but not required for the generated deliverable.
}

const reportDate = "01/06/2026";

const overviewRows = [
  ["Mục", "Giá trị", "Ghi chú"],
  ["Tên project", "DoAnJava - Quản lý nhà sách", "Spring Boot + giao diện HTML/CSS/JS"],
  ["Ngày cập nhật báo cáo", reportDate, "Dựa trên mã nguồn trong workspace"],
  ["Backend", "Đã có", "Controller, Service, Repository, Entity, DTO, Security, Scheduler"],
  ["Frontend", "Đã có", "Dashboard, đăng nhập, quản lý sách, hóa đơn, nhập hàng, phiếu giữ, khách hàng, nhân viên"],
  ["CSDL", "Đã cấu hình", "JPA/Hibernate, MySQL profile dev/prod"],
  ["Xác thực & phân quyền", "Đã có", "JWT, refresh token, quyền theo chức năng"],
  ["Báo cáo/KPI", "Đã có API", "Doanh thu, tồn kho, sách bán chạy, KPI ngày"],
  ["Tài liệu tiến độ", "Đã bổ sung", "Xem 2 sheet danh sách chi tiết bên cạnh"],
];

const doneRows = [
  ["Nhóm chức năng", "Hạng mục đã có", "Bằng chứng trong project", "Trạng thái", "Ghi chú báo cáo"],
  ["Nền tảng backend", "Ứng dụng Spring Boot Java 17", "pom.xml, LibraryManagerApplication.java", "Đã có", "Có Maven wrapper và cấu hình dependency"],
  ["Cấu hình môi trường", "Profile dev/prod, JPA, CORS, static resources", "application.properties, application-dev.properties, application-prod.properties", "Đã có", "Dùng MySQL và cấu hình deploy linh hoạt"],
  ["Bảo mật", "JWT filter, SecurityConfig, BCrypt, CORS", "config/JwtUtil.java, JwtFilter.java, SecurityConfig.java, AppConfig.java", "Đã có", "Có token, refresh token, logout"],
  ["Đăng nhập", "Login, refresh token, logout, forgot password", "AuthController.java, AuthServiceImpl.java, login.html", "Đã có", "Có UI đăng nhập và thống kê public"],
  ["Phân quyền", "Quản lý người dùng, nhóm quyền, chức năng, cập nhật quyền", "NguoiDungController.java, PhanQuyen.java, ChucNang.java", "Đã có", "Dùng @PreAuthorize theo authority"],
  ["Quản lý sách", "CRUD sách, tìm kiếm, phân trang, tồn kho, upload ảnh", "SachController.java, SachServiceImpl.java, pages/inventory.html", "Đã có", "Có Cloudinary và thống kê sách"],
  ["Danh mục sách", "CRUD thể loại, tác giả, nhà xuất bản", "TheLoaiController.java, TacGiaController.java, NhaXuatBanController.java", "Đã có", "Có frontend trang categories và inventory"],
  ["Nhà cung cấp", "CRUD và phân trang/tìm kiếm nhà cung cấp", "NhaCungCapController.java, NhaCungCapServiceImpl.java", "Đã có", "Dùng cho nghiệp vụ nhập hàng"],
  ["Khách hàng", "CRUD khách hàng, avatar, tìm kiếm phân trang, hạng thành viên", "KhachHangController.java, KhachHangServiceImpl.java, pages/customers.html", "Đã có", "Có điểm tích lũy và giảm giá"],
  ["Hóa đơn bán hàng", "Tạo/cập nhật/hủy hóa đơn, chi tiết hóa đơn, thống kê", "HoaDonController.java, ChiTietHoaDonController.java, HoaDon.html", "Đã có", "Có cập nhật tồn kho và báo cáo doanh thu"],
  ["Phiếu nhập", "Tạo/sửa chi tiết/hủy/xóa phiếu nhập, thống kê nhập hàng", "PhieuNhapController.java, PhieuNhapServiceImpl.java, NhapHang.html", "Đã có", "Có xử lý tăng/hoàn tồn kho khi nhập/hủy"],
  ["Phiếu giữ sách", "Tạo phiếu giữ, thêm/xóa sách, xác nhận, hủy, hết hạn", "PhieuGiuSachController.java, ChiTietPhieuGiuController.java, PhieuGiu.html", "Đã có", "Có trạng thái phiếu giữ và tác vụ tự hết hạn"],
  ["Báo cáo doanh thu", "Doanh thu ngày/tháng/năm, theo thể loại, 7 ngày, 30 ngày, khoảng ngày", "BaoCaoController.java, HoaDonRepository.java", "Đã có", "Phù hợp đưa vào dashboard báo cáo"],
  ["Báo cáo tồn kho", "Tồn kho nhiều/ít, tổng tồn kho, sách doanh thu cao", "SachRepository.java, BaoCaoController.java", "Đã có", "Hỗ trợ quản trị nhập hàng và bán hàng"],
  ["KPI ngày", "Xem và đặt KPI doanh thu trong ngày", "KpiNgayController.java, KpiNgayServiceImpl.java", "Đã có", "Có thể dùng để đo tiến độ kinh doanh"],
  ["Scheduler", "Tự hết hạn phiếu giữ, giảm điểm khách hàng hàng tháng", "PhieuGiuScheduler.java, KhachHangDiemScheduler.java", "Đã có", "Có xử lý tự động theo thời gian"],
  ["Upload ảnh", "Upload ảnh sách/avatar qua Cloudinary", "CloudinaryConfig.java, CloudinaryServiceImpl.java", "Đã có", "Có endpoint upload và xử lý MultipartFile"],
  ["Frontend dashboard", "Trang tổng quan có thống kê, điều hướng, báo cáo", "index.html, styles.css, ui-polish.css", "Đã có", "Có giao diện quản trị"],
  ["Frontend nghiệp vụ", "Trang bán hàng, hóa đơn, nhập hàng, phiếu giữ, khách hàng, nhân viên, cài đặt", "BanHang.html, HoaDon.html, NhapHang.html, PhieuGiu.html, pages/*.html, settings.html", "Đã có", "Có gọi API bằng fetch/localStorage token"],
  ["Tiện ích UI", "Dark mode, ngôn ngữ, nhạc nền, toast, icon UI", "js/app-settings.js, music/*.mp3", "Đã có", "Có trải nghiệm người dùng khá phong phú"],
  ["API docs", "Swagger/OpenAPI", "SwaggerConfig.java, springdoc dependency", "Đã có", "Có thể dùng để demo API"],
  ["Xử lý lỗi", "Global exception handler và AuthException", "GlobalExceptionHandler.java, AuthException.java", "Đã có", "Giúp trả lỗi rõ ràng hơn"],
  ["Kiểm thử", "Test khởi tạo context mặc định", "LibraryManagerApplicationTests.java", "Cơ bản", "Cần bổ sung test nghiệp vụ/API"],
];

const todoRows = [
  ["Ưu tiên", "Việc có thể làm tiếp", "Mục tiêu", "Gợi ý cách báo cáo tiến độ", "Trạng thái đề xuất"],
  ["Cao", "Kiểm thử luồng đăng nhập và phân quyền", "Đảm bảo từng role chỉ vào đúng chức năng", "Tạo bảng test case: admin/nhân viên/kế toán/kho", "Nên làm"],
  ["Cao", "Bổ sung unit/integration test cho nghiệp vụ chính", "Giảm lỗi khi demo hoặc sửa code", "Test sách, hóa đơn, nhập hàng, phiếu giữ, khách hàng", "Nên làm"],
  ["Cao", "Kiểm tra và ẩn thông tin nhạy cảm trong cấu hình", "Tránh lộ secret/API key khi nộp hoặc public repo", "Đưa Cloudinary/JWT/database secret vào biến môi trường", "Nên làm"],
  ["Cao", "Chuẩn hóa dữ liệu mẫu demo", "Có dữ liệu đẹp khi thuyết trình", "Tạo dữ liệu sách, khách hàng, hóa đơn, nhập hàng, người dùng mẫu", "Nên làm"],
  ["Cao", "Kiểm thử end-to-end các màn hình frontend", "Đảm bảo click, thêm, sửa, xóa chạy ổn", "Demo theo checklist từng trang", "Nên làm"],
  ["Trung bình", "Hoàn thiện báo cáo dashboard", "Hiển thị biểu đồ rõ hơn cho doanh thu/tồn kho/KPI", "Thêm biểu đồ top sách, doanh thu 7/30 ngày, KPI hôm nay", "Có thể làm"],
  ["Trung bình", "Xuất Excel/PDF cho hóa đơn và báo cáo", "Tăng tính thực tế của hệ thống", "Thêm nút xuất hóa đơn, xuất báo cáo tháng", "Có thể làm"],
  ["Trung bình", "Tối ưu tìm kiếm/lọc/phân trang", "Dễ dùng khi dữ liệu lớn", "Bổ sung filter nâng cao cho sách, hóa đơn, nhập hàng", "Có thể làm"],
  ["Trung bình", "Validation frontend đồng bộ backend", "Giảm nhập sai dữ liệu", "Hiện thông báo lỗi từng trường, giới hạn số lượng/giá/ngày", "Có thể làm"],
  ["Trung bình", "Chuẩn hóa REST API response", "Frontend xử lý lỗi và thành công thống nhất", "Dùng ResponseEntity/DTO chung cho success/error", "Có thể làm"],
  ["Trung bình", "Bổ sung audit log", "Biết ai đã tạo/sửa/xóa dữ liệu", "Lưu người thao tác, thời gian, hành động cho hóa đơn/nhập hàng", "Có thể làm"],
  ["Trung bình", "Tối ưu giao diện mobile/tablet", "Demo mượt trên nhiều màn hình", "Kiểm tra responsive cho dashboard, bảng dữ liệu, modal", "Có thể làm"],
  ["Trung bình", "Tách cấu hình API base URL rõ ràng", "Dễ deploy local/Railway/Vercel", "Một file cấu hình chung cho frontend", "Có thể làm"],
  ["Thấp", "Thêm reset mật khẩu thực tế qua email", "Hoàn thiện quên mật khẩu", "Tích hợp SMTP hoặc mail service", "Mở rộng"],
  ["Thấp", "Thêm barcode/QR cho sách hoặc hóa đơn", "Tăng tính chuyên nghiệp khi bán hàng", "Quét mã sách ở trang bán hàng", "Mở rộng"],
  ["Thấp", "Thêm quản lý khuyến mãi/voucher", "Tăng tính thực tế thương mại", "Áp dụng giảm giá theo khách hàng/đơn hàng/sách", "Mở rộng"],
  ["Thấp", "Thêm cảnh báo tồn kho thấp", "Hỗ trợ nhập hàng kịp thời", "Thông báo khi sách dưới ngưỡng tồn kho", "Mở rộng"],
  ["Thấp", "Tài liệu hướng dẫn cài đặt và sử dụng", "Dễ chấm bài và bàn giao", "README gồm setup DB, run backend, tài khoản demo, ảnh màn hình", "Mở rộng"],
  ["Thấp", "Sơ đồ CSDL và sơ đồ luồng nghiệp vụ", "Báo cáo rõ ràng hơn", "Vẽ ERD và flow bán hàng/nhập hàng/giữ sách", "Mở rộng"],
];

function getOrResetSheet(name) {
  const sheet = workbook.worksheets.getOrAdd(name);
  sheet.deleteAllDrawings();
  sheet.showGridLines = false;
  sheet.getRange("A1:H200").clear({ applyTo: "all" });
  return sheet;
}

function styleSheet(sheet, rows, title, subtitle, widths) {
  sheet.getRange("A1:E1").merge();
  sheet.getRange("A1").values = [[title]];
  sheet.getRange("A2:E2").merge();
  sheet.getRange("A2").values = [[subtitle]];
  sheet.getRange("A1:E2").format = {
    fill: "#F5F7FA",
    font: { bold: true, color: "#1F2937" },
    wrapText: true,
  };
  sheet.getRange("A1").format.font = { bold: true, color: "#0F172A", size: 16 };
  sheet.getRange("A2").format.font = { bold: false, color: "#475569", size: 10 };

  const startRow = 4;
  const colCount = rows[0].length;
  const endCol = String.fromCharCode("A".charCodeAt(0) + colCount - 1);
  sheet.getRange(`A${startRow}:${endCol}${startRow + rows.length - 1}`).values = rows;

  const header = sheet.getRange(`A${startRow}:${endCol}${startRow}`);
  header.format = {
    fill: "#1F4E79",
    font: { bold: true, color: "#FFFFFF" },
    wrapText: true,
  };
  const body = sheet.getRange(`A${startRow + 1}:${endCol}${startRow + rows.length - 1}`);
  body.format = {
    wrapText: true,
    verticalAlignment: "top",
  };
  sheet.getRange(`A${startRow}:${endCol}${startRow + rows.length - 1}`).format.borders = {
    insideHorizontal: { style: "Continuous", color: "#CBD5E1" },
    insideVertical: { style: "Continuous", color: "#CBD5E1" },
    edgeTop: { style: "Continuous", color: "#94A3B8" },
    edgeBottom: { style: "Continuous", color: "#94A3B8" },
    edgeLeft: { style: "Continuous", color: "#94A3B8" },
    edgeRight: { style: "Continuous", color: "#94A3B8" },
  };
  sheet.freezePanes.freezeRows(startRow);
  widths.forEach((width, idx) => {
    const col = String.fromCharCode("A".charCodeAt(0) + idx);
    sheet.getRange(`${col}:${col}`).format.columnWidthPx = width;
  });
  sheet.getRange(`A1:${endCol}2`).format.rowHeightPx = 28;
  sheet.getRange(`A${startRow}:${endCol}${startRow}`).format.rowHeightPx = 32;
}

const overview = getOrResetSheet("Tong quan");
styleSheet(
  overview,
  overviewRows,
  "Tổng quan tiến độ project quản lý nhà sách",
  "Tóm tắt nhanh các phần đã có trong mã nguồn và định hướng hoàn thiện để báo cáo tiến độ.",
  [190, 220, 380],
);

const done = getOrResetSheet("Da co trong project");
styleSheet(
  done,
  doneRows,
  "Danh sách những thứ đã có trong project",
  "Các hạng mục được tổng hợp từ cấu trúc source code, controller, service, entity, repository và giao diện hiện có.",
  [170, 270, 330, 95, 350],
);

const todo = getOrResetSheet("Co the lam tiep");
styleSheet(
  todo,
  todoRows,
  "Danh sách việc có thể làm tiếp",
  "Các việc gợi ý để bổ sung vào báo cáo tiến độ, ưu tiên theo mức cần thiết cho demo và hoàn thiện đồ án.",
  [95, 280, 290, 360, 130],
);

const errors = await workbook.inspect({
  kind: "match",
  searchTerm: "#REF!|#DIV/0!|#VALUE!|#NAME\\?|#N/A",
  options: { useRegex: true, maxResults: 100 },
  summary: "formula error scan",
});
console.log(errors.ndjson);

for (const sheetName of ["Tong quan", "Da co trong project", "Co the lam tiep"]) {
  const preview = await workbook.render({ sheetName, autoCrop: "all", scale: 1, format: "png" });
  const previewPath = path.join("C:\\Users\\dangt\\IdeaProjects\\DoAnJava\\.codex-tmp", `${sheetName}.png`);
  await fs.writeFile(previewPath, new Uint8Array(await preview.arrayBuffer()));
  console.log(`rendered ${sheetName}: ${previewPath}`);
}

const output = await SpreadsheetFile.exportXlsx(workbook);
await output.save(outputPath);
console.log(`saved ${outputPath}`);
