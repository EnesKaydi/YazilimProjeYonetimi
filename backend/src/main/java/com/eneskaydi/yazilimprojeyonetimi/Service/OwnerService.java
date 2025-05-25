package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.OwnerCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.OwnerDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.OwnerUpdateDto;

import java.util.List;

// Sahip ve Kiracı yönetimi işlemlerinden sorumlu servis arayüzü.
public interface OwnerService {

    /**
     * Belirli bir mülk için yeni bir sahip/kiracı oluşturur.
     *
     * @param propertyId     Sahip/kiracının ekleneceği mülkün ID'si.
     * @param ownerCreateDto Oluşturulacak sahip/kiracı bilgilerini içeren DTO.
     * @return Oluşturulan sahip/kiracı bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya birim türü bulunamazsa, e-posta çakışması
     *                          olursa vb.
     */
    OwnerDto createOwner(Long propertyId, OwnerCreateDto ownerCreateDto);

    /**
     * Belirli bir mülke ait tüm sahipleri/kiracıları listeler.
     *
     * @param propertyId Sahipleri/kiracıları listelenecek mülkün ID'si.
     * @return Mülke ait sahip/kiracı listesi.
     * @throws RuntimeException Mülk bulunamazsa.
     */
    List<OwnerDto> getOwnersByProperty(Long propertyId);

    /**
     * Belirli bir mülke ait spesifik bir sahibi/kiracıyı ID'sine göre getirir.
     *
     * @param propertyId Sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId    Getirilecek sahip/kiracının ID'si.
     * @return Sahip/kiracı bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya sahip/kiracı bulunamazsa, ya da
     *                          sahip/kiracı belirtilen mülke ait değilse.
     */
    OwnerDto getOwnerById(Long propertyId, Long ownerId);

    /**
     * Belirli bir mülke ait bir sahibi/kiracıyı günceller.
     *
     * @param propertyId     Güncellenecek sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId        Güncellenecek sahip/kiracının ID'si.
     * @param ownerUpdateDto Güncellenecek sahip/kiracı bilgilerini içeren DTO.
     * @return Güncellenmiş sahip/kiracı bilgilerini içeren DTO.
     * @throws RuntimeException Mülk, sahip/kiracı veya birim türü bulunamazsa,
     *                          e-posta çakışması olursa vb.
     */
    OwnerDto updateOwner(Long propertyId, Long ownerId, OwnerUpdateDto ownerUpdateDto);

    /**
     * Belirli bir mülke ait bir sahibi/kiracıyı siler.
     *
     * @param propertyId Silinecek sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId    Silinecek sahip/kiracının ID'si.
     * @throws RuntimeException Mülk veya sahip/kiracı bulunamazsa, ya da
     *                          sahip/kiracı belirtilen mülke ait değilse.
     *                          TODO: Sahip silinirken ilişkili Gelir Kayıtları
     *                          (IncomeRecord) veya Belgeler (Document) varsa ne
     *                          yapılmalı?
     */
    void deleteOwner(Long propertyId, Long ownerId);

    /**
     * Belirli bir mülke ait bir sahibi/kiracıyı çoğaltır.
     * Çoğaltılan kaydın bazı alanları (örn: tapu no) benzersiz olmalıdır veya
     * kullanıcı tarafından düzenlenmelidir.
     *
     * @param propertyId Sahip/kiracının ait olduğu mülkün ID'si.
     * @param ownerId    Çoğaltılacak sahip/kiracının ID'si.
     * @return Çoğaltılmış yeni sahip/kiracı bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya kaynak sahip/kiracı bulunamazsa.
     */
    OwnerDto duplicateOwner(Long propertyId, Long ownerId);

}