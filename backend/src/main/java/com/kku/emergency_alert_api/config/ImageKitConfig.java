package com.kku.emergency_alert_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.imagekit.sdk.ImageKit;
import jakarta.annotation.PostConstruct;

@Configuration
public class ImageKitConfig {
    @Value("${imagekit.public.key}")
    private String publicKey;

    @Value("${imagekit.private.key}")
    private String privateKey;

    @Value("${imagekit.url.endpoint}")
    private String urlEndpoint;

    @PostConstruct
    public void init() {
        ImageKit.getInstance().setConfig(new io.imagekit.sdk.config.Configuration(
                publicKey,
                privateKey,
                urlEndpoint));
    }
}
