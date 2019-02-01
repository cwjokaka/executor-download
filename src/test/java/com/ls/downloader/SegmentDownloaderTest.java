package com.ls.downloader;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * desc :
 * Created by Lon on 2019/2/1.
 */
public class SegmentDownloaderTest {

    // 测试下载地址(连接会尝试下载迅雷极速版软件,大概有33M)
    private static final String DOWNLOAD_URL = "http://www.downza.cn/download/24772?module=soft&id=24772&token=8b9cb50c9a9f83716d312719593c1717&isxzq=0";
    // 下载的目标目录与文件名
    private static final String TARGET_PATH = "D:\\test_download\\content\\xl_jianjie.exe";

    private Downloader partialDownloader;
    private Downloader entireDownloader;

    @Before
    public void init() {
        partialDownloader = new SegmentedDownloader(
                DOWNLOAD_URL,
                TARGET_PATH,
                1024 * 1024 // 下载1M
        );

        entireDownloader = new SegmentedDownloader(
                DOWNLOAD_URL,
                TARGET_PATH,
                1024 * 1024 * 1024 // 下载1G
        );

    }

    /**
     * 测试分段下载
     */
    @Test
    public void testPartedDownload() {
        int size = partialDownloader.download();
        System.out.println("size" + size);
        assertTrue(size > 0);
        assertFalse(partialDownloader.isDown());
    }

    /**
     * 测试完全下载
     */
    @Test
    public void testTotallyDownload() {
        int size = entireDownloader.download();
        System.out.println("size" + size);
        assertTrue(size > 0);
        assertTrue(entireDownloader.isDown());
    }

}
