package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Yeni belge yükleme isteğini (metadata) temsil eden DTO sınıfı
// Dosya içeriği (MultipartFile) controller metodunda ayrıca alınacaktır.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCreateDto {

    // Yüklenecek dosyanın adı.
    // Bu alan istemci tarafından dosya seçildiğinde otomatik olarak doldurulabilir
    // veya kullanıcı tarafından girilebilir. Boş bırakılamaz.
    // Not: Gerçek dosya adı MultipartFile nesnesinden de alınabilir.
    // Bu alan, isteğe bağlı olarak istemcinin belirttiği bir adı temsil edebilir.
    @NotBlank(message = "Dosya adı boş bırakılamaz.")
    @Size(max = 255, message = "Dosya adı en fazla 255 karakter olabilir.")
    private String fileName;

    // Belge ile ilgili kullanıcı tarafından girilen açıklama.
    // Opsiyoneldir, en fazla 1000 karakter olabilir.
    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir.")
    private String description;

    // `propertyId` URL path parametresinden alınır.
    // `ownerId` şu anki implementasyonda doğrudan belge ile ilişkilendirilmiyor;
    // gerekirse servis katmanında yönetilebilir veya DTO'ya eklenebilir.
    // Bu DTO'dan `propertyId` ve `ownerId` alanları kaldırılmıştır.
}