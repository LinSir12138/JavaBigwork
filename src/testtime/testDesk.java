package testtime;

//package com.xiets.desktop;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class testDesk {

    public static void main(String[] args) throws IOException {
        // 先判断当前平台是否支持桌面
        if (Desktop.isDesktopSupported()) {
            // 获取当前平台桌面实例
            Desktop desktop = Desktop.getDesktop();

            // 使用默认浏览器打开链接
            desktop.browse(URI.create("https://blog.csdn.net/xietansheng"));

            // 打开指定文件/文件夹
//            desktop.open(new File("C:\\"));

        } else {
            System.out.println("当前平台不支持 Desktop");
        }
    }

}
