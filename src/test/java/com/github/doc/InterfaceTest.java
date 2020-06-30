package com.github.doc;

import com.alibaba.fastjson.JSON;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * @author wangdongbo
 * @since 2020/3/24.
 */
public class InterfaceTest {

    public static void main(String[] args) throws FileNotFoundException {
        String s = "/Users/dongbo/Documents/workspce/java-doc/src/test/java/com/github/doc/TestService.java";
        System.out.println(JSON.toJSONString(DocExecute.generateDoc(Arrays.asList(s))));
    }
}
