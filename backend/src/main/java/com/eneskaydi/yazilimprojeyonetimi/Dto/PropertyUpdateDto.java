package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

// Mülk güncelleme isteği için DTO.
// API endpoint: PUT /api/properties/{propertyId}
// Tüm alanlar opsiyoneldir; sadece güncellenmek istenen alanlar gönderilir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUpdateDto {

    @NotBlank(message = "Mülk adı boş bırakılamaz")
    @Size(max = 255, message = "Mülk adı en fazla 255 karakter olabilir.")
    private String name;

    @Size(max = 500, message = "Adres en fazla 500 karakter olabilir.")
    private String address;

    @Size(max = 255, message = "Tapu bilgisi en fazla 255 karakter olabilir.")
    private String propertyTitle;

    @Size(max = 255, message = "Dernek adı en fazla 255 karakter olabilir.")
    private String associationName;

    @NotBlank(message = "Şehir boş bırakılamaz")
    @Size(max = 100, message = "Şehir adı en fazla 100 karakter olabilir.")
    private String city;

    @NotBlank(message = "Para birimi boş bırakılamaz")
    @Size(min = 3, max = 3, message = "Para birimi 3 karakterli ISO kodu olmalıdır (örn: TRY).")
    private String currency;
}