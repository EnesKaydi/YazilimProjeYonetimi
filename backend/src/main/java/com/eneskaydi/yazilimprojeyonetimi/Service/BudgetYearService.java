package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetTransferRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetYearCreateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.BudgetYearDto;

import java.util.List;

// Yıllık bütçe yönetimi işlemlerinden sorumlu servis arayüzü.
// Bu servis, bir mülke ait bütçe yıllarının oluşturulması, listelenmesi,
// güncellenmesi, silinmesi ve yıl sonu bütçe transferi gibi işlemleri yönetir.
public interface BudgetYearService {

    /**
     * Belirli bir mülk için yeni bir bütçe yılı oluşturur.
     * Yılın benzersiz olması beklenir.
     *
     * @param propertyId          Bütçe yılının ekleneceği mülkün ID'si.
     * @param budgetYearCreateDto Oluşturulacak bütçe yılı bilgilerini içeren DTO.
     * @return Oluşturulan bütçe yılı bilgilerini içeren DTO.
     * @throws RuntimeException Mülk bulunamazsa veya aynı yıl için zaten bir bütçe
     *                          varsa.
     */
    BudgetYearDto createBudgetYear(Long propertyId, BudgetYearCreateDto budgetYearCreateDto);

    /**
     * Belirli bir mülke ait tüm bütçe yıllarını, en yeniden eskiye doğru sıralı
     * olarak listeler.
     *
     * @param propertyId Bütçe yılları listelenecek mülkün ID'si.
     * @return Mülke ait bütçe yıllarının listesi.
     * @throws RuntimeException Mülk bulunamazsa.
     */
    List<BudgetYearDto> getBudgetYearsByProperty(Long propertyId);

    /**
     * Belirli bir mülke ait spesifik bir bütçe yılını ID'sine göre getirir.
     *
     * @param propertyId   Bütçe yılının ait olduğu mülkün ID'si.
     * @param budgetYearId Getirilecek bütçe yılının ID'si.
     * @return Bütçe yılı bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya bütçe yılı bulunamazsa, ya da bütçe yılı
     *                          belirtilen mülke ait değilse.
     */
    BudgetYearDto getBudgetYearById(Long propertyId, Long budgetYearId);

    /**
     * Belirli bir mülke ait bir bütçe yılını günceller.
     * Genellikle başlangıç bakiyesi veya notlar gibi alanlar güncellenebilir.
     *
     * @param propertyId    Güncellenecek bütçe yılının ait olduğu mülkün ID'si.
     * @param budgetYearId  Güncellenecek bütçe yılının ID'si.
     * @param budgetYearDto Güncellenecek bütçe yılı bilgilerini içeren DTO (veya
     *                      özel bir UpdateDto).
     * @return Güncellenmiş bütçe yılı bilgilerini içeren DTO.
     * @throws RuntimeException Mülk veya bütçe yılı bulunamazsa.
     */
    BudgetYearDto updateBudgetYear(Long propertyId, Long budgetYearId, BudgetYearDto budgetYearDto); // Basitlik için
                                                                                                     // BudgetYearDto
                                                                                                     // kullanıldı

    /**
     * Belirli bir mülke ait bir bütçe yılını siler.
     * Dikkat: Bu işlem, ilişkili tüm aylık bütçeleri ve gelir kayıtlarını da
     * silebilir.
     *
     * @param propertyId   Silinecek bütçe yılının ait olduğu mülkün ID'si.
     * @param budgetYearId Silinecek bütçe yılının ID'si.
     * @throws RuntimeException Mülk veya bütçe yılı bulunamazsa veya silme işlemi
     *                          engellenirse (örn: bağımlı kayıtlar varsa).
     */
    void deleteBudgetYear(Long propertyId, Long budgetYearId);

    /**
     * Bir mülkün bir bütçe yılından diğerine (genellikle bir sonraki yıla)
     * pozitif veya negatif bakiye transferini gerçekleştirir.
     * PRD [cite: 17]
     *
     * @param propertyId               İşlemin yapılacağı mülkün ID'si.
     * @param budgetTransferRequestDto Transfer detaylarını içeren DTO (kaynak yıl,
     *                                 hedef yıl, transfer metodu vb.).
     * @return Transfer işlemi sonrası hedef yılın güncel bütçe bilgilerini içeren
     *         DTO.
     * @throws RuntimeException Mülk, kaynak veya hedef bütçe yılı bulunamazsa, veya
     *                          transfer işlemi kurallara aykırıysa.
     */
    BudgetYearDto transferBudget(Long propertyId, BudgetTransferRequestDto budgetTransferRequestDto);

    /**
     * Belirli bir bütçe yılının toplam gelir, toplam gider ve kapanış bakiyesini,
     * ilişkili gelir kayıtları ve aylık bütçe giderlerine göre günceller.
     * Bu metot, genellikle bir IncomeRecord veya MonthlyBudget kaydı
     * eklendiğinde/güncellendiğinde/silindiğinde çağrılır.
     *
     * @param budgetYearId Güncellenecek bütçe yılının ID'si.
     * @throws RuntimeException Bütçe yılı bulunamazsa.
     */
    void updateBudgetYearTotals(Long budgetYearId);
}