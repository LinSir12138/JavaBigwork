package jdbc;

/*
*       封装的关于jdbc的操作的类
*       主要方法：
*           storeUser（） 存储用户的个人信息
*           readUser（）  读取用户的个人信息
* */

import javabean.User;
import main.CheckEmailOrPhone;
import slatMD5.SaltMd5Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserJDBC {
    private User user;
    private Connection conn;        // 连接
    private PreparedStatement ps;       // 使用 PreparedStatement
    ResultSet rs;       // 从数据库中获取的数据
    List<User> users = new ArrayList<>();
    boolean isPhoneFlag;       // 判断传进来的Number是手机号还是邮箱号

    public UserJDBC() {
    }

    // 在构造方法中初始化 User 对象
    public UserJDBC(String userName, String pwd, String number) {
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

    /**
    * @Description: 根据手机号检查数据库中是否存在，对应的用户，并且检查用户的密码是否和输入的参数匹配
     *              第1个参数~~~~~~ 用户手机号
    * @Param: []
    * @return:   1  ~~~~~ 存在手机号，密码正确
     *            2 ~~~~~~  不存在该手机号
     *            3 ~~~~~~~ 存在手机号，密码错误
    * @Author: 林凯
    * @Date: 2019/12/4
    */
    public int checkUserByPhoneNumber(String phoneNumber, String pwd) {
        String sql = "select * from user where phoneNumber = ?";

        try {
            conn = JDBCUtil.getMySqlConn("bigwork");
            ps   = conn.prepareStatement(sql);
            ps.setObject(1, phoneNumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                // 如果 rs.next() 为 true，说明查到了记录，说明用户输入的手机号正确
                String pwdHash = rs.getString(3);        // 获得密码对应的哈希值（用MD5加盐加密）
                String salt = SaltMd5Util.getSaltFromHash(pwdHash);     // 获得 盐值
                String userPwdHash = SaltMd5Util.MD5WithSalt2(pwd, salt);       // 用户输入的密码加盐之后生成的哈希值
                if (pwdHash.equals(userPwdHash)) {
                    return 1;       // 如果用户输入的密码加盐MD5加密后和数据库中读取的相同，则说明验证成功
                } else {
                    return  3;      // 表示 手机号输入正确，但是密码输入错误
                }
            } else {
                return 2;       // 查询无结果，表不存在该手机号
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;       // 抛出异常的话，也返回2
        }

    }

    /**
     * @Description: 根据 邮箱账号 检查数据库中是否存在，对应的用户，并且检查用户的密码是否和输入的参数匹配
     *              第1个参数~~~~~~ 用户手机号
     * @Param: []
     * @return:   1  ~~~~~ 表示用户手机号存在，且和密码对应
     *            2 ~~~~~~  不存在该手机号
     *            3 ~~~~~~~  密码错误
     * @Author: 林凯
     * @Date: 2019/12/4
     */
    public int checkUserByEmail(String emailNumber, String pwd) {
        String sql = "select * from user where emailNumber = ?";

        try {
            conn = JDBCUtil.getMySqlConn("bigwork");
            ps   = conn.prepareStatement(sql);
            ps.setObject(1, emailNumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                // 如果 rs.next() 为 true，说明查到了记录，说明用户输入的手机号正确
                String pwdHash = rs.getString(3);        // 获得密码对应的哈希值（用MD5加盐加密）
                String salt = SaltMd5Util.getSaltFromHash(pwdHash);     // 获得 盐值
                String userPwdHash = SaltMd5Util.MD5WithSalt2(pwd, salt);       // 用户输入的密码加盐之后生成的哈希值

                if (pwdHash.equals(userPwdHash)) {
                    return 1;       // 如果用户输入的密码加盐MD5加密后和数据库中读取的相同，则说明验证成功
                } else {
                    return  3;      // 表示 手机号输入正确，但是密码输入错误
                }
            } else {
                return 2;       // 查询无结果，表不存在该手机号
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;       // 抛出异常的话，也返回2
        }

    }

}
