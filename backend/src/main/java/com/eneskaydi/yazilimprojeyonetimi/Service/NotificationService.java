package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UnpaidOwnerNotificationDto;

import java.util.List;

/**
 * Bildirimlerle ilgili işlemleri yöneten servis arayüzü.
 * Özellikle ödenmemiş sahip bildirimleri gibi mülk bazlı uyarıları sağlar.
 */
public interface NotificationService {

    /**
     * Belirli bir mülk için ödenmemiş borcu olan sahiplerin bildirimlerini alır.
     * Bu metod, finansal durumu kritik olan ve ödeme yapmamış mülk sahiplerini
     * tespit etmek için kullanılır.
     *
     * @param propertyId Bildirimlerin alınacağı mülkün ID'si.
     * @return Ödenmemiş sahiplerin bilgilerini içeren DTO listesi.
     */
    List<UnpaidOwnerNotificationDto> getUnpaidOwnerNotifications(Long propertyId);
} 