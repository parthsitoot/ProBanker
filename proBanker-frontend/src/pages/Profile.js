import React, { useState, useEffect } from 'react';
import { dashboardAPI, userAPI } from '../services/api';
import AppShell from '../components/AppShell';
import '../styles/Profile.css';

const Profile = () => {
  const [userDetails, setUserDetails] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  useEffect(() => {
    fetchUserDetails();
  }, []);

  const fetchUserDetails = async () => {
    try {
      const response = await dashboardAPI.getUserDetails();
      const data = typeof response.data === 'string' 
        ? JSON.parse(response.data) 
        : response.data;
      
      setUserDetails(data);
      setFormData({
        name: data.name || '',
        email: data.email || '',
        phoneNumber: data.phoneNumber || '',
        countryCode: data.countryCode || '',
        address: data.adderss || '',
      });
    } catch (err) {
      setError('Failed to load profile details');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleEdit = () => {
    setIsEditing(true);
    setError('');
    setSuccess('');
  };

  const handleCancel = () => {
    setIsEditing(false);
    setFormData({
      name: userDetails?.name || '',
      email: userDetails?.email || '',
      phoneNumber: userDetails?.phoneNumber || '',
      countryCode: userDetails?.countryCode || '',
      address: userDetails?.adderss || '',
    });
    setError('');
    setSuccess('');
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    setSuccess('');

    try {
      const response = await userAPI.updateUser(formData);
      setSuccess(typeof response.data === 'string' ? response.data : 'Profile updated successfully');
      setUserDetails({
        ...userDetails,
        ...formData,
        adderss: formData.address,
      });
      setIsEditing(false);
    } catch (err) {
      setError(err.response?.data || 'Failed to update profile');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="page-state">
        <div className="loading">Loading profile...</div>
      </div>
    );
  }

  return (
    <AppShell
      title="Profile"
      subtitle="View and update the customer details returned by the dashboard user endpoint."
      actions={
        !isEditing ? (
          <button type="button" onClick={handleEdit} className="ghost-button">
            Edit profile
          </button>
        ) : null
      }
    >
      <div className="profile-card">
        <div className="profile-header">
          <div className="profile-avatar">
            {userDetails?.name?.split(' ').map((part) => part[0]).join('').slice(0, 2)}
          </div>
          <div>
            <h2>{userDetails?.name}</h2>
            <p className="profile-email">{userDetails?.email}</p>
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        {!isEditing ? (
          <div className="profile-details">
            <div className="detail-section">
              <h3>Personal Information</h3>
              <div className="detail-grid">
                <div className="detail-item">
                  <span className="detail-label">Full Name</span>
                  <span className="detail-value">{userDetails?.name}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Email</span>
                  <span className="detail-value">{userDetails?.email}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Phone Number</span>
                  <span className="detail-value">{userDetails?.countryCode} {userDetails?.phoneNumber}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Account Type</span>
                  <span className="detail-value">{userDetails?.accountType}</span>
                </div>
              </div>
            </div>

            <div className="detail-section">
              <h3>Address Information</h3>
              <div className="detail-grid">
                <div className="detail-item full-width">
                  <span className="detail-label">Address</span>
                  <span className="detail-value">{userDetails?.adderss}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Account Number</span>
                  <span className="detail-value">{userDetails?.accountNumber}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">IFSC Code</span>
                  <span className="detail-value">{userDetails?.ifscCode}</span>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <form onSubmit={handleSave} className="profile-form">
            <div className="form-section">
              <h3>Personal Information</h3>
              
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="name">Full Name</label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name || ''}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="countryCode">Country Code</label>
                  <input
                    type="text"
                    id="countryCode"
                    name="countryCode"
                    value={formData.countryCode || ''}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email || ''}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="phoneNumber">Phone Number</label>
                <input
                  type="tel"
                  id="phoneNumber"
                  name="phoneNumber"
                  value={formData.phoneNumber || ''}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-section">
              <h3>Address Information</h3>
              
              <div className="form-group">
                <label htmlFor="address">Address</label>
                <input
                  type="text"
                  id="address"
                  name="address"
                  value={formData.address || ''}
                  onChange={handleChange}
                  required
                />
              </div>

            </div>

            <div className="form-actions">
              <button type="button" onClick={handleCancel} className="btn-secondary">
                Cancel
              </button>
              <button type="submit" className="btn-primary" disabled={saving}>
                {saving ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          </form>
        )}
      </div>
    </AppShell>
  );
};

export default Profile;
