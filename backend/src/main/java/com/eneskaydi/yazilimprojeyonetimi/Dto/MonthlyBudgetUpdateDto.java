package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Mevcut aylık bütçeyi güncelleme isteğini temsil eden Veri Transfer Nesnesi (DTO).
// Bu DTO, var olan bir aylık bütçe kaydının detaylarını değiştirmek için kullanılır.
// API: PUT /api/properties/{propertyId}/budget-years/{budgetYearId}/monthly-budgets/{monthlyBudgetId}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBudgetUpdateDto {

    // Güncellenecek aylık beklenen toplam gelir.
    // Opsiyoneldir. 0'dan büyük veya eşit olmalıdır.
    @DecimalMin(value = "0.0", inclusive = true, message = "Beklenen gelir negatif olamaz.")
    private BigDecimal totalIncomeExpected;

    // Güncellenecek aylık toplam gider/sonuç.
    // Opsiyoneldir. 0'dan büyük veya eşit olmalıdır.
    @DecimalMin(value = "0.0", inclusive = true, message = "Toplam gider negatif olamaz.")
    private BigDecimal totalExpensesActual;

    // Ay (month) ve yıl (year) bilgileri genellikle güncelleme senaryolarında
    // değiştirilmez;
    // bu nedenle bu DTO'da yer almazlar. Eğer ay/yıl değişikliği gerekirse,
    // mevcut kaydın silinip yeni bir kayıt oluşturulması daha doğru bir yaklaşım
    // olabilir.
}