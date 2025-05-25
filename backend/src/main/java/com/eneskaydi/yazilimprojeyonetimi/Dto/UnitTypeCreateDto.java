package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

// Yeni Birim Türü oluşturma isteği için DTO.
// API: POST /api/properties/{propertyId}/unit-types
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitTypeCreateDto {

    @NotBlank(message = "Birim türü adı boş bırakılamaz.")
    @Size(max = 255, message = "Birim türü adı en fazla 255 karakter olabilir.")
    private String name;

    @NotNull(message = "Aylık ücret boş bırakılamaz.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Aylık ücret negatif olamaz.")
    private BigDecimal monthlyFee;

    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir.")
    private String description;

    // propertyId yol parametresi olarak alınacağı için DTO'da olmasına gerek yok.
    // Servis katmanında Property nesnesi bulunup UnitType'a set edilecek.
}