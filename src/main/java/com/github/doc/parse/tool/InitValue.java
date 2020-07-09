package com.github.doc.parse.tool;

import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdongbo
 * @since 2020/7/7.
 */
public class InitValue {

    public static Object parseValue(Expression expression) {
        if (Objects.isNull(expression) || expression.isNullLiteralExpr()) {
            return null;
        }
        if (expression.isStringLiteralExpr()) {
            StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) expression;
            return stringLiteralExpr.getValue();
        }
        if (expression.isIntegerLiteralExpr()) {
            IntegerLiteralExpr integerLiteralExpr = (IntegerLiteralExpr) expression;
            return integerLiteralExpr.asInt();
        }
        if (expression.isBooleanLiteralExpr()) {
            BooleanLiteralExpr booleanLiteralExpr = (BooleanLiteralExpr) expression;
            return booleanLiteralExpr.getValue();
        }
        if (expression.isLongLiteralExpr()) {
            LongLiteralExpr longLiteralExpr = (LongLiteralExpr) expression;
            return longLiteralExpr.asLong();
        }
        if (expression.isDoubleLiteralExpr()) {
            DoubleLiteralExpr doubleLiteralExpr = (DoubleLiteralExpr) expression;
            return doubleLiteralExpr.asDouble();
        }
        if (expression.isCharLiteralExpr()) {
            return ((CharLiteralExpr) expression).asChar();
        }
        if (expression.isArrayInitializerExpr()) {
            ArrayInitializerExpr arrayInitializerExpr = (ArrayInitializerExpr) expression;
            List<Object> tmp = Lists.newArrayListWithCapacity(arrayInitializerExpr.getValues().size());
            for (Expression value : arrayInitializerExpr.getValues()) {
                tmp.add(parseValue(value));
            }
            return tmp;
        }
        return expression.getTokenRange().isPresent() ? expression.getTokenRange().toString() : null;
    }

}
