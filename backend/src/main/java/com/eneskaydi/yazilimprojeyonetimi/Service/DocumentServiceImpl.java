package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.DocumentUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Document;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Owner;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import com.eneskaydi.yazilimprojeyonetimi.Repository.DocumentRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.OwnerRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.PropertyRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Value; // Dosya yolu için
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// Belge yönetimi servisinin implementasyonu.
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    // todo: Dosya depolama yolunu application.properties'ten al veya daha güvenli
    // bir yöntem kullan.
    // @Value("${file.upload-dir:./uploads}")
    private String uploadDir = "./uploads"; // Geçici varsayılan yükleme dizini

    // Mevcut kullanıcıyı (geçici olarak) getiren yardımcı metot.
    private User getCurrentUser() {
        return userRepository.findById(1L)
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
    public DocumentDto uploadDocument(Long propertyId, Long ownerId, DocumentCreateDto documentCreateDto,
            MultipartFile file) throws IOException {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Owner owner = null;
        if (ownerId != null) {
            owner = ownerRepository.findByIdAndProperty(ownerId, property)
                    .orElseThrow(() -> new RuntimeException("Sahip (ID: " + ownerId +
                            ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));
        }

        if (file.isEmpty()) {
            throw new RuntimeException("Yüklenecek dosya boş olamaz.");
        }

        // Benzersiz bir dosya adı oluştur (UUID + orijinal dosya adı)
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        int lastDot = originalFileName.lastIndexOf('.');
        if (lastDot > 0) {
            fileExtension = originalFileName.substring(lastDot);
        }
        String storedFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = Paths.get(uploadDir).resolve(storedFileName);

        // Hedef dizin yoksa oluştur
        Files.createDirectories(targetLocation.getParent());

        // Dosyayı depolama alanına kopyala
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setFileName(originalFileName); // Kullanıcıya gösterilecek orijinal ad
        document.setStoredFileName(storedFileName); // Depolama sistemindeki ad
        document.setFileType(file.getContentType());
        document.setSize(file.getSize());
        document.setUploadDate(LocalDateTime.now());
        document.setDescription(documentCreateDto.getDescription());
        document.setProperty(property);
        if (owner != null) {
            document.setOwner(owner);
        }
        // document.setFilePath(targetLocation.toString()); // Veya sadece
        // storedFileName ve baseDir tutulabilir.

        Document savedDocument = documentRepository.save(document);
        return mapToDocumentDto(savedDocument);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsByProperty(Long propertyId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        return documentRepository.findByProperty(property)
                .stream()
                .map(this::mapToDocumentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsByOwner(Long propertyId, Long ownerId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId); // Mülk kontrolü
        Owner owner = ownerRepository.findByIdAndProperty(ownerId, property)
                .orElseThrow(() -> new RuntimeException("Sahip (ID: " + ownerId +
                        ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));
        return documentRepository.findByOwner(owner)
                .stream()
                .map(this::mapToDocumentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentDto getDocumentById(Long propertyId, Long documentId) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId); // Mülk kontrolü
        Document document = documentRepository.findById(documentId)
                .filter(doc -> doc.getProperty().getId().equals(property.getId())) // Mülke ait mi kontrolü
                .orElseThrow(() -> new RuntimeException("Belge (ID: " + documentId +
                        ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));
        return mapToDocumentDto(document);
    }

    @Override
    public byte[] downloadDocument(Long propertyId, Long documentId) throws IOException {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Document document = documentRepository.findById(documentId)
                .filter(doc -> doc.getProperty().getId().equals(property.getId()))
                .orElseThrow(() -> new RuntimeException("İndirilecek belge (ID: " + documentId +
                        ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        // Dosya yolunu oluştur (storedFileName ve uploadDir kullanarak)
        Path filePath = Paths.get(uploadDir).resolve(document.getStoredFileName());
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new RuntimeException("Belge dosyası bulunamadı veya okunamıyor: " + document.getFileName());
        }
        return Files.readAllBytes(filePath);
    }

    @Override
    @Transactional
    public DocumentDto updateDocumentMetadata(Long propertyId, Long documentId, DocumentUpdateDto documentUpdateDto) {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Document document = documentRepository.findById(documentId)
                .filter(doc -> doc.getProperty().getId().equals(property.getId()))
                .orElseThrow(() -> new RuntimeException("Güncellenecek belge (ID: " + documentId +
                        ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        if (documentUpdateDto.getFileName() != null) {
            document.setFileName(documentUpdateDto.getFileName());
        }
        if (documentUpdateDto.getDescription() != null) {
            document.setDescription(documentUpdateDto.getDescription());
        }
        // Diğer meta veriler (owner gibi) güncellenecekse burada eklenebilir.

        Document updatedDocument = documentRepository.save(document);
        return mapToDocumentDto(updatedDocument);
    }

    @Override
    @Transactional
    public void deleteDocument(Long propertyId, Long documentId) throws IOException {
        Property property = getPropertyIfBelongsToCurrentUser(propertyId);
        Document document = documentRepository.findById(documentId)
                .filter(doc -> doc.getProperty().getId().equals(property.getId()))
                .orElseThrow(() -> new RuntimeException("Silinecek belge (ID: " + documentId +
                        ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        // Fiziksel dosyayı sil
        Path filePath = Paths.get(uploadDir).resolve(document.getStoredFileName());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Loglama yapılabilir, ancak işlem devam etmeli (veritabanı kaydı silinecek).
            System.err.println("Fiziksel dosya silinirken hata oluştu: " + filePath + " - " + e.getMessage());
            // throw new IOException("Belge dosyası silinemedi: " + document.getFileName(),
            // e); // İsteğe bağlı olarak hata fırlatılabilir
        }

        documentRepository.delete(document);
    }

    // Document entity'sini DocumentDto'ya mapleyen yardımcı metot
    private DocumentDto mapToDocumentDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setFileName(document.getFileName());
        dto.setFileType(document.getFileType());
        dto.setSize(document.getSize());
        dto.setUploadDate(document.getUploadDate());
        dto.setDescription(document.getDescription());
        if (document.getProperty() != null) {
            dto.setPropertyId(document.getProperty().getId());
        }
        if (document.getOwner() != null) {
            dto.setOwnerId(document.getOwner().getId());
        }
        // dto.setDownloadUrl(...); // İndirme linki controller katmanında
        // oluşturulabilir.
        return dto;
    }
}