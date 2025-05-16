'use client';

import { useState, useEffect } from 'react';
import { useLanguage } from '../context/LanguageContext';

export default function RegularBudgetModule({ property }) {
  const { translations: t } = useLanguage();
  const [budgetYear, setBudgetYear] = useState(2025);
  const [budgetData, setBudgetData] = useState({
    previousYearBalance: 0,
    months: Array(12).fill().map((_, i) => ({
      name: new Date(2025, i).toLocaleString('default', { month: 'long' }),
      income: 0,
      outcome: 0
    }))
  });
  
  // Calculate total income, outcome and balance
  const totalIncome = budgetData.months.reduce((sum, month) => sum + month.income, 0);
  const totalOutcome = budgetData.months.reduce((sum, month) => sum + month.outcome, 0);
  const currentBalance = budgetData.previousYearBalance + totalIncome - totalOutcome;
  
  // Handle input changes for income and outcome
  const handleBudgetChange = (index, type, value) => {
    const newValue = parseFloat(value) || 0;
    const updatedMonths = [...budgetData.months];
    updatedMonths[index][type] = newValue;
    setBudgetData({
      ...budgetData,
      months: updatedMonths
    });
  };
  
  // Handle previous year balance change
  const handlePreviousBalanceChange = (value) => {
    const newValue = parseFloat(value) || 0;
    setBudgetData({
      ...budgetData,
      previousYearBalance: newValue
    });
  };
  
  // Change budget year
  const changeYear = (increment) => {
    const newYear = budgetYear + increment;
    if (newYear >= 2025) {
      setBudgetYear(newYear);
      
      // If moving to next year, transfer the balance
      if (increment > 0) {
        setBudgetData({
          previousYearBalance: currentBalance,
          months: Array(12).fill().map((_, i) => ({
            name: new Date(newYear, i).toLocaleString('default', { month: 'long' }),
            income: 0,
            outcome: 0
          }))
        });
      } else {
        // For simplicity in this frontend demo, just reset the data when going back
        setBudgetData({
          previousYearBalance: 0,
          months: Array(12).fill().map((_, i) => ({
            name: new Date(newYear, i).toLocaleString('default', { month: 'long' }),
            income: 0,
            outcome: 0
          }))
        });
      }
    }
  };
  
  // Get currency symbol based on property settings
  const getCurrencySymbol = () => {
    if (!property || !property.currency) return '$';
    
    switch(property.currency) {
      case 'EUR': return '€';
      case 'GBP': return '£';
      default: return '$'; // USD default
    }
  };
  
  return (
    <div className="regular-budget-module">
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
          <h5 className="card-title mb-0">{t.regularBudget}</h5>
          <div className="d-flex align-items-center">
            <button 
              className="btn btn-outline-secondary btn-sm me-2" 
              onClick={() => changeYear(-1)}
              disabled={budgetYear <= 2025}
            >
              <i className="bi bi-chevron-left"></i>
            </button>
            <span className="fw-bold">{budgetYear}</span>
            <button 
              className="btn btn-outline-secondary btn-sm ms-2" 
              onClick={() => changeYear(1)}
            >
              <i className="bi bi-chevron-right"></i>
            </button>
          </div>
        </div>
        <div className="card-body">
          <div className="mb-4">
            <div className="row align-items-center mb-3">
              <div className="col-md-6">
                <h6>{t.previousYearBalance || 'Previous Year Balance'}</h6>
              </div>
              <div className="col-md-6">
                <div className="input-group">
                  <span className="input-group-text">{getCurrencySymbol()}</span>
                  <input 
                    type="number" 
                    className="form-control" 
                    value={budgetData.previousYearBalance}
                    onChange={(e) => handlePreviousBalanceChange(e.target.value)}
                  />
                </div>
              </div>
            </div>
          </div>
          
          <div className="table-responsive">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th>{t.month || 'Month'}</th>
                  <th>{t.income || 'Income'}</th>
                  <th>{t.outcome || 'Outcome'}</th>
                  <th>{t.balance || 'Balance'}</th>
                </tr>
              </thead>
              <tbody>
                {budgetData.months.map((month, index) => {
                  // Calculate running balance up to this month
                  const monthlyIncome = budgetData.months
                    .slice(0, index + 1)
                    .reduce((sum, m) => sum + m.income, 0);
                  
                  const monthlyOutcome = budgetData.months
                    .slice(0, index + 1)
                    .reduce((sum, m) => sum + m.outcome, 0);
                  
                  const runningBalance = budgetData.previousYearBalance + monthlyIncome - monthlyOutcome;
                  
                  return (
                    <tr key={index}>
                      <td>{month.name}</td>
                      <td>
                        <div className="input-group">
                          <span className="input-group-text text-success">+</span>
                          <input 
                            type="number" 
                            className="form-control" 
                            value={month.income}
                            onChange={(e) => handleBudgetChange(index, 'income', e.target.value)}
                          />
                          <span className="input-group-text">{getCurrencySymbol()}</span>
                        </div>
                      </td>
                      <td>
                        <div className="input-group">
                          <span className="input-group-text text-danger">-</span>
                          <input 
                            type="number" 
                            className="form-control" 
                            value={month.outcome}
                            onChange={(e) => handleBudgetChange(index, 'outcome', e.target.value)}
                          />
                          <span className="input-group-text">{getCurrencySymbol()}</span>
                        </div>
                      </td>
                      <td>
                        <span className={runningBalance >= 0 ? 'text-success' : 'text-danger'}>
                          {runningBalance >= 0 ? '+' : ''}{runningBalance.toFixed(2)} {getCurrencySymbol()}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
              <tfoot>
                <tr className="table-light fw-bold">
                  <td>{t.total || 'Total'}</td>
                  <td className="text-success">+{totalIncome.toFixed(2)} {getCurrencySymbol()}</td>
                  <td className="text-danger">-{totalOutcome.toFixed(2)} {getCurrencySymbol()}</td>
                  <td className={currentBalance >= 0 ? 'text-success' : 'text-danger'}>
                    {currentBalance >= 0 ? '+' : ''}{currentBalance.toFixed(2)} {getCurrencySymbol()}
                  </td>
                </tr>
              </tfoot>
            </table>
          </div>
          
          <div className="alert alert-info mt-4">
            <i className="bi bi-info-circle me-2"></i>
            {t.budgetTransferInfo || 'When the year ends, the final balance will be transferred to the next year.'}
          </div>
        </div>
      </div>
    </div>
  );
}