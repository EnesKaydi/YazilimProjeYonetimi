'use client';

import { useState } from 'react';
import { useLanguage } from '../context/LanguageContext';

export default function PropertyForm({ onSave, onCancel, initialData = null }) {
  const { translations: t } = useLanguage();
  const [formData, setFormData] = useState(initialData || {
    name: '',
    address: '',
    title: '',
    associationName: '',
    city: '',
    currency: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <div className="card border-0 shadow-sm mb-4">
      <div className="card-header bg-white py-3">
        <h5 className="card-title mb-0">
          {initialData ? t.editProperty || 'Edit Property' : t.newProperty}
        </h5>
      </div>
      <div className="card-body">
        <form onSubmit={handleSubmit}>
          <div className="row">
            <div className="col-md-6">
              <div className="mb-3">
                <label htmlFor="propertyName" className="form-label">{t.propertyName}</label>
                <input 
                  type="text" 
                  className="form-control" 
                  id="propertyName" 
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder={`${t.propertyName}...`} 
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="propertyAddress" className="form-label">{t.address}</label>
                <textarea 
                  className="form-control" 
                  id="propertyAddress" 
                  name="address"
                  value={formData.address}
                  onChange={handleChange}
                  rows="3" 
                  placeholder={`${t.address}...`}
                  required
                ></textarea>
              </div>
              <div className="mb-3">
                <label htmlFor="propertyTitle" className="form-label">{t.propertyTitle}</label>
                <input 
                  type="text" 
                  className="form-control" 
                  id="propertyTitle" 
                  name="title"
                  value={formData.title}
                  onChange={handleChange}
                  placeholder={`${t.propertyTitle}...`} 
                />
              </div>
            </div>
            <div className="col-md-6">
              <div className="mb-3">
                <label htmlFor="associationName" className="form-label">{t.associationName}</label>
                <input 
                  type="text" 
                  className="form-control" 
                  id="associationName" 
                  name="associationName"
                  value={formData.associationName}
                  onChange={handleChange}
                  placeholder={`${t.associationName}...`} 
                />
              </div>
              <div className="mb-3">
                <label htmlFor="propertyCity" className="form-label">{t.city}</label>
                <input 
                  type="text" 
                  className="form-control" 
                  id="propertyCity" 
                  name="city"
                  value={formData.city}
                  onChange={handleChange}
                  placeholder={`${t.city}...`} 
                />
              </div>
              <div className="mb-3">
                <label htmlFor="propertyCurrency" className="form-label">{t.currency}</label>
                <select 
                  className="form-select" 
                  id="propertyCurrency"
                  name="currency"
                  value={formData.currency}
                  onChange={handleChange}
                  required
                >
                  <option value="">{t.select}</option>
                  <option value="USD">{t.usDollar}</option>
                  <option value="EUR">{t.euro}</option>
                  <option value="GBP">{t.britishPound}</option>
                </select>
              </div>
            </div>
          </div>
          <div className="d-flex justify-content-end mt-3">
            <button 
              type="button" 
              className="btn btn-outline-secondary me-2" 
              onClick={onCancel}
            >
              {t.cancel || 'Cancel'}
            </button>
            <button type="submit" className="btn btn-primary">
              {t.save}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}