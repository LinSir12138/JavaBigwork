package main;

/*
*       2019年10月21日10:40:57
*       功能：用于登录注册界面的 按钮上的倒计时
*       调用关系：被 UIFram 类调用
*       利用多线程实现 按钮 上的倒计时
*   ----》 继承 Runnable 接口，重新 run() 方法
* */

import javax.swing.*;

public class CountDown implements Runnable {
    JButton button;
    public CountDown(JButton button) {
        this.button = button;
//        myInit();
    }

//    public void myInit() {
//        button.setEnabled(false);        // 设置按钮不能点击
//    }

    @Override
    public void run() {
        int begin = 60;
        while (true) {
            button.setText(Integer.toString(begin));
            try {
                Thread.sleep(1000);     // 线程睡眠
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            begin--;
            if (begin < 0) {
                break;
            }
        }
        button.setText("重新获取验证码");
        button.setEnabled(true);        // 设置按钮启用
    }
}
