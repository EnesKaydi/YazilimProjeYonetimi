package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.*;
import com.eneskaydi.yazilimprojeyonetimi.Entity.*;
import com.eneskaydi.yazilimprojeyonetimi.Repository.OwnerRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.PropertyRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UnitTypeRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Sahip ve Kiracı yönetimi servisinin implementasyonu
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final PropertyRepository propertyRepository;
    private final UnitTypeRepository unitTypeRepository;
    private final UserRepository userRepository; // Mülk sahibini doğrulamak için

    // Geçici helper metot - Mevcut kullanıcıyı almak için.
    private User getCurrentUser() {
        return userRepository.findById(1L) // Varsayılan test kullanıcısı (ID: 1)
                .orElseThrow(() -> new RuntimeException("Varsayılan test kullanıcısı (ID: 1) bulunamadı."));
    }

    // Mülkün mevcut kullanıcıya ait olup olmadığını kontrol eden yardımcı metot.
    private Property getPropertyIfBelongsToCurrentUser(Long propertyId) {
        User currentUser = getCurrentUser();
        return propertyRepository.findByIdAndUser(propertyId, currentUser)
                .orElseThrow(() -> new RuntimeException(
                        "Mülk (ID: " + propertyId + ") bulunamadı veya mevcut kullanıcıya ait değil."));
    }

    @Override
    @Transactional
    public OwnerDto createOwner(Long propertyId, OwnerCreateDto ownerCreateDto) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        UnitType unitType = unitTypeRepository.findByIdAndProperty(ownerCreateDto.getUnitTypeId(), property)
                .orElseThrow(() -> new RuntimeException("Birim Türü (ID: " + ownerCreateDto.getUnitTypeId()
                        + ") bulunamadı veya belirtilen mülke ait değil."));

        if (ownerCreateDto.getEmail() != null
                && ownerRepository.existsByEmailAndProperty(ownerCreateDto.getEmail(), property)) {
            throw new RuntimeException(
                    "Bu mülk için '" + ownerCreateDto.getEmail() + "' e-posta adresi zaten kullanımda.");
        }

        Owner owner = mapToOwnerEntity(ownerCreateDto, property, unitType);

        if (ownerCreateDto.getTenantDetails() != null) {
            Tenant tenant = mapToTenantEntity(ownerCreateDto.getTenantDetails());
            owner.setTenant(tenant);
            // tenant.setOwner(owner); // Eğer Tenant->Owner ilişkisi varsa (opsiyonel,
            // Owner'da @OneToOne(cascade=ALL) varsa gerekmeyebilir)
        }

        Owner savedOwner = ownerRepository.save(owner);
        return mapToOwnerDto(savedOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerDto> getOwnersByProperty(Long propertyId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        return ownerRepository.findByProperty(property)
                .stream()
                .map(this::mapToOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerDto getOwnerById(Long propertyId, Long ownerId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Owner owner = ownerRepository.findByIdAndProperty(ownerId, property)
                .orElseThrow(() -> new RuntimeException("Sahip/Kiracı (ID: " + ownerId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));
        return mapToOwnerDto(owner);
    }

    @Override
    @Transactional
    public OwnerDto updateOwner(Long propertyId, Long ownerId, OwnerUpdateDto ownerUpdateDto) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Owner owner = ownerRepository.findByIdAndProperty(ownerId, property)
                .orElseThrow(() -> new RuntimeException("Güncellenecek Sahip/Kiracı (ID: " + ownerId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        if (ownerUpdateDto.getEmail() != null && !ownerUpdateDto.getEmail().equals(owner.getEmail())) {
            if (ownerRepository.existsByEmailAndPropertyAndIdNot(ownerUpdateDto.getEmail(), property, ownerId)) {
                throw new RuntimeException("Bu mülk için '" + ownerUpdateDto.getEmail()
                        + "' e-posta adresi zaten başka bir kayıtta kullanımda.");
            }
            owner.setEmail(ownerUpdateDto.getEmail());
        }

        if (ownerUpdateDto.getUnitTypeId() != null) {
            UnitType unitType = unitTypeRepository.findByIdAndProperty(ownerUpdateDto.getUnitTypeId(), property)
                    .orElseThrow(() -> new RuntimeException("Birim Türü (ID: " + ownerUpdateDto.getUnitTypeId()
                            + ") bulunamadı veya belirtilen mülke ait değil."));
            owner.setUnitType(unitType);
        }

        updateOwnerEntityFromDto(owner, ownerUpdateDto);

        if (ownerUpdateDto.getTenantDetails() != null) {
            Tenant tenant = owner.getTenant() == null ? new Tenant() : owner.getTenant();
            updateTenantEntityFromDto(tenant, ownerUpdateDto.getTenantDetails());
            owner.setTenant(tenant);
            // tenant.setOwner(owner); // Opsiyonel
        } else {
            // Eğer TenantUpdateDto null gelirse ve mevcut tenant varsa, PRD'ye göre nasıl
            // davranılmalı?
            // Şimdilik kiracıyı null yapabiliriz eğer istenirse veya mevcut kiracıyı
            // koruyabiliriz.
            // owner.setTenant(null); // Kiracıyı kaldırmak için.
        }

        Owner updatedOwner = ownerRepository.save(owner);
        return mapToOwnerDto(updatedOwner);
    }

    @Override
    @Transactional
    public void deleteOwner(Long propertyId, Long ownerId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Owner owner = ownerRepository.findByIdAndProperty(ownerId, property)
                .orElseThrow(() -> new RuntimeException("Silinecek Sahip/Kiracı (ID: " + ownerId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        // TODO: İlişkili IncomeRecord ve Document'lar için silme/bağlantı koparma
        // mantığı PRD'ye göre eklenecek.
        ownerRepository.delete(owner);
    }

    @Override
    @Transactional
    public OwnerDto duplicateOwner(Long propertyId, Long ownerId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Owner originalOwner = ownerRepository.findByIdAndProperty(ownerId, property)
                .orElseThrow(() -> new RuntimeException("Çoğaltılacak Sahip/Kiracı (ID: " + ownerId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        Owner newOwner = new Owner();
        // Alanları kopyala, benzersiz olması gerekenleri (örn: tapuNo, email) temizle
        // veya değiştir.
        newOwner.setFullName(originalOwner.getFullName() + " (Kopya)");
        newOwner.setAddress(originalOwner.getAddress());
        newOwner.setPhoneNumber(originalOwner.getPhoneNumber());
        // newOwner.setEmail(null); // E-posta benzersiz olmalı, kullanıcıdan alınmalı
        // veya farklı üretilmeli.
        newOwner.setBankAccountNumber(originalOwner.getBankAccountNumber());
        newOwner.setDeedPollNumber(
                originalOwner.getDeedPollNumber() != null ? originalOwner.getDeedPollNumber() + "-KOPYA"
                        : null);
        newOwner.setDisplayAs(originalOwner.getDisplayAs());
        newOwner.setUnitType(originalOwner.getUnitType());
        newOwner.setProperty(property);

        if (originalOwner.getTenant() != null) {
            Tenant originalTenant = originalOwner.getTenant();
            Tenant newTenant = new Tenant();
            newTenant.setFullName(originalTenant.getFullName());
            newTenant.setAddress(originalTenant.getAddress());
            newTenant.setPhoneNumber(originalTenant.getPhoneNumber());
            // newTenant.setEmail(null);
            newTenant.setBankAccountNumber(originalTenant.getBankAccountNumber());
            newOwner.setTenant(newTenant);
        }
        // E-posta ve tapu no gibi alanlar kullanıcı tarafından güncellenmeli.
        // Bu yüzden createOwner'daki gibi bir email check burada yapılmadı, kullanıcıya
        // bırakıldı.
        Owner duplicatedOwner = ownerRepository.save(newOwner);
        return mapToOwnerDto(duplicatedOwner);
    }

    // Mapper metotları
    private OwnerDto mapToOwnerDto(Owner owner) {
        OwnerDto dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setFullName(owner.getFullName());
        dto.setAddress(owner.getAddress());
        dto.setPhoneNumber(owner.getPhoneNumber());
        dto.setEmail(owner.getEmail());
        dto.setBankAccountNumber(owner.getBankAccountNumber());
        dto.setDeedPollNumber(owner.getDeedPollNumber());
        dto.setDisplayAs(owner.getDisplayAs());
        if (owner.getUnitType() != null) {
            dto.setUnitTypeId(owner.getUnitType().getId());
            dto.setUnitTypeName(owner.getUnitType().getName());
        }
        if (owner.getProperty() != null) {
            dto.setPropertyId(owner.getProperty().getId());
        }
        if (owner.getTenant() != null) {
            dto.setTenantDetails(mapToTenantDto(owner.getTenant()));
        }
        return dto;
    }

    private TenantDto mapToTenantDto(Tenant tenant) {
        TenantDto dto = new TenantDto();
        dto.setId(tenant.getId());
        dto.setFullName(tenant.getFullName());
        dto.setAddress(tenant.getAddress());
        dto.setPhoneNumber(tenant.getPhoneNumber());
        dto.setEmail(tenant.getEmail());
        dto.setBankAccountNumber(tenant.getBankAccountNumber());
        return dto;
    }

    private Owner mapToOwnerEntity(OwnerCreateDto dto, Property property, UnitType unitType) {
        Owner owner = new Owner();
        owner.setFullName(dto.getFullName());
        owner.setAddress(dto.getAddress());
        owner.setPhoneNumber(dto.getPhoneNumber());
        owner.setEmail(dto.getEmail());
        owner.setBankAccountNumber(dto.getBankAccountNumber());
        owner.setDeedPollNumber(dto.getDeedPollNumber());
        owner.setDisplayAs(dto.getDisplayAs());
        owner.setUnitType(unitType);
        owner.setProperty(property);
        return owner;
    }

    private Tenant mapToTenantEntity(TenantCreateDto dto) {
        Tenant tenant = new Tenant();
        tenant.setFullName(dto.getFullName());
        tenant.setAddress(dto.getAddress());
        tenant.setPhoneNumber(dto.getPhoneNumber());
        tenant.setEmail(dto.getEmail());
        tenant.setBankAccountNumber(dto.getBankAccountNumber());
        return tenant;
    }

    private void updateOwnerEntityFromDto(Owner owner, OwnerUpdateDto dto) {
        if (dto.getFullName() != null)
            owner.setFullName(dto.getFullName());
        if (dto.getAddress() != null)
            owner.setAddress(dto.getAddress());
        if (dto.getPhoneNumber() != null)
            owner.setPhoneNumber(dto.getPhoneNumber());
        // Email ve UnitType güncellemesi özel olarak yukarıda ele alındı.
        if (dto.getBankAccountNumber() != null)
            owner.setBankAccountNumber(dto.getBankAccountNumber());
        if (dto.getDeedPollNumber() != null)
            owner.setDeedPollNumber(dto.getDeedPollNumber());
        if (dto.getIsTenantOccupied() != null)
            owner.setTenantOccupied(dto.getIsTenantOccupied());
        if (dto.getDisplayAs() != null)
            owner.setDisplayAs(dto.getDisplayAs());
    }

    private void updateTenantEntityFromDto(Tenant tenant, TenantUpdateDto dto) {
        if (dto.getFullName() != null)
            tenant.setFullName(dto.getFullName());
        if (dto.getAddress() != null)
            tenant.setAddress(dto.getAddress());
        if (dto.getPhoneNumber() != null)
            tenant.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmail() != null)
            tenant.setEmail(dto.getEmail());
        if (dto.getBankAccountNumber() != null)
            tenant.setBankAccountNumber(dto.getBankAccountNumber());
    }
}