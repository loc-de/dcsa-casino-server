package com.polis.security;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class CryptoService {

    private static final String secret_key = "secret_key_for_c";
    private static final String algorithm = "AES";

    private static Key getKey() {
        byte[] keyBytes = secret_key.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, algorithm);
    }

    @SneakyThrows
    public static byte[] encrypt(byte[] bytes) {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, getKey());

        return cipher.doFinal(bytes);
    }

    @SneakyThrows
    public static byte[] decrypt(byte[] bytes) {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, getKey());

        return cipher.doFinal(bytes);
    }

}
