import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { accountAPI } from '../services/api';
import AppShell from '../components/AppShell';
import '../styles/Transaction.css';

const Deposit = () => {
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
      const response = await accountAPI.cashDeposit({
        amount: parseFloat(formData.amount),
        pin: formData.pin,
      });
      
      setSuccess(response.data);
      setFormData({ amount: '', pin: '' });
      
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
    } catch (err) {
      setError(err.response?.data || 'Deposit failed. Please check your PIN and try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppShell title="Deposit funds" subtitle="Post a credit to the authenticated account using the transaction PIN.">
      <div className="transaction-layout">
        <div className="transaction-card">
          <div className="transaction-header">
            <p className="section-kicker">Cash in</p>
            <h2>Deposit into your account</h2>
            <p>Use this form to call the deposit endpoint with an amount and transaction PIN.</p>
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
                placeholder="Enter amount to deposit"
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
              {loading ? 'Processing...' : 'Deposit Cash'}
            </button>
          </form>
        </div>

        <aside className="transaction-aside">
          <div className="transaction-note-card">
            <p className="card-label">Deposit notes</p>
            <ul className="transaction-note-list">
              <li>Funds are credited immediately after backend validation.</li>
              <li>PIN verification is required on every transaction.</li>
              <li>Amounts are sent as numeric values to the API.</li>
            </ul>
          </div>
        </aside>
      </div>
    </AppShell>
  );
};

export default Deposit;
