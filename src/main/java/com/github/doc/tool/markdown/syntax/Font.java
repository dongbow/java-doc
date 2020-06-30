package com.github.doc.tool.markdown.syntax;

/**
 * @author wangdongbo
 * @since 2020/3/23.
 */
public class Font {

    public static String title1(String content) {
        return "# " + content;
    }

    public static String title2(String content) {
        return "## " + content;
    }

    public static String title3(String content) {
        return "### " + content;
    }

    public static String title4(String content) {
        return "#### " + content;
    }

    public static String Bold(String content) {
        return "**" + content + "** ";
    }

    public static String Delete(String content) {
        return "~~" + content + "~~ ";
    }

    public static String titleRoute(int titleSize, String name) {
        if (titleSize == 1) {
            return title1(name);
        } else if (titleSize == 2) {
            return title2(name);
        } else if (titleSize == 3) {
            return title3(name);
        } else if (titleSize == 4) {
            return title4(name);
        }
        return name;
    }
}
