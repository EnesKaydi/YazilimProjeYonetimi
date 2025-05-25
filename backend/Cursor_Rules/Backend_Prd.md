# Backend - Ürün Gereksinimleri Dokümanı (PRD)

## 1. Giriş

Bu doküman, Ev Sahipleri Derneği Yönetim Sistemi projesinin backend altyapısı için gereksinimleri ve özellikleri tanımlamaktadır. Backend, Spring Boot kullanılarak geliştirilecektir[cite: 2]. Sistem, mülk yönetimi, bütçeleme, raporlama ve belge işleme gibi temel işlevleri destekleyecektir[cite: 2].

## 2. Genel Amaçlar

* Güvenli ve ölçeklenebilir bir API sağlamak.
* Frontend uygulaması için gerekli tüm veri ve iş mantığını yönetmek.
* Veritabanı etkileşimlerini verimli bir şekilde gerçekleştirmek (JPA aracılığıyla).
* Kullanıcı kimlik doğrulama ve yetkilendirme işlemlerini güvenli bir şekilde yönetmek. [cite: 4]

## 3. Fonksiyonel Gereksinimler

### 3.1. Kullanıcı Yönetimi ve Kimlik Doğrulama
    * **Kullanıcı Girişi:**
        * Sistem, varsayılan yönetici kimlik bilgileriyle temel bir oturum açma işlevi sunmalıdır. [cite: 3]
        * API endpoint: `POST /api/auth/login`
    * **Güvenlik:**
        * Güvenli kimlik doğrulama (örn: JWT) ve rol bazlı yetkilendirme mekanizmaları sağlanmalıdır. [cite: 4]
        * Parolalar güvenli bir şekilde hashlenerek saklanmalıdır.
    * **Profil Yönetimi:**
        * Kullanıcıların kendi profil bilgilerini (örn: şifre değişikliği) yönetebilmesi için API desteği. [cite: 8]
        * API endpoint: `GET /api/users/me`, `PUT /api/users/me`

### 3.2. Mülk Yönetimi
    * **Mülk CRUD İşlemleri:**
        * Yeni mülk oluşturma, mevcut mülkleri görüntüleme, güncelleme ve silme işlemleri için API endpoint'leri. [cite: 5]
        * API endpoints: `POST /api/properties`, `GET /api/properties`, `GET /api/properties/{propertyId}`, `PUT /api/properties/{propertyId}`, `DELETE /api/properties/{propertyId}`
        * Bir kullanıcı birden fazla mülk oluşturabilmelidir. [cite: 5]
    * **Mülk Detayları:**
        * Mülk adı, adresi, tapu bilgisi, dernek adı, şehir ve para birimi gibi bilgilerin yönetimi. [cite: 9]
        * Şehirler için otomatik tamamlama özelliği desteklenmelidir (tüm dünya şehirleri). [cite: 9, 10]
        * Para birimleri için tüm dünya ülkelerinin para birimleri desteklenmelidir. [cite: 10, 11]
    * **Modül Erişimi Kontrolü:**
        * Bir mülk seçilmeden mülke özgü modüllerin (Birim Türleri, Sahip Detayları vb.) API'lerinin erişilemez olması sağlanmalıdır. [cite: 6] Bu önemli bir kuraldır. [cite: 7]

### 3.3. Birim Türleri Yönetimi
    * **Birim Türü CRUD İşlemleri:**
        * Farklı birim türleri (örn: büyük daire, küçük daire, garaj) oluşturma, aylık ücretlerini ve tanımlarını yönetme. [cite: 11]
        * API endpoints: `POST /api/properties/{propertyId}/unit-types`, `GET /api/properties/{propertyId}/unit-types`, `PUT /api/properties/{propertyId}/unit-types/{unitTypeId}`, `DELETE /api/properties/{propertyId}/unit-types/{unitTypeId}`

### 3.4. Sahipler Listesi Yönetimi
    * **Sahip CRUD ve Çoğaltma İşlemleri:**
        * Sahip bilgilerini (tam ad, adres, telefon, e-posta, banka hesap numarası, birim türü, tapu numarası) saklama, okuma, güncelleme, silme ve çoğaltma işlemleri. [cite: 12, 15]
        * API endpoints: `POST /api/properties/{propertyId}/owners`, `GET /api/properties/{propertyId}/owners`, `GET /api/properties/{propertyId}/owners/{ownerId}`, `PUT /api/properties/{propertyId}/owners/{ownerId}`, `DELETE /api/properties/{propertyId}/owners/{ownerId}`, `POST /api/properties/{propertyId}/owners/{ownerId}/duplicate`
    * **Kiracı Yönetimi:**
        * "Kiralanmış" seçeneği işaretlendiğinde, kiracı detaylarının (tam ad, adres, telefon, e-posta, banka hesap numarası) girilebilmesi için API desteği. [cite: 13]
        * Kullanıcının sistemde kişiyi "Mal Sahibi" veya "Kiracı" olarak gösterme seçeneği olmalıdır. [cite: 14]

### 3.5. Bütçe Yönetimi
    * **Düzenli Bütçe:**
        * Yıllık bütçelerin (2025'ten başlayarak) yönetimi. [cite: 16]
        * Yıl tamamlandığında, bütçenin bir önceki yıldan bir sonraki yıla pozitif veya negatif olarak aktarılması için bir iş mantığı ve API endpoint'i. [cite: 17]
            * API endpoint: `POST /api/properties/{propertyId}/budgets/transfer`
        * Tüm rakamlar, Mülk Detayları'nda seçilen para birimini takip etmelidir. [cite: 18]
        * Her ay için gelir ve sonucun takibi. [cite: 19] Gelirler "+" sembolü, sonuçlar "-" sembolü ile gösterilmelidir. [cite: 20]
        * Bakiyenin hesaplanması ve API üzerinden sunulması. [cite: 20]
    * **Gelir Matrisi Tablosu İş Mantığı:**
        * **Durum Yönetimi:**
            * "Henüz Gelmedi" (gelecek aylar için, sadece Peşin Ödeme yapılabilir). [cite: 23, 24]
            * "Ödendi" (miktar ve para birimi ile). [cite: 24]
            * "Ödenmedi" (tutarın önünde eksi sembolü ve para birimi ile). [cite: 25]
            * Giriş yapılmayan herhangi bir bitmiş ay, satırda herhangi bir işlem yapılmadıysa otomatik olarak "Ödenmedi" (kırmızı) olarak işaretlenmelidir (Bu bir kuraldır). [cite: 26, 28]
            * "Kısmen Ödenmiş" (çapraz bölünmüş, ödenmiş tutar için yeşil, ödenmemiş tutar için kırmızı). [cite: 27, 28]
        * **İşlemler:**
            * Ödeme yapıldığında.
            * Peşin ödeme yapabilme. [cite: 24]
    * **İstisnai Bütçe:** (Detaylar dokümanda belirtilmemiş, ancak modül olarak var[cite: 6]. Gerekirse API'ler tanımlanmalıdır.)

### 3.6. Raporlama
    * Çeşitli modüller için raporlama API'leri. (Detaylar dokümanda belirtilmemiş, ancak modül olarak var[cite: 6]. Gerekirse API'ler tanımlanmalıdır.)

### 3.7. Belge Yönetimi
    * Belgelerin yüklenmesi, saklanması ve erişimi için API'ler. (Detaylar dokümanda belirtilmemiş, ancak modül olarak var[cite: 6]. Gerekirse API'ler tanımlanmalıdır.)

### 3.8. Ayarlar
    * **Yedekleme ve Geri Yükleme:**
        * Veritabanı yedekleme ve geri yükleme işlemlerini tetikleyecek API endpoint'leri. [cite: 7]
        * API endpoints: `POST /api/settings/backup`, `POST /api/settings/restore`
    * **Dil Seçimi:**
        * Kullanıcının arayüz dilini (Fransızca/İngilizce) seçebilmesi için backend desteği (örn: kullanıcı tercihini saklama, uygun dil kaynaklarını döndürme). [cite: 8]
    * **Bildirimler için:**
        * `UnpaidOwnerNotificationDto.java` (Ödenmemiş sahip bildirimi için)

### 3.9. Bildirimler
    * **Uygulama İçi Bildirimler:**
        * Kullanıcıları mülkün ana sayfasında ödenmemiş mal sahipleri hakkında bilgilendirmek için bir mekanizma (örn: bir API endpoint'i ile bu bilgiyi sağlama). [cite: 21]
        * API endpoint: `GET /api/properties/{propertyId}/notifications/unpaid-owners`

## 4. Non-Fonksiyonel Gereksinimler

* **Performans:** API yanıt süreleri makul olmalı (örn: <500ms çoğu istek için).
* **Güvenlik:** OWASP Top 10 zafiyetlerine karşı koruma sağlanmalıdır.
* **Ölçeklenebilirlik:** Sistem, artan kullanıcı ve veri yükünü kaldırabilecek şekilde tasarlanmalıdır.
* **Bakım Kolaylığı:** Kod, anlaşılır, belgelenmiş ve test edilebilir olmalıdır.
* **API Versiyonlama:** Gelecekteki değişiklikler için API versiyonlama stratejisi düşünülmelidir.

## 5. Veritabanı Etkileşimi

* Tüm veritabanı işlemleri Spring Data JPA kullanılarak gerçekleştirilecektir.
* Karmaşık sorgular için Criteria API veya JPQL kullanılacaktır. Ham SQL kullanımından kaçınılacaktır.
* İlişkisel veritabanı olarak MySQL kullanılacaktır. [cite: 2]

## 6. Hata Yönetimi ve Loglama

* API'ler standart HTTP hata kodları ile anlamlı hata mesajları dönmelidir.
* Önemli sistem olayları ve hatalar loglanmalıdır.

## 7. "Cursor" ve "Rule" Odaklı Hususlar

* **Kurallar (Rules):**
    * Bütçe gelir matrisindeki hücre durumlarının otomatik güncellenmesi (örn: ödenmemiş ayların kırmızıya dönmesi) gibi iş kuralları backend'de tanımlanmalı ve uygulanmalıdır. [cite: 26, 28]
    * Bir mülk seçilmeden ilgili modüllere erişimin engellenmesi gibi yetkilendirme kuralları. [cite: 6, 7]
    * Belirli kullanıcı rollerine göre işlevlerin kısıtlanması.
* **Veri İşleme (Cursor Benzeri Operasyonlar):**
    * Yıl sonu bütçe devir işlemleri gibi toplu veri güncellemeleri. [cite: 17]
    * Raporlama için büyük veri setlerinin işlenmesi ve filtrelenmesi.
    * Sahiplerin veya birimlerin listelenmesi gibi iteratif işlemler.

## X. Yapılanlar (Backend)

### X.1. Veritabanı Katmanı Hazırlıkları:
*   **JPA Entity Sınıfları Oluşturuldu:** Projenin veri modelini temsil eden JPA entity sınıfları (`User`, `Property`, `Owner` vb.) `com.eneskaydi.yazilimprojeyonetimi.Entity` paketi altında `Database_Prd.md`'ye uygun olarak tanımlanmıştır. Bu, backend servislerinin veritabanı ile etkileşime geçmesi için temel yapıyı sağlar.
*   **Spring Data JPA Repository Arayüzleri Oluşturuldu:** Entity'ler için temel CRUD (Create, Read, Update, Delete) operasyonlarını ve özel sorguları içeren repository arayüzleri (`UserRepository`, `PropertyRepository` vb.) `com.eneskaydi.yazilimprojeyonetimi.Repository` paketi altında oluşturulmuştur. Bu arayüzler, servis katmanının veritabanı işlemlerini kolayca gerçekleştirmesini sağlayacaktır.

### X.2. Temel DTO (Data Transfer Object) Sınıfları Oluşturuldu:
*   `com.eneskaydi.yazilimprojeyonetimi.Dto` paketi altında aşağıdaki DTO sınıfları oluşturulmuştur:
    *   **Kullanıcı Yönetimi ve Kimlik Doğrulama için:**
        *   `LoginRequestDto.java` (Kullanıcı girişi isteği için)
        *   `LoginResponseDto.java` (Başarılı giriş yanıtı için)
        *   `UserDto.java` (Genel kullanıcı bilgilerini göstermek için)
        *   `UserProfileUpdateDto.java` (Kullanıcı profili güncelleme isteği için)
    *   **Mülk Yönetimi için:**
        *   `PropertyDto.java` (Mülk bilgilerini listeleme/görüntüleme için)
        *   `PropertyCreateDto.java` (Yeni mülk oluşturma isteği için)
        *   `PropertyUpdateDto.java` (Mülk güncelleme isteği için)
    *   **Birim Türleri Yönetimi için:**
        *   `UnitTypeDto.java` (Birim türü bilgilerini göstermek için)
        *   `UnitTypeCreateDto.java` (Yeni birim türü oluşturma isteği için)
        *   `UnitTypeUpdateDto.java` (Birim türü güncelleme isteği için)
    *   **Sahipler Listesi Yönetimi için:**
        *   `TenantDto.java` (Kiracı detayları için)
        *   `OwnerDto.java` (Sahip/kiracı bilgilerini göstermek için)
        *   `TenantCreateDto.java` (Kiracı oluşturma (OwnerCreateDto içinde) için)
        *   `OwnerCreateDto.java` (Yeni sahip/kiracı oluşturma isteği için)
        *   `TenantUpdateDto.java` (Kiracı güncelleme (OwnerUpdateDto içinde) için)
        *   `OwnerUpdateDto.java` (Sahip/kiracı güncelleme isteği için)
    *   **Bütçe Yönetimi için (Devam Ediyor):**
        *   `BudgetYearDto.java` (Yıllık bütçe bilgilerini göstermek için)
        *   `BudgetYearCreateDto.java` (Yeni yıllık bütçe oluşturma isteği için)
        *   `BudgetTransferRequestDto.java` (Yıllık bütçe transfer isteği için)
        *   `MonthlyBudgetDto.java` (Aylık bütçe bilgilerini göstermek için)
        *   `MonthlyBudgetCreateDto.java` (Yeni aylık bütçe oluşturma isteği için)
        *   `MonthlyBudgetUpdateDto.java` (Aylık bütçe güncelleme isteği için)
    *   **Gelir Yönetimi (Bütçe Altında) için:**
        *   `IncomeRecordDto.java` (Gelir kaydı bilgilerini göstermek için)
        *   `IncomeRecordCreateDto.java` (Yeni gelir kaydı oluşturma isteği için)
        *   `IncomeRecordUpdateDto.java` (Gelir kaydı güncelleme isteği için)
    *   **Belge Yönetimi için:**
        *   `DocumentDto.java` (Belge bilgilerini göstermek için)
        *   `DocumentCreateDto.java` (Yeni belge oluşturma/yükleme isteği için)
        *   `DocumentUpdateDto.java` (Belge bilgilerini güncelleme isteği için)
    *   **Ayarlar için:**
        *   `UserLanguagePreferenceDto.java` (Kullanıcı dil tercihi güncelleme isteği için)
    *   **Bildirimler için:**
        *   `UnpaidOwnerNotificationDto.java` (Ödenmemiş sahip bildirimi için)

### X.3. Servis Arayüzleri Oluşturuldu:
*   `com.eneskaydi.yazilimprojeyonetimi.Service` paketi altında aşağıdaki servis arayüzleri oluşturulmuştur:
    *   `AuthService.java` (Kimlik doğrulama işlemleri için)
    *   `UserService.java` (Kullanıcı yönetimi ve profil işlemleri için)
    *   `PropertyService.java` (Mülk yönetimi işlemleri için - YENİ EKLENDİ)
    *   `UnitTypeService.java` (Birim türleri yönetimi işlemleri için)
    *   `OwnerService.java` (Sahipler ve kiracılar listesi yönetimi işlemleri için)
    *   `BudgetYearService.java` (Yıllık bütçe yönetimi, bütçe transferi ve yıllık bütçe toplamlarını güncelleme işlemleri için (`updateBudgetYearTotals` metodu eklendi).)
    *   `MonthlyBudgetService.java` (Aylık bütçe yönetimi işlemleri için)
    *   `IncomeRecordService.java` (Gelir kayıtları yönetimi, durum takibi işlemleri için - PDF üretimi kaldırıldı.)
    *   `DocumentService.java` (Belge yönetimi işlemleri için)
    *   `SettingsService.java` (Veritabanı yedekleme, geri yükleme ve genel uygulama ayarları (dil, para birimi vb.) işlemleri için - `getSettings` ve `updateSettings` metodları eklendi.)
    *   `NotificationService.java` (Uygulama içi bildirimler, özellikle ödenmemiş sahipler için)

### X.4. Servis Implementasyonları Geliştiriliyor:
*   `com.eneskaydi.yazilimprojeyonetimi.Service` paketi altında aşağıdaki servis implementasyonları geliştirilmeye başlanmıştır (henüz `impl` alt paketi kullanılmamaktadır):
    *   `AuthServiceImpl.java` (Temel kullanıcı/şifre kontrolü ile kimlik doğrulama servisi)
    *   `UserServiceImpl.java` (Mevcut kullanıcıyı geçici olarak ID 1 ile alıp profil ve dil güncelleme işlemleri)
    *   `PropertyServiceImpl.java` (Mülkler için CRUD işlemleri, mevcut kullanıcıyı geçici olarak ID 1 ile alarak ve mülk adı benzersizliği kontrolü ile. `deleteProperty` içinde ilişkili varlıkların silinmesi TODO olarak not edildi. - YENİ EKLENDİ)
    *   `UnitTypeServiceImpl.java` (Birim türleri için CRUD işlemleri, mülk sahipliği ve birim türü adı benzersizliği kontrolü ile. `deleteUnitType` içinde, birim türüne bağlı `Owner` varsa silme işlemi engellendi.)
    *   `OwnerServiceImpl.java` (Sahipler ve kiracılar için CRUD işlemleri, `duplicateOwner` fonksiyonu, mülk sahipliği ve e-posta benzersizliği kontrolleri ile. DTO-Entity alan adı uyuşmazlıkları ve eksik repository metodu kaynaklı linter hataları giderildi.)
    *   `BudgetYearServiceImpl.java` (Yıllık bütçeler için CRUD işlemleri, bütçe transferi ve `updateBudgetYearTotals` implementasyonu (ilgili repository metotları eklendi ve linter hataları giderildi). Bu metot, yıllık bütçenin gelir, gider ve kapanış bakiyesini ilişkili kayıtlara göre hesaplar.)
    *   `MonthlyBudgetServiceImpl.java` (Aylık bütçeler için CRUD işlemleri tamamlandı. `BudgetYearService.updateBudgetYearTotals` çağrılarak yıllık bütçe güncellemeleri entegre edildi. Linter hataları ve DTO alanlarındaki tutarsızlıklar giderildi.)
    *   `IncomeRecordServiceImpl.java` (Gelir kayıtları için CRUD işlemleri, durum güncelleme mantığı (`updateStatusBasedOnPayment` metodu PRD'ye uygun hale getirildi). PDF üretimi ile ilgili kısımlar kaldırıldı. `BudgetYearService.updateBudgetYearTotals` çağrılarak yıllık bütçe güncellemeleri entegre edildi. Entity ve DTO'lardaki alanlar PRD ve servis ihtiyaçlarına göre güncellendi, linter hataları giderildi.)
    *   `DocumentServiceImpl.java` (Belge yükleme, indirme, listeleme, meta veri güncelleme ve silme işlemleri. Dosya sistemi üzerinde temel depolama mantığı içerir. Entity ve DTO'lardaki eksik alanlar tamamlanarak linter hataları giderildi.)
    *   `SettingsServiceImpl.java` (Veritabanı yedekleme ve geri yükleme işlemleri için yer tutucu implementasyon oluşturuldu. `application.properties` üzerinden veritabanı bilgilerini ve yedekleme yolunu okur. Gerçek komut çalıştırma (`mysqldump`, `mysql`) platform bağımlılığı ve güvenlik nedeniyle simüle edilmektedir.)
    *   `NotificationServiceImpl.java` (Uygulama içi bildirimler servisi. Temel yapısı oluşturuldu, ödenmemiş sahip bildirimleri için detaylı iş mantığı TODO olarak işaretlendi.)

### X.5. Controller Katmanı Geliştiriliyor:
*   `com.eneskaydi.yazilimprojeyonetimi.Controller` paketi altında aşağıdaki controller sınıfları geliştirilmeye başlanmıştır:
    *   `AuthController.java` (Kullanıcı girişi `/api/auth/login` endpoint'i oluşturuldu. `LoginRequestDto` için `@Valid` aktif edildi, `jakarta.validation.Valid` importu eklendi. `LoginRequestDto`'ya `@NotBlank` ve `@Size` validasyonları eklendi.)
    *   `UserController.java` (Kullanıcı profil yönetimi `/api/users/me` (GET) ve `/api/users/me` (PUT) endpoint'leri oluşturuldu. `UserProfileUpdateDto` için `@Valid` aktif edildi. `UserProfileUpdateDto`'ya `@NotBlank` (language için) validasyonu eklendi.)
    *   `PropertyController.java` (Mülk yönetimi için CRUD endpoint'leri (`/api/properties`) oluşturuldu. `PropertyCreateDto` ve `PropertyUpdateDto` için `@Valid` aktif edildi. Bu DTO'lara `@NotBlank` ve `@Size` gibi validasyonlar eklendi.)
    *   `UnitTypeController.java` (Belirli bir mülke ait birim türleri için CRUD endpoint'leri (`/api/properties/{propertyId}/unit-types`) oluşturuldu. `UnitTypeCreateDto` ve `UnitTypeUpdateDto` için `@Valid` aktif edildi. Bu DTO'lara `@NotBlank`, `@NotNull`, `@DecimalMin`, `@Size` validasyonları eklendi.)
    *   `OwnerController.java` (Belirli bir mülke ait sahipler/kiracılar için CRUD ve çoğaltma endpoint'leri (`/api/properties/{propertyId}/owners`) oluşturuldu. `OwnerCreateDto`, `TenantCreateDto`, `OwnerUpdateDto`, `TenantUpdateDto` için `@Valid` aktif edildi. Bu DTO'lara `@NotBlank`, `@Email`, `@Size`, `@NotNull` validasyonları ve iç içe DTO için `@Valid` eklendi. `phone` alanı `phoneNumber` olarak düzeltildi.)
    *   `BudgetYearController.java` (Belirli bir mülke ait yıllık bütçeler için temel CRUD (`POST`, `GET` list, `GET` byId) ve `/transfer` endpoint'leri (`/api/properties/{propertyId}/budget-years`) oluşturuldu. `BudgetYearCreateDto` ve `BudgetTransferRequestDto` için `@Valid` aktif edildi, `jakarta.validation.Valid` importu eklendi. `BudgetYearCreateDto` güncellenerek `annualBudgetAmount` (validasyonlu) eklendi, `notes` -> `description` (validasyonlu) yapıldı. `BudgetTransferRequestDto` için yorumlar ve `@Min` değeri güncellendi.)
    *   `MonthlyBudgetController.java` (Belirli bir yıllık bütçeye ait aylık bütçeler için CRUD endpoint'leri (`/api/properties/{propertyId}/budget-years/{budgetYearId}/monthly-budgets`) oluşturuldu. `MonthlyBudgetCreateDto` ve `MonthlyBudgetUpdateDto` için `@Valid` aktif edildi, `jakarta.validation.Valid` importu eklendi. `MonthlyBudgetCreateDto` güncellenerek `budgetAmount` (validasyonlu) eklendi, `income` ve `expense` kaldırıldı. `MonthlyBudgetUpdateDto` güncellenerek `budgetAmount` (validasyonlu) eklendi, `income` ve `expense` kaldırıldı.)
    *   `IncomeRecordController.java` (Belirli bir yıllık bütçeye ait gelir kayıtları için, sahip bazlı ve bütçe yılı bazlı CRUD endpoint'leri (`.../owners/{ownerId}/income-records` ve `.../income-records`) oluşturuldu. `IncomeRecordCreateDto` ve `IncomeRecordUpdateDto` için `@Valid` aktif edildi, `jakarta.validation.Valid` importu eklendi. `IncomeRecordCreateDto` için `@DecimalMin`, `@FutureOrPresent`, `@Size` eklendi. `IncomeRecordUpdateDto` için `@DecimalMin`, `@FutureOrPresent`, `@Size`, `@NotNull` (status için) eklendi ve alanların opsiyonel olduğu belirtildi.)
    *   `DocumentController.java` (Belirli bir mülke ait belgeler için yükleme, indirme, listeleme, meta veri güncelleme ve silme endpoint'leri (`/api/properties/{propertyId}/documents`) oluşturuldu. `DocumentCreateDto` ve `DocumentUpdateDto` için `@Valid` aktif edildi, `jakarta.validation.Valid` importu eklendi. `DocumentCreateDto`'dan `propertyId` ve `ownerId` kaldırıldı, `fileName` ve `description` için `@Size` eklendi. `DocumentUpdateDto` için `fileName` ve `description` alanlarına `@Size` eklendi ve opsiyonel oldukları belirtildi.)
    *   `SettingsController.java` (Genel ayarları getirme ve güncelleme için `/api/settings` (GET, PUT) endpoint'leri oluşturuldu. `SettingsDto` için `@Valid` aktif edildi, `jakarta.validation.Valid` importu eklendi. `SettingsDto`'daki `language`, `currency` ve `emailNotificationsEnabled` alanlarına uygun validasyonlar (`@NotBlank`, `@Size`, `@NotNull`) eklendi.)
    *   `NotificationController.java` (Belirli bir mülke ait ödenmemiş sahip bildirimlerini getirmek için `/api/properties/{propertyId}/notifications/unpaid-owners` (GET) endpoint'i oluşturuldu.)

*Not: Controller katmanları henüz implemente edilmemiştir. Bu adımlar "Yapılanlar" listesine eklendikçe güncellenecektir.*