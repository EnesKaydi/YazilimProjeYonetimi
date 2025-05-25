package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Kullanıcı adına göre kullanıcı bulma metodu.
    // Spring Data JPA bu metodun implementasyonunu otomatik olarak sağlar.
    Optional<User> findByUsername(String username);

    // E-posta adresine göre kullanıcı bulma metodu.
    Optional<User> findByEmail(String email);

    // Kullanıcı adı veya e-posta zaten var mı diye kontrol etmek için.
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Belirli bir ID hariç, verilen e-postanın başka bir kullanıcı tarafından
    // kullanılıp kullanılmadığını kontrol eder.
    // Profil güncelleme sırasında e-posta değişikliği için kullanışlıdır.
    boolean existsByEmailAndIdNot(String email, Long userId);
}