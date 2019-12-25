package excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *      将学生的成绩导出为 Excel 表格。
 *      这里需要调用 “文件资源选择器”，让用户选择导出的 Excel 表的名称和 位置
 *      2019年12月24日20:12:29
 *      可是，最终还是失败了，因为 JAR 包冲突了，网上找不着完整的 JAR 包，Maven有不会。
 *      所以只能换一种方式了，利用 List 嵌套存放Excel需要的数据。
 * */

public class UserScoreToExcel_Fail {


    /**
    * @Description: 将表格中的数据导出为 Excel 文件保存到本地，参数为 excel文件的名称
    * @Param: [excelName]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/24
    */
    public static void saveToExcel(String excelName) {
        String selectedPath = getSelectedPath();
        OutputStream out = null;

        try {
            /**
             *      1. 创建输出流（套一个缓冲流，加快速度）--》实际上可以用现有的 Commons I/O 组件，不过一开始还是尽量熟悉源代码比较好
             * */
            out = new BufferedOutputStream(new FileOutputStream(excelName + ".xlsx"));

            //  2. 创建 ExcelWriter 对象，通过 OutPutStream 对象和 需要生成的 Excel 表格类型为参数创建
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

            //  3. 设置 Sheet；这里需要通过反射加载 UserScoreModel 类 （虽然不知道为什么，但是它的参数列表就是这样写的...）
            Sheet sheet1 = new Sheet(1, 0, UserScoreModel.class);
            sheet1.setSheetName("sheet1");      // 设置 sheet 的名字

            /**
             *      ..............
             * */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
    * @Description: 通过 JFileChooser 获得需要保存的文件路径。
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/24
    */
    public static String getSelectedPath() {
        String selectPath = null;
        JFileChooser jf = new JFileChooser();
        // 2. 设置可以选择的文件模型，这里的 DIRECTORIES_ONLY 表示只能够选择文件夹
        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        /**
         *      3. 显示保存文件的界面，返回值有2种类型，在保存文件对话框中：
         *      “保存”按钮对应的常量值是 JFileChooser.APPROVE_OPTION，
         *      “取消”按钮对应的常量值是JFileChooser.CANCEL_ OPTION
         * */
        int returnVal = jf.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // 说明用户选中了文件夹，点击了保存按钮
            selectPath = jf.getSelectedFile().getAbsolutePath();        // 获得绝对路径
        }

        return selectPath;
    }
}
