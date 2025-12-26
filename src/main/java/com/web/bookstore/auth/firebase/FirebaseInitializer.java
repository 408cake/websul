package com.web.bookstore.api.auth.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class FirebaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(FirebaseInitializer.class);

    @Value("${firebase.credentials-base64:}")
    private String credentialsBase64;

    @PostConstruct
    public void init() {
        if (!FirebaseApp.getApps().isEmpty()) return;

        if (credentialsBase64 == null || credentialsBase64.isBlank()) {
            log.warn("Firebase credentials-base64 is empty. Firebase auth will not work.");
            return;
        }

        try {
            byte[] decoded = Base64.getDecoder().decode(credentialsBase64.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decoded));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);
            log.info("FirebaseApp initialized.");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize FirebaseApp. Check firebase.credentials-base64", e);
        }
    }
}
