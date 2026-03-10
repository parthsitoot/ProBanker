import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { accountAPI } from '../services/api';
import AppShell from '../components/AppShell';
import '../styles/Transaction.css';

const CreatePin = () => {
  const [formData, setFormData] = useState({
    password: '',
    pin: '',
    confirmPin: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const [pinExists, setPinExists] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    checkPinStatus();
  }, []);

  const checkPinStatus = async () => {
    try {
      const response = await accountAPI.checkPin();
      if (!response.data.toLowerCase().includes('not created')) {
        setPinExists(true);
      }
    } catch (err) {
      console.error('Error checking PIN status:', err);
    }
  };

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
    setSuccess('');

    if (formData.pin !== formData.confirmPin) {
      setError('PINs do not match');
      setLoading(false);
      return;
    }

    if (formData.pin.length !== 4) {
      setError('PIN must be 4 digits');
      setLoading(false);
      return;
    }

    try {
      const response = await accountAPI.createPin({
        password: formData.password,
        pin: formData.pin,
      });
      
      setSuccess(response.data);
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
    } catch (err) {
      setError(err.response?.data || 'Failed to create PIN. Please check your password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppShell
      title={pinExists ? 'Change transaction PIN' : 'Create transaction PIN'}
      subtitle="This flow uses your account password plus a 4-digit transaction PIN."
    >
      <div className="transaction-layout">
        <div className="transaction-card">
          <div className="transaction-header">
            <p className="section-kicker">Security setup</p>
            <h2>{pinExists ? 'Rotate your PIN' : 'Create your PIN'}</h2>
            <p>
              {pinExists
                ? 'Update the existing PIN used by deposit, withdrawal, and transfer endpoints.'
                : 'Set a 4-digit PIN before enabling money movement.'}
            </p>
          </div>

          {error && <div className="error-message">{error}</div>}
          {success && <div className="success-message">{success}</div>}

          <form onSubmit={handleSubmit} className="transaction-form">
            <div className="form-group">
              <label htmlFor="password">Account Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                placeholder="Enter your account password"
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="pin">New PIN</label>
                <input
                  type="password"
                  id="pin"
                  name="pin"
                  value={formData.pin}
                  onChange={handleChange}
                  required
                  placeholder="4 digits"
                  maxLength="4"
                  pattern="[0-9]{4}"
                />
              </div>

              <div className="form-group">
                <label htmlFor="confirmPin">Confirm PIN</label>
                <input
                  type="password"
                  id="confirmPin"
                  name="confirmPin"
                  value={formData.confirmPin}
                  onChange={handleChange}
                  required
                  placeholder="Repeat PIN"
                  maxLength="4"
                  pattern="[0-9]{4}"
                />
              </div>
            </div>

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Processing...' : pinExists ? 'Update PIN' : 'Create PIN'}
            </button>
          </form>
        </div>

        <aside className="transaction-aside">
          <div className="transaction-note-card">
            <p className="card-label">Rules</p>
            <ul className="transaction-note-list">
              <li>PIN must be exactly 4 digits.</li>
              <li>Password confirmation protects the change request.</li>
              <li>The same PIN is reused across deposit, withdrawal, and transfer actions.</li>
            </ul>
          </div>
        </aside>
      </div>
    </AppShell>
  );
};

export default CreatePin;
