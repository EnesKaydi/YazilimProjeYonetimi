package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Başarılı kullanıcı girişi (login) sonrası döndürülecek yanıt için DTO.
// Genellikle bir JWT (JSON Web Token) ve temel kullanıcı bilgilerini içerir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken; // Erişim token'ı (JWT)
    private String tokenType = "Bearer"; // Token türü, genellikle "Bearer"
    private Long userId; // Kullanıcının ID'si
    private String username; // Kullanıcının adı
    private String email; // Kullanıcının e-postası
    private String role; // Kullanıcının rolü

    // Gerekirse token geçerlilik süresi gibi ek bilgiler de eklenebilir.

    public LoginResponseDto(String accessToken, Long userId, String username, String email, String role) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}