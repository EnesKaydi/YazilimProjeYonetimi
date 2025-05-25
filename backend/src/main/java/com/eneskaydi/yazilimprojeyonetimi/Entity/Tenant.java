package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kiracı bilgilerini temsil eden entity.
// Bir Owner ile birebir ilişkilidir.
@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Kiracı kaydının benzersiz kimliği

    @Column(name = "full_name", nullable = false)
    private String fullName; // Tam ad

    private String address;
    private String phoneNumber;
    private String email;

    @Column(name = "bank_account_number")
    private String bankAccountNumber; // Banka hesap numarası

    // Bu kiracının ilişkili olduğu sahip (Owner) kaydı.
    // OneToOne ilişki: Bir kiracı sadece bir sahip kaydına bağlı olabilir ve
    // bir sahip kaydının da (eğer kiralanmışsa) sadece bir kiracısı olur.
    // `optional = false` çünkü bir Tenant kaydı mutlaka bir Owner'a bağlı
    // olmalıdır.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private Owner owner; // Bağlı olduğu Owner

    // Tenant entity'si, Owner entity'sindeki isTenantOccupied alanı 'true'
    // olduğunda
    // ve displayAs alanı 'KIRACI' olduğunda mantıksal olarak kullanılır.
    // Bu kontroller genellikle servis katmanında yapılır.
}