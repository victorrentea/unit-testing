package victor.testing.mocks.purity.domain;

import lombok.Data;

import java.util.List;

@Data
public class Customer {
	private List<Coupon> coupons;
}
