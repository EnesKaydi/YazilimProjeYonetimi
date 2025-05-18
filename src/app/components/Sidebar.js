'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';

export default function Sidebar() {
  const [expandedMenu, setExpandedMenu] = useState('ayarlar');
  const { user, logout } = useAuth();
  const { translations: t } = useLanguage();

  const toggleMenu = (menu) => {
    if (expandedMenu === menu) {
      setExpandedMenu(null);
    } else {
      setExpandedMenu(menu);
    }
  };

  return (
    <div className="d-flex flex-column vh-100 position-fixed" style={{ width: 'inherit', maxWidth: 'inherit' }}>
      

      {/* User Profile Section */}
      <div className="p-3 border-bottom border-secondary">
        <div className="d-flex align-items-center">
          <div className="bg-primary text-white rounded-circle me-2 d-flex align-items-center justify-content-center" style={{ width: '40px', height: '40px' }}>
            {user?.name?.charAt(0) || 'U'}
          </div>
          <div className="dropdown flex-grow-1">
            <button className="btn btn-dark dropdown-toggle d-flex align-items-center w-100 justify-content-between" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
              <span className="text-truncate">{user?.name || 'User'}</span>
            </button>
            <ul className="dropdown-menu w-100" aria-labelledby="userDropdown">
              <li><Link href="/profile" className="dropdown-item"><i className="bi bi-person me-2"></i>{t.profile}</Link></li>
              <li><hr className="dropdown-divider" /></li>
              <li><button className="dropdown-item text-danger" onClick={logout}><i className="bi bi-box-arrow-right me-2"></i>{t.logout}</button></li>
            </ul>
          </div>
        </div>
      </div>

      {/* Navigation Menu */}
      <div className="flex-grow-1 py-2 overflow-auto">
        <div className="nav flex-column">
          {/* Dashboard */}
          <Link href="/dashboard" className="nav-link text-white py-2 px-3 d-flex align-items-center">
            <i className="bi bi-speedometer2 me-3"></i>
            <span>{t.dashboard}</span>
          </Link>

          {/* Profile Management - Changed to Accounts */}
          <Link href="/accounts" className="nav-link text-white py-2 px-3 d-flex align-items-center">
            <i className="bi bi-people me-3"></i>
            <span>{t.accounts || 'Accounts'}</span>
          </Link>

          {/* Settings */}
          <div className="nav-item">
            <button 
              className={`nav-link text-white py-2 px-3 d-flex align-items-center justify-content-between w-100 border-0 bg-transparent ${expandedMenu === 'ayarlar' ? 'active bg-primary bg-opacity-25' : ''}`}
              onClick={() => toggleMenu('ayarlar')}
            >
              <div>
                <i className="bi bi-gear me-3"></i>
                <span>{t.settings}</span>
              </div>
              <i className={`bi ${expandedMenu === 'ayarlar' ? 'bi-chevron-down' : 'bi-chevron-right'}`}></i>
            </button>
            
            {expandedMenu === 'ayarlar' && (
              <div className="ms-4 mt-1">
                <Link href="#" className="nav-link text-white-50 py-2 ps-4 d-block">
                  <i className="bi bi-cloud-arrow-up me-2"></i> {t.backup}
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="p-3 border-top border-secondary mt-auto">
        <div className="text-center text-white-50 small">
          <p className="mb-0">© 2023 {t.associationManagement}</p>
          <p className="mb-0">v0.1.0</p>
        </div>
      </div>
    </div>
  );
}
  