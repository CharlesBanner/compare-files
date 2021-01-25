package com.charles;

import com.alibaba.excel.EasyExcel;
import com.charles.entity.CompareExcl;
import com.charles.entity.FileEntry;
import com.charles.entity.ResultExcel;
import com.charles.util.CustomerRunner;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.charles.util.Constants.*;
import static com.charles.util.FilesCheckUtils.buildFilesList;
import static com.charles.util.FilesCheckUtils.getFilesBinary;

/**
 * @author dell
 */
public class Main {

    public static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("deal-pool-%d").build();
    public static ExecutorService fixPool = new ThreadPoolExecutor(DEAL_THREAD_NUM, MAX_THREAD_NUM, 5000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(6000), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 开始
     * @param args 入参
     * @throws InterruptedException 异常
     */
    public static void main(String[] args) throws InterruptedException {
        List<FileEntry> prodFiles = buildFilesList(PROD_ROOT_PATH);
        List<FileEntry> localFiles = buildFilesList(LOCAL_ROOT_PATH);
        if (analysisFiles(prodFiles,localFiles)){
            writeExcel();
        }
        fixPool.shutdown();
    }

    /**
     * 文件解析开始
     * @param prodFiles 现网文件
     * @param localFiles 本地文件
     * @return 成功或失败
     * @throws InterruptedException 异常
     */
    public static boolean analysisFiles(List<FileEntry> prodFiles,List<FileEntry> localFiles) throws InterruptedException {
        int countFileNum = countFileNum(prodFiles, localFiles);
        CountDownLatch countDownLatch = new CountDownLatch(countFileNum);
        for (FileEntry prodFile : prodFiles) {
            for (FileEntry localFile : localFiles) {
                if (prodFile.getFile().getName().equals(localFile.getFile().getName())) {
                    if (!Arrays.equals(getFilesBinary(prodFile.getFilePath()), getFilesBinary(localFile.getFilePath()))) {
                        if (prodFile.getFile().getName().contains(".xml")) {
                            System.out.println("xml文件-----"+prodFile.getFile().getName());
                            continue;
                        }
                        fixPool.execute(new CustomerRunner(prodFile, localFile, countDownLatch));
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                }
            }

        }
        return countDownLatch.await(1000, TimeUnit.SECONDS);
    }

    /**
     * excel输出
     */
    public static void writeExcel(){
        List<ResultExcel> excelLists = Lists.newArrayList();
        for (CompareExcl compareExcl : resultList) {
            ResultExcel resultExcel = new ResultExcel();
            resultExcel.setFileName(compareExcl.getFilename());
            resultExcel.setContent(compareExcl.getCompareExcelLine().getContent().toString());
            resultExcel.setProd(compareExcl.getCompareExcelLine().getProText().toString());
            resultExcel.setLcoal(compareExcl.getCompareExcelLine().getLocalText().toString());
            excelLists.add(resultExcel);
        }
        EasyExcel.write("result.xlsx",ResultExcel.class).sheet().doWrite(excelLists);
    }

    /**
     * 计算文件数量
     * @param prodFiles
     * @param localFiles
     * @return
     */
    private static int countFileNum(List<FileEntry> prodFiles,List<FileEntry> localFiles){
        int eq = 0;
        for (FileEntry prodFile : prodFiles) {
            for (FileEntry localFile : localFiles) {
                if (prodFile.getFile().getName().equals(localFile.getFile().getName())) {
                    if (!prodFile.getFile().getName().contains(".xml")) {
                        if (!Arrays.equals(getFilesBinary(prodFile.getFilePath()), getFilesBinary(localFile.getFilePath()))) {
                            eq++;
                        }
                    }
                }
            }
        }
        return eq;
    }

}
