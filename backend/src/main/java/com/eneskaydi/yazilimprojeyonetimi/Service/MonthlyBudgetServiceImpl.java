package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.MonthlyBudgetUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.BudgetYear;
import com.eneskaydi.yazilimprojeyonetimi.Entity.MonthlyBudget;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import com.eneskaydi.yazilimprojeyonetimi.Repository.BudgetYearRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.MonthlyBudgetRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.PropertyRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

// Aylık bütçe yönetimi servisinin implementasyonu.
@Service
@RequiredArgsConstructor
public class MonthlyBudgetServiceImpl implements MonthlyBudgetService {

        private final MonthlyBudgetRepository monthlyBudgetRepository;
        private final BudgetYearRepository budgetYearRepository;
        private final PropertyRepository propertyRepository;
        private final UserRepository userRepository; // Mülk ve kullanıcı doğrulaması için
        private final BudgetYearService budgetYearService; // Eklendi

        // Mevcut kullanıcıyı (geçici olarak) getiren yardımcı metot.
        private User getCurrentUser() {
                // TODO: Spring Security entegrasyonu sonrası gerçek kullanıcı alınacak.
                return userRepository.findById(1L)
                                .orElseThrow(() -> new RuntimeException(
                                                "Varsayılan test kullanıcısı (ID: 1) bulunamadı."));
        }

        // Mülkün ve bütçe yılının mevcut kullanıcıya ait olup olmadığını kontrol eden
        // yardımcı metot.
        private BudgetYear getBudgetYearIfValid(Long propertyId, Long budgetYearId) {
                User currentUser = getCurrentUser();
                Property property = propertyRepository.findByIdAndUser(propertyId, currentUser)
                                .orElseThrow(() -> new RuntimeException(
                                                "Mülk (ID: " + propertyId
                                                                + ") bulunamadı veya mevcut kullanıcıya ait değil."));

                return budgetYearRepository.findById(budgetYearId)
                                .filter(by -> by.getProperty().getId().equals(property.getId()))
                                .orElseThrow(() -> new RuntimeException("Bütçe Yılı (ID: " + budgetYearId
                                                + ") bulunamadı veya belirtilen mülke (ID: " + propertyId
                                                + ") ait değil."));
        }

        @Override
        @Transactional
        public MonthlyBudgetDto createMonthlyBudget(Long propertyId, Long budgetYearId,
                        MonthlyBudgetCreateDto monthlyBudgetCreateDto) {
                BudgetYear budgetYear = getBudgetYearIfValid(propertyId, budgetYearId);

                // Aynı bütçe yılı içinde aynı ay için birden fazla kayıt olmamalı.
                if (monthlyBudgetRepository.existsByBudgetYearAndMonth(budgetYear, monthlyBudgetCreateDto.getMonth())) {
                        throw new RuntimeException(budgetYear.getYear() + " yılı " + monthlyBudgetCreateDto.getMonth() +
                                        ". ayı için zaten bir aylık bütçe mevcut.");
                }

                MonthlyBudget monthlyBudget = new MonthlyBudget();
                monthlyBudget.setMonth(monthlyBudgetCreateDto.getMonth());
                monthlyBudget.setTotalIncomeExpected(
                                monthlyBudgetCreateDto.getTotalIncomeExpected() != null
                                                ? monthlyBudgetCreateDto.getTotalIncomeExpected()
                                                : BigDecimal.ZERO);
                monthlyBudget.setTotalExpensesActual(
                                monthlyBudgetCreateDto.getTotalExpensesActual() != null
                                                ? monthlyBudgetCreateDto.getTotalExpensesActual()
                                                : BigDecimal.ZERO);
                monthlyBudget.setBudgetYear(budgetYear);

                MonthlyBudget savedMonthlyBudget = monthlyBudgetRepository.save(monthlyBudget);

                // BudgetYear'ın toplam gelir, toplam gider ve kapanış bakiyesi güncelleniyor.
                budgetYearService.updateBudgetYearTotals(budgetYear.getId());

                return mapToMonthlyBudgetDto(savedMonthlyBudget);
        }

        @Override
        @Transactional(readOnly = true)
        public List<MonthlyBudgetDto> getMonthlyBudgetsByBudgetYear(Long propertyId, Long budgetYearId) {
                BudgetYear budgetYear = getBudgetYearIfValid(propertyId, budgetYearId);
                return monthlyBudgetRepository.findByBudgetYearOrderByMonthAsc(budgetYear)
                                .stream()
                                .map(this::mapToMonthlyBudgetDto)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public MonthlyBudgetDto getMonthlyBudgetById(Long propertyId, Long budgetYearId, Long monthlyBudgetId) {
                BudgetYear budgetYear = getBudgetYearIfValid(propertyId, budgetYearId); // budgetYearId doğrulaması
                MonthlyBudget monthlyBudget = monthlyBudgetRepository.findById(monthlyBudgetId)
                                .filter(mb -> mb.getBudgetYear().getId().equals(budgetYear.getId()))
                                .orElseThrow(() -> new RuntimeException("Aylık Bütçe (ID: " + monthlyBudgetId +
                                                ") bulunamadı veya belirtilen bütçe yılına (ID: " + budgetYearId
                                                + ") ait değil."));
                return mapToMonthlyBudgetDto(monthlyBudget);
        }

        @Override
        @Transactional
        public MonthlyBudgetDto updateMonthlyBudget(Long propertyId, Long budgetYearId, Long monthlyBudgetId,
                        MonthlyBudgetUpdateDto monthlyBudgetUpdateDto) {
                BudgetYear budgetYear = getBudgetYearIfValid(propertyId, budgetYearId);
                MonthlyBudget monthlyBudget = monthlyBudgetRepository.findById(monthlyBudgetId)
                                .filter(mb -> mb.getBudgetYear().getId().equals(budgetYear.getId()))
                                .orElseThrow(() -> new RuntimeException(
                                                "Güncellenecek Aylık Bütçe (ID: " + monthlyBudgetId +
                                                                ") bulunamadı veya belirtilen bütçe yılına (ID: "
                                                                + budgetYearId + ") ait değil."));

                if (monthlyBudgetUpdateDto.getTotalIncomeExpected() != null) {
                        monthlyBudget.setTotalIncomeExpected(monthlyBudgetUpdateDto.getTotalIncomeExpected());
                }
                if (monthlyBudgetUpdateDto.getTotalExpensesActual() != null) {
                        monthlyBudget.setTotalExpensesActual(monthlyBudgetUpdateDto.getTotalExpensesActual());
                }

                MonthlyBudget updatedMonthlyBudget = monthlyBudgetRepository.save(monthlyBudget);

                // BudgetYear'ın toplam gelir, toplam gider ve kapanış bakiyesi güncelleniyor.
                budgetYearService.updateBudgetYearTotals(budgetYear.getId());

                return mapToMonthlyBudgetDto(updatedMonthlyBudget);
        }

        @Override
        @Transactional
        public void deleteMonthlyBudget(Long propertyId, Long budgetYearId, Long monthlyBudgetId) {
                BudgetYear budgetYear = getBudgetYearIfValid(propertyId, budgetYearId);
                MonthlyBudget monthlyBudget = monthlyBudgetRepository.findById(monthlyBudgetId)
                                .filter(mb -> mb.getBudgetYear().getId().equals(budgetYear.getId()))
                                .orElseThrow(() -> new RuntimeException(
                                                "Silinecek Aylık Bütçe (ID: " + monthlyBudgetId +
                                                                ") bulunamadı veya belirtilen bütçe yılına (ID: "
                                                                + budgetYearId + ") ait değil."));

                // IncomeRecord'lar doğrudan MonthlyBudget'e değil, BudgetYear'a bağlıdır,
                // bu nedenle MonthlyBudget silinmesi IncomeRecord'ları doğrudan etkilemez.
                // BudgetYear toplamları güncellenecektir.

                monthlyBudgetRepository.delete(monthlyBudget);

                // BudgetYear'ın toplam gelir, toplam gider ve kapanış bakiyesi güncelleniyor.
                budgetYearService.updateBudgetYearTotals(budgetYear.getId());
        }

        // MonthlyBudget entity'sini MonthlyBudgetDto'ya mapleyen yardımcı metot
        private MonthlyBudgetDto mapToMonthlyBudgetDto(MonthlyBudget monthlyBudget) {
                MonthlyBudgetDto dto = new MonthlyBudgetDto();
                dto.setId(monthlyBudget.getId());
                dto.setMonth(monthlyBudget.getMonth());
                dto.setTotalIncomeExpected(monthlyBudget.getTotalIncomeExpected());
                dto.setTotalExpensesActual(monthlyBudget.getTotalExpensesActual());
                // Bakiye hesaplama
                BigDecimal income = monthlyBudget.getTotalIncomeExpected() != null
                                ? monthlyBudget.getTotalIncomeExpected()
                                : BigDecimal.ZERO;
                BigDecimal expense = monthlyBudget.getTotalExpensesActual() != null
                                ? monthlyBudget.getTotalExpensesActual()
                                : BigDecimal.ZERO;
                dto.setBalance(income.subtract(expense));

                if (monthlyBudget.getBudgetYear() != null) {
                        dto.setBudgetYearId(monthlyBudget.getBudgetYear().getId());
                        dto.setYear(monthlyBudget.getBudgetYear().getYear()); // DTO'da year alanı var
                }
                return dto;
        }
}