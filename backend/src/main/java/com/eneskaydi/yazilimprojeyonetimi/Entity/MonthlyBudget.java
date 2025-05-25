package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

// Yıllık bütçenin aylık detaylarını temsil eden entity.
// Beklenen toplam geliri ve gerçekleşen toplam giderleri/sonuçları içerir.
// Database_Prd.md [cite: 19, 20] gereksinimlerine göre.
@Entity
@Table(name = "monthly_budgets", uniqueConstraints = {
        // Bir bütçe yılı içinde aynı ay birden fazla kaydedilemez.
        @UniqueConstraint(columnNames = { "budget_year_id", "month" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Aylık bütçe kaydının benzersiz kimliği

    // Ayı temsil eder (1: Ocak, 12: Aralık).
    // @Min(1) @Max(12) gibi bean validation anotasyonları eklenebilir.
    @Column(nullable = false)
    private int month;

    // O ay için beklenen toplam gelir.
    // Finansal veriler için BigDecimal kullanılır.
    @Column(name = "total_income_expected", precision = 19, scale = 4)
    private BigDecimal totalIncomeExpected;

    // O ay için gerçekleşen toplam giderler (sonuç).
    // PRD'de "sonuçlar '-' sembolü ile gösterilmelidir", bu gösterim UI
    // katmanında veya raporlamada yapılır.
    // Veritabanında pozitif bir değer olarak saklanması daha standarttır.
    @Column(name = "total_expenses_actual", precision = 19, scale = 4)
    private BigDecimal totalExpensesActual;

    // Bu aylık bütçenin ait olduğu bütçe yılı.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_year_id", nullable = false)
    private BudgetYear budgetYear; // Bu aylık bütçenin ilişkili olduğu bütçe yılı

    // Bakiye (Gelir - Gider) gibi hesaplanmış değerler genellikle veritabanında
    // saklanmaz,
    // ihtiyaç duyulduğunda hesaplanır. Ancak sık kullanılıyorsa ve performans
    // kritikse
    // bir view oluşturulabilir veya @Formula anotasyonu ile JPA entity'sinde
    // tanımlanabilir.
}