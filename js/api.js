export const BASE_URL = 'http://localhost:8081/api';

export function getToken() {
  return localStorage.getItem('token');
}

export function setToken(token) {
  localStorage.setItem('token', token);
}

export function removeToken() {
  localStorage.removeItem('token');
}

export function authHeaders() {
  const token = getToken();
  return {
    Authorization: token ? `Bearer ${token}` : '',
    'Content-Type': 'application/json',
  };
}

export async function apiFetch(endpoint, options = {}) {
  const url = `${BASE_URL}${endpoint.startsWith('/') ? endpoint : `/${endpoint}`}`;
  const headers = {
    ...authHeaders(),
    ...(options.headers || {}),
  };

  const response = await fetch(url, {
    ...options,
    headers,
  });

  if (response.status === 401) {
    throw new Error('401 Unauthorized: Bạn chưa đăng nhập hoặc token đã hết hạn.');
  }

  if (response.status === 403) {
    throw new Error('403 Forbidden: Bạn không có quyền truy cập tài nguyên này.');
  }

  if (response.status >= 500) {
    throw new Error(`500 Server Error: Backend gặp lỗi (${response.status}).`);
  }

  if (!response.ok) {
    const text = await response.text().catch(() => '');
    throw new Error(`HTTP ${response.status}: ${text || response.statusText || 'Request failed'}`);
  }

  return response;
}
