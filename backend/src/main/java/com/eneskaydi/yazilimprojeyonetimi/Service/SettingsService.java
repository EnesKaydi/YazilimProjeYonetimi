package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.SettingsDto;

// Ayarlar yönetimi ile ilgili işlemleri gerçekleştiren servis arayüzü.
// Kullanıcı tercihleri (dil, para birimi vb.) ve sistemsel ayarları (yedekleme/geri yükleme) yönetir.
public interface SettingsService {

    /**
     * Mevcut kullanıcıya ait veya sistem geneli ayarları getirir.
     * Implementasyon, kimliği doğrulanmış kullanıcıya göre uygun ayarları
     * döndürmelidir.
     *
     * @return Ayar bilgilerini içeren SettingsDto.
     */
    SettingsDto getSettings();

    /**
     * Mevcut kullanıcıya ait veya sistem geneli ayarları günceller.
     *
     * @param settingsDto Güncellenecek ayar bilgilerini içeren DTO.
     * @return Güncellenmiş ayar bilgilerini içeren SettingsDto.
     */
    SettingsDto updateSettings(SettingsDto settingsDto);

    /**
     * Veritabanının yedeğini alır.
     * Yedekleme işleminin detayları (örn: dosya yolu, formatı) implementasyona
     * bağlıdır.
     *
     * @throws RuntimeException Yedekleme sırasında bir hata oluşursa.
     */
    void backupDatabase(); // Başarılı olursa belki bir mesaj veya dosya yolu dönebilir.

    /**
     * Belirli bir yedekten veritabanını geri yükler.
     * Geri yüklenecek yedek dosyasının nasıl belirtileceği implementasyona
     * bağlıdır.
     * Bu işlem genellikle dikkatli kullanılmalıdır ve veri kaybına neden olabilir.
     *
     * @param backupIdentifier Geri yüklenecek yedek dosyasını tanımlayan bilgi
     *                         (örn: dosya adı, path).
     * @throws RuntimeException Geri yükleme sırasında bir hata oluşursa.
     */
    void restoreDatabase(String backupIdentifier);

}