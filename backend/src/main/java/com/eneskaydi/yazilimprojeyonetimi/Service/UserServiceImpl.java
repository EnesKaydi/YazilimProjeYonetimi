package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UserDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UserProfileUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PasswordChangeRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Kullanıcı yönetimi ve profil işlemlerinin implementasyonu
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder; // Security aktif edildiğinde
    // eklenecek

    // Geçici helper metot - Security entegrasyonu sonrası bu direkt
    // SecurityContextHolder'dan gelecek
    private User getAuthenticatedUser() {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // if (authentication == null || !authentication.isAuthenticated() ||
        // "anonymousUser".equals(authentication.getPrincipal())) {
        // throw new RuntimeException("Kullanıcı girişi yapılmamış veya kimlik doğrulama
        // bilgisi bulunamadı.");
        // }
        // String username = authentication.getName();
        // return userRepository.findByUsername(username)
        // .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " +
        // username));

        // Güvenlik entegrasyonu yapılana kadar varsayılan bir kullanıcıyı döndür (örn:
        // ID'si 1 olan)
        // Bu KESİNLİKLE geçicidir ve canlıda kullanılmamalıdır!
        // Test ve geliştirme aşamasında kolaylık sağlamak için eklendi.
        // Gerçek implementasyonda yukarıdaki SecurityContextHolder kısmı aktif
        // edilecektir.
        return userRepository.findById(1L) // Varsayılan kullanıcı ID'si, PRD'de yönetici hesabı tanımlanmıştı.
                .orElseThrow(() -> new RuntimeException(
                        "Varsayılan test kullanıcısı (ID: 1) bulunamadı. Lütfen veritabanını kontrol edin veya bir kullanıcı oluşturun."));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUserProfile() {
        User user = getAuthenticatedUser();
        return mapToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateCurrentUserProfile(UserProfileUpdateDto userProfileUpdateDto) {
        User user = getAuthenticatedUser();

        // E-posta güncellemesi
        if (userProfileUpdateDto.getEmail() != null && !userProfileUpdateDto.getEmail().isEmpty()) {
            if (!userProfileUpdateDto.getEmail().equals(user.getEmail())) { // Sadece e-posta değişmişse kontrol et
                if (userRepository.existsByEmailAndIdNot(userProfileUpdateDto.getEmail(), user.getId())) {
                    throw new RuntimeException("Bu e-posta adresi zaten kullanımda: " + userProfileUpdateDto.getEmail());
                }
                user.setEmail(userProfileUpdateDto.getEmail());
            }
        }

        // Şifre güncellemesi (Dikkat: Bu genellikle ayrı bir işlem olmalı)
        // Eğer şifre alanı DTO'da dolu gelirse güncelle.
        if (userProfileUpdateDto.getPassword() != null && !userProfileUpdateDto.getPassword().isEmpty()) {
            // Burada da encode işlemi yapılmalı, şimdilik düz metin.
            user.setPassword(userProfileUpdateDto.getPassword());
        }

        // Telefon numarası güncellemesi
        if (userProfileUpdateDto.getPhoneNumber() != null) { // Boş string de gelebilir, kontrol edilebilir.
            user.setPhoneNumber(userProfileUpdateDto.getPhoneNumber());
        }

        // Adres güncellemesi
        if (userProfileUpdateDto.getAddress() != null) { // Boş string de gelebilir, kontrol edilebilir.
            user.setAddress(userProfileUpdateDto.getAddress());
        }

        User updatedUser = userRepository.save(user);
        return mapToUserDto(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeRequestDto requestDto) {
        // Yeni şifre ve teyit şifresinin eşleşip eşleşmediğini kontrol et.
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmNewPassword())) {
            throw new RuntimeException("Yeni şifre ve yeni şifre tekrarı eşleşmiyor.");
        }

        // Kullanıcıyı ID ile veritabanından bul.
        User user = getAuthenticatedUser();

        // Mevcut şifrenin doğruluğunu kontrol et (Gerçek uygulamada passwordEncoder.matches kullanılmalı).
        // Şimdilik düz metin karşılaştırması yapılıyor, GÜVENLİK AÇIĞI!
        if (!user.getPassword().equals(requestDto.getCurrentPassword())) {
            throw new RuntimeException("Mevcut şifre yanlış.");
        }

        // Yeni şifreyi ayarla (Gerçek uygulamada passwordEncoder.encode kullanılmalı).
        user.setPassword(requestDto.getNewPassword());

        // Kullanıcıyı güncelle.
        userRepository.save(user);
    }

    // User entity'sini UserDto'ya mapleyen yardımcı metot
    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }
}