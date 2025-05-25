package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Size;

// Kullanıcı girişi (login) isteği için veri taşıma nesnesi (DTO).
// API endpoint: POST /api/auth/login
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    // Kullanıcı e-postası. Geçerli bir formatta ve boş olamaz.
    @NotBlank(message = "E-posta boş bırakılamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String email;

    // Kullanıcı şifresi. Boş olamaz.
    @NotBlank(message = "Şifre boş bırakılamaz")
    @Size(min = 4, message = "Şifre en az 4 karakter olmalıdır")
    private String password;
}