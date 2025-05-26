package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kullanıcının şifre değişikliği talebi için DTO.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDto {

    // Kullanıcının mevcut şifresi.
    @NotBlank(message = "Mevcut şifre boş bırakılamaz.")
    private String currentPassword;

    // Kullanıcının belirlediği yeni şifre.
    @NotBlank(message = "Yeni şifre boş bırakılamaz.")
    @Size(min = 6, message = "Yeni şifre en az 6 karakter olmalıdır.") // Örnek bir validasyon
    private String newPassword;

    // Yeni şifrenin teyidi.
    @NotBlank(message = "Yeni şifre tekrarı boş bırakılamaz.")
    private String confirmNewPassword;
} 