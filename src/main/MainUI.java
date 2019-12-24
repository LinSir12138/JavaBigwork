/*
 * Created by JFormDesigner on Fri Oct 25 18:37:11 CST 2019
 */

package main;

import com.spire.barcode.BarcodeScanner;
import com.spire.barcode.implementation.generator.generation.BarcodeCreator;
import examministration.AddExam;
import examministration.StartExam;
import facerecognition.WebcamCapture;
import javabean.User;
import jdbc.*;
import paperadministration.EditPaper;
import paperadministration.SearchPaper;
import subjectadministration.AddSubject;
import subjectadministration.EditSubject;
import subjectadministration.PreviewSubject;
import subjectadministration.SearchSubject;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

/*
*       2019年11月10日09:17:23
*       对应UI：登录之后显示的主界面
* */

/**
 * @author Lin Kai
 */
public class MainUI extends JFrame {
    private String userName;            // 对应的用户名，来识别那个用户登录了系统
    private String userPhoneNumber;     // 用户名对应的手机号，在构造方法中初始化
    private SubjectJDBC subjectJDBC;        // 题目管理执行JDBC操作时，对应JDBC操作的对象

    // 自己添加的控件（添加一些 JFormdesigner 不方便操作的空间）
    /**
    *       表格，表格模型，滚动面板的引用（用来统一管理，因为有3个界面）
     *      已经确保了3个界面里的表格，表格模型，滚动面板只有唯一的一个对象，
     *      分别用table，scrollPane，tableModel 加上序号1，2，3来表示
     *      还需要一个统一的引用来使用（方便操作）
    * */
    private JTable tableSubject;
    private JScrollPane scrollPaneSubject;
    private DefaultTableModel tableModel;            // tableModel 对应有 11 列，包括题目的id，changeTime
    // 1，2,3 分别对应 “试题”“试卷”“考试”界面的表格，滚动面板，表格模型（只能创建一个对象）
    private JTable table01;
    private JScrollPane scrollPane01;
    private DefaultTableModel tableModel01;
    private JTable table02;
    private JScrollPane scrollPane02;
    private DefaultTableModel tableModel02;
    private JTable table03;
    private JScrollPane scrollPane03;
    private DefaultTableModel tableModel03;
    // 4. 下面的是 “用户管理” 和 “成绩查询” 界面的表格，滚动面板，表格模型（同样只能够创建一个对象）
    private JTable table04, table05;
    private JScrollPane scrollPane04, scrollPane05;
    private DefaultTableModel tableModel04, tableModel05;

    public MainUI(String phoneNumber) {
        this.userPhoneNumber = phoneNumber;     // 保存用户名对应的手机号
        UserJDBC userJDBC = new UserJDBC();     // 创建 JDBC 对象，执行对应的 JDBC操作，可以封装成为工具类的
        this.userName = userJDBC.readNameByPhoneNumber(phoneNumber);   // 根据手机号查找用户名
        initComponents();

        // 初始化用户，根据用户类型（学生，教师）来更改界面的设计
        initUser();
    }

    /**
    * @Description: 根据不同的用户设置不同的权限
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/14
    */
    private void initUser() {
        if (userName.equals("林凯")) {
            /**
             *      管理员用户，什么都不用做，直接返回即可（不继续执行后面的代码）
             * */
            return;
        }

        /**
         *      如果是学生用户，那么只能进行考试，下面设置哪些界面是不可点击的
         * */
        buttonLeft03.setEnabled(false);     // 设置 “学生管理” 按钮不可点击
        buttonLeft04.setEnabled(false);     // 设置 “试题管理” 按钮不可点击
        buttonLeft05.setEnabled(false);     // 设置 “试卷管理” 按钮不可点击
    }

    /** 
    * @Description: 左边的选项按钮对应的点击事件---切换选项卡 
    * @Param: [e] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/10/27 
    */ 
    private void buttonLeftMouseReleased(MouseEvent e) {
        // TODO add your code here
        CardLayout cardLayout = (CardLayout) panelCenter.getLayout();
        JButton button = (JButton) e.getSource();
        if (button.getText().equals("系统首页")) {
            cardLayout.show(panelCenter, "card1");
        } else if (button.getText().equals("个人信息")) {
            cardLayout.show(panelCenter, "card2");
        } else if (button.getText().equals("用户管理")) {
            initUserControl();      //
            cardLayout.show(panelCenter, "card3");
        } else if (button.getText().equals("试题管理")) {
            initSubject();     // 初始化 “试题管理” 界面，这里不需要传递参数
            cardLayout.show(panelCenter, "card4");
        } else if (button.getText().equals("试卷管理")) {
            initPaper();        // 初始化   “试卷管理”  界面
            cardLayout.show(panelCenter, "card5");
        } else if (button.getText().equals("考试管理")) {
            initExam();     // 初始化   “考试管理”  界面
            cardLayout.show(panelCenter, "card6");
        } else if (button.getText().equals("成绩查询")){
            initExamResult();
            cardLayout.show(panelCenter, "card7");
        }
    }

    /**
     * @Description: 在 WebcamCapture 调用此方法，传入的参数为boolean类型，true说明人脸验证成功，flase说明人脸验证失败
     * @Param: [flag]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/12/5
     */
    public void matchResult(boolean flag) {
        if (flag) {
            JOptionPane.showMessageDialog(this, "验证成功！", "成功", JOptionPane.PLAIN_MESSAGE);

            /**
             *      下面执行开始考试的代码
             * */
            long startTime = 0;
            long endTime = 0;
            SimpleDateFormat tiemFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date startDate = tiemFormat.parse(tableModel.getValueAt(tableSubject.getSelectedRow(), 3).toString());
                Date endDate = tiemFormat.parse(tableModel.getValueAt(tableSubject.getSelectedRow(), 4).toString());
                startTime = startDate.getTime();
                endTime = endDate.getTime();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            String examName = tableModel.getValueAt(tableSubject.getSelectedRow(), 0).toString();
            String examPaperName = tableModel.getValueAt(tableSubject.getSelectedRow(), 1).toString();
            String[] paperTitles = PaperJDBC.getPaperSubjectTitles(examPaperName);

            StartExam startExam = new StartExam(this, examName, paperTitles, startTime, endTime);
            this.setEnabled(false);         // 设置当前窗体不可编辑
            startExam.setVisible(true);


        } else {
            JOptionPane.showMessageDialog(this, "验证失败!", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
    * @Description: (出了点下 BUG， 暂时注释掉)将 初始化化表格 的操作封装出来，给每个界面初始化的时候来调用
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/14
    */
//    public void initTable(String[] columnNames, String[][] datas, JTable table, JScrollPane scrollPane, DefaultTableModel tempTableModel, JPanel panel) {
//
//        /**
//         *      保证界面中的表格，表格模型，滚动面板只有一个对象
//         * */
//        if (table == null && scrollPane == null && tempTableModel == null) {
//            System.out.println("new 了一个对象");
//            tempTableModel = new DefaultTableModel(datas, columnNames);
//            table = new JTable(tempTableModel);     // 通过 表格模型 来创建 表格 对象
//            tempTableModel.setRowCount(datas.length);       // 设置表格行数
//            table.setRowHeight(50);     // 设置行高50像素
//            scrollPane = new JScrollPane(table);        //注意，向JScrollPane中添加控件只能在初始化的时候添加
//            panel.add(scrollPane);         // 将   表格  添加到对应的面板中，这里使用的是  BorderLayout   ，添加到中间即可
//            for (int row = 0; row < datas.length; row++) {
//                for (int column = 0; column< datas[0].length; column++) {
//                    table.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
//                }
//            }
//            tempTableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
//        }
//
//        // 重新赋值引用
//        tableSubject = table;
//        tableModel = tempTableModel;
//        scrollPaneSubject = scrollPane05;
//
//    }

// ######################################    下面是 “用户管理” 界面

    /**
     * @Description: 初始化  “用户管理”  界面，类似于一个小型的学生信息管理系统
     * @Param: []
     * @return: void
     * @Author: 林凯
     * @Date: 2019/12/11
     */
    private void initUserControl() {
        /**
         *       注：这里可能用时比较多（快的话用 1 秒，慢的话要2秒），%98 的时间都花在的链接数据库上，
         *       因为数据库放在云上，所以连接的时候和网络有关系。
         **/

        String[] columNames = {"用户名", "手机号", "邮箱账号", "注册日期"};

        UserJDBC userJDBC = new UserJDBC();
        // 获得数据，存放到 datas 二维数组里面
        String[][] tempDates = userJDBC.readAllInformation();     // 从数据库中获取全部的信息（不包含图片哪一列），保存到一个二维数组中
        /**
         *      上面的只是一个临时的数据，包含了处理图片以外的所有数据
         *      下面的二维数组要从临时数据里面选取需要的数据，因为不展示 id，和 pwd，所以列数还有减2
         * */
        String[][] datas = new String[tempDates.length][4];
        for (int i = 0; i < datas.length; i++) {
            datas[i][0] = tempDates[i][1];
            datas[i][1] = tempDates[i][3];
            datas[i][2] = tempDates[i][4];
            datas[i][3] = tempDates[i][5];
        }

        /**
         *      保证界面中的表格，表格模型，滚动面板只有一个对象
         * */
        if (table04 == null && scrollPane04 == null && tableModel04 == null) {
            System.out.println("重新 new 了一个对象");
            tableModel04 = new DefaultTableModel(datas, columNames);
            table04 = new JTable(tableModel04);
            tableModel04.setRowCount(datas.length);     // 设置表格行数
            table04.setRowHeight(50);       // 设置行高50像素
            scrollPane04 = new JScrollPane(table04);        // 注意，向JScrollPane中添加控件只能在初始化的时候添加
            panelContentStudent.add(scrollPane04);      // 将   表格  添加到对应的面板中，这里使用的是  BorderLayout   ，添加到中间即可
            for (int row = 0; row < datas.length; row++) {
                for (int column = 0; column< datas[0].length; column++) {
                    table04.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
                }
            }
            tableModel04.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
        }

        // 重新赋值引用
        tableSubject = table04;
        tableModel = tableModel04;
        scrollPaneSubject = scrollPane04;

    }

//#####################################################################  下面是   试题管理    ##########################

    /**
    * @Description: 初始化 “试题管理”  界面   直接从数据库中读取11列（全部数据），展示到表格中
    * @Param: [tableSubject] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/10/27 
    */ 
    private void initSubject() {
        System.out.println("initSubjectTable");
        String[] columNames = {"题目编号(标题)", "题目类型", "题目内容", "选项A", "选项B", "选项C", "选项D", "正确答案", "真假值", "题目备注", "最后修改时间"};
//        SubjectJDBC subjectJDBC = new SubjectJDBC("单选题", "构造方法何时被调用？", "类定义时", "创建对象时", "调用对象方法时", "使用对象的变量时", "A", "目的是让学生了解构造方法");
        subjectJDBC = new SubjectJDBC();
        // 这里没有使用表格模型，直接用的表格
        String[][] datas = subjectJDBC.readSubject("select * from subject");

        /**
         *      保证界面中的表格，表格模型，滚动面板只有一个对象
         * */
        if (table01 == null && scrollPane01 == null && tableModel01 == null) {
            tableModel01 = new DefaultTableModel(datas, columNames);
            table01 = new JTable(tableModel01);
            tableModel01.setRowCount(datas.length);     // 设置表格行数
            table01.setRowHeight(50);       // 设置行高50像素
            scrollPane01 = new JScrollPane(table01);        // 注意，向JScrollPane中添加控件只能在初始化的时候添加
            panelContentSubject.add(scrollPane01);      // 将   表格  添加到对应的面板中，这里使用的是  BorderLayout   ，添加到中间即可
            for (int row = 0; row < datas.length; row++) {
                for (int column = 0; column< datas[0].length; column++) {
                    table01.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
                }
            }
            tableModel01.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
        }

        // 重新赋值引用
        tableSubject = table01;
        tableModel = tableModel01;
        scrollPaneSubject = scrollPane01;
    }

    /** 
    * @Description: 点击  "试题管理"  界面的  “添加试题” 按钮对的鼠标点击事件
    * @Param: [e] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/10/27 
    */ 
    private void buttonAddSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        AddSubject addSubject;

        // 这里需要用到多线程     或者     在 SubjectUI  里面将数据存储到数据库中，然后MainUI列表里面的值
        /*
        *      这里采用第二种方式，等以后有时间了，在利用多线程
        *       创建 SubjectUI 对象时，需要传递3个参数
        *       第一个参数： 表格的总行数
        *       第二个参数：本窗口的对象，用来在子窗口中设置父窗口（本窗口）不可操作，只能操作子窗口
        *       第3个参数：本窗口的 tableModel (表格模型)，在子窗口中添加题目到表格中
        * */
        addSubject = new AddSubject(tableModel.getRowCount() + 1, this,this.tableModel);
        addSubject.setVisible(true);

        // 设置当前窗口为不可操作状态
        this.setEnabled(false);
    }

    /**
    * @Description: 在  “试题管理”  界面点击  删除试题  按钮对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/28
    */
    private void buttonDeleteSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 特别注意这里，移除元素之后，后面元素的行也会随之变化；而在数据库中由于使用的是事务删除，所以不需要考虑这些

        int[] selectedRows = tableSubject.getSelectedRows();

        // 因为没有选中表格任何行数据时，getSelectedRows() 返回的是 new Int[0]     0个元素的数组
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！可以安装Ctrl + 鼠标左键选择多个需要删除的题目！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        // 弹出提示信息，防止用户勿删
        String str = "确定要删除" + selectedRows.length + "项数据吗？";
        int result = JOptionPane.showConfirmDialog(this, str, "删除提示", 2);
        if (result == 2) {
            return;     // 用户选择了取消
        }

        /*
        *       注意删除的顺序，要向删除数据库中的数据，然后在删除表格中的数据，因为删除删除数据库中的数据时要求表格中的数据保持不变，才方便寻找数据库中的数据
        * */
        // 1.同时获得这些选中行的title，构造一个String类型的数组，用来对数据库里面的元素进行删除
        String[] title = new String[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
//            System.out.println("delete:" + selectedRows[i]);
            // getValueAt(0,0) 表示第一行，第一个元素
//            System.out.println(tableModel.getValueAt(selectedRows[i],0));
            title[i] = tableModel.getValueAt(selectedRows[i], 0).toString();
//            System.out.println("已经选中的title有：" + title[i]);
        }
        // 2.从数据库中删除数据
        boolean flag = subjectJDBC.deleteSubject(title);
        if (flag) {
            JOptionPane.showMessageDialog(this, "删除成功！", "成功！", 1);

            // 将选中的 行 从表格中移除
//            System.out.println("selectedRows.length" + selectedRows.length);
            for (int i = 0; i < selectedRows.length; i++) {
//                System.out.println(selectedRows[i]);
                // 特别注意这里，移除元素之后，后面元素的行也会随之变化；而在数据库中由于使用的是事务删除，所以不需要考虑这些
                tableModel.removeRow(selectedRows[i] - i);
            }

            tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
        } else {
            JOptionPane.showMessageDialog(this, "删除失败！", "失败！", 0);
        }
    }

    /**
    * @Description: 在 “试题管理” 界面点击 “查找试题”按钮对应的点击事件  ——》查找相应的试题，然后在表格中显示出来
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void buttonSearchSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        SearchSubject searchSubject = new SearchSubject(this, tableModel);
        searchSubject.setVisible(true);

        this.setEnabled(false);

    }

    /**
    * @Description: 在 “试题管理” 界面点击   “编辑试题”  按钮对应的点击事件 -----》 编辑鼠标选中的试题，
     *                然后通过 Updata 语句更新数据库中的信息
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/13
    */
    private void buttonEditSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 1.首先获得鼠标选中的行（要求：只能够选中一行，没有选中行或者选中多行显示警告提示用户）
        int[] selectedRows = tableSubject.getSelectedRows();
//        tableSubject.getS

        // 用户选中多行或者没有选中，警告提示用户
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！请先选中需要编辑的题目", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        } else if (selectedRows.length >= 2) {
            JOptionPane.showMessageDialog(this, "一次只能编辑一道题目！！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        EditSubject editSubject = new EditSubject(this, this.tableModel, selectedRows[0]);
        editSubject.setVisible(true);

        this.setEnabled(false);

    }

    /**
    * @Description: 在   “试题管理”  界面点击   “查看试题”  按钮对应的点击事件 -----》 显示试题的详细信息
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/16
    */
    private void buttonCheckSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        // TODO add your code here
        // 1.首先获得鼠标选中的行（要求：只能够选中一行，没有选中行或者选中多行显示警告提示用户）
        int[] selectedRows = tableSubject.getSelectedRows();

        // 用户选中多行或者没有选中，警告提示用户
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！请先选中需要编辑的题目", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        } else if (selectedRows.length >= 2) {
            JOptionPane.showMessageDialog(this, "一次只能编译一道题目！！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        PreviewSubject previewSubject = new PreviewSubject(this, tableModel, selectedRows[0]);
        previewSubject.setVisible(true);

        this.setEnabled(false);

    }

//######################  下面是   试卷管理   #########################################################################

    /**
     * @Description: 初始化   “试卷管理”  界面
     * @Param: []
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/16
     */
    private void initPaper() {
        System.out.println("initPaper");
        String[] columNames = {"试卷编号(标题)", "包含试题数量", "所包含试题编号(标题)", "最后修改时间"};
        String[][] datas = PaperJDBC.getAllPapers();

        // 重要，类似于单例模式，确保每个表格类在整个程序执行过程中只有一个对象

        if (table02 == null && scrollPane02 == null && tableModel02 == null) {
            tableModel02 = new DefaultTableModel(datas, columNames);      // tableModel 对应有 11 列
            tableModel02.setRowCount(datas.length);       // 设置表格的行数
            table02 = new JTable(tableModel02);
            table02.setRowHeight(50);        // 设置行高50像素
            scrollPane02 = new JScrollPane(table02);       // 注意，向JScrollPane中添加控件只能在初始化的时候添加
            panleContentPaper.add(scrollPane02);

            for (int row = 0; row < datas.length; row++) {
                for (int column = 0; column< datas[0].length; column++) {
                    table02.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
                }
            }
            tableModel02.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误

        }

        // 更新引用指向的对象
        tableSubject = table02;
        tableModel = tableModel02;
        scrollPaneSubject = scrollPane02;
    }

    /**
     * @Description: 在   “试卷管理”  界面点击   “新建试卷”   对应的点击事件-----》利用 输入对话框直接添加
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/17
     */
    private void buttonAddPaperMouseReleased(MouseEvent e) {
        // TODO add your code here
        String paperName = JOptionPane.showInputDialog(this,"请输入试卷名称","输入试卷名称",1);

        // 1. 首先判断用户输入的  试卷标题  是否重复，如果重复，则提示用户
        String[] tempTitle = PaperJDBC.readPaperTitle();
        List<String> list = new ArrayList<>();
        Collections.addAll(list, tempTitle);
        if (list.contains(paperName)) {
            // 如果 试卷标题  重复，则弹出提示框
            JOptionPane.showMessageDialog(this, "试卷编号（标题）重复！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 不在执行后面的语句
        }

        // 2. 用户输入的   “试卷标题”   不重复，更新表格中的数据，同时更新数据库里面的数据
//        System.out.println(paperName);
        if (paperName != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] dataToTable = {paperName, "0", "", timestamp.toString()};
            tableModel.addRow(dataToTable);
            tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误


            String[] dataToDatabase = {paperName, "0", ""};     // 存储到数据库中，第4个元素是 Timestamp 类型，要单独传递
            PaperJDBC.addPaper(dataToDatabase, timestamp);
        }



//        AddPaper addPaper = new AddPaper(this);
//        addPaper.setVisible(true);
//
//        // 设置当前窗口不可操作
//        this.setEnabled(false);

    }

    /**
     * @Description: 在   “试卷管理”  界面点击   “删除试卷”   按钮对应的点击事件----》 删除选定的试卷
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/17
     */
    private void buttonDeletePaperMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 特别注意这里，移除元素之后，后面元素的行也会随之变化；而在数据库中由于使用的是事务删除，所以不需要考虑这些
        // 1.获得选中的试卷
        int[] selectedRows = tableSubject.getSelectedRows();

        // 因为没有选中表格任何行数据时，getSelectedRows() 返回的是 new Int[0]     0个元素的数组
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！可以安装Ctrl + 鼠标左键选择多个需要删除的题目！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        // 弹出提示信息，防止用户勿删
        String str = "确定要删除" + selectedRows.length + "项数据吗？";
        int result = JOptionPane.showConfirmDialog(this, str, "删除提示", 2);
        if (result == 2) {
            return;     // 用户选择了取消
        }

        /*
         *       注意删除的顺序，要向删除数据库中的数据，然后在删除表格中的数据，因为删除删除数据库中的数据时要求表格中的数据保持不变，才方便寻找数据库中的数据
         * */
        // 1.同时获得这些选中行的title，构造一个String类型的数组，用来对数据库里面的元素进行删除
        String[] title = new String[selectedRows.length];
//        for (int i = 0; i < selectedRows.length; i++) {
////            System.out.println("delete:" + selectedRows[i]);
//            // getValueAt(0,0) 表示第一行，第一个元素
//            System.out.println(tableModel.getValueAt(selectedRows[i],0));
//            title[i] = tableModel.getValueAt(selectedRows[i], 0).toString();
//            System.out.println("已经选中的title有：" + title[i]);
//        }
        // 2.从数据库中删除数据
        boolean flag = PaperJDBC.deletePaper(title);
        if (flag) {
            JOptionPane.showMessageDialog(this, "删除成功！", "成功！", 1);

            // 将选中的 行 从表格中移除
//            System.out.println("selectedRows.length" + selectedRows.length);
            for (int i = 0; i < selectedRows.length; i++) {
//                System.out.println(selectedRows[i]);
                // 特别注意这里，需要减一个 i ，因为移除元素之后，后面元素的行也会随之变化；而在数据库中由于使用的是事务删除，所以不需要考虑这些
                tableModel.removeRow(selectedRows[i] - i);
            }

            tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
        } else {
            JOptionPane.showMessageDialog(this, "删除失败！", "失败！", 0);
        }

    }

    /**
     * @Description: 在   “试卷管理”  界面点击   “重命名试卷”  按钮对应点击事件 ----》 重命名试卷的名字
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/26
     */
    private void buttonRenamePaperMouseReleased(MouseEvent e) {
        // TODO add your code here

        // 1. 首先判断用户是否已经选中了某张试卷，如果没有选中或者选中的多张试卷，则提示用户重新选择
        int[] selectedRows =  tableSubject.getSelectedRows();
//        System.out.println(selectedRows.length);
        if (selectedRows.length > 1) {
            // 选中了多张试卷
            JOptionPane.showMessageDialog(this, "一次只能重命名一张试卷！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (selectedRows.length == 0) {
            // 没有选中试卷
            JOptionPane.showMessageDialog(this, "请先选择需要重命名的试卷！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回，不继续执行下面
        }

        // 2. 然后把原来试卷的名称先保存起来
//        String oldName = tableSubject.getSelectedRow();
        String oldName = tableModel.getValueAt(tableSubject.getSelectedRow(), 0).toString();
//        System.out.println("oldName = " + oldName);

        String newName = JOptionPane.showInputDialog(this,"请输入试卷名称","输入试卷名称",1);
//        System.out.println("newName = " + newName);

        // 3. 首先判断该试卷名称是否合法（即不能存在两张试卷的试卷名称一样的情况）
        String[] oldPaperNameList =  PaperJDBC.readPaperTitle();     // 首先获得已经存在的试卷名称
        ArrayList<String> namelist = new ArrayList<>();     // 将 String数组存放的 ArrayList 中，方便后面检测是否存在相同的元素
        Collections.addAll(namelist, oldPaperNameList);
        namelist.remove(oldName);       // 将老的试卷的名字从列表中移除

        if (newName.equals(oldName)) {
            // 如果重命名的名字和原来的名字一样，这认为重命名成功
            JOptionPane.showMessageDialog(this, "重命名成功！", "成功！", 1);
        } else if (namelist.contains(newName)) {
            JOptionPane.showMessageDialog(this, "重命名失败，可能存在重复名称，请修改之后再进行尝试！", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            // 如果新名字即不和原来的名字相同，在数据库中也不存在同名的，那么说明该名字可以使用
            JOptionPane.showMessageDialog(this, "重命名成功！", "成功！", 1);

            // 3. 接下来就是修改数据库中的信息，和UI界面中表格的信息
            // 3.1 更新数据库中的信息
            PaperJDBC.updatePaperName(oldName, newName);
            // 3.2 更新UI界面表格中的信息
            tableModel.setValueAt(newName, selectedRows[0], 0);
        }
    }


    /**
     * @Description: 在   “试卷管理”  界面点击   “筛选试卷”   按钮对应的点击事件
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/17
     */
    private void buttonSearchPaperMouseReleased(MouseEvent e) {
        // TODO add your code here
        SearchPaper searchPaper = new SearchPaper(this, tableModel);
        searchPaper.setVisible(true);
        this.setEnabled(false);

    }

    /**
     * @Description: 在   “试卷管理”  界面点击   “编辑试卷”  按钮对应的点击事件----》 可以向试卷里面添加或者删除试题
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/17
     */
    private void buttonEditPaperMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 1.首先获得鼠标选中的行（要求：只能够选中一行，没有选中行或者选中多行显示警告提示用户）
        System.out.println("2--hascode:" + tableSubject.hashCode());
        int[] selectedRows = tableSubject.getSelectedRows();
        System.out.println("选中的函数" + selectedRows.length);

        // 用户选中多行或者没有选中，警告提示用户
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！请先选中需要编辑的题目", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        } else if (selectedRows.length >= 2) {
            JOptionPane.showMessageDialog(this, "一次只能编辑一道题目！！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        EditPaper editPaper = new EditPaper(this, tableModel, selectedRows[0]);
        editPaper.setVisible(true);
        this.setEnabled(false);
    }

    /**
     * @Description: 在   “试卷管理”  界面点击   “预览试卷”  按钮对应的点击事件  ---》 预览事件
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/26
     */
    private void buttonPreviewPaperMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 1.首先获得已经选中的那张试卷
        int[] selectedRows = tableSubject.getSelectedRows();

        // 用户选中多行或者没有选中，警告提示用户
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！请先选中需要编辑的题目", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        } else if (selectedRows.length >= 2) {
            JOptionPane.showMessageDialog(this, "一次只能编辑一道题目！！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        // 2. 获得选中的那张试卷的 subjectTitle 字段，
        String[] subjectTitle = tableModel.getValueAt(tableSubject.getSelectedRow(), 2).toString().split("-");


    }

//######################  下面是   考试管理   #########################################################################

    /**
     * @Description: 初始化   “考试管理”   界面
     * @Param: []
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/27
     */
    private void initExam() {
        System.out.println("initExam!");
        String[] columNames = {"考试名称", "对应试卷", "参加考试人数", "考试开始时间", "考试结束时间"};
        String[][] datas = ExamJDBC.getAllExam();

        if (datas == null) {
            JOptionPane.showMessageDialog(this, "暂时没有考试哦！", "错误", 0);
//            return;         // 如果一场考试都没有发布，那么显示为空
        }

        if (table03 == null && tableModel03 == null && scrollPane03 == null) {
            tableModel03 = new DefaultTableModel(datas, columNames);        // tableModel 对应有 11 列
            table03 =new JTable(tableModel03);
            tableModel03.setRowCount(datas.length);     // 设置表格的行数
            table03.setRowHeight(50);       // 设置行高为50像素
            scrollPane03 = new JScrollPane(table03);        // 注意，向JScrollPane中添加控件只能在初始化的时候添加
            panelContentExam.add(scrollPane03);
            for (int row = 0; row < datas.length; row++) {
                for (int column = 0; column< datas[0].length; column++) {
                    table03.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
                }
            }
            tableModel03.fireTableDataChanged();       // 更新表格的信息，十分必要，否则可能显示错误
        }

        // 重新赋值引用
        tableSubject = table03;
        tableModel = tableModel03;
        scrollPaneSubject = scrollPane03;

    }

    /**
     * @Description: 在   “考试管理”  界面点击  “发布考试”  按钮对应的点击事件     ———》  发布新的考试
     *                同时还要调用 UserJDBC 类的 addColmn() 方法，动态得在 User 表中添加 1 列数据
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/27
     */
    private void buttonAddExamMouseReleased(MouseEvent e) {
        // TODO add your code here
        AddExam addExam = new AddExam(this, tableModel);
        addExam.setVisible(true);
        this.setEnabled(true);      // 设置本窗口不可编辑
    }

    /**
     * @Description: 在   “考试管理”  界面点击   “删除选中的考试”  按钮对应的点击事件   -----》 删除试卷
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/30
     */
    private void buttonDeleteExamMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 1.获得选中的 考试
        int[] selectedRows = tableSubject.getSelectedRows();

        // 因为没有选中表格任何行数据时，getSelectedRows() 返回的是 new Int[0]     0个元素的数组
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！可以安装Ctrl + 鼠标左键选择多个需要删除的题目！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        } else if (selectedRows.length >= 2) {
            JOptionPane.showMessageDialog(this, "一次只能删除一张试卷！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 弹出提示信息，防止用户勿删
        String str = "确定要删除" + selectedRows.length + "项数据吗？";
        int result = JOptionPane.showConfirmDialog(this, str, "删除提示", 2);
        if (result == 2) {
            return;     // 用户选择了取消
        }

        // 3. 一切准备就绪之后，从数据库和表格中删除数据
        String examName = tableModel.getValueAt(tableSubject.getSelectedRow(), 0).toString(); // 获得选中的哪一行的考试标题
        ExamJDBC.deleteExam(examName);
        tableModel.removeRow(tableSubject.getSelectedRow());        // 从表格中移除该行数据
        JOptionPane.showMessageDialog(this, "删除试卷成功！！", "成功", JOptionPane.PLAIN_MESSAGE);


    }

    /**
     * @Description: 在   “考试管理”  界面点击   “开始考试” 和 “开始考试(校园卡认证)”  按钮对应的点击事件   ----》 开始考试
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/11/30
     */
    /**
     *      注：因为拍照算法是利用多线程实现的，所以不能在本方法内部执行开始考试的代码，的根据识别结果，在 takePhoto 方法中决定是否开始考试
     *      即在 takePhoto() 方法中选择是否调用 matchResult() 方法
     **/
    private void buttonStartExamMouseReleased(MouseEvent e) {
        // TODO add your code here
        String examName;        // 考试名称，需要从表格中获取
        String examPaperName;       // 考试对应的试卷名称，同样从表格中获取
        String[] paperTitles;       // 考试对应的试卷里面的  题目标题对应的 字符串数组

        // 1.首先获得鼠标选中的行（要求：只能够选中一行，没有选中行或者选中多行显示警告提示用户）
        int[] selectedRows = tableSubject.getSelectedRows();

        // 用户选中多行或者没有选中，警告提示用户
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中考试！！请先选中需要开始时的考试", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        } else if (selectedRows.length >= 2) {
            JOptionPane.showMessageDialog(this, "开始一场考试！！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        /*
         *       2. 开始考试，需要获取一下信息
         *         0.检查考试时间，查看当前时间是否符合考试开始时间
         *       1. 考试名称
         *       2.考试对应的试卷，试卷中的题目信息（构造一个 String 数组，用来保存试卷中有哪些题目，然后作为参数传递过去）
         *
         * */

        // 2.3 检查考试时间
        long currentTime = System.currentTimeMillis();        // 获取当前时间毫秒
        long startTime = 0;
        long endTime = 0;
        SimpleDateFormat tiemFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//        Date startTime = begin.parse(tableModel.getValueAt(tableSubject.getSelectedRow().toString(), 3));
        try {
            Date startDate = tiemFormat.parse(tableModel.getValueAt(tableSubject.getSelectedRow(), 3).toString());
            Date endDate = tiemFormat.parse(tableModel.getValueAt(tableSubject.getSelectedRow(), 4).toString());
            startTime = startDate.getTime();
            endTime = endDate.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        // 如果不在考试范围内，直接返回，不继续执行后面的代码
        if (currentTime < startTime) {
            JOptionPane.showMessageDialog(this, "考试还未开始！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (currentTime > endTime) {
            JOptionPane.showMessageDialog(this, "考试已经结束！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2.3.0    因为这是2个按钮共用一个点击事件，所以要在这里进行区分判断一下，不同的按钮进行不同的操作

        /**
         *      原本这部分代码有复杂的 if~else 嵌套，优化之后可读性提高了很多
         * */
        JButton sourceButton = (JButton) e.getSource();

        if (sourceButton.getText().contentEquals("开始考试")) {
            // 2.3.1   根据用户的选择，判断是否要进行人脸识别检测
            int faceFlag = JOptionPane.showConfirmDialog(this, "是否要进行人脸识别验证？", "人脸识别验证", 0);
            if (faceFlag == 0) {
                try {
                    // 调用 摄像头 拍照
                    System.out.println("当前用户为：" + userName);
                    //  第 3 个参数为 1 表示：是进行人脸识别的时候调用的 takePhoto
                    WebcamCapture.takePhote(this, userName, 1);
                    // 不过有没有识别成功，本函数的功能已经完成了，是否需要开始开始考试的看 takePhoto 里面具体的函数调用
                    return;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            // 2.32    根据用户的选择，判断是否要进行 校园卡识别检测
            int barCodeFlag = JOptionPane.showConfirmDialog(this, "是否要进行校园卡认证？", "校园卡认证", 0);
            if (barCodeFlag == 0) {
                try {
                    // 调用 摄像头 拍照
                    System.out.println("当前用户为：" + userName);
                    // 第 3 个参数为 2 表示：是进行二维码/条形码验证的时候调用的 takePhoto
                    WebcamCapture.takePhote(this, userName, 2);
                    // 注：二维码识别放在 takePhoto() 方法内部执行，本方法功能已经完成
                    return;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


//        /**
//         *      只要用户点击了取消识别，或者人脸识别失败，二维码验证失败，该函数都会直接返回，执行不到这一步。
//         *      所以前面的代码可以去掉很多的 if~else 嵌套
//         * */
//        // 2.4   下面的代码就是开始考试。如果在考试时间范围内，获得 考试名称，考试对应试卷名称，试卷里题目的标题数组
//        examName = tableModel.getValueAt(tableSubject.getSelectedRow(), 0).toString();
//        examPaperName = tableModel.getValueAt(tableSubject.getSelectedRow(), 1).toString();
//        paperTitles = PaperJDBC.getPaperSubjectTitles(examPaperName);
//
//        StartExam startExam = new StartExam(this, examName, paperTitles, startTime, endTime);
//        this.setEnabled(false);         // 设置当前窗体不可编辑
//        startExam.setVisible(true);
    }

// ###################################    下面是 “成绩查询”  界面 ###############################################

    /**
     * @Description: 初始化 “成绩查询” 界面
     * @Param: []
     * @return: void
     * @Author: 林凯
     * @Date: 2019/12/14
     */
    private void initExamResult() {
        // 1. 设置每一列的 标题
        String[] columnNames = {"用户名称", "考试名称", "用户答案", "标准答案", "用户得分", "总分"};
        String[][] datas = ExamResultJDBC.readAllExamResult();      // 2. 获得需要展示的数据
        // 3. 调用封装好的方法，将数据展示在表格中
        /**
         *      保证界面中的表格，表格模型，滚动面板只有一个对象
         * */
        if (table05 == null && scrollPane05 == null && tableModel05 == null) {
            System.out.println("new 了一个对象");
            tableModel05 = new DefaultTableModel(datas, columnNames);
            table05 = new JTable(tableModel05);     // 通过 表格模型 来创建 表格 对象
            tableModel05.setRowCount(datas.length);       // 设置表格行数
            table05.setRowHeight(50);     // 设置行高50像素
            scrollPane05 = new JScrollPane(table05);        //注意，向JScrollPane中添加控件只能在初始化的时候添加
            panelContentExamResult.add(scrollPane05);         // 将   表格  添加到对应的面板中，这里使用的是  BorderLayout   ，添加到中间即可
            for (int row = 0; row < datas.length; row++) {
                for (int column = 0; column< datas[0].length; column++) {
                    table05.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
                }
            }
            tableModel05.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
        }

        // 重新赋值引用
        tableSubject = table05;
        tableModel = tableModel05;
        scrollPaneSubject = scrollPane05;
    }

    private void buttonSearchExamResultMouseReleased(MouseEvent e) {
        // TODO add your code here
        JButton tempButton = (JButton) e.getSource();
        String[][] newDatas = null;
        if (tempButton.getText().equals("成绩查询（按姓名）")) {
            String userName = JOptionPane.showInputDialog(this, "请输入学生姓名：", "输入学生姓名");
            if (userName == null) {
                return;     // 如果用户点击了 “取消” 按钮，则直接返回，不执行后面的代码
            }
            System.out.println(userName);
            newDatas = ExamResultJDBC.selectExamResultByUserName(userName);
        } else if (tempButton.getText().equals("成绩查询（按考试名称）")){
            String examName = JOptionPane.showInputDialog(this, "请输入数据名称：", "输入试卷名称");
            if (examName == null) {
                return;     // 如果用户点击了 “取消” 按钮，则直接返回，不执行后面的代码
            }
            newDatas = ExamResultJDBC.selectExamResultByExamName(examName);
        } else {
            // 如果点击
            newDatas = ExamResultJDBC.readAllExamResult();
        }

        // 将数组中的内容设置到表格当中
        tableModel.setRowCount(newDatas.length);       // 重新设置表格的行数

        for (int row = 0; row < newDatas.length; row++) {
            for (int column = 0; column< newDatas[0].length; column++) {
                tableModel.setValueAt(newDatas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
            }
        }
        tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
    }

// #####################################    下面是  “JFormDesigner 自动生成的代码”  #############################

    /** 
    * @Description: 初始化主界面  JFormdesigner 自动生成的 
    * @Param: [] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/10/27 
    */ 
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panelTop = new JPanel();
        labelTitle = new JLabel();
        panel6 = new JPanel();
        buttonWelcome = new JButton();
        buttonExit = new JButton();
        panelOther02 = new JPanel();
        panelOther01 = new JPanel();
        panelTime = new JPanel();
        labelTime = new JLabel();
        panelLeft = new JPanel();
        buttonLeft01 = new JButton();
        buttonLeft02 = new JButton();
        buttonLeft03 = new JButton();
        buttonLeft04 = new JButton();
        buttonLeft05 = new JButton();
        buttonLeft06 = new JButton();
        buttonLeft7 = new JButton();
        panelCenter = new JPanel();
        panelContent1 = new JPanel();
        label20 = new JLabel();
        panelContentMyInformation = new JPanel();
        panel1 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        textField4 = new JTextField();
        textField5 = new JTextField();
        button1 = new JButton();
        panelContentStudent = new JPanel();
        panel19 = new JPanel();
        buttonSearchStudent = new JButton();
        label30 = new JLabel();
        panelContentSubject = new JPanel();
        panel16 = new JPanel();
        buttonAddSubject = new JButton();
        buttonDeleteSubject = new JButton();
        buttonSearchSubject = new JButton();
        buttonEditSubject = new JButton();
        buttonCheckSubject = new JButton();
        label27 = new JLabel();
        panleContentPaper = new JPanel();
        panel17 = new JPanel();
        buttonAddPaper = new JButton();
        buttonDeletePaper = new JButton();
        buttonSearchPaper = new JButton();
        buttonRenamePaper = new JButton();
        buttonEditPaper = new JButton();
        buttonPreviewPaper = new JButton();
        label28 = new JLabel();
        panelContentExam = new JPanel();
        panel18 = new JPanel();
        buttonAddExam = new JButton();
        buttonDeleteExam = new JButton();
        buttonStartExam = new JButton();
        label29 = new JLabel();
        buttonStartExamWithCard = new JButton();
        panelContentExamResult = new JPanel();
        panel20 = new JPanel();
        buttonSearchExamResult = new JButton();
        label31 = new JLabel();
        buttonSearchExamResult2 = new JButton();
        buttonSearchExamResult3 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.white);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panelTop ========
        {
            panelTop.setBackground(new Color(66, 143, 185));
            panelTop.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing
            . border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e", javax. swing. border. TitledBorder
            . CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069al\u006fg" ,java .
            awt .Font .BOLD ,12 ), java. awt. Color. red) ,panelTop. getBorder( )) )
            ; panelTop. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
            ) {if ("\u0062or\u0064er" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} )
            ;
            panelTop.setLayout(new BorderLayout());

            //---- labelTitle ----
            labelTitle.setText(" ");
            labelTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 52));
            labelTitle.setForeground(Color.white);
            panelTop.add(labelTitle, BorderLayout.WEST);

            //======== panel6 ========
            {
                panel6.setBackground(new Color(66, 143, 185));
                panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));

                //---- buttonWelcome ----
                buttonWelcome.setText("\u6b22\u8fce\u60a8\uff01  ");
                panel6.add(buttonWelcome);

                //---- buttonExit ----
                buttonExit.setText("\u9000\u51fa\u767b\u5f55");
                panel6.add(buttonExit);
            }
            panelTop.add(panel6, BorderLayout.EAST);

            //======== panelOther02 ========
            {
                panelOther02.setBackground(new Color(66, 143, 185));
                panelOther02.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panelOther02.getComponentCount(); i++) {
                        Rectangle bounds = panelOther02.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panelOther02.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panelOther02.setMinimumSize(preferredSize);
                    panelOther02.setPreferredSize(preferredSize);
                }
            }
            panelTop.add(panelOther02, BorderLayout.SOUTH);

            //======== panelOther01 ========
            {
                panelOther01.setBackground(new Color(66, 143, 185));
                panelOther01.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panelOther01.getComponentCount(); i++) {
                        Rectangle bounds = panelOther01.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panelOther01.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panelOther01.setMinimumSize(preferredSize);
                    panelOther01.setPreferredSize(preferredSize);
                }
            }
            panelTop.add(panelOther01, BorderLayout.NORTH);

            //======== panelTime ========
            {
                panelTime.setBackground(new Color(66, 143, 185));
                panelTime.setLayout(null);

                //---- labelTime ----
                labelTime.setText("\u65f6\u95f4");
                labelTime.setForeground(Color.white);
                panelTime.add(labelTime);
                labelTime.setBounds(105, 30, 100, 30);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panelTime.getComponentCount(); i++) {
                        Rectangle bounds = panelTime.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panelTime.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panelTime.setMinimumSize(preferredSize);
                    panelTime.setPreferredSize(preferredSize);
                }
            }
            panelTop.add(panelTime, BorderLayout.CENTER);
        }
        contentPane.add(panelTop, BorderLayout.NORTH);

        //======== panelLeft ========
        {
            panelLeft.setBorder(new EtchedBorder());
            panelLeft.setLayout(null);

            //---- buttonLeft01 ----
            buttonLeft01.setText("\u7cfb\u7edf\u9996\u9875");
            buttonLeft01.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonLeft01.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonLeftMouseReleased(e);
                }
            });
            panelLeft.add(buttonLeft01);
            buttonLeft01.setBounds(0, 0, 135, 50);

            //---- buttonLeft02 ----
            buttonLeft02.setText("\u4e2a\u4eba\u4fe1\u606f");
            buttonLeft02.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonLeft02.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonLeftMouseReleased(e);
                }
            });
            panelLeft.add(buttonLeft02);
            buttonLeft02.setBounds(0, 55, 135, 50);

            //---- buttonLeft03 ----
            buttonLeft03.setText("\u7528\u6237\u7ba1\u7406");
            buttonLeft03.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonLeft03.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonLeftMouseReleased(e);
                }
            });
            panelLeft.add(buttonLeft03);
            buttonLeft03.setBounds(0, 110, 135, 50);

            //---- buttonLeft04 ----
            buttonLeft04.setText("\u8bd5\u9898\u7ba1\u7406");
            buttonLeft04.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonLeft04.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonLeftMouseReleased(e);
                }
            });
            panelLeft.add(buttonLeft04);
            buttonLeft04.setBounds(0, 165, 135, 50);

            //---- buttonLeft05 ----
            buttonLeft05.setText("\u8bd5\u5377\u7ba1\u7406");
            buttonLeft05.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonLeft05.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonLeftMouseReleased(e);
                }
            });
            panelLeft.add(buttonLeft05);
            buttonLeft05.setBounds(0, 220, 135, 50);

            //---- buttonLeft06 ----
            buttonLeft06.setText("\u8003\u8bd5\u7ba1\u7406");
            buttonLeft06.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonLeft06.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonLeftMouseReleased(e);
                }
            });
            panelLeft.add(buttonLeft06);
            buttonLeft06.setBounds(0, 275, 135, 50);

            //---- buttonLeft7 ----
            buttonLeft7.setText("\u6210\u7ee9\u67e5\u8be2");
            buttonLeft7.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonLeft7.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonLeftMouseReleased(e);
                }
            });
            panelLeft.add(buttonLeft7);
            buttonLeft7.setBounds(0, 330, 135, 50);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panelLeft.getComponentCount(); i++) {
                    Rectangle bounds = panelLeft.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panelLeft.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panelLeft.setMinimumSize(preferredSize);
                panelLeft.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panelLeft, BorderLayout.WEST);

        //======== panelCenter ========
        {
            panelCenter.setLayout(new CardLayout());

            //======== panelContent1 ========
            {
                panelContent1.setLayout(new BorderLayout());

                //---- label20 ----
                label20.setIcon(new ImageIcon(getClass().getResource("/images/view3.png")));
                panelContent1.add(label20, BorderLayout.CENTER);
            }
            panelCenter.add(panelContent1, "card1");

            //======== panelContentMyInformation ========
            {
                panelContentMyInformation.setLayout(new BorderLayout());

                //======== panel1 ========
                {
                    panel1.setLayout(null);

                    //---- label1 ----
                    label1.setText("\u59d3\u540d\uff1a");
                    label1.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    panel1.add(label1);
                    label1.setBounds(70, 90, 95, 40);

                    //---- label2 ----
                    label2.setText("\u5b66\u53f7\uff1a");
                    label2.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    panel1.add(label2);
                    label2.setBounds(70, 150, 100, 40);

                    //---- label3 ----
                    label3.setText("\u73ed\u7ea7\uff1a");
                    label3.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    panel1.add(label3);
                    label3.setBounds(70, 210, 100, 40);

                    //---- label4 ----
                    label4.setText("\u624b\u673a\u53f7\uff1a");
                    label4.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    panel1.add(label4);
                    label4.setBounds(70, 280, 95, 40);

                    //---- label5 ----
                    label5.setText("\u90ae\u7bb1\uff1a");
                    label5.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    panel1.add(label5);
                    label5.setBounds(70, 345, 95, 40);

                    //---- textField1 ----
                    textField1.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
                    textField1.setEditable(false);
                    panel1.add(textField1);
                    textField1.setBounds(185, 90, 195, 40);

                    //---- textField2 ----
                    textField2.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
                    textField2.setEditable(false);
                    panel1.add(textField2);
                    textField2.setBounds(185, 155, 280, 40);

                    //---- textField3 ----
                    textField3.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
                    textField3.setEditable(false);
                    panel1.add(textField3);
                    textField3.setBounds(185, 215, 195, 40);

                    //---- textField4 ----
                    textField4.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
                    textField4.setEditable(false);
                    panel1.add(textField4);
                    textField4.setBounds(185, 285, 280, 40);

                    //---- textField5 ----
                    textField5.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
                    textField5.setEditable(false);
                    panel1.add(textField5);
                    textField5.setBounds(185, 345, 280, 40);

                    //---- button1 ----
                    button1.setText("\u7f16\u8f91\u4e2a\u4eba\u4fe1\u606f");
                    button1.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    panel1.add(button1);
                    button1.setBounds(650, 10, 180, 50);

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
                panelContentMyInformation.add(panel1, BorderLayout.CENTER);
            }
            panelCenter.add(panelContentMyInformation, "card2");

            //======== panelContentStudent ========
            {
                panelContentStudent.setLayout(new BorderLayout());

                //======== panel19 ========
                {
                    panel19.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

                    //---- buttonSearchStudent ----
                    buttonSearchStudent.setText("\u67e5\u627e\u5b66\u751f");
                    buttonSearchStudent.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    buttonSearchStudent.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonCheckSubjectMouseReleased(e);
                        }
                    });
                    panel19.add(buttonSearchStudent);

                    //---- label30 ----
                    label30.setText(" ");
                    label30.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
                    panel19.add(label30);
                }
                panelContentStudent.add(panel19, BorderLayout.NORTH);
            }
            panelCenter.add(panelContentStudent, "card3");

            //======== panelContentSubject ========
            {
                panelContentSubject.setLayout(new BorderLayout());

                //======== panel16 ========
                {
                    panel16.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

                    //---- buttonAddSubject ----
                    buttonAddSubject.setText("\u6dfb\u52a0\u8bd5\u9898");
                    buttonAddSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonAddSubject.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonAddSubjectMouseReleased(e);
                        }
                    });
                    panel16.add(buttonAddSubject);

                    //---- buttonDeleteSubject ----
                    buttonDeleteSubject.setText("\u5220\u9664\u8bd5\u9898");
                    buttonDeleteSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonDeleteSubject.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonDeleteSubjectMouseReleased(e);
                        }
                    });
                    panel16.add(buttonDeleteSubject);

                    //---- buttonSearchSubject ----
                    buttonSearchSubject.setText("\u67e5\u627e\u8bd5\u9898");
                    buttonSearchSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonSearchSubject.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonSearchSubjectMouseReleased(e);
                        }
                    });
                    panel16.add(buttonSearchSubject);

                    //---- buttonEditSubject ----
                    buttonEditSubject.setText("\u7f16\u8f91\u8bd5\u9898");
                    buttonEditSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonEditSubject.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonEditSubjectMouseReleased(e);
                        }
                    });
                    panel16.add(buttonEditSubject);

                    //---- buttonCheckSubject ----
                    buttonCheckSubject.setText("\u67e5\u770b\u8bd5\u9898");
                    buttonCheckSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonCheckSubject.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonCheckSubjectMouseReleased(e);
                        }
                    });
                    panel16.add(buttonCheckSubject);

                    //---- label27 ----
                    label27.setText(" ");
                    label27.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
                    panel16.add(label27);
                }
                panelContentSubject.add(panel16, BorderLayout.NORTH);
            }
            panelCenter.add(panelContentSubject, "card4");

            //======== panleContentPaper ========
            {
                panleContentPaper.setLayout(new BorderLayout());

                //======== panel17 ========
                {
                    panel17.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

                    //---- buttonAddPaper ----
                    buttonAddPaper.setText("\u65b0\u5efa\u8bd5\u5377");
                    buttonAddPaper.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonAddPaper.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonAddPaperMouseReleased(e);
                        }
                    });
                    panel17.add(buttonAddPaper);

                    //---- buttonDeletePaper ----
                    buttonDeletePaper.setText("\u5220\u9664\u8bd5\u5377");
                    buttonDeletePaper.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonDeletePaper.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonDeletePaperMouseReleased(e);
                        }
                    });
                    panel17.add(buttonDeletePaper);

                    //---- buttonSearchPaper ----
                    buttonSearchPaper.setText("\u7b5b\u9009\u8bd5\u5377");
                    buttonSearchPaper.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonSearchPaper.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonSearchPaperMouseReleased(e);
                        }
                    });
                    panel17.add(buttonSearchPaper);

                    //---- buttonRenamePaper ----
                    buttonRenamePaper.setText("\u91cd\u547d\u540d\u8bd5\u5377");
                    buttonRenamePaper.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonRenamePaper.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonRenamePaperMouseReleased(e);
                        }
                    });
                    panel17.add(buttonRenamePaper);

                    //---- buttonEditPaper ----
                    buttonEditPaper.setText("\u7f16\u8f91\u8bd5\u5377");
                    buttonEditPaper.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonEditPaper.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonEditPaperMouseReleased(e);
                        }
                    });
                    panel17.add(buttonEditPaper);

                    //---- buttonPreviewPaper ----
                    buttonPreviewPaper.setText("\u9884\u89c8\u8bd5\u5377");
                    buttonPreviewPaper.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonPreviewPaper.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonPreviewPaperMouseReleased(e);
                        }
                    });
                    panel17.add(buttonPreviewPaper);

                    //---- label28 ----
                    label28.setText(" ");
                    label28.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
                    panel17.add(label28);
                }
                panleContentPaper.add(panel17, BorderLayout.NORTH);
            }
            panelCenter.add(panleContentPaper, "card5");

            //======== panelContentExam ========
            {
                panelContentExam.setLayout(new BorderLayout());

                //======== panel18 ========
                {
                    panel18.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

                    //---- buttonAddExam ----
                    buttonAddExam.setText("\u53d1\u5e03\u8003\u8bd5");
                    buttonAddExam.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonAddExam.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonAddExamMouseReleased(e);
                        }
                    });
                    panel18.add(buttonAddExam);

                    //---- buttonDeleteExam ----
                    buttonDeleteExam.setText("\u5220\u9664\u9009\u4e2d\u7684\u8003\u8bd5");
                    buttonDeleteExam.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                    buttonDeleteExam.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonDeleteExamMouseReleased(e);
                        }
                    });
                    panel18.add(buttonDeleteExam);

                    //---- buttonStartExam ----
                    buttonStartExam.setText("\u5f00\u59cb\u8003\u8bd5");
                    buttonStartExam.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    buttonStartExam.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonStartExamMouseReleased(e);
                        }
                    });
                    panel18.add(buttonStartExam);

                    //---- label29 ----
                    label29.setText(" ");
                    label29.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
                    panel18.add(label29);

                    //---- buttonStartExamWithCard ----
                    buttonStartExamWithCard.setText("\u5f00\u59cb\u8003\u8bd5(\u6821\u56ed\u5361\u8ba4\u8bc1)");
                    buttonStartExamWithCard.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    buttonStartExamWithCard.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonStartExamMouseReleased(e);
                        }
                    });
                    panel18.add(buttonStartExamWithCard);
                }
                panelContentExam.add(panel18, BorderLayout.NORTH);
            }
            panelCenter.add(panelContentExam, "card6");

            //======== panelContentExamResult ========
            {
                panelContentExamResult.setLayout(new BorderLayout());

                //======== panel20 ========
                {
                    panel20.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

                    //---- buttonSearchExamResult ----
                    buttonSearchExamResult.setText("\u6210\u7ee9\u67e5\u8be2\uff08\u6309\u59d3\u540d\uff09");
                    buttonSearchExamResult.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    buttonSearchExamResult.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonSearchExamResultMouseReleased(e);
                        }
                    });
                    panel20.add(buttonSearchExamResult);

                    //---- label31 ----
                    label31.setText(" ");
                    label31.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
                    panel20.add(label31);

                    //---- buttonSearchExamResult2 ----
                    buttonSearchExamResult2.setText("\u6210\u7ee9\u67e5\u8be2\uff08\u6309\u8003\u8bd5\u540d\u79f0\uff09");
                    buttonSearchExamResult2.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    buttonSearchExamResult2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonSearchExamResultMouseReleased(e);
                        }
                    });
                    panel20.add(buttonSearchExamResult2);

                    //---- buttonSearchExamResult3 ----
                    buttonSearchExamResult3.setText("\u663e\u793a\u5168\u90e8\u6210\u7ee9");
                    buttonSearchExamResult3.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                    buttonSearchExamResult3.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            buttonSearchExamResultMouseReleased(e);
                        }
                    });
                    panel20.add(buttonSearchExamResult3);
                }
                panelContentExamResult.add(panel20, BorderLayout.NORTH);
            }
            panelCenter.add(panelContentExamResult, "card7");
        }
        contentPane.add(panelCenter, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        /**
         *   ##############################3下面是自己添加的代码##################################################
         * */
        buttonWelcome.setText("欢迎您，" + userName);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panelTop;
    private JLabel labelTitle;
    private JPanel panel6;
    private JButton buttonWelcome;
    private JButton buttonExit;
    private JPanel panelOther02;
    private JPanel panelOther01;
    private JPanel panelTime;
    private JLabel labelTime;
    private JPanel panelLeft;
    private JButton buttonLeft01;
    private JButton buttonLeft02;
    private JButton buttonLeft03;
    private JButton buttonLeft04;
    private JButton buttonLeft05;
    private JButton buttonLeft06;
    private JButton buttonLeft7;
    private JPanel panelCenter;
    private JPanel panelContent1;
    private JLabel label20;
    private JPanel panelContentMyInformation;
    private JPanel panel1;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton button1;
    private JPanel panelContentStudent;
    private JPanel panel19;
    private JButton buttonSearchStudent;
    private JLabel label30;
    private JPanel panelContentSubject;
    private JPanel panel16;
    private JButton buttonAddSubject;
    private JButton buttonDeleteSubject;
    private JButton buttonSearchSubject;
    private JButton buttonEditSubject;
    private JButton buttonCheckSubject;
    private JLabel label27;
    private JPanel panleContentPaper;
    private JPanel panel17;
    private JButton buttonAddPaper;
    private JButton buttonDeletePaper;
    private JButton buttonSearchPaper;
    private JButton buttonRenamePaper;
    private JButton buttonEditPaper;
    private JButton buttonPreviewPaper;
    private JLabel label28;
    private JPanel panelContentExam;
    private JPanel panel18;
    private JButton buttonAddExam;
    private JButton buttonDeleteExam;
    private JButton buttonStartExam;
    private JLabel label29;
    private JButton buttonStartExamWithCard;
    private JPanel panelContentExamResult;
    private JPanel panel20;
    private JButton buttonSearchExamResult;
    private JLabel label31;
    private JButton buttonSearchExamResult2;
    private JButton buttonSearchExamResult3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        MainUI test1 = new MainUI("18170098712");
        test1.setVisible(true);
    }
}
