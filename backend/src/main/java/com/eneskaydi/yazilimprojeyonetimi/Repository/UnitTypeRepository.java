package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Long> {

    // Belirli bir mülke ait tüm birim türlerini bulma.
    // API: GET /api/properties/{propertyId}/unit-types
    List<UnitType> findByProperty(Property property);

    // Belirli bir mülke ait ve belirli bir ID'ye sahip birim türünü bulma.
    // API: PUT /api/properties/{propertyId}/unit-types/{unitTypeId}
    // API: DELETE /api/properties/{propertyId}/unit-types/{unitTypeId}
    Optional<UnitType> findByIdAndProperty(Long id, Property property);

    // Belirli bir mülk içinde aynı isimde birim türü olup olmadığını kontrol etme.
    boolean existsByNameAndProperty(String name, Property property);

    // Belirli bir mülk için, verilen ID dışındaki birim türleri arasında belirtilen
    // isimde bir birim türü olup olmadığını kontrol eder.
    // Birim türü güncelleme sırasında isim çakışmalarını önlemek için kullanılır.
    boolean existsByNameAndPropertyAndIdNot(String name, Property property, Long unitTypeId);

    // Belirli bir birim türüne sahip kaç tane Owner (Mal Sahibi/Kiracı) olduğunu
    // sayar.
    // Birim türü silme işleminden önce kontrol için kullanılır.
    @Query("SELECT COUNT(o) FROM Owner o WHERE o.unitType.id = :unitTypeId")
    long countOwnersByUnitTypeId(Long unitTypeId);
}