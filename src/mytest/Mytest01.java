package mytest;

/*
*       2019年10月11日17:09:35
*      测试Java发送邮件
*
* */

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mytest01 {
    public static void main(String[] args) {
        String to = "3426801998@qq.com";        // qq小号
        String from = "1670822659@qq.com";
//        String to = "1670822659@qq.com";
//        String from = "flipglwlsg@gmail.com";

        // 指定发送邮件的主机为 localhost （本机）
        String host = "smtp.qq.com";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");

        // 获取默认的 session 对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1670822659qq.com", "mnnebjzppdwlefcd");
            }
        });

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From：头部头字段
            message.setFrom(new InternetAddress(from));

            // Set to：头部头字段
            message.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(to)});

            // Set Subject:头部头字段
            message.setSubject("This is the Subject Line!");

            // 设置消息体
            message.setText("This is actual message");

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
