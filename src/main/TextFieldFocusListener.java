package main;

/*
*       2019年10月19日21:14:50
*       （封装了一个 文本框焦点事件类），用来处理文本框的焦点事件
*       功能：给文本框添加提示默认值，检测用户文本框中输入的内容
*       实现思路：   当获得焦点时，输入内容
*                     失去焦点时，显示提示内容
*                  通过构造方法传入需要提示的字符串
* */

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextFieldFocusListener implements FocusListener {
    private JTextField textField;   // 传进来的 文本框对象
    String hint;        // 提示字符串
    int flag;       // 组件的标志，用来判断是哪个组件(实际上也可以用名字区分)
    JLabel labelHint;       // 文本框对应的提示标签，如果没有的话，默认为null

    public TextFieldFocusListener() {

    }

    // 通过传递进来的flag来判断是哪个组件（因为所有的组件都是 JTextField 类的子类，所以不能用 instanceof ）
    public TextFieldFocusListener(JTextField textField, String hint, int flag) {
        this.textField = textField;
        this.hint = hint;
        this.flag = flag;
        this.labelHint = null;
    }

    // 构造方法的重载：多了一个参数：JLable，是文本框后面的提示信息
    public TextFieldFocusListener(JTextField textField, String hint, int flag, JLabel labelHint) {
        this.textField = textField;
        this.hint = hint;
        this.flag = flag;
        this.labelHint = labelHint;
    }

    @Override
    public void focusGained(FocusEvent e) {
        // 获得焦点
//        Object obj = e.getSource();     // 获得触发焦点事件的控件  可以省略，因为已经作为参数传进来了
        System.out.println(textField.getText());
        if (textField.getText().equals(hint)) {
            // 如果内容是提示内容，则置位空
            textField.setText("");
            textField.setForeground(Color.BLACK);
        } else if (textField.getText().equals("") == false){
            // 已经有了输入的内容，下面就需要改变字体
            textField.setFont(new Font("微软雅黑", Font.PLAIN, 22));
            textField.setForeground(Color.BLACK);
        }
        System.out.println("focusGet");

        switch (flag) {
            case 1:     // 登录用户名
                userNameGained();
                break;
            case 2:     // 图片验证码
                TPYZMGained();
                break;
            case 3:     // 邮箱或手机号
                emailOrPhoneGained();
                break;
            case 4:     // 动态验证码
                DTYZMGained();
                break;
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        // 失去焦点
        if (textField.getText().equals("")) {
            // 失去焦点时，如何内容为空，就显示提示文字
            textField.setText(hint);
            textField.setFont(new Font("微软雅黑", Font.PLAIN, 22));
            textField.setForeground(Color.lightGray);
        }

        switch (flag) {
            case 1:     // 登录用户名
                userNameLost();
                break;
            case 2:     // 图片验证码
                TPZYMLost();
                break;
            case 3:     // 邮箱或手机号
                emailOrPhoneLost();
                break;
            case 4:     // 动态验证码
                DTYZMLost();
                break;
        }
    }

    // 用户名框对应的 focus 事件
    public void userNameGained() {

    }
    public void userNameLost() {
        // 待完善----》从数据库中查找用户名，看是否存在相同的用户名
    }

    // 右边的图片验证码对应的 focus 事件
    public void TPYZMGained() {
        // 不需要做任何事情
    }
    public void TPZYMLost() {
        // 不需要做任何事情
//        this.imageVerificationCode = textField.getText();
    }

    // 邮箱或手机号 对应的 focus 事件
    public void emailOrPhoneGained() {
        // 设置边框
        textField.setBorder(new LineBorder(Color.GRAY, 1, false));
    }
    public void emailOrPhoneLost() {
        // 检测是否合法
        String tempInput = textField.getText();     // 获取文本框中的文本
        System.out.println(tempInput);
        if (CheckEmailOrPhone.checkEmail(tempInput) || CheckEmailOrPhone.checkPhoneNumber(tempInput) || tempInput.equals("邮箱账号或手机号")) {
            // 合法输入（未输入任何内容时，默认显示 “邮箱账号或手机号”）
            labelHint.setVisible(false);        // 合法输入时，隐藏提示信息
        } else {
            System.out.println("输入不和法");
            textField.setBorder(new LineBorder(Color.RED, 3, true));
            labelHint.setVisible(true);         // 输入不合法时，显示提示信息
        }
//        this.emainOrPhone = textField.getText();
    }

    // 动态验证码 对应的 focus 事件
    public void DTYZMGained() {

    }
    public void DTYZMLost() {
//        this.dynamicVerificationCode = textField.getText();
    }

}
