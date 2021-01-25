package com.charles.handler;

import com.charles.Enums.OperationEnum;
import com.charles.entity.CompareExcelLine;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;

import java.util.List;

/**
 * @author dell
 */
public class InsertFillResultHandler extends BaseFillResultHandler{
    public InsertFillResultHandler(OperationEnum operation) {
        super(operation);
    }

    @Override
    public void fillResult(List<Element> elements, CompareExcelLine compareExcelLine) {
        if (CollectionUtils.isNotEmpty(elements)) {
            for (Element element : elements) {
                compareExcelLine.getProText().append(element.getStringValue()).append(System.lineSeparator());
                if (!compareExcelLine.getContent().toString().contains(operation.getOperation())) {
                    compareExcelLine.getContent().append(operation.getOperation()).append(System.lineSeparator());
                }
                compareExcelLine.getContent().append(element.getStringValue()).append(System.lineSeparator());
            }
        }
    }
}
