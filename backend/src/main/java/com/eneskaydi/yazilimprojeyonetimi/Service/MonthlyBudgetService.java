package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetUpdateDto;

import java.util.List;

// Aylık bütçe yönetimi işlemlerinden sorumlu servis arayüzü.
// Bu servis, bir bütçe yılına ait aylık bütçelerin oluşturulması, listelenmesi,
// güncellenmesi ve silinmesi gibi işlemleri yönetir.
public interface MonthlyBudgetService {

    /**
     * Belirli bir bütçe yılı için yeni bir aylık bütçe kaydı oluşturur.
     * Ayın (month) o bütçe yılı içinde benzersiz olması beklenir.
     *
     * @param propertyId             Mülkün ID'si (yetkilendirme ve doğrulama için).
     * @param budgetYearId           Aylık bütçenin ekleneceği bütçe yılının ID'si.
     * @param monthlyBudgetCreateDto Oluşturulacak aylık bütçe bilgilerini içeren
     *                               DTO.
     * @return Oluşturulan aylık bütçe bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya bütçe yılı bulunamazsa, ya da o ay için
     *                          zaten bir kayıt varsa.
     */
    MonthlyBudgetDto createMonthlyBudget(Long propertyId, Long budgetYearId,
            MonthlyBudgetCreateDto monthlyBudgetCreateDto);

    /**
     * Belirli bir bütçe yılına ait tüm aylık bütçeleri, ay sırasına göre (Ocak,
     * Şubat...) listeler.
     *
     * @param propertyId   Mülkün ID'si.
     * @param budgetYearId Aylık bütçeleri listelenecek bütçe yılının ID'si.
     * @return Bütçe yılına ait aylık bütçelerin listesi.
     * @throws RuntimeException Mülk veya bütçe yılı bulunamazsa.
     */
    List<MonthlyBudgetDto> getMonthlyBudgetsByBudgetYear(Long propertyId, Long budgetYearId);

    /**
     * Belirli bir bütçe yılına ait spesifik bir aylık bütçeyi ID'sine göre getirir.
     *
     * @param propertyId      Mülkün ID'si.
     * @param budgetYearId    Aylık bütçenin ait olduğu bütçe yılının ID'si.
     * @param monthlyBudgetId Getirilecek aylık bütçenin ID'si.
     * @return Aylık bütçe bilgilerini içeren DTO.
     * @throws RuntimeException Mülk, bütçe yılı veya aylık bütçe bulunamazsa.
     */
    MonthlyBudgetDto getMonthlyBudgetById(Long propertyId, Long budgetYearId, Long monthlyBudgetId);

    /**
     * Belirli bir aylık bütçeyi günceller.
     * Genellikle planlanan gelir/gider veya notlar güncellenebilir.
     * Fiili gelir/giderler IncomeRecord'lardan veya ayrı bir işlemle güncellenir.
     *
     * @param propertyId             Mülkün ID'si.
     * @param budgetYearId           Aylık bütçenin ait olduğu bütçe yılının ID'si.
     * @param monthlyBudgetId        Güncellenecek aylık bütçenin ID'si.
     * @param monthlyBudgetUpdateDto Güncellenecek aylık bütçe bilgilerini içeren
     *                               DTO.
     * @return Güncellenmiş aylık bütçe bilgilerini içeren DTO.
     * @throws RuntimeException Mülk, bütçe yılı veya aylık bütçe bulunamazsa.
     */
    MonthlyBudgetDto updateMonthlyBudget(Long propertyId, Long budgetYearId, Long monthlyBudgetId,
            MonthlyBudgetUpdateDto monthlyBudgetUpdateDto);

    /**
     * Belirli bir aylık bütçeyi siler.
     * Bu işlem, o aya ait gelir kayıtlarını etkileyebilir (PRD'ye göre ele
     * alınmalı).
     *
     * @param propertyId      Mülkün ID'si.
     * @param budgetYearId    Aylık bütçenin ait olduğu bütçe yılının ID'si.
     * @param monthlyBudgetId Silinecek aylık bütçenin ID'si.
     * @throws RuntimeException Mülk, bütçe yılı veya aylık bütçe bulunamazsa.
     */
    void deleteMonthlyBudget(Long propertyId, Long budgetYearId, Long monthlyBudgetId);
}