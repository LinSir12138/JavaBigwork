package mytest;

/*
*       2019年10月11日17:58:48
*       为大作业做准备
*       封装一个发送邮件的类--------------封装完成，可以使用
* */

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class SendEmail {
    private String to;      // 收件人邮箱
    private String from;        //发件人邮箱
    private final String host = "smtp.qq.com";      //QQ邮件服务器
    private String password = "qmabzkcaxecxebii";       // QQ邮箱使用POP3/STMP服务是需要的授权码
    long randNumber = -1;

    public SendEmail() {
        this.to = "3426801998@qq.com";
        this.from = "1670822659@qq.com";
    }

    public SendEmail(String to) {
        this.from = "1670822659@qq.com";        // 默认是从自己的QQ邮箱发送邮件给注册的用户
        this.to = to;
    }

    // 用自己的邮箱给注册用户邮箱发送一个验证码
    public void send(){
        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");

        // 获取session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1670822659@qq.com", password);     //发件人邮件用户名、授权码
            }
        });

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From：头部头字段
            message.setFrom(new InternetAddress(from));

            // Set From：头部头字段
            message.addRecipients(MimeMessage.RecipientType.TO, new InternetAddress[]{new InternetAddress(to)});;

            // Set From:头部头字段
            message.setSubject("This is the verification code!");

            // 设置消息体
            randNumber = myRandom();
            message.setText("  您的验证码是：" + randNumber + '\n' +
                    "您正在使用短信验证码登录/注册功能，该验证码5分钟输入有效，请勿泄露给他人使用。");

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    // 生成随机数
    public long myRandom() {
        Random random = new Random(System.currentTimeMillis());
        long randNumber = random.nextInt(900000) + 100000;
        System.out.println(randNumber);
        return randNumber;
    }

    // 用于检测用户输入的验证码是否正确，需要一个接口返回验证码
    public long getRandNumber() {
        return randNumber;
    }

//    public static void main(String[] args) {
//        SendEmail sendEmail = new SendEmail("1670822659@qq.com", "3426801998@qq.com");
//        sendEmail.sendMyEmail();
//        System.out.println(sendEmail.getRandNumber());
//    }
}
