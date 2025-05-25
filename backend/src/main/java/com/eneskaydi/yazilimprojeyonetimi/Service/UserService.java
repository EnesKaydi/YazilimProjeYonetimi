package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UserDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UserProfileUpdateDto;
import org.springframework.transaction.annotation.Transactional;

// Kullanıcı yönetimi ve profil işlemlerinden sorumlu servis arayüzü.
public interface UserService {

    /**
     * Kimliği doğrulanmış mevcut kullanıcının profil bilgilerini getirir.
     * Bu metot, Spring Security context'inden mevcut kullanıcıyı çözümleyecektir.
     *
     * @return Mevcut kullanıcının bilgilerini içeren UserDto.
     * @throws RuntimeException Kullanıcı girişi yapılmamışsa veya kullanıcı
     *                          bulunamazsa.
     */
    UserDto getCurrentUserProfile();

    /**
     * Kimliği doğrulanmış mevcut kullanıcının profil bilgilerini (şifre, dil
     * tercihi vb.) günceller.
     * Bu metot, Spring Security context'inden mevcut kullanıcıyı çözümleyecektir.
     *
     * @param userProfileUpdateDto Güncellenecek profil bilgilerini içeren DTO.
     * @return Güncellenmiş kullanıcı bilgilerini içeren UserDto.
     * @throws RuntimeException Kullanıcı girişi yapılmamışsa veya kullanıcı
     *                          bulunamazsa.
     */
    UserDto updateCurrentUserProfile(UserProfileUpdateDto userProfileUpdateDto);

    // Rol bazlı yetkilendirme ve diğer kullanıcı yönetimi fonksiyonları için örnek
    // metot imzaları:
    // UserDto getUserById(Long userId); // Admin yetkisi gerekebilir
    // List<UserDto> getAllUsers(); // Admin yetkisi gerekebilir
    // void assignRoleToUser(Long userId, String role); // Admin yetkisi gerekebilir
    // void deleteUser(Long userId); // Admin yetkisi gerekebilir
}