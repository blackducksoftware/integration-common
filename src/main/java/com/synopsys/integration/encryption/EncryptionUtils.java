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

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class EncryptionUtils {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    public static final String EMBEDDED_SUN_KEY_FILE = "Sun-Key.jceks";
    public static final String EMBEDDED_IBM_KEY_FILE = "IBM-Key.jceks";

    // needs to be at least 8 characters
    private static final char[] KEY_PASS = { 'b', 'l', 'a', 'c', 'k', 'd', 'u', 'c', 'k', '1', '2', '3', 'I', 'n', 't', 'e', 'g', 'r', 'a', 't', 'i', 'o', 'n' };

    public static Cipher getEncryptionCipher(final String keyStoreType, final InputStream keyStoreStream, final String keyAlias, final char[] keyPassword, final String algorithm)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher(keyStoreType, keyStoreStream, keyAlias, keyPassword, Cipher.ENCRYPT_MODE, algorithm);
    }

    public static Cipher getDecryptionCipher(final String keyStoreType, final InputStream keyStoreStream, final String keyAlias, final char[] keyPassword, final String algorithm)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher(keyStoreType, keyStoreStream, keyAlias, keyPassword, Cipher.DECRYPT_MODE, algorithm);
    }

    public static Cipher getEmbeddedEncryptionCipher(final InputStream keyStoreStream)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher("JCEKS", keyStoreStream, "keyStore", KEY_PASS, Cipher.ENCRYPT_MODE, "DES/ECB/NoPadding");
    }

    public static Cipher getEmbeddedDecryptionCipher(final InputStream keyStoreStream)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        return getCipher("JCEKS", keyStoreStream, "keyStore", KEY_PASS, Cipher.DECRYPT_MODE, "DES/ECB/NoPadding");
    }

    private static Cipher getCipher(final String keyStoreType, final InputStream keyStoreStream, final String keyAlias, final char[] keyPassword, final int cipherMode, final String algorithm)
        throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException {
        final Key key = retrieveKeyFromInputStream(keyStoreType, keyStoreStream, keyAlias, keyPassword);

        final Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(cipherMode, key);
        return cipher;
    }

    private static Key retrieveKeyFromInputStream(final String keyStoreType, final InputStream keyStoreInputStream, final String keyAlias, final char[] keyPassword)
        throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        final KeyStore keystore = KeyStore.getInstance(keyStoreType);
        keystore.load(keyStoreInputStream, keyPassword);
        final Key key = keystore.getKey(keyAlias, keyPassword);
        return key;
    }

}
