package com.github.doc.parse;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.InterfaceDoc;
import com.github.doc.model.MethodParamDoc;
import com.github.doc.model.MethodPropDoc;
import com.github.doc.util.CollectionUtils;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
public class InterfaceParser implements Parser {

    @Override
    public <R extends ClassDoc, T extends TypeDeclaration> R parse(T declaration) {
        if (!(declaration instanceof ClassOrInterfaceDeclaration)) {
            return null;
        }
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) declaration;
        if (!classOrInterfaceDeclaration.isInterface()) {
            return null;
        }
        ClassDoc classDoc = generateBase(classOrInterfaceDeclaration);
        classDoc.setInterface(true);
        if (CollectionUtils.isNotEmpty(classOrInterfaceDeclaration.getExtendedTypes())) {
            setExtendsName(classDoc, classOrInterfaceDeclaration.getExtendedTypes(0));
        }
        InterfaceDoc interfaceDoc = InterfaceDoc.fromClassDoc(classDoc);
        for (BodyDeclaration member : classOrInterfaceDeclaration.getMembers()) {
            if (member instanceof MethodDeclaration) {
                MethodDeclaration field = (MethodDeclaration) member;
                MethodPropDoc propDoc = new MethodPropDoc();
                propDoc.setName(field.getNameAsString());
                String specialName = "";
                if (CollectionUtils.isNotEmpty(field.getTypeParameters())) {
                    specialName = "<";
                    specialName += Joiner.on(", ").join(field.getTypeParameters());
                    specialName += "> ";
                }
                String methodName = specialName + field.getType().toString() + " " + field.getName() + "(";
                if (CollectionUtils.isEmpty(field.getParameters())) {
                    methodName += ")";
                } else {
                    methodName += (Joiner.on(", ").join(field.getParameters()) + ")");
                }
                propDoc.setMethodName(methodName);
                if (field.getComment().isPresent()) {
                    propDoc.parse(field.getComment().get().getContent());
                }
                if (CollectionUtils.isNotEmpty(field.getParameters())) {
                    Map<String, MethodParamDoc> paramMap = propDoc.getMethodParamDocList().stream().collect(Collectors.toMap(MethodParamDoc::getName, m -> m));
                    List<MethodParamDoc> methodParamDocs = Lists.newLinkedList();
                    field.getParameters().forEach(s -> {
                        boolean isDeprecated = false;
                        if (s.getAnnotationByClass(Deprecated.class).isPresent()) {
                            isDeprecated = true;
                        }
                        MethodParamDoc methodParamDoc = paramMap.get(s.getNameAsString());
                        if (methodParamDoc != null) {
                            methodParamDoc.setDeprecated(isDeprecated);
                        } else {
                            methodParamDoc = new MethodParamDoc(isDeprecated, s.getNameAsString(), "");
                        }
                        methodParamDocs.add(methodParamDoc);
                    });
                    propDoc.setMethodParamDocList(methodParamDocs);
                }
                if (field.getAnnotationByClass(Deprecated.class).isPresent()) {
                    propDoc.setDeprecated(true);
                }
                interfaceDoc.getPropDocs().add(propDoc);
            }
        }
        return (R) interfaceDoc;
    }
}
