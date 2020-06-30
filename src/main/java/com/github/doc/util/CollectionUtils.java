package com.github.doc.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<? extends Object> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<? extends Object> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<? extends Object, ? extends Object> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<? extends Object, ? extends Object> map) {
        return !isEmpty(map);
    }
}
