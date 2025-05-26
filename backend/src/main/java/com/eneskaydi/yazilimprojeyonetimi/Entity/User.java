package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kullanıcı bilgilerini temsil eden entity sınıfı.
// Bu sınıf, sisteme giriş yapacak kullanıcıların temel bilgilerini ve rollerini saklar.
// Her kullanıcı aynı zamanda bir ev sahibidir.
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"), // Kullanıcı adı (Ad Soyad), profil için. Benzersiz.
        @UniqueConstraint(columnNames = "email")    // E-posta, sisteme giriş için kullanılır. Benzersiz.
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Kullanıcının benzersiz kimliği

    @Column(nullable = false, unique = true)
    private String username; // Kullanıcı adı (Ad Soyad), sisteme giriş için değil, profil için. Benzersiz.

    @Column(nullable = false)
    private String password; // Kullanıcının şifresi.

    @Column(nullable = false, unique = true)
    private String email; // Kullanıcının e-posta adresi, sisteme giriş için kullanılır. Benzersiz.

    @Column(name = "phone_number", length = 11) // Yeni eklendi
    private String phoneNumber; // Kullanıcının telefon numarası (opsiyonel)

    @Lob // Uzun metinler için (opsiyonel, adresin uzunluğuna göre)
    @Column(name = "address", columnDefinition = "TEXT") // Yeni eklendi
    private String address; // Kullanıcının adresi (opsiyonel)

    @Column(nullable = false)
    private String role; // Kullanıcının sistemdeki rolü (örn: ADMIN, USER). USER varsayılan ev sahibi olacak.

    // Dil tercihi alanı kaldırıldı.
    // private String languagePreference;

    // Bu kullanıcıya ait mülkler (Property entity'si ile ilişki kurulacak)
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<Property> properties;
}