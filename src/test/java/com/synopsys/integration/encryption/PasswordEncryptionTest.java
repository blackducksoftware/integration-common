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

import java.io.InputStream;

import javax.crypto.Cipher;

import org.junit.BeforeClass;
import org.junit.Test;

public final class PasswordEncryptionTest {
    private static InputStream keyStream = null;

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
        final EncryptionUtils encryptionUtils = new EncryptionUtils();
        final Cipher cipher = encryptionUtils.getEmbeddedEncryptionCipher(getEmbeddedKey());
        final String encryptedPassword = encryptionUtils.encrypt(encryptionUtils.getEmbeddedEncryptionCipher(getEmbeddedKey()), "Password");
        System.out.println(encryptedPassword);
        assertEquals("SaTaqurAqc7q0nf0n6IL4Q==", encryptedPassword);

        final String decrpytedPassword = encryptionUtils.decrypt(encryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey()), encryptedPassword);
        assertEquals("Password", decrpytedPassword);
    }

    @Test
    public void testEncryptLongPassword() throws Exception {
        final EncryptionUtils encryptionUtils = new EncryptionUtils();
        final String longPassword = "LongPasswordLetsSeeHowLongWeCanEncryptWithoutbreakingThingsLongPasswordLetsSeeHowLongWeCanEncryptWithoutbreakingThingsLongPasswordLetsSeeHowLongWeCanEncryptWithoutbreakingThings";
        final String encryptedPassword = encryptionUtils.encrypt(encryptionUtils.getEmbeddedEncryptionCipher(getEmbeddedKey()), longPassword);
        final String decryptedPassword = encryptionUtils.decrypt(encryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey()), encryptedPassword);

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

}
