package main;

/*
*       2019年10月21日22:19:18
*       使用多线程发送验证码到邮箱或手机号
*       因为发送耗时比较多，影响主线程的倒计时
* */

import mytest.SendEmail;
import mytest.SendMessage;

public class SendByThread implements Runnable {
    SendEmail sendEmail;
    SendMessage sendMessage;
    String destination;
    int flag;
    long randomNumber;

    public SendByThread(int flag, String destination) {
        this.flag = flag;
        this.destination = destination;
    }

    // 获取动态验证码
    public long getDynamicCode() {
        return randomNumber;
    }

    @Override
    public void run() {
        if (flag == 1) {
            // 发送到邮箱
            sendEmail = new SendEmail(destination);
            sendEmail.send();
            randomNumber = sendEmail.getRandNumber();       // 获取生成的验证码
        } else if (flag == 2) {
            // 发送到手机
            sendMessage = new SendMessage(destination);
            sendMessage.send();
            randomNumber = sendMessage.getRandomNumber();       // 获取生成的验证码
        }
    }
}
