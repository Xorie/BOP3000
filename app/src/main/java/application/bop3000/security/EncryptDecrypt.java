package application.bop3000.security;

import android.util.Base64;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt {
    // Class private variables
    private static final String SECRET_KEY = "5Fcyq893iatLEX51";

    private static final String SALT = "BOP3000R";

    // This method use to encrypt to string
    public static String encrypt(String strToEncrypt)
    {
        try {
            // Create default byte array
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            // Create SecretKeyFactory object
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // Create KeySpec object and assign with
            // constructor
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            byte [] encryptedByteValue = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
            // Return encrypted string
            return encryptedValue64;
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    // This method use to decrypt to string
    public static String decrypt(String strToDecrypt)
    {
        try {
            // Default byte array
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            // Create IvParameterSpec object and assign with
            // constructor
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            // Create SecretKeyFactory Object
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // Create KeySpec object and assign with
            // constructor
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(),65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte [] decryptedValue64 = Base64.decode(strToDecrypt, Base64.DEFAULT);
            byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
            String decryptedValue = new String(decryptedByteValue,"utf-8");
            // Return decrypted string
            return decryptedValue;
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}

/*
public class EncryptDecrypt {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    //private static final String ALGORITHM = "AES";
    private static final String KEY = "5Fcyq893iatLEX51";

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance(EncryptDecrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance(EncryptDecrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte [] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(EncryptDecrypt.KEY.getBytes(),EncryptDecrypt.ALGORITHM);
        return key;
    }

    public static IvParameterSpec generateIv() throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}*/
