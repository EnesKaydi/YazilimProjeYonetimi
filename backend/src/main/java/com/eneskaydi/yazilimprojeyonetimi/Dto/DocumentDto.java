package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Belge bilgilerini temsil eden DTO sınıfı.
// Bu DTO, belgeleri listelerken veya detaylarını gösterirken kullanılır.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    // Belgenin benzersiz kimliği
    private Long id;

    // Kullanıcıya gösterilen orijinal dosya adı
    private String fileName;

    // Dosyanın MIME türü (örn: "application/pdf", "image/jpeg")
    private String fileType;

    // Dosyanın boyutu byte cinsinden.
    private Long size;

    // Belgenin sisteme yüklendiği tarih ve saat
    private LocalDateTime uploadDate;

    // Belge hakkında kullanıcı tarafından girilen açıklama (opsiyonel)
    private String description;

    // Belgenin zorunlu olarak ilişkili olduğu mülkün kimliği
    private Long propertyId;

    // Belgenin isteğe bağlı olarak ilişkili olduğu mülk sahibinin kimliği
    // (opsiyonel)
    private Long ownerId;
}