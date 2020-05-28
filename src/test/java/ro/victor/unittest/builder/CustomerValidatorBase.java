package ro.victor.unittest.builder;

import org.junit.Before;
import org.junit.runner.RunWith;

abstract public class CustomerValidatorBase {
  protected CustomerValidator validator = new CustomerValidator();
  @Before
  public final void initialize() {
    System.out.println("Din super persist chestii ,mockuiesc chestii");
  }
}
