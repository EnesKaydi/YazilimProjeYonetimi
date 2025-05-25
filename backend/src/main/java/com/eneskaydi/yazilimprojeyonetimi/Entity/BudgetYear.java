package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// Mülkün yıllık bütçe bilgilerini temsil eden entity.
// Yıllık açılış ve kapanış bakiyelerini, ayrıca o yıla ait aylık bütçeleri ve gelir kayıtlarını içerir.
// Database_Prd.md [cite: 16, 17] gereksinimlerine göre.
@Entity
@Table(name = "budget_years", uniqueConstraints = {
        // Bir mülk için aynı yıl birden fazla kaydedilemez.
        @UniqueConstraint(columnNames = { "property_id", "year" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Bütçe yılı kaydının benzersiz kimliği

    @Column(nullable = false)
    private int year; // Bütçe yılı (örn: 2024, 2025)

    // Açılış bakiyesi, bir önceki yıldan devreden tutarı gösterir.
    // Finansal veriler için BigDecimal kullanılır.
    @Column(name = "opening_balance", precision = 19, scale = 4) // Daha yüksek hassasiyet gerekebilir
    private BigDecimal openingBalance;

    // Kapanış bakiyesi, yıl sonundaki net bakiyeyi gösterir.
    @Column(name = "closing_balance", precision = 19, scale = 4)
    private BigDecimal closingBalance;

    // Yıl boyunca elde edilen toplam gelir.
    // Bu alan, ilgili IncomeRecord'lar üzerinden hesaplanıp güncellenebilir.
    @Column(name = "total_income", precision = 19, scale = 4)
    private BigDecimal totalIncome;

    // Yıl boyunca yapılan toplam gider.
    // Bu alan, ilgili gider kayıtları (eğer varsa) veya aylık bütçe sonuçlarından
    // hesaplanabilir.
    @Column(name = "total_expense", precision = 19, scale = 4)
    private BigDecimal totalExpense;

    @Lob // Uzun metinler için
    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Bütçe yılıyla ilgili açıklamalar

    // Bu bütçe yılının ait olduğu mülk.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property; // Bu bütçe yılının ilişkili olduğu mülk

    // Bu bütçe yılına ait aylık bütçe detayları.
    // mappedBy MonthlyBudget entity'sindeki "budgetYear" alanına işaret eder.
    @OneToMany(mappedBy = "budgetYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyBudget> monthlyBudgets;

    // Bu bütçe yılı içinde gerçekleşen tüm gelir kayıtları.
    // mappedBy IncomeRecord entity'sindeki "budgetYear" alanına işaret eder.
    @OneToMany(mappedBy = "budgetYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomeRecord> incomeRecords;

    // Yıl sonu bütçe devir işlemleri gibi operasyonlar için servis katmanında
    // mantık geliştirilecektir.
    // Bu entity, bu işlemlerin temel veri yapısını oluşturur.
}