import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { dashboardAPI, accountAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import AppShell from '../components/AppShell';
import '../styles/Dashboard.css';

const Dashboard = () => {
  const [userDetails, setUserDetails] = useState(null);
  const [accountDetails, setAccountDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { setUser } = useAuth();

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [userResponse, accountResponse] = await Promise.all([
          dashboardAPI.getUserDetails(),
          dashboardAPI.getAccountDetails(),
        ]);

        const userData = typeof userResponse.data === 'string'
          ? JSON.parse(userResponse.data)
          : userResponse.data;

        const accountData = typeof accountResponse.data === 'string'
          ? JSON.parse(accountResponse.data)
          : accountResponse.data;

      setUserDetails(userData);
      setAccountDetails(accountData);
      setUser({
        name: userData?.name,
        firstName: userData?.name?.split(' ')[0] || userData?.name,
        lastName: userData?.name?.split(' ').slice(1).join(' '),
        email: userData?.email,
      });
      } catch (err) {
        setError('Failed to load dashboard data');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, [setUser]);

  const checkAndNavigateToPin = async (path) => {
    try {
      const response = await accountAPI.checkPin();
      if (response.data.includes('not created')) {
        navigate('/create-pin');
      } else {
        navigate(path);
      }
    } catch (err) {
      console.error(err);
      navigate(path);
    }
  };

  if (loading) {
    return (
      <div className="page-state">
        <div className="loading">Loading...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="page-state">
        <div className="error-message">{error}</div>
      </div>
    );
  }

  return (
    <AppShell
      title="Account overview"
      subtitle="A single place for balance, identity details, and transaction actions."
      actions={
        <button type="button" className="ghost-button" onClick={() => navigate('/statement')}>
          View statement
        </button>
      }
    >
      <div className="dashboard-content">
        <div className="welcome-section">
          <div>
            <p className="section-kicker">Primary account</p>
            <h2>{userDetails?.name}</h2>
            <p className="account-number">Account Number {accountDetails?.accountNumber}</p>
          </div>
          <div className="status-pill">Account active</div>
        </div>

        <div className="dashboard-grid">
          <div className="balance-card">
            <p className="card-label">Available balance</p>
            <p className="balance-amount">Rs {accountDetails?.balance?.toLocaleString('en-IN') || '0.00'}</p>
            <div className="balance-meta">
              <div>
                <span>Account type</span>
                <strong>{accountDetails?.accountType || 'Savings'}</strong>
              </div>
              <div>
                <span>Branch status</span>
                <strong>Online</strong>
              </div>
            </div>
          </div>

          <div className="account-insights">
            <div className="insight-card">
              <p className="card-label">Customer email</p>
              <strong>{userDetails?.email}</strong>
            </div>
            <div className="insight-card">
              <p className="card-label">Phone</p>
              <strong>{userDetails?.countryCode} {userDetails?.phoneNumber}</strong>
            </div>
            <div className="insight-card">
              <p className="card-label">Branch</p>
              <strong>{accountDetails?.branch}</strong>
            </div>
            <div className="insight-card stretch">
              <p className="card-label">Registered address</p>
              <strong>{userDetails?.adderss}</strong>
            </div>
          </div>
        </div>

        <div className="quick-actions">
          <div className="section-heading">
            <div>
              <p className="section-kicker">Actions</p>
              <h3>Move money and manage the account</h3>
            </div>
          </div>
          <div className="actions-grid">
            <button 
              className="action-card"
              onClick={() => checkAndNavigateToPin('/deposit')}
            >
              <span className="action-icon">IN</span>
              <span className="action-title">Deposit</span>
              <span className="action-copy">Add funds instantly</span>
            </button>

            <button 
              className="action-card"
              onClick={() => checkAndNavigateToPin('/withdraw')}
            >
              <span className="action-icon">OUT</span>
              <span className="action-title">Withdraw</span>
              <span className="action-copy">Move cash out securely</span>
            </button>

            <button 
              className="action-card"
              onClick={() => checkAndNavigateToPin('/transfer')}
            >
              <span className="action-icon">TX</span>
              <span className="action-title">Transfer</span>
              <span className="action-copy">Send to another account</span>
            </button>

            <button 
              className="action-card"
              onClick={() => navigate('/statement')}
            >
              <span className="action-icon">ST</span>
              <span className="action-title">Statement</span>
              <span className="action-copy">Review transaction history</span>
            </button>

            <button 
              className="action-card"
              onClick={() => navigate('/create-pin')}
            >
              <span className="action-icon">PI</span>
              <span className="action-title">Manage PIN</span>
              <span className="action-copy">Create or rotate your PIN</span>
            </button>

            <button 
              className="action-card"
              onClick={() => navigate('/profile')}
            >
              <span className="action-icon">ID</span>
              <span className="action-title">Profile</span>
              <span className="action-copy">Update customer details</span>
            </button>
          </div>
        </div>
      </div>
    </AppShell>
  );
};

export default Dashboard;
