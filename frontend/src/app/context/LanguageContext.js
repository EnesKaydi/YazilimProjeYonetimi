'use client';

import { createContext, useContext, useState, useEffect } from 'react';

// Create language context
const LanguageContext = createContext();

// English translations
const en = {
  // General
  appName: 'Homeowners Association Management System',
  loading: 'Loading...',
  save: 'Save',
  
  // Auth
  login: 'Login',
  register: 'Register',
  email: 'Email',
  password: 'Password',
  confirmPassword: 'Confirm Password',
  fullName: 'Full Name',
  loggingIn: 'Logging in...',
  registering: 'Registering...',
  invalidCredentials: 'Invalid email or password',
  passwordsNotMatch: 'Passwords do not match',
  haveAccount: 'Already have an account?',
  noAccount: 'Don\'t have an account?',
  
  // Profile
  profileManagement: 'Profile Management',
  updateProfile: 'Update Profile',
  changePassword: 'Change Password',
  currentPassword: 'Current Password',
  newPassword: 'New Password',
  confirmNewPassword: 'Confirm New Password',
  profileUpdated: 'Profile updated successfully',
  passwordChanged: 'Password changed successfully',
  profilePicture: 'Profile Picture',
  uploadPicture: 'Upload Picture',
  removePicture: 'Remove Picture',
  personalInfo: 'Personal Information',
  phoneNumber: 'Phone Number',
  address: 'Address',
  
  // Dashboard
  welcome: 'Welcome',
  dashboard: 'Dashboard',
  totalProperties: 'Total Properties',
  totalOwners: 'Total Owners',
  pendingPayments: 'Pending Payments',
  propertyCount: 'Total number of properties in the system',
  ownerCount: 'Total number of property owners in the system',
  dueCount: 'Number of unpaid dues',
  propertyManagement: 'Property Management',
  selectProperty: 'Select a property:',
  select: '-- Select --',
  newProperty: 'New Property',
  pleaseSelectProperty: 'Please select a property',
  propertyModulesInfo: 'Management modules will be displayed after selecting a property',
  propertyImages: 'Property Images',
  allImages: 'All Images',
  previous: 'Previous',
  next: 'Next',
  
  // Carousel
  modernLivingSpaces: 'Modern living spaces and social facilities',
  luxuryLiving: 'Luxury living and comfort combined',
  associationCenter: 'Association Center',
  managementMeetingAreas: 'Management and meeting areas',
  
  // Sidebar
  associationManagement: 'Association Management',
  settings: 'Settings',
  profile: 'Profile',
  logout: 'Logout',
  backup: 'Backup / Restore',
  changeLanguage: 'Change Language',
  
  // Property Modules
  propertyDetails: 'Property Details',
  unitTypes: 'Unit Types',
  ownerDetails: 'Owner Details',
  regularBudget: 'Regular Budget',
  exceptionalBudget: 'Exceptional Budget',
  reports: 'Reports',
  documents: 'Documents',
  
  // Property Details Form
  propertyInfo: 'Property Information',
  propertyName: 'Property Name',
  address: 'Address',
  propertyTitle: 'Property Title',
  associationName: 'Association Name',
  locationAndCurrency: 'Location and Currency',
  city: 'City',
  currency: 'Currency',
  propertySummary: 'Property Summary',
  totalUnits: 'Total Units',
  
  // Module Development
  inDevelopment: 'This module is under development.',
  addNewUnitType: 'Add New Unit Type',
  addNewOwner: 'Add New Owner',
  createBudget: 'Create Budget',
  createExceptionalBudget: 'Create Exceptional Budget',
  generateReport: 'Generate Report',
  uploadDocument: 'Upload Document',
  
  // Currency options
  usDollar: 'US Dollar (USD)',
  euro: 'Euro (EUR)',
  britishPound: 'British Pound (GBP)',
  
  // Unit Types Module
  unitTypeName: 'Unit Type Name',
  area: 'Area',
  share: 'Share',
  monthlyFee: 'Monthly Fee',
  description: 'Description',
  cancel: 'Cancel',
  update: 'Update',
  actions: 'Actions',
  noUnitTypesYet: 'No unit types defined yet',
  addUnitTypesDescription: 'Add unit types to define different apartment configurations',
  editUnitType: 'Edit Unit Type',
  unitTypeDescriptionPlaceholder: 'Describe the unit type...',
  
  // Owner Details Module
  unitType: 'Unit Type',
  titleNumber: 'Title Number',
  bankAccount: 'Bank Account',
  phone: 'Phone',
  isRented: 'Property is Rented',
  renterDetails: 'Renter Details',
  showRenterInSystem: 'Show Renter in System (instead of Owner)',
  editOwner: 'Edit Owner',
  contact: 'Contact',
  status: 'Status',
  rented: 'Rented',
  ownerOccupied: 'Owner Occupied',
  edit: 'Edit',
  duplicate: 'Duplicate',
  delete: 'Delete',
  copy: 'Copy',
  confirmDelete: 'Are you sure you want to delete this owner?',
  noOwnersYet: 'No owners added yet',
  addOwnersDescription: 'Add property owners to manage their information',

  // Regular Budget Module
  previousYearBalance: 'Previous Year Balance',
  month: 'Month',
  income: 'Income',
  outcome: 'Outcome',
  balance: 'Balance',
  total: 'Total',
  budgetTransferInfo: 'When the year ends, the final balance will be transferred to the next year.'
};

// French translations
const fr = {
  // General
  appName: 'Système de Gestion des Associations de Propriétaires',
  loading: 'Chargement...',
  save: 'Enregistrer',
  
  // Auth
  login: 'Connexion',
  register: 'S\'inscrire',
  email: 'Email',
  password: 'Mot de passe',
  confirmPassword: 'Confirmer le mot de passe',
  fullName: 'Nom complet',
  loggingIn: 'Connexion en cours...',
  registering: 'Inscription en cours...',
  invalidCredentials: 'Email ou mot de passe invalide',
  passwordsNotMatch: 'Les mots de passe ne correspondent pas',
  haveAccount: 'Vous avez déjà un compte?',
  noAccount: 'Vous n\'avez pas de compte?',
  
  // Dashboard
  welcome: 'Bienvenue',
  dashboard: 'Tableau de bord',
  totalProperties: 'Propriétés totales',
  totalOwners: 'Propriétaires totaux',
  pendingPayments: 'Paiements en attente',
  propertyCount: 'Nombre total de propriétés dans le système',
  ownerCount: 'Nombre total de propriétaires dans le système',
  dueCount: 'Nombre de cotisations impayées',
  propertyManagement: 'Gestion des propriétés',
  selectProperty: 'Sélectionnez une propriété:',
  select: '-- Sélectionner --',
  newProperty: 'Nouvelle propriété',
  pleaseSelectProperty: 'Veuillez sélectionner une propriété',
  propertyModulesInfo: 'Les modules de gestion seront affichés après avoir sélectionné une propriété',
  propertyImages: 'Images de la propriété',
  allImages: 'Toutes les images',
  previous: 'Précédent',
  next: 'Suivant',
  
  // Carousel
  modernLivingSpaces: 'Espaces de vie modernes et installations sociales',
  luxuryLiving: 'Luxe et confort combinés',
  associationCenter: 'Centre de l\'Association',
  managementMeetingAreas: 'Zones de gestion et de réunion',
  
  // Sidebar
  associationManagement: 'Gestion des Associations',
  settings: 'Paramètres',
  profile: 'Profil',
  logout: 'Déconnexion',
  backup: 'Sauvegarde / Restauration',
  changeLanguage: 'Changer de langue',
  
  // Property Modules
  propertyDetails: 'Détails de la propriété',
  unitTypes: 'Types d\'unités',
  ownerDetails: 'Détails du propriétaire',
  regularBudget: 'Budget régulier',
  exceptionalBudget: 'Budget exceptionnel',
  reports: 'Rapports',
  documents: 'Documents',
  
  // Profile
  profileManagement: 'Gestion du Profil',
  updateProfile: 'Mettre à jour le profil',
  changePassword: 'Changer le mot de passe',
  currentPassword: 'Mot de passe actuel',
  newPassword: 'Nouveau mot de passe',
  confirmNewPassword: 'Confirmer le nouveau mot de passe',
  profileUpdated: 'Profil mis à jour avec succès',
  passwordChanged: 'Mot de passe changé avec succès',
  profilePicture: 'Photo de profil',
  uploadPicture: 'Télécharger une photo',
  removePicture: 'Supprimer la photo',
  personalInfo: 'Informations personnelles',
  phoneNumber: 'Numéro de téléphone',
  address: 'Adresse',
  
  // Accounts Management
  accounts: 'Comptes',
  accountsManagement: 'Gestion des Comptes',
  userAccounts: 'Comptes Utilisateurs',
  addNewUser: 'Ajouter un Utilisateur',
  editUser: 'Modifier l\'Utilisateur',
  role: 'Rôle',
  lastLogin: 'Dernière Connexion',
  accountId: 'ID du Compte',
  noUsersYet: 'Aucun utilisateur ajouté',
  addUsersDescription: 'Ajoutez des utilisateurs pour gérer leurs comptes',
  confirmDeleteUser: 'Êtes-vous sûr de vouloir supprimer cet utilisateur?',
  requiredFields: 'Le nom et l\'email sont requis',
  
  // Property Details Form
  propertyInfo: 'Informations sur la propriété',
  propertyName: 'Nom de la propriété',
  address: 'Adresse',
  propertyTitle: 'Titre de propriété',
  associationName: 'Nom de l\'association',
  locationAndCurrency: 'Emplacement et devise',
  city: 'Ville',
  currency: 'Devise',
  propertySummary: 'Résumé de la propriété',
  totalUnits: 'Unités totales',
  
  // Module Development
  inDevelopment: 'Ce module est en cours de développement.',
  addNewUnitType: 'Ajouter un nouveau type d\'unité',
  addNewOwner: 'Ajouter un nouveau propriétaire',
  createBudget: 'Créer un budget',
  createExceptionalBudget: 'Créer un budget exceptionnel',
  generateReport: 'Générer un rapport',
  uploadDocument: 'Télécharger un document',
  
  // Currency options
  usDollar: 'Dollar américain (USD)',
  euro: 'Euro (EUR)',
  britishPound: 'Livre sterling (GBP)',
  
  // Unit Types Module
  unitTypeName: 'Nom du type d\'unité',
  area: 'Surface',
  share: 'Part',
  monthlyFee: 'Frais mensuels',
  description: 'Description',
  cancel: 'Annuler',
  update: 'Mettre à jour',
  actions: 'Actions',
  noUnitTypesYet: 'Aucun type d\'unité défini',
  addUnitTypesDescription: 'Ajoutez des types d\'unités pour définir différentes configurations d\'appartements',
  editUnitType: 'Modifier le type d\'unité',
  unitTypeDescriptionPlaceholder: 'Décrivez le type d\'unité...',
  
  // Owner Details Module
  unitType: 'Type d\'unité',
  titleNumber: 'Numéro de titre',
  bankAccount: 'Compte bancaire',
  phone: 'Téléphone',
  isRented: 'Propriété est louée',
  renterDetails: 'Détails du locataire',
  showRenterInSystem: 'Afficher le locataire dans le système (au lieu du propriétaire)',
  editOwner: 'Modifier le propriétaire',
  contact: 'Contact',
  status: 'Statut',
  rented: 'Loué',
  ownerOccupied: 'Occupé par le propriétaire',
  edit: 'Modifier',
  duplicate: 'Dupliquer',
  delete: 'Supprimer',
  copy: 'Copie',
  confirmDelete: 'Êtes-vous sûr de vouloir supprimer ce propriétaire?',
  noOwnersYet: 'Aucun propriétaire ajouté pour le moment',
  addOwnersDescription: 'Ajoutez des propriétaires pour gérer leurs informations',

  // Regular Budget Module
  previousYearBalance: 'Solde de l\'année précédente',
  month: 'Mois',
  income: 'Revenus',
  outcome: 'Dépenses',
  balance: 'Solde',
  total: 'Total',
  budgetTransferInfo: 'À la fin de l\'année, le solde final sera transféré à l\'année suivante.'
};

export function LanguageProvider({ children }) {
  const [language, setLanguage] = useState('en'); // Default to English
  const [translations, setTranslations] = useState(en);

  useEffect(() => {
    // Check if language preference is stored
    const storedLanguage = localStorage.getItem('language');
    
    if (storedLanguage) {
      setLanguage(storedLanguage);
      setTranslations(storedLanguage === 'en' ? en : fr);
    }
  }, []);

  const changeLanguage = (lang) => {
    setLanguage(lang);
    setTranslations(lang === 'en' ? en : fr);
    localStorage.setItem('language', lang);
  };

  return (
    <LanguageContext.Provider value={{ language, translations, changeLanguage }}>
      {children}
    </LanguageContext.Provider>
  );
}

export function useLanguage() {
  return useContext(LanguageContext);
}