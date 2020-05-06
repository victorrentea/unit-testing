package ro.victor.unittest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class Life {

    public Life() {
        System.out.println("NEW ");
    }
    @BeforeClass
    public void inainteDeClasa() {
        System.out.println("clasa");
    }
    @BeforeMethod
    public void inainteDeMethod() {
        System.out.println("method");
    }
    @BeforeGroups("a")
    public void inainteDeMethodA() {
        System.out.println("before group a");
    }
    @Test(groups={"a","b"})
    public void test1() {
        System.out.println("Test1 a");
    }
    @Test(groups={"a","b"})
    public void test2() {
        System.out.println("Test2 a");
    }
    @Test
    public void test3() {
        System.out.println("Test2");
    }
}
