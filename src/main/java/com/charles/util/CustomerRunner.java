package com.charles.util;


import com.charles.Enums.OperationEnum;
import com.charles.entity.CompareExcelLine;
import com.charles.entity.CompareExcl;
import com.charles.entity.FileEntry;
import com.charles.handler.BaseFillResultHandler;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.charles.util.Constants.*;
import static com.charles.util.FilesCheckUtils.*;

/**
 * @author dell
 */
public class CustomerRunner implements Runnable {

    private final FileEntry fileEntry;
    private final FileEntry localFileEntry;
    private final CountDownLatch countDownLatch;


    public CustomerRunner(FileEntry fileEntry, FileEntry localFileEntry, CountDownLatch countDownLatch) {
        this.fileEntry = fileEntry;
        this.localFileEntry = localFileEntry;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            compareStart(this.fileEntry, this.localFileEntry);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    /**
     * @param prodFileEntry  现网代码文件
     * @param localFileEntry 本地反编译代码文件
     */
    private void compareStart(FileEntry prodFileEntry, FileEntry localFileEntry) {
        String logFilePath = LOG_PREFIX + prodFileEntry.getFile().getName().replaceAll(".java|.xml|.txt", "") + ".log";
        String resultFilePath = RESULT_PREFIX + prodFileEntry.getFile().getName().replaceAll(".java|.xml|.txt", "") + ".txt";
        if (commandCompare(prodFileEntry.getFilePath(), localFileEntry.getFilePath(), logFilePath, resultFilePath)) {
            readXml(resultFilePath, prodFileEntry);
        }
    }

    /**
     * 读取结果文件
     *
     * @param resultPath    结果文件地址
     * @param prodFileEntry 现网文件对象数据
     */
    private void readXml(String resultPath, FileEntry prodFileEntry) {
        CompareExcl result = new CompareExcl();
        /*获取包名*/
        result.setFilename(prodFileEntry.getFilePath().replace(PROD_ROOT_PATH, ""));
        CompareExcelLine compareExcelLine = new CompareExcelLine();
        File file = new File(resultPath);
        String fileCharset = getFileCharsetByIcu4j(file);
        if (!StringUtils.hasText(fileCharset)) {
            fileCharset = "UTF-8";
        }
        String fileContent = readFileString(file, fileCharset);
        try {
            Document document = DocumentHelper.parseText(fileContent);
            Element rootElement = document.getRootElement();
            Element filecomparison = rootElement.element("filecomparison");
            if (null != filecomparison) {
                List<Element> linecomps = filecomparison.elements("linecomp");
                OperationEnum[] enums = OperationEnum.values();
                for (OperationEnum operation : enums) {
                    List<Element> elements = linecomps.stream().filter(element -> operation.getKey().equals(element.attributeValue("status"))).collect(Collectors.toList());
                    BaseFillResultHandler handler = BaseFillResultHandler.buildHandler(operation);
                    handler.fillResult(elements, compareExcelLine);
                }
            }
        } catch (DocumentException e) {
            System.out.println("-------error" + resultPath);
        }
        result.setCompareExcelLine(compareExcelLine);
        addResult(result);

    }

}
