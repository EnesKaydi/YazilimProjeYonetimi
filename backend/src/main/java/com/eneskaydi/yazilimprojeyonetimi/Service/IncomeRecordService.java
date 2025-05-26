package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.IncomeStatusType;

import java.util.List;

// Gelir kayıtları yönetimi işlemlerinden sorumlu servis arayüzü.
// Bu servis, bir mülk sahibine (Owner) ve bir bütçe yılına (BudgetYear) bağlı gelirlerin
// (aidat, kira vb.) oluşturulması, listelenmesi, güncellenmesi (ödeme yapılması),
// silinmesi ve ilgili belgelerin (makbuz, gecikme bildirimi) üretilmesi gibi işlemleri yönetir.
public interface IncomeRecordService {

        /**
         * Belirli bir mülk ve bütçe yılı için yeni bir gelir kaydı oluşturur.
         *
         * @param propertyId            Mülkün ID'si.
         * @param budgetYearId          Gelir kaydının ait olduğu bütçe yılının ID'si.
         * @param incomeRecordCreateDto Oluşturulacak gelir kaydı bilgilerini içeren
         *                              DTO.
         * @return Oluşturulan gelir kaydı bilgilerini içeren DTO.
         * @throws RuntimeException Mülk, bütçe yılı, sahip bulunamazsa veya ilgili ay
         *                          için zaten bir kayıt varsa.
         */
        IncomeRecordDto createIncomeRecord(Long propertyId, Long budgetYearId, IncomeRecordCreateDto incomeRecordCreateDto);

        /**
         * Belirli bir bütçe yılındaki tüm gelir kayıtlarını listeler (tüm sahipler
         * için).
         *
         * @param propertyId   Mülkün ID'si.
         * @param budgetYearId Gelir kayıtları listelenecek bütçe yılının ID'si.
         * @return Bütçe yılına ait gelir kayıtlarının listesi.
         * @throws RuntimeException Mülk veya bütçe yılı bulunamazsa.
         */
        List<IncomeRecordDto> getIncomeRecordsByBudgetYear(Long propertyId, Long budgetYearId);

        /**
         * Belirli bir gelir kaydını ID'sine göre getirir.
         *
         * @param propertyId     Mülkün ID'si.
         * @param budgetYearId   Bütçe yılının ID'si (doğrulama için).
         * @param incomeRecordId Getirilecek gelir kaydının ID'si.
         * @return Gelir kaydı bilgilerini içeren DTO.
         * @throws RuntimeException Mülk, bütçe yılı veya gelir kaydı bulunamazsa.
         */
        IncomeRecordDto getIncomeRecordById(Long propertyId, Long budgetYearId, Long incomeRecordId);

        /**
         * Mevcut bir gelir kaydını günceller. Genellikle ödeme yapıldığında durumu ve
         * ödenen tutar güncellenir.
         *
         * @param propertyId            Mülkün ID'si.
         * @param budgetYearId          Bütçe yılının ID'si.
         * @param incomeRecordId        Güncellenecek gelir kaydının ID'si.
         * @param incomeRecordUpdateDto Güncellenecek gelir kaydı bilgilerini içeren
         *                              DTO.
         * @return Güncellenmiş gelir kaydı bilgilerini içeren DTO.
         * @throws RuntimeException Mülk, bütçe yılı veya gelir kaydı bulunamazsa.
         */
        IncomeRecordDto updateIncomeRecord(Long propertyId, Long budgetYearId, Long incomeRecordId,
                        IncomeRecordUpdateDto incomeRecordUpdateDto);

        /**
         * Bir gelir kaydını siler.
         *
         * @param propertyId     Mülkün ID'si.
         * @param budgetYearId   Bütçe yılının ID'si.
         * @param incomeRecordId Silinecek gelir kaydının ID'si.
         * @throws RuntimeException Mülk, bütçe yılı veya gelir kaydı bulunamazsa.
         */
        void deleteIncomeRecord(Long propertyId, Long budgetYearId, Long incomeRecordId);

        /**
         * Belirli bir mülk, bütçe yılı ve gelir durumu listesine göre gelir kayıtlarını
         * getirir.
         * Örneğin, "ÖDENMEDİ" veya "KISMEN ÖDENDİ" durumundaki kayıtları bulmak için.
         * PRD [cite: 25, 27]
         *
         * @param propertyId   Mülkün ID'si.
         * @param budgetYearId Bütçe yılı ID'si.
         * @param statuses     Filtrelenecek gelir durumu tipleri listesi.
         * @return Filtrelenmiş gelir kayıtlarının listesi.
         */
        List<IncomeRecordDto> findByPropertyAndBudgetYearAndStatusIn(Long propertyId, Long budgetYearId,
                        List<IncomeStatusType> statuses);

}