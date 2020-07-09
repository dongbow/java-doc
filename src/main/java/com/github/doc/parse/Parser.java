package com.github.doc.parse;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.FieldPropDoc;
import com.github.doc.model.ObjectDoc;
import com.github.doc.model.PropType;
import com.github.doc.parse.tool.InitValue;
import com.github.doc.util.CollectionUtils;
import com.github.doc.util.TypeClassLoaderHolder;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
public interface Parser {

    /**
     * 解析方法
     *
     * @param declaration
     * @return 文档
     */
    <R extends ClassDoc, T extends TypeDeclaration> R parse(T declaration);

    /**
     * 生成基本数据
     *
     * @param declaration
     * @return 文档
     */
    default ClassDoc generateBase(TypeDeclaration declaration) {
        ClassDoc classDoc = new ClassDoc();
        classDoc.setName(declaration.getNameAsString());
        if (declaration.getComment().isPresent()) {
            classDoc.setComment(declaration.getComment().get().getContent());
        }
        if (declaration.getAnnotationByClass(Deprecated.class).isPresent()) {
            classDoc.setDeprecated(true);
        }
        if (declaration.getParentNode().orElse(null) != null) {
            CompilationUnit compilationUnit = (CompilationUnit) declaration.getParentNode().get();
            String pkg = null;
            if (compilationUnit.getPackageDeclaration().orElse(null) != null) {
                pkg = compilationUnit.getPackageDeclaration().get().getNameAsString();
            }
            classDoc.setPackageName(pkg);
            classDoc.setFullName(StringUtils.isNotEmpty(pkg) ? pkg + "." + classDoc.getName() : classDoc.getName());
            if (CollectionUtils.isNotEmpty(compilationUnit.getImports())) {
                for (ImportDeclaration anImport : compilationUnit.getImports()) {
                    classDoc.getImports().add(anImport.getNameAsString());
                }
            }
        }
        return classDoc;
    }

    /**
     * 设置继承
     *
     * @param classDoc
     * @param classOrInterfaceType
     */
    default void setExtendsName(ClassDoc classDoc, ClassOrInterfaceType classOrInterfaceType) {
        String extendsName = "";
        if (classOrInterfaceType.getScope().isPresent()) {
            extendsName = classOrInterfaceType.getScope().get().getNameAsString() + "." + classOrInterfaceType.getNameAsString();
        } else {
            boolean isImport = false;
            for (String anImport : classDoc.getImports()) {
                if (Objects.equals(anImport.substring(anImport.lastIndexOf(".") + 1), classOrInterfaceType.getNameAsString())) {
                    extendsName = anImport;
                    isImport = true;
                    break;
                }
            }
            if (!isImport) {
                try {
                    extendsName = TypeClassLoaderHolder.tryGet(classOrInterfaceType.getNameAsString());
                } catch (ClassNotFoundException e) {
                    extendsName = classDoc.getPackageName() + "." + classOrInterfaceType.getNameAsString();
                }
            }
        }
        classDoc.setExtendsFullName(extendsName);
    }

    /**
     * 设置属性
     *
     * @param classDoc
     * @param list
     */
    default void setFieldInfo(ObjectDoc classDoc, NodeList<BodyDeclaration<?>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (BodyDeclaration<?> member : list) {
            if (member instanceof FieldDeclaration) {
                FieldDeclaration field = (FieldDeclaration) member;
                if (field.hasModifier(Modifier.Keyword.STATIC)) {
                    continue;
                }
                FieldPropDoc propDoc = new FieldPropDoc();
                VariableDeclarator variableDeclarator = field.getVariables().get(0);
                propDoc.setName(variableDeclarator.getNameAsString());
                propDoc.setType(variableDeclarator.getTypeAsString());
                classDoc.setFieldClassSet(variableDeclarator.getType());
                propDoc.setPropType(PropType.parse(variableDeclarator.getType(), classDoc));

                if (variableDeclarator.getInitializer().isPresent()) {
                    propDoc.setDefaultValue(InitValue.parseValue(variableDeclarator.getInitializer().get()));
                }
                if (field.getComment().isPresent()) {
                    propDoc.parseComment(field.getComment().get().getContent(), classDoc);
                }
                if (field.getAnnotationByClass(Deprecated.class).isPresent()) {
                    propDoc.setDeprecated(true);
                }
                classDoc.getPropDocs().add(propDoc);
            }
        }
    }

}
