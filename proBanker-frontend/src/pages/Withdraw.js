import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { accountAPI } from '../services/api';
import AppShell from '../components/AppShell';
import '../styles/Transaction.css';

const Withdraw = () => {
  const [formData, setFormData] = useState({
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
      const response = await accountAPI.cashWithdraw({
        amount: parseFloat(formData.amount),
        pin: formData.pin,
      });
      
      setSuccess(response.data);
      setFormData({ amount: '', pin: '' });
      
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
    } catch (err) {
      setError(err.response?.data || 'Withdrawal failed. Please check your PIN and balance.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppShell title="Withdraw funds" subtitle="Debit the logged-in account with balance and PIN validation handled by the backend.">
      <div className="transaction-layout">
        <div className="transaction-card">
          <div className="transaction-header">
            <p className="section-kicker">Cash out</p>
            <h2>Withdraw from your account</h2>
            <p>Submit the amount and PIN. The backend enforces available balance checks.</p>
          </div>

          {error && <div className="error-message">{error}</div>}
          {success && <div className="success-message">{success}</div>}

          <form onSubmit={handleSubmit} className="transaction-form">
            <div className="form-group">
              <label htmlFor="amount">Amount (Rs)</label>
              <input
                type="number"
                id="amount"
                name="amount"
                value={formData.amount}
                onChange={handleChange}
                required
                placeholder="Enter amount to withdraw"
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
                placeholder="Enter your 4-digit PIN"
                maxLength="4"
              />
            </div>

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Processing...' : 'Withdraw Cash'}
            </button>
          </form>
        </div>

        <aside className="transaction-aside">
          <div className="transaction-note-card">
            <p className="card-label">Withdrawal notes</p>
            <ul className="transaction-note-list">
              <li>Debits are reflected immediately if the request passes validation.</li>
              <li>Insufficient balance errors come directly from the backend.</li>
              <li>Use the statement page to verify the posted transaction.</li>
            </ul>
          </div>
        </aside>
      </div>
    </AppShell>
  );
};

export default Withdraw;
