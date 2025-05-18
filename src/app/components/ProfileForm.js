'use client';

import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';

export default function ProfileForm() {
  const { user, updateUser } = useAuth();
  const { translations: t } = useLanguage();
  const [message, setMessage] = useState({ type: '', text: '' });
  const [loading, setLoading] = useState(false);
  
  const [formData, setFormData] = useState({
    name: user?.name || '',
    email: user?.email || '',
    phone: user?.phone || '',
    address: user?.address || '',
  });
  
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: '',
  });
  
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
    
    try {
      // BACKEND CONNECTION NEEDED: Update user profile
      // const response = await fetch('/api/users/profile', {
      //   method: 'PUT',
      //   headers: { 
      //     'Content-Type': 'application/json',
      //     'Authorization': `Bearer ${localStorage.getItem('token')}`
      //   },
      //   body: JSON.stringify(formData)
      // });
      // 
      // const data = await response.json();
      // if (data.success) {
      //   // Update the user in context/localStorage
      //   if (updateUser) {
      //     updateUser({
      //       ...user,
      //       ...formData
      //     });
      //   }
      //   
      //   setMessage({ 
      //     type: 'success', 
      //     text: t.profileUpdated || 'Profile updated successfully' 
      //   });
      // } else {
      //   setMessage({ 
      //     type: 'danger', 
      //     text: data.message || 'Failed to update profile' 
      //   });
      // }
      
      // For demo purposes only (remove in production)
      setTimeout(() => {
        // Update the user in context/localStorage
        if (updateUser) {
          updateUser({
            ...user,
            ...formData
          });
        }
        
        setMessage({ 
          type: 'success', 
          text: t.profileUpdated || 'Profile updated successfully' 
        });
        setLoading(false);
      }, 1000);
    } catch (error) {
      setMessage({ 
        type: 'danger', 
        text: error.message || 'An error occurred while updating profile' 
      });
      setLoading(false);
    }
  };
  
  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ type: '', text: '' });
    
    // Basic validation
    if (passwordData.newPassword !== passwordData.confirmNewPassword) {
      setMessage({ 
        type: 'danger', 
        text: t.passwordsNotMatch || 'Passwords do not match' 
      });
      setLoading(false);
      return;
    }
    
    try {
      // In a real app, you would make an API call here
      // For demo purposes, we'll just simulate a successful update
      setTimeout(() => {
        setMessage({ 
          type: 'success', 
          text: t.passwordChanged || 'Password changed successfully' 
        });
        setPasswordData({
          currentPassword: '',
          newPassword: '',
          confirmNewPassword: '',
        });
        setLoading(false);
      }, 1000);
    } catch (error) {
      setMessage({ 
        type: 'danger', 
        text: error.message || 'An error occurred while changing password' 
      });
      setLoading(false);
    }
  };
  
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
                <label htmlFor="name" className="form-label">{t.fullName}</label>
                <input
                  type="text"
                  className="form-control"
                  id="name"
                  name="name"
                  value={formData.name}
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
                <label htmlFor="phone" className="form-label">{t.phoneNumber}</label>
                <input
                  type="tel"
                  className="form-control"
                  id="phone"
                  name="phone"
                  value={formData.phone}
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