package com.baymax.exam.user.utils;

import java.util.Random;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/14 14:52
 * @description：
 * @modified By：
 * @version:
 */
public class RandomUtil {
    /**
     *
     * <p>Title: generateCode</p>
     * <p>Description: 生成6位随机码   如B5H447,AKNQ70</p>
     * @return 返回6位随机码
     */
    public static String generateCode() {

        String charList = "ABCDEFGHIJKLMNPQRSTUVWXY";
        String numList = "0123456789";
        StringBuffer rev=new StringBuffer();
        // 数字最多不超过四位
        int maxNumCount = 4;
        // 6位邀请码，字母数字混合
        int length = 6;
        Random f = new Random();
        for (int i = 0; i < length; i++) {
            if (f.nextBoolean() && maxNumCount > 0) {
                maxNumCount--;
                rev.append( numList.charAt(f.nextInt(numList.length())));
            } else {
                rev.append( charList.charAt(f.nextInt(charList.length())));
            }
        }
        return rev.toString();
    }
}
