/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
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
 *******************************************************************************/
package com.synopsys.integration.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class PasswordEncryptionTest {
    private static InputStream keyStream = null;

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void init() throws Exception {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        keyStream = classLoader.getResourceAsStream(EncryptionUtils.EMBEDDED_SUN_KEY_FILE);
    }

    private InputStream getEmbeddedKey() {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(EncryptionUtils.EMBEDDED_SUN_KEY_FILE);
    }

    @Test
    public void testMainEncryptPassword() throws Exception {
        final EncryptionService encryptionService = new EncryptionService();

        final Cipher encryptionCipher = EncryptionUtils.getEmbeddedEncryptionCipher(getEmbeddedKey());
        final String encryptedPassword = encryptionService.encrypt(encryptionCipher, "Password");
        System.out.println(encryptedPassword);
        assertEquals("SaTaqurAqc7q0nf0n6IL4Q==", encryptedPassword);

        final Cipher decryptionCipher = EncryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey());

        final String decrpytedPassword = encryptionService.decrypt(decryptionCipher, encryptedPassword);
        assertEquals("Password", decrpytedPassword);
    }

    @Test
    public void testEncryptLongPassword() throws Exception {
        final EncryptionService encryptionService = new EncryptionService();

        final String longPassword = "LongPasswordLetsSeeHowLongWeCanEncryptWithoutbreakingThingsLongPasswordLetsSeeHowLongWeCanEncryptWithoutbreakingThingsLongPasswordLetsSeeHowLongWeCanEncryptWithoutbreakingThings";
        final String encryptedPassword = encryptionService.encrypt(EncryptionUtils.getEmbeddedEncryptionCipher(getEmbeddedKey()), longPassword);
        final String decryptedPassword = encryptionService.decrypt(EncryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey()), encryptedPassword);

        System.out.println("Long Password : " + longPassword);
        System.out.println("Long Password Length : " + longPassword.length());
        System.out.println("");
        System.out.println("Encrypted Password : " + encryptedPassword);
        System.out.println("Encrypted Password Length : " + encryptedPassword.length());
        System.out.println("");
        System.out.println("Decrypted Password : " + decryptedPassword);
        System.out.println("Decrypted Password Length : " + decryptedPassword.length());

        assertEquals(longPassword.length(), decryptedPassword.length());
        assertEquals(longPassword, decryptedPassword);
    }

    @Test
    public void testAESEncryptPassword() throws Exception {
        final EncryptionUtils encryptionUtils = new EncryptionUtils();
        final String password = "PasswordToTestWith";

        ///////// Setting up the KeyStore /////////
        final String algorithm = "AES";
        final KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(256); // for example
        final SecretKey secretKey = keyGen.generateKey();

        final File rootDir = temporaryFolder.newFolder();
        final File keyFile = new File(rootDir, "tempKey.jceks");
        keyFile.createNewFile();

        final String keyStorePassword = "TemporaryKeyStorePassword";
        final String keyStoreType = "JCEKS";
        final KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, keyStorePassword.toCharArray());

        final String keyAlias = "SecretKeyAlias";
        final KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
        final KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(secretKey);
        ks.setEntry(keyAlias, skEntry, protParam);

        ks.store(new FileOutputStream(keyFile), keyStorePassword.toCharArray());
        ///////////////////////////////////////////
        final EncryptionService encryptionService = new EncryptionService();

        final Cipher encryptionCipher = EncryptionUtils.getEncryptionCipher(keyStoreType, new FileInputStream(keyFile), keyAlias, keyStorePassword.toCharArray(), algorithm);
        final Cipher decryptionCipher = EncryptionUtils.getDecryptionCipher(keyStoreType, new FileInputStream(keyFile), keyAlias, keyStorePassword.toCharArray(), algorithm);

        final String encryptedPassword = encryptionService.encrypt(encryptionCipher, password);
        // Cant assert the actual value here because the AES algorithm produces different output when encrypting String
        assertNotNull(encryptedPassword);

        final String decryptedPassword = encryptionService.decrypt(decryptionCipher, encryptedPassword);
        assertEquals(password, decryptedPassword);
    }

}
