import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { userAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import AuthFrame from '../components/AuthFrame';
import '../styles/Auth.css';

const Login = () => {
  const [formData, setFormData] = useState({
    identifier: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [otpSent, setOtpSent] = useState(false);
  const [otp, setOtp] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await userAPI.login(formData);

      if (typeof response.data === 'string' && response.data.includes('OTP sent')) {
        setOtpSent(true);
      } else {
        const token = typeof response.data === 'string'
          ? JSON.parse(response.data).token
          : response.data?.token;
        login(token);
        navigate('/dashboard');
      }
    } catch (err) {
      setError(err.response?.data || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  const handleOtpSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await userAPI.verifyOtp({
        identifier: formData.identifier,
        otp: otp,
      });

      const token = typeof response.data === 'string'
        ? JSON.parse(response.data).token
        : response.data?.token;
      login(token);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data || 'Invalid OTP. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthFrame
      eyebrow={otpSent ? 'Step 2 of 2' : 'Welcome back'}
      title={otpSent ? 'Verify OTP' : 'Sign in to your account'}
      description={
        otpSent
          ? 'Enter the one-time password issued by the backend to complete access.'
          : 'Use your account number and password. If OTP is enabled for the session, you will confirm in the next step.'
      }
      footer={
        <div className="auth-links">
          <Link to="/forgot-password">Forgot Password?</Link>
          <Link to="/register">Open a new account</Link>
        </div>
      }
    >
      <div className="auth-card">
        {error && <div className="error-message">{error}</div>}

        {!otpSent ? (
          <form onSubmit={handleSubmit} className="auth-form">
            <div className="form-group">
              <label htmlFor="identifier">Account Number or Email</label>
              <input
                type="text"
                id="identifier"
                name="identifier"
                value={formData.identifier}
                onChange={handleChange}
                required
                placeholder="Enter account number or email"
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                placeholder="Enter your password"
              />
            </div>

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Logging in...' : 'Login'}
            </button>
          </form>
        ) : (
          <form onSubmit={handleOtpSubmit} className="auth-form">
            <p className="otp-message">An OTP has been sent to your registered contact.</p>
            
            <div className="form-group">
              <label htmlFor="otp">Enter OTP</label>
              <input
                type="text"
                id="otp"
                name="otp"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                required
                placeholder="Enter 6-digit OTP"
                maxLength="6"
              />
            </div>

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Verifying...' : 'Verify OTP'}
            </button>
          </form>
        )}
      </div>
    </AuthFrame>
  );
};

export default Login;
