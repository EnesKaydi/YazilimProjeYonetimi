package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetTransferRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetYearCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetYearDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.BudgetYearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Belirli bir mülke ait Yıllık Bütçeler (BudgetYear) için CRUD işlemlerini ve
 * bütçe transferi işlemini yöneten REST controller.
 */
@RestController
@RequestMapping("/api/properties/{propertyId}/budget-years")
@RequiredArgsConstructor
public class BudgetYearController {

    private final BudgetYearService budgetYearService;

    /**
     * Belirli bir mülk için yeni bir yıllık bütçe oluşturur.
     *
     * @param propertyId          Yıllık bütçenin ekleneceği mülkün ID'si.
     * @param budgetYearCreateDto Oluşturulacak yıllık bütçe bilgilerini içeren DTO.
     * @return HTTP 201 Created ile oluşturulan yıllık bütçe bilgilerini içeren
     *         BudgetYearDto.
     */
    @PostMapping
    public ResponseEntity<BudgetYearDto> createBudgetYear(@PathVariable Long propertyId,
            @Valid @RequestBody BudgetYearCreateDto budgetYearCreateDto) {
        BudgetYearDto createdBudgetYear = budgetYearService.createBudgetYear(propertyId, budgetYearCreateDto);
        return new ResponseEntity<>(createdBudgetYear, HttpStatus.CREATED);
    }

    /**
     * Belirli bir mülke ait tüm yıllık bütçeleri listeler.
     *
     * @param propertyId Yıllık bütçelerin listeleneceği mülkün ID'si.
     * @return HTTP 200 OK ile mülke ait yıllık bütçelerin listesi (BudgetYearDto
     *         listesi).
     */
    @GetMapping
    public ResponseEntity<List<BudgetYearDto>> getAllBudgetYearsForProperty(@PathVariable Long propertyId) {
        List<BudgetYearDto> budgetYears = budgetYearService.getBudgetYearsByProperty(propertyId);
        return ResponseEntity.ok(budgetYears);
    }

    /**
     * Belirli bir mülke ait, belirli bir ID'ye sahip yıllık bütçeyi getirir.
     *
     * @param propertyId   Yıllık bütçenin ait olduğu mülkün ID'si.
     * @param budgetYearId Getirilecek yıllık bütçenin ID'si.
     * @return HTTP 200 OK ile yıllık bütçe bilgilerini içeren BudgetYearDto.
     */
    @GetMapping("/{budgetYearId}")
    public ResponseEntity<BudgetYearDto> getBudgetYearById(@PathVariable Long propertyId,
            @PathVariable Long budgetYearId) {
        BudgetYearDto budgetYearDto = budgetYearService.getBudgetYearById(propertyId, budgetYearId);
        return ResponseEntity.ok(budgetYearDto);
    }

    // PUT (Update) ve DELETE endpointleri PRD'de direkt belirtilmemiş ancak DTO'lar
    // olduğu için eklenebilir.
    // Şimdilik sadece create, get all, get by id ve transfer işlemlerini ekliyorum.

    /**
     * Yıl sonu bütçe devir işlemini gerçekleştirir.
     * Bir önceki yıldan mevcut yıla veya belirtilen bir yıldan sonrakine bakiye
     * aktarımı.
     * PRD: POST /api/properties/{propertyId}/budgets/transfer
     * Burada /budget-years/transfer olarak güncellendi.
     *
     * @param propertyId               İşlemin yapılacağı mülkün ID'si.
     * @param budgetTransferRequestDto Transfer detaylarını içeren DTO (örn:
     *                                 fromYear, toYear).
     * @return HTTP 200 OK ile başarılı transfer mesajı veya güncellenmiş bütçe yılı
     *         bilgileri.
     */
    @PostMapping("/transfer")
    public ResponseEntity<Void> transferBudget(@PathVariable Long propertyId,
            @Valid @RequestBody BudgetTransferRequestDto budgetTransferRequestDto) {
        budgetYearService.transferBudget(propertyId, budgetTransferRequestDto);
        // Başarılı yanıt için belki bir mesaj veya güncellenmiş bütçe bilgisi
        // döndürülebilir.
        return ResponseEntity.ok().build();
    }
}