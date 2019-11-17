package testtime;

/*
*       2019年10月12日19:28:43
*       测试倒计时，实现验证码10分钟有效
* */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestTime {
    public static void testDate() {
        Date date0 = new Date(0);
        System.out.println(date0);
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        // 把对象按照“格式字符串指定的格式”转成相应的字符串
        DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh:mm:ss");
        String time = df.format(new Date());
        System.out.println(time);
    }
    public static void testDateFormat() {
        // 把字符串按照“格式字符串指定的格式”转换成为相应的时间对象
        DateFormat df2 = new SimpleDateFormat("YYYY年MM月dd日 hh时mm分ss秒");
        try {
            // 这里的字符串必须匹配格式，包括空格也需要匹配，否则会抛出异常
            Date date = df2.parse("2019年10月12日 20时46分16秒");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void testCalendar() {
        // 如果直接输入Calendar对象的话，里面包含很多信息，我们可以通过 get(） 方法来获取指定的信息
        Calendar c1 = new GregorianCalendar(2000, 7, 28, 5, 20, 00);
        System.out.println(c1.get(Calendar.YEAR));
        System.out.println(c1.get(Calendar.MONTH));     //月份从 0~11，即11表示12月份
        System.out.println(Calendar.DAY_OF_WEEK);       //星期几：1~7,  1：星期天，2：星期一
        System.out.println(c1);

        // 不传递任何参数的话，Calendar对象表示当前日期
        Calendar c2 = new GregorianCalendar();
        c2.set(Calendar.YEAR, 2088);        //可以通过 set() 方法设置年月日，这里设置了年份，但是其他例如月份，星期之类的不改变
        System.out.println(c2);

        // 日期的计算
        Calendar c3 = new GregorianCalendar();
        c3.add(Calendar.DATE, 100);     //计算当前日期往后100天,也可以设置为负数，表示往前
        System.out.println(c3);

        // 日期对象和时间对象的转换
        Date d4 = c3.getTime();     // 日期对象------》时间对象
        Calendar c4 = new GregorianCalendar();
        c4.setTime(new Date());     // 时间对象--------》日期对象

    }

    // 封装打印日期的类方法，传入一个 Calendar 对象，打印年月时分秒
    public static void print(Calendar c) {
        // 打印：2019年10月12日21:38:41
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DAY_OF_MONTH);
        int dayWeek = c.get(Calendar.DAY_OF_WEEK);  // 星期：1~7,   1--》星期天
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        System.out.println(year + "年" + month + "月" + date + "日" + "  " + "星期" + dayWeek +
                hour + "时" + minute + "分" + second + "秒");

    }

    public static void testRandom() {
        Random random = new Random(47);
        for(int i = 0; i < 3; i++){
            System.out.print(random.nextInt(20) + " ");
        }
        System.out.println();
        Random randomII = new Random(47);
        for(int i = 0; i < 3; i++){
            System.out.print(randomII.nextInt(20)+ " ");
        }
    }

    public static void main(String[] args) {
//        testDateFormat();
//        testCalendar();
//        print(new GregorianCalendar());
        testRandom();
    }
}
