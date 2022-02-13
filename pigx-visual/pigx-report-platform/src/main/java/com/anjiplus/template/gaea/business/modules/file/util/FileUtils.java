package com.anjiplus.template.gaea.business.modules.file.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Created by raodeming on 2021/7/8.
 */
@Slf4j
public class FileUtils {
    public static byte[] readFileToByteArray(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            log.error("error", e);
        }
        return buffer;
    }

}
