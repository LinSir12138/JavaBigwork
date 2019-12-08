package jdbc;

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




}
