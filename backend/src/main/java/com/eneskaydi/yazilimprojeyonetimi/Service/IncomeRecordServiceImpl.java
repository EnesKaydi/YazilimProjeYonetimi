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
import java.util.List;
import java.util.stream.Collectors;

// Gelir kayıtları yönetimi servisinin implementasyonu.
@Service
@RequiredArgsConstructor
public class IncomeRecordServiceImpl implements IncomeRecordService {

    private final IncomeRecordRepository incomeRecordRepository;
    private final BudgetYearRepository budgetYearRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BudgetYearService budgetYearService;

    // Geçici helper metot - Mevcut kullanıcıyı almak için.
    private User getCurrentUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Varsayılan test kullanıcısı (ID: 1) bulunamadı."));
    }

    // Sadece Mülk ve Bütçe Yılı doğrulaması yapan yardımcı metot.
    private BudgetYear validatePropertyAndBudgetYear(Long propertyId, Long budgetYearId) {
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
    public IncomeRecordDto createIncomeRecord(Long propertyId, Long budgetYearId,
            IncomeRecordCreateDto incomeRecordCreateDto) {
        BudgetYear budgetYear = validatePropertyAndBudgetYear(propertyId, budgetYearId);

        if (incomeRecordRepository.findByBudgetYearAndMonth(budgetYear, incomeRecordCreateDto.getMonth())
                .isPresent()) {
            throw new RuntimeException(budgetYear.getYear() + " yılı " +
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
        incomeRecord.setBudgetYear(budgetYear);

        updateStatusBasedOnPayment(incomeRecord);

        IncomeRecord savedIncomeRecord = incomeRecordRepository.save(incomeRecord);

        if (savedIncomeRecord.getBudgetYear() != null) {
            budgetYearService.updateBudgetYearTotals(savedIncomeRecord.getBudgetYear().getId());
        }

        return mapToIncomeRecordDto(savedIncomeRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeRecordDto> getIncomeRecordsByBudgetYear(Long propertyId, Long budgetYearId) {
        BudgetYear budgetYear = validatePropertyAndBudgetYear(propertyId, budgetYearId);
        return incomeRecordRepository.findByBudgetYear(budgetYear)
                .stream()
                .map(this::mapToIncomeRecordDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeRecordDto getIncomeRecordById(Long propertyId, Long budgetYearId, Long incomeRecordId) {
        BudgetYear budgetYear = validatePropertyAndBudgetYear(propertyId, budgetYearId);
        IncomeRecord incomeRecord = incomeRecordRepository.findById(incomeRecordId)
                .filter(ir -> ir.getBudgetYear() != null && ir.getBudgetYear().getId().equals(budgetYear.getId()) &&
                               ir.getBudgetYear().getProperty() != null && ir.getBudgetYear().getProperty().getId().equals(propertyId))
                .orElseThrow(() -> new RuntimeException("Gelir Kaydı (ID: " + incomeRecordId
                        + ") bulunamadı veya belirtilen bütçe yılı/mülke ait değil."));
        return mapToIncomeRecordDto(incomeRecord);
    }

    @Override
    @Transactional
    public IncomeRecordDto updateIncomeRecord(Long propertyId, Long budgetYearId, Long incomeRecordId,
            IncomeRecordUpdateDto incomeRecordUpdateDto) {
        BudgetYear budgetYear = validatePropertyAndBudgetYear(propertyId, budgetYearId);
        IncomeRecord incomeRecord = incomeRecordRepository.findById(incomeRecordId)
                .filter(ir -> ir.getBudgetYear() != null && ir.getBudgetYear().getId().equals(budgetYear.getId()) &&
                               ir.getBudgetYear().getProperty() != null && ir.getBudgetYear().getProperty().getId().equals(propertyId))
                .orElseThrow(() -> new RuntimeException("Güncellenecek Gelir Kaydı (ID: " + incomeRecordId
                        + ") bulunamadı veya belirtilen bütçe yılı/mülke ait değil."));

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

        updateStatusBasedOnPayment(incomeRecord);

        IncomeRecord updatedIncomeRecord = incomeRecordRepository.save(incomeRecord);

        if (updatedIncomeRecord.getBudgetYear() != null) {
            budgetYearService.updateBudgetYearTotals(updatedIncomeRecord.getBudgetYear().getId());
        }

        return mapToIncomeRecordDto(updatedIncomeRecord);
    }

    private void updateStatusBasedOnPayment(IncomeRecord incomeRecord) {
        if (incomeRecord.getPaidAmount() == null) {
            incomeRecord.setPaidAmount(BigDecimal.ZERO);
        }
        IncomeStatusType initialStatus = incomeRecord.getStatus();
        if (incomeRecord.getExpectedAmount() == null || incomeRecord.getExpectedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            if (incomeRecord.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
                incomeRecord.setStatus(IncomeStatusType.ODENDI);
            } else {
                if (initialStatus == IncomeStatusType.ODENDI || initialStatus == IncomeStatusType.KISMEN_ODENMIS) {
                    if (incomeRecord.getDueDate() != null && incomeRecord.getDueDate().isBefore(java.time.LocalDate.now())) {
                        incomeRecord.setStatus(IncomeStatusType.ODENMEDI);
                    } else {
                        incomeRecord.setStatus(IncomeStatusType.HENUZ_GELMEDI);
                    }
                } else if (incomeRecord.getDueDate() != null && incomeRecord.getDueDate().isBefore(java.time.LocalDate.now())) {
                    incomeRecord.setStatus(IncomeStatusType.ODENMEDI);
                } else if (initialStatus == null) { 
                    incomeRecord.setStatus(IncomeStatusType.HENUZ_GELMEDI);
                } 
            }
            return;
        }
        int comparison = incomeRecord.getPaidAmount().compareTo(incomeRecord.getExpectedAmount());
        if (comparison >= 0) { 
            incomeRecord.setStatus(IncomeStatusType.ODENDI);
        } else if (incomeRecord.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) { 
            incomeRecord.setStatus(IncomeStatusType.KISMEN_ODENMIS);
        } else { 
            if (incomeRecord.getDueDate() != null && incomeRecord.getDueDate().isBefore(java.time.LocalDate.now())) {
                incomeRecord.setStatus(IncomeStatusType.ODENMEDI);
            } else {
                incomeRecord.setStatus(IncomeStatusType.HENUZ_GELMEDI);
            }
        }
    }

    @Override
    @Transactional
    public void deleteIncomeRecord(Long propertyId, Long budgetYearId, Long incomeRecordId) {
        BudgetYear budgetYear = validatePropertyAndBudgetYear(propertyId, budgetYearId);
        IncomeRecord incomeRecord = incomeRecordRepository.findById(incomeRecordId)
                .filter(ir -> ir.getBudgetYear() != null && ir.getBudgetYear().getId().equals(budgetYear.getId()) &&
                               ir.getBudgetYear().getProperty() != null && ir.getBudgetYear().getProperty().getId().equals(propertyId))
                .orElseThrow(() -> new RuntimeException("Silinecek Gelir Kaydı (ID: " + incomeRecordId
                        + ") bulunamadı veya belirtilen bütçe yılı/mülke ait değil."));

        Long budgetYearToUpdateId = incomeRecord.getBudgetYear() != null ? incomeRecord.getBudgetYear().getId() : null;

        incomeRecordRepository.delete(incomeRecord);

        if (budgetYearToUpdateId != null) {
            budgetYearService.updateBudgetYearTotals(budgetYearToUpdateId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeRecordDto> findByPropertyAndBudgetYearAndStatusIn(Long propertyId, Long budgetYearId,
            List<IncomeStatusType> statuses) {
        BudgetYear budgetYear = validatePropertyAndBudgetYear(propertyId, budgetYearId);
        return incomeRecordRepository.findByBudgetYearAndStatusIn(budgetYear, statuses)
                .stream()
                .map(this::mapToIncomeRecordDto)
                .collect(Collectors.toList());
    }

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

        if (incomeRecord.getBudgetYear() != null) {
            dto.setBudgetYearId(incomeRecord.getBudgetYear().getId());
            dto.setBudgetYear(incomeRecord.getBudgetYear().getYear());
            if (incomeRecord.getBudgetYear().getProperty() != null) {
                // dto.setPropertyId(incomeRecord.getBudgetYear().getProperty().getId()); // Gerekirse DTO'ya ekle
            }
        }
        return dto;
    }
}