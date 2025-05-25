package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// Belge yönetimi işlemlerinden sorumlu servis arayüzü.
// Bu servis, mülklere, sahiplere veya diğer entity'lere bağlı belgelerin
// yüklenmesi, indirilmesi, listelenmesi ve silinmesi gibi işlemleri yönetir.
public interface DocumentService {

    /**
     * Bir mülk ve isteğe bağlı olarak bir sahip ile ilişkili yeni bir belge
     * oluşturur/yükler.
     *
     * @param propertyId        Belgenin ilişkilendirileceği mülkün ID'si.
     * @param ownerId           (Opsiyonel) Belgenin ayrıca ilişkilendirileceği mülk
     *                          sahibinin ID'si.
     * @param documentCreateDto Belge meta verilerini ve isteğe bağlı olarak dosya
     *                          adını içeren DTO.
     * @param file              Yüklenecek belge dosyası.
     * @return Oluşturulan/yüklenen belge bilgilerini içeren DTO.
     * @throws IOException      Dosya işleme sırasında bir hata oluşursa.
     * @throws RuntimeException Mülk veya sahip bulunamazsa.
     */
    DocumentDto uploadDocument(Long propertyId, Long ownerId, DocumentCreateDto documentCreateDto, MultipartFile file)
            throws IOException;

    /**
     * Belirli bir mülke ait tüm belgeleri listeler.
     *
     * @param propertyId Belgeleri listelenecek mülkün ID'si.
     * @return Mülke ait belgelerin listesi.
     * @throws RuntimeException Mülk bulunamazsa.
     */
    List<DocumentDto> getDocumentsByProperty(Long propertyId);

    /**
     * Belirli bir mülk sahibine ait tüm belgeleri listeler.
     *
     * @param propertyId Mülkün ID'si (doğrulama için).
     * @param ownerId    Belgeleri listelenecek mülk sahibinin ID'si.
     * @return Mülk sahibine ait belgelerin listesi.
     * @throws RuntimeException Mülk veya sahip bulunamazsa.
     */
    List<DocumentDto> getDocumentsByOwner(Long propertyId, Long ownerId);

    /**
     * Belirli bir belgeyi ID'sine göre getirir.
     *
     * @param propertyId Mülkün ID'si (doğrulama için).
     * @param documentId Getirilecek belgenin ID'si.
     * @return Belge bilgilerini içeren DTO.
     * @throws RuntimeException Belge bulunamazsa veya belirtilen mülke ait değilse.
     */
    DocumentDto getDocumentById(Long propertyId, Long documentId);

    /**
     * Belirli bir belgeyi ID'sine göre indirir (byte dizisi olarak döndürür).
     *
     * @param propertyId Mülkün ID'si.
     * @param documentId İndirilecek belgenin ID'si.
     * @return Belge içeriğinin byte dizisi.
     * @throws RuntimeException Belge bulunamazsa veya okunamıyorsa.
     * @throws IOException      Dosya okuma sırasında hata oluşursa.
     */
    byte[] downloadDocument(Long propertyId, Long documentId) throws IOException;

    /**
     * Mevcut bir belgenin meta verilerini günceller (örn: adı, açıklaması).
     * Dosya içeriğinin güncellenmesi genellikle silip yeniden yükleme ile yapılır.
     *
     * @param propertyId        Mülkün ID'si.
     * @param documentId        Güncellenecek belgenin ID'si.
     * @param documentUpdateDto Güncellenecek belge bilgilerini içeren DTO.
     * @return Güncellenmiş belge bilgilerini içeren DTO.
     * @throws RuntimeException Belge bulunamazsa.
     */
    DocumentDto updateDocumentMetadata(Long propertyId, Long documentId, DocumentUpdateDto documentUpdateDto);

    /**
     * Bir belgeyi siler.
     *
     * @param propertyId Mülkün ID'si.
     * @param documentId Silinecek belgenin ID'si.
     * @throws RuntimeException Belge bulunamazsa.
     * @throws IOException      Dosya silme sırasında hata oluşursa.
     */
    void deleteDocument(Long propertyId, Long documentId) throws IOException;
}