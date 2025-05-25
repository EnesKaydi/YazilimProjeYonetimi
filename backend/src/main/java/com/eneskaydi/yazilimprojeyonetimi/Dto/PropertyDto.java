package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Mülk bilgilerini temsil eden DTO.
// GET /api/properties ve GET /api/properties/{propertyId} yanıtlarında kullanılabilir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDto {

    private Long id;
    private String name;
    private String address;
    private String propertyTitle;
    private String associationName;
    private String city;
    private String currency;
    private Long userId; // Mülkün sahibi olan kullanıcının ID'si
    private String username; // Mülkün sahibi olan kullanıcının adı (isteğe bağlı, gösterim için)

    // İlişkili diğer entity listeleri (UnitTypeDto, OwnerDto vb.) buraya
    // eklenebilir.
    // Örneğin:
    // private List<UnitTypeDto> unitTypes;
    // private List<OwnerDto> owners;
    // Şimdilik basit tutuyoruz.
}