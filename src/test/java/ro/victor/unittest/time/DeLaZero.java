package ro.victor.unittest.time;

import lombok.Builder;
import lombok.Data;
import org.junit.After;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DeLaZero {

    public static final int MILLIS_PER_DAY = 60 * 60 * 24 * 1000;


    @Test
    public void testTime2() {
        Date date = codeDeProd();
        assertEquals(new Date().getTime() / MILLIS_PER_DAY, date.getTime() / MILLIS_PER_DAY);
    }

    @Test
    public void testTime6() {
        Date date = codeDeProd();
        assertEquals(new Date().getTime() / MILLIS_PER_DAY, date.getTime() / MILLIS_PER_DAY);
    }

    @Test
    public void testTime5() {
        Date date = codeDeProd();
        assertEquals(new Date().getTime() / MILLIS_PER_DAY, date.getTime() / MILLIS_PER_DAY);
    }

    @Test
    public void testTime4() {
        Date date = codeDeProd();
        assertEquals(new Date().getTime() / MILLIS_PER_DAY, date.getTime() / MILLIS_PER_DAY);
    }

    @Test
    public void testTime3() throws ParseException {
        Date testTime = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2019");
        TimeMachine.setFixedTestTime(testTime);
        Date date = codeDeProd();
        assertEquals(testTime.getTime(), date.getTime());
    }

    @After
    public void flushTestTime() {
        TimeMachine.clearTestTime();
    }

    public Date codeDeProd() {
        Date date = TimeMachine.getCurrentDate();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return date;
    }
}

class TimeMachine {
    private static Date fixedTestTime;

    public static Date getCurrentDate() {
        if (fixedTestTime != null) {
            Date fixedTestTime = TimeMachine.fixedTestTime;
            return fixedTestTime;
        } else {
            return new Date();
        }
    }

    public static void setFixedTestTime(Date fixedTestTime) {
        TimeMachine.fixedTestTime = fixedTestTime;
    }


    public static void clearTestTime() {
        fixedTestTime = null;
    }
}


@Data
class CuDate {

    static {
        new CuDate()
                .setA("a")
                .setB("b")
                .setC("C");
    }

    private String a, b, c, d;

//    public CuDate setA(String a) {
//        this.a = a;
//        return this;
//    }
//
//    public CuDate setB(String b) {
//        this.b = b;
//        return this;
//    }
//
//    public CuDate setC(String c) {
//        this.c = c;
//        return this;
//    }
//
//    public CuDate setD(String d) {
//        this.d = d;
//        return this;
//    }
//
//    public String getA() {
//        return a;
//    }
//
//    public String getB() {
//        return b;
//    }
//
//    public String getC() {
//        return c;
//    }
//
//    public String getD() {
//        return d;
//    }
}