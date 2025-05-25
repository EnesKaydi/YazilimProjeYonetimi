package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.BudgetYear;
import com.eneskaydi.yazilimprojeyonetimi.Entity.MonthlyBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {

    // Belirli bir bütçe yılına ait tüm aylık bütçeleri bulma (aya göre sıralı).
    List<MonthlyBudget> findByBudgetYearOrderByMonthAsc(BudgetYear budgetYear);

    // Belirli bir bütçe yılına ait tüm aylık bütçeleri bulma.
    List<MonthlyBudget> findByBudgetYear(BudgetYear budgetYear);

    // Belirli bir bütçe yılına ait ve belirli bir aydaki aylık bütçeyi bulma.
    Optional<MonthlyBudget> findByBudgetYearAndMonth(BudgetYear budgetYear, int month);

    // Belirli bir bütçe yılı için belirli bir aylık bütçenin olup olmadığını
    // kontrol etme.
    boolean existsByBudgetYearAndMonth(BudgetYear budgetYear, int month);
}