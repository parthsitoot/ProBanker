import React, { useState, useEffect } from 'react';
import { accountAPI } from '../services/api';
import AppShell from '../components/AppShell';
import '../styles/Statement.css';

const Statement = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filter, setFilter] = useState('all'); // all, credit, debit
  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const response = await accountAPI.getStatement();
      const data = typeof response.data === 'string' 
        ? JSON.parse(response.data) 
        : response.data;
      
      setTransactions(Array.isArray(data) ? data : []);
    } catch (err) {
      setError('Failed to load transaction history');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const getFilteredTransactions = () => {
    if (filter === 'all') return transactions;
    return transactions.filter(t => t.transactionType?.toLowerCase() === filter);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getTransactionIcon = (type) => {
    const lowerType = type?.toLowerCase();
    if (lowerType === 'credit' || lowerType === 'deposit') return 'IN';
    if (lowerType === 'debit' || lowerType === 'withdrawal') return 'OUT';
    if (lowerType === 'transfer') return 'TX';
    return 'LOG';
  };

  const getTransactionClass = (type) => {
    const lowerType = type?.toLowerCase();
    if (lowerType === 'credit' || lowerType === 'deposit') return 'transaction-credit';
    if (lowerType === 'debit' || lowerType === 'withdrawal') return 'transaction-debit';
    return 'transaction-neutral';
  };

  if (loading) {
    return (
      <div className="page-state">
        <div className="loading">Loading transactions...</div>
      </div>
    );
  }

  return (
    <AppShell title="Account statement" subtitle="Review posted activity with simple credit and debit filters.">
      <div className="statement-card">
        <div className="statement-header">
          <div>
            <p className="section-kicker">History</p>
            <h2>Transaction timeline</h2>
            <p>Your backend statement endpoint is rendered as a scannable ledger.</p>
          </div>
          <div className="statement-summary">
            <span>Total records</span>
            <strong>{getFilteredTransactions().length}</strong>
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="filter-section">
          <button
            className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
            onClick={() => setFilter('all')}
          >
            All Transactions
          </button>
          <button
            className={`filter-btn ${filter === 'credit' ? 'active' : ''}`}
            onClick={() => setFilter('credit')}
          >
            Credits
          </button>
          <button
            className={`filter-btn ${filter === 'debit' ? 'active' : ''}`}
            onClick={() => setFilter('debit')}
          >
            Debits
          </button>
        </div>

        <div className="transactions-list">
          {getFilteredTransactions().length === 0 ? (
            <div className="no-transactions">
              <p>No transactions found</p>
            </div>
          ) : (
            getFilteredTransactions().map((transaction, index) => (
              <div key={transaction.id || index} className={`transaction-item ${getTransactionClass(transaction.transactionType)}`}>
                <div className="transaction-icon-container">
                  <span className="transaction-type-icon">
                    {getTransactionIcon(transaction.transactionType)}
                  </span>
                </div>
                
                <div className="transaction-details">
                  <div className="transaction-type">
                    {transaction.transactionType || 'Transaction'}
                  </div>
                  <div className="transaction-date">
                    {formatDate(transaction.transactionDate || transaction.timestamp)}
                  </div>
                  {transaction.description && (
                    <div className="transaction-description">
                      {transaction.description}
                    </div>
                  )}
                  {transaction.targetAccountNumber && (
                    <div className="transaction-account">
                      To/From: {transaction.targetAccountNumber}
                    </div>
                  )}
                </div>
                
                <div className="transaction-amount-container">
                  <div className={`transaction-amount ${getTransactionClass(transaction.transactionType)}`}>
                    {transaction.transactionType?.toLowerCase() === 'credit' || 
                     transaction.transactionType?.toLowerCase() === 'deposit' ? '+' : '-'}
                    Rs {transaction.amount?.toLocaleString('en-IN') || '0.00'}
                  </div>
                  {transaction.balanceAfter && (
                    <div className="balance-after">
                      Balance: Rs {transaction.balanceAfter.toLocaleString('en-IN')}
                    </div>
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </AppShell>
  );
};

export default Statement;
