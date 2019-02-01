package com.ls.util;

import com.ls.entity.HttpClientResult;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
/**
 * desc :
 * Created by Lon on 2019/2/1.
 */
public class HttpUtilsTest {

    @Test
    public void testDoGet() throws Exception {
        HttpClientResult result = HttpUtils.doGet("http://www.baidu.com");
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testDownload() throws IOException {

//        HttpUtils.download(
//                "http://www.downza.cn/download/24772?module=soft&id=24772&token=8b9cb50c9a9f83716d312719593c1717&isxzq=0",
//                "D:\\test_download\\content\\xl_jianjie.exe",
//                new HashMap<>());

//        HttpUtils.download(
//                "http://www.downza.cn/download/26182?module=soft&id=26182&token=203eaa1830a3fb9d8703b7861eb0ab1b&isxzq=0",
//                "D:\\test_download\\content\\xl_jisu.exe",
//                new HashMap<>());

    }

}
