package mytest;

/*
*       2019年10月19日20:00:04
*       注：实际的大作业中并没有用到这个类
*       最终测试，完成一个注册登录的的界面
* */

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JTextField textFieldUserName;       // 输入用户名的文本框
    private JPasswordField textFieldPassword;       // 输入密码的文本框
    private JLabel labelName;
    private JLabel labelPassword;
    private JButton buttonLogin;        // 登录按钮
    private JButton buttonReset;        // 重置按钮

    public Login() {
        setBounds(200, 200, 500, 500);
        setTitle("登录/注册");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container container = getContentPane();     // 获得当前窗体的 内容窗格
        container.setLayout(null);      // 设置布局：绝对布局

        // 初始化控件对象
        textFieldUserName = new JTextField();       // 用户名 文本框
        textFieldUserName.setBounds(200, 100, 250, 30);
        textFieldUserName.setFont(new Font("微软雅黑", Font.BOLD, 24));
        container.add(textFieldUserName);

        textFieldPassword = new JPasswordField();       // 密码  密码框
        textFieldPassword.setBounds(200, 150, 250, 30);
        textFieldPassword.setFont(new Font("微软雅黑", Font.BOLD, 24));
        container.add(textFieldPassword);

        labelName = new JLabel("用户名:");
        labelName.setBounds(150, 100, 35, 30);
        container.add(labelName);
        labelPassword = new JLabel("密码:");
        labelPassword.setBounds(150, 150, 35, 30);
        container.add(labelPassword);


        setVisible(true);
    }

    // 测试用到的主方法
    public static void main(String[] args) {
        new Login();
    }
}
