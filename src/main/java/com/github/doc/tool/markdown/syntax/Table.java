package com.github.doc.tool.markdown.syntax;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wangdongbo
 * @since 2020/3/23.
 */
public class Table {

    private List<String> titles;

    private List<List<String>> contents;

    public static String table(List<String> titles, List<List<String>> contents) {
        return new Table(titles, contents).toMarkDown();
    }

    private Table(List<String> titles, List<List<String>> contents) {
        this.titles = titles;
        this.contents = contents;
    }

    public String toMarkDown() {
        List<String> table = Lists.newArrayList(buildTitle(), spliterator());
        contents.forEach(c -> table.add(buildContent(c)));
        return Joiner.on("\n").join(table);
    }

    private String buildTitle() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("| ");
        for (int i = 0; i < titles.size(); i++) {
            stringBuilder.append(Font.Bold(titles.get(i)));
            if (i + 1 < titles.size()) {
                stringBuilder.append(" | ");
            }
        }
        stringBuilder.append(" |");
        return stringBuilder.toString();
    }

    private String spliterator() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("| ");
        for (int i = 0; i < titles.size(); i++) {
            stringBuilder.append("---");
            if (i + 1 < titles.size()) {
                stringBuilder.append(" | ");
            }
        }
        stringBuilder.append(" |");
        return stringBuilder.toString();
    }

    private String buildContent(List<String> lineContents) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("| ");
        for (int i = 0; i < lineContents.size(); i++) {
            stringBuilder.append(lineContents.get(i));
            if (i + 1 < lineContents.size()) {
                stringBuilder.append(" | ");
            }
        }
        stringBuilder.append(" |");
        return stringBuilder.toString();
    }

}
