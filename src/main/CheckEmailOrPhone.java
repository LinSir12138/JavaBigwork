package main;

/*
*       2019年10月20日16:28:18
*       封装一个检测邮箱或者手机号是否合法的工具类
* */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckEmailOrPhone {
    //利用正则表达式检测邮箱是否合法（封装，以后大作业可以直接用）
    public static boolean checkEmail(String email) {
        //电子邮件
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        System.out.println(isMatched);
        return isMatched;
    }

    // 利用正则表达式检测电话号码是否合法
    public static boolean checkPhoneNumber(String phoneNumber) {
        String check = "^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\d{8}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(phoneNumber);
        boolean isMatched = matcher.matches();
        System.out.println(isMatched);
        return isMatched;
    }
}

