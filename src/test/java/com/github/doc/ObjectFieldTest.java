package com.github.doc;

import com.alibaba.fastjson.JSON;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * @author wangdongbo
 * @since 2020/3/22.
 */
public class ObjectFieldTest {

    public static void main(String[] args) throws FileNotFoundException {
        String[] s = new String[]{
                "/Users/dongbo/Documents/workspce/java-doc/src/test/java/com/github/doc/test/ObjectTest.java"
        };
        System.out.println(JSON.toJSONString(DocExecute.generateDoc(Arrays.asList(s))));
    }

}
