package com.github.doc.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
@Data
public class EnumDoc extends ObjectDoc {

    private List<String> enumValues = Lists.newLinkedList();

    public static EnumDoc fromClassDoc(ClassDoc classDoc) {
        EnumDoc enumDoc = new EnumDoc();
        enumDoc.setComment(classDoc.getComment());
        enumDoc.setName(classDoc.getName());
        enumDoc.setFullName(classDoc.getFullName());
        enumDoc.setExtendsFullName(classDoc.getExtendsFullName());
        enumDoc.setInterface(classDoc.isInterface());
        enumDoc.setImports(classDoc.getImports());
        enumDoc.setPackageName(classDoc.getPackageName());
        enumDoc.setClassFilePath(classDoc.getClassFilePath());
        enumDoc.setDeprecated(classDoc.isDeprecated());
        return enumDoc;
    }

    public static ClassDoc copy(EnumDoc classDoc) {
        EnumDoc enumDoc = new EnumDoc();
        enumDoc.setComment(classDoc.getComment());
        enumDoc.setName(classDoc.getName());
        enumDoc.setPackageName(classDoc.getPackageName());
        enumDoc.setClassFilePath(classDoc.getClassFilePath());
        enumDoc.setFullName(classDoc.getFullName());
        enumDoc.setExtendsFullName(classDoc.getExtendsFullName());
        enumDoc.setInterface(classDoc.isInterface());
        enumDoc.setImports(classDoc.getImports());
        enumDoc.setEnumValues(classDoc.getEnumValues());
        enumDoc.setPropDocs(copy(classDoc.getPropDocs()));
        enumDoc.setDeprecated(classDoc.isDeprecated());
        return enumDoc;
    }
}
