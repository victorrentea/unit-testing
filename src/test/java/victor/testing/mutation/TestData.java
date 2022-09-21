package victor.testing.mutation;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// ObjectMother F... https://martinfowler.com/bliki/ObjectMother.html
public class TestData {

    public static Customer.CustomerBuilder john() { // too high coupling!!
        return new Customer.CustomerBuilder()
                .name("::name::")
                .email("::name::")
                .coupons(ImmutableList.of())
                .labels(ImmutableList.of("a"))
                .address(anAddress().build());
    }

    public static Address.AddressBuilder anAddress() {
        return new Address.AddressBuilder()
                .streetName("street")
                .streetNumber(12)
                .city("Bucharest")
                .country(Country.ROU);
    }
}
