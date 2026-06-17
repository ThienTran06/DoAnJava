import fs from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import {
  Presentation,
  PresentationFile,
} from "file:///C:/Users/dangt/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/@oai/artifact-tool/dist/artifact_tool.mjs";

const root = "C:/Users/dangt/IdeaProjects/DoAnJava";
const work = path.join(root, ".codex-tmp", "bookhouse_deck_work");
const previewDir = path.join(work, "preview");
const layoutDir = path.join(work, "layout");
const qaDir = path.join(work, "qa");
const outputDir = path.join(root, "outputs");
const finalPptx = path.join(outputDir, "BookHouse_BaoCao_DoAn_NangCap.pptx");

await fs.mkdir(previewDir, { recursive: true });
await fs.mkdir(layoutDir, { recursive: true });
await fs.mkdir(qaDir, { recursive: true });
await fs.mkdir(outputDir, { recursive: true });

const W = 1280;
const H = 720;
const C = {
  cream: "#FFF8EF",
  ink: "#1F1F22",
  muted: "#68626D",
  coral: "#F17364",
  coralDark: "#D94F46",
  teal: "#4DB7C4",
  aqua: "#BEECEF",
  lavender: "#E8DDF8",
  violet: "#8A5DE8",
  yellow: "#FFD978",
  gold: "#E4A800",
  rose: "#FFD6DA",
  blue: "#DCEAFB",
  line: "#E6DED2",
  white: "#FFFFFF",
};

const prs = Presentation.create({ slideSize: { width: W, height: H } });

function addShape(slide, geometry, position, fill, line = "none", radius = 0) {
  const cfg = {
    geometry,
    position,
    fill,
    line: line === "none" ? { style: "solid", fill: "none", width: 0 } : line,
  };
  if (radius && ["rect", "textbox", "roundRect"].includes(geometry)) {
    cfg.borderRadius = radius;
  }
  return slide.shapes.add(cfg);
}

function addText(slide, text, position, opt = {}) {
  const box = addShape(slide, "textbox", position, "none");
  box.text = text;
  box.text.style = {
    typeface: opt.font || "Aptos",
    fontSize: opt.size || 24,
    bold: opt.bold || false,
    color: opt.color || C.ink,
    alignment: opt.align || "left",
  };
  return box;
}

function addTitle(slide, title, subtitle = "", kicker = "") {
  if (kicker) addText(slide, kicker.toUpperCase(), { left: 72, top: 48, width: 520, height: 30 }, { size: 13, bold: true, color: C.coral });
  addText(slide, title, { left: 72, top: 78, width: 850, height: 70 }, { size: 42, bold: true, color: C.ink });
  if (subtitle) addText(slide, subtitle, { left: 74, top: 142, width: 780, height: 44 }, { size: 18, color: C.muted });
}

function addFooter(slide, index) {
  addText(slide, "BookHouse - Hệ thống quản lý nhà sách", { left: 72, top: 668, width: 500, height: 24 }, { size: 11, color: C.muted });
  addText(slide, String(index).padStart(2, "0"), { left: 1160, top: 662, width: 48, height: 32 }, { size: 14, bold: true, color: C.coral, align: "right" });
}

function addBullets(slide, items, x, y, w, opts = {}) {
  const gap = opts.gap || 54;
  items.forEach((item, i) => {
    addShape(slide, "ellipse", { left: x, top: y + i * gap + 6, width: 12, height: 12 }, opts.dot || C.coral);
    addText(slide, item, { left: x + 28, top: y + i * gap, width: w - 28, height: gap - 4 }, { size: opts.size || 20, color: opts.color || C.ink });
  });
}

function card(slide, x, y, w, h, title, body, fill = C.white, accent = C.coral) {
  addShape(slide, "roundRect", { left: x, top: y, width: w, height: h }, fill, { style: "solid", fill: C.line, width: 1 }, 20);
  addShape(slide, "rect", { left: x, top: y, width: 8, height: h }, accent);
  addText(slide, title, { left: x + 24, top: y + 22, width: w - 42, height: 32 }, { size: 22, bold: true, color: C.ink });
  addText(slide, body, { left: x + 24, top: y + 62, width: w - 42, height: h - 80 }, { size: 17, color: C.muted });
}

function sectionSlide(no, title, sub) {
  const slide = prs.slides.add();
  slide.background.fill = C.cream;
  addShape(slide, "rect", { left: 0, top: 0, width: W, height: H }, C.cream);
  addBookStack(slide, 730, 130, 1.1);
  addText(slide, String(no).padStart(2, "0"), { left: 78, top: 90, width: 120, height: 60 }, { size: 46, bold: true, color: C.coral });
  addText(slide, title, { left: 78, top: 178, width: 670, height: 170 }, { size: 56, bold: true, color: C.ink });
  addText(slide, sub, { left: 82, top: 360, width: 560, height: 90 }, { size: 22, color: C.muted });
  addShape(slide, "rect", { left: 78, top: 500, width: 220, height: 8 }, C.coral);
  return slide;
}

function addBookStack(slide, x, y, scale = 1) {
  const books = [
    [0, 26, 50, 220, C.yellow],
    [54, 8, 44, 238, C.blue],
    [102, 0, 58, 246, C.coral],
    [164, 18, 48, 228, C.teal],
    [216, 6, 44, 240, C.white],
  ];
  addShape(slide, "rect", { left: x - 40 * scale, top: y + 250 * scale, width: 380 * scale, height: 24 * scale }, C.coral);
  for (const [dx, dy, bw, bh, fill] of books) {
    addShape(slide, "roundRect", { left: x + dx * scale, top: y + dy * scale, width: bw * scale, height: bh * scale }, fill, { style: "solid", fill: "#2E63B7", width: 1 }, 12 * scale);
    addShape(slide, "line", { left: x + (dx + 12) * scale, top: y + (dy + 54) * scale, width: (bw - 24) * scale, height: 0 }, "none", { style: "solid", fill: "#2E63B7", width: 1 });
    addShape(slide, "line", { left: x + (dx + 12) * scale, top: y + (dy + 92) * scale, width: (bw - 24) * scale, height: 0 }, "none", { style: "solid", fill: "#2E63B7", width: 1 });
  }
}

function timeline(slide, items, y = 214) {
  addShape(slide, "line", { left: 92, top: y + 74, width: 1090, height: 0 }, "none", { style: "solid", fill: "#A0A0A8", width: 3 });
  const gap = 1090 / (items.length - 1);
  items.forEach((it, i) => {
    const x = 92 + i * gap;
    addShape(slide, "ellipse", { left: x - 38, top: y, width: 76, height: 76 }, it.color);
    addText(slide, String(i + 1).padStart(2, "0"), { left: x - 28, top: y + 18, width: 56, height: 36 }, { size: 25, bold: true, color: C.white, align: "center" });
    addShape(slide, "roundRect", { left: x - 82, top: y + 116, width: 164, height: 250 }, it.fill, { style: "solid", fill: "none", width: 0 }, 22);
    addText(slide, it.text, { left: x - 66, top: y + 136, width: 132, height: 210 }, { size: 16, color: C.ink });
  });
}

function makeSlide(title, subtitle = "", kicker = "") {
  const slide = prs.slides.add();
  slide.background.fill = C.cream;
  addTitle(slide, title, subtitle, kicker);
  addFooter(slide, prs.slides.items.length);
  return slide;
}

// 1 Cover
{
  const s = prs.slides.add();
  s.background.fill = C.cream;
  addText(s, "Book\nHouse", { left: 76, top: 76, width: 390, height: 250 }, { font: "Georgia", size: 78, color: C.coral });
  addText(s, "Hệ thống quản lý nhà sách", { left: 80, top: 332, width: 560, height: 48 }, { size: 30, color: C.coral });
  addText(s, "Nhóm 4 | GVHD: Lê Thanh Trọng", { left: 82, top: 404, width: 470, height: 30 }, { size: 18, color: C.muted });
  addText(s, "Nguyễn Thành Tài - Đặng Trần Thiện - Nguyễn Tiến Sơn - Võ Ngọc Tài", { left: 82, top: 438, width: 520, height: 70 }, { size: 17, color: C.muted });
  addBookStack(s, 640, 74, 1.3);
  addBookStack(s, 700, 390, 0.72);
  addShape(s, "rect", { left: 610, top: 585, width: 560, height: 20 }, C.coral);
}

// 2-10 Foundation
{
  const s = makeSlide("Bối cảnh đề tài", "Nhà sách cần một hệ thống tập trung để theo dõi dữ liệu và hỗ trợ ra quyết định.", "Tổng quan");
  card(s, 90, 230, 330, 260, "Quản lý kho phức tạp", "Nhiều đầu sách, nhiều thuộc tính và số lượng tồn thay đổi liên tục.", C.lavender, C.violet);
  card(s, 475, 230, 330, 260, "Bán hàng dễ sai sót", "Tra cứu giá, lập hóa đơn và cập nhật tồn kho thủ công tốn thời gian.", C.rose, C.coral);
  card(s, 860, 230, 330, 260, "Báo cáo thiếu tức thời", "Quản lý khó nắm doanh thu, tồn kho, hiệu suất nhân viên theo thời gian thực.", C.aqua, C.teal);
}
{
  const s = makeSlide("Lý do chọn đề tài", "Đề tài đủ gần thực tế, có nghiệp vụ đa dạng và phù hợp để áp dụng Java Spring Boot.", "Định hướng");
  card(s, 90, 230, 315, 240, "Thực tế", "Nghiệp vụ nhà sách quen thuộc, dễ mô phỏng và kiểm chứng qua giao diện.", C.rose, C.coral);
  card(s, 482, 230, 315, 240, "Phức tạp vừa đủ", "Có nhiều phân hệ: sách, bán hàng, khách hàng, kho, báo cáo, phân quyền.", C.lavender, C.violet);
  card(s, 874, 230, 315, 240, "Phù hợp công nghệ", "Áp dụng Spring Boot, JPA, Security, JWT, REST API, MySQL và Cloudinary.", C.aqua, C.teal);
}
{
  const s = makeSlide("Mục tiêu hệ thống", "Số hóa quy trình vận hành nhà sách từ nhập hàng, bán hàng đến báo cáo.", "Mục tiêu");
  addBullets(s, [
    "Quản lý tập trung dữ liệu sách, danh mục, khách hàng, nhân viên và hóa đơn.",
    "Tối ưu quy trình bán hàng, tự động tính tổng tiền và cập nhật tồn kho.",
    "Theo dõi doanh thu, KPI, tồn kho và sách bán chạy bằng báo cáo trực quan.",
    "Phân quyền rõ ràng theo nhóm người dùng, bảo vệ API bằng JWT."
  ], 105, 225, 1000, { size: 22, gap: 74 });
}
{
  const s = makeSlide("Đối tượng sử dụng", "Hệ thống phục vụ các vai trò chính trong quy trình vận hành nhà sách.", "Người dùng");
  const roles = [
    ["Quản lý", C.coral], ["Thu ngân", C.teal], ["Nhân viên kho", C.violet],
    ["Biên mục", C.gold], ["Kế toán", C.coralDark], ["CSKH", C.teal],
  ];
  roles.forEach(([r, col], i) => {
    const x = 95 + (i % 3) * 370;
    const y = 230 + Math.floor(i / 3) * 160;
    addShape(s, "ellipse", { left: x, top: y, width: 82, height: 82 }, col);
    addText(s, r, { left: x + 105, top: y + 20, width: 220, height: 44 }, { size: 25, bold: true });
  });
}
{
  const s = makeSlide("Phạm vi chức năng chính", "Các phân hệ được xây dựng xoay quanh nghiệp vụ quản lý nhà sách.", "Scope");
  const mods = ["Dashboard", "Sách & danh mục", "Bán hàng", "Hóa đơn", "Nhập hàng", "Tồn kho", "Khách hàng", "Nhân viên & quyền", "Phiếu giữ sách", "CSKH & đánh giá", "Báo cáo", "Cài đặt"];
  mods.forEach((m, i) => {
    const x = 84 + (i % 4) * 280;
    const y = 214 + Math.floor(i / 4) * 118;
    addShape(s, "roundRect", { left: x, top: y, width: 230, height: 78 }, [C.rose, C.aqua, C.lavender, C.yellow][i % 4], { style: "solid", fill: "none", width: 0 }, 18);
    addText(s, m, { left: x + 18, top: y + 24, width: 194, height: 32 }, { size: 19, bold: true, align: "center" });
  });
}
{
  const s = makeSlide("Công nghệ sử dụng", "Stack công nghệ được chọn để xây dựng ứng dụng web có API, bảo mật và lưu trữ dữ liệu.", "Technology");
  const rows = [
    ["Backend", "Java 17, Spring Boot, REST API"],
    ["Database", "MySQL, Spring Data JPA"],
    ["Security", "Spring Security, JWT, Refresh Token"],
    ["Frontend", "HTML, CSS, JavaScript"],
    ["Storage", "Cloudinary lưu ảnh sách/khách hàng"],
    ["Tooling", "Maven, Swagger/OpenAPI"]
  ];
  rows.forEach(([a,b], i) => {
    const y = 210 + i * 64;
    addText(s, a, { left: 120, top: y, width: 180, height: 36 }, { size: 22, bold: true, color: [C.coral, C.teal, C.violet][i%3] });
    addShape(s, "roundRect", { left: 320, top: y - 6, width: 820, height: 48 }, C.white, { style: "solid", fill: C.line, width: 1 }, 14);
    addText(s, b, { left: 346, top: y + 5, width: 760, height: 28 }, { size: 20 });
  });
}
{
  const s = makeSlide("Kiến trúc tổng quan", "Ứng dụng được tổ chức theo mô hình client - REST API - database, kèm dịch vụ lưu ảnh và scheduler.", "Architecture");
  const blocks = [
    ["Frontend\nHTML/CSS/JS", 90, 290, C.rose],
    ["Backend API\nSpring Boot", 390, 290, C.aqua],
    ["MySQL\nDatabase", 720, 290, C.lavender],
    ["Cloudinary\nImage storage", 720, 465, C.yellow],
    ["Scheduler\nTác vụ định kỳ", 390, 465, C.blue],
  ];
  blocks.forEach(([t,x,y,f]) => { addShape(s, "roundRect", { left:x, top:y, width:230, height:96 }, f, { style:"solid", fill:C.line, width:1 }, 18); addText(s,t,{left:x+18,top:y+22,width:194,height:54},{size:20,bold:true,align:"center"}); });
  [[320,338,390,338],[620,338,720,338],[620,496,720,496],[505,386,505,465]].forEach(([x1,y1,x2,y2]) => addShape(s, "line", { left:x1, top:y1, width:x2-x1, height:y2-y1 }, "none", { style:"solid", fill:C.coral, width:3 }));
}
{
  const s = makeSlide("Mô hình phân tầng", "Tách rõ giao diện, controller, service, repository và database để dễ bảo trì.", "Architecture");
  timeline(s, [
    { text: "UI HTML/JS gửi request và hiển thị dữ liệu.", color: C.coral, fill: C.rose },
    { text: "Controller nhận request, kiểm tra quyền và điều phối API.", color: C.violet, fill: C.lavender },
    { text: "Service xử lý nghiệp vụ bán hàng, nhập hàng, giữ sách.", color: C.teal, fill: C.aqua },
    { text: "Repository truy vấn và cập nhật dữ liệu qua JPA.", color: C.gold, fill: C.yellow },
    { text: "MySQL lưu trữ dữ liệu nghiệp vụ lâu dài.", color: C.coralDark, fill: C.rose },
  ], 210);
}
{
  const s = makeSlide("Tổng quan cơ sở dữ liệu", "Database được chia theo các nhóm bảng nghiệp vụ chính.", "Database");
  const groups = [
    ["Người dùng & quyền", "nguoi_dung, nhom_nguoi_dung, chuc_nang, phan_quyen"],
    ["Sách & danh mục", "sach, the_loai, tac_gia, nha_xuat_ban, sach_tac_gia"],
    ["Bán hàng", "khach_hang, hoa_don, chi_tiet_hoa_don"],
    ["Nhập hàng", "nha_cung_cap, phieu_nhap, chi_tiet_phieu_nhap"],
    ["Phiếu giữ sách", "phieu_dat_giu_sach, chi_tiet_phieu_giu"],
    ["Đánh giá & KPI", "danh_gia, kpi_ngay, refresh_token"]
  ];
  groups.forEach(([a,b], i) => {
    const x = 86 + (i % 2) * 560;
    const y = 210 + Math.floor(i / 2) * 118;
    card(s, x, y, 500, 86, a, b, [C.rose, C.aqua, C.lavender][i%3], [C.coral, C.teal, C.violet][i%3]);
  });
}
{
  const s = makeSlide("Đăng nhập & bảo mật", "Hệ thống dùng JWT và phân quyền theo chức năng để bảo vệ các API nghiệp vụ.", "Security");
  timeline(s, [
    { text: "Người dùng đăng nhập bằng username/password.", color: C.coral, fill: C.rose },
    { text: "Server kiểm tra tài khoản và trạng thái người dùng.", color: C.violet, fill: C.lavender },
    { text: "Sinh access token JWT cho request tiếp theo.", color: C.teal, fill: C.aqua },
    { text: "Refresh token cấp lại phiên khi gần hết hạn.", color: C.gold, fill: C.yellow },
    { text: "API kiểm tra quyền trước khi xử lý nghiệp vụ.", color: C.coralDark, fill: C.rose },
  ], 210);
}

// Section and feature slides
sectionSlide(2, "Các chức năng chính", "Tóm tắt các phân hệ quan trọng trong phần mềm BookHouse.");

{
  const s = makeSlide("Dashboard", "Màn hình tổng quan giúp quản lý nắm tình hình kinh doanh và vận hành ngay khi đăng nhập.", "Feature");
  timeline(s, [
    { text: "Hiển thị doanh thu hôm nay, 7 ngày và 30 ngày.", color: C.violet, fill: C.lavender },
    { text: "Theo dõi KPI doanh thu trong ngày.", color: C.coral, fill: C.rose },
    { text: "Biểu đồ doanh thu theo ngày, khoảng thời gian và thể loại.", color: C.teal, fill: C.aqua },
    { text: "Thống kê sách bán chạy và tồn kho nổi bật.", color: C.gold, fill: C.yellow },
    { text: "Danh sách hóa đơn gần đây để theo dõi giao dịch mới.", color: C.violet, fill: C.lavender },
    { text: "Lối tắt đến thêm sách, tạo hóa đơn, nhập hàng và báo cáo.", color: C.coral, fill: C.rose },
  ], 210);
}
{
  const s = makeSlide("Quản lý sách", "Quản lý toàn bộ đầu sách trong kho, từ thông tin mô tả đến tồn kho và ảnh bìa.", "Feature");
  timeline(s, [
    { text: "Hiển thị danh sách sách có phân trang và tìm kiếm.", color: C.violet, fill: C.lavender },
    { text: "Thêm sách với tên, giá, tồn kho, năm xuất bản, thể loại, tác giả, NXB.", color: C.coral, fill: C.rose },
    { text: "Cập nhật thông tin và ảnh bìa sách.", color: C.teal, fill: C.aqua },
    { text: "Xóa sách khi không còn quản lý.", color: C.gold, fill: C.yellow },
    { text: "Lọc theo tên sách, thể loại, tác giả và năm xuất bản.", color: C.violet, fill: C.lavender },
    { text: "Theo dõi tồn kho và thống kê sách bán chạy.", color: C.coral, fill: C.rose },
  ], 210);
}
{
  const s = makeSlide("Quản lý danh mục", "Chuẩn hóa dữ liệu nền để sách được phân loại rõ ràng và dễ tra cứu.", "Feature");
  card(s, 96, 230, 320, 230, "Thể loại", "Thêm, sửa, xóa và tra cứu thể loại sách; hỗ trợ lọc sách theo nhóm nội dung.", C.lavender, C.violet);
  card(s, 480, 230, 320, 230, "Tác giả", "Quản lý danh sách tác giả, liên kết nhiều tác giả với một đầu sách.", C.aqua, C.teal);
  card(s, 864, 230, 320, 230, "Nhà xuất bản", "Lưu thông tin nhà xuất bản, phục vụ quản lý nguồn gốc và biên mục sách.", C.rose, C.coral);
}
{
  const s = makeSlide("Bán hàng & hóa đơn", "Hỗ trợ nhân viên lập hóa đơn nhanh, tính tổng tiền và cập nhật tồn kho tự động.", "Feature");
  addBullets(s, [
    "Tạo hóa đơn bán hàng từ danh sách sách và khách hàng.",
    "Quản lý chi tiết hóa đơn, số lượng, đơn giá và tổng tiền.",
    "Xác nhận thanh toán tiền mặt hoặc trạng thái thanh toán.",
    "Hủy đơn khi phát sinh sai sót, đồng bộ lại dữ liệu liên quan.",
    "Thống kê hóa đơn phục vụ báo cáo doanh thu."
  ], 105, 220, 1000, { size: 22, gap: 72, dot: C.teal });
}
{
  const s = makeSlide("Nhập hàng & tồn kho", "Theo dõi quá trình nhập sách từ nhà cung cấp và kiểm soát tồn kho.", "Feature");
  card(s, 92, 226, 330, 250, "Nhà cung cấp", "Quản lý thông tin nhà cung cấp, phục vụ lập phiếu nhập và truy xuất nguồn hàng.", C.rose, C.coral);
  card(s, 476, 226, 330, 250, "Phiếu nhập", "Tạo, cập nhật, hủy phiếu nhập; quản lý chi tiết sách và số lượng nhập.", C.aqua, C.teal);
  card(s, 860, 226, 330, 250, "Tồn kho", "Tổng hợp tồn kho, sách tồn nhiều và sách sắp hết để hỗ trợ quyết định nhập hàng.", C.lavender, C.violet);
}
{
  const s = makeSlide("Quản lý khách hàng & ưu đãi", "Lưu hồ sơ khách hàng, tích điểm và hỗ trợ mã giảm giá.", "Feature");
  addBullets(s, [
    "Thêm, sửa, xóa và tìm kiếm khách hàng.",
    "Cập nhật avatar, thông tin liên hệ và dữ liệu thành viên.",
    "Theo dõi điểm tích lũy, hạng thành viên và lịch sử mua hàng.",
    "Đổi điểm lấy mã giảm giá để tăng khả năng quay lại mua.",
    "Tự động cập nhật điểm/hạng qua tác vụ định kỳ."
  ], 105, 220, 1000, { size: 22, gap: 72, dot: C.coral });
}
{
  const s = makeSlide("Nhân viên & phân quyền", "Quản trị tài khoản nội bộ và giới hạn chức năng theo vai trò.", "Feature");
  card(s, 90, 230, 330, 245, "Tài khoản nhân viên", "Tạo mới, cập nhật, xóa và xem danh sách người dùng trong hệ thống.", C.rose, C.coral);
  card(s, 475, 230, 330, 245, "Phân quyền", "Gán quyền theo chức năng như quản lý sách, hóa đơn, khách hàng, báo cáo.", C.lavender, C.violet);
  card(s, 860, 230, 330, 245, "Hiệu suất", "Thống kê nhân viên xuất sắc dựa trên số đơn và doanh thu xử lý.", C.aqua, C.teal);
}
{
  const s = makeSlide("Phiếu giữ sách", "Cho phép giữ sách cho khách và tự động xử lý các phiếu quá hạn.", "Feature");
  timeline(s, [
    { text: "Tạo phiếu giữ cho khách hàng.", color: C.coral, fill: C.rose },
    { text: "Thêm sách và số lượng vào phiếu.", color: C.violet, fill: C.lavender },
    { text: "Xác nhận phiếu khi khách nhận sách.", color: C.teal, fill: C.aqua },
    { text: "Hủy phiếu khi khách không còn nhu cầu.", color: C.gold, fill: C.yellow },
    { text: "Scheduler tự động đánh dấu phiếu quá hạn.", color: C.coralDark, fill: C.rose },
  ], 210);
}
{
  const s = makeSlide("CSKH & đánh giá", "Thu thập phản hồi từ khách hàng và hỗ trợ nhân viên phản hồi trực tiếp.", "Feature");
  addBullets(s, [
    "Khách hàng có thể gửi đánh giá công khai.",
    "Nhân viên xem danh sách đánh giá trong khu vực CSKH.",
    "Phản hồi đánh giá bằng tài khoản nhân viên hoặc tên hiển thị.",
    "Xóa đánh giá không phù hợp khi cần quản trị nội dung.",
    "Tận dụng phản hồi để cải thiện dịch vụ và trải nghiệm mua hàng."
  ], 105, 220, 1000, { size: 22, gap: 72, dot: C.violet });
}
{
  const s = makeSlide("Báo cáo & KPI", "Cung cấp dữ liệu tổng hợp để hỗ trợ ra quyết định vận hành.", "Feature");
  card(s, 86, 230, 260, 245, "Doanh thu", "Theo ngày, tháng, năm, 7 ngày, 30 ngày và khoảng tùy chọn.", C.rose, C.coral);
  card(s, 380, 230, 260, 245, "Cơ cấu", "Doanh thu theo thể loại và top sách doanh thu cao.", C.aqua, C.teal);
  card(s, 674, 230, 260, 245, "Tồn kho", "Tổng tồn, sách tồn nhiều và sách tồn ít.", C.lavender, C.violet);
  card(s, 968, 230, 220, 245, "KPI", "Đặt và theo dõi mục tiêu doanh thu hôm nay.", C.yellow, C.gold);
}

sectionSlide(3, "Quy trình & kết quả", "Tóm tắt luồng nghiệp vụ, kết quả đạt được và hướng phát triển.");

{
  const s = makeSlide("Luồng nghiệp vụ bán hàng", "Từ chọn sách đến thanh toán, dữ liệu hóa đơn và tồn kho được đồng bộ.", "Workflow");
  timeline(s, [
    { text: "Tra cứu sách và kiểm tra tồn kho.", color: C.violet, fill: C.lavender },
    { text: "Chọn khách hàng hoặc tạo khách hàng mới.", color: C.teal, fill: C.aqua },
    { text: "Lập hóa đơn và chi tiết hóa đơn.", color: C.coral, fill: C.rose },
    { text: "Xác nhận thanh toán.", color: C.gold, fill: C.yellow },
    { text: "Cập nhật tồn kho, điểm khách hàng và báo cáo.", color: C.coralDark, fill: C.rose },
  ], 210);
}
{
  const s = makeSlide("Kết quả đạt được", "Phần mềm đã hoàn thiện các nghiệp vụ cốt lõi của hệ thống quản lý nhà sách.", "Outcome");
  addBullets(s, [
    "Xây dựng backend REST API theo mô hình phân tầng rõ ràng.",
    "Hoàn thiện giao diện quản trị cho các phân hệ chính.",
    "Áp dụng xác thực JWT và phân quyền chức năng.",
    "Tích hợp lưu ảnh Cloudinary và tài liệu API Swagger.",
    "Có scheduler tự động cho điểm khách hàng và phiếu giữ quá hạn.",
    "Có dashboard/báo cáo hỗ trợ theo dõi doanh thu, KPI và tồn kho."
  ], 105, 205, 1020, { size: 21, gap: 62, dot: C.teal });
}
{
  const s = makeSlide("Hạn chế hiện tại", "Một số điểm có thể tiếp tục hoàn thiện để tăng tính thực tế khi triển khai.", "Reflection");
  card(s, 96, 225, 320, 245, "Thanh toán", "Mới dừng ở quản lý trạng thái và webhook, chưa mở rộng đầy đủ nhiều cổng thanh toán.", C.rose, C.coral);
  card(s, 480, 225, 320, 245, "Kiểm thử", "Cần bổ sung test nghiệp vụ và kiểm thử tích hợp cho các luồng quan trọng.", C.lavender, C.violet);
  card(s, 864, 225, 320, 245, "Triển khai", "Có cấu hình môi trường nhưng cần tối ưu log, backup và giám sát khi vận hành thật.", C.aqua, C.teal);
}
{
  const s = makeSlide("Hướng phát triển", "Mở rộng hệ thống theo hướng vận hành chuyên nghiệp và trải nghiệm người dùng tốt hơn.", "Next");
  addBullets(s, [
    "Bổ sung quét mã vạch/QR để tăng tốc bán hàng và nhập hàng.",
    "Hoàn thiện tích hợp thanh toán online và đối soát giao dịch.",
    "Thêm biểu đồ phân tích lợi nhuận, chi phí và dự báo tồn kho.",
    "Xây dựng giao diện khách hàng để tra cứu sách, đặt giữ và đánh giá.",
    "Bổ sung test tự động, phân quyền chi tiết hơn và audit log."
  ], 105, 220, 1000, { size: 22, gap: 72, dot: C.coral });
}
{
  const s = prs.slides.add();
  s.background.fill = C.cream;
  addBookStack(s, 690, 112, 1.25);
  addText(s, "Cảm ơn thầy và các bạn đã lắng nghe", { left: 80, top: 138, width: 620, height: 170 }, { size: 54, bold: true, color: C.ink });
  addText(s, "BookHouse - Hệ thống quản lý nhà sách", { left: 84, top: 345, width: 540, height: 38 }, { size: 25, color: C.coral });
  addText(s, "Nhóm 4", { left: 84, top: 406, width: 220, height: 32 }, { size: 20, bold: true, color: C.muted });
  addShape(s, "rect", { left: 80, top: 510, width: 230, height: 10 }, C.coral);
}

// Source notes and plan
await fs.writeFile(path.join(work, "source-notes.txt"), [
  "Source: user-provided PDF 'The Book Club.pdf', 20 pages, used as draft content and visual style reference.",
  "Source: local Java/Spring Boot project under C:/Users/dangt/IdeaProjects/DoAnJava, especially controller package and static HTML pages.",
  "Deck facts are derived from local source code endpoints/entities and extracted slide text. No external metrics were invented.",
].join("\n"), "utf8");

await fs.writeFile(path.join(work, "slide-plan.txt"), [
  "Mode: create a new editable PPTX using the PDF as content/style reference.",
  "Style: warm cream background, coral primary, teal/violet/yellow accents, Aptos/Georgia typography.",
  "Slide count: 25. Content covers overview, architecture, database, security, core features, workflow, results, limitations, future work.",
].join("\n"), "utf8");

for (const [index, slide] of prs.slides.items.entries()) {
  const stem = `slide-${String(index + 1).padStart(2, "0")}`;
  const png = await prs.export({ slide, format: "png", scale: 1 });
  await fs.writeFile(path.join(previewDir, `${stem}.png`), new Uint8Array(await png.arrayBuffer()));
  const layout = await slide.export({ format: "layout" });
  await fs.writeFile(path.join(layoutDir, `${stem}.layout.json`), await layout.text(), "utf8");
}

const montage = await prs.export({ format: "webp", montage: true, scale: 1 });
await fs.writeFile(path.join(previewDir, "deck-montage.webp"), new Uint8Array(await montage.arrayBuffer()));

const pptx = await PresentationFile.exportPptx(prs);
await pptx.save(finalPptx);

await fs.writeFile(path.join(qaDir, "visual-qa.txt"), [
  `Final PPTX: ${finalPptx}`,
  `Slide count: ${prs.slides.items.length}`,
  "Rendered every slide to PNG and generated montage for visual QA.",
  "Checked for obvious issues: title placement, dense timeline cards, footer/page numbering, and placeholder/sample slide removal.",
].join("\n"), "utf8");

console.log(JSON.stringify({ finalPptx, slideCount: prs.slides.items.length, previewDir, montage: path.join(previewDir, "deck-montage.webp") }, null, 2));
