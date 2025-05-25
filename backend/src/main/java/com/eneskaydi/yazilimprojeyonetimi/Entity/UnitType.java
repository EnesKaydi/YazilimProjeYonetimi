package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// Birim türlerini (örn: büyük daire, küçük daire, garaj) temsil eden entity sınıfı.
// Her birim türünün adını, aylık ücretini ve bir açıklamasını saklar.
@Entity
@Table(name = "unit_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Birim türünün benzersiz kimliği

    @Column(nullable = false)
    private String name; // Birim türünün adı

    @Column(name = "monthly_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyFee; // Aylık ücret/aidat

    @Lob
    private String description; // Birim türü açıklaması

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property; // Ait olduğu mülk

    @OneToMany(mappedBy = "unitType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Owner> owners;
}