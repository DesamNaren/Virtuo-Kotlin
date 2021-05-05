package com.cgg.virtuokotlin;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by Dell1 on 22-Dec-16.
 */

public class AesEncryption {

    private static AesEncryption INSTANCE = null;

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public static AesEncryption getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new AesEncryption();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (INSTANCE);
    }


    private AesEncryption() throws Exception {
        SecretKey secret = new SecretKeySpec("GGC@Virtu000AesC".getBytes(),
                "AES");

        encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secret);

        decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, secret);
    }


    public String encrypt(String encrypt) throws Exception {
        byte[] bytes = encrypt.getBytes("UTF8");
        byte[] encrypted = encrypt(bytes);
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public byte[] encrypt(byte[] plain) throws Exception {
        return encryptCipher.doFinal(plain);
    }

    public String decrypt(String encrypt) throws Exception {
        byte[] bytes = Base64.decode(encrypt, Base64.DEFAULT);
        byte[] decrypted = decrypt(bytes);
        return new String(decrypted, "UTF8");
    }

    /**
     * Decrypts given byte array
     *
     * @param encrypt
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] encrypt) throws Exception {
        return decryptCipher.doFinal(encrypt);
    }

}
