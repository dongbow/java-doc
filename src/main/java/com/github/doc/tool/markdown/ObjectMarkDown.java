package com.github.doc.tool.markdown;

import com.github.doc.model.FieldPropDoc;
import com.github.doc.model.ObjectDoc;
import com.github.doc.tool.markdown.syntax.Font;
import com.github.doc.tool.markdown.syntax.Table;
import com.github.doc.util.CollectionUtils;
import com.github.doc.util.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdongbo
 * @since 2020/3/23.
 */
public class ObjectMarkDown {

    private static final List<String> TITLES = Lists.newArrayList("属性", "类型", "描述", "默认值");

    public static String generate(ObjectDoc objectDoc) {
        return generate(objectDoc, 2);
    }

    public static String generate(ObjectDoc objectDoc, int titleSize) {
        List<String> lines = Lists.newArrayList();
        String className = objectDoc.isDeprecated() ? Font.Delete(objectDoc.getName()) : objectDoc.getName();
        lines.add(Font.titleRoute(titleSize, className + (StringUtils.isNotBlank(objectDoc.getComment()) ? (" - " + StringUtil.encodeStr(objectDoc.getComment())) : "")));
        lines.add(Font.Bold("文件位置：") + objectDoc.getClassFilePath());
        lines.add(Font.Bold("完整名称：") + objectDoc.getFullName());
        if (CollectionUtils.isNotEmpty(objectDoc.getPropDocs())) {
            lines.add(Font.Bold("属性列表："));
            List<List<String>> contents = Lists.newArrayListWithCapacity(objectDoc.getPropDocs().size());
            for (FieldPropDoc propDoc : objectDoc.getPropDocs()) {
                String name = propDoc.getName();
                if (propDoc.isDeprecated()) {
                    name = Font.Delete(name);
                }
                contents.add(Lists.newArrayList(StringUtil.encodeStr(name), StringUtil.encodeStr(propDoc.getType()), StringUtils.isNotBlank(propDoc.getComment()) ? StringUtil.encodeStr(propDoc.getComment()) : "", Objects.nonNull(propDoc.getDefaultValue()) ? StringUtil.encodeStr(String.valueOf(propDoc.getDefaultValue())) : ""));
            }
            lines.add(Table.table(TITLES, contents));
        }
        return Joiner.on("\n").join(lines);
    }

}
