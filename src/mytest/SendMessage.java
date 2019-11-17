package mytest;

/*
*       2019年10月11日21:19:16
*       测试用Java发送手机验证码短信
*       用的是 榛子科技的API ---------------》 已经完成封装，可以使用
* */

import com.zhenzi.sms.ZhenziSmsClient;

import java.util.Random;

public class SendMessage {
    private String phoneNumber;
    private long randomNumber;

    public SendMessage() {
        this.phoneNumber = "19979404626";       // 无参的话，就发送给电信的手机号
    }

    public SendMessage(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void send() {
        ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com", "102961", "161f3454-3672-4675-9b94-c9dbf3141d03");
        randomNumber = myRandom();
        try {
            String result = client.send("15970819628", "您的验证码是： " + randomNumber + "。您正在使用短信验证码登录/注册功能，该验证码10分钟输入有效，请勿泄漏给他人使用。\n");
            System.out.println(result);
            String balance = client.balance();
            System.out.println(balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成随机数
    public long myRandom() {
        Random random = new Random();
        long randNumber = random.nextInt(900000) + 100000;
        System.out.println(randNumber);
        return randNumber;
    }

    public long getRandomNumber() {
        return randomNumber;
    }

//    public static void main(String[] args) {
//        SendMessage sendMessage = new SendMessage("15970819628");
//        sendMessage.send();
//    }
}
