package javabean;

/*
*       封装的 javabean 对象
*       对应数据库bigwork中的user表
* */

import main.CheckEmailOrPhone;

import java.sql.Date;

public class User {
    private Integer id;
    private String userName;
    private String pwd;
    private String phoneNumber;
    private String emailNumber;
    private Date regTime;       // 是 java.sql 包下的 Date
//    private byte[] headImag;


    // javabean对象一定要有无参构造方法
    public User() {
    }

    // 构造函数初始化 用户名，密码，绑定的手机号或邮箱账号
    public User(String userName, String pwd, String number) {
        this.userName = userName;
        this.pwd = pwd;
        if (CheckEmailOrPhone.checkEmail(number)) {
            this.emailNumber = number;
        } else {
            this.phoneNumber = number;
        }
        Date date = new Date(System.currentTimeMillis());
        this.regTime = date;        // 日期设置为当前日期
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailNumber() {
        return emailNumber;
    }

    public void setEmailNumber(String emailNumber) {
        this.emailNumber = emailNumber;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
}
