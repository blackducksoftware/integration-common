/**
 * integration-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.encryption;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.exception.EncryptionException;

public class EncryptionUtils {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    public static final String EMBEDDED_SUN_KEY_FILE = "Sun-Key.jceks";
    public static final String EMBEDDED_IBM_KEY_FILE = "IBM-Key.jceks";

    // needs to be at least 8 characters
    public static final char[] KEY_PASS = { 'b', 'l', 'a', 'c', 'k', 'd', 'u', 'c', 'k', '1', '2', '3', 'I', 'n', 't', 'e', 'g', 'r', 'a', 't', 'i', 'o', 'n' };

    public Cipher getEncryptionCipher(final String keyStoreType, final InputStream keyStoreStream, final String keyAlias, final char[] keyPassword, final String algorithm)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher(keyStoreType, keyStoreStream, keyAlias, keyPassword, Cipher.ENCRYPT_MODE, algorithm);
    }

    public Cipher getDecryptionCipher(final String keyStoreType, final InputStream keyStoreStream, final String keyAlias, final char[] keyPassword, final String algorithm)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher(keyStoreType, keyStoreStream, keyAlias, keyPassword, Cipher.DECRYPT_MODE, algorithm);
    }

    public Cipher getEmbeddedEncryptionCipher(final InputStream keyStoreStream)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher("JCEKS", keyStoreStream, "keyStore", KEY_PASS, Cipher.ENCRYPT_MODE, "DES/ECB/NoPadding");
    }

    public Cipher getEmbeddedDecryptionCipher(final InputStream keyStoreStream)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher("JCEKS", keyStoreStream, "keyStore", KEY_PASS, Cipher.DECRYPT_MODE, "DES/ECB/NoPadding");
    }

    private Cipher getCipher(final String keyStoreType, final InputStream keyStoreStream, final String keyAlias, final char[] keyPassword, final int cipherMode, final String algorithm)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        final Key key = retrieveKeyFromInputStream(keyStoreType, keyStoreStream, keyAlias, keyPassword);

        final Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(cipherMode, key);
        return cipher;
    }

    public String encrypt(final Cipher cipher, final String password) throws EncryptionException {
        assertValidPassword(password);
        final String alteredString = getAlteredString(password, Cipher.ENCRYPT_MODE, cipher);
        if (alteredString == null) {
            throw new EncryptionException("The encrypted password is null");
        }
        return alteredString;
    }

    public String decrypt(final Cipher cipher, final String password) throws EncryptionException {
        assertValidPassword(password);
        final String alteredString = getAlteredString(password, Cipher.DECRYPT_MODE, cipher);
        if (alteredString == null) {
            throw new EncryptionException("The decrypted password is null");
        }
        return alteredString;
    }

    private void assertValidPassword(final String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Please provide a non-blank password.");
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

    public Key retrieveKeyFromInputStream(final String keyStoreType, final InputStream keyStoreInputStream, final String keyAlias, final char[] keyPassword)
        throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        final KeyStore keystore = KeyStore.getInstance(keyStoreType);
        keystore.load(keyStoreInputStream, keyPassword);
        final Key key = keystore.getKey(keyAlias, keyPassword);
        return key;
    }

}
