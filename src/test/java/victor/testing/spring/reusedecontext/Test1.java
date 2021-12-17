package victor.testing.spring.reusedecontext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.service.ProductService;

public class Test1  extends  MockExternalsSpringTestBase{

   @Autowired
   ProductService productService;

   @Test
   void test() {

   }
}
