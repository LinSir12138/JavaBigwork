package excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *      将表格中的 试题信息 导出为 Excel 表格，利用 List 嵌套来存储数据，不用注解
 *      2019年12月25日12:47:32
 * */

public class SubjectToExcel {

    /** 
    * @Description: 用 List 嵌套的方式存储数据，将 试题信息 导出为 Excel表格
    * @Param: [excelName, tableModel] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/12/25 
    */ 
    public static void saveToExcel(String excelName, DefaultTableModel tableModel) {
        OutputStream out = null;
        String selectedPath = getSelectedPath();     // 获得用户选择的路径

        try {
            /**
             *      1. 创建输出流（套一个缓冲流，加快速度）--》实际上可以用现有的 Commons I/O 组件，不过一开始还是尽量熟悉源代码比较好
             * */
            // 注意：这里利用字符串拼接的方式获得 Excel表格的路径
            out = new BufferedOutputStream(new FileOutputStream(selectedPath + "\\" + excelName + ".xlsx"));

            //  2. 创建 ExcelWriter 对象，通过 OutPutStream 对象和 需要生成的 Excel 表格类型为参数创建
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

            //  3. 设置 Sheet；这里需要通过反射加载 UserScoreModel 类 （虽然不知道为什么，但是它的参数列表就是这样写的...）
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName("sheet1");      // 设置 sheet 的名字

            // 4. 将需要导出的数据存放到双层 List嵌套列表中去，模拟二维数组
            List<List<String>> data = prepareData(tableModel);        // data中的每一个元素都代表一行数据

            // 5. 生成表头，因为表头可以是 多行，复杂的表头，所以这里也是用 List 嵌套。
            List<List<String>> head = prepareHead();

            // 6. 实例化 Table 对象，将 表头 作为参数调用 setHead() 方法
            Table table = new Table(1);
            table.setHead(head);

            // 7. 生成 Excel 表格
            writer.write0(data, sheet1, table);

            // 8. 刷新流
            writer.finish();
            out.flush();
            System.out.println("导出成功！！！");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    
    /** 
    * @Description: 为Excel准备数据，即被 saveToExcel 调用，返回一个 List 嵌套的集合 
    * @Param: [] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/12/25 
    */ 
    public static List<List<String>> prepareData(DefaultTableModel tableModel) {
        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            List<String> item = new ArrayList<>();      // 数据库中有12列数据，id不需要导出，所以这里共有11列数据
            item.add(tableModel.getValueAt(i, 0).toString());
            item.add(tableModel.getValueAt(i, 1).toString());
            item.add(tableModel.getValueAt(i, 2).toString());
            item.add(tableModel.getValueAt(i, 3).toString());
            item.add(tableModel.getValueAt(i, 4).toString());
            item.add(tableModel.getValueAt(i, 5).toString());
            item.add(tableModel.getValueAt(i, 6).toString());
            item.add(tableModel.getValueAt(i, 7).toString());
            item.add(tableModel.getValueAt(i, 8).toString());
            item.add(tableModel.getValueAt(i, 9).toString());
            item.add(tableModel.getValueAt(i, 10).toString());
            data.add(item);     // 将这一行数据添加到 Excel 中
        }
        return data;
    }
    
    /** 
    * @Description: 为 Excel 表格准备表头，被 saveToExcel 调用，返回存放着 表头 的List嵌套集合
    * @Param: [] 
    * @return: java.util.List<java.util.List<java.lang.String>> 
    * @Author: 林凯
    * @Date: 2019/12/25 
    */ 
    public static List<List<String>> prepareHead() {
        // 5. 生成表头，因为表头可以是 多行，复杂的表头，所以这里也是用 List 嵌套。
        List<List<String>> head = new ArrayList<List<String>>();    // 因为一共有11列(不需要ID那一列)
        List<String> headColumn1 = new ArrayList<>();
        List<String> headColumn2 = new ArrayList<>();
        List<String> headColumn3 = new ArrayList<>();
        List<String> headColumn4 = new ArrayList<>();
        List<String> headColumn5 = new ArrayList<>();
        List<String> headColumn6 = new ArrayList<>();
        List<String> headColumn7 = new ArrayList<>();
        List<String> headColumn8 = new ArrayList<>();
        List<String> headColumn9 = new ArrayList<>();
        List<String> headColumn10 = new ArrayList<>();
        List<String> headColumn11 = new ArrayList<>();
        headColumn1.add("Title");
        headColumn2.add("Type");
        headColumn3.add("Content");
        headColumn4.add("OptionA");
        headColumn5.add("OptionB");
        headColumn6.add("OptionC");
        headColumn7.add("OptionD");
        headColumn8.add("Answer");
        headColumn9.add("Judge");
        headColumn10.add("Remarks");
        headColumn11.add("ChangeTime");
        head.add(headColumn1);
        head.add(headColumn2);
        head.add(headColumn3);
        head.add(headColumn4);
        head.add(headColumn5);
        head.add(headColumn6);
        head.add(headColumn7);
        head.add(headColumn8);
        head.add(headColumn9);
        head.add(headColumn10);
        head.add(headColumn11);
        return head;
    }

    /**
     * @Description: 通过 JFileChooser 获得需要保存的文件路径, 被 saveToExcel 调用
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
