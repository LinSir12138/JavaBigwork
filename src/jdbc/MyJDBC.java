package jdbc;

/*
*       封装的关于jdbc的操作的类
*       主要方法：
*           storeUser（） 存储用户的个人信息
*           readUser（）  读取用户的个人信息
* */

import javabean.User;
import main.CheckEmailOrPhone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyJDBC {
    private User user;
    private Connection conn;        // 连接
    private PreparedStatement ps;       // 使用 PreparedStatement
    ResultSet rs;       // 从数据库中获取的数据
    List<User> users = new ArrayList<>();
    boolean isPhoneFlag;       // 判断传进来的Number是手机号还是邮箱号

    public MyJDBC() {
    }

    // 在构造方法中初始化 User 对象
    public MyJDBC(String userName, String pwd, String number) {
        this.conn = JDBCUtil.getMySqlConn("bigwork");
        user = new User(userName, pwd, number);
        if (CheckEmailOrPhone.checkPhoneNumber(number)) {
            this.isPhoneFlag = true;
        } else {
            this.isPhoneFlag = false;
        }
    }

    /**
    * @Description:  存储 用户的个人信息 到数据库中
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/25
    */
    public void storeUser() {
        String sqlPhone = "insert into user (userName, pwd, phoneNumber, regTime) values (?, ?, ?, ?) ";
        String sqlEmail = "insert into user (userName, pwd, emailNumber, regTime) values (?, ?, ?, ?) ";
        String sql;
        if (isPhoneFlag) {
            sql = sqlPhone;
        } else {
            sql = sqlEmail;
        }

        try {
            ps = conn.prepareStatement(sql);
            ps.setObject(1, user.getUserName());
            ps.setObject(2, user.getPwd());
            if (isPhoneFlag) {
                ps.setObject(3, user.getPhoneNumber());
            } else {
                ps.setObject(3, user.getEmailNumber());
            }
            ps.setObject(4, user.getRegTime());

            ps.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 先打开的后关闭
            JDBCUtil.close(ps, conn);
        }

    }

    public boolean loginCheck() {


        return true;
    }


}
