package com.eneskaydi.yazilimprojeyonetimi.Dto;

import com.eneskaydi.yazilimprojeyonetimi.Entity.IncomeStatusType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// Yeni gelir kaydı oluşturma isteğini temsil eden Veri Transfer Nesnesi (DTO).
// Bu DTO, bir mülkün belirli bir bütçe yılı ve sahibi için yeni bir gelir beklentisi/kaydı
// oluşturulurken kullanılır.
// API: POST /api/properties/{propertyId}/budget-years/{budgetYearId}/owners/{ownerId}/income-records
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeRecordCreateDto {

    // Gelirin tahakkuk edeceği ay (1 ile 12 arasında bir değer).
    // Boş bırakılamaz.
    @NotNull(message = "Ay bilgisi boş bırakılamaz.")
    @Min(value = 1, message = "Ay değeri 1 ile 12 arasında olmalıdır.")
    @Max(value = 12, message = "Ay değeri 1 ile 12 arasında olmalıdır.")
    private Integer month;

    // Beklenen ödemenin son tarihi (vade tarihi).
    // Boş bırakılamaz ve günümüz veya gelecek bir tarih olmalıdır.
    @NotNull(message = "Vade tarihi boş bırakılamaz.")
    @FutureOrPresent(message = "Vade tarihi geçmiş bir tarih olamaz.")
    private LocalDate dueDate;

    // Beklenen (tahakkuk eden) gelir miktarı.
    // Boş bırakılamaz ve 0.01'den büyük olmalıdır.
    @NotNull(message = "Beklenen miktar boş bırakılamaz.")
    @DecimalMin(value = "0.01", inclusive = true, message = "Beklenen miktar en az 0.01 olmalıdır.")
    private BigDecimal expectedAmount;

    // Kısmi veya tam ödeme yapıldıysa ödenen miktar.
    // Opsiyoneldir. Girilirse, 0.01'den büyük olmalıdır.
    @DecimalMin(value = "0.01", inclusive = true, message = "Ödenen miktar en az 0.01 olmalıdır.")
    private BigDecimal paidAmount;

    // Ödeme yapıldıysa ödemenin gerçekleştiği tarih.
    // Opsiyoneldir. Genellikle `paidAmount` ile birlikte girilir.
    private LocalDate paymentDate;

    // Gelir kaydının mevcut durumu (HENUZ_GELMEDI, ODENDI, ODENMEDI,
    // KISMEN_ODENDI).
    // Yeni kayıtlarda genellikle servis tarafından HENUZ_GELMEDI olarak ayarlanır
    // veya
    // ödeme durumuna göre otomatik güncellenir. İstemci tarafından da
    // gönderilebilir.
    private IncomeStatusType status;

    // Gelir kaydıyla ilgili ek notlar veya açıklamalar.
    // En fazla 500 karakter olabilir.
    @jakarta.validation.constraints.Size(max = 500, message = "Notlar en fazla 500 karakter olabilir.")
    private String notes;

    // `ownerId` ve `budgetYearId` bilgileri URL path parametrelerinden alınır,
    // bu nedenle bu DTO içerisinde ayrıca belirtilmelerine gerek yoktur.
}