package com.github.doc.model.merge;

import com.github.doc.model.ClassDoc;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author wangdongbo
 * @since 2020/3/20.
 */
@Data
public class MergeInterfaceDoc {

    private boolean isDeprecated = false;

    private String name;

    private String fullName;

    private String fileClassPath;

    private String comment;

    private List<MethodDoc> methodDocList = Lists.newLinkedList();

    private List<ClassDoc> relationClassDocList = Lists.newLinkedList();

}
