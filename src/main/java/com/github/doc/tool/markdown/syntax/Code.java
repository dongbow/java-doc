package com.github.doc.tool.markdown.syntax;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wangdongbo
 * @since 2020/3/24.
 */
public class Code {

    public static String toMarkDown(List<String> lines, String codeType) {
        List<String> contents = Lists.newArrayList("```" + codeType);
        contents.addAll(lines);
        contents.add("```");
        return Joiner.on("\n").join(contents);
    }

}
