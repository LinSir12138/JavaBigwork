package mytest;//package mytest;
//
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class MyDateTest extends JFrame {
//    // 窗体里面放置一个 JPanel
//    private JPanel myJpanel;
//
//    int startYear = 1980; // 默认【最小】显示年份
//    int lastYear = 2050; // 默认【最大】显示年份
//    int width = 390; // 界面宽度
//    int height = 210; // 界面高度
//    Color backGroundColor = Color.gray; // 底色
//    // 月历表格配色----------------//
//    Color palletTableColor = Color.white; // 日历表底色
//    Color todayBackColor = Color.orange; // 今天背景色
//    Color weekFontColor = Color.blue; // 星期文字色
//    Color dateFontColor = Color.black; // 日期文字色
//    Color weekendFontColor = Color.red; // 周末文字色
//    // 控制条配色------------------//
//    Color controlLineColor = Color.pink; // 控制条底色
//    Color controlTextColor = Color.white; // 控制条标签文字色
//    Color rbFontColor = Color.white; // RoundBox文字色
//    Color rbBorderColor = Color.red; // RoundBox边框色
//    Color rbButtonColor = Color.pink; // RoundBox按钮色
//    Color rbBtFontColor = Color.red; // RoundBox按钮文字色
//    /** 点击DateChooserButton时弹出的对话框，日历内容在这个对话框内 */
//    JDialog dialog;
//    JSpinner yearSpin;
//    JSpinner monthSpin;
//    JSpinner daySpin;
//    JSpinner hourSpin;
//    JSpinner minuteSpin;
//    JSpinner secondSpin;
//    JButton[][] daysButton = new JButton[6][7];
//
//    public MyDateTest() {
//        setBounds(100, 100, 200, 200);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        Container container = getContentPane();
//        setVisible(true);
//
//        myJpanel = new JPanel();
//        setLayout(new BorderLayout());
////        setBorder(new LineBorder(backGroundColor, 2));
//        setBackground(backGroundColor);
//
//        JPanel topYearAndMonth = createYearAndMonthPanal();
//        add(topYearAndMonth, BorderLayout.NORTH);
//        JPanel centerWeekAndDay = createWeekAndDayPanal();
//        add(centerWeekAndDay, BorderLayout.CENTER);
//        JPanel buttonBarPanel = createButtonBarPanel();
//        this.add(buttonBarPanel, java.awt.BorderLayout.SOUTH);
//
//
//    }
//
//    private JPanel createYearAndMonthPanal() {
//        Calendar c = getCalendar();
//        int currentYear = c.get(Calendar.YEAR);
//        int currentMonth = c.get(Calendar.MONTH) + 1;
//        int currentHour = c.get(Calendar.HOUR_OF_DAY);
//        int currentMinute = c.get(Calendar.MINUTE);
//        int currentSecond = c.get(Calendar.SECOND);
//
//        JPanel result = new JPanel();
//        result.setLayout(new FlowLayout());
//        result.setBackground(controlLineColor);
//
//        yearSpin = new JSpinner(new SpinnerNumberModel(currentYear, startYear, lastYear, 1));
//        yearSpin.setPreferredSize(new Dimension(48, 20));
//        yearSpin.setName("Year");
//        yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
//        yearSpin.addChangeListener(this);
//        result.add(yearSpin);
//
//        JLabel yearLabel = new JLabel("年");
//        yearLabel.setForeground(controlTextColor);
//        result.add(yearLabel);
//
//        monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 12, 1));
//        monthSpin.setPreferredSize(new Dimension(35, 20));
//        monthSpin.setName("Month");
//        monthSpin.addChangeListener(this);
//        result.add(monthSpin);
//
//        JLabel monthLabel = new JLabel("月");
//        monthLabel.setForeground(controlTextColor);
//        result.add(monthLabel);
//
//        //如果这里要能够选择,会要判断很多东西,比如每个月分别由多少日,以及闰年问题.所以,就干脆把Enable设为false
//        daySpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 31, 1));
//        daySpin.setPreferredSize(new Dimension(35, 20));
//        daySpin.setName("Day");
//        daySpin.addChangeListener(this);
//        daySpin.setEnabled(false);
//        daySpin.setToolTipText("请下下面的日历面板中进行选择哪一天！");
//        result.add(daySpin);
//
//        JLabel dayLabel = new JLabel("日");
//        dayLabel.setForeground(controlTextColor);
//        result.add(dayLabel);
//
//        hourSpin = new JSpinner(new SpinnerNumberModel(currentHour, 0, 23, 1));
//        hourSpin.setPreferredSize(new Dimension(35, 20));
//        hourSpin.setName("Hour");
//        hourSpin.addChangeListener(this);
//        result.add(hourSpin);
//
//        JLabel hourLabel = new JLabel("时");
//        hourLabel.setForeground(controlTextColor);
//        result.add(hourLabel);
//
//        minuteSpin = new JSpinner(new SpinnerNumberModel(currentMinute, 0, 59, 1));
//        minuteSpin.setPreferredSize(new Dimension(35, 20));
//        minuteSpin.setName("Minute");
//        minuteSpin.addChangeListener(this);
//        result.add(minuteSpin);
//
//        JLabel minuteLabel = new JLabel("分");
//        hourLabel.setForeground(controlTextColor);
//        result.add(minuteLabel);
//
//        secondSpin = new JSpinner(new SpinnerNumberModel(currentSecond, 0, 59, 1));
//        secondSpin.setPreferredSize(new Dimension(35, 20));
//        secondSpin.setName("Second");
//        secondSpin.addChangeListener(this);
//        result.add(secondSpin);
//
//        JLabel secondLabel = new JLabel("秒");
//        hourLabel.setForeground(controlTextColor);
//        result.add(secondLabel);
//
//        return result;
//    }
//
//    private JPanel createWeekAndDayPanal() {
//        String colname[] = {"日", "一", "二", "三", "四", "五", "六"};
//        JPanel result = new JPanel();
//        // 设置固定字体，以免调用环境改变影响界面美观
//        result.setFont(new Font("宋体", Font.PLAIN, 12));
//        result.setLayout(new GridLayout(7, 7));
//        result.setBackground(Color.white);
//        JLabel cell;
//
//        for (int i = 0; i < 7; i++) {
//            cell = new JLabel(colname[i]);
//            cell.setHorizontalAlignment(JLabel.RIGHT);
//            if (i == 0 || i == 6) {
//                cell.setForeground(weekendFontColor);
//            } else {
//                cell.setForeground(weekFontColor);
//            }
//            result.add(cell);
//        }
//
//        int actionCommandId = 0;
//        for (int i = 0; i < 6; i++) {
//            for (int j = 0; j < 7; j++) {
//                JButton numberButton = new JButton();
//                numberButton.setBorder(null);
//                numberButton.setHorizontalAlignment(SwingConstants.RIGHT);
//                numberButton.setActionCommand(String.valueOf(actionCommandId));
//                numberButton.addActionListener(this);
//                numberButton.setBackground(palletTableColor);
//                numberButton.setForeground(dateFontColor);
//                if (j == 0 || j == 6) {
//                    numberButton.setForeground(weekendFontColor);
//                } else {
//                    numberButton.setForeground(dateFontColor);
//                }
//                daysButton[i][j] = numberButton;
//                result.add(numberButton);
//                actionCommandId++;
//            }
//        }
//
//        return result;
//    }
//
//    private JPanel createButtonBarPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new java.awt.GridLayout(1, 2));
//
//        JButton ok = new JButton("确定");
//        ok.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //记忆原始日期时间
//                initOriginalText(getTextOfDateChooserButton());
//                //隐藏日历对话框
//                dialog.setVisible(false);
//            }
//        });
//        panel.add(ok);
//
//        JButton cancel = new JButton("取消");
//        cancel.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 取消的话，什么也不用做
//                //恢复原始的日期时间
////                    restoreTheOriginalDate();
//                //隐藏日历对话框
//                dialog.setVisible(false);
//            }
//        });
//
//        panel.add(cancel);
//        return panel;
//    }
//
//
//
//
//
//}