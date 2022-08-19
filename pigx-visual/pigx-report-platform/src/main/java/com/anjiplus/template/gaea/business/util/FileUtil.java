package com.anjiplus.template.gaea.business.util;

import com.anji.plus.gaea.code.ResponseCode;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * Created by raodeming on 2021/8/23.
 */
@Slf4j
public class FileUtil {

    //链接url下载图片
    public static void downloadPicture(String urlPath, String path) {
        URL url = null;
        try {
            url = new URL(urlPath);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
            log.info("链接下载图片：{}，临时路径：{}", urlPath, path);
        } catch (IOException e) {
            log.error("根据链接下载失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        }
    }


    /**
     * 复制文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            log.error("复制文件失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                log.error("", e);
                throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
            }
        }
    }

    /**
     * 复制文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(String source, String dest) {
        copyFileUsingFileChannels(new File(source), new File(dest));
    }


    public static void WriteStringToFile(String filePath, String content) {
        try {
            //不存在创建文件
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(outputWriter);
            bw.write(content);
            bw.close();
            outputWriter.close();
            outputStream.close();
        } catch (Exception e) {
            log.error("写入文件失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        }
    }


    /**
     * 根据文件读取文本文件内容
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        BufferedReader reader = null;
        InputStreamReader isr = null;
        StringBuilder sbf = new StringBuilder();
        try {
            isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            log.error("读文件失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        } finally {
            if (null != isr) {
                try {
                    isr.close();
                } catch (IOException e) {
                    throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e1.getMessage());
                }
            }
        }
    }

    /**
     * 根据文件路径读取文本文件内容
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        File file = new File(filePath);
        return readFile(file);
    }

    static final int BUFFER = 8192;

    /**
     * 将文件夹压缩zip包
     *
     * @param srcPath
     * @param dstPath
     * @throws IOException
     */
    public static void compress(String srcPath, String dstPath) {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);

        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(out, new CRC32());
            zipOut = new ZipOutputStream(cos, StandardCharsets.UTF_8);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        } catch (IOException e) {
            log.error("压缩文件夹失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        } finally {
            if (null != zipOut) {
                try {
                    zipOut.close();
                } catch (IOException e) {
                    log.error("", e);
                    throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
                }
                out = null;
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("", e);
                    throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
                }
            }
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) {
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        } catch (IOException e) {
            log.error("压缩文件夹失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        } finally {
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("", e);
                    throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
                }
            }
        }
    }

    public static void decompress(String zipFile, String dstPath) {
        try {
            decompress(new ZipFile(zipFile), dstPath);
        } catch (IOException e) {
            log.error("", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        }
    }

    public static void decompress(MultipartFile zipFile, String dstPath) {
        try {
            File file = new File(dstPath + File.separator + zipFile.getOriginalFilename());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            zipFile.transferTo(file);
            decompress(new ZipFile(file), dstPath);
            //解压完删除
            file.delete();
        } catch (IOException e) {
            log.error("", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        }
    }

    /**
     * 解压zip
     *
     * @param zip
     * @param dstPath
     * @throws IOException
     */
    public static void decompress(ZipFile zip, String dstPath) {
        log.info("解压zip：{}，临时目录：{}", zip.getName(), dstPath);
        File pathFile = new File(dstPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try {

            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = zip.getInputStream(entry);
                    String outPath = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");
                    ;
                    //判断路径是否存在,不存在则创建文件路径
                    File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                    if (new File(outPath).isDirectory()) {
                        continue;
                    }

                    out = new FileOutputStream(outPath);
                    byte[] buf1 = new byte[1024];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                } catch (IOException e) {
                    log.error("解压失败", e);
                    throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
                } finally {
                    if (null != in) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            log.error("", e);
                            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
                        }
                    }

                    if (null != out) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            log.error("", e);
                            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
                        }
                    }
                }
            }
            zip.close();
        } catch (IOException e) {
            log.error("解压失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        }
    }

    /**
     * 获取流文件
     * @param ins
     * @param file
     */
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            log.error("获取流文件失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        }
    }

    /**
     * 删除文件夹
     *
     * @param path
     */
    public static void delete(String path) {

        Path directory = Paths.get(path);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file); // this will work because it's always a File
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir); //this will work because Files in the directory are already deleted
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.error("删除文件失败", e);
            throw BusinessExceptionBuilder.build(ResponseCode.FAIL_CODE, e.getMessage());
        }
    }


    public static void main(String[] args) throws Exception {
//        String targetFolderPath = "D:\\aa";
//        String rawZipFilePath = "D:\\aa.zip";
//        String newZipFilePath = "D:\\aa.zip";
//
//
//        //将目标目录的文件压缩成Zip文件
//        FileUtil.compress(targetFolderPath, newZipFilePath);
//
//        //将Zip文件解压缩到目标目录
//        FileUtil.decompress(rawZipFilePath, targetFolderPath);

//        FileUtil.downloadPicture("http://10.108.26.197:9095/file/download/fd20d563-00aa-45e2-b5db-aff951f814ec", "D:\\abc.png");


//        delete("D:\\aa");

    }


}
