package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Aylık bütçe bilgilerini temsil eden DTO sınıfı
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBudgetDto {

    // Aylık bütçenin benzersiz kimliği
    private Long id;

    // Bütçenin ait olduğu ay (1-12)
    private Integer month;

    // Bütçenin ait olduğu yıl
    private Integer year;

    // O ay için beklenen toplam gelir
    private BigDecimal totalIncomeExpected;

    // O ay için gerçekleşen toplam giderler (sonuç)
    private BigDecimal totalExpensesActual;

    // O ayki bakiye (beklenen gelir - gerçekleşen giderler)
    private BigDecimal balance;

    // Bu aylık bütçenin bağlı olduğu yıllık bütçenin kimliği
    private Long budgetYearId;
}