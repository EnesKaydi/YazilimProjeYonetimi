package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Kullanıcı bilgilerini (hassas veriler hariç) göstermek için DTO.
// Örneğin, GET /api/users/me yanıtında kullanılabilir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username; // Bu alan User entity'sindeki username'i (Ad Soyad) temsil eder.
    private String email;
    private String role;
    private String phoneNumber;
    private String address;

    // Entity'den DTO'ya dönüşüm için bir yardımcı metot veya MapStruct gibi bir
    // kütüphane kullanılabilir.
    // Örnek:
    // public static UserDto fromEntity(User user) {
    // return new UserDto(user.getId(), user.getUsername(), user.getEmail(),
    // user.getRole(), user.getProfileSettings());
    // }
}