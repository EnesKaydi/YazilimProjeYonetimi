package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Yeni bir mülk (Property) oluşturma isteği için DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyCreateDto {

    @NotBlank(message = "Mülk adı boş bırakılamaz")
    @Size(max = 255, message = "Mülk adı en fazla 255 karakter olabilir")
    private String name; // Mülk adı

    @Size(max = 500, message = "Adres en fazla 500 karakter olabilir")
    private String address; // Mülkün tam adresi (opsiyonel olabilir, PRD'ye göre netleştirilmeli)

    @Size(max = 255, message = "Tapu bilgisi en fazla 255 karakter olabilir")
    private String propertyTitle; // Tapu Bilgisi (opsiyonel olabilir)

    @Size(max = 255, message = "Dernek adı en fazla 255 karakter olabilir")
    private String associationName; // Dernek Adı (opsiyonel olabilir)

    @NotBlank(message = "Şehir boş bırakılamaz")
    @Size(max = 100, message = "Şehir adı en fazla 100 karakter olabilir")
    private String city; // Şehir (tüm dünya şehirleri desteklenecek)

    @NotBlank(message = "Para birimi boş bırakılamaz")
    @Size(min = 3, max = 3, message = "Para birimi 3 karakterli ISO kodu olmalıdır (örn: TRY)")
    private String currency; // Para Birimi (tüm dünya ülkelerinin para birimleri desteklenecek, ISO 4217
                             // kodu)

    // user_id bu DTO'ya eklenmeyecek, çünkü mülkü oluşturan kullanıcı
    // genellikle güvenlik katmanından (SecurityContextHolder) alınır.
}