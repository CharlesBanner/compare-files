package com.charles.util;

import com.charles.entity.CompareExcl;
import com.charles.entity.FileEntry;
import com.google.common.collect.Lists;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.charles.util.Constants.resultList;

/**
 * @author dell
 */
public class FilesCheckUtils {



    public synchronized static void addResult(CompareExcl compareExcl) {
        resultList.add(compareExcl);
    }

    public static String getFileCharsetByIcu4j(File file) {
        String encoding = null;

        try {
            Path path = Paths.get(file.getPath());
            byte[] data = Files.readAllBytes(path);
            CharsetDetector detector = new CharsetDetector();
            detector.setText(data);
            CharsetMatch match = detector.detect();
            if (match == null) {
                return encoding;
            }

            encoding = match.getName();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return encoding;
    }

    public static boolean commandCompare(String prodFilePath, String localFilePath, String logPath, String resultPath) {
        String[] params = new String[]{"D:\\Program Files\\Beyond Compare 4\\BCompare.exe",
                "@D:\\bcscript.txt",
                logPath,
                resultPath,
                prodFilePath,
                localFilePath
        };
        int returnCd = run(params);
        if (returnCd != 0) {
            System.err.println("[returnCd]:" + returnCd+"------"+prodFilePath);
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }


    public static int run(String... array) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(array);
            Process process = builder.start();
            InputStream inSTest = process.getErrorStream();
            InputStreamReader reader = new InputStreamReader(inSTest);
            BufferedReader bfReader = new BufferedReader(reader);
            String strLine = "";
            while ((strLine = bfReader.readLine()) != null) {
                System.out.println(strLine);
            }
            bfReader.close();
            process.waitFor();
            process.destroy();
            return process.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -10000;
    }

    public static String readFileString(File file, String charset) {
        StringBuilder fileStringBuilder = new StringBuilder();
        FileInputStream fin = null;
        InputStreamReader in = null;
        try {
            //获取文件
            fin = new FileInputStream(file);
            //使用字符转换流  来设置编码格式
            in = new InputStreamReader(fin, charset);
            //使用char数组接收
            char[] buffer = new char[1024];
            int length = 0;
            //获取文件内容保存到 context 变量中

            while ((length = in.read(buffer)) != -1) {
                fileStringBuilder.append(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流       流必须关闭
            try {
                assert in != null;
                in.close();
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileStringBuilder.toString();
    }

    public static List<FileEntry> buildFilesList(String path) {
        List<FileEntry> fileNames = Lists.newArrayList();
        File file = new File(path);
        func(file, fileNames);
        return fileNames;
    }

    /**
     * 递归遍历指定文件夹，获取所有文件添加到返回Map中
     *
     * @param file         当前文件
     * @param fileNames 文件名称列表
     */
    public static void func(File file, List<FileEntry> fileNames) {
        File[] fs = file.listFiles();
        assert fs != null;
        for (File f : fs) {
            /*若是目录，则递归打印该目录下的文件*/
            if (f.isDirectory()) {
                func(f, fileNames);
            }
            /*若是文件，添加*/
            if (f.isFile()) {
                fileNames.add(new FileEntry(f.getPath(), f));
            }
        }
    }

    /**
     * 获取文件二进制数据
     * @param fileName 文件地址
     * @return 文件二进制
     */
    public static byte[] getFilesBinary(String fileName) {
        //字节输入流
        FileInputStream fin = null;
        //字节输入缓冲流：先从磁盘中将要读取的内容放入到内存中，再一次性从内存中取出来，避免了读一段取一段
        BufferedInputStream bin = null;
        //字节输出缓冲流：先将要输出的内容放入到内存中，再一次性全都输出。
        BufferedOutputStream bout = null;
        //字节数组输出流,将字节数据写入到字节数组中
        ByteArrayOutputStream baos = null;

        try {
            //建立读取文件的文件输出流
            fin = new FileInputStream(fileName);
            //在文件流上安装节点流（更大效率读取）
            bin = new BufferedInputStream(fin);
            //创建一个新的byte数组输出流，它具有指定大小的缓冲区容量
            baos = new ByteArrayOutputStream();
            //创建一个新的缓冲输出流，将以数据写入指定的底层出入流
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[2048];
            int len = bin.read(buffer);
            while (len != -1) {
                //void write(byte[] b, int off, int len)
                //将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此输出流
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流，并强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fin != null;
                fin.close();
                assert bin != null;
                bin.close();
                assert bout != null;
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
