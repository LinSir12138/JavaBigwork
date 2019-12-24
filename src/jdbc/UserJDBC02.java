package jdbc;

import javabean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *      改进算法，利用 javabean 对象 + 集合  来存储从数据库中读取的数据。
 *      10月底(见Subject类，javabean包下)，一开始的时候就是用这种方法，后来不知道怎么回事就忘了，觉得不好用，麻烦。
 *      现在觉得是真香，贼方便啊！
 *      2019年12月24日16:45:59
 * */

public class UserJDBC02 {

    private static User user;
    private static Connection conn;        // 连接
    private static PreparedStatement ps;       // 使用 PreparedStatement
    private static ResultSet rs;       // 从数据库中获取的数据
    private static List<User> users = new ArrayList<>();
    boolean isPhoneFlag;       // 判断传进来的Number是手机号还是邮箱号

    public UserJDBC02() {
    }

    public static User getUserByName(String userName) {
        user = new User();
        String sql = "select * from user where userName = ?";

        try {
            conn = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = conn.prepareStatement(sql);
            ps.setObject(1, userName);
            rs = ps.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("userName"));
                user.setPwd(rs.getString("pwd"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setEmailNumber(rs.getString("emailNumber"));
                user.setRegTime(rs.getDate("regTime"));
                user.setSchoolNumber(rs.getString("schoolNumber"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, ps, conn);
        }

        return null;
    }

    public static void main(String[] args) {
        UserJDBC02.getUserByName("林凯");
    }
}
