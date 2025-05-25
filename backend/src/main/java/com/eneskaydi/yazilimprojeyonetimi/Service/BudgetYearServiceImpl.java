package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetTransferRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetYearCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetYearDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.BudgetYear;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import com.eneskaydi.yazilimprojeyonetimi.Repository.BudgetYearRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.PropertyRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.IncomeRecordRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.MonthlyBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

// Yıllık bütçe yönetimi servisinin implementasyonu.
// Bu servis, bütçe yıllarının oluşturulması, güncellenmesi, silinmesi, listelenmesi,
// ve bütçe transferleri gibi işlemleri yönetir.
@Service
@RequiredArgsConstructor
public class BudgetYearServiceImpl implements BudgetYearService {

        private final BudgetYearRepository budgetYearRepository;
        private final PropertyRepository propertyRepository;
        private final UserRepository userRepository; // Mülk ve kullanıcı doğrulaması ve yetkilendirme işlemleri için
                                                     // kullanılır.
        private final IncomeRecordRepository incomeRecordRepository;
        private final MonthlyBudgetRepository monthlyBudgetRepository;

        // Mevcut kullanıcıyı (geçici olarak) getiren yardımcı metot.
        // TODO: Bu metot, Spring Security gibi gerçek bir kimlik doğrulama mekanizması
        // entegre edildiğinde güncellenmelidir.
        // Şimdilik, test ve geliştirme kolaylığı için ID'si 1 olan sabit bir kullanıcı
        // döndürmektedir.
        private User getCurrentUser() {
                return userRepository.findById(1L) // Varsayılan test kullanıcısı (ID: 1)
                                .orElseThrow(() -> new RuntimeException(
                                                "Varsayılan test kullanıcısı (ID: 1) bulunamadı."));
        }

        // Mülkün mevcut kullanıcıya ait olup olmadığını kontrol eden yardımcı metot.
        // Belirtilen mülkün (propertyId) o anki kullanıcıya ait olup olmadığını
        // doğrular.
        // Eğer mülk bulunamazsa veya kullanıcıya ait değilse RuntimeException fırlatır.
        private Property getPropertyIfBelongsToCurrentUser(Long propertyId) {
                User currentUser = getCurrentUser();
                return propertyRepository.findByIdAndUser(propertyId, currentUser)
                                .orElseThrow(() -> new RuntimeException(
                                                "Mülk (ID: " + propertyId
                                                                + ") bulunamadı veya mevcut kullanıcıya ait değil."));
        }

        @Override
        @Transactional
        // Belirtilen mülk için yeni bir bütçe yılı oluşturur.
        // budgetYearCreateDto içindeki bilgilerle bütçe yılını başlatır.
        // Aynı mülk için aynı yılda birden fazla bütçe oluşturulmasını engeller.
        public BudgetYearDto createBudgetYear(Long propertyId, BudgetYearCreateDto budgetYearCreateDto) {
                Property property = getPropertyIfBelongsToCurrentUser(propertyId);
                // Yılın benzersizliğini kontrol et
                if (budgetYearRepository.existsByPropertyAndYear(property, budgetYearCreateDto.getYear())) {
                        throw new RuntimeException(
                                        "Bu mülk için " + budgetYearCreateDto.getYear()
                                                        + " yılında zaten bir bütçe mevcut.");
                }

                BudgetYear budgetYear = new BudgetYear();
                budgetYear.setYear(budgetYearCreateDto.getYear());
                budgetYear.setOpeningBalance(
                                budgetYearCreateDto.getOpeningBalance() != null
                                                ? budgetYearCreateDto.getOpeningBalance()
                                                : BigDecimal.ZERO);
                budgetYear.setDescription(budgetYearCreateDto.getDescription());
                budgetYear.setProperty(property);
                // closingBalance ve totalIncome/totalExpense başlangıçta sıfır veya
                // hesaplanacak

                BudgetYear savedBudgetYear = budgetYearRepository.save(budgetYear);
                return mapToBudgetYearDto(savedBudgetYear);
        }

        @Override
        @Transactional(readOnly = true)
        // Belirli bir mülke (propertyId) ait tüm bütçe yıllarını listeler.
        // Sonuçlar, yıllara göre azalan sırada (en yeni yıl en başta) döner.
        public List<BudgetYearDto> getBudgetYearsByProperty(Long propertyId) {
                Property property = getPropertyIfBelongsToCurrentUser(propertyId);
                // Yıla göre tersten sıralı getirme (en son yıl en başta)
                return budgetYearRepository.findByPropertyOrderByYearDesc(property)
                                .stream()
                                .map(this::mapToBudgetYearDto)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        // Belirli bir mülke (propertyId) ait spesifik bir bütçe yılını (budgetYearId)
        // getirir.
        // Mülk sahipliği doğrulaması yapılır.
        public BudgetYearDto getBudgetYearById(Long propertyId, Long budgetYearId) {
                Property property = getPropertyIfBelongsToCurrentUser(propertyId);
                BudgetYear budgetYear = budgetYearRepository.findById(budgetYearId)
                                .filter(by -> by.getProperty().getId().equals(property.getId()))
                                .orElseThrow(() -> new RuntimeException("Bütçe Yılı (ID: " + budgetYearId
                                                + ") bulunamadı veya belirtilen mülke (ID: " + propertyId
                                                + ") ait değil."));
                return mapToBudgetYearDto(budgetYear);
        }

        @Override
        @Transactional
        // Mevcut bir bütçe yılının (budgetYearId) bilgilerini günceller.
        // Sadece açılış bakiyesi (openingBalance) ve açıklama (description) alanları
        // güncellenebilir.
        // Yıl (year) alanı değiştirilemez. Kapanış bakiyesi, toplam gelir/gider gibi
        // alanlar
        // diğer operasyonlarla (örn: updateBudgetYearTotals) hesaplanır.
        public BudgetYearDto updateBudgetYear(Long propertyId, Long budgetYearId, BudgetYearDto budgetYearDto) {
                Property property = getPropertyIfBelongsToCurrentUser(propertyId);
                BudgetYear budgetYear = budgetYearRepository.findById(budgetYearId)
                                .filter(by -> by.getProperty().getId().equals(property.getId()))
                                .orElseThrow(() -> new RuntimeException("Güncellenecek Bütçe Yılı (ID: " + budgetYearId
                                                + ") bulunamadı veya belirtilen mülke (ID: " + propertyId
                                                + ") ait değil."));

                // Yıl değiştirilemez varsayıyoruz, sadece diğer alanlar güncellenebilir
                if (budgetYearDto.getOpeningBalance() != null) {
                        budgetYear.setOpeningBalance(budgetYearDto.getOpeningBalance());
                }
                if (budgetYearDto.getDescription() != null) {
                        budgetYear.setDescription(budgetYearDto.getDescription());
                }
                // Diğer alanlar (closingBalance, totalIncome, totalExpense) aylık bütçelerden
                // hesaplanmalı

                BudgetYear updatedBudgetYear = budgetYearRepository.save(budgetYear);
                return mapToBudgetYearDto(updatedBudgetYear);
        }

        @Override
        @Transactional
        // Bir bütçe yılını (budgetYearId) siler.
        // Silme işleminden önce mülk sahipliği kontrol edilir.
        // TODO: PRD'ye (Proje Gereksinim Dokümanı) göre, eğer bütçe yılına bağlı aylık
        // bütçeler
        // veya gelir/gider kayıtları varsa, bu kayıtların durumu (silinmeli mi,
        // arşivlenmeli mi,
        // silme engellenmeli mi) netleştirilmelidir.
        public void deleteBudgetYear(Long propertyId, Long budgetYearId) {
                Property property = getPropertyIfBelongsToCurrentUser(propertyId);
                BudgetYear budgetYear = budgetYearRepository.findById(budgetYearId)
                                .filter(by -> by.getProperty().getId().equals(property.getId()))
                                .orElseThrow(() -> new RuntimeException("Silinecek Bütçe Yılı (ID: " + budgetYearId
                                                + ") bulunamadı veya belirtilen mülke (ID: " + propertyId
                                                + ") ait değil."));

                // TODO: İlişkili MonthlyBudget ve IncomeRecord kayıtlarının silinmesi veya
                // kontrolü (PRD'ye göre)
                // Örneğin, içinde kayıtlar varsa silme engellenebilir.
                budgetYearRepository.delete(budgetYear);
        }

        @Override
        @Transactional
        // Bir bütçe yılından (kaynak yıl) bir sonraki bütçe yılına (hedef yıl) bakiye
        // transferi yapar.
        // Genellikle yıl sonunda, kapanan yılın bakiyesinin yeni yıla devredilmesi için
        // kullanılır.
        // PRD [cite: 17] - Yıl tamamlandığında bütçe transferi maddesine göre işlem
        // yapar.
        // Kaynak ve hedef yılların varlığı ve mülke ait olduğu kontrol edilir.
        // TODO: Transfer edilecek miktarın (transferAmount) belirlenme mantığı PRD'ye
        // göre detaylı incelenmeli.
        // Şu anki implementasyon kaynak yılın kapanış bakiyesini doğrudan transfer
        // eder.
        public BudgetYearDto transferBudget(Long propertyId, BudgetTransferRequestDto budgetTransferRequestDto) {
                Property property = getPropertyIfBelongsToCurrentUser(propertyId);
                int sourceYearVal = budgetTransferRequestDto.getSourceYear();
                int targetYearVal = sourceYearVal + 1; // Hedef yılı kaynak yıldan bir sonraki yıl olarak belirle

                // Kaynak ve hedef bütçe yıllarını bul
                BudgetYear sourceBudgetYear = budgetYearRepository.findByPropertyAndYear(property, sourceYearVal)
                                .orElseThrow(() -> new RuntimeException(
                                                "Kaynak bütçe yılı (" + sourceYearVal + ") bulunamadı."));
                BudgetYear targetBudgetYear = budgetYearRepository.findByPropertyAndYear(property, targetYearVal)
                                .orElseThrow(() -> new RuntimeException("Hedef bütçe yılı (" + targetYearVal
                                                + ") bulunamadı. "
                                                + "Lütfen önce " + targetYearVal + " için bir bütçe yılı oluşturun."));

                // PRD [cite: 17] - Yıl tamamlandığında bütçe transferi
                // Basit bir transfer: Kaynak yılın kapanış bakiyesini hedef yılın açılış
                // bakiyesine ekle/ayarla.
                // Daha karmaşık kurallar (örn: sadece pozitif/negatif transfer, belirli bir
                // methoda göre) eklenebilir.

                BigDecimal transferAmount = sourceBudgetYear.getClosingBalance(); // Veya hesaplanmış bir değer
                // TODO: PRD'ye göre transferAmount nasıl belirlenecek?
                // Şimdilik kaynak yılın kapanış bakiyesini alıyoruz.
                // Eğer PRD'de belirtilmişse, transfer edilecek miktar (pozitif/negatif)
                // requestDto'dan da gelebilir.

                // Hedef yılın açılış bakiyesini güncelle
                targetBudgetYear.setOpeningBalance(targetBudgetYear.getOpeningBalance().add(transferAmount));

                // Not: Kaynak yılın durumu "Tamamlandı" veya "Arşivlendi" gibi bir işarete
                // sahip olabilir.

                BudgetYear updatedTargetBudgetYear = budgetYearRepository.save(targetBudgetYear);
                return mapToBudgetYearDto(updatedTargetBudgetYear);
        }

        @Override
        @Transactional
        // Belirtilen bir bütçe yılının (budgetYearId) toplam gelir, toplam gider ve
        // kapanış bakiyesi
        // alanlarını, ilişkili kayıtlara (IncomeRecord, MonthlyBudget) göre yeniden
        // hesaplar ve günceller.
        // Bu metot, genellikle yeni bir gelir/gider eklendiğinde veya güncellendiğinde
        // çağrılır.
        public void updateBudgetYearTotals(Long budgetYearId) {
                BudgetYear budgetYear = budgetYearRepository.findById(budgetYearId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Bütçe Yılı (ID: " + budgetYearId + ") bulunamadı."));

                // Toplam geliri hesapla (IncomeRecord'lardan)
                BigDecimal totalIncome = incomeRecordRepository.findByBudgetYear(budgetYear).stream()
                                .map(incomeRecord -> incomeRecord.getPaidAmount() != null ? incomeRecord.getPaidAmount()
                                                : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                budgetYear.setTotalIncome(totalIncome);

                // Toplam gideri hesapla (MonthlyBudget'lerden - totalExpensesActual
                // varsayımıyla)
                // PRD'ye göre bu kısım değişebilir eğer giderler farklı bir yerden geliyorsa.
                BigDecimal totalExpense = monthlyBudgetRepository.findByBudgetYear(budgetYear).stream()
                                .map(monthlyBudget -> monthlyBudget.getTotalExpensesActual() != null
                                                ? monthlyBudget.getTotalExpensesActual()
                                                : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                budgetYear.setTotalExpense(totalExpense);

                // Kapanış bakiyesini hesapla
                BigDecimal openingBalance = budgetYear.getOpeningBalance() != null ? budgetYear.getOpeningBalance()
                                : BigDecimal.ZERO;
                budgetYear.setClosingBalance(openingBalance.add(totalIncome).subtract(totalExpense));

                budgetYearRepository.save(budgetYear);
        }

        // BudgetYear entity nesnesini BudgetYearDto transfer nesnesine dönüştürür.
        // Bu metot, servis katmanından dışarıya veri döndürülürken kullanılır.
        private BudgetYearDto mapToBudgetYearDto(BudgetYear budgetYear) {
                BudgetYearDto dto = new BudgetYearDto();
                dto.setId(budgetYear.getId());
                dto.setYear(budgetYear.getYear());
                dto.setOpeningBalance(budgetYear.getOpeningBalance());
                dto.setClosingBalance(budgetYear.getClosingBalance()); // Bu alanın hesaplanması gerekebilir
                dto.setTotalIncome(budgetYear.getTotalIncome()); // Bu alanın hesaplanması gerekebilir
                dto.setTotalExpense(budgetYear.getTotalExpense()); // Bu alanın hesaplanması gerekebilir
                dto.setDescription(budgetYear.getDescription());
                if (budgetYear.getProperty() != null) {
                        dto.setPropertyId(budgetYear.getProperty().getId());
                }
                // Aylık bütçe detayları (MonthlyBudgetDto listesi) burada yüklenebilir veya
                // ayrı bir endpoint ile alınabilir.
                // dto.setMonthlyBudgets(...);
                return dto;
        }
}