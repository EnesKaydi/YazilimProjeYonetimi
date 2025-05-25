package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

// Kiracı güncelleme (OwnerUpdateDto içinde) için DTO.
// Tüm alanlar opsiyoneldir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantUpdateDto {

    @NotBlank(message = "Kiracı tam adı boş bırakılamaz")
    @Size(max = 255, message = "Kiracı tam adı en fazla 255 karakter olabilir")
    private String fullName;

    @Size(max = 500, message = "Kiracı adresi en fazla 500 karakter olabilir")
    private String address;

    @Size(max = 20, message = "Kiracı telefon numarası en fazla 20 karakter olabilir")
    private String phoneNumber;

    @Email(message = "Geçerli bir kiracı e-posta adresi giriniz")
    @Size(max = 255, message = "Kiracı e-postası en fazla 255 karakter olabilir")
    private String email;

    @Size(max = 50, message = "Kiracı banka hesap numarası en fazla 50 karakter olabilir")
    private String bankAccountNumber;
}