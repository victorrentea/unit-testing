package ro.victor.unittest.goldenmaster;

import java.util.Random;

public class RandomNuERandom {
    public static void main(String[] args) {

        Random r = new Random(1);
        System.out.println(r.nextInt());
        System.out.println(r.nextInt());
        System.out.println(r.nextInt());
        System.out.println(r.nextInt());
        System.out.println(r.nextInt());
        System.out.println("---");
        Random r2 = new Random(1);
        System.out.println(r2.nextInt());
        System.out.println(r2.nextInt());
        System.out.println(r2.nextInt());
        System.out.println(r2.nextInt());
        System.out.println(r2.nextInt());
    }
}
