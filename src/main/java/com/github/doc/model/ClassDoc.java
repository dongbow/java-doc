package com.github.doc.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
@Data
public class ClassDoc {

    private boolean isInterface = false;

    private boolean isDeprecated = false;

    private String name;

    private String packageName;

    private String fullName;

    private String classFilePath;

    private String comment;

    private String extendsFullName;

    private transient Set<String> imports = Sets.newLinkedHashSet();

    public void setComment(String comment) {
        this.comment = parse(comment);
    }

    private String parse(String comment) {
        if (StringUtils.isBlank(comment)) {
            return null;
        }
        comment = comment.replace("*", "");
        comment = comment.replaceAll("(\n|\r\n)\\s+", "$1");
        comment = comment.replaceAll("[ ]+", " ");
        if (StringUtils.isBlank(comment)) {
            return null;
        }
        List<String> strings = Stream.of(comment.split("\n")).filter(this::filterStr).map(this::getStr).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return Joiner.on(", ").join(strings);
    }

    private boolean filterStr(String s) {
        if (!s.startsWith("@")) {
            return true;
        }
        if (s.startsWith("@describe")) {
            return true;
        }
        if (s.startsWith("@description")) {
            return true;
        }
        return false;
    }

    private String getStr(String s) {
        if (s.startsWith("@describe")) {
            return s.replace("@describe:", "");
        }
        if (s.startsWith("@description")) {
            return s.replace("@description:", "");
        }
        return s;
    }
}
