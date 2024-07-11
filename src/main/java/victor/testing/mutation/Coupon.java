package victor.testing.mutation;

import lombok.EqualsAndHashCode;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

import java.util.Set;

@EqualsAndHashCode
public class Coupon {
  private final ProductCategory category;
  private final int discountAmount;
  private final Set<Long> applicableSuppliers;
  private boolean autoApply = true;

  public Coupon(ProductCategory category, int discountAmount, Set<Long> applicableSuppliers) {
    this.category = category;
    this.discountAmount = discountAmount;
    this.applicableSuppliers = applicableSuppliers;
  }

  public Coupon setAutoApply(boolean autoApply) {
    this.autoApply = autoApply;
    return this;
  }

  public boolean autoApply() {
    return autoApply;
  }

  public boolean isApplicableFor(Product product, Double price) {
    return product.getCategory() == category
           && price > 2.5 * discountAmount
           && applicableSuppliers.contains(product.getSupplier().getId());
  }

  public Double apply(Product product, Double price) {
    if (!isApplicableFor(product, price)) {
      throw new IllegalArgumentException();
    }
    return price - discountAmount;
  }

  @Override
  public String toString() {
    return "Coupon{" +
           "category=" + category +
           ", discountAmount=" + discountAmount +
           ", applicableSuppliers=" + applicableSuppliers +
           ", autoApply=" + autoApply +
           '}';
  }
}
