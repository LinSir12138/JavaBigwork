package slatMD5;

/*
*       自己写的 加盐MD5加密算法
*               加盐详解：
*       1.以hash值存储用户密码（MD5哈希函数），由于哈希函数存在哈希碰撞的特性，当数据库被攻击时，通过密码的哈希值，还是可以同过一定的方法（例如彩虹表攻击,字典攻击，暴力破解等）破解用户密码
*       2.加盐原理：由原来的 H(p) 变成了 H(p + slat) ,哈希函数发生了变化，且 salt 是随机的，使得彩虹表无法使用
*       3.加盐注意事项：不要使用重复的盐值和较短的盐值，盐值一般和哈希函数输出的字符串等长
*       4. 常用的哈希算法：
*           算法          输出长度（位）     输入长度（字节）
*           MD5         128 bits            16 bytes
*           SHA-1       160 bits            20 bytes
*           SHA-256     256 bits            32 bytes
*       5.利用哈希函数加密用户密码的基本思路：
*       （1）注册时
*               1.获取用户输入的账号，密码
*               2.后台随机生成一个salt
*               3.H(p + salt) 生成哈希值，同时将哈希值和盐存入数据库中
*       （2）登录时
*               1.获取用户输入的账号，密码
*               2.通过账号取得对应的记录，取得带盐的密码（密码以哈希值的方式存储），再从密码中获得盐（盐是排列在固定位置的）
*               3.将用户输入的密码和盐混合生成一个新的哈希值
*               4.将这个新的哈希值和数据库里面的哈希值匹配，如果相等，登录成功，反之登录失败
* */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class SaltMd5Util {
    // 生成盐时用到的字符数组
    private static char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
    * @Description: 自定义简单生成盐，是一个随机的长度为16的字符串，每个字符是随机的16进制zhif
    * @Param: []
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/5
    */
    public static String creatSalt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < sb.capacity(); i++) {
            sb.append(hex[random.nextInt(16)]);     // 生成 1~16 之间的随机数，然后以随数作为下标，在字符数组中取值
        }
        System.out.println("生成的盐为：" + sb.toString().toLowerCase());
        return sb.toString().toLowerCase();     // 全部转换成为小写字母
    }

    /**
    * @Description: 不带盐的MD5加密
    * @Param: [inputPassword]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/5
    */
    public static String MD5WithoutSalt(String inputPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(inputPassword.getBytes());
            // 下面两步是将字节数组转换成为对应的16进制字符串
            BigInteger bigInteger = new BigInteger(1, result);
            return bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("加密失败");
            return inputPassword;
        }
    }

    /**
    * @Description: 加盐的MD5算法,返回单独密文（里面不包含盐），是一个16位的字符串，盐在方法内部生成
    * @Param: [inputPassword]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/5
    */
    public static String MD5WithSalt(String inputPassword, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String inputSalt = inputPassword + salt;        // 加盐，输入加盐，盐默认添加到用户密码的后面
            byte[] hashResultByte = md.digest(inputSalt.getBytes());        // 获得用MD5生成的密文，16字节（byte）的字节数组，128位（bit）
            // 将字节数组转换成为字符串
            String hashResult = new BigInteger(1, hashResultByte).toString(16);
            return hashResult;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("加密错误");
            return inputPassword;
        }
    }

    /**
    * @Description:   加盐的MD5算法,返回的密文里面包含盐，是一个48位的字符串,盐在方法内部生成（用在注册时生成哈希值）
    * @Param: [inputPassword]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/5
    */
    public static String MD5WithSalt2(String inputPassword) {
        String salt = creatSalt();
        int indexMD5password = 0;
        int indexSalt = 0;
        /**
         *      调用已有的方法，获得密文，然后将盐和密文混合在一起
         * */
        char[] MD5password = MD5WithSalt(inputPassword, salt).toCharArray();        // 密文对应的字符数组
        char[] saltChar = salt.toCharArray();       // 盐对应的字符数组
        char[] ch = new char[48];
        // 每两位密文后面加一位盐（密文对应的字符数组长度为32，盐对应的字符数组长度为16，所以为2个密文后面添加1个盐）
        for (int i = 0; i < 48; i +=3) {
            ch[i] = MD5password[indexMD5password];
            ch[i + 1] = MD5password[++indexMD5password];
            ch[i + 2] = saltChar[indexSalt++];
        }
        return new String(ch);
    }

    /**
    * @Description: 重载方法，需要传入盐值和密码，生成哈希值，登录时  用来检测用户输入密码是否正确
    * @Param: [inputPassword, salt]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/12/4
    */
    public static String MD5WithSalt2(String inputPassword, String salt) {
        int indexMD5password = 0;
        int indexSalt = 0;
        /**
         *      调用已有的方法，获得密文，然后将盐和密文混合在一起
         * */
        char[] MD5password = MD5WithSalt(inputPassword, salt).toCharArray();        // 密文对应的字符数组
        char[] saltChar = salt.toCharArray();       // 盐对应的字符数组
        char[] ch = new char[48];
        // 每两位密文后面加一位盐（密文对应的字符数组长度为32，盐对应的字符数组长度为16，所以为2个密文后面添加1个盐）
        for (int i = 0; i < 48; i +=3) {
            ch[i] = MD5password[indexMD5password];
            ch[i + 1] = MD5password[++indexMD5password];
            ch[i + 2] = saltChar[indexSalt++];
        }
        return new String(ch);
    }

    /**
    * @Description: 从hash值中提取盐
    * @Param: [hash]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/5
    */
    public static String getSaltFromHash(String hash) {
        char[] salt = new char[16];
        char[] hashToCharArray = hash.toCharArray();
        int j = 0;
        for (int i = 0; i < 48; i++) {
            // 因为数组下标从0开始，对3取余是因为在将密文哈盐混在一起时，每隔2个密文后面放一个盐
            if ((i + 1) % 3 == 0) {
                salt[j++] = hashToCharArray[i];
            }
        }
        return new String(salt);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputPassword = scanner.nextLine();
        System.out.println("明文：" + inputPassword);
        System.out.println("无盐MD5加密：" + MD5WithoutSalt(inputPassword));
        System.out.println("带盐MD5加密：" + MD5WithSalt(inputPassword, creatSalt()));
        String test = MD5WithSalt2(inputPassword);
        System.out.println("带盐MD5加密(生成带盐密文)：" + test);
        System.out.println("从带盐MD5密文中获取的盐值：" + getSaltFromHash(test));

    }
}
