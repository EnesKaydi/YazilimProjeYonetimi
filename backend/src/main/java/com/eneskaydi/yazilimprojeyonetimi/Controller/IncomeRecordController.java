package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.IncomeStatusType;
import com.eneskaydi.yazilimprojeyonetimi.Service.IncomeRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gelir Kayıtları (IncomeRecord) için CRUD işlemlerini yöneten REST controller.
 * Gelirler, bir mülkün belirli bir bütçe yılına aittir.
 */
@RestController
@RequestMapping("/api/properties/{propertyId}/budget-years/{budgetYearId}")
@RequiredArgsConstructor
public class IncomeRecordController {

    private final IncomeRecordService incomeRecordService;

    // Owner-specific endpoint'ler kaldırıldı çünkü Owner entity'si silindi.
    // Gelir kayıtları artık doğrudan BudgetYear ile ilişkilidir.

    /**
     * Belirli bir bütçe yılı için yeni bir gelir kaydı oluşturur.
     */
    @PostMapping("/income-records") // Path güncellendi, ownerId kaldırıldı
    public ResponseEntity<IncomeRecordDto> createIncomeRecord(@PathVariable Long propertyId,
            @PathVariable Long budgetYearId,
            @Valid @RequestBody IncomeRecordCreateDto createDto) {
        // Servis çağrısı da ownerId olmadan yapılacak şekilde güncellenmeli (IncomeRecordService arayüzü zaten güncellenmişti)
        IncomeRecordDto createdRecord = incomeRecordService.createIncomeRecord(propertyId, budgetYearId, createDto);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    /**
     * Belirli bir bütçe yılına ait tüm gelir kayıtlarını listeler.
     */
    @GetMapping("/income-records")
    public ResponseEntity<List<IncomeRecordDto>> getAllIncomeRecordsForBudgetYear(@PathVariable Long propertyId,
            @PathVariable Long budgetYearId) {
        List<IncomeRecordDto> records = incomeRecordService.getIncomeRecordsByBudgetYear(propertyId, budgetYearId);
        return ResponseEntity.ok(records);
    }

    /**
     * Belirli bir bütçe yılındaki gelir kayıtlarını durumlarına göre filtreleyerek listeler.
     */
    @GetMapping("/income-records/status")
    public ResponseEntity<List<IncomeRecordDto>> getIncomeRecordsByStatus(@PathVariable Long propertyId,
            @PathVariable Long budgetYearId,
            @RequestParam List<IncomeStatusType> statuses) {
        List<IncomeRecordDto> records = incomeRecordService.findByPropertyAndBudgetYearAndStatusIn(propertyId,
                budgetYearId, statuses);
        return ResponseEntity.ok(records);
    }

    /**
     * Belirli bir ID'ye sahip gelir kaydını getirir.
     */
    @GetMapping("/income-records/{incomeRecordId}")
    public ResponseEntity<IncomeRecordDto> getIncomeRecordById(@PathVariable Long propertyId,
            @PathVariable Long budgetYearId,
            @PathVariable Long incomeRecordId) {
        IncomeRecordDto record = incomeRecordService.getIncomeRecordById(propertyId, budgetYearId, incomeRecordId);
        return ResponseEntity.ok(record);
    }

    /**
     * Belirli bir ID'ye sahip gelir kaydını günceller.
     */
    @PutMapping("/income-records/{incomeRecordId}")
    public ResponseEntity<IncomeRecordDto> updateIncomeRecord(@PathVariable Long propertyId,
            @PathVariable Long budgetYearId,
            @PathVariable Long incomeRecordId,
            @Valid @RequestBody IncomeRecordUpdateDto updateDto) {
        IncomeRecordDto updatedRecord = incomeRecordService.updateIncomeRecord(propertyId, budgetYearId, incomeRecordId,
                updateDto);
        return ResponseEntity.ok(updatedRecord);
    }

    /**
     * Belirli bir ID'ye sahip gelir kaydını siler.
     */
    @DeleteMapping("/income-records/{incomeRecordId}")
    public ResponseEntity<Void> deleteIncomeRecord(@PathVariable Long propertyId,
            @PathVariable Long budgetYearId,
            @PathVariable Long incomeRecordId) {
        incomeRecordService.deleteIncomeRecord(propertyId, budgetYearId, incomeRecordId);
        return ResponseEntity.noContent().build();
    }
}