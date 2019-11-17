package jdbc;

/*
*       2019年10月23日21:57:55
*       封装一个JDBC 工具类
* */

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {


    static Properties pros = null;      // 用来读取和处理资源文件中的信息

    static {        // 只会在加载 JDBCUtil 类时执行一次，因为资源文件只需要读取一次
        pros = new Properties();
        // 用于读取项目文件Src下的内容，实际上是到bin目录去读取（存放.clas文件下的）
        try {
            // 经过一下语句之后，pros语句里面就又内容了
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getMySqlConn(String databaseName) {
        try {
            // 读取资源文件
            // 获得 MySQL 的连接
            Class.forName(pros.getProperty("mysqlDriver"));
            // 后面加 ?useSSL=false 是为了避免MySQL的警告
            return DriverManager.getConnection(pros.getProperty("mysqlURL") + databaseName + "?useSSL=false", pros.getProperty("mysqlUser"), pros.getProperty("mysqlPwd"));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(ResultSet rs, Statement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement ps, Connection conn) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
