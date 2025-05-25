package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Bütçe devir işlemi için Veri Transfer Nesnesi (DTO).
// Bu DTO, bir mali yıldan diğerine bakiye transferi istendiğinde kullanılır.
// API: POST /api/properties/{propertyId}/budget-years/transfer
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetTransferRequestDto {

    // Bakiye devrinin yapılacağı kaynak mali yıl.
    // Bu alan boş bırakılamaz ve sistemdeki en erken mali yıldan küçük olmamalıdır.
    // Servis katmanı, bu yıldan bir sonraki mali yıla (sourceYear + 1) devir
    // işlemini gerçekleştirir.
    @NotNull(message = "Kaynak mali yıl boş bırakılamaz.")
    @Min(value = 2023, message = "Kaynak mali yıl en az 2023 olmalıdır.")
    private Integer sourceYear;

    // propertyId yol parametresi olarak alınacaktır.
    // Devredilecek olan bakiye miktarı (closingBalance), servis katmanında kaynak
    // yıldan (sourceYear)
    // otomatik olarak hesaplanır.
    // Hedef mali yıl (sourceYear + 1) da servis katmanında otomatik olarak
    // belirlenir.
    // Bu nedenle bu DTO'da ek alanlara ihtiyaç duyulmamıştır.
}