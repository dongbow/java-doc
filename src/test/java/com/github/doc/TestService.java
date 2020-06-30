package com.github.doc;

import com.github.doc.model.ObjectDoc;

import java.util.List;
import java.util.Map;

/**
 * @author wangdongbo
 * @since 2020/3/24.
 */
public interface TestService {

    ObjectTest test1(String a);

    /**
     * test2
     *
     * @param a aa
     * @param b
     * @return
     */
    @Deprecated
    com.github.doc.test.ObjectTest test2(String a, String b);

    String test3(@Deprecated String a, ObjectTest objectTest, Map<String, Object> map, Map<String, String> map1,
                 List<String> list, List<ObjectTest> objectTests,
                 List<com.github.doc.test.ObjectTest> list2,
                 Map<ObjectTest, com.github.doc.test.ObjectTest> map2);

    Map<String, ObjectTest> test4();

    List<com.github.doc.test.ObjectTest> test5();

    <T extends ObjectDoc, R extends ObjectDoc> String test6(T o, R r);

}
