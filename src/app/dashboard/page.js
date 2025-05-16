'use client';

import { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import PropertyModules from '@/app/components/PropertyModule';
import AuthGuard from '../components/AuthGuard';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import LanguageSwitcher from '../components/LanguageSwitcher';

export default function DashboardPage() {
  const [selectedProperty, setSelectedProperty] = useState('');
  const [showWelcome, setShowWelcome] = useState(true);
  const { user } = useAuth();
  const { translations: t } = useLanguage();
  
  // Hide welcome message after 5 seconds
  useEffect(() => {
    const timer = setTimeout(() => {
      setShowWelcome(false);
    }, 5000);
    
    return () => clearTimeout(timer);
  }, []);

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
                          <h3 className="mb-0">2</h3>
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
                        <option value="1">Akkent Sitesi</option>
                        <option value="2">Göl Rezidans</option>
                      </select>
                      <button className="btn btn-primary" type="button">
                        <i className="bi bi-plus-lg"></i> {t.newProperty}
                      </button>
                    </div>
                  </div>

                  {/* Modüller */}
                  {selectedProperty ? (
                    <div className="mt-4 mb-2">
                      <PropertyModules />
                    </div>
                  ) : (
                    <div className="text-center py-5 text-muted">
                      <i className="bi bi-building fs-1 mb-3 d-block"></i>
                      <h5>{t.pleaseSelectProperty}</h5>
                      <p className="mb-0">{t.propertyModulesInfo}</p>
                    </div>
                  )}
                </div>
              </div>

              {/* Carousel in a card */}
              <div className="card border-0 shadow-sm">
                <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                  <h5 className="card-title mb-0">{t.propertyImages}</h5>
                  <button className="btn btn-sm btn-outline-primary">{t.allImages}</button>
                </div>
                <div className="card-body p-0">
                  <div id="carouselExample" className="carousel slide" data-bs-ride="carousel">
                    <div className="carousel-indicators">
                      <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="0" className="active" aria-current="true" aria-label="Slide 1"></button>
                      <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="1" aria-label="Slide 2"></button>
                      <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="2" aria-label="Slide 3"></button>
                    </div>
                    <div className="carousel-inner">
                      <div className="carousel-item active">
                        <img src="https://images.unsplash.com/photo-1560518883-ce09059eeffa?q=80&w=1000&auto=format&fit=crop" className="d-block w-100" style={{ height: '400px', objectFit: 'cover' }} alt="Site Görüntüsü 1" />
                        <div className="carousel-caption d-none d-md-block bg-dark bg-opacity-50 rounded p-2">
                          <h5>Akkent Sitesi</h5>
                          <p>{t.modernLivingSpaces || 'Modern living spaces and social facilities'}</p>
                        </div>
                      </div>
                      <div className="carousel-item">
                        <img src="https://images.unsplash.com/photo-1580587771525-78b9dba3b914?q=80&w=1000&auto=format&fit=crop" className="d-block w-100" style={{ height: '400px', objectFit: 'cover' }} alt="Site Görüntüsü 2" />
                        <div className="carousel-caption d-none d-md-block bg-dark bg-opacity-50 rounded p-2">
                          <h5>Göl Rezidans</h5>
                          <p>{t.luxuryLiving || 'Luxury living and comfort combined'}</p>
                        </div>
                      </div>
                      <div className="carousel-item">
                        <img src="https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=1000&auto=format&fit=crop" className="d-block w-100" style={{ height: '400px', objectFit: 'cover' }} alt="Site Görüntüsü 3" />
                        <div className="carousel-caption d-none d-md-block bg-dark bg-opacity-50 rounded p-2">
                          <h5>{t.associationCenter || 'Association Center'}</h5>
                          <p>{t.managementMeetingAreas || 'Management and meeting areas'}</p>
                        </div>
                      </div>
                    </div>
                    <button className="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
                      <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                      <span className="visually-hidden">{t.previous}</span>
                    </button>
                    <button className="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
                      <span className="carousel-control-next-icon" aria-hidden="true"></span>
                      <span className="visually-hidden">{t.next}</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </AuthGuard>
  );
}