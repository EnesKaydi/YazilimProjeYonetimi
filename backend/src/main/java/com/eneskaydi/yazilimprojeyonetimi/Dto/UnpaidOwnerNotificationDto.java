package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Ödenmemiş borcu olan mülk sahibi bildirim bilgilerini temsil eden DTO sınıfı
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnpaidOwnerNotificationDto {

    // Mülk sahibinin benzersiz kimliği
    private Long ownerId;

    // Mülk sahibinin tam adı
    private String ownerFullName;

    // Mülk sahibine ait birimin tanımlayıcısı (örn: Daire No: 5, Blok A - Dükkan 2)
    private String unitIdentifier; // Bu alan, Owner ve UnitType entity'lerinden birleştirilerek oluşturulabilir.

    // Mülk sahibinin toplam ödenmemiş borç miktarı
    private BigDecimal totalUnpaidAmount;

    // Para birimi (Mülk ayarlarından alınacak)
    private String currency;
}