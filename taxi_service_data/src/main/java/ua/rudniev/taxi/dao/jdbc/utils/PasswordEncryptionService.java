package ua.rudniev.taxi.dao.jdbc.utils;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * This class has fields and methods that transforms passwords to hash
 */
public class PasswordEncryptionService {
    private static final byte[] salt = "yummy_salt".getBytes();

    /**
     * This method returns hash password from given string
     * @param password given string
     * @return hash
     */
    public byte[] getEncryptedPassword(String password) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 10000;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
            return f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("An error occurred while encrypting password.", e);
        }
    }


}
