package com.ls.downloader;

import com.ls.util.HttpDownloadUtils;
import com.ls.util.HttpUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * desc : 分段下载器(适合大文件下载)
 * Created by Lon on 2019/2/1.
 */
public class SegmentedDownloader implements Downloader {

    // 下载的url
    private String url;
    // 下载的文件地址
    private String targetPath;
    // 当前已下载的字节数
    private long curBytes = 0;
    // 分段下载的文件大小
    private long rangeSize;

    // 整个文件是否下载完毕
    private boolean isDone;

    public SegmentedDownloader(String url, String targetPath, long rangeSize) {
        this.url = url;
        this.targetPath = targetPath;
        this.rangeSize = rangeSize;
        this.isDone = false;
    }

    @Override
    public int download() {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Range", String.format("bytes=%d-%d", curBytes, curBytes + rangeSize));

        InputStream is = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        // 本次下载总共读取了多少个字节
        int totalReads = 0;

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // 获取response的主体内容
            is = response.getEntity().getContent();
            File storeFile = new File(targetPath);
            // 创建下载目录(没有的话)
            boolean mkSuccess = storeFile.getParentFile().mkdirs();

            fos = new FileOutputStream(storeFile, true);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[4096];   // 4kb
            // 实际读取到的字节数
            int reads;
            while ((reads = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, reads);
                totalReads += reads;
            }

            // 缓冲区的内容写入到文件
            bos.flush();

            // 如果本次读取到的字节数 < 分段字节数，证明整个文件下载完毕
            if (reads < rangeSize) {
                this.isDone = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return totalReads;
    }

    @Override
    public boolean isDown() {
        return isDone;
    }

}
