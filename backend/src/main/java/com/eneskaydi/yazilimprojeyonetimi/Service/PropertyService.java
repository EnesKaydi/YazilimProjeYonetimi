package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PropertyUpdateDto;

import java.util.List;

/**
 * Mülk (Property) ile ilgili iş mantığını yöneten servis arayüzü.
 * CRUD operasyonlarını ve mülkle ilgili diğer özel işlemleri içerir.
 */
public interface PropertyService {

    /**
     * Yeni bir mülk oluşturur.
     * Oluşturulan mülk, işlemi yapan (giriş yapmış) kullanıcı ile ilişkilendirilir.
     *
     * @param propertyCreateDto Oluşturulacak mülkün bilgilerini içeren DTO.
     * @return Oluşturulan mülkün bilgilerini içeren DTO.
     */
    PropertyDto createProperty(PropertyCreateDto propertyCreateDto);

    /**
     * Giriş yapmış olan kullanıcıya ait tüm mülkleri listeler.
     *
     * @return Kullanıcının mülklerinin listesi.
     */
    List<PropertyDto> getAllPropertiesForCurrentUser();

    /**
     * Belirli bir ID'ye sahip mülkü getirir.
     * Mülkün, işlemi yapan (giriş yapmış) kullanıcıya ait olup olmadığını kontrol
     * eder.
     *
     * @param propertyId Getirilecek mülkün ID'si.
     * @return İstenen mülkün bilgilerini içeren DTO.
     * @throws RuntimeException Mülk bulunamazsa veya kullanıcıya ait değilse.
     */
    PropertyDto getPropertyById(Long propertyId);

    /**
     * Belirli bir ID'ye sahip mülkü günceller.
     * Mülkün, işlemi yapan (giriş yapmış) kullanıcıya ait olup olmadığını kontrol
     * eder.
     *
     * @param propertyId        Güncellenecek mülkün ID'si.
     * @param propertyUpdateDto Güncellenecek mülk bilgilerini içeren DTO.
     * @return Güncellenmiş mülkün bilgilerini içeren DTO.
     * @throws RuntimeException Mülk bulunamazsa veya kullanıcıya ait değilse.
     */
    PropertyDto updateProperty(Long propertyId, PropertyUpdateDto propertyUpdateDto);

    /**
     * Belirli bir ID'ye sahip mülkü siler.
     * Mülkün, işlemi yapan (giriş yapmış) kullanıcıya ait olup olmadığını kontrol
     * eder.
     * Silme işlemi, ilişkili alt varlıkları da (UnitType, Owner vb.) gerekiyorsa
     * silmelidir.
     * Bu PRD'de TODO olarak belirtilmiş bir konudur.
     *
     * @param propertyId Silinecek mülkün ID'si.
     * @throws RuntimeException Mülk bulunamazsa veya kullanıcıya ait değilse.
     */
    void deleteProperty(Long propertyId);
}