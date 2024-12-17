package org.vehicles;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptionUtils {

    // Генерация ключа AES
    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            return keyGen.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Шифрование данных
    public static String encrypt(String data, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
        }
        return null;
    }

    // Расшифровка данных
    public static String decrypt(String encryptedData, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
        }
        return null;
    }
}
