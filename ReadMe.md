## 项目介绍
这个项目是一个基于 Java Swing + 人脸识别 编写的学校考试系统，是在学习 Java 基础的时候编写的，几乎涵盖了 Java 基础面向对象，封装继承多态，I/O 流，多线程，网络，数据库的所有知识。
在此基础之上充分利用了各大云服务厂商提供的API接口实现了一些高级的需求。

## 项目预览
- 用户登录界面
![登录注册界面](https://github.com/LinSir12138/JavaBigwork/blob/master/src/images/readme_1.png)
- 试题管理界面
![试题管理界面](https://github.com/LinSir12138/JavaBigwork/blob/master/src/images/readme_2.png)
- 考试前人脸识别验证界面
![人脸识别验证界面](https://github.com/LinSir12138/JavaBigwork/blob/master/src/images/readme_4.png)
- 考试界面
![考试界面](https://github.com/LinSir12138/JavaBigwork/blob/master/src/images/readme_5.png)
- 考试成绩导出为图表
![考试成绩导出为图表](https://github.com/LinSir12138/JavaBigwork/blob/master/src/images/readme_6.png)

## 项目用到的主要技术
1. 利用阿里云的短信API服务提供登录注册时的短信发送
2. 利用 Java Mail 实现发送邮件
3. 用户密码采用 MD5 加盐加密
4. 利用百度AI的人脸识别接口整合Java Swing 实现了人脸识别
5. 利用 Spire 类库实现校园卡上的条形码识别
6. 利用 JFreeChart 将学生成绩等导出为图表
7. 利用阿里巴巴的 easyExcel 组件实现了试题导出，导入 Excel 表格
8. 基本的 CRUD 操作
9. 借助 JFromDesigner 辅助设置UI界面
10. 利用原生 I/O 流配合网络已经正则表达式从必应官网网爬取每日一图作为封面

## 项目主要的功能
1. 用户分为教师，学生，和管理员
2. 教师可以对试题进行增删改查，对试卷进行增删改查，对考试进行增删改查
3. 学生完成人脸识别验证之后就可以参加考试，考试成绩将保存到数据库中
4. 管理员可以创建或者删除教师和学生账户
5. 利用I/O流爬取网络图片作为封面
