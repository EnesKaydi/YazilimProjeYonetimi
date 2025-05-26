package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kullanıcı bilgilerini temsil eden entity sınıfı.
// Bu sınıf, sisteme giriş yapacak kullanıcıların temel bilgilerini ve rollerini saklar.
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"), // Ad Soyad, benzersiz
        @UniqueConstraint(columnNames = "email")    // E-posta, benzersiz
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Kullanıcının benzersiz kimliği

    @Column(nullable = false, unique = true)
    private String username; // Kullanıcı adı (Ad Soyad), sisteme giriş için değil, profil için. Benzersiz.

    @Column(nullable = false)
    private String password; // Kullanıcının şifresi.

    @Column(nullable = false, unique = true)
    private String email; // Kullanıcının e-posta adresi, sisteme giriş için kullanılır. Benzersiz.

    @Column(nullable = false)
    private String role; // Kullanıcının sistemdeki rolü (örn: ADMIN, USER).
}