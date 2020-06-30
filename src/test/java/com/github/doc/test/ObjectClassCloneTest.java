package com.github.doc.test;

import lombok.Data;

/**
 * 测试cloneable
 *
 * @author wangdongbo
 * @since 2020/6/5.
 */
@Data
public class ObjectClassCloneTest extends ObjectClass implements Cloneable {

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

}
