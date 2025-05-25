package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kiracı detaylarını temsil eden DTO.
// OwnerDto içinde kullanılacak.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDto {

    private Long id; // Tenant entity'sinin ID'si (gerekirse)
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String bankAccountNumber;
}