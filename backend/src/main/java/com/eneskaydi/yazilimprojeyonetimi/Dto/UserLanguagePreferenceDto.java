package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Kullanıcı dil tercihi için DTO sınıfı
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLanguagePreferenceDto {

    // Dil kodu (örn: "en" için İngilizce, "fr" için Fransızca)
    @NotBlank(message = "Dil kodu boş olamaz.")
    @Size(min = 2, max = 5, message = "Dil kodu 2 ile 5 karakter arasında olmalıdır.")
    private String languageCode;
}