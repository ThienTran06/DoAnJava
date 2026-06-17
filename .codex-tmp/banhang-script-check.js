
    const API_BASE_URL =  window.location.origin;
    let books = [];
    let selectedCartItems = [];
    let selectedCustomer = null;
    let customerDiscountCodes = [];
    let currentUser = null;

    let currentBookPage = 0;
    let bookPageSize = 5;
    let totalBookPages = 0;
    let searchBookQuery = "";
    let customerTypingTimeout = null;

    let currentPaymentMethod = "TIEN_MAT";
    let paymentCheckInterval = null;
    let activeInvoiceId = null;

    let isInvoiceFromReservation = false;
    let activeInvoiceObjectCache = null;

    const imgPlaceholder = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPSc0MCcgaGVpZ2h0PSc1NCcgdmlld0JveD0nMCAwIDQwIDU0JyBzdHlsZT0nYmFja2dyb3VuZDolMjNlZWU7Jz48dGV4dCB4PSc1MCUnIHk9JzUwJScgZm9udC1zaXplPSc5JyBmb250LWZhbWlseT0nc2Fucy1zZXJpZicgZmlsbD0nJTIzOTk5JyBkb21pbmFudC1iYXNlbGluZT0nbWlkZGxlJyB0ZXh0LWFuY2hvcj0nbWlkZGxlJz5ObyBJbWFnZTwvdGV4dD48L3N2Zz4=";
    const cartPlaceholder = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPSc1MCcgaGVpZ2h0PSc2OCcgdmlld0JveD0nMCAwIDUwIDY4JyBzdHlsZT0nYmFja2dyb3VuZDolMjNlZWU7Jz48dGV4dCB4PSc1MCUnIHk9JzUwJScgZm9udC1zaXplPSc5JyBmb250LWZhbWlseT0nc2Fucy1zZXJpZicgZmlsbD0nJTIzOTk5JyBkb21pbmFudC1iYXNlbGluZT0nbWlkZGxlJyB0ZXh0LWFuY2hvcj0nbWlkZGxlJz5ObyBJbWFnZTwvdGV4dD48L3N2Zz4=";

    function initStockRealtime() {
        if (typeof SockJS !== "function" || typeof Stomp === "undefined") {
            console.warn("Không tải được SockJS/STOMP, bỏ qua realtime tồn kho.");
            return;
        }

        try {
            const socket = new SockJS(window.location.origin + "/ws");
            const stompClient = Stomp.over(socket);
            if (stompClient.debug) stompClient.debug = null;

            stompClient.connect({}, function () {
                stompClient.subscribe("/topic/stock", async function (msg) {
                    const data = JSON.parse(msg.body);
                    if (data.type === "STOCK_CHANGED") {
                        console.log("Đã nhận sự kiện tồn kho, đang tải lại sách...");
                        await fetchBooks();
                    }
                });
            }, function (error) {
                console.warn("Không kết nối được realtime tồn kho:", error);
            });
        } catch (error) {
            console.warn("Không khởi tạo được realtime tồn kho:", error);
        }
    }

    function getSafeBookImage(src, fallback = imgPlaceholder) {
        const value = String(src || "").trim();
        if (!value) return fallback;
        const lower = value.toLowerCase();
        if (lower.includes("zalo") || lower.includes("zaloapp") || lower.includes("zalo.me")) {
            return fallback;
        }
        return value;
    }

    function getAuthHeaders(contentType = "application/json") {
        const token = localStorage.getItem("token") || sessionStorage.getItem("token");
        const headers = {};
        if (contentType) headers["Content-Type"] = contentType;
        if (token) headers["Authorization"] = `Bearer ${token}`;
        return headers;
    }

    async function readApiError(response, fallback) {
        try {
            const text = await response.text();
            if (!text) return fallback;
            const data = JSON.parse(text);
            return data.message || data.error || text;
        } catch {
            return fallback;
        }
    }

    function renderBookLoadError(message) {
        const tbody = document.getElementById("bookSearchTableBody");
        if (!tbody) return;
        tbody.innerHTML = `<tr><td colspan="3" style="text-align:center; color:var(--red); padding:30px 12px;">${message}</td></tr>`;
        document.getElementById("bookPaginationInfo").innerText = "Trang 1";
        document.getElementById("btnPrevBookPage").disabled = true;
        document.getElementById("btnNextBookPage").disabled = true;
    }

    function decodeJwtPayload() {
        const token = localStorage.getItem("token");
        if (!token) return null;
        const parts = token.split(".");
        if (parts.length < 2) return null;
        try {
            const base64 = parts[1].replace(/-/g, "+").replace(/_/g, "/");
            const padded = base64.padEnd(Math.ceil(base64.length / 4) * 4, "=");
            const bytes = Uint8Array.from(atob(padded), (char) => char.charCodeAt(0));
            return JSON.parse(new TextDecoder("utf-8").decode(bytes));
        } catch {
            return null;
        }
    }

    function getStoredCurrentUser() {
        try {
            const user = JSON.parse(localStorage.getItem("user") || "null");
            if (user?.id) return user;
        } catch {}

        const payload = decodeJwtPayload();
        if (!payload?.id) return null;
        return {
            id: payload.id,
            username: payload.sub,
            hoTen: payload.hoTen || payload.name || payload.sub || "Admin",
            role: payload.role,
        };
    }

    function loadCurrentUser() {
        const user = getStoredCurrentUser();
        currentUser = user;
        if (user) {
            const nameBox = document.getElementById("user-display-name");
            const avatarBox = document.getElementById("avatar-box");
            if (nameBox) nameBox.innerText = user.hoTen || user.username || "Admin";
            if (avatarBox) avatarBox.innerText = String(user.hoTen || user.username || "A").charAt(0).toUpperCase();
        }
    }

    window.addEventListener('storage', (event) => {
        if (event.key === 'DRAFT_INVOICE') {
            if (event.newValue === null) {
                if (event.oldValue) {
                    const oldDraft = JSON.parse(event.oldValue);
                    if (activeInvoiceId && activeInvoiceId === oldDraft.id) {
                        if (paymentCheckInterval) {
                            clearInterval(paymentCheckInterval);
                            paymentCheckInterval = null;
                        }
                        document.getElementById("invoicePrintModal").classList.remove("open");
                        clearCartAndRefresh();
                        alert("Hóa đơn này đã được xử lý hoặc hoãn (Pending) ở một Tab khác!");
                    }
                }
            } else if (event.newValue !== null) {
                const newDraft = JSON.parse(event.newValue);
                activeInvoiceId = newDraft.id;
                activeInvoiceObjectCache = newDraft.data;
                selectedCartItems = newDraft.items;

                if (newDraft.customer) {
                    selectCustomer(newDraft.customer);
                }

                renderCartItems();
                openInvoicePrintModal(newDraft.data, newDraft.items);
            }
        }
    });

    window.addEventListener("DOMContentLoaded", () => {
        loadCurrentUser();
        initStockRealtime();
        fetchBooks();
        fetchSidebarInvoiceCount();
        setupEventListeners();

        const savedReservation = localStorage.getItem('RESERVATION_INVOICE');

        if (savedReservation) {
            checkFromReservationFlow();
        } else {
            const savedDraft = localStorage.getItem('DRAFT_INVOICE');
            if (savedDraft) {
                const { id, data, items, customer } = JSON.parse(savedDraft);
                activeInvoiceId = id;
                activeInvoiceObjectCache = data;

                selectedCartItems = items || [];
                renderCartItems();

                if (customer) {
                    selectCustomer(customer);
                } else if (data.khachHang) {
                    selectCustomer(data.khachHang);
                } else if (data.tenKhachHang) {
                    document.getElementById("customerSearchInput").value = data.tenKhachHang;
                }

                openInvoicePrintModal(data, items);
            }
        }
    });

    async function checkFromReservationFlow() {
        const savedInvoiceData = localStorage.getItem("RESERVATION_INVOICE");
        if (!savedInvoiceData) return;

        try {
            const invoice = JSON.parse(savedInvoiceData);
            console.log("Đồng bộ dữ liệu hóa đơn từ đặt giữ sách:", invoice);

            isInvoiceFromReservation = true;

            const mockCartItems = (invoice.danhSachChiTiet || []).map((ct) => ({
                id: ct.sach?.id || ct.sachID,
                tenSach: ct.tenSach || "Sách",
                gia: ct.donGia,
                sl: ct.soLuong,
                hinhAnh: ct.sach?.hinhAnh,
                tonKho: ct.sach?.soLuongTon || 999
            }));

            selectedCartItems = mockCartItems;
            renderCartItems();

            if (invoice.khachHang) {
                selectCustomer(invoice.khachHang);
            } else if (invoice.tenKhachHang) {
                document.getElementById("customerSearchInput").value = invoice.tenKhachHang;
            }

            await initNewInvoiceSession(invoice, mockCartItems);
        } catch (e) {
            console.error("Lỗi đồng bộ dữ liệu:", e);
        } finally {
            localStorage.removeItem("RESERVATION_INVOICE");
        }
    }

    function switchPaymentMethod(method) {
        currentPaymentMethod = method;
        document.getElementById("method-cash").classList.toggle("active", method === "TIEN_MAT");
        document.getElementById("method-qrcode").classList.toggle("active", method === "QUET_MA");

        const qrArea = document.getElementById("p-qr-area");
        const overlay = document.getElementById("qrStatusOverlay");
        const qrImage = document.getElementById("p-invoice-qr");
        const btnCashConfirm = document.getElementById("btnConfirmCashPay");
        const btnPrint = document.getElementById("btnExecutePrint");
        const btnCloseModal = document.getElementById("btnCloseInvoiceModal");

        if (method === "TIEN_MAT") {
            document.getElementById("p-modal-title").innerText = "🎉 CHỜ XÁC NHẬN THU TIỀN MẶT";
            document.getElementById("p-invoice-title-type").innerText = "PHIẾU TẠM TÍNH (CHỜ THANH TOÁN)";
            document.getElementById("p-invoice-method").innerText = "Tiền mặt";
            qrArea.style.display = "none";
            btnCashConfirm.style.display = "inline-flex";
            btnCashConfirm.innerText = "✓ Xác nhận đã thu tiền mặt";
            btnCloseModal.disabled = true;
            btnPrint.disabled = true;

            if (paymentCheckInterval) {
                clearInterval(paymentCheckInterval);
                paymentCheckInterval = null;
            }
        } else {
            document.getElementById("p-modal-title").innerText = "⏳ Chờ khách quét mã QR...";
            document.getElementById("p-invoice-title-type").innerText = "PHIẾU TẠM TÍNH (CHỜ THANH TOÁN)";
            document.getElementById("p-invoice-method").innerText = "Chuyển khoản (VietQR)";
            qrArea.style.display = "block";
            btnCashConfirm.style.display = "inline-flex";
            btnCashConfirm.innerText = "✓ Xác nhận đã nhận chuyển khoản QR";
            overlay.style.display = "flex";
            document.getElementById("countdownText").innerText = "Đang tải mã QR thanh toán...";

            qrImage.onload = function () {
                overlay.style.display = "none";
            };

            const totalMoney = activeInvoiceObjectCache ? activeInvoiceObjectCache.tongTien || 0 : 0;
            qrImage.src = `https://img.vietqr.io/image/MB-0393107717-compact2.png?amount=${totalMoney}&addInfo=Thanh%20toan%20HD${activeInvoiceId}`;

            btnCloseModal.disabled = true;
            btnPrint.disabled = true;

            startQrCheckPolling(activeInvoiceId);
        }
    }

    async function initNewInvoiceSession(invoice, cartItems) {
        const currentDraft = localStorage.getItem('DRAFT_INVOICE');
        if (currentDraft) {
            const { id } = JSON.parse(currentDraft);
            if (id !== invoice.id) {
                try {
                    await fetch(`${API_BASE_URL}/api/hoa-don/${id}/Pending`, {
                        method: "PUT",
                        headers: getAuthHeaders(null)
                    });
                    console.log(`Đã đẩy hóa đơn cũ #${id} về PENDING`);
                } catch (err) {
                    console.error("Lỗi khi đẩy hóa đơn cũ về PENDING:", err);
                }
            }
        }

        activeInvoiceId = invoice.id;
        activeInvoiceObjectCache = invoice;

        localStorage.setItem('DRAFT_INVOICE', JSON.stringify({
            id: invoice.id,
            data: invoice,
            items: cartItems,
            customer: selectedCustomer
        }));

        openInvoicePrintModal(invoice, cartItems);
    }

    function startQrCheckPolling(invoiceId) {
        if (paymentCheckInterval) clearInterval(paymentCheckInterval);
        paymentCheckInterval = setInterval(async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/api/hoa-don/${invoiceId}`, {
                    method: "GET",
                    headers: getAuthHeaders(null),
                });
                const currentStatusData = await response.json();
                if (currentStatusData.trangThai === "PAID" || currentStatusData.trangThai === "HOAN_THANH") {
                    clearInterval(paymentCheckInterval);
                    paymentCheckInterval = null;

                    document.getElementById("p-modal-title").innerText = "🎉 QUÉT MÃ QR THÀNH CÔNG!";
                    document.getElementById("p-invoice-title-type").innerText = "HÓA ĐƠN BÁN LẺ";
                    document.getElementById("p-invoice-method").innerText = "Chuyển khoản (Đã thanh toán)";
                    document.getElementById("paymentToggleZone").style.display = "none";

                    localStorage.removeItem('DRAFT_INVOICE');

                    document.getElementById("qrStatusOverlay").style.display = "flex";
                    document.getElementById("countdownText").innerText = "Thanh toán hoàn tất! Vui lòng nhấn nút in hóa đơn.";
                    document.getElementById("btnCloseInvoiceModal").disabled = false;
                    document.getElementById("btnExecutePrint").disabled = false;

                    setTimeout(() => {
                        document.getElementById("qrStatusOverlay").style.display = "none";
                    }, 1000);
                }
            } catch (err) {
                console.log("Hệ thống đang kiểm tra biến động số dư...");
            }
        }, 2000);
    }

    async function executeConfirmCashPay() {
        if (!confirm("Xác nhận bạn đã thu đủ tiền mặt của khách hàng?")) return;
        try {
            const response = await fetch(`${API_BASE_URL}/api/hoa-don/${activeInvoiceId}/thanh-toan-tien-mat`, {
                method: "PUT",
                headers: getAuthHeaders(null),
            });
            if (!response.ok) throw new Error("Cập nhật trạng thái thanh toán tiền mặt thất bại.");

            localStorage.removeItem('DRAFT_INVOICE');

            document.getElementById("p-modal-title").innerText = "🎉 ĐÃ THU TIỀN MẶT THÀNH CÔNG!";
            document.getElementById("p-invoice-title-type").innerText = "HÓA ĐƠN BÁN LẺ";
            document.getElementById("p-invoice-method").innerText = "Tiền mặt (Đã thanh toán)";
            document.getElementById("paymentToggleZone").style.display = "none";
            document.getElementById("btnConfirmCashPay").style.display = "none";
            document.getElementById("btnCloseInvoiceModal").disabled = false;
            document.getElementById("btnExecutePrint").disabled = false;

            alert("Hóa đơn chuyển sang trạng thái ĐÃ THANH TOÁN (Tiền mặt). Hãy nhấn nút In hóa đơn!");
        } catch (err) {
            alert("Lỗi: " + err.message);
        }
      }, 2000);
    }

    async function executeConfirmCashPay() {
      const isQrPayment = currentPaymentMethod === "QUET_MA";
      const confirmMessage = isQrPayment
        ? "Xác nhận bạn đã nhận đủ tiền chuyển khoản QR của khách hàng?"
        : "Xác nhận bạn đã thu đủ tiền mặt của khách hàng?";
      if (!confirm(confirmMessage)) return;
      try {
        const response = await fetch(
          `${API_BASE_URL}/api/hoa-don/${activeInvoiceId}/xac-nhan-thanh-toan`,
          {
            method: "PUT",
            headers: getAuthHeaders(null),
          },
        );
        if (!response.ok) {
          throw new Error("Cập nhật trạng thái thanh toán thất bại.");
        }

        document.getElementById("p-modal-title").innerText =
          isQrPayment ? "🎉 ĐÃ NHẬN CHUYỂN KHOẢN QR!" : "🎉 ĐÃ THU TIỀN MẶT THÀNH CÔNG!";
        document.getElementById("p-invoice-title-type").innerText =
          "HÓA ĐƠN BÁN LẺ";
        document.getElementById("p-invoice-method").innerText =
          isQrPayment ? "Chuyển khoản QR (Đã thanh toán)" : "Tiền mặt (Đã thanh toán)";

        document.getElementById("paymentToggleZone").style.display = "none";
        document.getElementById("btnConfirmCashPay").style.display = "none";
        document.getElementById("qrStatusOverlay").style.display = "none";
        if (paymentCheckInterval) {
          clearInterval(paymentCheckInterval);
          paymentCheckInterval = null;
        }

        document.getElementById("btnCloseInvoiceModal").disabled = false;
        document.getElementById("btnExecutePrint").disabled = false;

        alert("Hóa đơn đã chuyển sang trạng thái ĐÃ THANH TOÁN. Hãy nhấn nút In hóa đơn!");
      } catch (err) {
        alert("Lỗi: " + err.message);
      }
    }
    async function fetchSidebarInvoiceCount() {
        try {
            const response = await fetch(`${API_BASE_URL}/api/hoa-don?page=0&size=1`, {
                method: "GET",
                headers: getAuthHeaders(null),
            });
            const pageData = await response.json();
            const countBadge = document.getElementById("sidebar-invoice-count");
            if (countBadge) countBadge.innerText = pageData.totalElements || 0;
        } catch (err) {}
    }

    function setupEventListeners() {
        document.getElementById("searchBookInput").addEventListener("input", (e) => {
            searchBookQuery = e.target.value;
            currentBookPage = 0;
            clearTimeout(window.bookTimeout);
            window.bookTimeout = setTimeout(() => {
                fetchBooks();
            }, 300);
        });

        document.getElementById("customerSearchInput").addEventListener("input", (e) => {
            const keyword = e.target.value.trim();
            clearTimeout(customerTypingTimeout);
            if (keyword.length < 2) {
                document.getElementById("customerSuggestions").style.display = "none";
                return;
            }
            customerTypingTimeout = setTimeout(() => {
                fetchCustomerSuggestions(keyword);
            }, 300);
        });

        document.addEventListener("click", (e) => {
            if (!e.target.closest("#customerSearchInput") && !e.target.closest("#customerSuggestions")) {
                document.getElementById("customerSuggestions").style.display = "none";
            }
            if (!e.target.closest("#discountCodeInput") && !e.target.closest("#discountCodeSuggestions")) {
                document.getElementById("discountCodeSuggestions").style.display = "none";
            }
        });

        document.getElementById("discountCodeInput").addEventListener("focus", () => renderDiscountCodeSuggestions());
        document.getElementById("discountCodeInput").addEventListener("click", () => renderDiscountCodeSuggestions());
        document.getElementById("discountCodeInput").addEventListener("input", () => renderCartItems());

        document.getElementById("discountCodeSuggestions").addEventListener("click", (e) => {
            const deleteBtn = e.target.closest(".js-delete-used-discount");
            if (!deleteBtn) return;
            e.stopPropagation();
            deleteUsedDiscountCode(deleteBtn.dataset.id);
        });

        document.getElementById("btnPrevBookPage").addEventListener("click", () => {
            if (currentBookPage > 0) {
                currentBookPage--;
                fetchBooks();
            }
        });
        document.getElementById("btnNextBookPage").addEventListener("click", () => {
            if (currentBookPage < totalBookPages - 1) {
                currentBookPage++;
                fetchBooks();
            }
        });
    }

    async function fetchBooks() {
        try {
            let url = `${API_BASE_URL}/api/sach/page?page=${currentBookPage}&size=${bookPageSize}`;
            if (searchBookQuery) url += `&keyword=${encodeURIComponent(searchBookQuery)}`;
            const response = await fetch(url, {
                method: "GET",
                headers: getAuthHeaders(null),
            });
            if (!response.ok) {
                const message = await readApiError(response, response.status === 403
                    ? "Tài khoản chưa có quyền xem danh sách sách."
                    : "Không tải được danh sách sách.");
                throw new Error(message);
            }
            const pageData = await response.json();
            books = pageData.content || [];
            totalBookPages = pageData.totalPages || 0;

            renderBookSearchList();
            document.getElementById("bookPaginationInfo").innerText = `Trang ${currentBookPage + 1} / ${totalBookPages || 1}`;
            document.getElementById("btnPrevBookPage").disabled = currentBookPage === 0;
            document.getElementById("btnNextBookPage").disabled = currentBookPage >= totalBookPages - 1 || totalBookPages === 0;
        } catch (err) {
            console.error(err);
            books = [];
            totalBookPages = 0;
            renderBookLoadError(err.message || "Không tải được danh sách sách.");
        }
    }

    function renderBookSearchList() {
        const tbody = document.getElementById("bookSearchTableBody");
        tbody.innerHTML = "";
        if (books.length === 0) {
            tbody.innerHTML = `<tr><td colspan="3" style="text-align:center; color:var(--text-muted); padding:30px 0;">Không tìm thấy sách phù hợp</td></tr>`;
            return;
        }
        books.forEach((b) => {
            const tr = document.createElement("tr");
            const imgUrl = b.hinhAnh || imgPlaceholder;
            tr.innerHTML = `
              <td>
                <div style="display:flex; gap:8px; align-items:start;">
                  <img src="${imgUrl}" style="width:40px; height:54px; object-fit:cover; border-radius:4px;" onerror="this.onerror=null; this.src=imgPlaceholder;">
                  <div>
                    <div style="font-weight:600; line-height:1.2;">${b.tenSach}</div>
                    <div style="color:var(--text-muted); font-size:11px; margin-top:2px;">Tồn kho: ${b.soLuongTon}</div>
                  </div>
                </div>
              </td>
              <td style="white-space:nowrap;">${renderBookPrice(b)}</td>
              <td style="text-align:right; padding-right:14px;"><button class="btn" style="padding:4px 8px; font-size:12px;" onclick="addBookToCart(${b.id})">Thêm</button></td>
            `;
            tbody.appendChild(tr);
        });
    }

    function getSalePrice(book) {
        const originalPrice = Number(book.giaBan || 0);
        const salePrice = Number(book.giaSauUuDai || originalPrice);
        return salePrice > 0 && salePrice < originalPrice ? salePrice : originalPrice;
    }

    function hasActiveSale(book) {
        return Number(book.phanTramUuDai || 0) > 0 && getSalePrice(book) < Number(book.giaBan || 0);
    }

    function renderBookPrice(book) {
        const originalPrice = Number(book.giaBan || 0);
        const salePrice = getSalePrice(book);
        if (!hasActiveSale(book)) return `${originalPrice.toLocaleString()} đ`;
        return `
          <div style="font-weight:800;color:var(--red)">${salePrice.toLocaleString()} đ</div>
          <div style="font-size:11px;color:var(--text-muted);text-decoration:line-through">${originalPrice.toLocaleString()} đ</div>
          <div style="font-size:11px;color:var(--green);font-weight:800">Giảm ${Number(book.phanTramUuDai || 0)}%</div>
        `;
    }

    function discountStatusLabel(status) {
        if (status === "CHUA_SU_DUNG") return "Chưa sử dụng";
        if (status === "DA_SU_DUNG") return "Đã sử dụng";
        if (status === "HET_HAN") return "Hết hạn";
        return status || "Không rõ";
    }

    function renderDiscountCodeSuggestions() {
        const dropdown = document.getElementById("discountCodeSuggestions");
        if (!dropdown) return;
        dropdown.innerHTML = "";

        if (!selectedCustomer) {
            dropdown.innerHTML = `<div class="suggestion-item" style="color:var(--text-muted); cursor:default;">Vui lòng chọn khách hàng trước</div>`;
            dropdown.style.display = "block";
            return;
        }

        if (!customerDiscountCodes.length) {
            dropdown.innerHTML = `<div class="suggestion-item" style="color:var(--text-muted); cursor:default;">Khách hàng chưa có mã giảm giá</div>`;
            dropdown.style.display = "block";
            return;
        }

        customerDiscountCodes.forEach((code) => {
            const div = document.createElement("div");
            div.className = "suggestion-item";
            div.style.display = "flex";
            div.style.alignItems = "center";
            div.style.justifyContent = "space-between";
            div.style.gap = "8px";
            const value = Number(code.giaTriGiam || 0).toLocaleString();
            div.innerHTML = `
              <div style="min-width:0">
                <div style="font-weight:800">${code.ma}</div>
                <div style="font-size:11.5px;color:var(--text-muted)">Giảm ${value} đ · ${discountStatusLabel(code.trangThai)}</div>
              </div>
              ${code.trangThai === "DA_SU_DUNG" ? `<button type="button" class="btn btn-danger-ghost js-delete-used-discount" data-id="${code.id}" style="padding:3px 8px;font-size:11px;">Xóa</button>` : ""}
            `;
            if (code.trangThai === "CHUA_SU_DUNG") {
                div.addEventListener("click", (event) => {
                    if (event.target.closest("button")) return;
                    document.getElementById("discountCodeInput").value = code.ma;
                    dropdown.style.display = "none";
                    renderCartItems();
                });
            } else {
                div.style.cursor = "default";
            }
            dropdown.appendChild(div);
        });

        dropdown.style.display = "block";
    }

    async function fetchCustomerDiscountCodes(customerId) {
        customerDiscountCodes = [];
        document.getElementById("discountCodeInput").value = "";
        try {
            const response = await fetch(`${API_BASE_URL}/api/ma-giam-gia/khach-hang/${customerId}`, {
                method: "GET",
                headers: getAuthHeaders(null),
            });
            if (!response.ok) throw new Error("Không tải được mã giảm giá");
            customerDiscountCodes = await response.json();
        } catch (err) {
            customerDiscountCodes = [];
        }
    }

    async function deleteUsedDiscountCode(id) {
        if (!confirm("Xóa mã giảm giá đã sử dụng này?")) return;
        try {
            const response = await fetch(`${API_BASE_URL}/api/ma-giam-gia/${id}`, {
                method: "DELETE",
                headers: getAuthHeaders(null),
            });
            if (!response.ok) throw new Error("Không thể xóa mã giảm giá");
            if (selectedCustomer) await fetchCustomerDiscountCodes(selectedCustomer.id);
            renderDiscountCodeSuggestions();
        } catch (err) {
            alert(err.message);
        }
    }

    async function fetchCustomerSuggestions(keyword) {
        try {
            let url = `${API_BASE_URL}/api/khach-hang/page?keyword=${encodeURIComponent(keyword)}&page=0&size=10`;
            const response = await fetch(url, {
                method: "GET",
                headers: getAuthHeaders(null),
            });
            if (!response.ok) {
                const message = await readApiError(response, response.status === 403
                    ? "Tài khoản chưa có quyền tìm khách hàng."
                    : "Không tải được danh sách khách hàng.");
                throw new Error(message);
            }
            const pageData = await response.json();
            renderCustomerSuggestions(pageData.content || []);
        } catch (err) {
            renderCustomerSuggestions([], err.message || "Không tải được danh sách khách hàng.");
        }
    }

    function renderCustomerSuggestions(list, errorMessage = "") {
        const dropdown = document.getElementById("customerSuggestions");
        dropdown.innerHTML = "";
        if (errorMessage) {
            dropdown.innerHTML = `<div class="suggestion-item" style="color:var(--red); cursor:default; padding:10px 14px;">${errorMessage}</div>`;
            dropdown.style.display = "block";
            return;
        }
        if (!list || list.length === 0) {
            dropdown.innerHTML = `<div class="suggestion-item" style="color:var(--text-muted); cursor:default; padding:10px 14px;">Không tìm thấy khách hàng</div>`;
            dropdown.style.display = "block";
            return;
        }
        list.forEach((kh) => {
            const div = document.createElement("div");
            div.className = "suggestion-item";
            div.innerHTML = `<strong>${kh.hoTen}</strong> - SĐT: ${kh.sdt || kh.soDienThoai || "N/A"}`;
            div.addEventListener("click", () => selectCustomer(kh));
            dropdown.appendChild(div);
        });
        dropdown.style.display = "block";
    }

    function selectCustomer(kh) {
        selectedCustomer = kh;
        document.getElementById("customerSearchInput").value = kh.hoTen;
        document.getElementById("customerSuggestions").style.display = "none";
        const card = document.getElementById("selectedCustomerCard");
        card.innerHTML = `
          <div style="font-weight:700; font-size:13.5px;">📋 Đã chọn: ${kh.hoTen}</div>
          <div style="margin: 4px 0;"><span class="badge id-tag">KH#${kh.id}</span></div>
          <div style="font-size:12px; color:var(--text-muted)">SĐT: ${kh.sdt || kh.soDienThoai || "N/A"} | Email: ${kh.email || "N/A"}</div>
        `;
        card.style.display = "block";
        fetchCustomerDiscountCodes(kh.id);
    }

    function openCreateCustomerModal() {
        document.getElementById("createCustomerModal").classList.add("open");
    }
    function closeCreateCustomerModal() {
        document.getElementById("createCustomerModal").classList.remove("open");
        document.getElementById("customerForm").reset();
    }

    async function submitCreateCustomer() {
        const hoTen = document.getElementById("custName").value.trim();
        const sdt = document.getElementById("custPhone").value.trim();
        const email = document.getElementById("custEmail").value.trim();
        if (!hoTen || !sdt) {
            alert("Vui lòng nhập đầy đủ họ tên và số điện thoại.");
            return;
        }
        try {
            const response = await fetch(`${API_BASE_URL}/api/khach-hang`, {
                method: "POST",
                headers: getAuthHeaders("application/json"),
                body: JSON.stringify({ hoTen, sdt, email }),
            });
            const newCustomer = await response.json();
            selectCustomer(newCustomer);
            closeCreateCustomerModal();
            alert("Tạo tài khoản khách hàng thành công!");
        } catch (err) {
            alert("Lỗi tạo khách hàng mới");
        }
    }

    function addBookToCart(bookId) {
        const book = books.find((b) => b.id === bookId);
        if (!book) return;
        const existing = selectedCartItems.find((x) => x.id === bookId);
        if (existing) {
            if (existing.sl < book.soLuongTon) {
                existing.sl += 1;
            } else {
                alert("Số lượng vượt quá giới hạn tồn kho hiện tại.");
            }
        } else {
            const salePrice = getSalePrice(book);
            selectedCartItems.push({
                id: book.id,
                tenSach: book.tenSach,
                gia: salePrice,
                giaGoc: book.giaBan,
                phanTramUuDai: hasActiveSale(book) ? book.phanTramUuDai : 0,
                tenUuDai: book.tenUuDai || "",
                sl: 1,
                hinhAnh: book.hinhAnh,
                tonKho: book.soLuongTon,
            });
        }
        renderCartItems();
    }

    function updateCartQuantity(itemId, numericValue) {
        let parsed = parseInt(numericValue) || 1;
        const target = selectedCartItems.find((x) => x.id === itemId);
        if (target) {
            if (parsed > target.tonKho) {
                parsed = target.tonKho;
                alert(`Chỉ có thể đặt tối đa ${target.tonKho} sản phẩm.`);
            }
            if (parsed < 1) parsed = 1;
            target.sl = parsed;
        }
        renderCartItems();
    }

    function removeCartItem(itemId) {
        selectedCartItems = selectedCartItems.filter((x) => x.id !== itemId);
        renderCartItems();
    }

    function getCartSubtotal() {
        return selectedCartItems.reduce((sum, item) => sum + item.gia * item.sl, 0);
    }

    function getSelectedDiscountCode() {
        const inputValue = (document.getElementById("discountCodeInput")?.value || "").trim().toUpperCase();
        if (!inputValue) return null;
        return customerDiscountCodes.find((code) => String(code.ma || "").toUpperCase() === inputValue) || {
            ma: inputValue,
            giaTriGiam: 0,
            trangThai: "KHONG_XAC_DINH",
        };
    }

    function getCartDiscountAmount(subtotal) {
        const code = getSelectedDiscountCode();
        if (!code || code.trangThai !== "CHUA_SU_DUNG") return 0;
        return Math.min(subtotal, Number(code.giaTriGiam || 0));
    }

    function renderDiscountSummary(subtotal) {
        const summary = document.getElementById("discountCodeSummary");
        if (!summary) return;
        const code = getSelectedDiscountCode();
        if (!code) {
            summary.style.display = "none";
            summary.innerHTML = "";
            return;
        }

        const discountAmount = getCartDiscountAmount(subtotal);
        if (code.trangThai === "CHUA_SU_DUNG") {
            summary.style.display = "block";
            summary.innerHTML = `
              <div style="display:flex;justify-content:space-between;gap:12px"><span>Mã giảm giá áp dụng</span><b>${code.ma}</b></div>
              <div style="display:flex;justify-content:space-between;gap:12px;color:var(--green);font-weight:800"><span>Số tiền giảm</span><span>-${discountAmount.toLocaleString()} đ</span></div>
            `;
            return;
        }

        summary.style.display = "block";
        summary.innerHTML = `<div style="color:var(--red);font-weight:800">Mã ${code.ma} không khả dụng (${discountStatusLabel(code.trangThai)})</div>`;
    }

    function renderCartItems() {
        const container = document.getElementById("cartItemsList");
        const totalDisplay = document.getElementById("cartTotalDisplay");
        document.getElementById("cartCount").innerText = selectedCartItems.length;

        if (selectedCartItems.length === 0) {
            container.innerHTML = `<div style="color: #999; text-align: center; padding-top: 150px">Chưa có sản phẩm trong giỏ hàng</div>`;
            totalDisplay.innerText = "0 đ";
            renderDiscountSummary(0);
            return;
        }
        container.innerHTML = "";
        let calculatedTotal = 0;
        selectedCartItems.forEach((item) => {
            calculatedTotal += item.gia * item.sl;
            const card = document.createElement("div");
            card.className = "mini-card";
            card.innerHTML = `
              <img src="${getSafeBookImage(item.hinhAnh, cartPlaceholder)}" onerror="this.onerror=null; this.src=cartPlaceholder;">
              <div class="mini-card-info">
                <div class="mini-card-title">${item.tenSach}</div>
                <div class="mini-card-price">
                  ${item.gia.toLocaleString()} đ
                  ${Number(item.phanTramUuDai || 0) > 0 ? `<span style="margin-left:6px;color:var(--green);font-size:11px;font-weight:800">Giảm ${item.phanTramUuDai}%</span>` : ""}
                </div>
                ${Number(item.phanTramUuDai || 0) > 0 ? `<div style="font-size:11px;color:var(--text-muted);text-decoration:line-through">${Number(item.giaGoc || item.gia).toLocaleString()} đ</div>` : ""}
                <div style="display:flex; justify-content:space-between; align-items:center; margin-top:4px;">
                  <input type="number" class="input-text" min="1" max="${item.tonKho}" value="${item.sl}" style="width:60px; padding:2px 4px; font-size:12px;" onchange="updateCartQuantity(${item.id}, this.value)">
                  <button class="btn btn-danger-ghost" style="padding:2px 6px; font-size:11px; border:none;" onclick="removeCartItem(${item.id})">Xóa</button>
                </div>
              </div>
            `;
            container.appendChild(card);
        });
        const discountAmount = getCartDiscountAmount(calculatedTotal);
        renderDiscountSummary(calculatedTotal);
        totalDisplay.innerText = Math.max(0, calculatedTotal - discountAmount).toLocaleString() + " đ";
    }

    async function submitCreateInvoice() {
        if (selectedCartItems.length === 0) {
            alert("Giỏ hàng đang trống!");
            return;
        }
        if (!selectedCustomer) {
            alert("Vui lòng chọn thông tin khách hàng!");
            return;
        }
        if (!currentUser?.id) {
            alert("Không xác định được thông tin định danh nhân viên phiên hiện tại.");
            return;
        }

        try {
            const payload = {
                khachHangId: selectedCustomer.id,
                nhanVienId: Number(currentUser.id),
                maGiamGia: document.getElementById("discountCodeInput").value.trim(),
                danhSachChiTiet: selectedCartItems.map((x) => ({
                    sachID: x.id,
                    soLuong: x.sl,
                })),
            };
            const response = await fetch(`${API_BASE_URL}/api/hoa-don`, {
                method: "POST",
                headers: getAuthHeaders("application/json"),
                body: JSON.stringify(payload),
            });
            if (!response.ok) throw new Error("Hệ thống gặp lỗi khi tạo hóa đơn");

            const invoiceResult = await response.json();
            console.log("Dữ liệu Hóa đơn từ Server:", invoiceResult);
            await initNewInvoiceSession(invoiceResult, selectedCartItems);
        } catch (err) {
            alert(err.message);
        }
    }

    function openInvoicePrintModal(invoice, cartItems) {
        let totalAmount = invoice.tongTien || invoice.tongGiaTri || 0;
        const subtotalAmount = cartItems.reduce((sum, item) => sum + item.gia * item.sl, 0);
        const invoiceDiscount = Number(invoice.tienGiamGia || invoice.giamGia || 0);
        const invoiceDiscountCode = typeof invoice.maGiamGia === "object"
            ? invoice.maGiamGia?.ma
            : (invoice.maGiamGia || document.getElementById("discountCodeInput")?.value || "");
        const tbody = document.getElementById("p-invoice-items");
        tbody.innerHTML = "";

        cartItems.forEach((item) => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
              <td>${item.tenSach}</td>
              <td style="text-align:center;">${item.sl}</td>
              <td style="text-align:right;">
                ${(item.gia * item.sl).toLocaleString()}
                ${Number(item.phanTramUuDai || 0) > 0 ? `<div style="font-size:10.5px;color:#3a7d44;font-weight:700">Giảm ${item.phanTramUuDai}%</div>` : ""}
              </td>
            `;
            tbody.appendChild(tr);
        });

        document.getElementById("p-invoice-id").innerText = `#HD${invoice.id}`;
        document.getElementById("p-invoice-date").innerText = new Date().toLocaleString("vi-VN");
        const subtotalEl = document.getElementById("p-invoice-subtotal");
        const discountEl = document.getElementById("p-invoice-discount");
        if (subtotalEl && discountEl && invoiceDiscount > 0) {
            subtotalEl.style.display = "block";
            subtotalEl.innerText = `Tạm tính: ${subtotalAmount.toLocaleString()} đ`;
            discountEl.style.display = "block";
            discountEl.innerText = `Mã giảm giá ${invoiceDiscountCode || ""}: -${invoiceDiscount.toLocaleString()} đ`;
        } else if (subtotalEl && discountEl) {
            subtotalEl.style.display = "none";
            discountEl.style.display = "none";
            subtotalEl.innerText = "";
            discountEl.innerText = "";
        }
        document.getElementById("p-invoice-total").innerText = totalAmount.toLocaleString() + " đ";

        const customerName = invoice.khachHang?.hoTen || invoice.tenKhachHang || selectedCustomer?.hoTen || "Khách lẻ";
        document.getElementById("p-invoice-customer").innerText = customerName;
        document.getElementById("p-order-staff").innerText = invoice.nhanVien?.hoTen || "Mã: #" + currentUser?.id || "Nhân viên bán hàng";
        document.getElementById("paymentToggleZone").style.display = "block";
        document.getElementById("btnConfirmCashPay").style.display = "inline-flex";

        switchPaymentMethod("TIEN_MAT");
        document.getElementById("invoicePrintModal").classList.add("open");
    }

    function clearCartAndRefresh() {
      selectedCartItems = [];
      selectedCustomer = null;
      customerDiscountCodes = [];
      document.getElementById("customerSearchInput").value = "";
      document.getElementById("discountCodeInput").value = "";
      document.getElementById("discountCodeSummary").style.display = "none";
      document.getElementById("discountCodeSuggestions").style.display = "none";
      document.getElementById("selectedCustomerCard").style.display = "none";
      activeInvoiceId = null;
      isInvoiceFromReservation = false;
      activeInvoiceObjectCache = null;
      localStorage.removeItem("RESERVATION_INVOICE");
      renderCartItems();
      fetchBooks();
      fetchSidebarInvoiceCount();
      document.getElementById("invoicePrintModal").classList.remove("open");
    }

    async function closeInvoicePrintModal() {
        const currentDraft = localStorage.getItem('DRAFT_INVOICE');
        if (!currentDraft) {
            document.getElementById("invoicePrintModal").classList.remove("open");
            clearCartAndRefresh();
            return;
        }
        const isConfirmed = confirm("Bạn có chắc chắn muốn hoãn thanh toán và lưu hóa đơn này vào danh sách chờ (Pending) không?");

        if (!isConfirmed) return;

        if (activeInvoiceId) {
            try {
                await fetch(`${API_BASE_URL}/api/hoa-don/${activeInvoiceId}/Pending`, {
                    method: "PUT",
                    headers: getAuthHeaders(null)
                });
                console.log("Hóa đơn đã được lưu trạng thái PENDING");
            } catch (err) {
                console.error("Lỗi lưu trạng thái PENDING:", err);
            }
        }

        if (paymentCheckInterval) {
            clearInterval(paymentCheckInterval);
            paymentCheckInterval = null;
        }

        localStorage.removeItem('DRAFT_INVOICE');
        clearCartAndRefresh();
    }

    function executePrint() {
        window.print();
    }
