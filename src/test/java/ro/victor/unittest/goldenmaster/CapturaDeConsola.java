package ro.victor.unittest.goldenmaster;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CapturaDeConsola
{
    public static void main(String[] args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream initialOut = System.out;
        System.setOut(new PrintStream(baos));
        System.out.println("Halo");
        System.out.println("World");

        System.setOut(initialOut);
        String s2 = new String(baos.toByteArray());
//        String s = "Halo\nWorld";

        System.out.println("S2 adunat este " +s2);
    }
}
