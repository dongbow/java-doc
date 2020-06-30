package com.github.doc.util;

/**
 * @author wangdongbo
 * @since 2020/3/24.
 */
public class StringUtil {

    public static String encodeStr(String content) {
        return content.replace("<", "&#60;").replace(">", "&#62;");
    }

}
