package com.github.doc.tool.markdown;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.EnumDoc;
import com.github.doc.model.ObjectDoc;
import com.github.doc.model.merge.MergeInterfaceDoc;
import com.github.doc.model.merge.MethodDoc;
import com.github.doc.tool.markdown.syntax.Code;
import com.github.doc.tool.markdown.syntax.Font;
import com.github.doc.tool.markdown.syntax.Table;
import com.github.doc.util.CollectionUtils;
import com.github.doc.util.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author wangdongbo
 * @since 2020/3/23.
 */
public class InterfaceMergeMarkDown {

    private static final List<String> METHOD_PARAM_TITLES = Lists.newArrayList("参数", "含义");

    public static String generate(MergeInterfaceDoc mergeInterfaceDoc) {
        List<String> lines = Lists.newArrayList();
        String className = mergeInterfaceDoc.isDeprecated() ? Font.Delete(mergeInterfaceDoc.getName()) : mergeInterfaceDoc.getName();
        lines.add(Font.title2(className + (StringUtils.isNotBlank(mergeInterfaceDoc.getComment()) ? (" - " + StringUtil.encodeStr(mergeInterfaceDoc.getComment())) : "")));
        lines.add(Font.Bold("文件位置：") + mergeInterfaceDoc.getFileClassPath());
        lines.add(Font.Bold("完整名称：") + mergeInterfaceDoc.getFullName());
        lines.add(Font.title3("方法列表"));
        for (MethodDoc methodDoc : mergeInterfaceDoc.getMethodDocList()) {
            String methodName = methodDoc.isDeprecated() ? Font.Delete(methodDoc.getShortMethod()) : methodDoc.getShortMethod();
            lines.add(Font.title4(Font.Bold(methodName + (StringUtils.isNotBlank(methodDoc.getMethodComment()) ? (" - " + StringUtil.encodeStr(methodDoc.getMethodComment())) : ""))));
            lines.add(Font.Bold("方法名称："));
            List<String> methodCode = Lists.newArrayList();
            if (methodDoc.isDeprecated()) {
                methodCode.add("@Deprecated");
            }
            methodCode.add(methodDoc.getMethod());
            lines.add(Code.toMarkDown(methodCode, "java"));
            lines.add(Font.Bold("返回描述：") + StringUtil.encodeStr(methodDoc.getReturnComment()));
            if (CollectionUtils.isNotEmpty(methodDoc.getMethodParamDocList())) {
                lines.add(Font.Bold("参数列表："));
                List<List<String>> contents = Lists.newArrayListWithCapacity(methodDoc.getMethodParamDocList().size());
                methodDoc.getMethodParamDocList().forEach(param -> {
                    String paramName = param.isDeprecated() ? Font.Delete(param.getName()) : param.getName();
                    contents.add(Lists.newArrayList(StringUtil.encodeStr(paramName), StringUtil.encodeStr(param.getComment())));
                });
                lines.add(Table.table(METHOD_PARAM_TITLES, contents));
            }
        }
        if (CollectionUtils.isNotEmpty(mergeInterfaceDoc.getRelationClassDocList())) {
            lines.add(Font.title3("涉及关联类"));
            for (ClassDoc classDoc : mergeInterfaceDoc.getRelationClassDocList()) {
                if (classDoc instanceof EnumDoc) {
                    lines.add(EnumMarkDown.generate((EnumDoc) classDoc, 4));
                } else if (classDoc instanceof ObjectDoc) {
                    lines.add(ObjectMarkDown.generate((ObjectDoc) classDoc, 4));
                }
            }
        }
        return Joiner.on("\n").join(lines);
    }

}
