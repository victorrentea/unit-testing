package victor.testing.designhints.functionalcore;

import victor.testing.designhints.functionalcore.ImperativeShell;

public class OtherInProdCallingVisibleForTesting {
    private final ImperativeShell imperativeShell;

    public OtherInProdCallingVisibleForTesting(ImperativeShell imperativeShell) {
        this.imperativeShell = imperativeShell;

    }

    public void method() {
        imperativeShell.theLogic(null, null);
    }
}
