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

import jdbc.PaperJDBC;
import jdbc.SubjectJDBC;
import subjectadministration.SearchSubject;

import java.awt.*;
import java.sql.Timestamp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class AddSubjectToPaper extends JFrame {
    private JFrame fatherFrame;     // 父窗体的引用
    private DefaultTableModel tableModelOfEditPaper;       //  父窗体表格模型的引用( 对应的是  编辑试卷   的表格模型的引用)
    private DefaultTableModel tableModelOfMainUI;       // MainUI  界面中的表格模型的引用
    private SubjectJDBC subjectJDBC;
    private String editPaperTitle;      // 正在编辑的试卷的 Title
    private int mainUISelectedRow;      // 在  MainUI  中选中的行数


    // 自己添加的控件（添加一些 JFormdesigner 不方便操作的空间）
    private JTable tableSubject;
    private JScrollPane scrollPaneSubject;
    private DefaultTableModel tableModel;




    public AddSubjectToPaper(JFrame fatherFrame, DefaultTableModel tableModelOfEditPaper, DefaultTableModel tableModelOfMainUI, int mainUISelectedRow) {
        this.fatherFrame = fatherFrame;
        this.tableModelOfEditPaper = tableModelOfEditPaper;
        this.tableModelOfMainUI = tableModelOfMainUI;
        this.mainUISelectedRow = mainUISelectedRow;
        this.editPaperTitle = tableModelOfMainUI.getValueAt(mainUISelectedRow, 0).toString();
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
    * @Description: 点击   “添加选中的试题到试卷中” ---> 试卷已经创建了，只要修改里面的 subjectTitle 字段等，就相当于添加了试题  对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void buttonAddSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 1. 首先获取已经选中了哪些试题
        int[] selectedRows = tableSubject.getSelectedRows();
        // 1.1. 获取选中试题的 Title
        String[] chooseTitles = new String[selectedRows.length];
        System.out.print("chooseTItles: ");
        for (int i = 0; i < selectedRows.length; i++) {
            chooseTitles[i] = tableModel.getValueAt(selectedRows[i], 0).toString();
            System.out.print(chooseTitles[i] + "  ");
        }
        System.out.println("~~~~~");

        // 2.判断用户选中的 Title 是否重复
        String[] paperAlreadySubjectTitle = new String[tableModelOfEditPaper.getRowCount()];       // 试卷中已经存在的试题 Title
        System.out.print("paperAlready:  ");
        for (int i = 0; i < tableModelOfEditPaper.getRowCount(); i++) {
            // 2.1 获取试卷中已经存在的 Title
            paperAlreadySubjectTitle[i] = tableModelOfEditPaper.getValueAt(i, 0).toString();
            System.out.print(paperAlreadySubjectTitle[i] + "  ");
        }

        System.out.println("#####");
        // 2.2 检测是否存在相同的 Title
        for (int i = 0; i < chooseTitles.length; i++) {
            for (int j = 0; j < paperAlreadySubjectTitle.length; j++) {
                if (chooseTitles[i].equals(paperAlreadySubjectTitle[j])) {
                    JOptionPane.showMessageDialog(this, "部分试题已经添加到试卷中了！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        // 3.1 更新该试卷中数据库的  subjectTitle  字段，
        String title = editPaperTitle;      // 获取正在编辑的试卷的 Title
        StringBuilder subjectTitle = new StringBuilder();       // 更新之后该试卷对应的  subjectTitle 字段
        for (int i = 0; i < paperAlreadySubjectTitle.length; i++) {
            subjectTitle.append(paperAlreadySubjectTitle[i] + "-");
        }
        for (int i = 0; i < chooseTitles.length; i++) {
            subjectTitle.append(chooseTitles[i] + "-");
        }
        System.out.println(subjectTitle.toString());
        String subjectNumber = String.valueOf(paperAlreadySubjectTitle.length + chooseTitles.length);
        Timestamp changeTime = new Timestamp(System.currentTimeMillis());
        System.out.println("~~~~~~~~~~~~~~~~~");
        System.out.println(subjectNumber + " ## " +  subjectTitle.toString() + " ## " + changeTime + "  ##  " + title);
        // 执行 JDBC 操作
        PaperJDBC.updatePapers(subjectNumber, subjectTitle.toString(), changeTime, title);

        // 3.2 更新 “编辑试卷” 窗口中的  表格中的信息，表格里面存储的是该试卷中已经添加的  试题信息
        String[] subjectOfPaper = new String[11];       // 需要显示 11 列信息表示一道试题
        String sql = setSQL(chooseTitles);
        String[][] result = subjectJDBC.readSubject(sql);       // 获得需要添加的试题对应的 二维数组
        for (int i = 0; i < chooseTitles.length; i++) {
            tableModelOfEditPaper.addRow(result[i]);       // 添加的时候是添加 一维数组，所以只要添加得到的二维数组的一行
        }

        // 3.3 更新  MainUI  界面中表格的信息，表格里面存储的是  试卷
        tableModelOfMainUI.setValueAt(subjectNumber, mainUISelectedRow, 1);
        tableModelOfMainUI.setValueAt(subjectTitle.toString(), mainUISelectedRow, 2);
        tableModelOfMainUI.setValueAt(changeTime.toString(), mainUISelectedRow, 3);


    }

    /**
    * @Description: 根据传进来的题目的 Title  数组，生成查询的  SQL  语句
    * @Param: [paperAlreadySubjectTitle]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/18
    */
    private String setSQL(String[] paperAlreadySubjectTitle) {
        // sql 语句中的字符串要用  ‘’  括起来
        StringBuilder sql = new StringBuilder("select * from subject where title = '" + paperAlreadySubjectTitle[0] + "' ");

        for (int i = 1; i < paperAlreadySubjectTitle.length; i++) {
            sql.append("or title = '" + paperAlreadySubjectTitle[i] + "' ");
        }
        System.out.println(sql.toString());
        return sql.toString();
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
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
            . EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax
            . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
            12 ), java. awt. Color. red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans
            . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .
            getPropertyName () )) throw new RuntimeException( ); }} );
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
