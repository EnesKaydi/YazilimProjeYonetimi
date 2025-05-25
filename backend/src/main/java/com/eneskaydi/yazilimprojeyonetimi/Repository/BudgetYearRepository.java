package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.BudgetYear;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetYearRepository extends JpaRepository<BudgetYear, Long> {

    // Belirli bir mülke ait tüm bütçe yıllarını bulma (yıla göre sıralı).
    List<BudgetYear> findByPropertyOrderByYearDesc(Property property);

    // Belirli bir mülke ait ve belirli bir yıldaki bütçe yılını bulma.
    Optional<BudgetYear> findByPropertyAndYear(Property property, int year);

    // Belirli bir mülk için belirli bir bütçe yılının olup olmadığını kontrol etme.
    boolean existsByPropertyAndYear(Property property, int year);
}