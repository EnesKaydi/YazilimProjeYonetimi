package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UnpaidOwnerNotificationDto;
import com.eneskaydi.yazilimprojeyonetimi.Repository.IncomeRecordRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.OwnerRepository;
import com.eneskaydi.yazilimprojeyonetimi.Repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * NotificationService arayüzünün implementasyonudur.
 * Mülk sahiplerine yönelik bildirimleri oluşturur ve yönetir.
 * Özellikle ödeme yapmayan sahiplerin takibi için kullanılır.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final OwnerRepository ownerRepository;
    private final IncomeRecordRepository incomeRecordRepository;
    private final PropertyRepository propertyRepository;

    /**
     * Gerekli repository bağımlılıkları ile NotificationServiceImpl örneği
     * oluşturur.
     * Bu constructor, Spring tarafından bağımlılıkların enjekte edilmesi için
     * kullanılır.
     *
     * @param ownerRepository        Sahip verilerine erişim için repository.
     * @param incomeRecordRepository Gelir kaydı verilerine erişim için repository.
     * @param propertyRepository     Mülk verilerine erişim için repository.
     */
    public NotificationServiceImpl(OwnerRepository ownerRepository,
            IncomeRecordRepository incomeRecordRepository,
            PropertyRepository propertyRepository) {
        this.ownerRepository = ownerRepository;
        this.incomeRecordRepository = incomeRecordRepository;
        this.propertyRepository = propertyRepository;
    }

    /**
     * Belirli bir mülk için ödenmemiş borcu olan sahiplerin listesini döndürür.
     * Henüz tam implementasyonu yapılmamıştır. PRD'ye göre, ödenmemiş gelir
     * kayıtlarını
     * kontrol ederek ilgili sahipleri bulması beklenir.
     *
     * @param propertyId Bildirimlerin oluşturulacağı mülkün ID'si.
     * @return Ödenmemiş sahip bildirimlerinin bir listesi. Şu anda boş bir liste
     *         döner.
     */
    @Override
    public List<UnpaidOwnerNotificationDto> getUnpaidOwnerNotifications(Long propertyId) {
        // TODO: Implement actual logic to find unpaid owners based on IncomeRecords.
        // 1. Property var mı kontrol et.
        // 2. Mülke ait tüm sahipleri al.
        // 3. Her sahip için ödenmemiş (ODENMEDI) veya kısmen ödenmiş (KISMEN_ODENMIS)
        // gelir kayıtlarını kontrol et.
        // 4. Eğer varsa, UnpaidOwnerNotificationDto oluştur ve listeye ekle.

        // Geçici olarak boş liste döndürülüyor.
        // Bu kısım, projenin ilerleyen aşamalarında detaylı iş mantığı ile
        // doldurulacaktır.
        return new ArrayList<>();
    }
}