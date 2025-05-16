'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from './context/AuthContext';

export default function RootPage() {
  const router = useRouter();
  const { user, loading } = useAuth();

  useEffect(() => {
    if (!loading) {
      if (user) {
        // If user is already logged in, redirect to dashboard
        router.push('/dashboard');
      } else {
        // If user is not logged in, redirect to login page
        router.push('/auth/login');
      }
    }
  }, [user, loading, router]);

  // Show loading spinner while checking authentication status
  return (
    <div className="d-flex justify-content-center align-items-center vh-100">
      <div className="spinner-border text-primary" role="status">
        <span className="visually-hidden">Yükleniyor...</span>
      </div>
    </div>
  );
}