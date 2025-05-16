'use client';

import { useState } from 'react';
import { useLanguage } from '../context/LanguageContext';

export default function UnitTypeModule() {
  const { translations: t } = useLanguage();
  const [unitTypes, setUnitTypes] = useState([
    { id: 1, name: '1+1', area: 65, sharePercentage: 5, monthlyFee: 150, description: 'Small apartment with one bedroom' },
    { id: 2, name: '2+1', area: 85, sharePercentage: 7, monthlyFee: 200, description: 'Medium apartment with two bedrooms' },
    { id: 3, name: '3+1', area: 120, sharePercentage: 10, monthlyFee: 300, description: 'Large apartment with three bedrooms' },
    { id: 4, name: 'Garage', area: 20, sharePercentage: 2, monthlyFee: 50, description: 'Parking space for vehicles' }
  ]);
  const [newUnitType, setNewUnitType] = useState({ 
    name: '', 
    area: '', 
    sharePercentage: '', 
    monthlyFee: '', 
    description: '' 
  });
  const [isAdding, setIsAdding] = useState(false);
  const [editingId, setEditingId] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewUnitType(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleAddUnitType = () => {
    if (!newUnitType.name || !newUnitType.area || !newUnitType.sharePercentage || !newUnitType.monthlyFee) {
      return; // Basic validation
    }

    if (editingId !== null) {
      // Update existing unit type
      setUnitTypes(prev => prev.map(ut => 
        ut.id === editingId ? { 
          ...ut, 
          name: newUnitType.name, 
          area: parseFloat(newUnitType.area), 
          sharePercentage: parseFloat(newUnitType.sharePercentage),
          monthlyFee: parseFloat(newUnitType.monthlyFee),
          description: newUnitType.description
        } : ut
      ));
      setEditingId(null);
    } else {
      // Add new unit type
      const newId = unitTypes.length > 0 ? Math.max(...unitTypes.map(ut => ut.id)) + 1 : 1;
      
      setUnitTypes(prev => [
        ...prev,
        { 
          id: newId, 
          name: newUnitType.name, 
          area: parseFloat(newUnitType.area), 
          sharePercentage: parseFloat(newUnitType.sharePercentage),
          monthlyFee: parseFloat(newUnitType.monthlyFee),
          description: newUnitType.description
        }
      ]);
    }
    
    // Reset form
    setNewUnitType({ name: '', area: '', sharePercentage: '', monthlyFee: '', description: '' });
    setIsAdding(false);
  };

  const handleEditUnitType = (id) => {
    const unitType = unitTypes.find(ut => ut.id === id);
    if (unitType) {
      setNewUnitType({
        name: unitType.name,
        area: unitType.area.toString(),
        sharePercentage: unitType.sharePercentage.toString(),
        monthlyFee: unitType.monthlyFee.toString(),
        description: unitType.description || ''
      });
      setEditingId(id);
      setIsAdding(true);
    }
  };

  const handleDeleteUnitType = (id) => {
    setUnitTypes(prev => prev.filter(ut => ut.id !== id));
  };

  const handleCancelEdit = () => {
    setNewUnitType({ name: '', area: '', sharePercentage: '', monthlyFee: '', description: '' });
    setEditingId(null);
    setIsAdding(false);
  };

  return (
    <div className="unit-type-module">
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
          <h5 className="card-title mb-0">{t.unitTypes}</h5>
          <button 
            className="btn btn-primary btn-sm" 
            onClick={() => setIsAdding(true)}
            disabled={isAdding}
          >
            <i className="bi bi-plus-circle me-2"></i>
            {t.addNewUnitType}
          </button>
        </div>
        <div className="card-body">
          {isAdding && (
            <div className="mb-4 p-3 border rounded bg-light">
              <h6 className="mb-3">{editingId !== null ? t.editUnitType || 'Edit Unit Type' : t.addNewUnitType}</h6>
              <div className="row g-3">
                <div className="col-md-6">
                  <label htmlFor="unitTypeName" className="form-label">{t.unitTypeName || 'Unit Type Name'}</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    id="unitTypeName" 
                    name="name"
                    value={newUnitType.name}
                    onChange={handleInputChange}
                    placeholder="e.g. 2+1, Studio, Garage, etc."
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="unitTypeArea" className="form-label">{t.area || 'Area'} (m²)</label>
                  <input 
                    type="number" 
                    className="form-control" 
                    id="unitTypeArea" 
                    name="area"
                    value={newUnitType.area}
                    onChange={handleInputChange}
                    placeholder="e.g. 85"
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="unitTypeShare" className="form-label">{t.share || 'Share'} (%)</label>
                  <input 
                    type="number" 
                    className="form-control" 
                    id="unitTypeShare" 
                    name="sharePercentage"
                    value={newUnitType.sharePercentage}
                    onChange={handleInputChange}
                    placeholder="e.g. 7.5"
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="unitTypeMonthlyFee" className="form-label">{t.monthlyFee || 'Monthly Fee'}</label>
                  <input 
                    type="number" 
                    className="form-control" 
                    id="unitTypeMonthlyFee" 
                    name="monthlyFee"
                    value={newUnitType.monthlyFee}
                    onChange={handleInputChange}
                    placeholder="e.g. 200"
                  />
                </div>
                <div className="col-12">
                  <label htmlFor="unitTypeDescription" className="form-label">{t.description || 'Description'}</label>
                  <textarea 
                    className="form-control" 
                    id="unitTypeDescription" 
                    name="description"
                    value={newUnitType.description}
                    onChange={handleInputChange}
                    rows="3"
                    placeholder={t.unitTypeDescriptionPlaceholder || "Describe the unit type..."}
                  ></textarea>
                </div>
              </div>
              <div className="mt-3 d-flex justify-content-end">
                <button 
                  className="btn btn-outline-secondary me-2" 
                  onClick={handleCancelEdit}
                >
                  {t.cancel || 'Cancel'}
                </button>
                <button 
                  className="btn btn-primary" 
                  onClick={handleAddUnitType}
                >
                  {editingId !== null ? t.update || 'Update' : t.save}
                </button>
              </div>
            </div>
          )}

          {unitTypes.length > 0 ? (
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>{t.unitTypeName}</th>
                    <th>{t.area} (m²)</th>
                    <th>{t.share} (%)</th>
                    <th>{t.monthlyFee}</th>
                    <th>{t.description}</th>
                    <th>{t.actions}</th>
                  </tr>
                </thead>
                <tbody>
                  {unitTypes.map(unitType => (
                    <tr key={unitType.id}>
                      <td>{unitType.id}</td>
                      <td>{unitType.name}</td>
                      <td>{unitType.area}</td>
                      <td>{unitType.sharePercentage}%</td>
                      <td>{unitType.monthlyFee}</td>
                      <td className="text-truncate" style={{ maxWidth: '200px' }}>{unitType.description}</td>
                      <td>
                        <div className="btn-group">
                          <button 
                            className="btn btn-sm btn-outline-primary me-2"
                            onClick={() => handleEditUnitType(unitType.id)}
                            title={t.edit || 'Edit'}
                          >
                            <i className="bi bi-pencil me-1"></i> {t.edit || 'Edit'}
                          </button>
                          <button 
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => handleDeleteUnitType(unitType.id)}
                            title={t.delete || 'Delete'}
                          >
                            <i className="bi bi-trash me-1"></i> {t.delete || 'Delete'}
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="text-center py-5">
              <div className="mb-4">
                <i className="bi bi-grid fs-1 text-muted"></i>
              </div>
              <h5 className="text-muted">{t.noUnitTypesYet || 'No unit types defined yet'}</h5>
              <p className="text-muted">{t.addUnitTypesDescription || 'Add unit types to define different apartment configurations'}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}