package com.synopsys.integration.encryption;

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.exception.EncryptionException;

public class EncryptionService {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public String encrypt(final Cipher encryptionCipher, final String plainString) throws EncryptionException {
        assertNotBlank(plainString);
        final String alteredString = getAlteredString(plainString, Cipher.ENCRYPT_MODE, encryptionCipher);
        if (alteredString == null) {
            throw new EncryptionException("The encrypted password is null");
        }
        return alteredString;
    }

    public String decrypt(final Cipher decryptionCipher, final String encryptedString) throws EncryptionException {
        assertNotBlank(encryptedString);
        final String alteredString = getAlteredString(encryptedString, Cipher.DECRYPT_MODE, decryptionCipher);
        if (alteredString == null) {
            throw new EncryptionException("The decrypted password is null");
        }
        return alteredString;
    }

    private void assertNotBlank(final String input) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("Please provide a non-blank input.");
        }
    }

    private String getAlteredString(final String original, final int cipherMode, final Cipher cipher) throws EncryptionException {
        String alteredString = null;
        try {
            byte[] bytes = original.getBytes(UTF8);
            final int originalBytesLength = bytes.length;
            // The buffer must be a multiple of 8 or else
            // the Cipher cant handle it
            final int bufferSize = originalBytesLength + (8 - originalBytesLength % 8);
            bytes = Arrays.copyOf(bytes, bufferSize);
            if (Cipher.ENCRYPT_MODE == cipherMode) {
                alteredString = doEncryption(cipher, bytes);
            } else {
                alteredString = doDecryption(cipher, bytes);
            }
        } catch (final Exception e) {
            throw new EncryptionException(e);
        }

        return alteredString;
    }

    private String doEncryption(final Cipher cipher, final byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
        final byte[] buffer = cipher.doFinal(bytes);
        final String encryptedPassword = new String(Base64.encodeBase64(buffer), UTF8).trim();
        return encryptedPassword;
    }

    private String doDecryption(final Cipher cipher, final byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
        final byte[] buffer = cipher.doFinal(Base64.decodeBase64(bytes));
        final String decryptedString = new String(buffer, UTF8).trim();
        return decryptedString;
    }
}
