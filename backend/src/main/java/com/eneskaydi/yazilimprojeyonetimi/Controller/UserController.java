package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.UserDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UserProfileUpdateDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.PasswordChangeRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kullanıcı profili ve kullanıcı ile ilgili diğer işlemleri yöneten REST
 * controller sınıfı.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Mevcut (giriş yapmış) kullanıcının profil bilgilerini getirir.
     * Kimlik doğrulama mekanizması (örn: JWT token) üzerinden kullanıcı bilgisi
     * alınır.
     *
     * @return HTTP 200 OK ile UserDto içerisindeki kullanıcı bilgileri.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserProfile() {
        UserDto userDto = userService.getCurrentUserProfile();
        return ResponseEntity.ok(userDto);
    }

    /**
     * Mevcut (giriş yapmış) kullanıcının profil bilgilerini günceller.
     * Adres, telefon gibi bilgiler bu endpoint üzerinden güncellenebilir.
     * UserProfileUpdateDto'nun User entity'sindeki yeni alanları (address, phoneNumber)
     * destekleyecek şekilde güncellenmesi gerekebilir.
     *
     * @param userProfileUpdateDto Güncellenecek profil bilgilerini içeren DTO.
     * @return HTTP 200 OK ile güncellenmiş UserDto.
     */
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUserProfile(
            @Valid @RequestBody UserProfileUpdateDto userProfileUpdateDto) {
        // TODO: UserProfileUpdateDto'nun User entity'sine eklenen address ve phoneNumber alanlarını içermesi ve
        // UserServiceImpl.updateCurrentUserProfile metodunun bu alanları işlemesi gerekir.
        UserDto updatedUserDto = userService.updateCurrentUserProfile(userProfileUpdateDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    /**
     * Mevcut (giriş yapmış) kullanıcının şifresini değiştirir.
     *
     * @param requestDto Şifre değişikliği bilgilerini içeren DTO.
     * @return Başarılı olursa HTTP 200 OK (String mesaj ile), aksi takdirde hata mesajı.
     */
    @PostMapping("/me/change-password")
    public ResponseEntity<String> changeCurrentUserPassword(@Valid @RequestBody PasswordChangeRequestDto requestDto) {
        userService.changePassword(requestDto);
        return ResponseEntity.ok("Şifre başarıyla güncellendi.");
    }
}