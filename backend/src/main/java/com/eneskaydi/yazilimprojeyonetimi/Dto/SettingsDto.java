package com.eneskaydi.yazilimprojeyonetimi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Uygulama ayarlarını (dil, para birimi formatı vb.) içeren veri transfer
 * nesnesi.
 * Hem ayarları getirmek hem de güncellemek için kullanılabilir.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto {

    // Kullanıcının tercih ettiği dil (örn: "tr-TR", "en-US")
    @NotBlank(message = "Dil tercihi boş bırakılamaz.")
    @Size(min = 2, max = 5, message = "Dil kodu 2 ile 5 karakter arasında olmalıdır (örn: tr, en-US).")
    private String language;

    // Kullanıcının tercih ettiği para birimi (örn: "TRY", "USD", "EUR")
    @NotBlank(message = "Para birimi tercihi boş bırakılamaz.")
    @Size(min = 3, max = 3, message = "Para birimi kodu 3 karakter olmalıdır (örn: TRY, USD).")
    private String currency;

    // Bildirim tercihleri (örneğin e-posta ile bildirim açık mı?)
    // Bu daha karmaşık bir yapıya da sahip olabilir, şimdilik boolean varsayalım.
    @NotNull(message = "E-posta bildirim ayarı boş bırakılamaz.")
    private Boolean emailNotificationsEnabled;

    // Diğer olası ayarlar eklenebilir...
    // private String dateFormat;
    // private String timeZone;
}