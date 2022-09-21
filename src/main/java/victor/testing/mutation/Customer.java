package victor.testing.mutation;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Value
@Builder
public class Customer {
    Long id;
    String name;
    String email;
    ImmutableList<String> labels;
    Address address;
    Date createDate;
    ImmutableList<Coupon> coupons;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public Address getAddress() {
        return this.address;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public List<Coupon> getCoupons() {
        return this.coupons;
    }


    public String toString() {
        return "Customer(id=" + this.getId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", labels=" + this.getLabels() + ", address=" + this.getAddress() + ", createDate=" + this.getCreateDate() + ", coupons=" + this.getCoupons() + ")";
    }
}
