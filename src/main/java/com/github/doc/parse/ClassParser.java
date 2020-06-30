package com.github.doc.parse;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.ObjectDoc;
import com.github.doc.util.CollectionUtils;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
public class ClassParser implements Parser {

    @Override
    public <R extends ClassDoc, T extends TypeDeclaration> R parse(T declaration) {
        if (!(declaration instanceof ClassOrInterfaceDeclaration)) {
            return null;
        }
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) declaration;
        if (classOrInterfaceDeclaration.isInterface()) {
            return null;
        }
        ClassDoc classDoc = generateBase(classOrInterfaceDeclaration);
        if (CollectionUtils.isNotEmpty(classOrInterfaceDeclaration.getExtendedTypes())) {
            setExtendsName(classDoc, classOrInterfaceDeclaration.getExtendedTypes(0));
        }
        ObjectDoc objectDoc = ObjectDoc.fromClassDoc(classDoc);
        setFieldInfo(objectDoc, classOrInterfaceDeclaration.getMembers());
        return (R) objectDoc;
    }
}
