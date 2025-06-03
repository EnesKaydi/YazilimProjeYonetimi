package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.SettingsDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kullanıcıya özel veya sistem genelindeki ayarları yöneten REST controller.
 * Dil tercihleri, para birimi formatları, bildirim ayarları gibi ayarları
 * içerir.
 */
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    @Autowired
    private SettingsService settingsService;



    /**
     * Mevcut kullanıcıya ait veya sistem geneli ayarları getirir.
     * Servis katmanı, kimliği doğrulanmış kullanıcıya göre uygun ayarları
     * döndürmelidir.
     *
     * @return HTTP 200 OK ile SettingsDto içerisindeki ayar bilgileri.
     */
    @GetMapping
    public ResponseEntity<SettingsDto> getSettings() {
        // Eğer kullanıcıya özel ayarlar varsa, servis mevcut kullanıcıyı context'ten
        // almalıdır.
        SettingsDto settingsDto = settingsService.getSettings();
        return ResponseEntity.ok(settingsDto);
    }

    /**
     * Mevcut kullanıcıya ait veya sistem geneli ayarları günceller.
     *
     * @param settingsDto Güncellenecek ayar bilgilerini içeren DTO.
     * @return HTTP 200 OK ile güncellenmiş SettingsDto.
     */
    @PutMapping
    public ResponseEntity<SettingsDto> updateSettings(@Valid @RequestBody SettingsDto settingsDto) {
        SettingsDto updatedSettings = settingsService.updateSettings(settingsDto);
        return ResponseEntity.ok(updatedSettings);
    }
}