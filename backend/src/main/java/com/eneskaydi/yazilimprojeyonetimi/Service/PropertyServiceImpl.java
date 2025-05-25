package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import com.eneskaydi.yazilimprojeyonetimi.Repository.PropertyRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PropertyService arayüzünün implementasyonudur.
 * Mülklerle ilgili CRUD operasyonlarını ve diğer iş mantıklarını içerir.
 * Türkçe yorum satırları eklenmiştir.
 */
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository; // Mülkü kullanıcıyla ilişkilendirmek için

    // Geçici helper metot - Mevcut kullanıcıyı almak için (Gerçek uygulamada Spring
    // Security Context kullanılmalı)
    private User getCurrentUser() {
        // Bu kısım normalde Spring Security'den alınır.
        // Şimdilik varsayılan bir kullanıcı döndürüyoruz veya hata fırlatıyoruz.
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Varsayılan test kullanıcısı (ID: 1) bulunamadı."));
    }

    @Override
    @Transactional
    public PropertyDto createProperty(PropertyCreateDto propertyCreateDto) {
        User currentUser = getCurrentUser();

        // Mülk adı benzersizliği kontrolü (kullanıcı bazında veya global olabilir -
        // PRD'ye göre)
        if (propertyRepository.findByNameAndUser(propertyCreateDto.getName(), currentUser).isPresent()) {
            throw new RuntimeException(
                    "Bu kullanıcı için '" + propertyCreateDto.getName() + "' adında bir mülk zaten mevcut.");
        }

        Property property = new Property();
        property.setName(propertyCreateDto.getName());
        property.setAddress(propertyCreateDto.getAddress());
        property.setPropertyTitle(propertyCreateDto.getPropertyTitle());
        property.setAssociationName(propertyCreateDto.getAssociationName());
        property.setCity(propertyCreateDto.getCity());
        property.setCurrency(propertyCreateDto.getCurrency());
        property.setUser(currentUser);

        Property savedProperty = propertyRepository.save(property);
        return mapToPropertyDto(savedProperty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto> getAllPropertiesForCurrentUser() {
        User currentUser = getCurrentUser();
        return propertyRepository.findByUser(currentUser)
                .stream()
                .map(this::mapToPropertyDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyDto getPropertyById(Long propertyId) {
        User currentUser = getCurrentUser();
        Property property = propertyRepository.findByIdAndUser(propertyId, currentUser)
                .orElseThrow(() -> new RuntimeException(
                        "Mülk (ID: " + propertyId + ") bulunamadı veya mevcut kullanıcıya ait değil."));
        return mapToPropertyDto(property);
    }

    @Override
    @Transactional
    public PropertyDto updateProperty(Long propertyId, PropertyUpdateDto propertyUpdateDto) {
        User currentUser = getCurrentUser();
        Property property = propertyRepository.findByIdAndUser(propertyId, currentUser)
                .orElseThrow(() -> new RuntimeException(
                        "Güncellenecek mülk (ID: " + propertyId + ") bulunamadı veya mevcut kullanıcıya ait değil."));

        // Mülk adı benzersizlik kontrolü (güncelleme sırasında, kendisi hariç)
        propertyRepository.findByNameAndUserAndIdNot(propertyUpdateDto.getName(), currentUser, propertyId)
                .ifPresent(p -> {
                    throw new RuntimeException("Bu kullanıcı için '" + propertyUpdateDto.getName()
                            + "' adında başka bir mülk zaten mevcut.");
                });

        property.setName(propertyUpdateDto.getName());
        property.setAddress(propertyUpdateDto.getAddress());
        property.setPropertyTitle(propertyUpdateDto.getPropertyTitle());
        property.setAssociationName(propertyUpdateDto.getAssociationName());
        property.setCity(propertyUpdateDto.getCity());
        property.setCurrency(propertyUpdateDto.getCurrency());

        Property updatedProperty = propertyRepository.save(property);
        return mapToPropertyDto(updatedProperty);
    }

    @Override
    @Transactional
    public void deleteProperty(Long propertyId) {
        User currentUser = getCurrentUser();
        Property property = propertyRepository.findByIdAndUser(propertyId, currentUser)
                .orElseThrow(() -> new RuntimeException(
                        "Silinecek mülk (ID: " + propertyId + ") bulunamadı veya mevcut kullanıcıya ait değil."));

        // TODO: İlişkili varlıkların silinmesi (UnitTypes, Owners, BudgetYears,
        // Documents vb.)
        // Bu, PRD'de belirtildiği gibi dikkatlice ele alınmalı (CASCADE veya manuel
        // silme).
        // Örneğin: unitTypeRepository.deleteByProperty(property);
        // ownerRepository.deleteByProperty(property);

        propertyRepository.delete(property);
    }

    // Property entity'sini PropertyDto'ya mapleyen yardımcı metot.
    private PropertyDto mapToPropertyDto(Property property) {
        PropertyDto dto = new PropertyDto();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setAddress(property.getAddress());
        dto.setPropertyTitle(property.getPropertyTitle());
        dto.setAssociationName(property.getAssociationName());
        dto.setCity(property.getCity());
        dto.setCurrency(property.getCurrency());
        if (property.getUser() != null) {
            dto.setUserId(property.getUser().getId());
        }
        // İlişkili entity'lerin ID'lerini veya özet bilgilerini eklemek gerekebilir.
        // dto.setUnitTypeIds(...);
        // dto.setOwnerIds(...);
        return dto;
    }
}