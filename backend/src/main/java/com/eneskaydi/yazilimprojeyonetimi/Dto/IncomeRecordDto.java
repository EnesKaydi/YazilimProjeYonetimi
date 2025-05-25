package com.eneskaydi.yazilimprojeyonetimi.Dto;

import com.eneskaydi.yazilimprojeyonetimi.Entity.IncomeStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// Gelir kaydı bilgilerini göstermek için kullanılan DTO.
// Servis katmanından Controllera ve son kullanıcıya veri taşır.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeRecordDto {

    private Long id;

    // Gelirin ait olduğu ay (1-12)
    private Integer month;

    // Beklenen ödeme tarihi (vade tarihi)
    private LocalDate dueDate;

    // Beklenen gelir miktarı
    private BigDecimal expectedAmount;

    // Ödenen miktar
    private BigDecimal paidAmount;

    // Ödeme tarihi (eğer ödendiyse)
    private LocalDate paymentDate;

    // Gelir kaydının durumu
    private IncomeStatusType status;

    // Gelir kaydı ile ilgili notlar (opsiyonel)
    private String notes;

    // İlişkili Mülk Sahibinin ID'si
    private Long ownerId;

    // İlişkili Mülk Sahibinin Adı Soyadı (Gösterim için)
    private String ownerFullName;

    // İlişkili Bütçe Yılının ID'si
    private Long budgetYearId;

    // İlişkili Bütçe Yılı (örn: 2024)
    private Integer budgetYear; // Sadece yıl bilgisi

    // private Long monthlyBudgetId; // İleride gerekirse eklenebilir
}