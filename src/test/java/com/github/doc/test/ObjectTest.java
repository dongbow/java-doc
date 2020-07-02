package com.github.doc.test;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author wangdongbo
 * @since 2020/3/23.
 */
@Data
public class ObjectTest {

    private Map<String, List<Map<List<String>, String>>> map;

    private String name;

    private int i;

    private int ints3[][];

    private int[] ints;

    private int[][] ints2;

    private String[] strings;

    private String[][] strings2;

    private ObjectTest[] objectTest2;

    private com.github.doc.ObjectTest[] objectTest;

}
