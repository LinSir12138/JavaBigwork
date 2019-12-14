package jdbc;

import javabean.ExamResult;
import jdk.nashorn.internal.scripts.JD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *        examResult 类（考试结果类） 对应的 JDBC操作的工具类
 * */

public class ExamResultJDBC {
    private static Connection connection;      // 数据库连接
    private static PreparedStatement ps;       // 使用 PreparedStatement
    private static ResultSet rs;            // 从数据库中获取的数据

    /**
     *      最终发现不能这样写，因为 sql 语句不好生成
     * */
    /**
    * @Description: 初始化 链接，prepareStatement， 结果集等等,参数是 需要执行的操作的类型
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/2
    */
//    private static void initExamResultJDBC(int type) throws SQLException {
//        connection = JDBCUtil.getMySqlConn("ALY_bigwork");
//        ps = connection.prepareStatement(sql);
//
//    }

    /**
    * @Description: 向数据库中插入一条 examResult 的记录
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/2
    */
    public static void insertExamResult(String userName, String examName, String[] userAnswer, String[] standardAnswer, String useScore, String totalScore) {
        String sql = "insert into examresult (userName, examName, userAnswer, standardAnswer, userScore, totalScore) values (?, ?, ?, ?, ?, ?)";

        /**
         *      将字符串数组转换为以 -  连接的字符串
         * */
        StringBuilder tempUserAnswer = new StringBuilder();
        for (int i = 0; i < userAnswer.length; i++) {
            tempUserAnswer.append(userAnswer[i] + "-");
        }
        StringBuilder tempstandardAnswer = new StringBuilder();
        for (int i = 0; i < standardAnswer.length; i++) {
            tempstandardAnswer.append(standardAnswer[i] + "-");
        }

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, userName);
            ps.setObject(2, examName);
            ps.setObject(3, tempUserAnswer.toString());
            ps.setObject(4, tempstandardAnswer.toString());
            ps.setObject(5, useScore);
            ps.setObject(6, totalScore);
            ps.execute();       // 执行
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(ps, connection);     // 关闭连接
        }
    }
    
    /** 
    * @Description: 从数据库中读取全部考试成绩，以二维数组的形式返回（不包含 id 哪一列）
    * @Param: [] 
    * @return: java.lang.String[][] 
    * @Author: 林凯
    * @Date: 2019/12/14 
    */ 
    public static String[][] readAllExamResult() {
        String sql = "select * from examresult";
        String[][] result = null;
        int rows = 0;           // 行数
        int columns = 0;        // 列数

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();     // 执行 SQL 语句

            // 初始化 二维数组，因为不需要 id 那一列，所以 列数要减 1
            rows = JDBCUtil.getRows(rs);        // 获得行数
            columns = JDBCUtil.getColumns(rs);      // 获得列数
            result = new String[rows][columns - 1];     // 初始化 二维数组 ，注意列数要改变

            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < columns - 1; j++) {
                    result[i][j] = rs.getObject(j + 1 + 1).toString();      // 注意下标，因为不包含id那一列
                }
                i++;        // 千万要记得 i++
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
    * @Description: 根据 学生姓名 查询对应的考试结果，并以二维数组的形式返回
    * @Param: [] 
    * @return: java.lang.String[][] 
    * @Author: 林凯
    * @Date: 2019/12/14
    */ 
    public static String[][] selectExamResultByUserName(String userName) {
        String[][] reslut = null;
        String sql = "select * from examresult where userName = ?";
        int rows = 0;       // 行数
        int columns = 0;        // 列数

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, userName);
            rs = ps.executeQuery();     // 执行 SQL 语句

            rows = JDBCUtil.getRows(rs);
            columns = JDBCUtil.getColumns(rs);
            reslut = new String[rows][columns - 1];     // 减 1 的原因是不需要 id 那一列

            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < columns - 1; j++) {
                    reslut[i][j] = rs.getObject(j + 1 + 1).toString();      // 注意下标
                    System.out.println(reslut[i][j]);
                }
                i++;        // 不要忘了 i++
            }
            return reslut;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
    * @Description: 根据 考试名称  查询对应的考试结果，并以二维数组的形式返回
    * @Param: []
    * @return: java.lang.String[][]
    * @Author: 林凯
    * @Date: 2019/12/14
    */
    public static String[][] selectExamResultByExamName(String examName) {
        String[][] reslut = null;
        String sql = "select * from examresult where examName = ?";
        int rows = 0;       // 行数
        int columns = 0;        // 列数

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, examName);
            rs = ps.executeQuery();     // 执行 SQL 语句

            rows = JDBCUtil.getRows(rs);
            columns = JDBCUtil.getColumns(rs);
            reslut = new String[rows][columns - 1];     // 不需要 id 那一列

            int i = 0;
            while (rs.next()) {
                for (int j = 0; j < columns - 1; j++) {
                    reslut[i][j] = rs.getObject(j + 1 + 1).toString();      // 注意下标
                }
                i++;        // 不要忘了 i++
            }
            return reslut;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        ExamResultJDBC.selectExamResultByUserName("linkai");
    }
}
