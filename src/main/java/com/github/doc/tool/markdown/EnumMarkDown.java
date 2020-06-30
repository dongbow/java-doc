package com.github.doc.tool.markdown;

import com.github.doc.model.EnumDoc;
import com.github.doc.model.FieldPropDoc;
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
public class EnumMarkDown {

    private static final List<String> TITLES = Lists.newArrayList("属性", "类型", "描述");

    public static String generate(EnumDoc enumDoc) {
        return generate(enumDoc, 2);
    }

    public static String generate(EnumDoc enumDoc, int titleSize) {
        List<String> lines = Lists.newArrayList();
        String className = enumDoc.isDeprecated() ? Font.Delete(enumDoc.getName()) : enumDoc.getName();
        lines.add(Font.titleRoute(titleSize, className + (StringUtils.isNotBlank(enumDoc.getComment()) ? (" - " + StringUtil.encodeStr(enumDoc.getComment())) : "")));
        lines.add(Font.Bold("文件位置：") + enumDoc.getClassFilePath());
        lines.add(Font.Bold("完整名称：") + enumDoc.getFullName());
        lines.add(Font.Bold("枚举值列表："));
        lines.add(Code.toMarkDown(enumDoc.getEnumValues(), "java"));
        if (CollectionUtils.isNotEmpty(enumDoc.getPropDocs())) {
            lines.add(Font.Bold("属性列表："));
            List<List<String>> contents = Lists.newArrayListWithCapacity(enumDoc.getPropDocs().size());
            for (FieldPropDoc propDoc : enumDoc.getPropDocs()) {
                String name = propDoc.getName();
                if (propDoc.isDeprecated()) {
                    name = Font.Delete(name);
                }
                contents.add(Lists.newArrayList(StringUtil.encodeStr(name), StringUtil.encodeStr(propDoc.getType()), StringUtils.isNotBlank(propDoc.getComment()) ? StringUtil.encodeStr(propDoc.getComment()) : ""));
            }
            lines.add(Table.table(TITLES, contents));
        }
        return Joiner.on("\n").join(lines);
    }

}
