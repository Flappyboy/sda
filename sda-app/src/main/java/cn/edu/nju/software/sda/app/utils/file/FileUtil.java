//package cn.edu.nju.software.pinpoint.statistics.utils.file;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class FileUtil {
//
//    public static void main(String args[]) {
//
//        FileUtil f = new FileUtil();
//        f.instal();
//    }
//
//    /**
//     * 安装jar包
//     */
//    private void instal() {
//        System.out.println("Start Installing Portal......");
//        System.out.println("Start Extracting File......");
//
//        String jarFileName = "/Users/yaya/Desktop/jedis-2.4.2.jar";
//        Date d = new Date();
//        String outputPath = "/Users/yaya/Desktop/jedis" + d;
//        try {
////执行解压
//            decompress(jarFileName, outputPath);
//            System.out.println("Extracting OK!");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Extracting File Failed!");
//            dealError(outputPath);
//            System.out.println("Installing Portal Failed");
//            return;
//        }
//
////        String systemName = System.getProperty("os.name");
////        System.out.println("System is " + systemName);
//////如果是unix系列操作系统则赋予用户可执行权限
////        if (!systemName.toLowerCase().contains("windows")) {
////            System.out.println("Start Granting User Excecute Rights......");
////            try {
////                Process p1 = Runtime.getRuntime().exec("chmod +x portal.sh");
////                p1.waitFor();
////                Process p2 = Runtime.getRuntime().exec("portal.sh");
////                p2.waitFor();
////                System.out.println("Granting User Excecute Rights OK!");
////            } catch (Exception e) {
////                e.printStackTrace();
////                System.out.println("Granting User Excecute Rights Failed!");
////                dealError(outputPath);
////                System.out.println("Installing Portal Failed");
////                return;
////            }
////        }
//    }
//
//
//    /**
//     * 解压缩JAR包
//     *
//     * @param fileName   文件名
//     * @param outputPath 解压输出路径
//     * @throws IOException IO异常
//     */
//    private void decompress(String fileName, String outputPath) throws IOException {
//
//        if (!outputPath.endsWith(File.separator)) {
//
//            outputPath += File.separator;
//
//        }
//
//        JarFile jf = new JarFile(fileName);
//
//        for (Enumeration e = jf.entries(); e.hasMoreElements(); ) {
//            JarEntry je = (JarEntry) e.nextElement();
//            String outFileName = outputPath + je.getName();
//            File f = new File(outFileName);
//            System.out.println(f.getAbsolutePath());
//
////创建该路径的目录和所有父目录
//            makeSupDir(outFileName);
//
////如果是目录,则直接进入下一个循环
//            if (f.isDirectory()) {
//                continue;
//            }
//
//            InputStream in = null;
//            OutputStream out = null;
//
//            try {
//                in = jf.getInputStream(je);
//                out = new BufferedOutputStream(new FileOutputStream(f));
//                byte[] buffer = new byte[2048];
//                int nBytes = 0;
//                while ((nBytes = in.read(buffer)) > 0) {
//                    out.write(buffer, 0, nBytes);
//                }
//            } catch (IOException ioe) {
//                throw ioe;
//            } finally {
//                try {
//                    if (null != out) {
//                        out.flush();
//                        out.close();
//                    }
//                } catch (IOException ioe) {
//                    throw ioe;
//                } finally {
//                    if (null != in) {
//                        in.close();
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 循环创建父目录
//     *
//     * @param outFileName
//     */
//    private void makeSupDir(String outFileName) {
////匹配分隔符
//        Pattern p = Pattern.compile("[///" + File.separator + "]");
//        Matcher m = p.matcher(outFileName);
////每找到一个匹配的分隔符,则创建一个该分隔符以前的目录
//        while (m.find()) {
//            int index = m.start();
//            String subDir = outFileName.substring(0, index);
//            File subDirFile = new File(subDir);
//            if (!subDirFile.exists())
//                subDirFile.mkdir();
//        }
//    }
//
//    /**
//     * 递归删除目录及子目录
//     *
//     * @param path
//     */
//    private void clean(String path) throws IOException {
//        File file = new File(path);
////如果该路径不存在
//        if (!file.exists()) {
//            System.out.println(path + " Not Exist!");
//        } else {
////如果是目录,则递归删除
//            if (file.isDirectory()) {
//                String[] fileNames = file.list();
//
//                if (null == fileNames) {
//                    throw new IOException("IO ERROR While Deleting Files");
//                }
////如果是空目录则直接删除
//                else if (fileNames.length == 0) {
//                    file.delete();
//                } else {
//                    for (String fileName : fileNames) {
//                        File subFile = new File(fileName);
//                        clean(path + File.separator + subFile);
//                    }
//                    System.out.println(file.getAbsolutePath());
////最后删除父目录
//                    file.delete();
//
//                }
//            }
////如果是文件,则直接删除
//            else {
//                System.out.println(file.getAbsolutePath());
//                file.delete();
//            }
//        }
//    }
//
//    /**
//     * 处理安装错误的异常,调用清洗过程
//     *
//     * @param outputPath 输出路径
//     * @throws IOException IO异常
//     */
//    private void dealError(String outputPath) {
////删除已解压的文件
//        System.out.println("Start Deleting Files......");
//        try {
//            clean(outputPath);
//            System.out.println("Deleting Files OK!");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Deleting Files Failed!");
//        }
//    }
//
//
//}