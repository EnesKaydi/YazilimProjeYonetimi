package com.eneskaydi.yazilimprojeyonetimi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API path'leriniz için geçerli olacak şekilde daraltabilirsiniz, şimdilik genel tuttuk.
                .allowedOrigins("http://localhost:3000") // Frontend uygulamanızın adresi
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // İzin verilen HTTP metotları (OPTIONS dahil)
                .allowedHeaders("*") // İzin verilen tüm header'lar
                .allowCredentials(true); // Kimlik bilgileriyle (örn: cookie) isteklere izin ver
    }
} 