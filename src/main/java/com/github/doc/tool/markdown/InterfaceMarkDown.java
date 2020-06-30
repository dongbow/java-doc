package com.github.doc.tool.markdown;

import com.github.doc.model.InterfaceDoc;
import com.github.doc.model.MethodPropDoc;
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
public class InterfaceMarkDown {

    private static final List<String> TITLES = Lists.newArrayList("参数", "含义");

    public static String generate(InterfaceDoc interfaceDoc) {
        List<String> lines = Lists.newArrayList();
        String className = interfaceDoc.isDeprecated() ? Font.Delete(interfaceDoc.getName()) : interfaceDoc.getName();
        lines.add(Font.title2(className + (StringUtils.isNotBlank(interfaceDoc.getComment()) ? (" - " + StringUtil.encodeStr(interfaceDoc.getComment())) : "")));
        lines.add(Font.Bold("文件位置：") + interfaceDoc.getClassFilePath());
        lines.add(Font.Bold("完整名称：") + interfaceDoc.getFullName());
        lines.add(Font.title3("方法列表"));
        for (MethodPropDoc propDoc : interfaceDoc.getPropDocs()) {
            String methodName = propDoc.isDeprecated() ? Font.Delete(propDoc.getName()) : propDoc.getName();
            lines.add(Font.title4(Font.Bold(methodName + (StringUtils.isNotBlank(propDoc.getComment()) ? (" - " + StringUtil.encodeStr(propDoc.getComment())) : ""))));
            lines.add(Font.Bold("方法名称：") + propDoc.getMethodName());
            lines.add(Font.Bold("返回描述：") + StringUtil.encodeStr(propDoc.getReturnComment()));
            if (CollectionUtils.isNotEmpty(propDoc.getMethodParamDocList())) {
                lines.add(Font.Bold("参数列表："));
                List<List<String>> contents = Lists.newArrayListWithCapacity(propDoc.getMethodParamDocList().size());
                propDoc.getMethodParamDocList().forEach(s -> {
                    String propName = s.isDeprecated() ? Font.Delete(s.getName()) : s.getName();
                    contents.add(Lists.newArrayList(StringUtil.encodeStr(propName), StringUtil.encodeStr(s.getComment())));
                });
                lines.add(Table.table(TITLES, contents));
            }
        }
        return Joiner.on("\n").join(lines);
    }

}
