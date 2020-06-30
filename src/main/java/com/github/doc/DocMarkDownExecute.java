package com.github.doc;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.EnumDoc;
import com.github.doc.model.InterfaceDoc;
import com.github.doc.model.ObjectDoc;
import com.github.doc.model.merge.MergeInterfaceDoc;
import com.github.doc.tool.markdown.EnumMarkDown;
import com.github.doc.tool.markdown.InterfaceMarkDown;
import com.github.doc.tool.markdown.InterfaceMergeMarkDown;
import com.github.doc.tool.markdown.ObjectMarkDown;
import com.github.doc.util.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author wangdongbo
 * @since 2020/3/23.
 */
public class DocMarkDownExecute {

    public static List<String> generateMarkDown(List<ClassDoc> classDocs) {
        if (CollectionUtils.isEmpty(classDocs)) {
            return Collections.emptyList();
        }
        List<String> markDowns = Lists.newArrayListWithCapacity(classDocs.size());
        classDocs.forEach(c -> {
            if (c instanceof InterfaceDoc) {
                markDowns.add(InterfaceMarkDown.generate((InterfaceDoc) c));
            } else if (c instanceof EnumDoc) {
                markDowns.add(EnumMarkDown.generate((EnumDoc) c));
            } else if (c instanceof ObjectDoc) {
                markDowns.add(ObjectMarkDown.generate((ObjectDoc) c));
            }
        });
        return markDowns;
    }

    public static Map<String, String> generateMergeInterfaceMarkDown(List<MergeInterfaceDoc> mergeInterfaceDocList) {
        if (CollectionUtils.isEmpty(mergeInterfaceDocList)) {
            return Collections.emptyMap();
        }
        Map<String, String> map = Maps.newHashMapWithExpectedSize(mergeInterfaceDocList.size());
        mergeInterfaceDocList.forEach(doc -> map.put(doc.getFullName(), InterfaceMergeMarkDown.generate(doc)));
        return map;
    }

}
