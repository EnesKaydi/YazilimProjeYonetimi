package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// Mülk sahiplerinin veya kiracılarının bilgilerini temsil eden entity.
@Entity
@Table(name = "owners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Sahip kaydının benzersiz kimliği

    @Column(name = "full_name", nullable = false)
    private String fullName; // Tam ad

    private String address;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "bank_account_number")
    private String bankAccountNumber; // Banka hesap numarası

    @Column(name = "deed_poll_number", length = 100)
    private String deedPollNumber; // Tapu/Sahiplik belge numarası

    // Bu alan, birimin kiracı tarafından kullanılıp kullanılmadığını belirtir.
    // Database_Prd.md [cite: 13] gereksinimine göre.
    @Column(name = "is_tenant_occupied", nullable = false)
    private boolean isTenantOccupied = false; // Birim kiralanmış mı?

    // Kişinin sistemde "Mal Sahibi" mi yoksa "Kiracı" olarak mı gösterileceğini
    // belirtir.
    // Database_Prd.md [cite: 14] gereksinimine göre.
    @Enumerated(EnumType.STRING)
    @Column(name = "display_as", nullable = false)
    private DisplayAsType displayAs; // Gösterim şekli (Mal Sahibi / Kiracı)

    // Bir sahip bir mülke aittir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property; // Sahip olunan mülk

    // Bir sahip belirli bir birim türüne sahip olabilir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_type_id") // Null olabilir, örneğin sadece mülk sahibi ama birim atanmamışsa
    private UnitType unitType; // Sahip olunan birim türü

    // Eğer isTenantOccupied true ise, bu sahip ile ilişkili bir kiracı olabilir.
    // mappedBy Tenant entity'sindeki "owner" alanına işaret eder.
    // CascadeType.ALL: Sahip silindiğinde ilişkili kiracı da silinir.
    // orphanRemoval=true: İlişkiden çıkarılan kiracı veritabanından silinir.
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Tenant tenant; // Varsa, kiracı

    // Bir sahibin birden fazla gelir kaydı olabilir (aidat, kira vb.).
    // Gelir matrisi için kullanılır.
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomeRecord> incomeRecords; // Gelir kayıtları

    // isTenantOccupied true olduğunda tenant nesnesinin null olmamasını sağlamak
    // için
    // prePersist ve preUpdate gibi JPA lifecycle callback'leri kullanılabilir veya
    // servis katmanında kontrol edilebilir.
}