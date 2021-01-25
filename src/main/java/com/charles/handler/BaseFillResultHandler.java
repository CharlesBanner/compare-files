package com.charles.handler;

import com.charles.Enums.OperationEnum;
import com.charles.entity.CompareExcelLine;
import com.charles.entity.CompareExcl;
import com.charles.entity.FileEntry;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.charles.util.Constants.PROD_ROOT_PATH;
import static com.charles.util.FilesCheckUtils.*;

/**
 * @author dell
 */
public abstract class BaseFillResultHandler {

    public final OperationEnum operation;

    public static BaseFillResultHandler buildHandler(OperationEnum operation){
        BaseFillResultHandler handler = null;
        switch (operation){
            case INSERT:handler = new InsertFillResultHandler(operation);break;
            case DELETE:handler = new DeleteFillResultHandler(operation);break;
            case MODIFY:handler = new ModifyFillResultHandler(operation);break;
            default:break;
        }
        return handler;
    }

    public BaseFillResultHandler(OperationEnum operation) {
        this.operation = operation;
    }

    /**
     * 填充结果
     */
    public abstract void fillResult(List<Element> elements, CompareExcelLine compareExcelLine);
}
