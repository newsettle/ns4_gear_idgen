package com.creditease.ns4.gear.idgen.util;

import org.apache.commons.lang.StringUtils;

/**
 * 10进制、62进制互转
 */
public class ConversionUtil {

    /**
     * 初始化 62 进制数据，索引位置代表字符的数值，比如 A代表10，z代表61等
     */
    private static String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int scale = 62;


    /**
     * 将数字转为36进制
     *
     * @param num Long 型数字
     * @return 36进制字符串
     */
    public static String _10_to_36(long num) {
        return Long.toString(num, 36);
    }

    /**
     * 将数字转为36进制
     *
     * @param num Long 型数字
     * @param length 转换后的字符串长度，不足则左侧补0
     * @return 62进制字符串
     */
    public static String _10_to_36(long num, int length) {
        String value = Long.toString(num, 36);
        return StringUtils.leftPad(value, length, '0');
    }

    /**
     * 36进制字符串转为数字
     *
     * @param str 36进制字符串
     * @return 10 进制字符串
     */
    public static long _36_to_10(String str) {
        /**
         * 将 0 开头的字符串进行替换
         */
        str = str.replace("^0*", "");
        return Long.valueOf(str, 36);
    }


    /**
     * 将数字转为62进制
     *
     * @param num    Long 型数字
     * @return 62进制字符串
     */
    public static String _10_to_62(long num) {
        StringBuilder sb = new StringBuilder();
        int remainder = 0;

        while (num > scale - 1) {
            /**
             * 对 scale 进行求余，然后将余数追加至 sb 中，由于是从末位开始追加的，因此最后需要反转（reverse）字符串
             */
            remainder = Long.valueOf(num % scale).intValue();
            sb.append(chars.charAt(remainder));

            num = num / scale;
        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));

        return sb.reverse().toString();
    }


    /**
     * 将数字转为62进制
     *
     * @param num    Long 型数字
     * @param length 转换后的字符串长度，不足则左侧补0
     * @return 62进制字符串
     */
    public static String _10_to_62(long num, int length) {
        StringBuilder sb = new StringBuilder();
        int remainder = 0;

        while (num > scale - 1) {
            /**
             * 对 scale 进行求余，然后将余数追加至 sb 中，由于是从末位开始追加的，因此最后需要反转（reverse）字符串
             */
            remainder = Long.valueOf(num % scale).intValue();
            sb.append(chars.charAt(remainder));

            num = num / scale;
        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        String value = sb.reverse().toString();
        return StringUtils.leftPad(value, length, '0');
    }

    /**
     * 62进制字符串转为数字
     *
     * @param str 62进制字符串
     * @return 10 进制字符串
     */
    public static long _62_to_10(String str) {
        /**
         * 将 0 开头的字符串进行替换
         */
        str = str.replace("^0*", "");
        long num = 0;
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            /**
             * 查找字符的索引位置
             */
            index = chars.indexOf(str.charAt(i));
            /**
             * 索引位置代表字符的数值
             */
            num += (long) (index * (Math.pow(scale, str.length() - i - 1)));
        }

        return num;
    }


//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        System.out.println("10进制转36进制：" + _10_to_36(2576460L));
//        System.out.println("36进制转10进制：" + _36_to_10("1j80c"));
//
//        System.out.println("10进制转36进制：" + _10_to_36(2576460L, 11));
//        System.out.println("36进制转10进制：" + _36_to_10("1j80c"));
//
//        System.out.println("10进制转62进制：" + _10_to_62(2576460L));
//        System.out.println("62进制转10进制：" + _62_to_10("aOfO"));
//
//        System.out.println("10进制转62进制：" + _10_to_62(2576460L, 11));
//        System.out.println("62进制转10进制：" + _62_to_10("aOfO"));
//    }
}