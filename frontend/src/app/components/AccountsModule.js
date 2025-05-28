'use client';

import { useState, useEffect } from 'react';
import { useLanguage } from '../context/LanguageContext';

export default function AccountsModule() {
  const { translations: t } = useLanguage();
  
  // BACKEND CONNECTION NEEDED: Fetch users from backend
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    const fetchUsers = async () => {
      setLoading(true); // Set loading true at the beginning of fetch
      try {
        const response = await fetch('/api/added-users', {
          headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
        });
        const data = await response.json();
        
        if (response.ok) {
          setUsers(data); 
        } else {
          console.error('Failed to fetch users:', data.message || 'Unknown error');
          setUsers([]); // Set to empty array on error to avoid issues
        }
      } catch (error) {
        console.error('Failed to fetch users:', error);
        setUsers([]); // Set to empty array on error
      } finally {
        setLoading(false);
      }
    };
    
    fetchUsers();
  }, []);
  
  const [expandedUser, setExpandedUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isAdding, setIsAdding] = useState(false);
  const [currentUser, setCurrentUser] = useState({
    id: null,
    name: '',
    email: '',
    password: '',
    role: 'user',
    phoneNumber: '',
    address: '',
    status: 'active'
  });
  
  // Toggle user details expansion
  const toggleUserExpand = (userId) => {
    if (expandedUser === userId) {
      setExpandedUser(null);
    } else {
      setExpandedUser(userId);
    }
  };
  
  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCurrentUser(prev => ({
      ...prev,
      [name]: value
    }));
  };
  
  // Add new user
  const handleAddUser = () => {
    setIsAdding(true);
    setIsEditing(false);
    setCurrentUser({
      id: null,
      name: '',
      email: '',
      password: '',
      role: 'user',
      phoneNumber: '',
      address: '',
      status: 'active'
    });
  };
  
  // Edit user
  const handleEditUser = (user) => {
    setCurrentUser({ ...user });
    setIsEditing(true);
    setIsAdding(true);
  };
  
  // Delete user
  const handleDeleteUser = async (userId) => {
    if (confirm(t.confirmDeleteUser || 'Are you sure you want to delete this user?')) {
      try {
        const response = await fetch(`/api/added-users/${userId}`, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
        });
        if (response.ok) {
          setUsers(prev => prev.filter(user => user.id !== userId));
          if (expandedUser === userId) {
            setExpandedUser(null);
          }
        } else {
          // Handle error
          alert(t.deleteUserError || 'Failed to delete user.');
          console.error('Failed to delete user:', await response.text());
        }
      } catch (error) {
        alert(t.deleteUserError || 'Failed to delete user.');
        console.error('Failed to delete user:', error);
      }
    }
  };
  
  // Save user (create or update)
  const handleSaveUser = async () => {
    if (!currentUser.name || !currentUser.email || (!isEditing && !currentUser.password)) {
      alert(t.requiredFieldsPassword || 'Name, email, and password (for new users) are required');
      return;
    }
    
    const url = isEditing ? `/api/added-users/${currentUser.id}` : '/api/added-users';
    const method = isEditing ? 'PUT' : 'POST';
    
    // Exclude id and potentially password if not changed during edit
    const { id, ...userData } = currentUser;
    if (isEditing && !userData.password) { // Don\'t send empty password on update unless intended
        delete userData.password;
    }

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(userData)
      });
      
      const result = await response.json();
      
      if (response.ok) {
        if (isEditing) {
          setUsers(prev => prev.map(user => 
            user.id === result.id ? result : user
          ));
        } else {
          setUsers(prev => [...prev, { ...result, lastLogin: 'Never' }]); // lastLogin might need adjustment based on backend DTO
        }
        setIsAdding(false);
        setIsEditing(false);
      } else {
        // Handle error (e.g., email already exists)
         alert(result.message || t.saveUserError || 'Failed to save user.');
        console.error('Failed to save user:', result);
      }
    } catch (error) {
      alert(t.saveUserError || 'Failed to save user.');
      console.error('Failed to save user:', error);
    }
  };
  
  // Cancel form
  const handleCancel = () => {
    setIsAdding(false);
    setIsEditing(false);
  };
  
  // Get badge color based on role
  const getRoleBadgeColor = (role) => {
    switch(role) {
      case 'admin': return 'danger';
      case 'manager': return 'warning';
      default: return 'info';
    }
  };
  
  // Get badge color based on status
  const getStatusBadgeColor = (status) => {
    return status === 'active' ? 'success' : 'secondary';
  };
  
  return (
    <div className="accounts-module">
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
          <h5 className="card-title mb-0">{t.userAccounts || 'User Accounts'}</h5>
          <button 
            className="btn btn-primary btn-sm" 
            onClick={handleAddUser}
          >
            <i className="bi bi-person-plus me-2"></i>
            {t.addNewUser || 'Add New User'}
          </button>
        </div>
        <div className="card-body">
          {isAdding ? (
            <div className="user-form p-3 border rounded bg-light mb-4">
              <h6 className="mb-3">{isEditing ? (t.editUser || 'Edit User') : (t.addNewUser || 'Add New User')}</h6>
              <div className="row g-3">
                <div className="col-md-6">
                  <label htmlFor="userName" className="form-label">{t.fullName || 'Full Name'}</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    id="userName" 
                    name="name"
                    value={currentUser.name}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="userEmail" className="form-label">{t.email || 'Email'}</label>
                  <input 
                    type="email" 
                    className="form-control" 
                    id="userEmail" 
                    name="email"
                    value={currentUser.email}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="userPassword" className="form-label">
                    {isEditing ? (t.newPasswordOptional || 'New Password (optional)') : (t.password || 'Password')}
                  </label>
                  <input 
                    type="password" 
                    className="form-control" 
                    id="userPassword" 
                    name="password"
                    value={currentUser.password}
                    onChange={handleInputChange}
                    required={!isEditing}
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="userRole" className="form-label">{t.role || 'Role'}</label>
                  <select 
                    className="form-select" 
                    id="userRole" 
                    name="role"
                    value={currentUser.role}
                    onChange={handleInputChange}
                  >
                    <option value="admin">Admin</option>
                    <option value="manager">Manager</option>
                    <option value="user">User</option>
                  </select>
                </div>
                <div className="col-md-6">
                  <label htmlFor="userStatus" className="form-label">{t.status || 'Status'}</label>
                  <select 
                    className="form-select" 
                    id="userStatus" 
                    name="status"
                    value={currentUser.status}
                    onChange={handleInputChange}
                  >
                    <option value="active">Active</option>
                    <option value="inactive">Inactive</option>
                  </select>
                </div>
                <div className="col-md-6">
                  <label htmlFor="userPhone" className="form-label">{t.phone || 'Phone'}</label>
                  <input 
                    type="tel" 
                    className="form-control" 
                    id="userPhone" 
                    name="phoneNumber"
                    value={currentUser.phoneNumber}
                    onChange={handleInputChange}
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="userAddress" className="form-label">{t.address || 'Address'}</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    id="userAddress" 
                    name="address"
                    value={currentUser.address}
                    onChange={handleInputChange}
                  />
                </div>
              </div>
              <div className="mt-3 d-flex justify-content-end">
                <button 
                  className="btn btn-outline-secondary me-2" 
                  onClick={handleCancel}
                >
                  {t.cancel || 'Cancel'}
                </button>
                <button 
                  className="btn btn-primary" 
                  onClick={handleSaveUser}
                >
                  {isEditing ? (t.update || 'Update') : (t.save || 'Save')}
                </button>
              </div>
            </div>
          ) : null}
          
          {users.length > 0 ? (
            <div className="user-list">
              {users.map(user => (
                <div key={user.id} className="user-item mb-3 border rounded overflow-hidden">
                  <div 
                    className="user-header d-flex justify-content-between align-items-center p-3 bg-light cursor-pointer"
                    onClick={() => toggleUserExpand(user.id)}
                    style={{ cursor: 'pointer' }}
                  >
                    <div className="d-flex align-items-center">
                      <div className="bg-primary text-white rounded-circle me-3 d-flex align-items-center justify-content-center" style={{ width: '40px', height: '40px' }}>
                        {user.name.charAt(0)}
                      </div>
                      <div>
                        <h6 className="mb-0">{user.name}</h6>
                        <small className="text-muted">{user.email}</small>
                      </div>
                    </div>
                    <div className="d-flex align-items-center">
                      <span className={`badge bg-${getRoleBadgeColor(user.role)} me-2`}>{user.role}</span>
                      <span className={`badge bg-${getStatusBadgeColor(user.status)} me-3`}>{user.status}</span>
                      <i className={`bi ${expandedUser === user.id ? 'bi-chevron-up' : 'bi-chevron-down'}`}></i>
                    </div>
                  </div>
                  
                  {expandedUser === user.id && (
                    <div className="user-details p-3 border-top">
                      <div className="row mb-3">
                        <div className="col-md-6">
                          <p className="mb-1"><strong>{t.phone || 'Phone'}:</strong> {user.phone || 'N/A'}</p>
                          <p className="mb-1"><strong>{t.address || 'Address'}:</strong> {user.address || 'N/A'}</p>
                        </div>
                        <div className="col-md-6">
                          <p className="mb-1"><strong>{t.lastLogin || 'Last Login'}:</strong> {user.lastLogin}</p>
                          <p className="mb-1"><strong>{t.accountId || 'Account ID'}:</strong> #{user.id}</p>
                        </div>
                      </div>
                      <div className="d-flex justify-content-end">
                        <button 
                          className="btn btn-outline-primary btn-sm me-2" 
                          onClick={() => handleEditUser(user)}
                        >
                          <i className="bi bi-pencil me-1"></i> {t.edit || 'Edit'}
                        </button>
                        <button 
                          className="btn btn-outline-danger btn-sm" 
                          onClick={() => handleDeleteUser(user.id)}
                        >
                          <i className="bi bi-trash me-1"></i> {t.delete || 'Delete'}
                        </button>
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-5">
              <div className="mb-4">
                <i className="bi bi-people fs-1 text-muted"></i>
              </div>
              <h5>{t.noUsersYet || 'No users added yet'}</h5>
              <p className="text-muted">{t.addUsersDescription || 'Add users to manage their accounts'}</p>
              <button className="btn btn-primary mt-2" onClick={handleAddUser}>
                <i className="bi bi-person-plus me-2"></i>
                {t.addNewUser || 'Add New User'}
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}