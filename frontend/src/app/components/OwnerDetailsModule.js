'use client';

import { useState, useEffect } from 'react';
import { useLanguage } from '../context/LanguageContext';

export default function OwnerDetailsModule() {
  const { translations: t } = useLanguage();
  
  // BACKEND CONNECTION NEEDED: Fetch unit types and owners from backend
  // const [unitTypes, setUnitTypes] = useState([]);
  // const [owners, setOwners] = useState([]);
  // const [loading, setLoading] = useState(true);
  // 
  // useEffect(() => {
  //   const fetchData = async () => {
  //     try {
  //       const propertyId = localStorage.getItem('selectedPropertyId');
  //       
  //       // Fetch unit types
  //       const unitTypesResponse = await fetch(`/api/properties/${propertyId}/unit-types`, {
  //         headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  //       });
  //       const unitTypesData = await unitTypesResponse.json();
  //       
  //       // Fetch owners
  //       const ownersResponse = await fetch(`/api/properties/${propertyId}/owners`, {
  //         headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  //       });
  //       const ownersData = await ownersResponse.json();
  //       
  //       if (unitTypesData.success) {
  //         setUnitTypes(unitTypesData.unitTypes);
  //       }
  //       
  //       if (ownersData.success) {
  //         setOwners(ownersData.owners);
  //       }
  //     } catch (error) {
  //       console.error('Failed to fetch data:', error);
  //     } finally {
  //       setLoading(false);
  //     }
  //   };
  //   
  //   fetchData();
  // }, []);
  
  // Sample unit types (in a real app, this would come from your UnitTypeModule)
  const [unitTypes, setUnitTypes] = useState([
    { id: 1, name: '1+1' },
    { id: 2, name: '2+1' },
    { id: 3, name: '3+1' },
    { id: 4, name: 'Garage' }
  ]);
  
  // Sample owners data
  const [owners, setOwners] = useState([
    { 
      id: 1, 
      fullName: 'John Doe', 
      address: '123 Main St, Apt 4B', 
      phone: '+1 555-123-4567', 
      email: 'john@example.com',
      bankAccount: 'TR123456789012345678901234',
      unitTypeId: 2,
      titleNumber: 'A12345',
      isRented: true,
      renter: {
        fullName: 'Alice Smith',
        address: '123 Main St, Apt 4B',
        phone: '+1 555-987-6543',
        email: 'alice@example.com',
        bankAccount: 'TR987654321098765432109876'
      },
      showRenterInSystem: true
    },
    { 
      id: 2, 
      fullName: 'Jane Smith', 
      address: '456 Oak Ave, Suite 7C', 
      phone: '+1 555-987-6543', 
      email: 'jane@example.com',
      bankAccount: 'TR234567890123456789012345',
      unitTypeId: 3,
      titleNumber: 'B67890',
      isRented: false,
      renter: null,
      showRenterInSystem: false
    }
  ]);
  
  // Form state
  const emptyRenter = {
    fullName: '',
    address: '',
    phone: '',
    email: '',
    bankAccount: ''
  };
  
  const emptyOwner = {
    id: null,
    fullName: '',
    address: '',
    phone: '',
    email: '',
    bankAccount: '',
    unitTypeId: '',
    titleNumber: '',
    isRented: false,
    renter: null,
    showRenterInSystem: false
  };
  
  const [currentOwner, setCurrentOwner] = useState({ ...emptyOwner });
  const [isEditing, setIsEditing] = useState(false);
  const [isAdding, setIsAdding] = useState(false);
  
  // Handle form input changes for owner
  const handleOwnerInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    
    if (type === 'checkbox') {
      // Handle checkbox input
      setCurrentOwner(prev => ({
        ...prev,
        [name]: checked
      }));
      
      // If isRented is unchecked, reset renter data
      if (name === 'isRented' && !checked) {
        setCurrentOwner(prev => ({
          ...prev,
          renter: null,
          showRenterInSystem: false
        }));
      } else if (name === 'isRented' && checked) {
        // Initialize renter object when isRented is checked
        setCurrentOwner(prev => ({
          ...prev,
          renter: { ...emptyRenter }
        }));
      }
    } else {
      // Handle other input types
      setCurrentOwner(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };
  
  // Handle form input changes for renter
  const handleRenterInputChange = (e) => {
    const { name, value } = e.target;
    
    setCurrentOwner(prev => ({
      ...prev,
      renter: {
        ...prev.renter,
        [name]: value
      }
    }));
  };
  
  // Save owner (create or update)
  const handleSaveOwner = () => {
    if (!currentOwner.fullName || !currentOwner.unitTypeId) {
      // Basic validation
      return;
    }
    
    if (isEditing) {
      // Update existing owner
      setOwners(prev => prev.map(owner => 
        owner.id === currentOwner.id ? currentOwner : owner
      ));
    } else {
      // Create new owner
      const newId = owners.length > 0 ? Math.max(...owners.map(owner => owner.id)) + 1 : 1;
      setOwners(prev => [...prev, { ...currentOwner, id: newId }]);
    }
    
    // Reset form and state
    setCurrentOwner({ ...emptyOwner });
    setIsEditing(false);
    setIsAdding(false);
  };
  
  // Edit owner
  const handleEditOwner = (id) => {
    const ownerToEdit = owners.find(owner => owner.id === id);
    if (ownerToEdit) {
      setCurrentOwner({ ...ownerToEdit });
      setIsEditing(true);
      setIsAdding(true);
    }
  };
  
  // Delete owner
  const handleDeleteOwner = (id) => {
    if (confirm(t.confirmDelete || 'Are you sure you want to delete this owner?')) {
      setOwners(prev => prev.filter(owner => owner.id !== id));
    }
  };
  
  // Duplicate owner
  const handleDuplicateOwner = (id) => {
    const ownerToDuplicate = owners.find(owner => owner.id === id);
    if (ownerToDuplicate) {
      const newId = owners.length > 0 ? Math.max(...owners.map(owner => owner.id)) + 1 : 1;
      const duplicatedOwner = {
        ...ownerToDuplicate,
        id: newId,
        fullName: `${ownerToDuplicate.fullName} (${t.copy || 'Copy'})`,
        titleNumber: `${ownerToDuplicate.titleNumber}-${t.copy || 'Copy'}`
      };
      
      setOwners(prev => [...prev, duplicatedOwner]);
    }
  };
  
  // Cancel editing
  const handleCancelEdit = () => {
    setCurrentOwner({ ...emptyOwner });
    setIsEditing(false);
    setIsAdding(false);
  };
  
  // Get unit type name by id
  const getUnitTypeName = (id) => {
    const unitType = unitTypes.find(ut => ut.id === parseInt(id));
    return unitType ? unitType.name : '';
  };
  
  return (
    <div className="owner-details-module">
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
          <h5 className="card-title mb-0">{t.ownerDetails}</h5>
        </div>
        <div className="card-body">
          {isAdding && (
            <div className="mb-4 p-3 border rounded bg-light">
              <h6 className="mb-3">{isEditing ? t.editOwner : t.addNewOwner}</h6>
              <div className="row g-3">
                <div className="col-md-6">
                  <label htmlFor="ownerFullName" className="form-label">{t.fullName}</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    id="ownerFullName" 
                    name="fullName"
                    value={currentOwner.fullName}
                    onChange={handleOwnerInputChange}
                    required
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="ownerUnitType" className="form-label">{t.unitType || 'Unit Type'}</label>
                  <select 
                    className="form-select" 
                    id="ownerUnitType" 
                    name="unitTypeId"
                    value={currentOwner.unitTypeId}
                    onChange={handleOwnerInputChange}
                    required
                  >
                    <option value="">{t.select}</option>
                    {unitTypes.map(unitType => (
                      <option key={unitType.id} value={unitType.id}>{unitType.name}</option>
                    ))}
                  </select>
                </div>
                <div className="col-md-6">
                  <label htmlFor="ownerTitleNumber" className="form-label">{t.titleNumber || 'Title Number'}</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    id="ownerTitleNumber" 
                    name="titleNumber"
                    value={currentOwner.titleNumber}
                    onChange={handleOwnerInputChange}
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="ownerAddress" className="form-label">{t.address}</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    id="ownerAddress" 
                    name="address"
                    value={currentOwner.address}
                    onChange={handleOwnerInputChange}
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="ownerPhone" className="form-label">{t.phone || 'Phone'}</label>
                  <input 
                    type="tel" 
                    className="form-control" 
                    id="ownerPhone" 
                    name="phone"
                    value={currentOwner.phone}
                    onChange={handleOwnerInputChange}
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="ownerEmail" className="form-label">{t.email}</label>
                  <input 
                    type="email" 
                    className="form-control" 
                    id="ownerEmail" 
                    name="email"
                    value={currentOwner.email}
                    onChange={handleOwnerInputChange}
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="ownerBankAccount" className="form-label">{t.bankAccount || 'Bank Account'}</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    id="ownerBankAccount" 
                    name="bankAccount"
                    value={currentOwner.bankAccount}
                    onChange={handleOwnerInputChange}
                  />
                </div>
                <div className="col-md-6">
                  <div className="form-check mt-4">
                    <input 
                      className="form-check-input" 
                      type="checkbox" 
                      id="isRented" 
                      name="isRented"
                      checked={currentOwner.isRented}
                      onChange={handleOwnerInputChange}
                    />
                    <label className="form-check-label" htmlFor="isRented">
                      {t.isRented || 'Property is Rented'}
                    </label>
                  </div>
                </div>
                
                {currentOwner.isRented && (
                  <div className="col-md-6">
                    <div className="form-check mt-4">
                      <input 
                        className="form-check-input" 
                        type="checkbox" 
                        id="showRenterInSystem" 
                        name="showRenterInSystem"
                        checked={currentOwner.showRenterInSystem}
                        onChange={handleOwnerInputChange}
                      />
                      <label className="form-check-label" htmlFor="showRenterInSystem">
                        {t.showRenterInSystem || 'Show Renter in System (instead of Owner)'}
                      </label>
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}

          {owners.length > 0 ? (
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>{t.fullName}</th>
                    <th>{t.unitType}</th>
                    <th>{t.titleNumber}</th>
                    <th>{t.contact}</th>
                    <th>{t.status}</th>
                    <th>{t.actions}</th>
                  </tr>
                </thead>
                <tbody>
                  {owners.map(owner => (
                    <tr key={owner.id}>
                      <td>{owner.id}</td>
                      <td>
                        {owner.isRented && owner.showRenterInSystem 
                          ? <span className="text-success">{owner.renter?.fullName}</span> 
                          : owner.fullName}
                      </td>
                      <td>{getUnitTypeName(owner.unitTypeId)}</td>
                      <td>{owner.titleNumber}</td>
                      <td>
                        {owner.isRented && owner.showRenterInSystem 
                          ? owner.renter?.phone || owner.renter?.email
                          : owner.phone || owner.email}
                      </td>
                      <td>
                        {owner.isRented 
                          ? <span className="badge bg-info">{t.rented || 'Rented'}</span>
                          : <span className="badge bg-success">{t.ownerOccupied || 'Owner Occupied'}</span>}
                      </td>
                      <td>
                        <div className="btn-group">
                          <button 
                            className="btn btn-sm btn-outline-primary me-2"
                            onClick={() => handleEditOwner(owner.id)}
                            title={t.edit || 'Edit'}
                          >
                            <i className="bi bi-pencil me-1"></i> {t.edit || 'Edit'}
                          </button>
                          <button 
                            className="btn btn-sm btn-outline-secondary me-2"
                            onClick={() => handleDuplicateOwner(owner.id)}
                            title={t.duplicate || 'Duplicate'}
                          >
                            <i className="bi bi-copy me-1"></i> {t.duplicate || 'Duplicate'}
                          </button>
                          <button 
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => handleDeleteOwner(owner.id)}
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
                <i className="bi bi-people fs-1 text-muted"></i>
              </div>
              <h5 className="text-muted">{t.noOwnersYet || 'No owners added yet'}</h5>
              <p className="text-muted">{t.addOwnersDescription || 'Add property owners to manage their information'}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}