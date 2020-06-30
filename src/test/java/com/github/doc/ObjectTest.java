package com.github.doc;

import com.github.doc.test.ObjectExtendsTest;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author wangdongbo
 * @since 2020/3/22.
 */
@Data
@Deprecated
public class ObjectTest extends ObjectExtendsTest {

    private String test1;

    @Deprecated
    private List<String> test2;

    private Map<String, String> test3;

    private Map<String, List<String>> test4;

    private Map<String, ObjectTest> test5;

    private Map<String, List<ObjectTest>> test6;

    private ObjectTest test7;

    private List<ObjectTest> test8;

    private List<List<ObjectTest>> test9;

    private List<Map<String, ObjectTest>> test10;

    private List<Map<String, List<ObjectTest>>> test11;

    private com.github.doc.test.ObjectTest objectTest;

}
