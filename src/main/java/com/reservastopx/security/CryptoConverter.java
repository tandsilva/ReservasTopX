package com.reservastopx.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private final SecretKeySpec key;

    public CryptoConverter(@Value("${crypto.secret-key}") String secret) {
        // Garante 16 bytes para AES-128
        if (secret.length() < 16) {
            secret = String.format("%-16s", secret); // preenche com espaços
        } else if (secret.length() > 16) {
            secret = secret.substring(0, 16); // corta para 16 bytes
        }
        key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] enc = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(enc);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao criptografar", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(dbData);
            byte[] dec = cipher.doFinal(decoded);
            return new String(dec, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao descriptografar", e);
        }
    }
}
