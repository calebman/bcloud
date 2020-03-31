package com.bcloud.common.util;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author JianhuiChen
 * @description 文件操作工具类
 * @date 2020-03-24
 */
public class FileUtils {

    /**
     * 根据文件的互联网链接执行下载操作
     * 支持 HTTP/HTTPS 协议的链接
     * @param url 文件下载链接
     * @return 文件的输入流
     * @throws NoSuchAlgorithmException 下载异常
     * @throws IOException              下载异常
     * @throws KeyManagementException   下载异常
     */
    public static InputStream downloadFileAsUrl(String url) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        if (url.startsWith("https")) {
            return downloadFileAsHttps(url);
        } else if (url.startsWith("http")) {
            return downloadFileAsHttp(url);
        } else {
            throw new RuntimeException("Invaild url " + url);
        }
    }

    /**
     * 根据文件的互联网链接地址获取文件名称
     *
     * @param url 链接地址
     * @return 文件名称
     */
    public static String getFileNameAsUrl(String url) {
        int paramsEnd = url.lastIndexOf("?");
        return url.substring(url.lastIndexOf("/"), paramsEnd > -1 ? paramsEnd : url.length());
    }


    private static InputStream downloadFileAsHttp(String httpUrl) throws IOException {
        URL url = new URL(httpUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(60 * 1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        return conn.getInputStream();
    }

    private static InputStream downloadFileAsHttps(String httpsUrl) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, null);
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(httpsUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(ssf);
        conn.setConnectTimeout(60 * 1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        return conn.getInputStream();
    }
}
