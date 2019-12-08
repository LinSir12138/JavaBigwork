package jdbc;

/*
*       2019年11月16日09:45:55
*       试卷管理   对应的JDBC操作所封装的   工具类
* */

import javabean.MyPaper;
import jdk.nashorn.internal.scripts.JD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaperJDBC {
    private static MyPaper myPaper;        // 试卷对应的Javabean对象
    private static Connection connection;      // 连接
    private static PreparedStatement ps;       // 使用 PreparedStatement
    private static ResultSet rs;            // 从数据库中获取的数据
    private static List<MyPaper> paperList = new ArrayList<>();
    private static int rows;       // 从数据库中读取题目信息时，读取到的行数


    /** 
    * @Description: 获得数据库中的所有试卷信息，以二维数组的形式返回，总共有5列信息，id不需要，所以返回的数组有4列
    * @Param: [] 
    * @return: java.lang.String[][] 
    * @Author: 林凯
    * @Date: 2019/11/16 
    */ 
    public static String[][] getAllPapers() {
        String[][] datas;
        String sql = "select * from examinationpaper";

        try {
            // 1.获取数据库连接
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            // 2.下面几行代码获取数据的 总行数，根据行数初始化二维数组
            rs.last();          // 将 “指针” 移动到最后一行
            rows = rs.getRow();     // 返回当前行的行数（第一行对应的行数为1，即下标从1开始）
            datas = new String[rows][4];
            rs.beforeFirst();       // 将  “指针”  重新移动到第一行的前面

            while (rs.next()) {
                for (int i = 0; i < 4; i++) {
                    // getRow(),第一行返回1，第二行返回2，为了和数组下标一致，所以要减一 （Ctrl + Q查看快捷文档）
                    // getObjec(index),index == 1 表示返回第一列
                    datas[rs.getRow() - 1][i] = rs.getObject(i + 1 + 1).toString();
//                    System.out.println(datas[rs.getRow() - 1][i]);
                }
            }
            return datas;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            JDBCUtil.close(rs, ps, connection);     // 关闭连接
        }
    }

    /**
    * @Description: 将新建的试卷保存到数据库中
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    public static void addPaper(String[] data, Timestamp timestamp) {
        String sql = "insert into examinationpaper (title, subjectNumber, subjectTitle, changeTime) values (?, ?, ?, ?)";

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, data[0]);
            ps.setObject(2, data[1]);
            ps.setObject(3, data[2]);
            ps.setObject(4, timestamp);

            ps.execute();       // 执行
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(ps, connection);
        }

    }

    /**
    * @Description: 获得试卷数据库中的所有试卷标题，并返回一个String 数组
    * @Param: []
    * @return: java.lang.String[]
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    public static String[] readPaperTitle() {
        String sql = "select title from examinationpaper";
        int index = 0;      // 遍历数组时用到的变量
        String[] tempDatas;     // 用来保存 title 的临时数组


        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();     // 执行

            // 下面3行代码来获取结果集的行数
            rs.last();      // 将光标移动到最后一行
            rows = rs.getRow();         // 返回当前行的行数（可以按 Ctrl + Q 查看快捷文档）
            rs.beforeFirst();       // 将光标重新置为第一行前面的位置
            tempDatas = new String[rows];

            while (rs.next()) {
                tempDatas[index] = rs.getString(1);
                index++;
            }
            return tempDatas;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * @Description: 获得指定试卷里面的所有题目，边将题目转换成一个 String 数组返回
    * @Param: [paperTitle]
    * @return: java.lang.String[]
    * @Author: 林凯
    * @Date: 2019/11/30
    */
    public static String[] getPaperSubjectTitles(String paperTitle) {
        String sql = "select * from examinationpaper where title = ?";
        String[] resultDatas;

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, paperTitle);
            rs = ps.executeQuery();     // 执行 SQL  语句，获得结果集
            // 获取列数
            ResultSetMetaData rsmd = rs.getMetaData() ;             // 用到了 ResultSetMetaData 工具类，可以用来获得ResultSet的列数
            int columnCount = rsmd.getColumnCount();        // 获取ResultSet的列数
            resultDatas = new String[columnCount];      // 按照 列数初始化 String 数组

            String tempTitles = null;
            while (rs.next()) {
                tempTitles = rs.getString(4);
//                System.out.println(tempTitles);
            }
            resultDatas = tempTitles.split("-");


            return resultDatas;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    * @Description: 从数据库中删除选中的 试卷
    * @Param: []
    * @return: boolean
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    public static boolean deletePaper(String[] titles) {
        boolean flag = false;       // 是否删除成功的标志
        String sql = "delete from examinationpaper where title = ?";

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            connection.setAutoCommit(false);      // 设置自动提交为 flase
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < titles.length; i++) {
                ps.setObject(1, titles[i]);
                ps.addBatch();
            }

            ps.executeBatch();      // 批量执行
            connection.commit();      // 提交事务
            flag = true;        // 删除成功

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();        // 进行事务回滚
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return flag;
    }

    /** 
    * @Description: 根据传进来的   sql  语句执行查询操作，返回对应的 String二维数组 
    * @Param: [sql] 
    * @return: java.lang.String[][] 
    * @Author: 林凯
    * @Date: 2019/11/17 
    */ 
    public static String[][] getAllPapers(String sql) {
        String[][] datas;

        try {
            // 1.获取数据库连接
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            // 2.下面几行代码获取数据的 总行数，根据行数初始化二维数组
            rs.last();
            rows = rs.getRow();
            datas = new String[rows][4];
            rs.beforeFirst();

            while (rs.next()) {
                for (int i = 0; i < 4; i++) {
                    // getRow(),第一行返回1，第二行返回2，为了和数组下标一致，所以要减一 （Ctrl + Q查看快捷文档）
                    // getObjec(index),index == 1 表示返回第一列
                    datas[rs.getRow() - 1][i] = rs.getObject(i + 1 + 1).toString();
//                    System.out.println(datas[rs.getRow() - 1][i]);
                }
            }
            return datas;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            JDBCUtil.close(rs, ps, connection);     // 关闭连接
        }
    }
    
    /** 
    * @Description: 更新试卷信息，传入的参数： 1~~~需要更新的 subjectNumber 字段(试卷中更新之后的题目数量)  ，  2~~~需要更新的试卷的 subjectTitle 字段，用来表示
     *                  试卷中存有哪些题目,    3~~~更新之后的 时间戳， 4 ~~~~ 需要更新试卷的试卷标题——
    * @Param: [] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/11/17 
    */ 
    public static void updatePapers(String subjectNumber, String subjectTitle, Timestamp timestamp, String title) {
        // 不要忘了字符要用 ‘’  括起来
//        String sql = "update examinationpaper set subjectTitle = '" + subjectTitle + "' " + ", subjectNumber = '" + subjectNumber + ", " + " where title = '" + title + "'";
        String sql = "update examinationpaper set subjectNumber = ? , subjectTitle = ? , changeTime = ? where title = ?";

//        System.out.println(sql);

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, subjectNumber);
            ps.setObject(2, subjectTitle);
            ps.setObject(3, timestamp);
            ps.setObject(4, title);
            ps.execute();       // 执行
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(ps, connection);     // 关闭连接
        }

    }

    /**
    * @Description: 更新试卷的名字（标题）， 传入的参数：1 ~~~~~~需要更新的试卷的旧 Title，2 ~~~~~~~更新之后新的 Title
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/26
    */
    public static void updatePaperName(String oldPaperName, String newPaperName) {
        String sql = "update examinationpaper set title = ? where title = ?";

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, newPaperName);
            ps.setObject(2, oldPaperName);
            ps.execute();       // 执行
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {
        PaperJDBC.getAllPapers();
    }

}
