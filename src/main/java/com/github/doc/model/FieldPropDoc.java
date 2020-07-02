package com.github.doc.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangdongbo
 * @since 2020/3/20.
 */
@Data
public class FieldPropDoc extends PropDoc {

    private Object defaultValue;

    private String comment;

    public void parseComment(String comment, ClassDoc classDoc) {
        this.setComment(parse(comment, classDoc));
    }

    private String parse(String comment, ClassDoc classDoc) {
        if (StringUtils.isBlank(comment)) {
            return null;
        }
        comment = comment.replace("*", "");
        comment = comment.replaceAll("(\n|\r\n)\\s+", "$1");
        comment = comment.replaceAll("[ ]+", " ");
        if (StringUtils.isBlank(comment)) {
            return null;
        }
        return Stream.of(comment.split("\n")).filter(StringUtils::isNotBlank)
                .map(s -> {
                    if (s.startsWith("@see")) {
                        String tmp = s.replace("@see", "").trim();
                        if (StringUtils.isNotBlank(tmp)) {
                            if (!tmp.contains(".")) {
                                tmp = classDoc.getPackageName() + "." + tmp;
                            }
                            classDoc.getImports().add(tmp);
                        }
                    }
                    return s;
                }).collect(Collectors.joining(", "));
    }

    public static FieldPropDoc copy(FieldPropDoc fieldPropDoc) {
        FieldPropDoc newProp = new FieldPropDoc();
        newProp.setName(fieldPropDoc.getName());
        newProp.setDefaultValue(fieldPropDoc.getDefaultValue());
        newProp.setComment(fieldPropDoc.getComment());
        newProp.setType(fieldPropDoc.getType());
        newProp.setPropType(fieldPropDoc.getPropType().copy());
        return newProp;
    }
}
