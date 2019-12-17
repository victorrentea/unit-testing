package ro.victor.unittest;

import java.util.ArrayList;
import java.util.List;

public class Builder {
    public static void main(String[] args) {
//        A.builder().a("a").b("b").numere(asLi)
        new A()
                .setA("a")
                .setB("b")
                .addNumar(1)
                .addNumar(2)
                .setC("c");
    }
}

//@lombok.Builder
class A {

    private String a,b,c,d;
    private List<Integer> numere = new ArrayList<>();

    public String getA() {
        return a;
    }
    public A addNumar(int n) {
        numere.add(n);
        return this;
    }

    public A setA(String a) {
        this.a = a;
        return this;
    }

    public String getB() {
        return b;
    }

    public A setB(String b) {
        this.b = b;
        return this;
    }

    public String getC() {
        return c;
    }

    public A setC(String c) {
        this.c = c;
        return this;
    }

    public String getD() {
        return d;
    }

    public A setD(String d) {
        this.d = d;
        return this;
    }
}