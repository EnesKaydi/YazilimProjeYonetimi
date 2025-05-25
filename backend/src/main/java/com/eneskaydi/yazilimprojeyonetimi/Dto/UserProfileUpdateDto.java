package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kullanıcının kendi profilini güncellemesi için DTO.
// API endpoint: PUT /api/users/me
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {

    // E-posta güncellenmek isteniyorsa. Format doğrulaması eklenmiştir.
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email; // Yeni e-posta adresi (opsiyonel)

    // Şifre güncellenmek isteniyorsa. Minimum uzunluk eklenebilir.
    @Size(min = 6, max = 100, message = "Şifre 6 ile 100 karakter arasında olmalıdır.")
    private String password; // Yeni şifre (opsiyonel)

    // Kullanıcıların hangi alanları güncelleyebileceği PRD'ye göre
    // detaylandırılmalıdır.
    // Örneğin, kullanıcı adı (username) değiştirilemez varsayılmıştır.
}