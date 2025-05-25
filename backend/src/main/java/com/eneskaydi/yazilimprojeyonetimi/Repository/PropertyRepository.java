package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // Belirli bir kullanıcıya ait tüm mülkleri bulma.
    // Kullanıcı kendi mülklerini listelerken kullanılabilir.
    List<Property> findByUser(User user);

    // Belirli bir kullanıcıya ait ve belirli bir ID'ye sahip mülkü bulma.
    // Yetkilendirme ve spesifik mülk işlemleri için önemli.
    Optional<Property> findByIdAndUser(Long id, User user);

    // Bir kullanıcının belirli bir isimde mülkü olup olmadığını kontrol etme.
    // Aynı isimde mükerrer mülk kaydını önlemek için kullanılabilir.
    Optional<Property> findByNameAndUser(String name, User user);

    // Güncelleme sırasında, mevcut mülk hariç, bir kullanıcının belirli bir isimde
    // başka bir mülkü olup olmadığını kontrol eder.
    Optional<Property> findByNameAndUserAndIdNot(String name, User user, Long id);
}