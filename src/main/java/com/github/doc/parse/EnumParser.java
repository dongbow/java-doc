package com.github.doc.parse;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.EnumDoc;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
public class EnumParser implements Parser {

    @Override
    public <R extends ClassDoc, T extends TypeDeclaration> R parse(T declaration) {
        if (!(declaration instanceof EnumDeclaration)) {
            return null;
        }
        EnumDeclaration enumDeclaration = (EnumDeclaration) declaration;
        EnumDoc enumDoc = EnumDoc.fromClassDoc(generateBase(enumDeclaration));
        for (EnumConstantDeclaration entry : enumDeclaration.getEntries()) {
            if (entry.getAnnotationByClass(Deprecated.class).isPresent()) {
                enumDoc.getEnumValues().add("@Deprecated");
            }
            enumDoc.getEnumValues().add(entry.getTokenRange().get().toString());
        }
        setFieldInfo(enumDoc, enumDeclaration.getMembers());
        return (R) enumDoc;
    }
}
