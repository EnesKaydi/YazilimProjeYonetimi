package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.DocumentService;
import lombok.RequiredArgsConstructor;
// import org.springframework.core.io.Resource; // byte[] kullanılacağı için kaldırıldı
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException; // Eklendi
import java.util.List;

/**
 * Belge yönetimi (yükleme, indirme, listeleme, silme) işlemlerini yöneten REST
 * controller.
 * Belgeler genellikle bir mülke aittir.
 */
@RestController
@RequestMapping("/api/properties/{propertyId}/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Belirli bir mülk için yeni bir belge yükler.
     * DocumentCreateDto, dosyanın adı ve açıklaması gibi meta verileri içerebilir.
     *
     * @param propertyId Belgenin ilişkilendirileceği mülkün ID'si.
     * @param file       Yüklenecek dosya.
     * @param createDto  Belge meta verilerini içeren DTO (örn: açıklama).
     * @return HTTP 201 Created ile yüklenen belgenin bilgilerini içeren
     *         DocumentDto.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDto> uploadDocument(@PathVariable Long propertyId,
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute DocumentCreateDto createDto) throws IOException { // @Valid aktif edildi
        DocumentDto uploadedDocument = documentService.uploadDocument(propertyId, null, createDto, file); // ownerId
                                                                                                          // null
                                                                                                          // geçildi,
                                                                                                          // metod adı
                                                                                                          // düzeltildi
        return new ResponseEntity<>(uploadedDocument, HttpStatus.CREATED);
    }

    /**
     * Belirli bir mülke ait tüm belgeleri listeler.
     *
     * @param propertyId Belgelerin listeleneceği mülkün ID'si.
     * @return HTTP 200 OK ile mülke ait belgelerin listesi (DocumentDto listesi).
     */
    @GetMapping
    public ResponseEntity<List<DocumentDto>> getAllDocumentsForProperty(@PathVariable Long propertyId) {
        List<DocumentDto> documents = documentService.getDocumentsByProperty(propertyId);
        return ResponseEntity.ok(documents);
    }

    /**
     * Belirli bir ID'ye sahip belgeyi indirir.
     *
     * @param propertyId Belgenin ait olduğu mülkün ID'si.
     * @param documentId İndirilecek belgenin ID'si.
     * @return HTTP 200 OK ile belge dosyası.
     */
    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long propertyId, @PathVariable Long documentId)
            throws IOException { // Resource yerine byte[], throws IOException eklendi
        byte[] documentBytes = documentService.downloadDocument(propertyId, documentId);
        DocumentDto documentDto = documentService.getDocumentById(propertyId, documentId); // Dosya adını almak için
        String filename = documentDto.getFileName(); // Dto'dan dosya adı alındı (varsayım: Dto'da bu alan var)

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(documentBytes);
    }

    /**
     * Belirli bir ID'ye sahip belgenin meta verilerini getirir.
     *
     * @param propertyId Belgenin ait olduğu mülkün ID'si.
     * @param documentId Getirilecek belgenin ID'si.
     * @return HTTP 200 OK ile belge meta verilerini içeren DocumentDto.
     */
    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentDto> getDocumentDetails(@PathVariable Long propertyId,
            @PathVariable Long documentId) {
        DocumentDto documentDto = documentService.getDocumentById(propertyId, documentId); // Metod adı düzeltildi
        return ResponseEntity.ok(documentDto);
    }

    /**
     * Belirli bir ID'ye sahip belgenin meta verilerini günceller.
     *
     * @param propertyId        Belgenin ait olduğu mülkün ID'si.
     * @param documentId        Güncellenecek belgenin ID'si.
     * @param documentUpdateDto Güncellenecek meta verileri içeren DTO.
     * @return HTTP 200 OK ile güncellenmiş belge meta verilerini içeren
     *         DocumentDto.
     */
    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentDto> updateDocumentMeta(@PathVariable Long propertyId,
            @PathVariable Long documentId,
            @Valid @RequestBody DocumentUpdateDto documentUpdateDto) { // @Valid aktif edildi
        DocumentDto updatedDocument = documentService.updateDocumentMetadata(propertyId, documentId, documentUpdateDto); // Metod
                                                                                                                         // adı
                                                                                                                         // düzeltildi
        return ResponseEntity.ok(updatedDocument);
    }

    /**
     * Belirli bir ID'ye sahip belgeyi siler.
     *
     * @param propertyId Belgenin ait olduğu mülkün ID'si.
     * @param documentId Silinecek belgenin ID'si.
     * @return HTTP 204 No Content.
     */
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long propertyId, @PathVariable Long documentId)
            throws IOException { // throws IOException eklendi
        documentService.deleteDocument(propertyId, documentId);
        return ResponseEntity.noContent().build();
    }
}