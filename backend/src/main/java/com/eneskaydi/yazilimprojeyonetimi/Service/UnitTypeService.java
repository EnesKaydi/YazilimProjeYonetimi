package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UnitTypeUpdateDto;

import java.util.List;

// Birim Türü yönetimi işlemlerinden sorumlu servis arayüzü.
public interface UnitTypeService {

    /**
     * Belirli bir mülk için yeni bir birim türü oluşturur.
     * Mülkün mevcut kullanıcıya ait olduğu varsayılır (Controller katmanında veya
     * bu servis içinde doğrulanacak).
     *
     * @param propertyId        Birim türünün ekleneceği mülkün ID'si.
     * @param unitTypeCreateDto Oluşturulacak birim türü bilgilerini içeren DTO.
     * @return Oluşturulan birim türünün bilgilerini içeren DTO.
     * @throws RuntimeException Mülk bulunamazsa veya kullanıcıya ait değilse, ya da
     *                          aynı isimde birim türü zaten varsa.
     */
    UnitTypeDto createUnitType(Long propertyId, UnitTypeCreateDto unitTypeCreateDto);

    /**
     * Belirli bir mülke ait tüm birim türlerini listeler.
     *
     * @param propertyId Birim türleri listelenecek mülkün ID'si.
     * @return Mülke ait birim türlerinin listesi.
     * @throws RuntimeException Mülk bulunamazsa veya kullanıcıya ait değilse.
     */
    List<UnitTypeDto> getUnitTypesByProperty(Long propertyId);

    /**
     * Belirli bir mülke ait spesifik bir birim türünü ID'sine göre getirir.
     *
     * @param propertyId Birim türünün ait olduğu mülkün ID'si.
     * @param unitTypeId Getirilecek birim türünün ID'si.
     * @return Birim türü bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya birim türü bulunamazsa, ya da birim türü
     *                          belirtilen mülke ait değilse.
     */
    UnitTypeDto getUnitTypeById(Long propertyId, Long unitTypeId);

    /**
     * Belirli bir mülke ait bir birim türünü günceller.
     *
     * @param propertyId        Güncellenecek birim türünün ait olduğu mülkün ID'si.
     * @param unitTypeId        Güncellenecek birim türünün ID'si.
     * @param unitTypeUpdateDto Güncellenecek birim türü bilgilerini içeren DTO.
     * @return Güncellenmiş birim türü bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya birim türü bulunamazsa, ya da birim türü
     *                          belirtilen mülke ait değilse, ya da isim çakışması
     *                          olursa.
     */
    UnitTypeDto updateUnitType(Long propertyId, Long unitTypeId, UnitTypeUpdateDto unitTypeUpdateDto);

    /**
     * Belirli bir mülke ait bir birim türünü siler.
     *
     * @param propertyId Silinecek birim türünün ait olduğu mülkün ID'si.
     * @param unitTypeId Silinecek birim türünün ID'si.
     * @throws RuntimeException Mülk veya birim türü bulunamazsa, ya da birim türü
     *                          belirtilen mülke ait değilse.
     *                          TODO: Birim türü silinirken bu türe sahip Sahipler
     *                          (Owner) varsa ne yapılmalı? PRD'de detay yok.
     */
    void deleteUnitType(Long propertyId, Long unitTypeId);
}