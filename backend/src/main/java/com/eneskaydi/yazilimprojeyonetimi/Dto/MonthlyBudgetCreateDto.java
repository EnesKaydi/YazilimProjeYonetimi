package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Yeni aylık bütçe oluşturma isteğini temsil eden Veri Transfer Nesnesi (DTO).
// Bu DTO, belirli bir yıllık bütçe için yeni bir aylık bütçe kaydı oluşturulurken kullanılır.
// API: POST /api/properties/{propertyId}/budget-years/{budgetYearId}/monthly-budgets
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBudgetCreateDto {

    // Bütçenin ait olacağı ay (1-12 arası bir değer).
    // Boş bırakılamaz.
    @NotNull(message = "Ay bilgisi boş olamaz.")
    @Min(value = 1, message = "Ay değeri 1 ile 12 arasında olmalıdır.")
    @Max(value = 12, message = "Ay değeri 1 ile 12 arasında olmalıdır.")
    private Integer month;

    // O ay için beklenen toplam gelir.
    // Opsiyoneldir, girilmezse 0 olarak kabul edilebilir. 0'dan büyük veya eşit
    // olmalıdır.
    @DecimalMin(value = "0.0", inclusive = true, message = "Beklenen gelir negatif olamaz.")
    private BigDecimal totalIncomeExpected;

    // O ay için planlanan/gerçekleşen toplam gider.
    // Opsiyoneldir, girilmezse 0 olarak kabul edilebilir. 0'dan büyük veya eşit
    // olmalıdır.
    @DecimalMin(value = "0.0", inclusive = true, message = "Toplam gider negatif olamaz.")
    private BigDecimal totalExpensesActual;

    // Yıl bilgisi, URL'deki {budgetYearId} path parametresinden alınır,
    // bu nedenle bu DTO içerisinde ayrıca belirtilmesine gerek yoktur.
    // Aynı şekilde propertyId de path parametresinden gelir.
}