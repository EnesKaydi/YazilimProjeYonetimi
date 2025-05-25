package com.eneskaydi.yazilimprojeyonetimi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

// Sisteme yüklenen belgeleri temsil eden entity.
// Her belge, bir mülke ve isteğe bağlı olarak bir mülk sahibine ait olabilir.
@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Belge kaydının benzersiz kimliği

    @Column(name = "file_name", nullable = false)
    private String fileName; // Kullanıcıya gösterilen orijinal dosya adı

    // Depolama sisteminde kullanılan, benzersiz olarak üretilmiş dosya adı.
    // Bu, olası dosya adı çakışmalarını önler.
    @Column(name = "stored_file_name", nullable = false, unique = true)
    private String storedFileName;

    @Column(name = "file_type", nullable = false)
    private String fileType; // Dosyanın MIME türü (örn: "application/pdf", "image/jpeg")

    // Dosyanın boyutu byte cinsinden.
    @Column(name = "size", nullable = false)
    private Long size;

    // Belge hakkında kullanıcı tarafından girilen açıklama.
    @Lob // Uzun metinler için
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate; // Belgenin sisteme yüklendiği tarih ve saat

    // Bu belgenin zorunlu olarak ilişkili olduğu mülk.
    // Bir belge mülk olmadan var olamaz.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // Bu belgenin isteğe bağlı olarak ilişkili olduğu mülk sahibi.
    // Bir belge, doğrudan bir mülk sahibine de ait olabilir (örn: kira sözleşmesi).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    // Entity veritabanına kaydedilmeden hemen önce bu metot çalışır.
    // Yükleme tarihini otomatik olarak ayarlar.
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
}