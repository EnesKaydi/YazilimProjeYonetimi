package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.IncomeRecordUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.*;
import com.eneskaydi.yazilimprojeyonetimi.Repository.*;
import com.eneskaydi.yazilimprojeyonetimi.Service.BudgetYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Gelir kayıtları yönetimi servisinin implementasyonu.
@Service
@RequiredArgsConstructor
public class IncomeRecordServiceImpl implements IncomeRecordService {

    private final IncomeRecordRepository incomeRecordRepository;
    private final OwnerRepository ownerRepository;
    private final BudgetYearRepository budgetYearRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BudgetYearService budgetYearService;
    // private final MonthlyBudgetRepository monthlyBudgetRepository; // Gerekirse
    // eklenecek

    // Geçici helper metot - Mevcut kullanıcıyı almak için.
    private User getCurrentUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Varsayılan test kullanıcısı (ID: 1) bulunamadı."));
    }

    // Mülkün, bütçe yılının ve sahibin geçerliliğini kontrol eden yardımcı metot.
    private Owner validateAndGetOwner(Long propertyId, Long budgetYearId, Long ownerId) {
        User currentUser = getCurrentUser();
        Property property = propertyRepository.findByIdAndUser(propertyId, currentUser)
                .orElseThrow(() -> new RuntimeException(
                        "Mülk (ID: " + propertyId + ") bulunamadı veya mevcut kullanıcıya ait değil."));

        BudgetYear budgetYear = budgetYearRepository.findById(budgetYearId)
                .filter(by -> by.getProperty().getId().equals(property.getId()))
                .orElseThrow(() -> new RuntimeException("Bütçe Yılı (ID: " + budgetYearId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));

        return ownerRepository.findByIdAndProperty(ownerId, property)
                .orElseThrow(() -> new RuntimeException("Sahip (ID: " + ownerId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));
    }

    // Sadece Mülk ve Bütçe Yılı doğrulaması yapan yardımcı metot.
    private BudgetYear validateAndGetBudgetYear(Long propertyId, Long budgetYearId) {
        User currentUser = getCurrentUser();
        Property property = propertyRepository.findByIdAndUser(propertyId, currentUser)
                .orElseThrow(() -> new RuntimeException(
                        "Mülk (ID: " + propertyId + ") bulunamadı veya mevcut kullanıcıya ait değil."));

        return budgetYearRepository.findById(budgetYearId)
                .filter(by -> by.getProperty().getId().equals(property.getId()))
                .orElseThrow(() -> new RuntimeException("Bütçe Yılı (ID: " + budgetYearId
                        + ") bulunamadı veya belirtilen mülke (ID: " + propertyId + ") ait değil."));
    }

    @Override
    @Transactional
    public IncomeRecordDto createIncomeRecord(Long propertyId, Long budgetYearId, Long ownerId,
            IncomeRecordCreateDto incomeRecordCreateDto) {
        Owner owner = validateAndGetOwner(propertyId, budgetYearId, ownerId);
        BudgetYear budgetYear = owner.getProperty().getBudgetYears().stream()
                .filter(by -> by.getId().equals(budgetYearId)).findFirst().orElse(null); // budgetYear zaten owner
                                                                                         // üzerinden dolaylı olarak
                                                                                         // doğrulanmış sayılabilir.
        if (budgetYear == null) { // Ekstra kontrol
            throw new RuntimeException("Bütçe yılı sahip ile ilişkili değil.");
        }

        // Aynı sahip, aynı bütçe yılı ve aynı ay için birden fazla gelir kaydı olmamalı
        // (genellikle).
        // Bu kural PRD'ye göre esnetilebilir veya farklı bir gelir türü alanı
        // eklenebilir.
        if (incomeRecordRepository.findByOwnerAndBudgetYearAndMonth(owner, budgetYear, incomeRecordCreateDto.getMonth())
                .isPresent()) {
            throw new RuntimeException(owner.getFullName() + " için " + budgetYear.getYear() + " yılı " +
                    incomeRecordCreateDto.getMonth() + ". ayına ait zaten bir gelir kaydı mevcut.");
        }

        IncomeRecord incomeRecord = new IncomeRecord();
        incomeRecord.setMonth(incomeRecordCreateDto.getMonth());
        incomeRecord.setDueDate(incomeRecordCreateDto.getDueDate());
        incomeRecord.setExpectedAmount(incomeRecordCreateDto.getExpectedAmount());
        incomeRecord.setPaidAmount(incomeRecordCreateDto.getPaidAmount() != null ? incomeRecordCreateDto.getPaidAmount()
                : BigDecimal.ZERO);
        incomeRecord.setPaymentDate(incomeRecordCreateDto.getPaymentDate());
        incomeRecord.setStatus(incomeRecordCreateDto.getStatus() != null ? incomeRecordCreateDto.getStatus()
                : IncomeStatusType.HENUZ_GELMEDI);
        incomeRecord.setNotes(incomeRecordCreateDto.getNotes());
        incomeRecord.setOwner(owner);
        incomeRecord.setBudgetYear(budgetYear);
        // incomeRecord.setMonthlyBudget(); // Eğer MonthlyBudget ile direkt ilişki
        // varsa set edilmeli

        // Duruma göre paidAmount ve paymentDate kontrolü yapılabilir.
        // Örneğin status ODENDI ise paidAmount ve paymentDate zorunlu olabilir.
        updateStatusBasedOnPayment(incomeRecord); // Ödeme durumuna göre status'u otomatik ayarla

        IncomeRecord savedIncomeRecord = incomeRecordRepository.save(incomeRecord);

        // Yıllık bütçe toplamları güncelleniyor.
        if (savedIncomeRecord.getBudgetYear() != null) {
            budgetYearService.updateBudgetYearTotals(savedIncomeRecord.getBudgetYear().getId());
        }

        return mapToIncomeRecordDto(savedIncomeRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeRecordDto> getIncomeRecordsByBudgetYear(Long propertyId, Long budgetYearId) {
        BudgetYear budgetYear = validateAndGetBudgetYear(propertyId, budgetYearId);
        return incomeRecordRepository.findByBudgetYear(budgetYear)
                .stream()
                .map(this::mapToIncomeRecordDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeRecordDto> getIncomeRecordsByOwnerAndBudgetYear(Long propertyId, Long budgetYearId,
            Long ownerId) {
        Owner owner = validateAndGetOwner(propertyId, budgetYearId, ownerId);
        BudgetYear budgetYear = owner.getProperty().getBudgetYears().stream()
                .filter(by -> by.getId().equals(budgetYearId)).findFirst().orElse(null);
        if (budgetYear == null) {
            return Collections.emptyList();
        }

        return incomeRecordRepository.findByOwnerAndBudgetYearOrderByMonthAsc(owner, budgetYear)
                .stream()
                .map(this::mapToIncomeRecordDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeRecordDto getIncomeRecordById(Long propertyId, Long budgetYearId, Long incomeRecordId) {
        BudgetYear budgetYear = validateAndGetBudgetYear(propertyId, budgetYearId);
        IncomeRecord incomeRecord = incomeRecordRepository.findById(incomeRecordId)
                .filter(ir -> ir.getBudgetYear().getId().equals(budgetYear.getId())
                        && ir.getOwner().getProperty().getId().equals(propertyId))
                .orElseThrow(() -> new RuntimeException("Gelir Kaydı (ID: " + incomeRecordId
                        + ") bulunamadı veya belirtilen bütçe yılı/mülke ait değil."));
        return mapToIncomeRecordDto(incomeRecord);
    }

    @Override
    @Transactional
    public IncomeRecordDto updateIncomeRecord(Long propertyId, Long budgetYearId, Long incomeRecordId,
            IncomeRecordUpdateDto incomeRecordUpdateDto) {
        BudgetYear budgetYear = validateAndGetBudgetYear(propertyId, budgetYearId); // Mülk ve Bütçe yılı kontrolü
        IncomeRecord incomeRecord = incomeRecordRepository.findById(incomeRecordId)
                .filter(ir -> ir.getBudgetYear().getId().equals(budgetYear.getId())
                        && ir.getOwner().getProperty().getId().equals(propertyId))
                .orElseThrow(() -> new RuntimeException("Güncellenecek Gelir Kaydı (ID: " + incomeRecordId
                        + ") bulunamadı veya belirtilen bütçe yılı/mülke ait değil."));

        // Sadece belirli alanların güncellenmesine izin verilebilir.
        // Örneğin, ay veya sahip değiştirilemez.
        if (incomeRecordUpdateDto.getDueDate() != null) {
            incomeRecord.setDueDate(incomeRecordUpdateDto.getDueDate());
        }
        if (incomeRecordUpdateDto.getExpectedAmount() != null) {
            incomeRecord.setExpectedAmount(incomeRecordUpdateDto.getExpectedAmount());
        }
        if (incomeRecordUpdateDto.getPaidAmount() != null) {
            incomeRecord.setPaidAmount(incomeRecordUpdateDto.getPaidAmount());
        }
        if (incomeRecordUpdateDto.getPaymentDate() != null) {
            incomeRecord.setPaymentDate(incomeRecordUpdateDto.getPaymentDate());
        }
        if (incomeRecordUpdateDto.getStatus() != null) {
            incomeRecord.setStatus(incomeRecordUpdateDto.getStatus());
        }
        if (incomeRecordUpdateDto.getNotes() != null) {
            incomeRecord.setNotes(incomeRecordUpdateDto.getNotes());
        }

        updateStatusBasedOnPayment(incomeRecord); // Ödeme bilgilerine göre durumu tekrar saptayabiliriz.

        IncomeRecord updatedIncomeRecord = incomeRecordRepository.save(incomeRecord);

        // Yıllık bütçe toplamları güncelleniyor.
        if (updatedIncomeRecord.getBudgetYear() != null) {
            budgetYearService.updateBudgetYearTotals(updatedIncomeRecord.getBudgetYear().getId());
        }

        return mapToIncomeRecordDto(updatedIncomeRecord);
    }

    // Gelir durumunu ödeme miktarına ve beklenen miktara göre güncelleyen yardımcı
    // metot.
    // PRD [cite: 24, 25, 26, 27, 28]
    private void updateStatusBasedOnPayment(IncomeRecord incomeRecord) {
        if (incomeRecord.getPaidAmount() == null) {
            incomeRecord.setPaidAmount(BigDecimal.ZERO);
        }

        // Durumun ne olacağını belirlemeden önce, eğer bir statü DTO üzerinden geldiyse
        // onu koru,
        // ancak ödeme bilgileri bu statüyle çelişiyorsa üzerine yaz.
        IncomeStatusType initialStatus = incomeRecord.getStatus();

        // Beklenen tutar girilmemişse veya sıfırsa (genellikle olmaması gereken bir
        // durum ama kontrol edelim)
        if (incomeRecord.getExpectedAmount() == null
                || incomeRecord.getExpectedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            if (incomeRecord.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
                incomeRecord.setStatus(IncomeStatusType.ODENDI); // Beklenti yok ama ödeme var -> Ödendi
            } else {
                // Beklenti yok, ödeme de yok.
                // Vadesi geçtiyse ODENMEDI, aksi halde HENUZ_GELMEDI (ya da DTO'dan gelen
                // status korunur)
                if (initialStatus == IncomeStatusType.ODENDI || initialStatus == IncomeStatusType.KISMEN_ODENMIS) {
                    // Eğer DTO'dan ODENDI/KISMEN_ODENMIS gelmişse ama ödeme yoksa, bu bir
                    // çelişkidir.
                    // Bu durumda HENUZ_GELMEDI veya ODENMEDI daha mantıklı.
                    if (incomeRecord.getDueDate() != null
                            && incomeRecord.getDueDate().isBefore(java.time.LocalDate.now())) {
                        incomeRecord.setStatus(IncomeStatusType.ODENMEDI);
                    } else {
                        incomeRecord.setStatus(IncomeStatusType.HENUZ_GELMEDI);
                    }
                } else if (incomeRecord.getDueDate() != null
                        && incomeRecord.getDueDate().isBefore(java.time.LocalDate.now())) {
                    incomeRecord.setStatus(IncomeStatusType.ODENMEDI);
                } else if (initialStatus == null) { // DTO'dan status gelmemişse
                    incomeRecord.setStatus(IncomeStatusType.HENUZ_GELMEDI);
                } // else DTO'dan gelen initialStatus (HENUZ_GELMEDI veya ODENMEDI ise) korunur
            }
            return;
        }

        // Beklenen tutar var, şimdi ödenen miktarla karşılaştır.
        int comparison = incomeRecord.getPaidAmount().compareTo(incomeRecord.getExpectedAmount());

        if (comparison >= 0) { // Ödenen >= Beklenen (Tam veya fazla ödeme)
            incomeRecord.setStatus(IncomeStatusType.ODENDI);
        } else if (incomeRecord.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) { // 0 < Ödenen < Beklenen (Kısmi ödeme)
            incomeRecord.setStatus(IncomeStatusType.KISMEN_ODENMIS);
        } else { // Ödenen <= 0 (Ödeme yok veya geçersiz bir negatif ödeme)
            // Ödeme yok. Vadesi geçti mi?
            if (incomeRecord.getDueDate() != null && incomeRecord.getDueDate().isBefore(java.time.LocalDate.now())) {
                incomeRecord.setStatus(IncomeStatusType.ODENMEDI); // Vadesi geçmiş ve ödeme yok -> Ödenmedi
            } else {
                incomeRecord.setStatus(IncomeStatusType.HENUZ_GELMEDI); // Vadesi gelmemiş ve ödeme yok -> Henüz Gelmedi
            }
        }
    }

    @Override
    @Transactional
    public void deleteIncomeRecord(Long propertyId, Long budgetYearId, Long incomeRecordId) {
        BudgetYear budgetYear = validateAndGetBudgetYear(propertyId, budgetYearId);
        IncomeRecord incomeRecord = incomeRecordRepository.findById(incomeRecordId)
                .filter(ir -> ir.getBudgetYear().getId().equals(budgetYear.getId())
                        && ir.getOwner().getProperty().getId().equals(propertyId))
                .orElseThrow(() -> new RuntimeException("Silinecek Gelir Kaydı (ID: " + incomeRecordId
                        + ") bulunamadı veya belirtilen bütçe yılı/mülke ait değil."));

        Long budgetYearToUpdateId = incomeRecord.getBudgetYear() != null ? incomeRecord.getBudgetYear().getId() : null;

        incomeRecordRepository.delete(incomeRecord);

        // Yıllık bütçe toplamları güncelleniyor.
        if (budgetYearToUpdateId != null) {
            budgetYearService.updateBudgetYearTotals(budgetYearToUpdateId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeRecordDto> findByPropertyAndBudgetYearAndStatusIn(Long propertyId, Long budgetYearId,
            List<IncomeStatusType> statuses) {
        BudgetYear budgetYear = validateAndGetBudgetYear(propertyId, budgetYearId);
        return incomeRecordRepository.findByBudgetYearAndStatusIn(budgetYear, statuses)
                .stream()
                .map(this::mapToIncomeRecordDto)
                .collect(Collectors.toList());
    }

    // IncomeRecord entity'sini IncomeRecordDto'ya mapleyen yardımcı metot
    private IncomeRecordDto mapToIncomeRecordDto(IncomeRecord incomeRecord) {
        IncomeRecordDto dto = new IncomeRecordDto();
        dto.setId(incomeRecord.getId());
        dto.setMonth(incomeRecord.getMonth());
        dto.setDueDate(incomeRecord.getDueDate());
        dto.setExpectedAmount(incomeRecord.getExpectedAmount());
        dto.setPaidAmount(incomeRecord.getPaidAmount());
        dto.setPaymentDate(incomeRecord.getPaymentDate());
        dto.setStatus(incomeRecord.getStatus());
        dto.setNotes(incomeRecord.getNotes());
        if (incomeRecord.getOwner() != null) {
            dto.setOwnerId(incomeRecord.getOwner().getId());
            dto.setOwnerFullName(incomeRecord.getOwner().getFullName());
        }
        if (incomeRecord.getBudgetYear() != null) {
            dto.setBudgetYearId(incomeRecord.getBudgetYear().getId());
            dto.setBudgetYear(incomeRecord.getBudgetYear().getYear());
        }
        // dto.setMonthlyBudgetId(incomeRecord.getMonthlyBudget() != null ?
        // incomeRecord.getMonthlyBudget().getId() : null);
        return dto;
    }
}