package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.MonthlyBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Belirli bir mülkün belirli bir yıllık bütçesine ait Aylık Bütçeler
 * (MonthlyBudget)
 * için CRUD işlemlerini yöneten REST controller.
 */
@RestController
@RequestMapping("/api/properties/{propertyId}/budget-years/{budgetYearId}/monthly-budgets")
@RequiredArgsConstructor
public class MonthlyBudgetController {

        private final MonthlyBudgetService monthlyBudgetService;

        /**
         * Belirli bir yıllık bütçe için yeni bir aylık bütçe kaydı oluşturur.
         *
         * @param propertyId             Mülk ID'si (URL üzerinden, yetkilendirme için
         *                               kullanılabilir).
         * @param budgetYearId           Aylık bütçenin ait olduğu yıllık bütçenin
         *                               ID'si.
         * @param monthlyBudgetCreateDto Oluşturulacak aylık bütçe bilgilerini içeren
         *                               DTO.
         * @return HTTP 201 Created ile oluşturulan aylık bütçe bilgilerini içeren
         *         MonthlyBudgetDto.
         */
        @PostMapping
        public ResponseEntity<MonthlyBudgetDto> createMonthlyBudget(@PathVariable Long propertyId,
                        @PathVariable Long budgetYearId,
                        @Valid @RequestBody MonthlyBudgetCreateDto monthlyBudgetCreateDto) {
                MonthlyBudgetDto createdMonthlyBudget = monthlyBudgetService.createMonthlyBudget(propertyId,
                                budgetYearId,
                                monthlyBudgetCreateDto);
                return new ResponseEntity<>(createdMonthlyBudget, HttpStatus.CREATED);
        }

        /**
         * Belirli bir yıllık bütçeye ait tüm aylık bütçeleri listeler.
         *
         * @param propertyId   Mülk ID'si (URL üzerinden).
         * @param budgetYearId Aylık bütçelerin listeleneceği yıllık bütçenin ID'si.
         * @return HTTP 200 OK ile yıllık bütçeye ait aylık bütçelerin listesi.
         */
        @GetMapping
        public ResponseEntity<List<MonthlyBudgetDto>> getAllMonthlyBudgetsForBudgetYear(@PathVariable Long propertyId,
                        @PathVariable Long budgetYearId) {
                List<MonthlyBudgetDto> monthlyBudgets = monthlyBudgetService.getMonthlyBudgetsByBudgetYear(propertyId,
                                budgetYearId);
                return ResponseEntity.ok(monthlyBudgets);
        }

        /**
         * Belirli bir yıllık bütçeye ait, belirli bir ID'ye sahip aylık bütçeyi
         * getirir.
         *
         * @param propertyId      Mülk ID'si (URL üzerinden).
         * @param budgetYearId    Yıllık bütçenin ID'si.
         * @param monthlyBudgetId Getirilecek aylık bütçenin ID'si.
         * @return HTTP 200 OK ile aylık bütçe bilgilerini içeren MonthlyBudgetDto.
         */
        @GetMapping("/{monthlyBudgetId}")
        public ResponseEntity<MonthlyBudgetDto> getMonthlyBudgetById(@PathVariable Long propertyId,
                        @PathVariable Long budgetYearId,
                        @PathVariable Long monthlyBudgetId) {
                MonthlyBudgetDto monthlyBudgetDto = monthlyBudgetService.getMonthlyBudgetById(propertyId, budgetYearId,
                                monthlyBudgetId);
                return ResponseEntity.ok(monthlyBudgetDto);
        }

        /**
         * Belirli bir yıllık bütçeye ait, belirli bir ID'ye sahip aylık bütçeyi
         * günceller.
         *
         * @param propertyId             Mülk ID'si (URL üzerinden).
         * @param budgetYearId           Yıllık bütçenin ID'si.
         * @param monthlyBudgetId        Güncellenecek aylık bütçenin ID'si.
         * @param monthlyBudgetUpdateDto Güncellenecek aylık bütçe bilgilerini içeren
         *                               DTO.
         * @return HTTP 200 OK ile güncellenmiş aylık bütçe bilgilerini içeren
         *         MonthlyBudgetDto.
         */
        @PutMapping("/{monthlyBudgetId}")
        public ResponseEntity<MonthlyBudgetDto> updateMonthlyBudget(@PathVariable Long propertyId,
                        @PathVariable Long budgetYearId,
                        @PathVariable Long monthlyBudgetId,
                        @Valid @RequestBody MonthlyBudgetUpdateDto monthlyBudgetUpdateDto) {
                MonthlyBudgetDto updatedMonthlyBudget = monthlyBudgetService.updateMonthlyBudget(propertyId,
                                budgetYearId,
                                monthlyBudgetId, monthlyBudgetUpdateDto);
                return ResponseEntity.ok(updatedMonthlyBudget);
        }

        /**
         * Belirli bir yıllık bütçeye ait, belirli bir ID'ye sahip aylık bütçeyi siler.
         *
         * @param propertyId      Mülk ID'si (URL üzerinden).
         * @param budgetYearId    Yıllık bütçenin ID'si.
         * @param monthlyBudgetId Silinecek aylık bütçenin ID'si.
         * @return HTTP 204 No Content.
         */
        @DeleteMapping("/{monthlyBudgetId}")
        public ResponseEntity<Void> deleteMonthlyBudget(@PathVariable Long propertyId,
                        @PathVariable Long budgetYearId,
                        @PathVariable Long monthlyBudgetId) {
                monthlyBudgetService.deleteMonthlyBudget(propertyId, budgetYearId, monthlyBudgetId);
                return ResponseEntity.noContent().build();
        }
}