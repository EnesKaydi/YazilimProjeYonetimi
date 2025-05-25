package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.Owner;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    // Belirli bir mülke ait tüm sahipleri/kiracıları bulma.
    // API: GET /api/properties/{propertyId}/owners
    List<Owner> findByProperty(Property property);

    // Belirli bir mülke ve birim türüne ait sahipleri/kiracıları bulma.
    List<Owner> findByPropertyAndUnitType(Property property, UnitType unitType);

    // Belirli bir mülke ait ve belirli bir ID'ye sahip sahibi/kiracıyı bulma.
    // API: GET /api/properties/{propertyId}/owners/{ownerId}
    // API: PUT /api/properties/{propertyId}/owners/{ownerId}
    // API: DELETE /api/properties/{propertyId}/owners/{ownerId}
    Optional<Owner> findByIdAndProperty(Long id, Property property);

    // Bir mülk içinde belirli bir e-posta adresine sahip sahip/kiracı olup
    // olmadığını kontrol etme.
    // (Eğer e-posta benzersizliği mülk bazında isteniyorsa)
    boolean existsByEmailAndProperty(String email, Property property);

    // Belirli bir mülkte, verilen ID dışındaki bir kayıtta e-postanın zaten var
    // olup olmadığını kontrol eder.
    // Sahip/Kiracı güncelleme işleminde e-posta benzersizliğini sağlamak için
    // kullanılır.
    boolean existsByEmailAndPropertyAndIdNot(String email, Property property, Long id);
}