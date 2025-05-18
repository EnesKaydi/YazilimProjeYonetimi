'use client';

import { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import PropertyModules from '@/app/components/PropertyModule';
import AuthGuard from '../components/AuthGuard';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import LanguageSwitcher from '../components/LanguageSwitcher';
import PropertyForm from '../components/PropertyForm';

export default function DashboardPage() {
  const [selectedProperty, setSelectedProperty] = useState('');
  const [showWelcome, setShowWelcome] = useState(true);
  const [properties, setProperties] = useState([
    { id: '1', name: 'Akkent Sitesi', address: 'Ankara, Turkey', currency: 'EUR' },
    { id: '2', name: 'Göl Rezidans', address: 'Istanbul, Turkey', currency: 'USD' }
  ]);
  const [isAddingProperty, setIsAddingProperty] = useState(false);
  const [isEditingProperty, setIsEditingProperty] = useState(false);
  const [currentProperty, setCurrentProperty] = useState(null);
  
  const { user } = useAuth();
  const { translations: t } = useLanguage();
  
  // Hide welcome message after 5 seconds
  useEffect(() => {
    const timer = setTimeout(() => {
      setShowWelcome(false);
    }, 5000);
    
    return () => clearTimeout(timer);
  }, []);

  // BACKEND CONNECTION NEEDED: Fetch properties from backend
  // useEffect(() => {
  //   const fetchProperties = async () => {
  //     try {
  //       const response = await fetch('/api/properties', {
  //         headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  //       });
  //       const data = await response.json();
  //       if (data.success) {
  //         setProperties(data.properties);
  //       }
  //     } catch (error) {
  //       console.error('Failed to fetch properties:', error);
  //     }
  //   };
  //   
  //   fetchProperties();
  // }, []);

  // Handle property creation
  const handleAddProperty = () => {
    setIsAddingProperty(true);
    setCurrentProperty(null);
  };

  // BACKEND CONNECTION NEEDED: Save property to backend
const handlePropertySave = (propertyData) => {
  // const saveProperty = async () => {
  //   try {
  //     const url = currentProperty 
  //       ? `/api/properties/${currentProperty.id}` 
  //       : '/api/properties';
  //     const method = currentProperty ? 'PUT' : 'POST';
  //     
  //     const response = await fetch(url, {
  //       method,
  //       headers: { 
  //         'Content-Type': 'application/json',
  //         'Authorization': `Bearer ${localStorage.getItem('token')}`
  //       },
  //       body: JSON.stringify(propertyData)
  //     });
  //     
  //     const data = await response.json();
  //     if (data.success) {
  //       if (currentProperty) {
  //         setProperties(prev => prev.map(p => 
  //           p.id === currentProperty.id ? data.property : p
  //         ));
  //       } else {
  //         setProperties(prev => [...prev, data.property]);
  //       }
  //     }
  //   } catch (error) {
  //     console.error('Failed to save property:', error);
  //   }
  // };
  // saveProperty();
  
  if (currentProperty) {
    // Update existing property
    setProperties(prev => prev.map(p => 
      p.id === currentProperty.id ? { ...propertyData, id: currentProperty.id } : p
    ));
  } else {
    // Create new property
    const newId = (Math.max(...properties.map(p => parseInt(p.id))) + 1).toString();
    setProperties(prev => [...prev, { ...propertyData, id: newId }]);
  }
  
  setIsAddingProperty(false);
  setIsEditingProperty(false);
  setCurrentProperty(null);
  };

  // Handle property deletion
  const handleDeleteProperty = () => {
    if (!selectedProperty) return;
    
    if (confirm(t.confirmDeleteProperty || 'Are you sure you want to delete this property?')) {
      setProperties(prev => prev.filter(p => p.id !== selectedProperty));
      setSelectedProperty('');
    }
  };

  // Save property (create or update)
  const handleSaveProperty = (propertyData) => {
    if (currentProperty) {
      // Update existing property
      setProperties(prev => prev.map(p => 
        p.id === currentProperty.id ? { ...propertyData, id: currentProperty.id } : p
      ));
    } else {
      // Create new property
      const newId = (Math.max(...properties.map(p => parseInt(p.id))) + 1).toString();
      setProperties(prev => [...prev, { ...propertyData, id: newId }]);
    }
    
    setIsAddingProperty(false);
    setIsEditingProperty(false);
    setCurrentProperty(null);
  };

  // Cancel property form
  const handleCancelPropertyForm = () => {
    setIsAddingProperty(false);
    setIsEditingProperty(false);
    setCurrentProperty(null);
  };

  return (
    <AuthGuard>
      <div className="container-fluid">
        <div className="row min-vh-100">
          {/* Sidebar */}
          <div className="col-md-3 col-lg-2 bg-dark text-white shadow p-0">
            <Sidebar />
          </div>

          {/* Main Content */}
          <div className="col-md-9 col-lg-10 p-0 bg-light ms-auto">
            {/* Top Navigation Bar */}
            <nav className="navbar navbar-expand navbar-light bg-white shadow-sm px-4 py-3">
              <div className="container-fluid">
                <h5 className="mb-0 text-primary fw-bold">{t.appName}</h5>
                <div className="ms-auto">
                  <LanguageSwitcher />
                </div>
              </div>
            </nav>

            {/* Main Content Area */}
            <div className="p-4">
              {/* Welcome Alert */}
              {showWelcome && (
                <div className="alert alert-success alert-dismissible fade show" role="alert">
                  <strong>{t.welcome}, {user?.name || 'User'}!</strong> {t.appName}
                  <button type="button" className="btn-close" onClick={() => setShowWelcome(false)} aria-label="Close"></button>
                </div>
              )}

              {/* Dashboard Overview */}
              <div className="row mb-4">
                <div className="col-md-4 mb-3">
                  <div className="card border-0 shadow-sm h-100">
                    <div className="card-body d-flex flex-column">
                      <div className="d-flex align-items-center mb-3">
                        <div className="bg-primary bg-opacity-10 p-3 rounded me-3">
                          <i className="bi bi-building text-primary fs-4"></i>
                        </div>
                        <div>
                          <h6 className="mb-0 text-muted">{t.totalProperties}</h6>
                          <h3 className="mb-0">{properties.length}</h3>
                        </div>
                      </div>
                      <p className="text-muted mb-0 mt-auto">{t.propertyCount}</p>
                    </div>
                  </div>
                </div>
                <div className="col-md-4 mb-3">
                  <div className="card border-0 shadow-sm h-100">
                    <div className="card-body d-flex flex-column">
                      <div className="d-flex align-items-center mb-3">
                        <div className="bg-success bg-opacity-10 p-3 rounded me-3">
                          <i className="bi bi-people text-success fs-4"></i>
                        </div>
                        <div>
                          <h6 className="mb-0 text-muted">{t.totalOwners}</h6>
                          <h3 className="mb-0">24</h3>
                        </div>
                      </div>
                      <p className="text-muted mb-0 mt-auto">{t.ownerCount}</p>
                    </div>
                  </div>
                </div>
                <div className="col-md-4 mb-3">
                  <div className="card border-0 shadow-sm h-100">
                    <div className="card-body d-flex flex-column">
                      <div className="d-flex align-items-center mb-3">
                        <div className="bg-warning bg-opacity-10 p-3 rounded me-3">
                          <i className="bi bi-exclamation-triangle text-warning fs-4"></i>
                        </div>
                        <div>
                          <h6 className="mb-0 text-muted">{t.pendingPayments}</h6>
                          <h3 className="mb-0">5</h3>
                        </div>
                      </div>
                      <p className="text-muted mb-0 mt-auto">{t.dueCount}</p>
                    </div>
                  </div>
                </div>
              </div>

              {/* Property Selection */}
              <div className="card border-0 shadow-sm mb-4">
                <div className="card-header bg-white py-3">
                  <h5 className="card-title mb-0">{t.propertyManagement}</h5>
                </div>
                <div className="card-body">
                  <div className="mb-4">
                    <label htmlFor="propertyDropdown" className="form-label fw-bold">{t.selectProperty}</label>
                    <div className="input-group">
                      <select
                        id="propertyDropdown"
                        className="form-select form-select-lg"
                        value={selectedProperty}
                        onChange={(e) => setSelectedProperty(e.target.value)}
                      >
                        <option value="">{t.select}</option>
                        {properties.map(property => (
                          <option key={property.id} value={property.id}>
                            {property.name}
                          </option>
                        ))}
                      </select>
                      <button 
                        className="btn btn-primary" 
                        type="button"
                        onClick={handleAddProperty}
                      >
                        <i className="bi bi-plus-lg"></i> {t.newProperty}
                      </button>
                    </div>
                  </div>

                  {/* Property Modules */}
                  {selectedProperty && !isAddingProperty && !isEditingProperty && (
                    <div className="mt-4">
                      <div className="d-flex justify-content-between mb-3">
                        <h5></h5>
                        <div>
                          <button 
                            className="btn btn-outline-danger btn-sm" 
                            onClick={handleDeleteProperty}
                          >
                            <i className="bi bi-trash me-1"></i> {t.delete || 'Delete'}
                          </button>
                        </div>
                      </div>
                      <PropertyModules 
                        property={properties.find(p => p.id === selectedProperty)} 
                      />
                    </div>
                  )}

                  {/* Property Form */}
                  {(isAddingProperty || isEditingProperty) && (
                    <PropertyForm 
                      onSave={handleSaveProperty}
                      onCancel={handleCancelPropertyForm}
                      initialData={currentProperty}
                    />
                  )}

                  {/* No Property Selected Message */}
                  {!selectedProperty && !isAddingProperty && !isEditingProperty && (
                    <div className="alert alert-info mt-4">
                      <i className="bi bi-info-circle me-2"></i>
                      {t.pleaseSelectProperty}
                      <p className="mb-0 mt-2">{t.propertyModulesInfo}</p>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </AuthGuard>
  );
}