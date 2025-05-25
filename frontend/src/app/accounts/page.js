'use client';

import { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import AuthGuard from '../components/AuthGuard';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import LanguageSwitcher from '../components/LanguageSwitcher';
import AccountsModule from '../components/AccountsModule';

// Add this import at the top
import { useRouter } from 'next/navigation';

export default function AccountsPage() {
  const { user, loading } = useAuth();
  const router = useRouter();
  const { translations: t } = useLanguage();

  useEffect(() => {
    if (!loading && !user) {
      router.push('/auth/login');
    }
  }, [user, loading, router]);

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
                <h5 className="mb-0 text-primary fw-bold">{t.accountsManagement || 'Accounts Management'}</h5>
                <div className="ms-auto">
                  <LanguageSwitcher />
                </div>
              </div>
            </nav>

            {/* Main Content Area */}
            <div className="p-4">
              <AccountsModule />
            </div>
          </div>
        </div>
      </div>
    </AuthGuard>
  );
}