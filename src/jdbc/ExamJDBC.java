package jdbc;

/*
*           进行与 考试 相关的  JDBC  操作的工具类
* */

import javabean.Exam;

import java.sql.*;

public class ExamJDBC {
    private Exam exam;          // 每一次考试对应的 Javabean 对象
    private static Connection connection;      // 连接
    private static PreparedStatement ps;       // 使用 PreparedStatement
    private static ResultSet rs;            // 从数据库中获取的数据

    /**
    * @Description: 从数据库中获得除id 以外的考试信息，并以一个二维数组的形式返回-
    * @Param: []
    * @return: java.lang.String[][]
    * @Author: 林凯
    * @Date: 2019/11/27
    */
    public static String[][] getAllExam() {
        String[][] result;          // 需要返回的二维数组
        int row;            // 结果集对应的总行数
        String sql = "select examName, paperName, studentNumber, startTime, endTime from exam";      // 查询所有记录的 sql 语句（除id以外）

        try {
            connection = JDBCUtil.getMySqlConn("bigwork");
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();         // 执行获得结果

            // 获得结果集的总行数
            rs.last();      // 将结果集的指针移动到最后一行
            row =  rs.getRow();         // 获得当前指向行的行数，因为指向的是最后一行，所以返回的可以认为是总行数
            result = new String[row][rs.getMetaData().getColumnCount()];     // 初始化数组（需要作为结果返回）
            rs.beforeFirst();           // 将 “指针” 重新执行第一行前面的位置

            /*
            *       注： getMetaData()方法可以检索此 ResultSet对象的列的数量，类型和属性，返回的 ResultSetMetaData 对象
            *           的 getColumnCount() 方法可以获得结果集的列数
            * */
//            System.out.println(row);
//            System.out.println(rs.getMetaData().getColumnCount());
//            rs.next();
//
//            System.out.println(rs.getObject(1).toString());

            for (int i = 0; i < row; i++) {
                rs.next();      // rs 的指针向下移动一行
                System.out.println("rs.object" + rs.getObject(1));
                for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
                    System.out.println(i);
                    System.out.println(j);
                    result[i][j] = rs.getObject(j + 1).toString();      // 下标从1开始，所以要加一
                    System.out.println(result[i][j]);
                }
            }

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            JDBCUtil.close(rs, ps, connection);     // 关闭连接
        }
    }

    /**
    * @Description: 想数据库中增加新的考试记录
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/27
    */
    public static void insertExam(String examName, String paperName, String studentNumber, Timestamp beginTime, Timestamp endTime) {
        String sql = "insert into exam (examName, paperName, studentNumber, startTime, endTime) values (?, ?, ?, ?, ?)";

        try {
            connection = JDBCUtil.getMySqlConn("bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, examName);
            ps.setObject(2, paperName);
            ps.setObject(3, studentNumber);
            ps.setObject(4, beginTime);
            ps.setObject(5, endTime);
            ps.execute();       // 执行
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(ps, connection);     // 关闭连接
        }

    }
    
    /** 
    * @Description: 根据传入的 考试的名字，在数据库中删除对应的记录
    * @Param: [examName] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/11/30 
    */ 
    public static void deleteExam(String examName) {
        String sql = "delete from exam where examName = ?";

        try {
            connection = JDBCUtil.getMySqlConn("bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, examName);
            ps.execute();       // 执行 SQL 语句
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(ps, connection);     // 关闭流
        }

    }
}
