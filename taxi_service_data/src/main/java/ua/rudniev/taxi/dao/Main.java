package ua.rudniev.taxi.dao;

import ua.rudniev.taxi.dao.utils.PasswordEncryptionService;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String str = "password1";
        String encPass = new String(Base64.getEncoder().encode(PasswordEncryptionService.getEncryptedPassword(str)));
        System.out.println(encPass);
    }
}
