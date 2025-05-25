package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// Mülk bilgilerini temsil eden entity sınıfı.
// Sistemdeki her bir mülkün detaylarını (adres, tapu bilgisi, dernek adı vb.) ve ilişkili olduğu kullanıcıyı saklar.
@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Mülkün benzersiz kimliği

    @Column(nullable = false)
    private String name; // Mülkün adı (örn: "Güneş Apartmanı")

    private String address; // Mülkün tam adresi

    @Column(name = "property_title")
    private String propertyTitle; // Tapu bilgisi

    @Column(name = "association_name")
    private String associationName; // Dernek adı (varsa)

    @Column(nullable = false)
    private String city; // Mülkün bulunduğu şehir

    @Column(nullable = false)
    private String currency; // Mülk için geçerli para birimi (örn: "TRY", "USD")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Mülkü oluşturan veya yöneten kullanıcı

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitType> unitTypes;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Owner> owners;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetYear> budgetYears;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;
}