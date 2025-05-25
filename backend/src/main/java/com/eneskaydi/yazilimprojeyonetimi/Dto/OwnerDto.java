package com.eneskaydi.yazilimprojeyonetimi.Dto;

import com.eneskaydi.yazilimprojeyonetimi.Entity.DisplayAsType; // Enum importu
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Sahip/Kiracı bilgilerini temsil eden DTO.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDto {

    private Long id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String bankAccountNumber;
    private String deedPollNumber;
    private boolean isTenantOccupied;
    private DisplayAsType displayAs; // MAL_SAHIBI veya KIRACI

    private Long unitTypeId; // İlişkili birim türünün ID'si
    private String unitTypeName; // İlişkili birim türünün adı (gösterim kolaylığı için)

    private Long propertyId; // Ait olduğu mülkün ID'si

    // Eğer isTenantOccupied true ise ve displayAs KIRACI ise bu alan dolu olur.
    private TenantDto tenantDetails;
}