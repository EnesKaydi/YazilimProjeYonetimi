package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Mülk (Property) CRUD işlemlerini ve mülkle ilgili diğer operasyonları yöneten
 * REST controller sınıfı.
 */
@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * Yeni bir mülk oluşturur.
     * Giriş yapmış olan kullanıcı bu mülkün sahibi olarak atanır.
     *
     * @param propertyCreateDto Oluşturulacak mülk bilgilerini içeren DTO.
     * @return HTTP 201 Created ile oluşturulan mülkün bilgilerini içeren
     *         PropertyDto.
     */
    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(@Valid @RequestBody PropertyCreateDto propertyCreateDto) {
        PropertyDto createdProperty = propertyService.createProperty(propertyCreateDto);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    /**
     * Giriş yapmış kullanıcıya ait tüm mülkleri listeler.
     *
     * @return HTTP 200 OK ile kullanıcıya ait mülklerin listesi (PropertyDto
     *         listesi).
     */
    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllPropertiesForCurrentUser() {
        List<PropertyDto> properties = propertyService.getAllPropertiesForCurrentUser();
        return ResponseEntity.ok(properties);
    }

    /**
     * Belirli bir ID'ye sahip mülkü getirir.
     * Mülkün giriş yapmış kullanıcıya ait olup olmadığı kontrol edilir.
     *
     * @param propertyId Getirilecek mülkün ID'si.
     * @return HTTP 200 OK ile mülk bilgilerini içeren PropertyDto.
     */
    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDto> getPropertyById(@PathVariable Long propertyId) {
        PropertyDto propertyDto = propertyService.getPropertyById(propertyId);
        return ResponseEntity.ok(propertyDto);
    }

    /**
     * Belirli bir ID'ye sahip mülkü günceller.
     * Mülkün giriş yapmış kullanıcıya ait olup olmadığı kontrol edilir.
     *
     * @param propertyId        Güncellenecek mülkün ID'si.
     * @param propertyUpdateDto Güncellenecek mülk bilgilerini içeren DTO.
     * @return HTTP 200 OK ile güncellenmiş mülk bilgilerini içeren PropertyDto.
     */
    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyDto> updateProperty(@PathVariable Long propertyId,
            @Valid @RequestBody PropertyUpdateDto propertyUpdateDto) {
        PropertyDto updatedProperty = propertyService.updateProperty(propertyId, propertyUpdateDto);
        return ResponseEntity.ok(updatedProperty);
    }

    /**
     * Belirli bir ID'ye sahip mülkü siler.
     * Mülkün giriş yapmış kullanıcıya ait olup olmadığı kontrol edilir.
     * İlişkili varlıkların (UnitType, Owner vb.) silinmesi servis katmanında ele
     * alınmalıdır.
     *
     * @param propertyId Silinecek mülkün ID'si.
     * @return HTTP 204 No Content.
     */
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long propertyId) {
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.noContent().build();
    }
}