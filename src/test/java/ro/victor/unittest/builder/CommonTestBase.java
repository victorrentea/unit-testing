package ro.victor.unittest.builder;

import org.junit.Before;

public abstract class CommonTestBase {
//    protected User currentUser;
    @Before
    public final void deSus() {
        System.out.println("din super");
    }
}
