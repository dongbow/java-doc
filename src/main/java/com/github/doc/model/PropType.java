package com.github.doc.model;

import com.github.doc.util.CollectionUtils;
import com.github.doc.util.TypeClassLoaderHolder;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdongbo
 * @since 2020/7/1.
 */
@Data
public class PropType {

    /**
     * 类型
     */
    private String type;

    /**
     * 类型完整名称, 根据import和当前类尝试解析，不一定有
     */
    private String typeFullName;

    /**
     * 属性的特殊
     */
    private TypeSpecialProp typeSpecialProp = TypeSpecialProp.defaultValue();

    /**
     * 类型中的类型，例如泛型类型， Map<String, List<String>>
     * type : Map
     * typeFullName: java.util.Map
     * propTypes: [
     * {
     * type: String
     * typeFullName: null
     * propTypes: []
     * },
     * {
     * type: List
     * typeFullName: java.util.List
     * propTypes: [
     * type: String
     * typeFullName: null
     * propTypes: []
     * ]
     * }
     * ]
     */
    private List<PropType> propTypes = Lists.newLinkedList();

    public PropType copy() {
        PropType propType = new PropType();
        propType.setType(this.type);
        propType.setTypeFullName(this.typeFullName);
        propType.setTypeSpecialProp(this.typeSpecialProp.copy());
        propType.setPropTypes(this.propTypes.stream().map(PropType::copy).collect(Collectors.toList()));
        return propType;
    }

    public static PropType parse(Type sourceType, ObjectDoc classDoc) {
        Type type = sourceType;
        PropType propType = new PropType();
        int i = 0;
        if (type instanceof ArrayType) {
            Type tmp = ((ArrayType) type).getComponentType();
            while (tmp != null) {
                ++i;
                if (tmp instanceof ClassOrInterfaceType) {
                    propType.setTypeSpecialProp(TypeSpecialProp.orderValue(i));
                    type = tmp;
                    break;
                } else if (tmp instanceof ArrayType) {
                    tmp = ((ArrayType) tmp).getComponentType();
                } else {
                    propType.setTypeSpecialProp(TypeSpecialProp.orderValue(i));
                    propType.setType(tmp.asString());
                    return propType;
                }
            }
        }
        if (!(type instanceof ClassOrInterfaceType)) {
            propType.setType(type.asString());
            return propType;
        }
        ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) type;
        propType.setType(classOrInterfaceType.getNameAsString());
        String fullName = null;
        if (classOrInterfaceType.getScope().isPresent()) {
            fullName = classOrInterfaceType.getScope().get().getNameAsString() + "." + classOrInterfaceType.getNameAsString();
        } else {
            if (CollectionUtils.isNotEmpty(classDoc.getImports())) {
                for (String anImport : classDoc.getImports()) {
                    if (Objects.equals(anImport.substring(anImport.lastIndexOf(".") + 1), classOrInterfaceType.getNameAsString())) {
                        fullName = anImport;
                        break;
                    }
                }
            }
            if (StringUtils.isEmpty(fullName)) {
                try {
                    fullName = TypeClassLoaderHolder.tryGet(classOrInterfaceType.getNameAsString());
                } catch (ClassNotFoundException e) {
                    fullName = classDoc.getPackageName() + "." + classOrInterfaceType.getNameAsString();
                }
            }
        }
        propType.setTypeFullName(fullName);
        if (classOrInterfaceType.getTypeArguments().isPresent()) {
            for (Type tmp : classOrInterfaceType.getTypeArguments().get()) {
                propType.getPropTypes().add(PropType.parse(tmp, classDoc));
            }
        }
        return propType;
    }

    @Data
    private static class TypeSpecialProp {

        /**
         * 是否是数组
         */
        private boolean array;

        /**
         * 数组长度
         */
        private int arrayLength;

        public static TypeSpecialProp defaultValue() {
            TypeSpecialProp typeSpecialProp = new TypeSpecialProp();
            typeSpecialProp.setArray(false);
            typeSpecialProp.setArrayLength(0);
            return typeSpecialProp;
        }

        public static TypeSpecialProp orderValue(int length) {
            TypeSpecialProp typeSpecialProp = new TypeSpecialProp();
            typeSpecialProp.setArray(true);
            typeSpecialProp.setArrayLength(length);
            return typeSpecialProp;
        }

        public TypeSpecialProp copy() {
            TypeSpecialProp typeSpecialProp = new TypeSpecialProp();
            typeSpecialProp.setArray(this.array);
            typeSpecialProp.setArrayLength(this.arrayLength);
            return typeSpecialProp;
        }
    }

}
