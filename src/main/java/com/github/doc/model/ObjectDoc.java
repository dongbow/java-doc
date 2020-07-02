package com.github.doc.model;

import com.github.doc.util.TypeClassLoaderHolder;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdongbo
 * @since 2020/3/20.
 */
@Data
public class ObjectDoc extends ClassDoc {

    private List<FieldPropDoc> propDocs = Lists.newLinkedList();

    public static ObjectDoc fromClassDoc(ClassDoc classDoc) {
        ObjectDoc objectDoc = new ObjectDoc();
        objectDoc.setName(classDoc.getName());
        objectDoc.setPackageName(classDoc.getPackageName());
        objectDoc.setExtendsFullName(classDoc.getExtendsFullName());
        objectDoc.setComment(classDoc.getComment());
        objectDoc.setFullName(classDoc.getFullName());
        objectDoc.setInterface(classDoc.isInterface());
        objectDoc.setImports(classDoc.getImports());
        objectDoc.setClassFilePath(classDoc.getClassFilePath());
        objectDoc.setDeprecated(classDoc.isDeprecated());
        return objectDoc;
    }

    public static ClassDoc copy(ObjectDoc classDoc) {
        ObjectDoc objectDoc = new ObjectDoc();
        objectDoc.setName(classDoc.getName());
        objectDoc.setExtendsFullName(classDoc.getExtendsFullName());
        objectDoc.setComment(classDoc.getComment());
        objectDoc.setFullName(classDoc.getFullName());
        objectDoc.setInterface(classDoc.isInterface());
        objectDoc.setImports(classDoc.getImports());
        objectDoc.setPropDocs(copy(classDoc.getPropDocs()));
        objectDoc.setClassFilePath(classDoc.getClassFilePath());
        objectDoc.setPackageName(classDoc.getPackageName());
        objectDoc.setDeprecated(classDoc.isDeprecated());
        return objectDoc;
    }

    protected static List<FieldPropDoc> copy(List<FieldPropDoc> list) {
        return list.stream().map(FieldPropDoc::copy).collect(Collectors.toList());
    }

    public void setFieldClassSet(Type type) {
        if (!(type instanceof ClassOrInterfaceType)) {
            return;
        }
        ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) type;

        String fieldClass = "";
        if (classOrInterfaceType.getScope().isPresent()) {
            fieldClass = classOrInterfaceType.getScope().get().getNameAsString() + "." + classOrInterfaceType.getNameAsString();
        } else {
            boolean isImport = false;
            for (String anImport : this.getImports()) {
                if (Objects.equals(anImport.substring(anImport.lastIndexOf(".") + 1), classOrInterfaceType.getNameAsString())) {
                    fieldClass = anImport;
                    isImport = true;
                    break;
                }
            }
            if (!isImport) {
                try {
                    fieldClass = TypeClassLoaderHolder.tryGet(classOrInterfaceType.getNameAsString());
                } catch (ClassNotFoundException e) {
                    fieldClass = this.getPackageName() + "." + classOrInterfaceType.getNameAsString();
                }
            }
        }
        this.getImports().add(fieldClass);
        if (classOrInterfaceType.getTypeArguments().isPresent()) {
            for (Type tmp : classOrInterfaceType.getTypeArguments().get()) {
                if (!(tmp instanceof ClassOrInterfaceType)) {
                    continue;
                }
                setFieldClassSet(tmp);
            }
        }
    }
}
