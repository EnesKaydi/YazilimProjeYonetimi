'use client';

import { useState, useEffect } from 'react';
import { useLanguage } from '../context/LanguageContext';

// Varsayılan ID'ler - normalde giriş yapmış kullanıcıdan veya URL'den gelir
const DEFAULT_PROPERTY_ID = 1; 
const DEFAULT_OWNER_ID = 1; // Bu ID, User ID 1'e karşılık gelen Owner kaydının ID'si olmalı

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
  
  // Profil Bilgileri Form State
  const [profileData, setProfileData] = useState({
    fullName: '',
    email: '',
    phoneNumber: '',
    address: '',
    bankAccountNumber: '', // Formda yok ama DTO'da var, gerekirse eklenir
    deedPollNumber: '',    // Formda yok ama DTO'da var, gerekirse eklenir
    isTenantOccupied: false,
    displayAs: 'MAL_SAHIBI', // 'MAL_SAHIBI' or 'KIRACI'
    unitTypeId: '', // Güncelleme için gerekli, fetch ile gelecek
    // tenantDetails: null // Gerekirse TenantUpdateDto yapısında olacak
  });

  // Şifre Değiştirme Form State
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: '',
  });

  const [loadingProfile, setLoadingProfile] = useState(true);
  const [profileError, setProfileError] = useState(null);
  const [passwordError, setPasswordError] = useState(null);
  const [profileSuccessMessage, setProfileSuccessMessage] = useState('');
  const [passwordSuccessMessage, setPasswordSuccessMessage] = useState('');

  // API'den profil bilgilerini çekmek için
  useEffect(() => {
    const fetchProfileData = async () => {
      setLoadingProfile(true);
      setProfileError(null);
      try {
        // const token = localStorage.getItem('token'); // Token varsa
        const response = await fetch(`/api/properties/${DEFAULT_PROPERTY_ID}/owners/${DEFAULT_OWNER_ID}`, {
          // headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `Failed to fetch profile data. Status: ${response.status}`);
        }
        const data = await response.json(); // Bu OwnerDto olmalı
        setProfileData({
          fullName: data.fullName || '',
          email: data.email || '',
          phoneNumber: data.phoneNumber || '',
          address: data.address || '',
          bankAccountNumber: data.bankAccountNumber || '',
          deedPollNumber: data.deedPollNumber || '',
          isTenantOccupied: data.isTenantOccupied || false,
          displayAs: data.displayAs || 'MAL_SAHIBI',
          unitTypeId: data.unitTypeId || '', // Bu çok önemli
          // tenantDetails: data.tenantDetails // Eğer varsa ve güncellenecekse
        });
      } catch (error) {
        console.error('Error fetching profile data:', error);
        setProfileError(error.message);
      } finally {
        setLoadingProfile(false);
      }
    };

    fetchProfileData();
  }, []);

  const handleProfileInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setProfileSuccessMessage('');
    setProfileError('');
    if (type === 'checkbox') {
      if (name === 'isTenantOccupied') {
        setProfileData(prev => ({
          ...prev,
          isTenantOccupied: checked,
          // Eğer kiracı yoksa ve "isTenantOccupied" false yapılıyorsa, displayAs MAL_SAHIBI olmalı
          // Eğer "isTenantOccupied" true yapılıyorsa, displayAs KIRACI olabilir (başka bir UI elemanı ile seçilebilir)
          // Şimdilik displayAs'ı isTenantOccupied'a göre basitçe ayarlayalım
          displayAs: checked ? 'KIRACI' : 'MAL_SAHIBI' // Bu mantık gözden geçirilmeli, UI'da displayAs için ayrı kontrol olabilir
        }));
      } else {
         // Başka checkbox'lar varsa (örneğin showRenterInSystem gibi bir şey olsaydı)
         // Bu örnekte Profile Management sayfasında doğrudan displayAs için bir checkbox yok
         // Ama backend DTO'sunda displayAs var.
         // isTenantOccupied ile displayAs arasındaki ilişkiyi kuruyoruz.
      }
    } else {
      setProfileData(prev => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  const handlePasswordInputChange = (e) => {
    const { name, value } = e.target;
    setPasswordSuccessMessage('');
    setPasswordError('');
    setPasswordData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    setProfileError(null);
    setProfileSuccessMessage('');

    // Temel frontend validasyonu (opsiyonel, backend zaten yapıyor)
    if (!profileData.fullName || !profileData.email) {
      setProfileError(t.fillRequiredFields || 'Lütfen tüm zorunlu alanları doldurun.');
      return;
    }
    if (!profileData.unitTypeId) {
        setProfileError('Birim türü ID\'si eksik. Profil verileri tam yüklenememiş olabilir.');
        return;
    }

    const payload = {
      fullName: profileData.fullName,
      address: profileData.address,
      phoneNumber: profileData.phoneNumber,
      email: profileData.email,
      bankAccountNumber: profileData.bankAccountNumber,
      deedPollNumber: profileData.deedPollNumber,
      isTenantOccupied: profileData.isTenantOccupied,
      displayAs: profileData.displayAs, // Bu, isTenantOccupied'a göre ayarlanmıştı
      unitTypeId: profileData.unitTypeId, // Başlangıçta fetch edilen unitTypeId
      // tenantDetails: profileData.isTenantOccupied ? (profileData.tenantDetails || null) : null, // Eğer tenant formu varsa
    };

    try {
      // const token = localStorage.getItem('token');
      const response = await fetch(`/api/properties/${DEFAULT_PROPERTY_ID}/owners/${DEFAULT_OWNER_ID}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          // 'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || `Profil güncellenemedi. Status: ${response.status}`);
      }
      const updatedData = await response.json();
      setProfileSuccessMessage(t.profileUpdatedSuccess || 'Profil başarıyla güncellendi!');
      // İsteğe bağlı: Güncellenmiş veriyi tekrar state'e set et
      setProfileData({
        fullName: updatedData.fullName || '',
        email: updatedData.email || '',
        phoneNumber: updatedData.phoneNumber || '',
        address: updatedData.address || '',
        bankAccountNumber: updatedData.bankAccountNumber || '',
        deedPollNumber: updatedData.deedPollNumber || '',
        isTenantOccupied: updatedData.isTenantOccupied || false,
        displayAs: updatedData.displayAs || 'MAL_SAHIBI',
        unitTypeId: updatedData.unitTypeId || '',
      });
    } catch (error) {
      console.error('Error updating profile:', error);
      setProfileError(error.message);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    setPasswordError(null);
    setPasswordSuccessMessage('');

    if (passwordData.newPassword !== passwordData.confirmNewPassword) {
      setPasswordError(t.passwordsDoNotMatch || 'Yeni şifreler eşleşmiyor.');
      return;
    }
    if (!passwordData.currentPassword || !passwordData.newPassword) {
      setPasswordError(t.fillAllPasswordFields || 'Lütfen tüm şifre alanlarını doldurun.');
      return;
    }
    
    const payload = {
        currentPassword: passwordData.currentPassword,
        newPassword: passwordData.newPassword,
        confirmNewPassword: passwordData.confirmNewPassword,
    };

    try {
    //   const token = localStorage.getItem('token');
      const response = await fetch('/api/users/me/change-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        //   'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });
      const responseText = await response.text(); // Backend string döndürüyor
      if (!response.ok) {
        // Backend hata durumunda da JSON yerine string dönebilir, veya JSON dönebilir
        // Hata mesajını responseText'ten veya response.json() ile almaya çalışalım
        let errorMessage = responseText;
        try {
            const errorJson = JSON.parse(responseText); // Eğer JSON ise
            errorMessage = errorJson.message || responseText;
        } catch (parseError) {
            // JSON değilse, responseText'i kullan
        }
        throw new Error(errorMessage || `Şifre değiştirilemedi. Status: ${response.status}`);
      }
      setPasswordSuccessMessage(responseText || (t.passwordChangedSuccess || 'Şifre başarıyla değiştirildi!'));
      setPasswordData({ currentPassword: '', newPassword: '', confirmNewPassword: '' }); // Formu temizle
    } catch (error) {
      console.error('Error changing password:', error);
      setPasswordError(error.message);
    }
  };
  
  if (loadingProfile) {
    return <div className="text-center p-5"><div className="spinner-border" role="status"><span className="visually-hidden">Loading...</span></div></div>;
  }

  return (
    <div className="profile-management-module container py-4"> {/* Daha genel bir class adı */}
      <h3 className="mb-4">{t.profileManagement || 'Profile Management'}</h3>
      
      {/* Personal Information Form */}
      <div className="card shadow-sm mb-4">
        <div className="card-header">
          <h5 className="mb-0">{t.personalInformation || 'Personal Information'}</h5>
        </div>
        <div className="card-body">
          <form onSubmit={handleUpdateProfile}>
            {profileError && <div className="alert alert-danger">{profileError}</div>}
            {profileSuccessMessage && <div className="alert alert-success">{profileSuccessMessage}</div>}
            <div className="row g-3">
              <div className="col-md-6">
                <label htmlFor="fullName" className="form-label">{t.fullName || 'Full Name'}</label>
                <input
                  type="text"
                  className="form-control"
                  id="fullName"
                  name="fullName"
                  value={profileData.fullName}
                  onChange={handleProfileInputChange}
                  required
                />
              </div>
              <div className="col-md-6">
                <label htmlFor="email" className="form-label">{t.email || 'Email'}</label>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  name="email"
                  value={profileData.email}
                  onChange={handleProfileInputChange}
                  required
                />
              </div>
              <div className="col-md-6">
                <label htmlFor="phoneNumber" className="form-label">{t.phone || 'Phone Number'}</label>
                <input
                  type="tel"
                  className="form-control"
                  id="phoneNumber"
                  name="phoneNumber"
                  value={profileData.phoneNumber}
                  onChange={handleProfileInputChange}
                />
              </div>
              <div className="col-md-12">
                <label htmlFor="address" className="form-label">{t.address || 'Address'}</label>
                <textarea
                  className="form-control"
                  id="address"
                  name="address"
                  rows="3"
                  value={profileData.address}
                  onChange={handleProfileInputChange}
                ></textarea>
              </div>
               {/* Opsiyonel: Banka ve Tapu bilgileri için alanlar eklenebilir */}
               {/* 
               <div className="col-md-6">
                <label htmlFor="bankAccountNumber" className="form-label">{t.bankAccount || 'Bank Account'}</label>
                <input type="text" className="form-control" id="bankAccountNumber" name="bankAccountNumber" value={profileData.bankAccountNumber} onChange={handleProfileInputChange} />
               </div>
               <div className="col-md-6">
                <label htmlFor="deedPollNumber" className="form-label">{t.titleNumber || 'Title/Deed Poll Number'}</label>
                <input type="text" className="form-control" id="deedPollNumber" name="deedPollNumber" value={profileData.deedPollNumber} onChange={handleProfileInputChange} />
               </div>
               */}
              <div className="col-md-6">
                  <div className="form-check mt-3">
                    <input 
                      className="form-check-input" 
                      type="checkbox" 
                      id="isTenantOccupied" 
                      name="isTenantOccupied"
                      checked={profileData.isTenantOccupied}
                      onChange={handleProfileInputChange}
                    />
                    <label className="form-check-label" htmlFor="isTenantOccupied">
                      {t.isRented || 'Property is Rented'} 
                      {/* t.isTenantOccupied kullanılabilir */}
                    </label>
                  </div>
              </div>
              {/* 
                displayAs (MAL_SAHIBI/KIRACI) için ayrı bir kontrol eklenebilir
                Örneğin bir dropdown veya radio button grubu.
                Şimdilik isTenantOccupied'a göre otomatik ayarlanıyor.
              */}
            </div>
            <button type="submit" className="btn btn-primary mt-3">
              {t.updateProfile || 'Update Profile'}
            </button>
          </form>
        </div>
      </div>

      {/* Change Password Form */}
      <div className="card shadow-sm">
        <div className="card-header">
          <h5 className="mb-0">{t.changePassword || 'Change Password'}</h5>
        </div>
        <div className="card-body">
          <form onSubmit={handleChangePassword}>
            {passwordError && <div className="alert alert-danger">{passwordError}</div>}
            {passwordSuccessMessage && <div className="alert alert-success">{passwordSuccessMessage}</div>}
            <div className="mb-3">
              <label htmlFor="currentPassword"className="form-label">{t.currentPassword || 'Current Password'}</label>
              <input
                type="password"
                className="form-control"
                id="currentPassword"
                name="currentPassword"
                value={passwordData.currentPassword}
                onChange={handlePasswordInputChange}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="newPassword"className="form-label">{t.newPassword || 'New Password'}</label>
              <input
                type="password"
                className="form-control"
                id="newPassword"
                name="newPassword"
                value={passwordData.newPassword}
                onChange={handlePasswordInputChange}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="confirmNewPassword"className="form-label">{t.confirmNewPassword || 'Confirm New Password'}</label>
              <input
                type="password"
                className="form-control"
                id="confirmNewPassword"
                name="confirmNewPassword"
                value={passwordData.confirmNewPassword}
                onChange={handlePasswordInputChange}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary">
              {t.changePasswordBtn || 'Change Password'}
            </button>
          </form>
        </div>
      </div>
      
      {/* 
        Owner Listesi ve Ekleme/Düzenleme Formu (Mevcut OwnerDetailsModule'den)
        Bu kısım şimdilik bu isteğin ana odağı dışındadır.
        İhtiyaç duyulursa bu kısım da ayrıca ele alınabilir.
      */}
      {/* 
      <div className="card border-0 shadow-sm mb-4 mt-5">
        // ... (Mevcut owner listesi yönetimi için olan başlık ve butonlar) ...
      </div> 
      */}

    </div>
  );
}