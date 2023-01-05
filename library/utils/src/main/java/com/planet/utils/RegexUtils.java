package com.planet.utils;

 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * @author tqf
 * @Description 手机号格式校验
 * @Version 1.0
 * @since 2022-03-15 15:28
 */
public class RegexUtils {
    // 手机号验证规则
    private static String REGEX_PHONE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    // 整数验证规则
    private static String REGEX_NUMBER = "-?[1-9]\\d*";
 
    /**
     * 校验手机号格式是否正确
     * @param phone
     * @return
     */
     public static  Boolean regexPhone(String phone){
        boolean b;
        if(phone.length() != 11){
            b = false;
        }else{
            Pattern p = Pattern.compile(REGEX_PHONE);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            b = isMatch;
        }
        return b;
    }
 
    /**
     * 校验数据是否是整数
     * @param number
     * @return
     */
    public static  Boolean regexNumber(String number){
        boolean b;
        if(number.length() == 0){
            b = false;
        }else{
            Pattern p = Pattern.compile(REGEX_NUMBER);
            Matcher m = p.matcher(number);
            boolean isMatch = m.matches();
            b = isMatch;
        }
        return b;
    }
 
}