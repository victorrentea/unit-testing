package victor.testing.spring.hack;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class BeanDestroyerExtension implements AfterEachCallback {

   @Override
   public void afterEach(ExtensionContext context) throws Exception {
      DefaultListableBeanFactory springContext = (DefaultListableBeanFactory) 
          SpringExtension.getApplicationContext(context).getAutowireCapableBeanFactory();
      System.out.println("Found:" + springContext.containsBean("statefulSingleton"));
      System.out.println("Destroying");
      springContext.destroySingleton("statefulSingleton");
      System.out.println("Recreate");
      springContext.getBean("statefulSingleton");
   }

}
