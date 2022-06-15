package victor.testing.mocks;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CodCeDoarDelega {
    private final A a;
    private final B b;

    public void deTestat() {
        a.met1();
        b.met2();
    }
}
class A {

    public void met1() {
    }
}

class B {
    public void met2() {
    }
}