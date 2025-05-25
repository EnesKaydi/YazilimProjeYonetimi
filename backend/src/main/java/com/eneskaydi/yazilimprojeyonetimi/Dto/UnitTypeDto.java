package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

// Birim Türü bilgilerini temsil eden DTO.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitTypeDto {

    private Long id;
    private String name; // Örn: Büyük Daire, Küçük Daire, Garaj
    private BigDecimal monthlyFee; // Aylık Ücret
    private String description;
    private Long propertyId; // Ait olduğu mülkün ID'si

    // Bu DTO'ya OwnerDto listesi de eklenebilir (bu birim türüne sahip olanlar).
    // private List<OwnerDto> owners;
}