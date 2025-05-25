package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Mevcut belge bilgilerini (metadata) güncelleme isteğini temsil eden DTO sınıfı.
// Bu DTO, bir belgenin dosya adı ve açıklaması gibi meta verilerini değiştirmek için kullanılır.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUpdateDto {

    // Güncellenmek istenen yeni dosya adı.
    // Opsiyoneldir. Eğer bu alan istekte null olarak gelirse veya hiç
    // gönderilmezse,
    // mevcut dosya adı değişmez. Eğer bir değer gönderilirse, boş olmamalıdır.
    // En fazla 255 karakter olabilir.
    @Size(max = 255, message = "Dosya adı en fazla 255 karakter olabilir.")
    private String fileName;

    // Güncellenmek istenen yeni belge açıklaması.
    // Opsiyoneldir. Eğer bu alan istekte null olarak gelirse veya hiç
    // gönderilmezse,
    // mevcut açıklama değişmez. Eğer bir değer gönderilirse, boş olmamalıdır.
    // En fazla 1000 karakter olabilir.
    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir.")
    private String description;

}