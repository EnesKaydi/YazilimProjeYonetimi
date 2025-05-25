package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.BudgetYear;
import com.eneskaydi.yazilimprojeyonetimi.Entity.IncomeRecord;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Owner;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.IncomeStatusType; // IncomeStatusType enum importu
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRecordRepository extends JpaRepository<IncomeRecord, Long> {

        // Belirli bir sahip ve bütçe yılına ait tüm gelir kayıtlarını bulma (aya göre
        // sıralı).
        List<IncomeRecord> findByOwnerAndBudgetYearOrderByMonthAsc(Owner owner, BudgetYear budgetYear);

        // Belirli bir bütçe yılına ait tüm gelir kayıtlarını bulma.
        List<IncomeRecord> findByBudgetYear(BudgetYear budgetYear);

        // Belirli bir sahip, bütçe yılı ve ay için gelir kaydını bulma.
        Optional<IncomeRecord> findByOwnerAndBudgetYearAndMonth(Owner owner, BudgetYear budgetYear, int month);

        // Belirli bir mülkün belirli bir bütçe yılındaki ödenmemiş veya kısmen ödenmiş
        // kayıtlarını bulma.
        // Bu, Backend_Prd.md'deki "Uygulama İçi Bildirimler" (GET
        // /api/properties/{propertyId}/notifications/unpaid-owners)
        // için kullanılabilir. Owner bilgisine ulaşmak için join yapmak gerekebilir
        // veya servis katmanında işlenebilir.
        @Query("SELECT ir FROM IncomeRecord ir JOIN ir.owner o JOIN o.property p " +
                        "WHERE p = :property AND ir.budgetYear = :budgetYear AND ir.status IN (:statuses)")
        List<IncomeRecord> findByPropertyAndBudgetYearAndStatusIn(
                        @Param("property") Property property,
                        @Param("budgetYear") BudgetYear budgetYear,
                        @Param("statuses") List<IncomeStatusType> statuses);

        // Belirli bir mülke ait, belirli bir bütçe yılında ve belirli bir ayda, belirli
        // durumlardaki gelir kayıtları
        List<IncomeRecord> findByBudgetYearAndMonthAndStatusInAndOwner_Property(
                        BudgetYear budgetYear, int month, List<IncomeStatusType> statuses, Property property);

        // Belirli bir bütçe yılı ve durum listesine göre gelir kayıtlarını bulur.
        List<IncomeRecord> findByBudgetYearAndStatusIn(BudgetYear budgetYear, List<IncomeStatusType> statuses);

}