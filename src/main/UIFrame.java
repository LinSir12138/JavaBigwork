/*
 * Created by JFormDesigner on Sat Oct 19 20:16:10 CST 2019
 */
package main;

/*
*       2019年11月10日09:18:48
*       对应UI： 登录/注册界面
*       注意事项：
*       1.避免空指针异常：最好写成  textFieldDTYZML.getText().equals(dynamicCode)
*                       或者给 dynamicCode 设置初始值
*       2. 为了方便测试，输入动态验证码是，输入666666就能够正确登录
*           而且在初始化是初始化了 oldTime，正式上线时需要修改一下
* d
* */

import javax.swing.event.*;

import jdbc.UserJDBC;
import mytest.SendMessage;
import slatMD5.SaltMd5Util;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author Lin Kai
 */
public class UIFrame extends JFrame {
    private String userName;        // 用户名，登录时根据用户的手机号或者邮箱号来确定
    private String imageCode;       // 保存图片验证码对应的字符串
    private String dynamicCode;      // 动态验证码
    private String password1;
    private String password2;       // 用户输入的密码（必须确保两次输入密码一致）

    // 对应文本框焦点事件的监听对象
    // 对应 注册界面
    private TextFieldFocusListener tfFocusUserNameR;        // 用户名
    private TextFieldFocusListener tfFocusTPYZMR;       // 图片验证码
    private TextFieldFocusListener tfFocusEmailR;       // 邮箱或手机号
    private TextFieldFocusListener tfFocusDTYZMR;       // 动态验证码
    // 对应 登录界面
    private TextFieldFocusListener tfFocusEmailL;       // 邮箱或手机号
    private TextFieldFocusListener tfFocusTPYZML;       // 图片验证码
    private TextFieldFocusListener tfFocusDTYZML;       // 动态验证码


    // 各种文本框中输入正确或者错误的标志，在构造方法中初始化为 false
//    private boolean emailOrPhoneFlag;       // 邮箱或者手机号
    private boolean emailOrPhoneFlagL;       // 邮箱或者手机号(登录界面)
    private boolean emailOrPhoneFlagR;       // 邮箱或者手机号(注册界面)
    private boolean checkBoxFlagL;       // 多选框（登录界面）
    private boolean checkBoxFlagR;       // 多选框（注册界面）
    // 时间的控制
    private long oldTimeL;       // 点击 获取验证码 时，记录的当前时间-------》 登录界面
    private long newTimeL;       // 点击 登录 时，记录的当前时间 （验证码10分钟有效）
    private long oldTimeR;       // 点击 登录 时，记录的当前时间 （验证码10分钟有效）----> 注册界面
    private long newTimeR;       // 点击 登录 时，记录的当前时间 （验证码10分钟有效）
    private int minute;
    private String imageName;       // 从 Bing.com 下载的图片的名称


    /**
    * @Description: 构造函数
    * @Param: []
    * @return:
    * @Author: 林凯
    * @Date: 2019/10/21
    */
    public UIFrame() {
        // 标志初始值为 false
        emailOrPhoneFlagL = false;
        emailOrPhoneFlagR = false;
        checkBoxFlagL = false;
        checkBoxFlagR = false;
        minute = 10;        // 设置 验证码 的有效时间为10分钟
        /**
         *      方便测试，这里初始化了 oldTime，正式上线时需要删除
         * */
        oldTimeL = System.currentTimeMillis();
        initComponents();
        setBackgroundImage();       // 从网络上获取 Bing.com 的背景图片
    }

    /**
     * @Description: 给主界面设置背景图片
     * @Param: []
     * @return: void
     * @Author: 林凯
     * @Date: 2019/12/27
     */
    private void setBackgroundImage() {
        // 创建一个局部内部类，实现Runnable接口，利用多线程来下载网络图片
        class MyDownLoad implements Runnable {
            String myImageName;

            public MyDownLoad(String myImageName) {
                this.myImageName = myImageName;
            }

            @Override
            public void run() {
                DownLoadImageFromBing.downloadImageFromBing(myImageName);
            }
        }

        imageName = "Download" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        Thread thread = new Thread(new MyDownLoad(imageName));
        thread.start();
    }

    /**
     * @Description: 点击 登录 标签，选项卡切换的登录界面
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/10/21
     */
    private void labelLoginMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 初始化标记值
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();     // 通过强制类型转换，获得 卡片布局面板的布局
        cardLayout.show(cardPanel, "card1");
        // 更换标签下面的颜色提示
        panelLoginUnder.setVisible(true);
        panelRegisterUnder.setVisible(false);
    }

    /**
     * @Description: 点击 注册 标签，选项卡切换到注册界面
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/10/21
     */
    private void labelRegisterMouseReleased(MouseEvent e) {
        // TODO add your code here
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();     // 通过强制类型转换，获得 卡片布局面板的布局
        cardLayout.show(cardPanel, "card2");
        // 更换标签下面的颜色提示
        panelLoginUnder.setVisible(false);
        panelRegisterUnder.setVisible(true);
    }

    /**
    * @Description:  验证码后面的 “看不清楚对应的鼠标点击事件”----鼠标按下
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/21
    */
    private void labelUnClearMousePressed(MouseEvent e) {
        // TODO add your code here
        Object obj = e.getSource();
        JLabel tempLabel = (JLabel) obj;        // 强制类型转换
        tempLabel.setForeground(Color.BLUE);
        System.out.println("点击了label");
    }

    /**
    * @Description:  验证码后面的 “看不清楚对应的鼠标点击事件”----鼠标抬起
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/21
    */
    private void labelUnClearMouseReleased(MouseEvent e) {
        // TODO add your code here
        Object obj = e.getSource();
        JLabel tempLabel = (JLabel) obj;        // 强制类型转换
        tempLabel.setForeground(Color.RED);
        // 更换验证码
        imageCode = VerifyCodeUtils.createOneCodeImage();       // 生成图片验证码，并保存图片对应的字符串，用于验证
        System.out.println(imageCode);
        File file = new File("src/YZMimages/" + imageCode + ".jpg");        // 获得图片的file对象
        Icon icon = new ImageIcon(file.toString());
        labelImageL.setIcon(icon);
        labelImageR.setIcon(icon);
    }

    /**
     * @Description:  给 复选框 添加的 状态改变 监听事件
     *                  这里做了类型判断，根据不同的复选框，执行不同操作
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/10/21
     */
    private void checkBox1StateChanged(ChangeEvent e) {
        // TODO add your code here
        JCheckBox checkBox = (JCheckBox) e.getSource();     // 通过类型转换获得对应组件
        if (checkBox == checkBoxL) {
            if (checkBox.isSelected()) {
                checkBoxFlagL = true;
            } else {
                checkBoxFlagL = false;
            }
        } else {
            if (checkBox.isSelected()) {
                checkBoxFlagR = true;
            } else {
                checkBoxFlagR = false;
            }
        }

    }

//#########################################     登录界面    ###################################3

    /**
    * @Description:  登录界面，点击 "获取验证码" 按钮 ----(动态验证码)---> 鼠标释放时的事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/24
    */
    private void buttonDTYZMLMouseReleased(MouseEvent e) {
        // TODO add your code here
        if (buttonDTYZML.isEnabled() == false) {
            return;     // 如果组件为禁用状态，直接返回
        }
        // TODO add your code here
        boolean emailFlag = CheckEmailOrPhone.checkEmail(textFieldEmailL.getText());
        boolean phoneFlag = CheckEmailOrPhone.checkPhoneNumber(textFieldEmailL.getText());
        if (emailFlag || phoneFlag) {

            /**
             *      首先检查手机号或者邮箱号是否已经注册过
             * */
            UserJDBC userJDBC = new UserJDBC();

            if (phoneFlag) {
                if (userJDBC.checkUserByPhoneNumber(textFieldEmailL.getText(), "0") == 2) {
                    // 说明 手机号 在数据库中找不到
                    JOptionPane.showMessageDialog(this, "手机号还未注册，请先注册后在登录！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (emailFlag) {
                if (userJDBC.checkUserByEmail(textFieldEmailL.getText(), "0") == 2) {
                    // 说明 邮箱账号 在数据库中找不到
                    JOptionPane.showMessageDialog(this, "邮箱号还未注册，请先注册后在登录！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 发送验证码的同时开始计时
            oldTimeL = System.currentTimeMillis();
            // 同时，按钮上进行倒计时60秒，在60秒之内，按钮不能点击，60秒时候，显示“重新发送（用到多线程）”
            new Thread(new CountDown(buttonDTYZML)).start();     //启动另一个线程
            buttonDTYZML.setEnabled(false);

            if (emailFlag) {
                JOptionPane.showMessageDialog(this, "验证码已经发送到您的邮箱！", "成功", JOptionPane.PLAIN_MESSAGE);
                SendByThread emailThread = new SendByThread(2, textFieldEmailR.getText());
                dynamicCode = String.valueOf(emailThread.getDynamicCode());       // 获得对应的验证码，转换成字符串保存
            } else {
                JOptionPane.showMessageDialog(this, "验证码已经发送到您的手机！", "成功", JOptionPane.PLAIN_MESSAGE);
//                SendByThread messageThread = new SendByThread(2, textFieldEmailR.getText());
//                new Thread(messageThread).start();
                SendMessage sendMessage = new SendMessage(textFieldEmailL.getText());
                sendMessage.send();
                dynamicCode = String.valueOf(sendMessage.getRandomNumber());   // 获得对应的验证码，转换成字符串保存
            }
            emailOrPhoneFlagL = true;        // 最后在 checkInput() 方法中会用到
        } else {
            // 提示 “请先输入邮箱或者手机号”
            emailOrPhoneFlagL = false;
            JOptionPane.showMessageDialog(this, "请先输入正确的邮箱或手机号！", "错误", 0);
        }
    }

    /**
     * @Description:  登录界面或者注册界面的 “《服务条款》”鼠标点击事件
     *                 跳转到相应的界面
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/10/21
     */
    private void labelURLMouseReleased(MouseEvent e) {
        System.out.println();
        // TODO add your code here
        // 首先判断当前平台是否支持桌面
        if (Desktop.isDesktopSupported()) {
            // 获取当前平台桌面实例
            Desktop desktop = Desktop.getDesktop();
            // 使用默认浏览器打开链接
            try {
                desktop.browse(URI.create("https://policies.google.com/terms?hl=zh-CN"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("当前平台不支持DeskTop！");
        }
    }

    /**
     * @Description: 在登录界面点击 登录按钮
     * @Param: [e]
     * @return: void
     * @Author: 林凯
     * @Date: 2019/10/24
     */
    private void buttonLoginMouseReleased(MouseEvent e) {
        // TODO add your code here
        newTimeL = System.currentTimeMillis();
        System.out.println("已用时间：" + (newTimeL - oldTimeL) / 1000);

        /**
         *      调用 checkInputLogin()  方法检查用户密码是否输入正确
         * */
        if (checkInputLogin()) {
            // 动态验证码10分钟有效
            if ((newTimeL - oldTimeL) < (1000 * 60 * minute)) {

                UserJDBC userJDBC = new UserJDBC();



                System.out.println("登录成功");
                JOptionPane.showMessageDialog(this, "登录成功！", "恭喜", JOptionPane.PLAIN_MESSAGE);

                /**
                 *      启动主界面，传入的参数为用户输入的 手机号（每个用户只有唯一的一个手机号）
                 * */
                MainUI mainUI = new MainUI(textFieldEmailL.getText().toString(), imageName);
                mainUI.setVisible(true);
                this.dispose();

            }
        }
    }

    /**
     * @Description:  在登录界面统一检测用户输入是否正确
     * @Param: []
     * @return: boolean
     * @Author: 林凯
     * @Date: 2019/10/25
     */
    public boolean checkInputLogin() {
        /**
         *      注意：这里为了方便测试，动态验证码设置为输入 666666 也能登录
         * */

        // 检测用户是否输入登录信息
        System.out.println("1---->" + textFieldEmailL.getText());
        if (CheckEmailOrPhone.checkEmail(textFieldEmailL.getText()) == false && CheckEmailOrPhone.checkPhoneNumber(textFieldEmailL.getText()) == false) {
            JOptionPane.showMessageDialog(this, "请先输入正确的邮箱或手机号！", "错误", 0);
            return false;
        } else if (textFieldEmailL.getText().equals("邮箱或手机号")) {
            JOptionPane.showMessageDialog(this, "邮箱或手机号不能为空！", "错误", 0);
            return false;
        } else if (textFieldTPYZML.getText().equalsIgnoreCase(imageCode) == false) {
            JOptionPane.showMessageDialog(this, "图片验证码输入错误！", "错误", 0);
            return false;
        } else if (textFieldDTYZML.getText().equals(dynamicCode)  == false && textFieldDTYZML.getText().equals("666666") == false) {
            JOptionPane.showMessageDialog(this, "动态验证码输入错误！", "错误", 0);
            return false;
        } else if (checkBoxFlagL == false) {
            JOptionPane.showMessageDialog(this, "请同意服务条款！", "错误", 0);
            return false;
        }

        // 检测用户输入的登录信息是否正确
        if (CheckEmailOrPhone.checkEmail(textFieldEmailL.getText())) {
            System.out.println("电子邮箱！");
            /**
             *      邮箱比较麻烦，需要设置 POP3/SMTP 服务，而且有时间限制，得反复开启
             *      所以这里暂时不写
             * */
        } else if (CheckEmailOrPhone.checkPhoneNumber(textFieldEmailL.getText())) {
            // 如果是 手机号码
            System.out.println("手机号码！");
            UserJDBC userJDBC = new UserJDBC();

            String userPwd = new String(passwordFieldL.getPassword());      // 获得用户输入的密码
            /**
             *      将 手机号 和 密码 作为参数传入，在方法内部会 自动将密码加盐和和数据库中的对比，判断是否正确
             * */
            System.out.println("2---->" + textFieldEmailL.getText());

            int flag = userJDBC.checkUserByPhoneNumber(textFieldEmailL.getText(), userPwd);
            if (flag == 2) {
                // 手机号未注册
                JOptionPane.showMessageDialog(this, "用户为注册，请先注册后再登录！", "错误", JOptionPane.ERROR_MESSAGE);

                return false;
            } else if (flag == 3) {
                // 密码错误
                JOptionPane.showMessageDialog(this, "密码错误，请检查密码后再次输入！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        }

        return true;
    }

//########################################      注册界面    ######################################################

    /**
    * @Description:  注册界面，点击 "获取验证码" 按钮 ----(动态验证码)---> 鼠标释放时的事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/21
    */
    private void buttonDTYZMRMouseReleased(MouseEvent e) {
        if (buttonDTYZMR.isEnabled() == false) {
            return;     // 如果组件为禁用状态，直接返回
        }
        // TODO add your code here
        boolean emailFlag = CheckEmailOrPhone.checkEmail(textFieldEmailR.getText());
        boolean phoneFlag = CheckEmailOrPhone.checkPhoneNumber(textFieldEmailR.getText());
        if (emailFlag || phoneFlag) {
            // 发送验证码的同时开始计时
            oldTimeR = System.currentTimeMillis();
            // 同时，按钮上进行倒计时60秒，在60秒之内，按钮不能点击，60秒时候，显示“重新发送（用到多线程）”
            new Thread(new CountDown(buttonDTYZMR)).start();     //启动另一个线程
            buttonDTYZMR.setEnabled(false);

            if (emailFlag) {
                JOptionPane.showMessageDialog(this, "验证码已经发送到您的邮箱！", "成功", JOptionPane.PLAIN_MESSAGE);
                SendByThread emailThread = new SendByThread(2, textFieldEmailR.getText());
                dynamicCode = String.valueOf(emailThread.getDynamicCode());       // 获得对应的验证码，转换成字符串保存
            } else {
                JOptionPane.showMessageDialog(this, "验证码已经发送到您的手机！", "成功", JOptionPane.PLAIN_MESSAGE);
                SendMessage sendMessage = new SendMessage(textFieldEmailR.getText());
                System.out.println("手机号为：" + textFieldEmailR.getText());
                sendMessage.send();
                dynamicCode = String.valueOf(sendMessage.getRandomNumber());   // 获得对应的验证码，转换成字符串保存
            }
            emailOrPhoneFlagR = true;        // 最后在 checkInput() 方法中会用到
        } else {
            // 提示 “请先输入邮箱或者手机号”
            emailOrPhoneFlagR = false;
            JOptionPane.showMessageDialog(this, "请先输入正确的邮箱或手机号！", "错误", 0);
        }
    }

    /**
    * @Description:  在注册界面 点击 注册按钮
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/21
    */
    private void buttonRegisterMouseReleased(MouseEvent e) {
        // TODO add your code here
        newTimeR = System.currentTimeMillis();       // 获取当前时间毫秒数
        System.out.println("已用时间" + (newTimeR - oldTimeR) / 1000);
        if (checkInputRegister()) {
            // 动态验证码的有效时间为 10 分钟
            if ((newTimeR - oldTimeR) < (1000 * 60 * minute)) {
                System.out.println("注册");
                JOptionPane.showMessageDialog(this, "注册成功！", "恭喜", JOptionPane.PLAIN_MESSAGE);


                // 下面将数据存储到数据库中
                String pwdHash = SaltMd5Util.MD5WithSalt2(password1);       // 获取 密码对应的 哈希值
                UserJDBC myJDBC = new UserJDBC(textFieldUserNameR.getText(), pwdHash, textFieldEmailR.getText());

                myJDBC.storeUser();
                this.dispose();

                /**
                 *      让主界面显示出来，传递的参数为用户名
                 * */
                MainUI mainUI = new MainUI(textFieldUserNameR.getText(), imageName);
                mainUI.setVisible(true);
            }
        }
    }

    /**
    * @Description: 在注册界面 统一的检测用户输入是否正确
    * @Param: []
    * @return: boolean
    * @Author: 林凯
    * @Date: 2019/10/21
    */
    private boolean checkInputRegister() {
        /*
         *         注意：这里在比较字符串内容是否相等时，用到的是 equals() 方法，
         *        由于是判断验证码是否符合，所以改为使用 equalsIgnoreCase() 方法
         * */
        System.out.println("dynamcCode" + dynamicCode);
        System.out.println(textFieldDTYZMR.getText());
        char[] ch1 = passwordFieldR.getPassword();
        char[] ch2 = passwordFieldR2.getPassword();
        password1 = new String(ch1);
        password2 = new String(ch2);



        if (textFieldUserNameR.getText().equals("登录用户名")) {
            JOptionPane.showMessageDialog(this, "用户名不合法！", "错误", 0);
            return false;
        } else if (password1.equals("") || password2.equals("")) {
            JOptionPane.showMessageDialog(this, "密码不能为空！", "错误", 0);
            return false;
        } else if(password1.equals(password2) == false) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致！", "错误", 0);
            return false;
        } else if (textFieldTPYZMR.getText().equalsIgnoreCase(imageCode) == false) {
            JOptionPane.showMessageDialog(this, "图片验证码输入错误！", "错误", 0);
            return false;
        } else if (CheckEmailOrPhone.checkPhoneNumber(textFieldEmailR.getText()) == false && CheckEmailOrPhone.checkEmail(textFieldEmailR.getText()) == false) {
            JOptionPane.showMessageDialog(this, "请先输入正确的邮箱或手机号！", "错误", 0);
            return false;
        } else if (textFieldDTYZMR.getText().equals(dynamicCode) == false) {
            JOptionPane.showMessageDialog(this, "动态验证码输入错误！", "错误", 0);
            return false;
        } else if (checkBoxFlagR == false) {
            JOptionPane.showMessageDialog(this, "请同意服务条款！", "错误", 0);
            return false;
        }
        return true;
    }






    /**
    * @Description:  JFrameDesigner 自动生成的初始化UI界面的函数
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/21
    */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        labelLogin = new JLabel();
        labelRegister = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        scrollPane2 = new JScrollPane();
        textArea2 = new JTextArea();
        cardPanel = new JPanel();
        panelLogin = new JPanel();
        labelEmailL = new JLabel();
        labelPasswordL = new JLabel();
        labelYZML = new JLabel();
        labelDTYZML = new JLabel();
        textFieldEmailL = new JTextField();
        passwordFieldL = new JPasswordField();
        textFieldTPYZML = new JTextField();
        textFieldDTYZML = new JTextField();
        labelImageL = new JLabel();
        buttonDTYZML = new JButton();
        labelUnClear2 = new JLabel();
        labelHintL1 = new JLabel();
        checkBoxL = new JCheckBox();
        labelURLL = new JLabel();
        buttonLogin = new JButton();
        panelRegister = new JPanel();
        labelUserNameR = new JLabel();
        labelPasswordR = new JLabel();
        labelPasswordR2 = new JLabel();
        labelYZMR = new JLabel();
        labelEmailR = new JLabel();
        labelDTYZMR = new JLabel();
        textFieldUserNameR = new JTextField();
        passwordFieldR = new JPasswordField();
        passwordFieldR2 = new JPasswordField();
        textFieldTPYZMR = new JTextField();
        textFieldEmailR = new JTextField();
        textFieldDTYZMR = new JTextField();
        labelImageR = new JLabel();
        labelHintR1 = new JLabel();
        labelUnClearR = new JLabel();
        labelHintR2 = new JLabel();
        buttonDTYZMR = new JButton();
        labelURLR = new JLabel();
        checkBoxR = new JCheckBox();
        buttonRegister = new JButton();
        panelRegisterUnder = new JPanel();
        panelLoginUnder = new JPanel();

        //======== this ========
        setTitle("\u7528\u6237\u6ce8\u518c");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(0, 255, 51));
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- labelLogin ----
        labelLogin.setText("\u767b\u5f55");
        labelLogin.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
        labelLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                labelLoginMouseReleased(e);
            }
        });
        contentPane.add(labelLogin);
        labelLogin.setBounds(305, 65, 67, 29);

        //---- labelRegister ----
        labelRegister.setText("\u6ce8\u518c");
        labelRegister.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
        labelRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                labelRegisterMouseReleased(e);
            }
        });
        contentPane.add(labelRegister);
        labelRegister.setBounds(460, 65, 60, 29);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(405, 55, 2, 49);

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(textArea2);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(275, 115, 265, 2);

        //======== cardPanel ========
        {
            cardPanel.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
            . EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax
            . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
            12 ), java. awt. Color. red) ,cardPanel. getBorder( )) ); cardPanel. addPropertyChangeListener (new java. beans
            . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .
            getPropertyName () )) throw new RuntimeException( ); }} );
            cardPanel.setLayout(new CardLayout());

            //======== panelLogin ========
            {
                panelLogin.setLayout(null);

                //---- labelEmailL ----
                labelEmailL.setText("\u90ae\u7bb1/\u624b\u673a\u53f7:");
                labelEmailL.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelLogin.add(labelEmailL);
                labelEmailL.setBounds(48, 20, 150, 30);

                //---- labelPasswordL ----
                labelPasswordL.setText("\u5bc6   \u7801:");
                labelPasswordL.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelLogin.add(labelPasswordL);
                labelPasswordL.setBounds(100, 75, 105, 25);

                //---- labelYZML ----
                labelYZML.setText("\u9a8c\u8bc1\u7801:");
                labelYZML.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelLogin.add(labelYZML);
                labelYZML.setBounds(100, 135, 105, 25);

                //---- labelDTYZML ----
                labelDTYZML.setText("\u52a8\u6001\u9a8c\u8bc1\u7801\uff1a");
                labelDTYZML.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelLogin.add(labelDTYZML);
                labelDTYZML.setBounds(55, 195, 150, 30);

                //---- textFieldEmailL ----
                textFieldEmailL.setForeground(Color.lightGray);
                textFieldEmailL.setText("\u90ae\u7bb1\u8d26\u53f7\u6216\u624b\u673a\u53f7");
                textFieldEmailL.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                textFieldEmailL.setBorder(LineBorder.createGrayLineBorder());
                panelLogin.add(textFieldEmailL);
                textFieldEmailL.setBounds(200, 15, 270, 35);

                //---- passwordFieldL ----
                passwordFieldL.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
                passwordFieldL.setBorder(LineBorder.createGrayLineBorder());
                panelLogin.add(passwordFieldL);
                passwordFieldL.setBounds(200, 70, 270, 35);

                //---- textFieldTPYZML ----
                textFieldTPYZML.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                textFieldTPYZML.setForeground(Color.lightGray);
                textFieldTPYZML.setText("\u53f3\u4fa7\u9a8c\u8bc1\u7801");
                panelLogin.add(textFieldTPYZML);
                textFieldTPYZML.setBounds(200, 130, 115, 35);

                //---- textFieldDTYZML ----
                textFieldDTYZML.setForeground(Color.lightGray);
                textFieldDTYZML.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                textFieldDTYZML.setText("\u52a8\u6001\u9a8c\u8bc1\u7801");
                textFieldDTYZML.setBorder(LineBorder.createBlackLineBorder());
                panelLogin.add(textFieldDTYZML);
                textFieldDTYZML.setBounds(200, 195, 115, 35);

                //---- labelImageL ----
                labelImageL.setBackground(new Color(255, 153, 0));
                labelImageL.setBorder(new EtchedBorder());
                panelLogin.add(labelImageL);
                labelImageL.setBounds(380, 125, 120, 45);

                //---- buttonDTYZML ----
                buttonDTYZML.setText("\u83b7\u53d6\u9a8c\u8bc1\u7801");
                buttonDTYZML.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 18));
                buttonDTYZML.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        buttonDTYZMLMouseReleased(e);
                    }
                });
                panelLogin.add(buttonDTYZML);
                buttonDTYZML.setBounds(370, 195, 160, 34);

                //---- labelUnClear2 ----
                labelUnClear2.setText("\u770b\u4e0d\u6e05\u695a\uff1f");
                labelUnClear2.setForeground(Color.red);
                labelUnClear2.setFont(labelUnClear2.getFont().deriveFont(labelUnClear2.getFont().getStyle() | Font.BOLD));
                labelUnClear2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        labelUnClearMousePressed(e);
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        labelUnClearMouseReleased(e);
                    }
                });
                panelLogin.add(labelUnClear2);
                labelUnClear2.setBounds(525, 140, 80, 27);

                //---- labelHintL1 ----
                labelHintL1.setText("\u8bf7\u8f93\u5165\u5408\u6cd5\u7684\u90ae\u7bb1\u6216\u624b\u673a\u53f7");
                labelHintL1.setForeground(Color.red);
                labelHintL1.setVisible(false);
                panelLogin.add(labelHintL1);
                labelHintL1.setBounds(495, 30, 170, 18);

                //---- checkBoxL ----
                checkBoxL.setText("\u9605\u8bfb\u5e76\u540c\u610f");
                checkBoxL.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
                checkBoxL.addChangeListener(e -> checkBox1StateChanged(e));
                panelLogin.add(checkBoxL);
                checkBoxL.setBounds(215, 265, 105, 28);

                //---- labelURLL ----
                labelURLL.setText("\u300a\u670d\u52a1\u6761\u6b3e\u300b");
                labelURLL.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
                labelURLL.setForeground(Color.blue);
                labelURLL.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        labelURLMouseReleased(e);
                    }
                });
                panelLogin.add(labelURLL);
                labelURLL.setBounds(320, 265, 110, 28);

                //---- buttonLogin ----
                buttonLogin.setText("\u767b\u5f55");
                buttonLogin.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 22));
                buttonLogin.setBackground(new Color(51, 150, 193));
                buttonLogin.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        buttonLoginMouseReleased(e);
                    }
                });
                panelLogin.add(buttonLogin);
                buttonLogin.setBounds(260, 325, 160, 39);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panelLogin.getComponentCount(); i++) {
                        Rectangle bounds = panelLogin.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panelLogin.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panelLogin.setMinimumSize(preferredSize);
                    panelLogin.setPreferredSize(preferredSize);
                }
            }
            cardPanel.add(panelLogin, "card1");

            //======== panelRegister ========
            {
                panelRegister.setLayout(null);

                //---- labelUserNameR ----
                labelUserNameR.setText("\u7528\u6237\u540d:");
                labelUserNameR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelRegister.add(labelUserNameR);
                labelUserNameR.setBounds(100, 20, 105, 25);

                //---- labelPasswordR ----
                labelPasswordR.setText("\u5bc6   \u7801:");
                labelPasswordR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelRegister.add(labelPasswordR);
                labelPasswordR.setBounds(100, 70, 105, 25);

                //---- labelPasswordR2 ----
                labelPasswordR2.setText("\u786e\u8ba4\u5bc6\u7801\uff1a");
                labelPasswordR2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelRegister.add(labelPasswordR2);
                labelPasswordR2.setBounds(75, 125, 135, 25);

                //---- labelYZMR ----
                labelYZMR.setText("\u9a8c\u8bc1\u7801:");
                labelYZMR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelRegister.add(labelYZMR);
                labelYZMR.setBounds(100, 175, 105, 25);

                //---- labelEmailR ----
                labelEmailR.setText("\u90ae\u7bb1/\u624b\u673a\u53f7:");
                labelEmailR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelRegister.add(labelEmailR);
                labelEmailR.setBounds(50, 230, 150, 30);

                //---- labelDTYZMR ----
                labelDTYZMR.setText("\u52a8\u6001\u9a8c\u8bc1\u7801\uff1a");
                labelDTYZMR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                panelRegister.add(labelDTYZMR);
                labelDTYZMR.setBounds(55, 280, 150, 30);

                //---- textFieldUserNameR ----
                textFieldUserNameR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                textFieldUserNameR.setForeground(Color.lightGray);
                textFieldUserNameR.setText("\u767b\u5f55\u7528\u6237\u540d");
                textFieldUserNameR.setBorder(LineBorder.createBlackLineBorder());
                panelRegister.add(textFieldUserNameR);
                textFieldUserNameR.setBounds(200, 15, 270, 35);

                //---- passwordFieldR ----
                passwordFieldR.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
                passwordFieldR.setBorder(LineBorder.createGrayLineBorder());
                panelRegister.add(passwordFieldR);
                passwordFieldR.setBounds(200, 65, 270, 35);

                //---- passwordFieldR2 ----
                passwordFieldR2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
                passwordFieldR2.setBorder(LineBorder.createGrayLineBorder());
                panelRegister.add(passwordFieldR2);
                passwordFieldR2.setBounds(200, 120, 270, 35);

                //---- textFieldTPYZMR ----
                textFieldTPYZMR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                textFieldTPYZMR.setForeground(Color.lightGray);
                textFieldTPYZMR.setText("\u53f3\u4fa7\u9a8c\u8bc1\u7801");
                panelRegister.add(textFieldTPYZMR);
                textFieldTPYZMR.setBounds(200, 170, 115, 35);

                //---- textFieldEmailR ----
                textFieldEmailR.setForeground(Color.lightGray);
                textFieldEmailR.setText("\u90ae\u7bb1\u8d26\u53f7\u6216\u624b\u673a\u53f7");
                textFieldEmailR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                textFieldEmailR.setBorder(LineBorder.createGrayLineBorder());
                panelRegister.add(textFieldEmailR);
                textFieldEmailR.setBounds(200, 225, 270, 35);

                //---- textFieldDTYZMR ----
                textFieldDTYZMR.setForeground(Color.lightGray);
                textFieldDTYZMR.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                textFieldDTYZMR.setText("\u52a8\u6001\u9a8c\u8bc1\u7801");
                textFieldDTYZMR.setBorder(LineBorder.createBlackLineBorder());
                panelRegister.add(textFieldDTYZMR);
                textFieldDTYZMR.setBounds(200, 280, 115, 35);

                //---- labelImageR ----
                labelImageR.setBackground(new Color(255, 153, 0));
                labelImageR.setBorder(new EtchedBorder());
                panelRegister.add(labelImageR);
                labelImageR.setBounds(380, 165, 120, 45);

                //---- labelHintR1 ----
                labelHintR1.setText("\u7528\u6237\u540d\u5df2\u5b58\u5728");
                labelHintR1.setForeground(Color.red);
                labelHintR1.setVisible(false);
                panelRegister.add(labelHintR1);
                labelHintR1.setBounds(505, 30, 100, labelHintR1.getPreferredSize().height);

                //---- labelUnClearR ----
                labelUnClearR.setText("\u770b\u4e0d\u6e05\u695a\uff1f");
                labelUnClearR.setForeground(Color.red);
                labelUnClearR.setFont(labelUnClearR.getFont().deriveFont(labelUnClearR.getFont().getStyle() | Font.BOLD));
                labelUnClearR.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        labelUnClearMousePressed(e);
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        labelUnClearMouseReleased(e);
                    }
                });
                panelRegister.add(labelUnClearR);
                labelUnClearR.setBounds(525, 180, 80, 27);

                //---- labelHintR2 ----
                labelHintR2.setText("\u8bf7\u8f93\u5165\u5408\u6cd5\u7684\u90ae\u7bb1\u6216\u624b\u673a\u53f7");
                labelHintR2.setForeground(Color.red);
                labelHintR2.setVisible(false);
                panelRegister.add(labelHintR2);
                labelHintR2.setBounds(490, 240, 170, labelHintR2.getPreferredSize().height);

                //---- buttonDTYZMR ----
                buttonDTYZMR.setText("\u83b7\u53d6\u9a8c\u8bc1\u7801");
                buttonDTYZMR.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 18));
                buttonDTYZMR.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        buttonDTYZMRMouseReleased(e);
                    }
                });
                panelRegister.add(buttonDTYZMR);
                buttonDTYZMR.setBounds(370, 280, 160, 34);

                //---- labelURLR ----
                labelURLR.setText("\u300a\u670d\u52a1\u6761\u6b3e\u300b");
                labelURLR.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
                labelURLR.setForeground(new Color(51, 0, 255));
                labelURLR.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        labelURLMouseReleased(e);
                    }
                });
                panelRegister.add(labelURLR);
                labelURLR.setBounds(325, 350, 130, 28);

                //---- checkBoxR ----
                checkBoxR.setText("\u9605\u8bfb\u5e76\u540c\u610f");
                checkBoxR.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
                checkBoxR.addChangeListener(e -> checkBox1StateChanged(e));
                panelRegister.add(checkBoxR);
                checkBoxR.setBounds(220, 350, 105, checkBoxR.getPreferredSize().height);

                //---- buttonRegister ----
                buttonRegister.setText("\u6ce8\u518c");
                buttonRegister.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 22));
                buttonRegister.setBackground(new Color(51, 150, 193));
                buttonRegister.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        buttonRegisterMouseReleased(e);
                    }
                });
                panelRegister.add(buttonRegister);
                buttonRegister.setBounds(260, 410, 160, 39);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panelRegister.getComponentCount(); i++) {
                        Rectangle bounds = panelRegister.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panelRegister.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panelRegister.setMinimumSize(preferredSize);
                    panelRegister.setPreferredSize(preferredSize);
                }
            }
            cardPanel.add(panelRegister, "card2");
        }
        contentPane.add(cardPanel);
        cardPanel.setBounds(70, 150, 680, 520);

        //======== panelRegisterUnder ========
        {
            panelRegisterUnder.setBackground(Color.green);
            panelRegisterUnder.setVisible(false);
            panelRegisterUnder.setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panelRegisterUnder.getComponentCount(); i++) {
                    Rectangle bounds = panelRegisterUnder.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panelRegisterUnder.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panelRegisterUnder.setMinimumSize(preferredSize);
                panelRegisterUnder.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panelRegisterUnder);
        panelRegisterUnder.setBounds(420, 110, 120, 5);

        //======== panelLoginUnder ========
        {
            panelLoginUnder.setBackground(Color.green);
            panelLoginUnder.setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panelLoginUnder.getComponentCount(); i++) {
                    Rectangle bounds = panelLoginUnder.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panelLoginUnder.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panelLoginUnder.setMinimumSize(preferredSize);
                panelLoginUnder.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panelLoginUnder);
        panelLoginUnder.setBounds(275, 110, 120, 5);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        setSize(825, 685);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        // >>>>>>>>>>>>>>>>>>>>>      手动添加的代码        <<<<<<<<<<<<<<<<<<<
        // 0.首先向显示一张图片验证码
        imageCode = VerifyCodeUtils.createOneCodeImage();        // 调用静态方法，生成一张图片，然后返回代表图片的字符串
        System.out.println(imageCode);
//        File file = new File("D:\\IDEA文件\\测试Java发送邮件\\verifyCodeImg\\" + codeImage + ".jpg");       // 获得该图片的file对象
        File file = new File("src/YZMimages/" + imageCode + ".jpg");        // 获得图片的file对象
        Icon icon = new ImageIcon(file.toString());
        labelImageL.setIcon(icon);
        labelImageR.setIcon(icon);

        //

        /**
        * @Description: 1.添加文本框焦点事件  (给构造函数传入不同的参数，因为有的文本框后面要有 提示信息)
        */
        /**
        * @Description: // 给 注册界面添加
        */
        tfFocusUserNameR = new TextFieldFocusListener(textFieldUserNameR, "登录用户名", 1, labelHintR1);
        tfFocusTPYZMR = new TextFieldFocusListener(textFieldTPYZMR, "右侧验证码", 2);
        tfFocusEmailR = new TextFieldFocusListener(textFieldEmailR, "邮箱账号或手机号", 3, labelHintR2);
        tfFocusDTYZMR = new TextFieldFocusListener(textFieldDTYZMR, "动态验证码", 4);
        textFieldUserNameR.addFocusListener(tfFocusUserNameR);
        textFieldTPYZMR.addFocusListener(tfFocusTPYZMR);
        textFieldEmailR.addFocusListener(tfFocusEmailR);
        textFieldDTYZMR.addFocusListener(tfFocusDTYZMR);

        /**
        * @Description:   给 登录界面添加
        */
        tfFocusEmailL = new TextFieldFocusListener(textFieldEmailL, "邮箱账号或手机号", 3, labelHintL1);
        tfFocusTPYZML = new TextFieldFocusListener(textFieldTPYZML, "右侧验证码", 2);
        tfFocusDTYZML = new TextFieldFocusListener(textFieldDTYZML, "动态验证码", 4);
        textFieldEmailL.addFocusListener(tfFocusEmailL);        // 添加监听事件
        textFieldTPYZML.addFocusListener(tfFocusTPYZML);
        textFieldDTYZML.addFocusListener(tfFocusDTYZML);
        // 2.验证各项信息是否输入正确
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel labelLogin;
    private JLabel labelRegister;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;
    private JPanel cardPanel;
    private JPanel panelLogin;
    private JLabel labelEmailL;
    private JLabel labelPasswordL;
    private JLabel labelYZML;
    private JLabel labelDTYZML;
    private JTextField textFieldEmailL;
    private JPasswordField passwordFieldL;
    private JTextField textFieldTPYZML;
    private JTextField textFieldDTYZML;
    private JLabel labelImageL;
    private JButton buttonDTYZML;
    private JLabel labelUnClear2;
    private JLabel labelHintL1;
    private JCheckBox checkBoxL;
    private JLabel labelURLL;
    private JButton buttonLogin;
    private JPanel panelRegister;
    private JLabel labelUserNameR;
    private JLabel labelPasswordR;
    private JLabel labelPasswordR2;
    private JLabel labelYZMR;
    private JLabel labelEmailR;
    private JLabel labelDTYZMR;
    private JTextField textFieldUserNameR;
    private JPasswordField passwordFieldR;
    private JPasswordField passwordFieldR2;
    private JTextField textFieldTPYZMR;
    private JTextField textFieldEmailR;
    private JTextField textFieldDTYZMR;
    private JLabel labelImageR;
    private JLabel labelHintR1;
    private JLabel labelUnClearR;
    private JLabel labelHintR2;
    private JButton buttonDTYZMR;
    private JLabel labelURLR;
    private JCheckBox checkBoxR;
    private JButton buttonRegister;
    private JPanel panelRegisterUnder;
    private JPanel panelLoginUnder;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        UIFrame uiFrame = new UIFrame();
        uiFrame.setVisible(true);
    }
}
