package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kullanıcı kayıt isteği için veri taşıma nesnesi (DTO).
// API endpoint: POST /api/auth/register
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    // Kullanıcının tam adı (ad soyad). Boş olamaz.
    // Bu alan, User entity'sindeki 'username' alanına map edilecek.
    @NotBlank(message = "Kullanıcı adı (Ad Soyad) boş bırakılamaz")
    @Size(min = 3, max = 100, message = "Kullanıcı adı (Ad Soyad) en az 3, en fazla 100 karakter olmalıdır")
    private String username;

    // Kullanıcının e-posta adresi. Geçerli bir formatta ve boş olamaz.
    @NotBlank(message = "E-posta boş bırakılamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 100, message = "E-posta en fazla 100 karakter olabilir")
    private String email;

    // Kullanıcı şifresi. Boş olamaz.
    // Güvenlik gereksinimleri (örn: minimum uzunluk, karmaşıklık) daha sonra eklenebilir.
    @NotBlank(message = "Şifre boş bırakılamaz")
    @Size(min = 4, max = 100, message = "Şifre en az 4, en fazla 100 karakter olmalıdır")
    private String password;

    // Frontend tarafında şifre tekrarı istenecek ve orada doğrulanacak.
    // Bu DTO'ya 'confirmPassword' eklenmesine gerek yoktur.
} 