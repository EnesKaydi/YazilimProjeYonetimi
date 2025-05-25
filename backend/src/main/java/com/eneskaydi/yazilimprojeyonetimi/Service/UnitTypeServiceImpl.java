package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.UnitType;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import com.eneskaydi.yazilimprojeyonetimi.Repository.PropertyRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UnitTypeRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Birim Türü yönetimi servisinin implementasyonu
@Service
@RequiredArgsConstructor
public class UnitTypeServiceImpl implements UnitTypeService {

    private final UnitTypeRepository unitTypeRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository; // Mülk sahibini doğrulamak için

    // Geçici helper metot - Mevcut kullanıcıyı almak için. Güvenlik entegrasyonu
    // sonrası güncellenecek.
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
    public UnitTypeDto createUnitType(Long propertyId, UnitTypeCreateDto unitTypeCreateDto) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);

        if (unitTypeRepository.existsByNameAndProperty(unitTypeCreateDto.getName(), property)) {
            throw new RuntimeException(
                    "Bu mülk için '" + unitTypeCreateDto.getName() + "' adında bir birim türü zaten mevcut.");
        }

        UnitType unitType = new UnitType();
        unitType.setName(unitTypeCreateDto.getName());
        unitType.setMonthlyFee(unitTypeCreateDto.getMonthlyFee());
        unitType.setDescription(unitTypeCreateDto.getDescription());
        unitType.setProperty(property);

        UnitType savedUnitType = unitTypeRepository.save(unitType);
        return mapToUnitTypeDto(savedUnitType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitTypeDto> getUnitTypesByProperty(Long propertyId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        return unitTypeRepository.findByProperty(property)
                .stream()
                .map(this::mapToUnitTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UnitTypeDto getUnitTypeById(Long propertyId, Long unitTypeId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        UnitType unitType = unitTypeRepository.findByIdAndProperty(unitTypeId, property)
                .orElseThrow(() -> new RuntimeException("Birim Türü (ID: " + unitTypeId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));
        return mapToUnitTypeDto(unitType);
    }

    @Override
    @Transactional
    public UnitTypeDto updateUnitType(Long propertyId, Long unitTypeId, UnitTypeUpdateDto unitTypeUpdateDto) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        UnitType unitType = unitTypeRepository.findByIdAndProperty(unitTypeId, property)
                .orElseThrow(() -> new RuntimeException("Güncellenecek Birim Türü (ID: " + unitTypeId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        if (unitTypeUpdateDto.getName() != null && !unitTypeUpdateDto.getName().equals(unitType.getName())) {
            if (unitTypeRepository.existsByNameAndPropertyAndIdNot(unitTypeUpdateDto.getName(), property, unitTypeId)) {
                throw new RuntimeException(
                        "Bu mülk için '" + unitTypeUpdateDto.getName() + "' adında başka bir birim türü zaten mevcut.");
            }
            unitType.setName(unitTypeUpdateDto.getName());
        }

        if (unitTypeUpdateDto.getMonthlyFee() != null) {
            unitType.setMonthlyFee(unitTypeUpdateDto.getMonthlyFee());
        }
        if (unitTypeUpdateDto.getDescription() != null) {
            unitType.setDescription(unitTypeUpdateDto.getDescription());
        }

        UnitType updatedUnitType = unitTypeRepository.save(unitType);
        return mapToUnitTypeDto(updatedUnitType);
    }

    @Override
    @Transactional
    public void deleteUnitType(Long propertyId, Long unitTypeId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        UnitType unitType = unitTypeRepository.findByIdAndProperty(unitTypeId, property)
                .orElseThrow(() -> new RuntimeException("Silinecek Birim Türü (ID: " + unitTypeId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        // TODO: Bu birim türünü kullanan Owner kayıtları varsa ne yapılmalı? Engelle,
        // uyar, null yap vs.
        // PRD'de bu detay yok. Şimdilik direkt silme.
        long ownerCount = unitTypeRepository.countOwnersByUnitTypeId(unitTypeId); // Bu metot repository'de olmalı
        if (ownerCount > 0) {
            throw new RuntimeException("Bu birim türüne ('" + unitType.getName() + "') kayıtlı " + ownerCount
                    + " mülk sahibi/kiracı bulunmaktadır. Önce bu kayıtları düzenlemelisiniz.");
        }

        unitTypeRepository.delete(unitType);
    }

    // UnitType entity'sini UnitTypeDto'ya mapleyen yardımcı metot
    private UnitTypeDto mapToUnitTypeDto(UnitType unitType) {
        UnitTypeDto dto = new UnitTypeDto();
        dto.setId(unitType.getId());
        dto.setName(unitType.getName());
        dto.setMonthlyFee(unitType.getMonthlyFee());
        dto.setDescription(unitType.getDescription());
        if (unitType.getProperty() != null) {
            dto.setPropertyId(unitType.getProperty().getId());
        }
        return dto;
    }
}