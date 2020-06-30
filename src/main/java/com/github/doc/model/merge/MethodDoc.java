package com.github.doc.model.merge;

import com.github.doc.model.MethodParamDoc;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author wangdongbo
 * @since 2020/3/20.
 */
@Data
public class MethodDoc {

    private boolean isDeprecated = false;

    private String shortMethod;

    private String method;

    private String methodComment;

    private List<MethodParamDoc> methodParamDocList = Lists.newLinkedList();

    private String returnComment;

}
