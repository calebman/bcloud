package com.bcloud.common.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 字符串工具
 * @date 2020-03-28
 */
public class StringUtils {

    /**
     * 是否为空字符串
     *
     * @param string 字符串
     * @return 是否为空
     */
    public static boolean empty(String string) {
        return null == string || "".equals(string.trim());
    }

    /**
     * 下划线命名转驼峰命名
     *
     * @param underscore
     * @return
     */
    public static String underscoreToCamelCase(String underscore) {
        String[] ss = underscore.split("_");
        if (ss.length == 1) {
            return underscore;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(ss[0]);
        for (int i = 1; i < ss.length; i++) {
            sb.append(upperFirstCase(ss[i]));
        }

        return sb.toString();
    }

    /**
     * 驼峰 转下划线
     *
     * @param camelCase
     * @return
     */
    public static String toLine(String camelCase) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(camelCase);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 首字母 转小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 首字母 转大写
     *
     * @param str
     * @return
     */
    public static String upperFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

    /**
     * 对象数组转字符串 并加入分隔符
     *
     * @param delimiter 分隔符
     * @param elements  对象数组
     * @return 转换结果
     */
    public static String join(CharSequence delimiter, Object[] elements) {
        return Arrays.stream(elements).map(Object::toString).collect(Collectors.joining(delimiter));
    }
}
