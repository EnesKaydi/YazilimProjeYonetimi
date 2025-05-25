'use client';

import { createContext, useContext, useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    // BACKEND CONNECTION NEEDED: Verify token validity with backend
    // Check if user is logged in
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    const userData = localStorage.getItem('user');
    const token = localStorage.getItem('token'); // Add token storage
    
    if (isLoggedIn && userData && token) {
      // Verify token with backend
      // const verifyToken = async () => {
      //   try {
      //     const response = await fetch('/api/auth/verify', {
      //       headers: { 'Authorization': `Bearer ${token}` }
      //     });
      //     const data = await response.json();
      //     if (data.valid) {
      //       setUser(JSON.parse(userData));
      //     } else {
      //       logout(); // Token invalid, log out user
      //     }
      //   } catch (error) {
      //     console.error('Token verification failed:', error);
      //   } finally {
      //     setLoading(false);
      //   }
      // };
      // verifyToken();
      
      setUser(JSON.parse(userData));
    }
    
    setLoading(false);
  }, []);

  const login = (userData) => {
    // BACKEND CONNECTION NEEDED: Store authentication token from backend response
    // Set user data in localStorage
    localStorage.setItem('isLoggedIn', 'true');
    localStorage.setItem('user', JSON.stringify(userData));
    // localStorage.setItem('token', userData.token); // Store authentication token
    setUser(userData);
    router.push('/dashboard');
  };

  const logout = () => {
    // Clear user data from localStorage
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('user');
    setUser(null);
    router.push('/auth/login');
  };

  const updateUser = (userData) => {
    // Update user data in localStorage
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout, updateUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}