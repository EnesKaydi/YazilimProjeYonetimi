'use client';

import { useLanguage } from '../context/LanguageContext';

export default function LanguageSwitcher() {
  const { language, changeLanguage } = useLanguage();

  return (
    <div className="dropdown">
      <button 
        className="btn btn-sm btn-outline-secondary dropdown-toggle" 
        type="button" 
        id="languageDropdown" 
        data-bs-toggle="dropdown" 
        aria-expanded="false"
      >
        {language === 'en' ? '🇬🇧 English' : '🇫🇷 Français'}
      </button>
      <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="languageDropdown">
        <li>
          <button 
            className={`dropdown-item ${language === 'en' ? 'active' : ''}`} 
            onClick={() => changeLanguage('en')}
          >
            🇬🇧 English
          </button>
        </li>
        <li>
          <button 
            className={`dropdown-item ${language === 'fr' ? 'active' : ''}`} 
            onClick={() => changeLanguage('fr')}
          >
            🇫🇷 Français
          </button>
        </li>
      </ul>
    </div>
  );
}