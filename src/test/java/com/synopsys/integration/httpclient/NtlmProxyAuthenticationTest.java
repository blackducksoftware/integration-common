package com.synopsys.integration.httpclient;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class NtlmProxyAuthenticationTest {
    public static void main(String[] args) throws Exception {
        final String proxyHostName = "int-proxy02.dc1.lan";
        final int proxyPort = 3128;

        NTCredentials ntlmCredentials = new NTCredentials("ntlm", "Blackduck5!".toCharArray(), null, null);

        HttpHost proxy = new HttpHost(proxyHostName, proxyPort);

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope("blackducksoftware.com", 443), ntlmCredentials);

        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setProxy(proxy)
                .build()) {

            HttpGet httpget = new HttpGet("https://www.blackducksoftware.com");

            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri() +
                    " via " + proxy);

            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }

}