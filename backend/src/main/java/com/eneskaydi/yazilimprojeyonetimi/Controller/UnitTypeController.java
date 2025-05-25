package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.UnitTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Belirli bir mülke ait Birim Türleri (UnitType) için CRUD işlemlerini yöneten
 * REST controller sınıfı.
 * Birim türleri (örn: daire tipi, garaj) ve bunlara ait ücret bilgilerini
 * yönetir.
 */
@RestController
@RequestMapping("/api/properties/{propertyId}/unit-types")
@RequiredArgsConstructor
public class UnitTypeController {

    private final UnitTypeService unitTypeService;

    /**
     * Belirli bir mülk için yeni bir birim türü oluşturur.
     *
     * @param propertyId        Sahip olan mülkün ID'si.
     * @param unitTypeCreateDto Oluşturulacak birim türü bilgilerini içeren DTO.
     * @return HTTP 201 Created ile oluşturulan birim türünün bilgilerini içeren
     *         UnitTypeDto.
     */
    @PostMapping
    public ResponseEntity<UnitTypeDto> createUnitType(@PathVariable Long propertyId,
            @Valid @RequestBody UnitTypeCreateDto unitTypeCreateDto) {
        UnitTypeDto createdUnitType = unitTypeService.createUnitType(propertyId, unitTypeCreateDto);
        return new ResponseEntity<>(createdUnitType, HttpStatus.CREATED);
    }

    /**
     * Belirli bir mülke ait tüm birim türlerini listeler.
     *
     * @param propertyId Sahip olan mülkün ID'si.
     * @return HTTP 200 OK ile mülke ait birim türlerinin listesi (UnitTypeDto
     *         listesi).
     */
    @GetMapping
    public ResponseEntity<List<UnitTypeDto>> getAllUnitTypesForProperty(@PathVariable Long propertyId) {
        List<UnitTypeDto> unitTypes = unitTypeService.getUnitTypesByProperty(propertyId);
        return ResponseEntity.ok(unitTypes);
    }

    /**
     * Belirli bir mülke ait, belirli bir ID'ye sahip birim türünü günceller.
     *
     * @param propertyId        Sahip olan mülkün ID'si.
     * @param unitTypeId        Güncellenecek birim türünün ID'si.
     * @param unitTypeUpdateDto Güncellenecek birim türü bilgilerini içeren DTO.
     * @return HTTP 200 OK ile güncellenmiş birim türü bilgilerini içeren
     *         UnitTypeDto.
     */
    @PutMapping("/{unitTypeId}")
    public ResponseEntity<UnitTypeDto> updateUnitType(@PathVariable Long propertyId,
            @PathVariable Long unitTypeId,
            @Valid @RequestBody UnitTypeUpdateDto unitTypeUpdateDto) {
        UnitTypeDto updatedUnitType = unitTypeService.updateUnitType(propertyId, unitTypeId, unitTypeUpdateDto);
        return ResponseEntity.ok(updatedUnitType);
    }

    /**
     * Belirli bir mülke ait, belirli bir ID'ye sahip birim türünü siler.
     * Birim türüne bağlı Sahipler (Owner) varsa silme işlemi engellenebilir (Servis
     * katmanında kontrol edilir).
     *
     * @param propertyId Sahip olan mülkün ID'si.
     * @param unitTypeId Silinecek birim türünün ID'si.
     * @return HTTP 204 No Content.
     */
    @DeleteMapping("/{unitTypeId}")
    public ResponseEntity<Void> deleteUnitType(@PathVariable Long propertyId, @PathVariable Long unitTypeId) {
        unitTypeService.deleteUnitType(propertyId, unitTypeId);
        return ResponseEntity.noContent().build();
    }
}