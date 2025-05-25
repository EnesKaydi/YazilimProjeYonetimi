package com.eneskaydi.yazilimprojeyonetimi.Dto;

import com.eneskaydi.yazilimprojeyonetimi.Entity.DisplayAsType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Yeni Sahip/Kiracı oluşturma isteği için DTO.
// API: POST /api/properties/{propertyId}/owners
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerCreateDto {

    @NotBlank(message = "Tam ad boş bırakılamaz.")
    @Size(max = 255, message = "Tam ad en fazla 255 karakter olabilir")
    private String fullName;

    @Size(max = 500, message = "Adres en fazla 500 karakter olabilir")
    private String address;

    @Size(max = 20, message = "Telefon numarası en fazla 20 karakter olabilir")
    private String phoneNumber;

    @NotBlank(message = "E-posta boş bırakılamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 255, message = "E-posta en fazla 255 karakter olabilir")
    private String email;

    @Size(max = 50, message = "Banka hesap numarası en fazla 50 karakter olabilir")
    private String bankAccountNumber;

    @Size(max = 100, message = "Tapu numarası en fazla 100 karakter olabilir")
    private String deedPollNumber; // Tapu numarası (opsiyonel olabilir)

    @NotNull(message = "'Kiralanmış mı?' durumu belirtilmelidir.")
    private Boolean isTenantOccupied;

    @NotNull(message = "Gösterim türü (Mal Sahibi/Kiracı) belirtilmelidir.")
    private DisplayAsType displayAs;

    @NotNull(message = "Birim türü ID'si boş bırakılamaz")
    private Long unitTypeId; // Atanacak birim türünün ID'si (opsiyonel olabilir)

    // Eğer isTenantOccupied true ise ve displayAs KIRACI ise, bu alan dolu olmalı.
    // @Valid ile iç içe DTO'nun da validasyonu tetiklenir.
    @Valid
    private TenantCreateDto tenantDetails; // Kiracı oluşturmak için ayrı bir DTO kullanalım

    // propertyId yol parametresi olarak alınacak.
}