package com.ls.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.Map;

/**
 * desc : 基于httpClient 4.x 的http下载工具类
 * Created by Lon on 2019/2/1.
 */
public class HttpDownloadUtils {

    /**
     * 下载文件
     *
     * @param url 下载地址
     * @param filePath 下载的文件路径
     */
    public static void download(String url, String filePath, Map<String, String> headers) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity httpEntity = response.getEntity();
                // 获取内容总长度
//                long contentLength = httpEntity.getContentLength();
                InputStream is = httpEntity.getContent();
                // 根据InputStream 下载文件
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                File storeFile = new File(filePath);
                // 创建下载目录(没有的话)
                storeFile.getParentFile().mkdirs();
                byte[] buffer = new byte[4096];
                int r = 0;
//                long totalRead = 0;
                while ((r = is.read(buffer)) > 0) {
                    output.write(buffer, 0, r);
//                    totalRead += r;
//                    if (progress != null) {// 回调进度
//                        progress.onProgress((int) (totalRead * 100 / contentLength));
//                    }
                }
                FileOutputStream fos = new FileOutputStream(storeFile);
                output.writeTo(fos);
                output.flush();
                output.close();
                fos.close();
                EntityUtils.consume(httpEntity);
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            httpClient.close();
        }
    }


}
