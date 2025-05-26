package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate; // Ödeme tarihi için LocalDate daha uygun

// Gelir matrisindeki ödeme kayıtlarını temsil eden entity.
// Bir sahibin belirli bir aydaki ödeme durumunu, tutarlarını ve diğer detaylarını saklar.
// Database_Prd.md [cite: 22, 23, 24, 25, 26, 27, 28] gereksinimlerine göre.
@Entity
@Table(name = "income_records", uniqueConstraints = {
    // Bir bütçe yılında, belirli bir ay için sadece bir gelir kaydı olmalı.
    @UniqueConstraint(columnNames = { "budget_year_id", "month" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Kayıt ID

    // Bu gelir kaydının ait olduğu bütçe yılı (BudgetYear).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_year_id", nullable = false)
    private BudgetYear budgetYear; // İlişkili bütçe yılı

    // Ödemenin ait olduğu ay (1: Ocak, 12: Aralık).
    @Column(nullable = false)
    private int month; // Ay (1-12)

    // Beklenen ödeme tarihi (vade tarihi).
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    // O ay için ödenmesi beklenen toplam tutar.
    @Column(name = "expected_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal expectedAmount;

    // O ay için ödenen toplam tutar.
    @Column(name = "paid_amount", precision = 19, scale = 4)
    private BigDecimal paidAmount;

    // Ödemenin yapıldığı tarih.
    @Column(name = "payment_date")
    private LocalDate paymentDate; // Ödeme tarihi

    // Ödeme durumu (Henüz Gelmedi, Ödendi, Ödenmedi, Kısmen Ödendi).
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncomeStatusType status; // Ödeme durumu

    // Gelir kaydı ile ilgili notlar (opsiyonel).
    @Lob
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Bu ödemenin peşin bir ödeme olup olmadığını belirtir.
    // Database_Prd.md [cite: 24]
    @Column(name = "is_advance_payment", nullable = false)
    private boolean isAdvancePayment = false; // Peşin ödeme mi?

    // Otomatik durum güncellemeleri (örn: vadesi geçen ödenmemişlerin
    // işaretlenmesi)
    // servis katmanında ve/veya zamanlanmış görevlerle ele alınacaktır.
}