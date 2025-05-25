package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.OwnerCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.OwnerDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.OwnerUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Belirli bir mülke ait Sahipler (Owner) ve Kiracılar için CRUD işlemlerini ve
 * çoğaltma işlemini yöneten REST controller.
 */
@RestController
@RequestMapping("/api/properties/{propertyId}/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    /**
     * Belirli bir mülk için yeni bir sahip/kiracı oluşturur.
     *
     * @param propertyId     Sahip/kiracının ekleneceği mülkün ID'si.
     * @param ownerCreateDto Oluşturulacak sahip/kiracı bilgilerini içeren DTO.
     * @return HTTP 201 Created ile oluşturulan sahip/kiracı bilgilerini içeren
     *         OwnerDto.
     */
    @PostMapping
    public ResponseEntity<OwnerDto> createOwner(@PathVariable Long propertyId,
            @Valid @RequestBody OwnerCreateDto ownerCreateDto) {
        OwnerDto createdOwner = ownerService.createOwner(propertyId, ownerCreateDto);
        return new ResponseEntity<>(createdOwner, HttpStatus.CREATED);
    }

    /**
     * Belirli bir mülke ait tüm sahipleri/kiracıları listeler.
     *
     * @param propertyId Sahip/kiracıların listeleneceği mülkün ID'si.
     * @return HTTP 200 OK ile mülke ait sahip/kiracıların listesi (OwnerDto
     *         listesi).
     */
    @GetMapping
    public ResponseEntity<List<OwnerDto>> getAllOwnersForProperty(@PathVariable Long propertyId) {
        List<OwnerDto> owners = ownerService.getOwnersByProperty(propertyId);
        return ResponseEntity.ok(owners);
    }

    /**
     * Belirli bir mülke ait, belirli bir ID'ye sahip sahibi/kiracıyı getirir.
     *
     * @param propertyId Sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId    Getirilecek sahip/kiracının ID'si.
     * @return HTTP 200 OK ile sahip/kiracı bilgilerini içeren OwnerDto.
     */
    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable Long propertyId, @PathVariable Long ownerId) {
        OwnerDto ownerDto = ownerService.getOwnerById(propertyId, ownerId);
        return ResponseEntity.ok(ownerDto);
    }

    /**
     * Belirli bir mülke ait, belirli bir ID'ye sahip sahibi/kiracıyı günceller.
     *
     * @param propertyId     Sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId        Güncellenecek sahip/kiracının ID'si.
     * @param ownerUpdateDto Güncellenecek sahip/kiracı bilgilerini içeren DTO.
     * @return HTTP 200 OK ile güncellenmiş sahip/kiracı bilgilerini içeren
     *         OwnerDto.
     */
    @PutMapping("/{ownerId}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable Long propertyId,
            @PathVariable Long ownerId,
            @Valid @RequestBody OwnerUpdateDto ownerUpdateDto) {
        OwnerDto updatedOwner = ownerService.updateOwner(propertyId, ownerId, ownerUpdateDto);
        return ResponseEntity.ok(updatedOwner);
    }

    /**
     * Belirli bir mülke ait, belirli bir ID'ye sahip sahibi/kiracıyı siler.
     *
     * @param propertyId Sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId    Silinecek sahip/kiracının ID'si.
     * @return HTTP 204 No Content.
     */
    @DeleteMapping("/{ownerId}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long propertyId, @PathVariable Long ownerId) {
        ownerService.deleteOwner(propertyId, ownerId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Belirli bir mülke ait, belirli bir ID'ye sahip sahibi/kiracıyı çoğaltır
     * (kopyalar).
     *
     * @param propertyId Sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId    Çoğaltılacak sahip/kiracının ID'si.
     * @return HTTP 201 Created ile yeni oluşturulan (çoğaltılmış) sahip/kiracı
     *         bilgilerini içeren OwnerDto.
     */
    @PostMapping("/{ownerId}/duplicate")
    public ResponseEntity<OwnerDto> duplicateOwner(@PathVariable Long propertyId, @PathVariable Long ownerId) {
        OwnerDto duplicatedOwner = ownerService.duplicateOwner(propertyId, ownerId);
        return new ResponseEntity<>(duplicatedOwner, HttpStatus.CREATED);
    }
}