package com.github.doc.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdongbo
 * @since 2020/3/24.
 */
public class StringUtil {

    public static String encodeStr(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        return content.replace("<", "&#60;").replace(">", "&#62;");
    }

}
