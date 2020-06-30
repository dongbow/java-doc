package com.github.doc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangdongbo
 * @since 2020/3/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MethodParamDoc {

    private boolean isDeprecated = false;

    private String name;

    private String comment;

}
