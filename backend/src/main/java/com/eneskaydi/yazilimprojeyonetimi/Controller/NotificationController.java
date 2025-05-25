package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UnpaidOwnerNotificationDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Uygulama içi bildirimleri yöneten REST controller.
 * Özellikle ödenmemiş mülk sahipleri gibi uyarıları sağlar.
 */
@RestController
@RequestMapping("/api/properties/{propertyId}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Belirli bir mülk için ödenmemiş borcu olan sahiplerin bildirimlerini getirir.
     *
     * @param propertyId Bildirimlerin alınacağı mülkün ID'si.
     * @return HTTP 200 OK ile ödenmemiş sahiplerin bilgilerini içeren DTO listesi.
     */
    @GetMapping("/unpaid-owners")
    public ResponseEntity<List<UnpaidOwnerNotificationDto>> getUnpaidOwnerNotifications(@PathVariable Long propertyId) {
        List<UnpaidOwnerNotificationDto> notifications = notificationService.getUnpaidOwnerNotifications(propertyId);
        return ResponseEntity.ok(notifications);
    }

}