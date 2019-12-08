package jdbc;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @Description:  将图片保存到数据库，从数据库中读取图片到本地 的工具类
*        图片在本地的路径： 位于 src 目录下的 Images 目录
*        记得关闭流
*        2019年12月5日13:04:40
*        实现思路：
*        1. 将图片保存到数据库
*            （1） 以本地图片为源路径创建输入流（FileInputStream），    ----》 关键
*            （2） 调用 PreparedSatement 对象的 setBinaryStream() 方法，以输入流为参数传入，----》 关键
*            （3）然后在指定图片所在表中行的id，就可以将图片插入到该位置了
*        2. 从数据库中读取图片到本地
*           （1） 先获取图片所在行的结果集对象 （ResultSet 对象）
*           （2） 然后调用该对象的 getBinaryStream()  方法，获得该图片字段对应的 输入流
*           （3） 在从 输入流 中读取信息存到本地即可 （基本 I/O操作）,
*                  需要注意的是：还有获取对应行的id，和路径拼接中得到本地图片路径，因为传入的路径是文件夹的路径
 *
 *        注：
 *          在Mysql里,BLOB类型，最大长度64K，恐怕不太适合存储大一点的图像。可以使用MEDIUMBLOB(最大16M）或者LONGBLOB类型(4G)
 *          1.写入时并无格式之分，只是保存二进制数据，读取后和写入时的格式一样。
 *      jpg图片用二进制格式写入mysql，再以二进制格式读取，图片格式为jpg
 *      gif图片用二进制格式写入mysql，再以二进制格式读取，图片格式为gif
 *      ...就是说与图片的格式无关。
 *      2.图片用数据库保存，以文件方式保存这两种方式各有利弊。
 *      3.mysql保存图片用BLOB类型。BLOB类型按可存储数据的大小分几种(详细看mysql help)。
 *      4.如以文件方式保存，用某种配置文件保存路径为好，如注册表，ini文件等，可以灵活配置 图片路径.
 *      强烈不建议用数据库存储图片在的路径！
 *
* @Param:
* @return:
* @Author: 林凯
* @Date: 2019/12/5
*/

public class ImageJDBC {
    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static FileInputStream in;
    private static FileOutputStream out;

    /**
    * @Description: 保存图片到数据库中,需要传入的是本地图片路径，和被更新记录的id
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/5
    */
    public static void saveImageToDatabase(String path, String id) {
        String sql = "update user set headImg = ? where id = ?";
//        String path = "D:\\Temp";

        try {
            in = new FileInputStream(path);
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            /**
             *      关键代码，利用字节流将图片写入数据库中
             * */
            ps.setBinaryStream(1, in, in.available());;
            ps.setObject(2, id);
            ps.execute();       // 执行插入
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JDBCUtil.close(ps, connection);     // 关闭数据连接
        }
    }
    
    /** 
    * @Description: 从数据库中读取指定id的图片保存到本地路径（利用相对路径，在工程目录下的Images下面）
    * @Param:           参数：
     *                 1  ~~~~~~~   保存到的 目的路径
     *                 2   ~~~~~~   数据库中图片所在行的对应 id
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/12/5 
    */ 
    public static void readImageFromDatabase(String destPath, String id) {
        String sql = "select * from user where id = ?";

        try {
            connection = JDBCUtil.getMySqlConn("ALY_bigwork");
            ps = connection.prepareStatement(sql);
            ps.setObject(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                /**
                 *      从数据库中获得对应字段的 输入流 ，用来读取该字段
                 * */
                InputStream in = rs.getBinaryStream(7); // 图片保存在第7列，所以指定索引为7（下标从 1 开始）
                /**
                 *      再从 输入流 中读取图片信息保存到本地,将该行的id传入，作为图片的名称
                 * */
                readImageFromInputStream(in, destPath, id);     // 输入流会在这里方法里面关闭
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
    * @Description: 从 输入流 中读取图片到本地，传入的路径是文件夹路径，和id拼接之后形成图片路径
    * @Param: [in, destPath, id]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/5
    */
    public static void readImageFromInputStream(InputStream in, String destPath, String id) {
        File file = new File(destPath + id + ".png");       //  加上id，拼接成为新的路径，图片格式为png
        if (!file.exists()) {
            new File(destPath).mkdir();
        }

        try {
            out = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];        // 一次读取1个字节的信息
            while (-1 != (len = in.read(buf))) {
                out.write(buf, 0, len);
            }
            out.flush();        // 强制刷新一下

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
//        ImageJDBC.saveImageToDatabase("D:\\Temp\\face06.png", "7");
        ImageJDBC.readImageFromDatabase("src//images//", "7");
    }
}
