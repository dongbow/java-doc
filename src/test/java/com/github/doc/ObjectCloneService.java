package com.github.doc;

import com.github.doc.test.ObjectClass;
import com.github.doc.test.ObjectClassCloneTest;

import java.util.List;

/**
 * 测试clone
 *
 * @author wangdongbo
 * @since 2020/6/5.
 */
public interface ObjectCloneService {

    /**
     * 获取接口
     *
     * @param id ID
     * @return 返回对象
     */
    ObjectClassCloneTest get(Integer id);

    /**
     * 查询ID
     *
     * @param name 名字
     * @return 返回列表
     */
    List<ObjectClass> getId(String name);

}
