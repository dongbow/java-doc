package com.github.doc.model;

import lombok.Data;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
@Data
public class PropDoc {

    private boolean isDeprecated = false;

    private String name;

    private String type;

}
