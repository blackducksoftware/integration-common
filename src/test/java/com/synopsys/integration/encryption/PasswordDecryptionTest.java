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
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.crypto.Cipher;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PasswordDecryptionTest {
    private static Properties encryptedUserPassword = null;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void init() throws Exception {
        encryptedUserPassword = new Properties();
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final InputStream is = classLoader.getResourceAsStream("encryptedPasswordFile.txt");
        try {
            encryptedUserPassword.load(is);
        } catch (final IOException e) {
            System.err.println("reading encryptedPasswordFile failed!");
        }
    }

    private InputStream getEmbeddedKey() {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(EncryptionUtils.EMBEDDED_SUN_KEY_FILE);
    }

    @Test
    public void testPasswordDecryption() throws Exception {
        final Cipher cipher = EncryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey());
        final DecryptionService decryptionService = new DecryptionService(cipher);

        final String encryptedPassword = encryptedUserPassword.getProperty("super");
        System.out.println("Encrypted password: " + encryptedPassword);
        final String decryptedPassword = decryptionService.decrypt(encryptedPassword);
        System.out.println("Decrypted password: " + decryptedPassword);
        assertEquals("super", decryptedPassword);
    }

    @Test
    public void testPasswordDecryptionAgain() throws Exception {
        final Cipher cipher = EncryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey());
        final DecryptionService decryptionService = new DecryptionService(cipher);
        assertEquals("testing", decryptionService.decrypt(encryptedUserPassword.getProperty("test@blackducksoftware.com")));
    }

    @Test
    public void testPasswordDecryptionEmptyKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Please provide a non-blank encrypted string.");
        final Cipher cipher = EncryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey());
        final DecryptionService decryptionService = new DecryptionService(cipher);
        assertNull(decryptionService.decrypt(""));
    }

    @Test
    public void testPasswordDecryptionNullKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Please provide a non-blank encrypted string.");
        final Cipher cipher = EncryptionUtils.getEmbeddedDecryptionCipher(getEmbeddedKey());
        final DecryptionService decryptionService = new DecryptionService(cipher);
        assertNull(decryptionService.decrypt(null));
    }

}
