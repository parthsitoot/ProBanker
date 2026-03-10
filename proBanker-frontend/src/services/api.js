import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8180/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// User APIs
export const userAPI = {
  register: (userData) => api.post('/user/register', userData),
  login: (credentials) => api.post('/user/login', credentials),
  generateOtp: (data) => api.post('/user/generate-otp', data),
  verifyOtp: (data) => api.post('/user/verify-otp', data),
  updateUser: (userData) => api.post('/user/update', userData),
  logout: () => api.get('/user/logout'),
};

// Auth APIs
export const authAPI = {
  sendOtpForPasswordReset: (data) => api.post('/authentication/password-reset/send-otp', data),
  verifyOtpAndIssueResetToken: (data) => api.post('/authentication/password-reset/verify-otp', data),
  resetPassword: (data) => api.post('/authentication/password-reset', data),
};

// Dashboard APIs
export const dashboardAPI = {
  getUserDetails: () => api.get('/dashboard/user'),
  getAccountDetails: () => api.get('/dashboard/account'),
};

// Account APIs
export const accountAPI = {
  checkPin: () => api.get('/pin/check'),
  createPin: (data) => api.post('/pin/create', data),
  cashDeposit: (data) => api.post('/deposit', data),
  cashWithdraw: (data) => api.post('/withdraw', data),
  fundTransfer: (data) => api.post('/fundTransfer', data),
  getStatement: () => api.get('/statement'),
};

export default api;
