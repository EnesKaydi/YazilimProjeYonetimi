'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useLanguage } from '../context/LanguageContext';
import UnitTypeModule from './UnitTypeModule';
import OwnerDetailsModule from './OwnerDetailsModule';
import RegularBudgetModule from './RegularBudgetModule';

export default function PropertyModules() {
  const [activeTab, setActiveTab] = useState('details');
  const { translations: t } = useLanguage();
  
  // Sample property data for the RegularBudgetModule
  const sampleProperty = {
    currency: 'EUR'
  };

  return (
    <div className="property-modules">
      {/* Modül Navigasyonu */}
      <ul className="nav nav-tabs mb-4 justify-content-center">
        <li className="nav-item">
          <button 
            className={`nav-link ${activeTab === 'details' ? 'active' : ''}`}
            onClick={() => setActiveTab('details')}
          >
            <i className="bi bi-info-circle me-2"></i>
            {t.propertyDetails}
          </button>
        </li>
        <li className="nav-item">
          <button 
            className={`nav-link ${activeTab === 'units' ? 'active' : ''}`}
            onClick={() => setActiveTab('units')}
          >
            <i className="bi bi-grid me-2"></i>
            {t.unitTypes}
          </button>
        </li>
        <li className="nav-item">
          <button 
            className={`nav-link ${activeTab === 'owners' ? 'active' : ''}`}
            onClick={() => setActiveTab('owners')}
          >
            <i className="bi bi-people me-2"></i>
            {t.ownerDetails}
          </button>
        </li>
        <li className="nav-item">
          <button 
            className={`nav-link ${activeTab === 'budget' ? 'active' : ''}`}
            onClick={() => setActiveTab('budget')}
          >
            <i className="bi bi-cash-coin me-2"></i>
            {t.regularBudget}
          </button>
        </li>
      </ul>

      {/* Modül İçeriği */}
      <div className="tab-content">
        {/* Mülk Detayları */}
        {activeTab === 'details' && (
          <div className="tab-pane fade show active">
            <div className="row">
              <div className="col-md-6">
                <div className="card border-0 shadow-sm mb-4">
                  <div className="card-header bg-white py-3">
                    <h5 className="card-title mb-0">{t.propertyInfo}</h5>
                  </div>
                  <div className="card-body">
                    <form>
                      <div className="mb-3">
                        <label htmlFor="propertyName" className="form-label">{t.propertyName}</label>
                        <input type="text" className="form-control" id="propertyName" placeholder={`${t.propertyName}...`} />
                      </div>
                      <div className="mb-3">
                        <label htmlFor="propertyAddress" className="form-label">{t.address}</label>
                        <textarea className="form-control" id="propertyAddress" rows="3" placeholder={`${t.address}...`}></textarea>
                      </div>
                      <div className="mb-3">
                        <label htmlFor="propertyTitle" className="form-label">{t.propertyTitle}</label>
                        <input type="text" className="form-control" id="propertyTitle" placeholder={`${t.propertyTitle}...`} />
                      </div>
                      <div className="mb-3">
                        <label htmlFor="associationName" className="form-label">{t.associationName}</label>
                        <input type="text" className="form-control" id="associationName" placeholder={`${t.associationName}...`} />
                      </div>
                      <div className="d-grid">
                        <button type="submit" className="btn btn-primary">{t.save}</button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
              <div className="col-md-6">
                <div className="card border-0 shadow-sm mb-4">
                  <div className="card-header bg-white py-3">
                    <h5 className="card-title mb-0">{t.locationAndCurrency}</h5>
                  </div>
                  <div className="card-body">
                    <form>
                      <div className="mb-3">
                        <label htmlFor="propertyCity" className="form-label">{t.city}</label>
                        <input type="text" className="form-control" id="propertyCity" placeholder={`${t.city}...`} />
                      </div>
                      <div className="mb-3">
                        <label htmlFor="propertyCurrency" className="form-label">{t.currency}</label>
                        <select className="form-select" id="propertyCurrency">
                          <option value="">{t.select}</option>
                          <option value="USD">{t.usDollar}</option>
                          <option value="EUR">{t.euro}</option>
                          <option value="GBP">{t.britishPound}</option>
                        </select>
                      </div>
                      <div className="d-grid">
                        <button type="submit" className="btn btn-primary">{t.save}</button>
                      </div>
                    </form>
                  </div>
                </div>
                <div className="card border-0 shadow-sm">
                  <div className="card-header bg-white py-3">
                    <h5 className="card-title mb-0">{t.propertySummary}</h5>
                  </div>
                  <div className="card-body">
                    <div className="d-flex align-items-center mb-3">
                      <div className="bg-primary bg-opacity-10 p-3 rounded me-3">
                        <i className="bi bi-house-door text-primary fs-4"></i>
                      </div>
                      <div>
                        <h6 className="mb-0 text-muted">{t.totalUnits}</h6>
                        <h3 className="mb-0">24</h3>
                      </div>
                    </div>
                    <div className="d-flex align-items-center">
                      <div className="bg-success bg-opacity-10 p-3 rounded me-3">
                        <i className="bi bi-people text-success fs-4"></i>
                      </div>
                      <div>
                        <h6 className="mb-0 text-muted">{t.totalOwners}</h6>
                        <h3 className="mb-0">18</h3>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Unit Types Tab */}
        {activeTab === 'units' && (
          <div className="tab-pane fade show active">
            <UnitTypeModule />
          </div>
        )}

        {/* Owner Details Tab */}
        {activeTab === 'owners' && (
          <div className="tab-pane fade show active">
            <OwnerDetailsModule />
          </div>
        )}
        
        {/* Regular Budget Tab */}
        {activeTab === 'budget' && (
          <div className="tab-pane fade show active">
            <RegularBudgetModule property={sampleProperty} />
          </div>
        )}

        {/* Other tabs except 'details', 'units', 'owners', and 'budget' */}
        {activeTab !== 'details' && activeTab !== 'units' && activeTab !== 'owners' && activeTab !== 'budget' && (
          <div className="text-center py-5">
            <div className="mb-4">
              <i className={`bi ${
                activeTab === 'exceptional' ? 'bi-cash-stack' : 
                activeTab === 'reports' ? 'bi-bar-chart' : 
                'bi-file-earmark-text'
              } fs-1 text-primary`}></i>
            </div>
            <h4>{
              activeTab === 'exceptional' ? t.exceptionalBudget : 
              activeTab === 'reports' ? t.reports : 
              t.documents
            }</h4>
            <p className="text-muted">{t.inDevelopment}</p>
            <button className="btn btn-primary mt-3">
              <i className="bi bi-plus-circle me-2"></i>
              {
                activeTab === 'exceptional' ? t.createExceptionalBudget : 
                activeTab === 'reports' ? t.generateReport : 
                t.uploadDocument
              }
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
  