/*
 * Created by JFormDesigner on Sun Nov 17 20:11:08 CST 2019
 */

package paperadministration;

/*
*       2019年11月17日20:28:52
*       对应UI：在  “试卷管理”  界面点击 “编辑试卷”之后，再点击  “添加新的题目到试卷中”  所弹出来的UI
*       调用关系： 在 EditPaper 中被创建对象，调用
* */

import java.awt.event.*;
import jdbc.SubjectJDBC;
import subjectadministration.SearchSubject;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class AddSubjectToPaper extends JFrame {
    private JFrame fatherFrame;     // 父窗体的引用
    private DefaultTableModel tableModelOfFather;       //  父窗体表格模型的引用
    private SubjectJDBC subjectJDBC;

    // 自己添加的控件（添加一些 JFormdesigner 不方便操作的空间）
    private JTable tableSubject;
    private JScrollPane scrollPaneSubject;
    private DefaultTableModel tableModel;


    public AddSubjectToPaper(JFrame fatherFrame, DefaultTableModel tableModelOfFather) {
        this.fatherFrame = fatherFrame;
        this.tableModelOfFather = tableModelOfFather;
        initComponents();
        // 1.初始化面板中的表格，将所有的试题添加到面板中，让用户选择需要添加到试卷中的试题
        initJTable();
    }

    /**
    * @Description:  1.初始化面板中的表格，将所有的试题添加到面板中，让用户选择需要添加到试卷中的试题
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void initJTable() {
        System.out.println("initSubjectTable");
        String[] columNames = {"题目编号(标题)", "题目类型", "题目内容", "选项A", "选项B", "选项C", "选项D", "正确答案", "真假值", "题目备注", "最后修改时间"};
//        SubjectJDBC subjectJDBC = new SubjectJDBC("单选题", "构造方法何时被调用？", "类定义时", "创建对象时", "调用对象方法时", "使用对象的变量时", "A", "目的是让学生了解构造方法");
        subjectJDBC = new SubjectJDBC();
        // 这里没有使用表格模型，直接用的表格
        String[][] datas = subjectJDBC.readSubject("select * from subject");

        tableModel = new DefaultTableModel(datas, columNames);         // tableModel 对应有 11 列
        tableSubject = new JTable(tableModel);
        tableModel.setRowCount(datas.length);       // 设置表格的行数
        tableSubject.setRowHeight(50);      // 设置行高30像素
        // 注意，向JScrollPane中添加控件只能在初始化的时候添加
        scrollPaneSubject = new JScrollPane(tableSubject);

        // 将   表格  添加到对应的面板中，这里使用的是  BorderLayout   ，添加到中间即可
        this.add(scrollPaneSubject);
//        scrollPaneSubject.add(tableSubject);       // 将表格添加到滚动面板中----》   无效
        for (int row = 0; row < datas.length; row++) {
            for (int column = 0; column< datas[0].length; column++) {
                tableSubject.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
            }
        }
        tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
    }

    /**
    * @Description: 窗体关闭时自动调用的方法，让父类窗口可见
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        fatherFrame.setEnabled(true);
    }

    /**
    * @Description: 点击  “筛选试题”  对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void buttonSearchSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        SearchSubject searchSubject = new SearchSubject(this, tableModel);
        searchSubject.setVisible(true);
        this.setEnabled(false);     // 设置当前窗口不可操作
    }

    /**
    * @Description: 点击   “添加选中的试题到试卷中”  对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void buttonAddSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        panel1 = new JPanel();
        label1 = new JLabel();
        buttonSearchSubject = new JButton();
        buttonAddSubject = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u6dfb\u52a0\u65b0\u7684\u8bd5\u9898\u5230\u8bd5\u5377\u4e2d");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border
            .EmptyBorder(0,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax
            .swing.border.TitledBorder.BOTTOM,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,
            12),java.awt.Color.red),panel1. getBorder()));panel1. addPropertyChangeListener(new java.beans
            .PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062order".equals(e.
            getPropertyName()))throw new RuntimeException();}});
            panel1.setLayout(null);

            //---- label1 ----
            label1.setText(" ");
            panel1.add(label1);
            label1.setBounds(0, 0, label1.getPreferredSize().width, 75);

            //---- buttonSearchSubject ----
            buttonSearchSubject.setText("\u7b5b\u9009\u8bd5\u9898");
            buttonSearchSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonSearchSubject.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonSearchSubjectMouseReleased(e);
                }
            });
            panel1.add(buttonSearchSubject);
            buttonSearchSubject.setBounds(90, 20, 120, 40);

            //---- buttonAddSubject ----
            buttonAddSubject.setText("\u6dfb\u52a0\u9009\u4e2d\u7684\u8bd5\u9898\u5230\u8bd5\u5377\u4e2d");
            buttonAddSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonAddSubject.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonAddSubjectMouseReleased(e);
                }
            });
            panel1.add(buttonAddSubject);
            buttonAddSubject.setBounds(280, 20, 245, 40);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel1, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        this.setBounds(490, 170, 1100, 730);

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JPanel panel1;
    private JLabel label1;
    private JButton buttonSearchSubject;
    private JButton buttonAddSubject;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
