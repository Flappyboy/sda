package cn.edu.nju.software.sda.core.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class FileCompress {
//    public static void main(String[] args) {
//        unCompress("/Users/yaya/Desktop/analysis_code-0.0.1-SNAPSHOT.war", "/Users/yaya/Desktop/analysis_code");
//
//        unCompress("/Users/yaya/Desktop/jedis-2.4.2.jar", "/Users/yaya/Desktop/jedis-2.4.2");
//    }
    public static final int BUFFER_SIZE = 1024;

    public static void unCompress(String warOrJarFile, String unzipPath) {
        if (warOrJarFile.trim().endsWith(".war") || warOrJarFile.trim().endsWith(".jar")) {
            File warFile = new File(warOrJarFile);

            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(warFile));
                ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, bufferedInputStream);
                JarArchiveEntry entry = null;
                while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        new File(unzipPath, entry.getName()).mkdir();
                    } else {
                        OutputStream out = FileUtils.openOutputStream(new File(unzipPath, entry.getName()));
                        IOUtils.copy(in, out);
                        out.close();
                    }
                }
                in.close();
            } catch (FileNotFoundException e) {
                System.err.println("未找到war文件");
            } catch (ArchiveException e) {
                System.err.println("不支持的压缩格式");
            } catch (IOException e) {
                System.err.println("文件写入发生错误");
            }
        }

    }

    public static File unZip(String fileName, String destDir){
        return unCompress(new File(fileName), destDir, ArchiveStreamFactory.ZIP);
    }
    public static File unZip(File file, String destDir){
        return unCompress(file, destDir, ArchiveStreamFactory.ZIP);
    }

    public static File unCompress(File file, String destDir, String type) {
        // 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
        if(StringUtils.isBlank(destDir)) {
            destDir = file.getParent();
        }

        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(type, bufferedInputStream);
            ArchiveEntry entry = null;
            while ((entry = in.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(destDir, entry.getName()).mkdir();
                } else {
                    OutputStream out = FileUtils.openOutputStream(new File(destDir, entry.getName()));
                    IOUtils.copy(in, out);
                    out.close();
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("未找到目标文件");
        } catch (ArchiveException e) {
            System.err.println("不支持的压缩格式");
        } catch (IOException e) {
            System.err.println("文件写入发生错误");
        }

        File result = new File(destDir);
        return result;
    }

    public static void main(String[] args) throws Exception {
        File file = unZip("E:\\tmp_workspace\\project\\pinpoint-plugin-generate\\build\\distributions\\pinpoint-plugin-generate-1.0.5-released.zip",
                "E:/tmp_workspace/project/pinpoint-plugin-generate/build/distributions/"+System.currentTimeMillis());
        System.out.println(file.getAbsolutePath());
//        FileUtils.deleteDirectory(file);
    }
}
