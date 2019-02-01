package com.ls.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 文件工具类
 */
public class FileUtils {

    public static long getFileSize(String localPath) {
        File f = new File(localPath);
        return getFileSize(f);
    }

    public static long getFileSize(File f) {
        if (!f.exists()) {
            return 0;
        }
        return f.length();
    }

    public static void resetFileSize(String localPath, long targetSize) throws IOException {

        if (targetSize == 0) {
            deleteFile(localPath);
            return;
        }

        String backupPath = String.format("%s.bk", localPath);

        int sizeOfFiles = 1024 * 1024; // 1MB
        byte[] buffer = new byte[sizeOfFiles];

        try (FileInputStream fis = new FileInputStream(localPath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream(backupPath, true)) {

            long bytesAmount;
            long bytesLeft = targetSize;

            while ((bytesAmount = bis.read(buffer)) > 0 && bytesLeft > 0) {
                if (bytesLeft < bytesAmount) {
                    bytesAmount = bytesLeft;
                }
                fos.write(buffer, 0, (int)bytesAmount);
                bytesLeft -= bytesAmount;
            }
        }
        Files.move(Paths.get(backupPath), Paths.get(localPath), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteFile(String localPath) {
        File f = new File(localPath);
        if (!f.exists()) {
            return;
        }
        f.delete();
    }

    public static long getFreeSpace(String localPath) {
        File f = new File(localPath);
        return f.getFreeSpace();
    }
}
