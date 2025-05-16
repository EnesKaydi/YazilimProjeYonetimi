'use client';

import { useLanguage } from '../context/LanguageContext';
import { useEffect } from 'react';

export default function LanguageWrapper({ children }) {
  const { language } = useLanguage();

  useEffect(() => {
    // Set the HTML lang attribute based on the selected language
    document.documentElement.lang = language;
  }, [language]);

  return children;
}