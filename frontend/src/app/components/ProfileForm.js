'use client';

import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';

export default function ProfileForm() {
  const { user, updateUser, token } = useAuth();
  const { translations: t } = useLanguage();
  const [message, setMessage] = useState({ type: '', text: '' });
  const [loading, setLoading] = useState(false);
  const [profileLoading, setProfileLoading] = useState(true);
  
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    phoneNumber: '',
    address: '',
  });
  
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: '',
  });
  
  useEffect(() => {
    const fetchUserProfile = async () => {
      setProfileLoading(true);
      try {
        const response = await fetch('/api/users/me', {
          headers: {
            'Authorization': `Bearer ${token || localStorage.getItem('token')}`
          }
        });
        if (!response.ok) {
          const errorData = await response.json().catch(() => ({ message: 'Failed to load profile data.' }));
          throw new Error(errorData.message || 'Failed to load profile data.');
        }
        const data = await response.json();
        setFormData({
          fullName: data.username || '',
          email: data.email || '',
          phoneNumber: data.phoneNumber || '',
          address: data.address || '',
        });
      } catch (error) {
        setMessage({ type: 'danger', text: error.message || 'Could not load profile information.' });
      }
      setProfileLoading(false);
    };

    fetchUserProfile();
  }, [token, t]);
  
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };
  
  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswordData(prev => ({
      ...prev,
      [name]: value
    }));
  };
  
  const handleProfileSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ type: '', text: '' });
    
    const profileDataToSend = {
        fullName: formData.fullName,
        email: formData.email,
        phoneNumber: formData.phoneNumber,
        address: formData.address,
    };

    try {
      const response = await fetch('/api/users/me', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token || localStorage.getItem('token')}`
        },
        body: JSON.stringify(profileDataToSend)
      });

      const responseData = await response.json();

      if (response.ok) {
        if (updateUser) {
            updateUser({
                ...user,
                username: responseData.username,
                email: responseData.email,
                phoneNumber: responseData.phoneNumber,
                address: responseData.address,
            });
        }
        setMessage({
          type: 'success',
          text: t.profileUpdated || 'Profile updated successfully'
        });
      } else {
        setMessage({
          type: 'danger',
          text: responseData.message || 'Failed to update profile'
        });
      }
    } catch (error) {
      setMessage({
        type: 'danger',
        text: error.message || 'An error occurred while updating profile'
      });
    } finally {
        setLoading(false);
    }
  };
  
  const handlePasswordSubmit = async (e) => {
    e.preventDefault();

    if (passwordData.newPassword !== passwordData.confirmNewPassword) {
      setMessage({ type: 'danger', text: t.passwordsNotMatch || 'Passwords do not match' });
      return;
    }
    setLoading(true);
    setMessage({ type: '', text: '' });

    try {
      const response = await fetch('/api/users/me/change-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token || localStorage.getItem('token')}`
        },
        body: JSON.stringify(passwordData)
      });

      if (response.ok) {
        const responseText = await response.text();
        setMessage({
          type: 'success',
          text: responseText || t.passwordChanged || 'Password changed successfully'
        });
        setPasswordData({
          currentPassword: '',
          newPassword: '',
          confirmNewPassword: '',
        });
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to change password.' }));
        setMessage({
          type: 'danger',
          text: errorData.message || 'Failed to change password'
        });
      }
    } catch (error) {
      setMessage({
        type: 'danger',
        text: error.message || 'An error occurred while changing password'
      });
    } finally {
        setLoading(false);
    }
  };
  
  if (profileLoading) {
    return <div className="text-center p-5"><div className="spinner-border" role="status"><span className="visually-hidden">Loading...</span></div></div>;
  }
  
  return (
    <div className="row">
      {message.text && (
        <div className={`alert alert-${message.type} alert-dismissible fade show`} role="alert">
          {message.text}
          <button 
            type="button" 
            className="btn-close" 
            onClick={() => setMessage({ type: '', text: '' })} 
            aria-label="Close"
          ></button>
        </div>
      )}
      
      <div className="col-md-6 mb-4">
        <div className="card border-0 shadow-sm">
          <div className="card-header bg-white py-3">
            <h5 className="card-title mb-0">{t.personalInfo}</h5>
          </div>
          <div className="card-body">
            <form onSubmit={handleProfileSubmit}>
              <div className="mb-3">
                <label htmlFor="fullName" className="form-label">{t.fullName}</label>
                <input
                  type="text"
                  className="form-control"
                  id="fullName"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleInputChange}
                  required
                />
              </div>
              
              <div className="mb-3">
                <label htmlFor="email" className="form-label">{t.email}</label>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                />
              </div>
              
              <div className="mb-3">
                <label htmlFor="phoneNumber" className="form-label">{t.phoneNumber}</label>
                <input
                  type="tel"
                  className="form-control"
                  id="phoneNumber"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  onChange={handleInputChange}
                />
              </div>
              
              <div className="mb-3">
                <label htmlFor="address" className="form-label">{t.address}</label>
                <textarea
                  className="form-control"
                  id="address"
                  name="address"
                  rows="3"
                  value={formData.address}
                  onChange={handleInputChange}
                ></textarea>
              </div>
              
              <div className="d-grid">
                <button 
                  type="submit" 
                  className="btn btn-primary" 
                  disabled={loading}
                >
                  {loading ? t.loading : t.updateProfile}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      
      <div className="col-md-6 mb-4">
        <div className="card border-0 shadow-sm">
          <div className="card-header bg-white py-3">
            <h5 className="card-title mb-0">{t.changePassword}</h5>
          </div>
          <div className="card-body">
            <form onSubmit={handlePasswordSubmit}>
              <div className="mb-3">
                <label htmlFor="currentPassword" className="form-label">{t.currentPassword}</label>
                <input
                  type="password"
                  className="form-control"
                  id="currentPassword"
                  name="currentPassword"
                  value={passwordData.currentPassword}
                  onChange={handlePasswordChange}
                  required
                />
              </div>
              
              <div className="mb-3">
                <label htmlFor="newPassword" className="form-label">{t.newPassword}</label>
                <input
                  type="password"
                  className="form-control"
                  id="newPassword"
                  name="newPassword"
                  value={passwordData.newPassword}
                  onChange={handlePasswordChange}
                  required
                />
              </div>
              
              <div className="mb-3">
                <label htmlFor="confirmNewPassword" className="form-label">{t.confirmNewPassword}</label>
                <input
                  type="password"
                  className="form-control"
                  id="confirmNewPassword"
                  name="confirmNewPassword"
                  value={passwordData.confirmNewPassword}
                  onChange={handlePasswordChange}
                  required
                />
              </div>
              
              <div className="d-grid">
                <button 
                  type="submit" 
                  className="btn btn-primary" 
                  disabled={loading}
                >
                  {loading ? t.loading : t.changePassword}
                </button>
              </div>
            </form>
          </div>
        </div>
        
        <div className="card border-0 shadow-sm mt-4">
          <div className="card-header bg-white py-3">
            <h5 className="card-title mb-0">{t.profilePicture}</h5>
          </div>
          <div className="card-body">
            <div className="text-center mb-3">
              <div className="bg-light rounded-circle mx-auto d-flex align-items-center justify-content-center" style={{ width: '120px', height: '120px', fontSize: '3rem' }}>
                {user?.name?.charAt(0) || 'U'}
              </div>
            </div>
            <div className="d-flex justify-content-center">
              <button className="btn btn-outline-primary me-2">
                <i className="bi bi-upload me-2"></i>
                {t.uploadPicture}
              </button>
              <button className="btn btn-outline-danger">
                <i className="bi bi-trash me-2"></i>
                {t.removePicture}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}