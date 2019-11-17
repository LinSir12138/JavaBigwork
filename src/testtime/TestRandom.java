package testtime;

import java.util.Random;

public class TestRandom {
    public static void main(String[] args) {
        Random random = new Random(666);
        for(int i = 0; i < 5; i++){
            // 生成[1,100]之间的随机数
            System.out.print(random.nextInt(100) + 1 + " ");
        }
        System.out.println();
        Random randomII = new Random(666);
        for(int i = 0; i < 5; i++){
            System.out.print(randomII.nextInt(100) + 1 + " ");
        }
    }
}
