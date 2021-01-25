package com.charles.handler;

import com.charles.Enums.OperationEnum;
import com.charles.entity.CompareExcelLine;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author dell
 */
public class ModifyFillResultHandler extends BaseFillResultHandler{
    public ModifyFillResultHandler(OperationEnum operation) {
        super(operation);
    }

    @Override
    public void fillResult(List<Element> elements, CompareExcelLine compareExcelLine) {
        if (CollectionUtils.isNotEmpty(elements)) {
            for (Element element : elements) {
                List<Element> texts = element.elements("text");
                if (CollectionUtils.isNotEmpty(texts)) {
                    for (Element text : texts) {
                        if (!compareExcelLine.getContent().toString().contains(operation.getOperation())) {
                            compareExcelLine.getContent().append(operation.getOperation()).append(text.getStringValue()).append(System.lineSeparator());
                        }
                        if (StringUtils.hasText(text.attributeValue("ltid"))) {
                            compareExcelLine.getProText().append(text.getStringValue()).append(System.lineSeparator());
                        }
                        if (StringUtils.hasText(text.attributeValue("rtid"))) {
                            compareExcelLine.getLocalText().append(text.getStringValue()).append(System.lineSeparator());
                        }
                    }
                }
            }
        }
    }
}
