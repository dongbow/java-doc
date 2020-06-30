package com.github.doc.model;

import com.github.doc.util.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
@Data
public class MethodPropDoc extends PropDoc {

    private String methodName;

    private String comment;

    private List<MethodParamDoc> methodParamDocList = Lists.newLinkedList();

    private String returnComment;

    public void parse(String comment) {
        if (StringUtils.isBlank(comment)) {
            return;
        }
        comment = comment.replace("*", "");
        comment = comment.replaceAll("(\n|\r\n)\\s+", "$1");
        comment = comment.replaceAll("[ ]+", "&@&");
        if (StringUtils.isBlank(comment)) {
            return;
        }
        StringBuilder methodComment = new StringBuilder();
        StringBuilder returnComment = new StringBuilder();
        Map<String, String> methodParamMap = Maps.newLinkedHashMap();
        boolean stopTmpComment = false;
        boolean stopTmpParamComment = false;
        String lastParamKey = null;
        for (String s : comment.split("\n")) {
            if (StringUtils.isBlank(s)) {
                continue;
            }
            if (!s.startsWith("@")) {
                if (!stopTmpComment) {
                    methodComment.append(s).append(", ");
                } else {
                    if (!stopTmpParamComment) {
                        String value = methodParamMap.get(lastParamKey) + ", " + s;
                        methodParamMap.put(lastParamKey, value);
                    } else {
                        returnComment.append(s);
                    }
                }
            } else {
                stopTmpComment = true;
                if (s.startsWith("@param")) {
                    String[] values = s.split("&@&");
                    if (values.length > 1) {
                        lastParamKey = values[1];
                        if (values.length > 2) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 2; i < values.length; i++) {
                                stringBuilder.append(values[i] + " ");
                            }
                            methodParamMap.put(values[1], stringBuilder.toString());
                        }
                    }
                }
                if (s.startsWith("@return")) {
                    stopTmpParamComment = true;
                    String[] values = s.split("&@&");
                    if (values.length > 1) {
                        for (int i = 1; i < values.length; i++) {
                            returnComment.append(values[i] + " ");
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(methodParamMap)) {
            methodParamMap.forEach((k, v) -> {
                this.getMethodParamDocList().add(new MethodParamDoc(false, k, v));
            });
        }
        if (StringUtils.isNotBlank(methodComment.toString())) {
            this.setComment(methodComment.toString().substring(0, methodComment.length() - 2).replace("&@&", " "));
        }
        this.setReturnComment(returnComment.toString());
    }
}
