package com.creditease.ns4.gear.idgen.util;

public class StringDealUtil {
    public static String parseValue(String str) {

        int startIndex = str.indexOf("${");

        int endIndex = str.lastIndexOf("}");

        if (startIndex != -1 && startIndex < endIndex) {

            String value = str.substring(startIndex + 2, endIndex);

            String dateString = DateUtil.getDateTimeStr();
            if (value.equals("date12")) {//yyMMddHHmmss
                dateString = dateString.substring(2, 14);
            } else if (value.equals("date6")) {//yyMMdd
                dateString = dateString.substring(2, 8);
            } else if (value.equals("date8")) {//yyMMddHH
                dateString = dateString.substring(2, 10);
            }else if (value.equals("date10")) {//yyMMddHHmm
                dateString = dateString.substring(2, 12);
            }else if (value.equals("date14")) {//yyyyMMddHHmmss
                dateString = dateString.substring(0, 14);
            }else if (value.equals("date15")) {//yyMMddHHmmssSSS
                dateString = dateString.substring(2, 17);
            }else if (value.equals("date16")) {//yyyMMddHHmmssSSS
                dateString = dateString.substring(1, 17);
            }else if (value.equals("date17")) {//yyyyMMddHHmmssSSS
                dateString = dateString.substring(0, 17);
            }

            str = str.substring(0, startIndex) + dateString + str.substring(endIndex + 1, str.length());
        }

        return str;
    }

    /**
     * <pre>
     * StringUtils.trim(null)          = null
     * StringUtils.trim("")            = ""
     * StringUtils.trim("     ")       = ""
     * StringUtils.trim("abc")         = "abc"
     * StringUtils.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed string, <code>null</code> if null String input
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /*
     * 转换成任意进制
     * */
    public static String long2Str(long source, int scale) {
        if (source == 0) {
            return "0";
        }

        StringBuffer target = new StringBuffer();
        char c;
        long a;
        while (source != 0) {
            a = source % scale;
            if (a >= 10) {
                c = (char) ('a' + source % scale - 10);
            } else {
                c = (char) ('0' + a);
            }
            source = source / scale;
            target.append(c);
        }

        return target.reverse().toString();
    }
}
