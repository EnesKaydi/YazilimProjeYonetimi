package com.eneskaydi.yazilimprojeyonetimi.Dto;

import com.eneskaydi.yazilimprojeyonetimi.Entity.IncomeStatusType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// Mevcut gelir kaydını güncelleme isteğini temsil eden Veri Transfer Nesnesi (DTO).
// Bu DTO, var olan bir gelir kaydının bilgilerini değiştirmek için kullanılır.
// Güncelleme sırasında tüm alanlar opsiyoneldir; sadece değiştirilmek istenen alanların
// değerleri gönderilir. Null gönderilen alanlar güncellenmez (servis implementasyonuna bağlı).
// API: PUT /api/properties/{propertyId}/budget-years/{budgetYearId}/income-records/{incomeRecordId}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeRecordUpdateDto {

    // Güncellenmek istenen beklenen ödemenin son tarihi (vade tarihi).
    // Eğer güncelleniyorsa, günümüz veya gelecek bir tarih olmalıdır.
    @FutureOrPresent(message = "Vade tarihi geçmiş bir tarih olamaz.")
    private LocalDate dueDate;

    // Güncellenmek istenen beklenen (tahakkuk eden) gelir miktarı.
    // Eğer güncelleniyorsa, 0.01'den büyük olmalıdır.
    @DecimalMin(value = "0.01", inclusive = true, message = "Beklenen miktar en az 0.01 olmalıdır.")
    private BigDecimal expectedAmount;

    // Güncellenmek istenen ödenen miktar.
    // Eğer güncelleniyorsa, 0.01'den büyük olmalıdır.
    @DecimalMin(value = "0.01", inclusive = true, message = "Ödenen miktar en az 0.01 olmalıdır.")
    private BigDecimal paidAmount;

    // Güncellenmek istenen ödeme tarihi.
    private LocalDate paymentDate;

    // Güncellenmek istenen gelir kaydının durumu.
    // Bu alanın güncellenmesi, ödeme mantığını tetikleyebilir.
    // PRD'ye göre durum yönetimi önemli olduğu için @NotNull eklenmiştir.
    // Ancak, bu alanın null gönderilerek güncellenmemesi isteniyorsa,
    // bu validasyon kaldırılıp servis katmanında kontrol edilebilir.
    @NotNull(message = "Gelir kaydı durumu boş bırakılamaz.")
    private IncomeStatusType status;

    // Güncellenmek istenen notlar.
    // En fazla 500 karakter olabilir.
    @Size(max = 500, message = "Notlar en fazla 500 karakter olabilir.")
    private String notes;
}