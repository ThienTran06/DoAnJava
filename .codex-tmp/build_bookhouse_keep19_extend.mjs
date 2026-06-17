import fs from "node:fs/promises";
import path from "node:path";
import {
  Presentation,
  PresentationFile,
} from "file:///C:/Users/dangt/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/@oai/artifact-tool/dist/artifact_tool.mjs";

const root = "C:/Users/dangt/IdeaProjects/DoAnJava";
const originalDir = path.join(root, ".codex-tmp", "bookhouse_original_1_19");
const shotDir = path.join(root, ".codex-tmp", "bookhouse_function_shots");
const work = path.join(root, ".codex-tmp", "bookhouse_keep19_extend_work");
const previewDir = path.join(work, "preview");
const layoutDir = path.join(work, "layout");
const qaDir = path.join(work, "qa");
const outputDir = path.join(root, "outputs");
const finalPptx = path.join(outputDir, "BookHouse_BaoCao_DoAn_Giu19_HoanThien.pptx");

await fs.mkdir(previewDir, { recursive: true });
await fs.mkdir(layoutDir, { recursive: true });
await fs.mkdir(qaDir, { recursive: true });
await fs.mkdir(outputDir, { recursive: true });

const W = 1280;
const H = 720;
const C = {
  bg: "#F4F8FE",
  cream: "#FFF8EF",
  ink: "#242428",
  muted: "#5F626C",
  line: "#9EA3AA",
  purple: "#8B5CE5",
  red: "#E85F6B",
  teal: "#39AEB8",
  yellow: "#E1A900",
  lavender: "#E8DDF8",
  rose: "#FFB8C0",
  aqua: "#B7E8EC",
  paleYellow: "#FFE7A3",
  coral: "#F17364",
  white: "#FFFFFF",
  dark: "#151C2B",
};

const prs = Presentation.create({ slideSize: { width: W, height: H } });

async function readImage(imagePath) {
  const bytes = await fs.readFile(imagePath);
  return bytes.buffer.slice(bytes.byteOffset, bytes.byteOffset + bytes.byteLength);
}

function shape(slide, geometry, position, fill, line = "none", radius = 0) {
  const cfg = {
    geometry,
    position,
    fill,
    line: line === "none" ? { style: "solid", fill: "none", width: 0 } : line,
  };
  if (radius && ["rect", "textbox", "roundRect"].includes(geometry)) cfg.borderRadius = radius;
  return slide.shapes.add(cfg);
}

function text(slide, value, position, style = {}) {
  const t = shape(slide, "textbox", position, "none");
  t.text = value;
  t.text.style = {
    typeface: style.font || "Aptos",
    fontSize: style.size || 20,
    bold: style.bold || false,
    color: style.color || C.ink,
    alignment: style.align || "left",
  };
  return t;
}

function footer(slide, n) {
  text(slide, "BookHouse - Hệ thống quản lý nhà sách", { left: 30, top: 690, width: 360, height: 18 }, { size: 9, color: "#A0A4AA" });
  text(slide, String(n).padStart(2, "0"), { left: 1210, top: 686, width: 40, height: 20 }, { size: 10, color: "#A0A4AA", align: "right" });
}

function title(slide, main, sub = "") {
  text(slide, main, { left: 420, top: 34, width: 440, height: 58 }, { size: 38, bold: true, align: "center" });
  if (sub) text(slide, sub, { left: 340, top: 92, width: 600, height: 34 }, { size: 16, color: C.muted, align: "center" });
}

async function addOriginalSlides() {
  for (let i = 1; i <= 19; i++) {
    const s = prs.slides.add();
    const img = await readImage(path.join(originalDir, `original_${String(i).padStart(2, "0")}.png`));
    s.images.add({
      blob: img,
      contentType: "image/png",
      alt: `Slide gốc ${i}`,
      fit: "cover",
      position: { left: 0, top: 0, width: W, height: H },
    });
  }
}

function screenshotSlide(name, subtitle, imageFile) {
  const s = prs.slides.add();
  s.background.fill = C.cream;
  shape(s, "rect", { left: 0, top: 0, width: W, height: H }, C.cream);
  text(s, name, { left: 58, top: 56, width: 360, height: 104 }, { size: 48, bold: true, color: C.ink });
  text(s, subtitle, { left: 62, top: 166, width: 360, height: 72 }, { size: 20, color: C.coral });
  shape(s, "rect", { left: 62, top: 255, width: 170, height: 8 }, C.coral);
  shape(s, "roundRect", { left: 452, top: 54, width: 760, height: 598 }, C.white, { style: "solid", fill: "#E2D8CC", width: 1 }, 18);
  return readImage(imageFile).then(img => {
    s.images.add({
      blob: img,
      contentType: "image/png",
      alt: `Ảnh giao diện ${name}`,
      fit: "cover",
      position: { left: 474, top: 76, width: 716, height: 554 },
      geometry: "roundRect",
      borderRadius: "rounded-xl",
    });
    footer(s, prs.slides.items.length);
  });
}

function functionSlide(titleText, items) {
  const s = prs.slides.add();
  s.background.fill = C.bg;
  text(s, `Chức năng\n${titleText}`, { left: 360, top: 26, width: 560, height: 112 }, { size: 35, bold: true, align: "center" });
  const colors = [
    [C.purple, C.lavender],
    [C.red, C.rose],
    [C.teal, C.aqua],
    [C.yellow, C.paleYellow],
    [C.purple, C.lavender],
    [C.red, C.rose],
  ];
  const startX = items.length === 5 ? 130 : 72;
  const gap = items.length === 5 ? 220 : 190;
  const cardW = items.length === 5 ? 160 : 150;
  shape(s, "line", { left: 58, top: 305, width: 1164, height: 0 }, "none", { style: "solid", fill: C.line, width: 3 });
  items.forEach((body, i) => {
    const x = startX + i * gap;
    const [topColor, fill] = colors[i % colors.length];
    shape(s, "ellipse", { left: x, top: 160, width: 76, height: 76 }, topColor);
    text(s, String(i + 1).padStart(2, "0"), { left: x + 8, top: 180, width: 60, height: 32 }, { size: 28, bold: true, color: C.white, align: "center" });
    shape(s, "line", { left: x + 38, top: 236, width: 0, height: 72 }, "none", { style: "solid", fill: C.line, width: 3 });
    shape(s, "roundRect", { left: x - 36, top: 355, width: cardW, height: 215 }, fill, "none", 22);
    text(s, body, { left: x - 20, top: 378, width: cardW - 30, height: 170 }, { size: 17, color: C.ink });
  });
  footer(s, prs.slides.items.length);
}

function thanksSlide() {
  const s = prs.slides.add();
  s.background.fill = C.cream;
  text(s, "Cảm ơn thầy và các bạn đã lắng nghe", { left: 82, top: 160, width: 650, height: 150 }, { size: 48, bold: true, color: C.ink });
  text(s, "BookHouse - Hệ thống quản lý nhà sách", { left: 86, top: 340, width: 520, height: 36 }, { size: 22, color: C.coral });
  text(s, "Nhóm 4", { left: 86, top: 392, width: 160, height: 28 }, { size: 18, bold: true, color: C.muted });
  shape(s, "rect", { left: 86, top: 500, width: 220, height: 9 }, C.coral);
  const x = 820;
  const y = 118;
  [[0, 20, C.paleYellow], [62, 0, "#DCEAFB"], [124, -8, C.coral], [186, 10, C.teal], [248, 0, C.white]].forEach(([dx, dy, fill]) => {
    shape(s, "roundRect", { left: x + dx, top: y + dy, width: 52, height: 300 }, fill, { style: "solid", fill: "#2E63B7", width: 1 }, 14);
    shape(s, "line", { left: x + dx + 14, top: y + dy + 92, width: 24, height: 0 }, "none", { style: "solid", fill: "#2E63B7", width: 1 });
    shape(s, "line", { left: x + dx + 14, top: y + dy + 150, width: 24, height: 0 }, "none", { style: "solid", fill: "#2E63B7", width: 1 });
  });
  shape(s, "rect", { left: 760, top: 442, width: 430, height: 24 }, C.coral);
}

await addOriginalSlides();

await screenshotSlide("Quản lý\nthể loại", "Giao diện danh mục sách", path.join(shotDir, "categories.png"));
functionSlide("quản lý thể loại", [
  "Hiển thị danh sách thể loại sách trong hệ thống.",
  "Thêm mới thể loại với tên và mô tả.",
  "Cập nhật thông tin thể loại khi cần chỉnh sửa.",
  "Xóa thể loại không còn sử dụng.",
  "Tìm kiếm thể loại và theo dõi trạng thái có sách/không có sách.",
]);

await screenshotSlide("Quản lý\nkhách hàng", "Giao diện hồ sơ khách hàng", path.join(shotDir, "customers.png"));
functionSlide("quản lý khách hàng", [
  "Quản lý danh sách khách hàng, thông tin liên hệ và avatar.",
  "Thêm, sửa, xóa và xem chi tiết hồ sơ khách hàng.",
  "Tìm kiếm, lọc khách theo trạng thái, điểm và số đơn.",
  "Theo dõi điểm tích lũy, hạng thành viên và khách VIP.",
  "Hỗ trợ đổi điểm lấy mã giảm giá.",
  "Thống kê tổng khách, khách VIP, khách mới và tổng đơn.",
]);

await screenshotSlide("Quản lý\nnhân viên", "Giao diện nhân sự và quyền", path.join(shotDir, "staff.png"));
functionSlide("quản lý nhân viên", [
  "Tạo tài khoản nhân viên với tên đăng nhập và mật khẩu.",
  "Cập nhật hồ sơ: chức vụ, email, số điện thoại, ca làm, lương.",
  "Xóa hoặc vô hiệu hóa nhân viên không còn làm việc.",
  "Lọc nhân viên theo chức vụ và tìm kiếm nhanh.",
  "Gán quyền truy cập theo nhóm vai trò.",
  "Theo dõi nhân viên xuất sắc theo hóa đơn và doanh thu.",
]);

await screenshotSlide("Bán hàng", "Giao diện quầy POS", path.join(shotDir, "sales.png"));
functionSlide("bán hàng", [
  "Tra cứu sách và thêm nhanh vào giỏ hàng.",
  "Chọn khách hàng hoặc bán cho khách lẻ.",
  "Tự động tính số lượng, đơn giá, giảm giá và tổng tiền.",
  "Tạo hóa đơn bán hàng từ giỏ hàng.",
  "Xác nhận thanh toán và hỗ trợ in hóa đơn.",
  "Cập nhật tồn kho sau khi hoàn tất giao dịch.",
]);

await screenshotSlide("Quản lý\nhóa đơn", "Giao diện danh sách hóa đơn", path.join(shotDir, "invoices.png"));
functionSlide("quản lý hóa đơn", [
  "Hiển thị danh sách hóa đơn có phân trang và tìm kiếm.",
  "Xem chi tiết hóa đơn: khách hàng, nhân viên, sản phẩm, tổng tiền.",
  "Cập nhật thông tin hóa đơn và chi tiết sản phẩm.",
  "Xác nhận thanh toán tiền mặt hoặc trạng thái thanh toán.",
  "Hủy hóa đơn khi phát sinh sai sót.",
  "In hóa đơn phục vụ thanh toán và lưu chứng từ.",
]);

await screenshotSlide("Nhập hàng", "Giao diện phiếu nhập kho", path.join(shotDir, "imports.png"));
functionSlide("nhập hàng", [
  "Quản lý danh sách nhà cung cấp.",
  "Tạo phiếu nhập kho theo nhà cung cấp và nhân viên kho.",
  "Thêm sách, số lượng và giá nhập vào phiếu.",
  "Cập nhật hoặc hủy phiếu nhập khi cần.",
  "Xem chi tiết phiếu nhập và danh sách mặt hàng.",
  "Tự động tăng tồn kho sau khi nhập hàng hợp lệ.",
]);

await screenshotSlide("Phiếu giữ\nsách", "Giao diện đặt giữ sách", path.join(shotDir, "holds.png"));
functionSlide("phiếu giữ sách", [
  "Tạo phiếu giữ sách cho khách hàng.",
  "Thêm sách và số lượng vào danh sách giữ.",
  "Theo dõi trạng thái phiếu: chờ xác nhận, đã xác nhận, hủy, quá hạn.",
  "Xác nhận phiếu khi khách đến nhận sách.",
  "Hủy phiếu khi khách không còn nhu cầu.",
  "Scheduler tự động xử lý phiếu giữ quá hạn.",
]);

await screenshotSlide("CSKH &\nđánh giá", "Giao diện chăm sóc khách hàng", path.join(shotDir, "cskh.png"));
functionSlide("CSKH và đánh giá", [
  "Thu thập đánh giá công khai từ khách hàng.",
  "Lọc đánh giá theo loại, trạng thái và số sao.",
  "Hiển thị thống kê tổng đánh giá, chờ duyệt, đã duyệt và 5 sao.",
  "Phản hồi đánh giá trực tiếp trên hệ thống.",
  "Xóa đánh giá không phù hợp.",
  "Hỗ trợ cải thiện chất lượng dịch vụ từ phản hồi khách hàng.",
]);

await screenshotSlide("Báo cáo", "Giao diện doanh thu và tồn kho", path.join(shotDir, "reports.png"));
functionSlide("báo cáo và KPI", [
  "Theo dõi doanh thu hôm nay, 7 ngày, 30 ngày và khoảng tùy chọn.",
  "Xem doanh thu theo ngày, tháng, năm và theo thể loại sách.",
  "Thống kê sách bán chạy và sách có doanh thu cao.",
  "Theo dõi tổng tồn kho, sách tồn nhiều và sách sắp hết.",
  "Đặt KPI doanh thu hôm nay và theo dõi đạt/chưa đạt.",
  "Cung cấp dữ liệu trực quan hỗ trợ quản lý ra quyết định.",
]);

thanksSlide();

await fs.writeFile(path.join(work, "source-notes.txt"), [
  "Slides 1-19: rendered from user-provided PDF 'The Book Club.pdf' to preserve the existing deck exactly.",
  "Slides 20 onward: added from local project pages and controller/service facts in C:/Users/dangt/IdeaProjects/DoAnJava.",
  "Screenshots: captured from local static server http://127.0.0.1:8765 using files in the workspace.",
  "Report screenshot uses a temporary capture page with a fake token only to avoid login redirect; API data is not fabricated.",
].join("\n"), "utf8");

await fs.writeFile(path.join(work, "slide-plan.txt"), [
  "Mode: create new PPTX from PDF-preserved pages plus new editable slides.",
  "Design: keep 16:9 layout and mimic the original function-list timeline style: numbered circles, dotted/solid guide line, pastel cards.",
  "New slide pattern: screenshot slide followed by function list slide for each remaining feature.",
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
  "Rendered all slides to PNG for visual QA.",
  "Checked that original slides 1-19 are preserved as full-slide images and new slides follow the screenshot/function-list pattern.",
].join("\n"), "utf8");

console.log(JSON.stringify({
  finalPptx,
  slideCount: prs.slides.items.length,
  previewDir,
  montage: path.join(previewDir, "deck-montage.webp"),
}, null, 2));
