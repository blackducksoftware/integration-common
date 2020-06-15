package com.synopsys.integration.httpclient;

import org.apache.hc.client5.http.auth.*;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicSchemeFactory;
import org.apache.hc.client5.http.impl.auth.NTLMSchemeFactory;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * A simple example that uses HttpClient to execute an HTTP request
 * over a secure connection tunneled through an authenticating proxy.
 */
public class NtlmProxyAuthenticationTestOriginal {

    public static void main(String[] args) throws Exception {
        final String proxyHostName = "int-proxy02.dc1.lan";
        final int proxyPort = 3128;

        String proxyDomain = null;
        final String proxyUserName = "ntlm";
        final String proxyUserPassword = "Blackduck5!";

        NTCredentials ntlmCredentials = new NTCredentials(
                proxyUserName,
                proxyUserPassword.toCharArray(),
                null,
                proxyDomain);

        Registry<AuthSchemeFactory> authRegistry = RegistryBuilder.<AuthSchemeFactory>create()
                .register(StandardAuthScheme.NTLM, new NTLMSchemeFactory())
                .register(StandardAuthScheme.BASIC, new BasicSchemeFactory())
                .build();

        HttpHost proxy = new HttpHost(proxyHostName, proxyPort);
        HttpHost target = new HttpHost("http", "httpbin.org", 80);

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(null, null, -1, null, null), ntlmCredentials);
        credentialsProvider.setCredentials(
                new AuthScope("httpbin.org", 80),
                new UsernamePasswordCredentials("user", "passwd".toCharArray()));

        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultAuthSchemeRegistry(authRegistry)
                .setProxy(proxy)
                .build()) {

            HttpGet httpget = new HttpGet("/basic-auth/user/passwd");

            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri() +
                    " via " + proxy);

            try (CloseableHttpResponse response = httpclient.execute(target, httpget)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }
}