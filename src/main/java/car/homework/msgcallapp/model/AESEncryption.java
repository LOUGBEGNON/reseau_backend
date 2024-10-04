package car.homework.msgcallapp.model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESEncryption {

    private static SecretKey secretKey;

    // Générer une clé AES de manière dynamique
    public static void generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Taille de la clé AES
        secretKey = keyGen.generateKey();  // Générer la clé
    }

    // Chiffrement des données
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // Déchiffrement des données
    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedValue);
        return new String(decryptedData);
    }

    // Retourner la clé sous forme de chaîne pour stockage éventuel (facultatif)
    public static String getSecretKey() {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Définir la clé à partir d'une chaîne (facultatif si vous stockez la clé)
    public static void setSecretKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}