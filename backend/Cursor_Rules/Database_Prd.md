# Database - Ürün Gereksinimleri Dokümanı (PRD)

## 1. Giriş

Bu doküman, Ev Sahipleri Derneği Yönetim Sistemi projesinin veritabanı yapısını ve gereksinimlerini tanımlamaktadır. Veritabanı olarak MySQL kullanılacak [cite: 2] ve veri erişim katmanı olarak JPA (Java Persistence API) ile ORM (Object-Relational Mapping) prensipleri benimsenecektir. Ham SQL kullanımından mümkün olduğunca kaçınılacaktır.

## 2. Genel Amaçlar

* Sistemin ihtiyaç duyduğu tüm verileri güvenli, tutarlı ve verimli bir şekilde saklamak.
* JPA entity'leri aracılığıyla kolay yönetilebilir bir şema sağlamak.
* Veri bütünlüğünü sağlamak için gerekli kısıtlamaları tanımlamak.
* Gelecekteki genişlemelere uygun bir yapı oluşturmak.

## 3. Veri Modeli (JPA Entity'leri ve İlişkileri)

Aşağıda temel entity'ler ve aralarındaki ilişkiler özetlenmiştir. Detaylı alanlar ve veri tipleri geliştirme aşamasında JPA entity sınıflarında tanımlanacaktır.

### [YAPILDI] 3.1. `User` (Kullanıcı)
    * `id` (Primary Key)
    * `username` (Benzersiz)
    * `password` (Hashlenmiş)
    * `email` (Benzersiz)
    * `role` (örn: ADMIN, USER)
    * `profile_settings` (JSON veya ayrı tablo, örn: dil tercihi [cite: 8])

### [YAPILDI] 3.2. `Property` (Mülk) [cite: 5, 9]
    * `id` (Primary Key)
    * `name`
    * `address`
    * `property_title` (Tapu Bilgisi)
    * `association_name` (Dernek Adı)
    * `city` (Şehir - dünya şehirleri listesinden [cite: 9])
    * `currency` (Para Birimi - dünya para birimleri listesinden [cite: 10, 11])
    * `user_id` (Foreign Key -> User: Mülkü oluşturan veya yöneten kullanıcı)
    * **İlişkiler:**
        * OneToMany -> `UnitType`
        * OneToMany -> `Owner`
        * OneToMany -> `BudgetYear`
        * OneToMany -> `Document` (Belge Yönetimi için)

### [YAPILDI] 3.3. `UnitType` (Birim Türü) [cite: 11]
    * `id` (Primary Key)
    * `name` (örn: Büyük Daire, Küçük Daire, Garaj)
    * `monthly_fee` (Aylık Ücret)
    * `description`
    * `property_id` (Foreign Key -> Property)
    * **İlişkiler:**
        * ManyToOne -> `Property`
        * OneToMany -> `Owner` (Bir birim türüne sahip birden fazla sahip olabilir)

### [YAPILDI] 3.4. `Owner` (Sahip) [cite: 12]
    * `id` (Primary Key)
    * `full_name`
    * `address`
    * `phone`
    * `email`
    * `bank_account_number`
    * `ownership_title_number` (Sahiplik Unvan Numarası)
    * `is_tenant_occupied` (Kiralanmış mı? Boolean) [cite: 13]
    * `display_as` (Mal Sahibi / Kiracı Enum) [cite: 14]
    * `property_id` (Foreign Key -> Property)
    * `unit_type_id` (Foreign Key -> UnitType, Sahip olduğu birim türü)
    * **İlişkiler:**
        * ManyToOne -> `Property`
        * ManyToOne -> `UnitType`
        * OneToOne -> `Tenant` (Eğer `is_tenant_occupied` true ise, isteğe bağlı ilişki)
        * OneToMany -> `IncomeRecord` (Sahibin gelir kayıtları)

### [YAPILDI] 3.5. `Tenant` (Kiracı) [cite: 13]
    * `id` (Primary Key)
    * `full_name`
    * `address`
    * `phone`
    * `email`
    * `bank_account_number`
    * `owner_id` (Foreign Key -> Owner, bu kiracının ilişkili olduğu sahip)
    * **İlişkiler:**
        * OneToOne -> `Owner`

### [YAPILDI] 3.6. `BudgetYear` (Bütçe Yılı) [cite: 16]
    * `id` (Primary Key)
    * `year` (örn: 2025, 2026 - Benzersiz olmalı per property)
    * `opening_balance` (Açılış Bakiyesi - bir önceki yıldan devir [cite: 17])
    * `closing_balance` (Kapanış Bakiyesi)
    * `property_id` (Foreign Key -> Property)
    * **İlişkiler:**
        * ManyToOne -> `Property`
        * OneToMany -> `MonthlyBudget` (Yılın aylık bütçeleri)
        * OneToMany -> `IncomeRecord` (Yıl içindeki tüm gelir kayıtları)

### [YAPILDI] 3.7. `MonthlyBudget` (Aylık Bütçe)
    * `id` (Primary Key)
    * `month` (örn: 1-12 arası)
    * `total_income_expected` (Beklenen Toplam Gelir)
    * `total_expenses_actual` (Gerçekleşen Toplam Gider/Sonuç) [cite: 19, 20]
    * `budget_year_id` (Foreign Key -> BudgetYear)
    * **İlişkiler:**
        * ManyToOne -> `BudgetYear`

### [YAPILDI] 3.8. `IncomeRecord` (Gelir Kaydı - Gelir Matrisi için) [cite: 22, 23, 24, 25, 26, 27, 28]
    * `id` (Primary Key)
    * `owner_id` (Foreign Key -> Owner)
    * `budget_year_id` (Foreign Key -> BudgetYear)
    * `month` (Ödemenin ait olduğu ay, örn: 1-12)
    * `due_date` (Beklenen ödeme tarihi / Vade tarihi)
    * `expected_amount` (Ödenmesi Beklenen Tutar)
    * `paid_amount` (Ödenen Tutar)
    * `payment_date` (Ödeme Tarihi)
    * `status` (Enum: HENUZ_GELMEDI, ODENDI, ODENMEDI, KISMEN_ODENMIS)
    * `notes` (Gelir kaydı ile ilgili notlar)
    * `is_advance_payment` (Peşin Ödeme mi?) [cite: 24]
    * **İlişkiler:**
        * ManyToOne -> `Owner`
        * ManyToOne -> `BudgetYear`

### [YAPILDI] 3.9. `Document` (Belge) [cite: 6]
    * `id` (Primary Key)
    * `file_name` (Kullanıcıya gösterilen orijinal dosya adı)
    * `stored_file_name` (Depolama sistemindeki benzersiz dosya adı)
    * `file_type` (Dosyanın MIME türü)
    * `size` (Dosyanın boyutu byte cinsinden)
    * `description` (Belge açıklaması)
    * `upload_date` (Yükleme tarihi ve saati)
    * `property_id` (Foreign Key -> Property, zorunlu)
    * `owner_id` (Foreign Key -> Owner, isteğe bağlı, sahibe özel belgeler için)
    * **İlişkiler:**
        * ManyToOne -> `Property`
        * ManyToOne -> `Owner` (opsiyonel)

## 4. Veri Bütünlüğü ve Kısıtlamalar

* **Primary Keys:** Tüm tablolarda `id` (genellikle auto-increment Long) kullanılacaktır.
* **Foreign Keys:** İlişkilerde uygun foreign key kısıtlamaları ve `ON DELETE`, `ON UPDATE` davranışları (örn: `CASCADE`, `SET NULL`, `RESTRICT`) JPA üzerinden tanımlanacaktır.
* **Benzersiz Kısıtlamalar (Unique Constraints):** `User.username`, `User.email` gibi alanlarda.
* **Null Olmama Kısıtlamaları (Not Null Constraints):** Gerekli alanlar için (örn: `Property.name`, `Owner.full_name`).
* **Enum Tipleri:** `IncomeRecord.status`, `Owner.display_as` gibi durumları belirten alanlar için enum kullanılacaktır.

## 5. Veri Yedekleme ve Geri Yükleme [cite: 7]

* Veritabanı için düzenli yedekleme mekanizmaları planlanmalıdır.
* Geri yükleme prosedürleri tanımlanmalı ve test edilmelidir. Bu işlemler backend API'leri aracılığıyla tetiklenebilir olmalıdır.

## 6. Veri Geçişi (Data Migration)

* Gelecekte şema değişiklikleri olması durumunda Liquibase veya Flyway gibi araçlarla veri geçişi yönetimi düşünülmelidir.

## 7. "Cursor" ve "Rule" Odaklı Hususlar (Veritabanı Perspektifi)

* **Kurallar (Rules - Veritabanı Seviyesinde):**
    * Veri bütünlüğünü sağlamak için CHECK kısıtlamaları (destekleniyorsa) veya JPA @PrePersist/@PreUpdate lifecycle callback'leri ile entity seviyesinde doğrulamalar.
    * Örneğin, bir `IncomeRecord` için `amount_paid` değerinin `amount_due` değerinden büyük olmaması gibi.
* **Veri Erişimi ve İşleme (Cursor Benzeri Operasyonlar - JPA ile):**
    * JPA ve Spring Data JPA, büyük veri setlerini verimli bir şekilde işlemek için `Pageable` ve `Slice` gibi mekanizmalar sunar. Bu, "cursor" benzeri davranışlar için kullanılabilir (örn: sahiplerin sayfalı listelenmesi).
    * Toplu güncellemeler (örn: yıl sonu bütçe devri) için JPQL `UPDATE` sorguları veya batch processing (toplu işleme) yetenekleri kullanılabilir. [cite: 17]
    * Raporlama için karmaşık sorgular gerektiğinde, veritabanı view'ları (görünümleri) veya optimize edilmiş JPQL/Criteria API sorguları kullanılabilir.

## 8. İndeksleme

* Sık sorgulanan alanlar (örn: `Property.name`, `Owner.email`, Foreign Key alanları) için performans artışı amacıyla indeksler oluşturulmalıdır. JPA'de `@Index` anotasyonu ile tanımlanabilir.

## Yapılanlar (Database)

### 1. Temel Veri Modeli (JPA Entity'leri) Oluşturuldu:
*   `com.eneskaydi.yazilimprojeyonetimi.Entity` paketi altında aşağıdaki entity sınıfları ve ilgili enum'lar PRD'ye uygun olarak oluşturulmuştur:
    *   `User.java`
    *   `Property.java`
    *   `UnitType.java`
    *   `Owner.java` (ve `DisplayAsType.java` enum'u)
    *   `Tenant.java`
    *   `BudgetYear.java`
    *   `MonthlyBudget.java`
    *   `IncomeRecord.java` (ve `IncomeStatusType.java` enum'u). `IncomeRecord` entity'sindeki alanlar (`expectedAmount`, `dueDate`, `notes` eklendi, `amountDue` ve `currency` kaldırıldı) ve `IncomeStatusType` enum değerleri (Türkçeleştirildi ve PRD ile uyumlu hale getirildi) güncellenmiştir.
    *   `Document.java`. `Document` entity'sindeki alanlar (`storedFileName`, `size`, `description` eklendi, `filePath` kaldırıldı, `property_id` zorunlu hale getirildi) güncellenmiştir.
*   Entity başlıkları "[YAPILDI]" olarak işaretlenmiştir.

### 2. Spring Data JPA Repository Arayüzleri Oluşturuldu:
*   `com.eneskaydi.yazilimprojeyonetimi.Repository` paketi altında aşağıdaki repository arayüzleri oluşturulmuştur:
    *   `UserRepository.java`
    *   `PropertyRepository.java`
    *   `UnitTypeRepository.java`
    *   `OwnerRepository.java`
    *   `BudgetYearRepository.java`
    *   `MonthlyBudgetRepository.java`
    *   `IncomeRecordRepository.java`
    *   `DocumentRepository.java`
    *   (`TenantRepository` için şimdilik ayrı bir arayüz oluşturulmamış, `OwnerRepository` üzerinden yönetilmesi düşünülmektedir.)