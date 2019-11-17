/*
 * Created by JFormDesigner on Sun Nov 17 18:49:00 CST 2019
 */

package paperadministration;

import jdbc.PaperJDBC;
import org.apache.commons.lang.text.StrBuilder;
import subjectadministration.MyClander;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class SearchPaper extends JFrame {
    private JFrame fatherFrame;     // 父窗口的引用
    private DefaultTableModel tableModel;       // 父窗口表格模型的引用
    private long beginTime = 0;     // 时间范围内的开始时间
    private long endTime = 0;       // 时间范围内的结束时间

    public SearchPaper(JFrame fatherFrame, DefaultTableModel tableModel) {
        this.fatherFrame = fatherFrame;
        this.tableModel = tableModel;
        initComponents();
    }

    /**
     * @Description: 选择时间按钮对应的点击事件---》 选择起始时间
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/10
     */
    private void buttonChooseTimeBeginMouseReleased(MouseEvent e) {
        // TODO add your code here
        MyClander myClander = new MyClander(textFieldDateBegin);
        myClander.setVisible(true);
    }

    /**
     * @Description: 选择时间按钮对应的点击事件----》 选择结束时间
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/10
     */
    private void buttonChooseTimeEndMouseReleased(MouseEvent e) {
        // TODO add your code here
        MyClander myClander = new MyClander(textFieldDateEnd);
        myClander.setVisible(true);
    }

    /**
    * @Description: 点击   “筛选试卷”  按钮对应的点击事件 ---》 从数据库中筛选符合要求的试卷，更新到表格中
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void buttonSureMouseReleased(MouseEvent e) {
        // TODO add your code here
        /*
         *       首先：检测用户输入的时间是否合法
         *       通过 DateFormat 类将时间转换为 long 类型的毫秒数
         * */
        SimpleDateFormat begin = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            Date dateBegin = begin.parse(textFieldDateBegin.getText());
            Date dateEnd = end.parse(textFieldDateEnd.getText());
            beginTime = dateBegin.getTime();
            endTime = dateEnd.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if (beginTime > endTime) {
            // 如果开始时间大于结束事件，弹出警告框
            JOptionPane.showMessageDialog(this, "时间选择错误！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回，不在执行后面的语句
        }

        String sql = setMySQL();        // 获得对应的 sql 语句
        String[][] tempDatas = PaperJDBC.getAllPapers(sql);


        // 将数组中的内容设置到表格当中
        tableModel.setRowCount(tempDatas.length);       // 重新设置表格的行数

        for (int row = 0; row < tempDatas.length; row++) {
            for (int column = 0; column< tempDatas[0].length; column++) {
                tableModel.setValueAt(tempDatas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
            }
        }
        tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误

//        fatherFrame.setEnabled(true);       // 设置父窗口可以操作
        this.dispose();     // 设置本窗口隐藏

    }

    /**
    * @Description: 根据用户输入的筛选条件生成对应的查询 sql 语句
    * @Param: []
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private String setMySQL() {
        StringBuilder sql = new StringBuilder("select * from examinationpaper where ");

        sql.append("title like '%" + textFieldTitle.getText() + "%' ");

        /*
         *       处理 “最后修改时间”,要求两个时间框中都有时间时才添加关于时间查询的 SQL 语句
         *       时间戳的比较，有3种方式，见博客：https://blog.csdn.net/wide288/article/details/37744773
         * */

        if (beginTime <= endTime && (!textFieldDateBegin.getText().equals("")) && (!textFieldDateEnd.getText().equals(""))) {
//            sql.append("and changeTime >= " + textFieldDateBegin.getText() + " " + "and changeTime <= " + textFieldDateEnd.getText());
            sql.append("and (changeTime between " +  "'" + textFieldDateBegin.getText() + "'" + " and " + "'" + textFieldDateEnd.getText() + "')");
        }

        return sql.toString();
    }

    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        fatherFrame.setEnabled(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        label4 = new JLabel();
        textFieldDateBegin = new JTextField();
        buttonChooseTimeBegin = new JButton();
        textFieldDateEnd = new JTextField();
        label5 = new JLabel();
        buttonChooseTimeEnd = new JButton();
        label1 = new JLabel();
        textFieldTitle = new JTextField();
        buttonSure = new JButton();
        separator1 = new JSeparator();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label4 ----
        label4.setText("\u6700\u7ec8\u4fee\u6539\u65f6\u95f4\u8303\u56f4\uff1a");
        label4.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label4);
        label4.setBounds(35, 165, 155, 40);

        //---- textFieldDateBegin ----
        textFieldDateBegin.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        textFieldDateBegin.setEditable(false);
        contentPane.add(textFieldDateBegin);
        textFieldDateBegin.setBounds(200, 165, 205, 40);

        //---- buttonChooseTimeBegin ----
        buttonChooseTimeBegin.setText("\u9009\u62e9\u65f6\u95f4");
        buttonChooseTimeBegin.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        buttonChooseTimeBegin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonChooseTimeBeginMouseReleased(e);
            }
        });
        contentPane.add(buttonChooseTimeBegin);
        buttonChooseTimeBegin.setBounds(240, 220, 110, 35);

        //---- textFieldDateEnd ----
        textFieldDateEnd.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        textFieldDateEnd.setEditable(false);
        contentPane.add(textFieldDateEnd);
        textFieldDateEnd.setBounds(500, 165, 210, 40);

        //---- label5 ----
        label5.setText("\u5230");
        label5.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label5);
        label5.setBounds(445, 175, 25, 25);

        //---- buttonChooseTimeEnd ----
        buttonChooseTimeEnd.setText("\u9009\u62e9\u65f6\u95f4");
        buttonChooseTimeEnd.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        buttonChooseTimeEnd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonChooseTimeEndMouseReleased(e);
            }
        });
        contentPane.add(buttonChooseTimeEnd);
        buttonChooseTimeEnd.setBounds(555, 225, 105, 33);

        //---- label1 ----
        label1.setText("\u8bd5\u5377\u7f16\u53f7(\u6807\u9898)\uff1a");
        label1.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label1);
        label1.setBounds(55, 65, 135, 45);

        //---- textFieldTitle ----
        textFieldTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(textFieldTitle);
        textFieldTitle.setBounds(200, 65, 190, 45);

        //---- buttonSure ----
        buttonSure.setText("\u7b5b\u9009\u8bd5\u5377");
        buttonSure.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        buttonSure.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonSureMouseReleased(e);
            }
        });
        contentPane.add(buttonSure);
        buttonSure.setBounds(290, 370, 160, 50);
        contentPane.add(separator1);
        separator1.setBounds(0, 325, 785, 2);

        contentPane.setPreferredSize(new Dimension(785, 505));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        // 设置窗口大小
        this.setSize(785, 505);


    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JLabel label4;
    private JTextField textFieldDateBegin;
    private JButton buttonChooseTimeBegin;
    private JTextField textFieldDateEnd;
    private JLabel label5;
    private JButton buttonChooseTimeEnd;
    private JLabel label1;
    private JTextField textFieldTitle;
    private JButton buttonSure;
    private JSeparator separator1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
