package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.LoginRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.LoginResponseDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.RegisterRequestDto;
import com.eneskaydi.yazilimprojeyonetimi.Dto.UserDto;

// Kimlik doğrulama işlemlerinden sorumlu servis arayüzü.
public interface AuthService {

    /**
     * Kullanıcı adı/e-posta ve şifre ile kullanıcı girişi yapar.
     *
     * @param loginRequestDto Kullanıcı giriş bilgilerini içeren DTO.
     * @return Başarılı giriş sonrası JWT ve kullanıcı bilgilerini içeren DTO.
     */
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    /**
     * Yeni bir kullanıcı kaydı oluşturur.
     *
     * @param registerRequestDto Kullanıcı kayıt bilgilerini içeren DTO.
     * @return Kaydedilen kullanıcının bilgilerini içeren UserDto (şifre olmadan).
     * @throws RuntimeException Kullanıcı adı veya e-posta zaten mevcutsa.
     */
    UserDto register(RegisterRequestDto registerRequestDto);
}