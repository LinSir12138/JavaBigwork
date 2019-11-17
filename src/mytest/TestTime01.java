package mytest;

/*
*       2019年10月12日13:56:42
*       用来测试验证码10分钟内有效
* */

import java.util.Scanner;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestTime01 {


    //利用正则表达式检测邮箱是否合法（封装，以后大作业可以直接用）
    public boolean checkEmail(String email) {
        //电子邮件
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        System.out.println(isMatched);
        return isMatched;
    }

    // 利用正则表达式检测电话号码是否合法
    public boolean checkPhoneNumber(String phoneNumber) {
        String check = "^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\d{8}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(phoneNumber);
        boolean isMatched = matcher.matches();
        System.out.println(isMatched);
        return isMatched;
    }


    public static void main(String[] args) {
        TestTime01 t1 = new TestTime01();
        System.out.println("请选择登陆方式：1---->邮箱注册，2---->手机号注册");
        Scanner in = new Scanner(System.in);;
        int choose =  in.nextInt();
        if (choose == 1) {
            while (true) {
                System.out.println("请输入您的邮箱：");
                String email = in.next();
                // 考虑到最后的大作业是以用GUI实现的，所以这里尽量封装一一些功能作为接口，然后到时候字节调用，就不用重复写代码了
                boolean isMatched = t1.checkEmail(email);
                if (isMatched) {
                    SendEmail sendEmail = new SendEmail(email);
                    sendEmail.send();
                    break;      // 成功之后就跳出break循环
                } else {
                    System.out.println("输入的邮箱不合法，请重新输入：");
                }
            }
        } else if (choose == 2) {
            SendMessage sendMessage;
            long beginTime;
            long afterTime;
            while (true) {
                System.out.println("请输入您的电话号码：");
                String phoneNumber = in.next();
                boolean isMatched = t1.checkPhoneNumber(phoneNumber);
                if (isMatched) {
                    sendMessage = new SendMessage(phoneNumber);
                    sendMessage.send();
                    // 发送验证码之后开始计时
                    beginTime = System.currentTimeMillis();
                    System.out.println("10分钟倒计时开始.....");
                    break;
                } else {
                    System.out.println("请输入正确的电话号码！");
                }
            }
            while (true) {
                System.out.println("请输入您的验证码：");
                String verificationCode = in.next();
                afterTime = System.currentTimeMillis();
                if (verificationCode == String.valueOf(sendMessage.getRandomNumber()) && (afterTime - beginTime) < (1000 * 60 * 10)) {
                    System.out.println("验证成功！");
                } else {
                    System.out.println("验证码输入错误，请重新输入！");
                }
            }
        }
    }
}
