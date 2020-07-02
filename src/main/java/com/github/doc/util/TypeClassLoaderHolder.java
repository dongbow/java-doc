package com.github.doc.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author wangdongbo
 * @since 2020/7/1.
 */
public class TypeClassLoaderHolder {

    private static final ThreadLocal<Map<String, String>> LOCAL = new ThreadLocal<>();

    public static String tryGet(String name) throws ClassNotFoundException {
        name = "java.lang." + name;
        String fullName = get(name);
        if (fullName != null) {
            return fullName;
        }
        Class clazz = Class.forName(name);
        set(clazz.getName());
        return clazz.getName();
    }

    public static void set(String clazz) {
        Map<String, String> map = LOCAL.get();
        if (CollectionUtils.isEmpty(map)) {
            map = Maps.newHashMap();
        }
        map.put(clazz, clazz);
        LOCAL.set(map);
    }

    public static String get(String clazz) {
        if (LOCAL.get() == null) {
            return null;
        }
        return LOCAL.get().get(clazz);
    }

    public static void remove() {
        LOCAL.remove();
    }

}
