package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Accounts sayfasından eklenen kullanıcıları temsil eden entity sınıfı.
@Entity
@Table(name = "added_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email") // E-posta, benzersiz olmalı.
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Kullanıcının benzersiz kimliği

    @Column(nullable = false)
    private String name; // Kullanıcının adı soyadı

    @Column(nullable = false, unique = true)
    private String email; // Kullanıcının e-posta adresi

    @Column(nullable = false)
    private String password; // Kullanıcının şifresi (hashlenmiş olarak saklanacak)

    @Column(nullable = false)
    private String role; // Kullanıcının rolü (admin, manager, user)

    @Column(name = "phone_number", length = 20) // Telefon numarası için uzunluk artırıldı
    private String phoneNumber; // Kullanıcının telefon numarası (opsiyonel)

    @Lob
    @Column(name = "address", columnDefinition = "TEXT")
    private String address; // Kullanıcının adresi (opsiyonel)

    @Column(nullable = false)
    private String status; // Kullanıcının durumu (active, inactive)
} 