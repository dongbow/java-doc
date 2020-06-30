package com.github.doc;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.io.FileNotFoundException;

/**
 * @author wangdongbo
 * @since 2020/6/5.
 */
public class DocCloneTest {

    private static final String PATH_0 = "/Users/dongbo/Documents/workspce/java-doc/src/test/java/com/github/doc/test/ObjectClassCloneTest.java";
    private static final String PATH_1 = "/Users/dongbo/Documents/workspce/java-doc/src/test/java/com/github/doc/test/ObjectClass.java";
    private static final String PATH_2 = "/Users/dongbo/Documents/workspce/java-doc/src/test/java/com/github/doc/ObjectCloneService.java";

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(JSON.toJSONString(DocExecute.generateMergeInterfaceDoc(Lists.newArrayList(PATH_0, PATH_1, PATH_2))));
    }

}
