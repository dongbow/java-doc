package com.github.doc.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author wangdongbo
 * @since 2020/3/20.
 */
@Data
public class InterfaceDoc extends ClassDoc {

    private List<MethodPropDoc> propDocs = Lists.newLinkedList();

    public static InterfaceDoc fromClassDoc(ClassDoc classDoc) {
        InterfaceDoc interfaceDoc = new InterfaceDoc();
        interfaceDoc.setInterface(classDoc.isInterface());
        interfaceDoc.setName(classDoc.getName());
        interfaceDoc.setPackageName(classDoc.getPackageName());
        interfaceDoc.setFullName(classDoc.getFullName());
        interfaceDoc.setComment(classDoc.getComment());
        interfaceDoc.setExtendsFullName(classDoc.getExtendsFullName());
        interfaceDoc.setImports(classDoc.getImports());
        interfaceDoc.setClassFilePath(classDoc.getClassFilePath());
        interfaceDoc.setDeprecated(classDoc.isDeprecated());
        return interfaceDoc;
    }
}
