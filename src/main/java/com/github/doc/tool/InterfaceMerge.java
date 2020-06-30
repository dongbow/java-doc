package com.github.doc.tool;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.EnumDoc;
import com.github.doc.model.InterfaceDoc;
import com.github.doc.model.MethodPropDoc;
import com.github.doc.model.ObjectDoc;
import com.github.doc.model.merge.MergeInterfaceDoc;
import com.github.doc.model.merge.MethodDoc;
import com.github.doc.util.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
public class InterfaceMerge {

    private static final int MAX_DEEP = 10;

    public static <R extends ClassDoc> List<MergeInterfaceDoc> merge(List<R> classDocs) {
        if (CollectionUtils.isEmpty(classDocs)) {
            return Collections.emptyList();
        }
        List<InterfaceDoc> interfaceDocList = Lists.newArrayList();
        Map<String, ClassDoc> fullNameClassDocMap = Maps.newHashMap();
        Map<String, List<ClassDoc>> nameClassDocMap = Maps.newHashMap();
        Set<String> extendsNameSet = Sets.newHashSet();
        for (R classDoc : classDocs) {
            if (StringUtils.isNotBlank(classDoc.getExtendsFullName())) {
                extendsNameSet.add(classDoc.getExtendsFullName());
            }
            fullNameClassDocMap.put(classDoc.getFullName(), classDoc);
            nameClassDocMap.computeIfAbsent(classDoc.getName(), key -> Lists.newArrayList()).add(classDoc);
            if (classDoc instanceof InterfaceDoc) {
                interfaceDocList.add((InterfaceDoc) classDoc);
            }
        }
        if (CollectionUtils.isEmpty(interfaceDocList)) {
            return Collections.emptyList();
        }
        List<MergeInterfaceDoc> mergeInterfaceDocList = Lists.newLinkedList();
        Map<String, InterfaceDoc> extendsMap = Maps.newHashMap();
        for (InterfaceDoc interfaceDoc : interfaceDocList) {
            if (extendsMap.containsKey(interfaceDoc.getName())) {
                continue;
            }

            Map<String, ClassDoc> fullNameRelationClassDocMap = Maps.newHashMap();

            MergeInterfaceDoc mergeInterfaceDoc = new MergeInterfaceDoc();
            mergeInterfaceDoc.setName(interfaceDoc.getName());
            mergeInterfaceDoc.setFullName(interfaceDoc.getFullName());
            mergeInterfaceDoc.setFileClassPath(interfaceDoc.getClassFilePath());
            mergeInterfaceDoc.setComment(interfaceDoc.getComment());
            mergeInterfaceDoc.setDeprecated(interfaceDoc.isDeprecated());
            List<MethodPropDoc> propDocs = Lists.newArrayList(interfaceDoc.getPropDocs());
            Set<String> imports = Sets.newHashSet(interfaceDoc.getImports());

            if (StringUtils.isNotBlank(interfaceDoc.getExtendsFullName())) {
                parseAllInterface(fullNameClassDocMap, propDocs, imports, extendsMap, interfaceDoc.getExtendsFullName(), 1);
            }

            if (CollectionUtils.isEmpty(propDocs)) {
                continue;
            }

            for (MethodPropDoc propDoc : propDocs) {
                MethodDoc methodDoc = new MethodDoc();
                methodDoc.setDeprecated(propDoc.isDeprecated());
                methodDoc.setShortMethod(propDoc.getName());
                methodDoc.setMethod(propDoc.getMethodName());
                methodDoc.setMethodComment(propDoc.getComment());
                methodDoc.setMethodParamDocList(propDoc.getMethodParamDocList());
                methodDoc.setReturnComment(propDoc.getReturnComment());
                mergeInterfaceDoc.getMethodDocList().add(methodDoc);
            }

            Set<String> interfaceImports = Sets.newHashSet(interfaceDoc.getImports());

            Set<String> otherImports = Sets.newHashSet();
            Set<String> tmpAll = imports;
            for (int i = 0; i < MAX_DEEP; i++) {
                Set<String> tmp = Sets.newHashSet();
                parseRelationClass(tmpAll, fullNameClassDocMap, interfaceImports, extendsNameSet, fullNameRelationClassDocMap, mergeInterfaceDoc, true, tmp);
                if (CollectionUtils.isEmpty(tmp)) {
                    break;
                }
                tmpAll = tmp;
                otherImports.addAll(tmp);
            }
            if (CollectionUtils.isNotEmpty(otherImports)) {
                parseRelationClass(otherImports, fullNameClassDocMap, interfaceImports, extendsNameSet, fullNameRelationClassDocMap, mergeInterfaceDoc, false, otherImports);
            }
            mergeInterfaceDocList.add(mergeInterfaceDoc);
        }
        return mergeInterfaceDocList;
    }

    private static void parseAllInterface(Map<String, ClassDoc> fullNameClassDocMap, List<MethodPropDoc> propDocs, Set<String> imports, Map<String, InterfaceDoc> extendsMap, String extendsName, int deep) {
        if (deep > MAX_DEEP) {
            return;
        }
        ClassDoc classDoc = fullNameClassDocMap.get(extendsName);
        if (classDoc instanceof InterfaceDoc) {
            InterfaceDoc tmp = (InterfaceDoc) classDoc;
            propDocs.addAll(tmp.getPropDocs());
            imports.addAll(tmp.getImports());
            extendsMap.putIfAbsent(extendsName, (InterfaceDoc) classDoc);
            if (StringUtils.isNotBlank(tmp.getExtendsFullName())) {
                parseAllInterface(fullNameClassDocMap, propDocs, imports, extendsMap, tmp.getExtendsFullName(), deep + 1);
            }
        }
    }

    private static void parseRelationClass(Set<String> imports, Map<String, ClassDoc> fullNameClassDocMap, Set<String> interfaceImports, Set<String> extendsNameSet, Map<String, ClassDoc> fullNameRelationClassDocMap,
                                           MergeInterfaceDoc mergeInterfaceDoc, boolean collectOtherImports, Set<String> otherImports) {
        for (String anImport : imports) {
            if (!fullNameClassDocMap.containsKey(anImport)) {
                continue;
            }
            ClassDoc classDoc = fullNameClassDocMap.get(anImport);
            if (!interfaceImports.contains(anImport) && extendsNameSet.contains(anImport)) {
                continue;
            }
            if (classDoc instanceof InterfaceDoc || fullNameRelationClassDocMap.putIfAbsent(classDoc.getFullName(), classDoc) != null) {
                continue;
            }

            if (classDoc instanceof EnumDoc) {
                mergeInterfaceDoc.getRelationClassDocList().add(EnumDoc.copy((EnumDoc) classDoc));
                continue;
            }
            if (classDoc instanceof ObjectDoc) {
                ObjectDoc objectDoc = (ObjectDoc) classDoc;
                if (StringUtils.isNotBlank(classDoc.getExtendsFullName())) {
                    parseAllObject(objectDoc, fullNameClassDocMap, classDoc.getExtendsFullName(), 1);
                }
                if (collectOtherImports) {
                    otherImports.addAll(objectDoc.getImports());
                }
                mergeInterfaceDoc.getRelationClassDocList().add(ObjectDoc.copy(objectDoc));
            }
        }
    }

    private static void parseAllObject(ObjectDoc objectDoc, Map<String, ClassDoc> fullNameClassDocMap, String extendsName, int deep) {
        if (deep > MAX_DEEP) {
            return;
        }
        ClassDoc classDoc = fullNameClassDocMap.get(extendsName);
        if (classDoc instanceof ObjectDoc) {
            ObjectDoc tmp = (ObjectDoc) classDoc;
            objectDoc.getImports().addAll(tmp.getImports());
            objectDoc.getPropDocs().addAll(tmp.getPropDocs());
            if (StringUtils.isNotBlank(tmp.getExtendsFullName())) {
                parseAllObject(objectDoc, fullNameClassDocMap, tmp.getExtendsFullName(), deep + 1);
            }
        }
    }

}
