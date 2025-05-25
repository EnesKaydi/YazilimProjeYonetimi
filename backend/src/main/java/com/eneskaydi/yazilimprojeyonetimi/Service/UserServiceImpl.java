package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UserDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UserProfileUpdateDto;
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

        if (userProfileUpdateDto.getEmail() != null && !userProfileUpdateDto.getEmail().isEmpty()) {
            if (userRepository.existsByEmailAndIdNot(userProfileUpdateDto.getEmail(), user.getId())) {
                throw new RuntimeException("Bu e-posta adresi zaten kullanımda: " + userProfileUpdateDto.getEmail());
            }
            user.setEmail(userProfileUpdateDto.getEmail());
        }

        if (userProfileUpdateDto.getPassword() != null && !userProfileUpdateDto.getPassword().isEmpty()) {
            user.setPassword(userProfileUpdateDto.getPassword()); // Geçici: Düz metin şifre ataması
        }

        User updatedUser = userRepository.save(user);
        return mapToUserDto(updatedUser);
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