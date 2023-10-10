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

  public boolean autoApply() {
    return autoApply;
  }

  public void setAutoApply(boolean autoApply) {
    this.autoApply = autoApply;
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
}
