import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { accountAPI } from '../services/api';
import AppShell from '../components/AppShell';
import '../styles/Transaction.css';

const Transfer = () => {
  const [formData, setFormData] = useState({
    targetAccountNumber: '',
    amount: '',
    pin: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
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

    if (parseFloat(formData.amount) <= 0) {
      setError('Amount must be greater than 0');
      setLoading(false);
      return;
    }

    try {
      const response = await accountAPI.fundTransfer({
        targetAccountNumber: formData.targetAccountNumber,
        amount: parseFloat(formData.amount),
        pin: formData.pin,
      });
      
      setSuccess(response.data);
      setFormData({ targetAccountNumber: '', amount: '', pin: '' });
      
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
    } catch (err) {
      setError(err.response?.data || 'Transfer failed. Please check your details and try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppShell title="Transfer funds" subtitle="Send money to another account number using the backend transfer endpoint.">
      <div className="transaction-layout">
        <div className="transaction-card">
          <div className="transaction-header">
            <p className="section-kicker">Account to account</p>
            <h2>Move money to another recipient</h2>
            <p>Review the destination account carefully before submitting the transfer.</p>
          </div>

          {error && <div className="error-message">{error}</div>}
          {success && <div className="success-message">{success}</div>}

          <form onSubmit={handleSubmit} className="transaction-form">
            <div className="form-group">
              <label htmlFor="targetAccountNumber">Recipient Account Number</label>
              <input
                type="text"
                id="targetAccountNumber"
                name="targetAccountNumber"
                value={formData.targetAccountNumber}
                onChange={handleChange}
                required
                placeholder="Enter recipient account number"
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="amount">Amount (Rs)</label>
                <input
                  type="number"
                  id="amount"
                  name="amount"
                  value={formData.amount}
                  onChange={handleChange}
                  required
                  placeholder="Enter amount"
                  step="0.01"
                  min="0.01"
                />
              </div>

              <div className="form-group">
                <label htmlFor="pin">Transaction PIN</label>
                <input
                  type="password"
                  id="pin"
                  name="pin"
                  value={formData.pin}
                  onChange={handleChange}
                  required
                  placeholder="4-digit PIN"
                  maxLength="4"
                />
              </div>
            </div>

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Processing...' : 'Transfer Funds'}
            </button>
          </form>
        </div>

        <aside className="transaction-aside">
          <div className="transaction-note-card">
            <p className="card-label">Transfer notes</p>
            <ul className="transaction-note-list">
              <li>Transfers are immediate when validation succeeds.</li>
              <li>Recipient account number is required exactly as stored by the backend.</li>
              <li>Balance and PIN failures return as API errors in the form.</li>
            </ul>
          </div>
        </aside>
      </div>
    </AppShell>
  );
};

export default Transfer;
