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
    @Size(max = 255, message = "E-posta en fazla 255 karakter olabilir") // User entity ile uyumlu
    private String email; // Yeni e-posta adresi (opsiyonel)

    // Şifre güncellenmek isteniyorsa. Minimum uzunluk eklenebilir.
    // Şifre değişikliği için ayrı bir endpoint (/api/users/me/change-password) olduğundan
    // bu alan buradan kaldırılabilir veya sadece admin tarafından kullanılabilir.
    // Şimdilik bırakıyorum, ancak profil güncellemede şifre göndermek yaygın bir pratik değildir.
    @Size(min = 6, max = 100, message = "Şifre 6 ile 100 karakter arasında olmalıdır.")
    private String password; // Yeni şifre (opsiyonel - dikkatli kullanılmalı)

    @Size(max = 20, message = "Telefon numarası en fazla 20 karakter olabilir") // User entity ile uyumlu
    private String phoneNumber; // Yeni telefon numarası (opsiyonel)

    @Size(max = 1000, message = "Adres en fazla 1000 karakter olabilir") // User entity @Lob olduğu için daha uzun olabilir
    private String address; // Yeni adres (opsiyonel)

    // Kullanıcıların hangi alanları güncelleyebileceği PRD'ye göre
    // detaylandırılmalıdır.
    // Örneğin, kullanıcı adı (username) ve rol (role) değiştirilemez varsayılmıştır.
}