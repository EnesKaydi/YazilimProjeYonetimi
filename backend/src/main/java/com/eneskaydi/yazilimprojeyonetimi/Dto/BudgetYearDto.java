package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// Yıllık Bütçe bilgilerini temsil eden DTO.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetYearDto {

    private Long id;
    private int year;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance; // Yıl sonunda hesaplanıp güncellenebilir
    private BigDecimal totalIncome; // Yıl boyunca toplam gelir (hesaplanmış)
    private BigDecimal totalExpense; // Yıl boyunca toplam gider (hesaplanmış)
    private String description; // Bütçe yılı ile ilgili açıklamalar (notes -> description)
    private Long propertyId;

    // İsteğe bağlı olarak bu yıllık bütçeye ait aylık detaylar ve gelir kayıtları
    // da eklenebilir.
    // Henüz MonthlyBudgetDto ve IncomeRecordDto tanımlanmadığı için yorumda
    // bırakıldı.
    // private List<MonthlyBudgetDto> monthlyBudgets;
    // private List<IncomeRecordDto> incomeRecordsSummary; // Özet bilgiler veya tüm
    // kayıtlar
}