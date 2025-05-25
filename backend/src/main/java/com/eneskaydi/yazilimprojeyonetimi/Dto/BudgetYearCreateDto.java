package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

// Yeni Yıllık Bütçe oluşturma isteği için Veri Transfer Nesnesi (DTO).
// Bu DTO, bir mülk için yeni bir mali yıl bütçesi oluşturulurken kullanılır.
// API: POST /api/properties/{propertyId}/budget-years
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetYearCreateDto {

    // Bütçenin ait olduğu mali yıl.
    // Boş bırakılamaz ve en az 2024 olmalıdır.
    @NotNull(message = "Yıl boş bırakılamaz.")
    @Min(value = 2024, message = "Bütçe yılı en az 2024 olmalıdır.")
    private Integer year;

    // Yıllık toplam bütçe miktarı.
    // Boş bırakılamaz ve 0.01'den büyük olmalıdır.
    @NotNull(message = "Yıllık bütçe tutarı boş bırakılamaz.")
    @DecimalMin(value = "0.01", inclusive = true, message = "Yıllık bütçe tutarı 0.01'den büyük olmalıdır.")
    private BigDecimal annualBudgetAmount; // Eklendi ve PRD'ye uyarlandı

    // Bütçe yılı ile ilgili açıklama veya notlar.
    // En fazla 500 karakter olabilir.
    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir.")
    private String description; // 'notes' alanı 'description' olarak değiştirildi ve validasyon eklendi

    // Açılış bakiyesi genellikle bir önceki yıldan devir ile otomatik olarak
    // ayarlanır
    // veya bazı durumlarda manuel olarak girilebilir. Şimdilik opsiyonel bırakıldı.
    private BigDecimal openingBalance; // Bu alan korundu, yorum eklendi.

    // propertyId yol parametresi olarak alınacağından bu DTO içinde bulunmaz.
}