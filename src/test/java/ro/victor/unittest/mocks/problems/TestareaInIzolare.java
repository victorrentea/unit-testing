package ro.victor.unittest.mocks.problems;

public class TestareaInIzolare {
}


class A {
    private final B b;

    A(B b) {
        this.b = b;
    }

    public void m(int x) {
        System.out.println(b.m(x)); //A,B
        // 2: daca testam separat A fata de B (mockuind B), cand modificam implem
        // lui B, vom fi nevoiti sa corectam testele pe B, dar
        // vom UITA sa modificam mockurile pe B

        // 3: dopamine hit: vrei repede!! #eroina ta: teste verzi.
        // Nu suporti sa lucrezi ca maniacu (Victor) 3 zile la acelasi test.

        // 4: testele puse post-implem e grele. E mai usor sa pui un mock.
        // "Cu Mock-uri scapi mai usor"
    }
}

class B {
    public String m(Integer i) {
        if (i == null) { // 1: TEST-induced design damage (mai bine zis mockuri prea fine)
            return "";
        }
        if (i % 2  == 1) {
            return "A"; // TODO change "A" to "a"
        } else {
            return "B";
        }
    }
}