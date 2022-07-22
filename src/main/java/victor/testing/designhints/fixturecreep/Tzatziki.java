package victor.testing.designhints.fixturecreep;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Tzatziki {
    private final Dependency dependency;

    public void tzatziki() {
        if (!dependency.isCucumberAllowed()) {
            throw new IllegalArgumentException();
        }
        // complex logic 7 ifuri
    }
}
