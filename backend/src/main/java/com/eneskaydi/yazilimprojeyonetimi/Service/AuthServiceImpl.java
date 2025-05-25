package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.LoginRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.LoginResponseDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.RegisterRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UserDto;
import com.eneskaydi.yazilimprojeyonetimi.Entity.User;
import com.eneskaydi.yazilimprojeyonetimi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.AuthenticationManager; // Aktif edildiğinde eklenecek
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Aktif edildiğinde eklenecek
// import org.springframework.security.core.Authentication; // Aktif edildiğinde eklenecek
// import org.springframework.security.core.context.SecurityContextHolder; // Aktif edildiğinde eklenecek
// import org.springframework.security.crypto.password.PasswordEncoder; // Aktif edildiğinde eklenecek
import org.springframework.stereotype.Service;

// Kimlik doğrulama servisinin implementasyonu
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder; // JWT ve Security aktif
    // edildiğinde kullanılacak
    // private final AuthenticationManager authenticationManager; // JWT ve Security
    // aktif edildiğinde kullanılacak
    // private final JwtTokenProvider jwtTokenProvider; // Oluşturulacak JWT helper
    // sınıfı

    // User entity'sini UserDto'ya mapleyen yardımcı metot
    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername()); // User entity'sindeki username (Ad Soyad)
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        // userDto.setLanguagePreference(user.getLanguagePreference()); // Kaldırıldı
        return userDto;
    }

    /**
     * Kullanıcı adı/e-posta ve şifre ile kullanıcı girişi yapar.
     * JWT ve tam güvenlik entegrasyonu daha sonra eklenecektir.
     *
     * @param loginRequestDto Kullanıcı giriş bilgilerini içeren DTO.
     * @return Başarılı giriş sonrası (şimdilik dummy) JWT ve kullanıcı bilgilerini
     *         içeren DTO.
     * @throws RuntimeException Kullanıcı bulunamazsa veya şifre eşleşmezse.
     */
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // Kullanıcıyı e-postaya göre bul
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException(
                        "Kullanıcı bulunamadı veya e-posta hatalı: " + loginRequestDto.getEmail()));

        // Şifre kontrolü (şimdilik düz metin karşılaştırması, security aktif edilince PasswordEncoder kullanılacak)
        if (!user.getPassword().equals(loginRequestDto.getPassword())) { // Geçici düz metin kontrolü
            throw new RuntimeException("Geçersiz şifre");
        }

        // JWT ve SecurityContext işlemleri daha sonra eklenecek
        // Authentication authentication = authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), // E-posta kullanılacak
        // loginRequestDto.getPassword()));
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        // String token = jwtTokenProvider.generateToken(user.getEmail()); // Token e-posta ile üretilebilir veya username (AdSoyad) ile

        // Şimdilik dummy token ve bilgilerle yanıt oluştur
        String dummyToken = "dummy-jwt-token-" + user.getId();
        return new LoginResponseDto(
                dummyToken,
                "Bearer",
                user.getId(),
                user.getUsername(), // Ad Soyad
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public UserDto register(RegisterRequestDto registerRequestDto) {
        // Kullanıcı adı (Ad Soyad) zaten var mı diye kontrol et
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı (Ad Soyad: '" + registerRequestDto.getUsername() + "') zaten alınmış.");
        }

        // E-posta zaten var mı diye kontrol et
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new RuntimeException("Bu e-posta adresi ('" + registerRequestDto.getEmail() + "') zaten kullanımda.");
        }

        // Yeni kullanıcı oluştur
        User newUser = new User();
        newUser.setUsername(registerRequestDto.getUsername()); // Ad Soyad'ı username olarak kaydediyoruz
        newUser.setEmail(registerRequestDto.getEmail());
        newUser.setPassword(registerRequestDto.getPassword()); // Şimdilik düz metin şifre
        newUser.setRole("USER"); // Varsayılan rol
        // newUser.setLanguagePreference("tr"); // Kaldırıldı

        User savedUser = userRepository.save(newUser);

        return mapToUserDto(savedUser);
    }
}