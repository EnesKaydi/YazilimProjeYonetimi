package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Mevcut bir birim türünü (UnitType) güncelleme isteği için DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitTypeUpdateDto {

    @NotBlank(message = "Birim türü adı boş bırakılamaz")
    @Size(max = 255, message = "Birim türü adı en fazla 255 karakter olabilir")
    private String name;

    @NotNull(message = "Aylık ücret boş bırakılamaz")
    @DecimalMin(value = "0.0", inclusive = true, message = "Aylık ücret negatif olamaz")
    private BigDecimal monthlyFee;

    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir")
    private String description;
}