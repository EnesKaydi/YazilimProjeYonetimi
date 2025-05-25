package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.LoginRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.LoginResponseDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.RegisterRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UserDto;
import com.eneskaydi.yazilimprojeyonetimi.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Kimlik doğrulama işlemlerini yöneten REST controller sınıfı.
 * Kullanıcı girişi gibi temel kimlik doğrulama endpoint'lerini içerir.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Kullanıcı girişi için API endpoint'i.
     * Kullanıcı adı ve şifre ile kimlik doğrulaması yapar.
     * Başarılı kimlik doğrulama sonucunda bir JWT veya benzeri bir token içeren
     * LoginResponseDto döndürür.
     *
     * @param loginRequestDto Kullanıcı adı ve şifreyi içeren DTO.
     * @return Başarılı giriş durumunda HTTP 200 OK ile LoginResponseDto,
     *         başarısız durumda uygun HTTP hata kodu.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        // authService'den login metodunu çağırarak kimlik doğrulama işlemi yapılır.
        // Dönen response, ResponseEntity ile sarmalanarak istemciye gönderilir.
        LoginResponseDto loginResponse = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Yeni kullanıcı kaydı için API endpoint'i.
     * Kullanıcı adı/soyadı, e-posta ve şifre ile yeni bir kullanıcı oluşturur.
     *
     * @param registerRequestDto Kullanıcı kayıt bilgilerini içeren DTO.
     * @return Başarılı kayıt durumunda HTTP 201 Created ile UserDto (şifresiz),
     *         başarısız durumda uygun HTTP hata kodu.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserDto registeredUser = authService.register(registerRequestDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}