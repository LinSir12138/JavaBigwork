package jdbc;

/*
*       对试题进行修改时用到的JDBC操作
*       注：可以把这个 类 设计成为工具类，里面的方法都是静态方法，可以直接调
* */

import javabean.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectJDBC {
    private Subject subject;        // 题目对应的Javabean对象
    private Connection conn;        // 连接
    private PreparedStatement ps;       // 使用 PreparedStatement
    ResultSet rs;       // 从数据库中获取的数据
    List<Subject> users = new ArrayList<>();
    int rows;       // 从数据库中读取题目信息时，读取到的行数

    public SubjectJDBC() {
    }


    /**
    * @Description: 构造方法需要传入 11 个参数，包括id，changeTime
    * @Param: [type, content, optionA, optionB, optionC, optionD, answer, judge, remarks]
    * @return:
    * @Author: 林凯
    * @Date: 2019/10/27
    */
    public SubjectJDBC(String id, String type, String content, String optionA, String optionB, String optionC, String optionD, String answer, String judge, String remarks, Timestamp changeTime) {
        subject = new Subject(id, type, content, optionA, optionB, optionC, optionD, answer, judge, remarks, changeTime);
        // 设置题目的最后一次修改时间，通过 Timestamp 类
//        Timestamp changeTime = new Timestamp(System.currentTimeMillis());
//        String time = changeTime.toString();
//        Timestamp ch = new Timestamp()
//        subject.setChangeTime(changeTime);
    }

//    public SubjectJDBC(String[] datas) {
//        subject = new Subject(Integer.valueOf(datas[0]), datas[1], datas[2], datas[3], datas[4], datas[5], datas[6], datas[7], datas[8], datas[9]);
//        // 设置题目的最后一次修改时间，通过 Timestamp 类
//        Timestamp changeTime = new Timestamp(System.currentTimeMillis());
//        subject.setChangeTime(changeTime);
//    }

    /**
     * @Description:  存储 题目信息 到数据库中(需要构造方法传参)  数据库中总共有12列，这里传递11个参数，题目的id和title是唯一的
     *                     id有数据库自动生成，所以不需要传递，这样删除起来会更加方便
     * @Param: []
     * @return: void
     * @Author: 林凯
     * @Date: 2019/10/25
     */
    public void storeSubject() {
        String sql = "insert into subject (title, type, content, optionA, optionB, optionC, optionD, answer, judge, remarks, changeTime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println(sql);

        try {
            conn = JDBCUtil.getMySqlConn("bigwork");
            ps = conn.prepareStatement(sql);
            ps.setObject(1, subject.getTitle());
            ps.setObject(2, subject.getType());
            ps.setObject(3, subject.getContent());
            ps.setObject(4, subject.getOptionA());
            ps.setObject(5, subject.getOptionB());
            ps.setObject(6, subject.getOptionC());
            ps.setObject(7, subject.getOptionD());
            ps.setObject(8, subject.getAnswer());
            ps.setObject(9, subject.getJudge());
            ps.setObject(10, subject.getRemarks());
            ps.setObject(11, subject.getChangeTime());
            ps.execute();       // 执行
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(ps, conn);
        }
    }

    /**
    * @Description:  根据传进来的 SQL 语句，从数据库中  读取题目信息  到程序中（返回结果集转换而成的二维数组）
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/27
    */
    public String[][] readSubject(String sql) {
        System.out.println("开始执行readSubject");
        String[][] tempDatas;       // 数据库中查询的结果保存到对应的二维数组中
//        String sql = "select * from subject";       // 查询 subject  表中的所有
        System.out.println(sql);        // 保存都运行日志中

        try {
            conn = JDBCUtil.getMySqlConn("bigwork");        // 连接数据库
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // 获得总共的行数,用来创建需要返回的二维数组（下面几行代码就是为了获得结果集的行数）
            rs.last();      // 将光标移动到最后一行
            rows = rs.getRow();         // 返回当前行的行数（可以按 Ctrl + Q 查看快捷文档）
            // 获取列数
            ResultSetMetaData rsmd = rs.getMetaData() ;             // 用到了 ResultSetMetaData 工具类，可以用来获得ResultSet的列数
            int columnCount = rsmd.getColumnCount();        // 获取ResultSet的列数
            tempDatas = new String[rows][columnCount - 1];      // 返回的数组不包含id那一列
            rs.beforeFirst();       // 将光标重新置为第一行前面的位置

            while (rs.next()) {
                for (int i = 0; i < columnCount - 1; i++) {
                    // 注意，数组下标从0开始，数组下标为0的元素对应ResultSet 的第一行（主义下标）
                    // columnIndex - 第一列是1，第二列是2，...
                    tempDatas[rs.getRow() - 1][i] = rs.getString(i + 1 + 1);     // 这里要加一，还要再次加一，第一次是为了和数组保持一致，第二次是为了跳过id那一列，因为用不到
                }
            }

            // 将得到的数组打印出来，用于测试代码，可以省略
//            for (int i = 0; i < tempDatas.length; i++) {
//                for (int j = 0; j < tempDatas[0].length; j++) {
//                    System.out.println(tempDatas[i][j]);
//                }
//            }

            return tempDatas;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    * @Description: 从数据库中读取title那一列，防止用户输入题目编号时输入相同的编号（标题）
    * @Param: []
    * @return: java.lang.String[][]
    * @Author: 林凯
    * @Date: 2019/11/9
    */
    public String[] readTitle() {
        String[] tempDatas;         // 用来将数据库的数据存储到一维数组中
        int index = 0;      // 遍历数组时用到的变量
        String sql = "select title from subject";
        System.out.println(sql);

        try {
            conn = JDBCUtil.getMySqlConn("bigwork");
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // 下面3行代码来获取结果集的行数
            rs.last();      // 将光标移动到最后一行
            rows = rs.getRow();         // 返回当前行的行数（可以按 Ctrl + Q 查看快捷文档）
            rs.beforeFirst();       // 将光标重新置为第一行前面的位置

            tempDatas = new String[rows];        //初始化数组

            // 将数据存储到一维数组中返回，因为只有1列，所以只要用一维数组
            while (rs.next()) {
                // 因为只有一列数据，所以该列数据对应的columnIndex为1
                // columnIndex - 第一列是1，第二列是2，...
                tempDatas[index] = rs.getString(1);
                index++;        // 继续保持的数组的下一个元素
//                System.out.println(tempDatas[index]);       // 打印出来，测试用，可以省略
            }
            System.out.println(tempDatas.length);
            return tempDatas;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    * @Description: 根据传进来的 Title 数组，删除对应的数据 ---> 用事务进行删除，抛异常时进行事务回滚
    * @Param: [title]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    public boolean deleteSubject(String[] title) {
        boolean flag = false;       // 是否删除成功的标志
        String sql = "delete from subject where title = ?";

        try {
            conn = JDBCUtil.getMySqlConn("bigwork");
            conn.setAutoCommit(false);      // 设置自动提交为 flase
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < title.length; i++) {
                ps.setObject(1, title[i]);
                ps.addBatch();
            }

            ps.executeBatch();      // 批量执行
            conn.commit();      // 提交事务
            flag = true;        // 删除成功

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();        // 进行事务回滚
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return flag;
    }

    /**
    * @Description: 根据传进来的 data 数组,10个元素，changeTime在这里生成，更新数据库中对应的信息
    * @Param: [datas]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/15
    */
    public void updateSubject(String[] datas, String title, Timestamp timestamp) {
        for (String str:datas
             ) {
            System.out.println(str);
        }

        String sql = "update subject set title = ?, type = ?, content = ?, optionA = ?, optionB = ?, optionC = ?, optionD = ?, answer = ?, judge = ?, remarks = ?, changeTime = ? where title = ?";

        try {
            conn = JDBCUtil.getMySqlConn("bigwork");
            ps = conn.prepareStatement(sql);
            ps.setObject(1, datas[0]);
            ps.setObject(2, datas[1]);
            ps.setObject(3, datas[2]);
            ps.setObject(4, datas[3]);
            ps.setObject(5, datas[4]);
            ps.setObject(6, datas[5]);
            ps.setObject(7, datas[6]);
            ps.setObject(8, datas[7]);
            ps.setObject(9, datas[8]);
            ps.setObject(10, datas[9]);     // 数组有10个元素，最后一个元素下标为9
            ps.setObject(11, timestamp);
            ps.setObject(12, title);
            ps.execute();       // 执行

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(ps, conn);       // 关闭流
        }


    }

    public int getRows() {
        return rows;
    }

    public static void main(String[] args) {
        SubjectJDBC t1 = new SubjectJDBC();
        String[][] temp = t1.readSubject("aaa");
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j <temp[i].length; j++) {
                System.out.println(temp[i][j]);
            }
        }

    }
}
