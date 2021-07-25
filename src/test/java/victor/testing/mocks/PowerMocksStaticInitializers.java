package victor.testing.mocks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataManager.class})
@SuppressStaticInitializationFor("victor.testing.mocks.DataManager") // leaves dataStore = null;
public class PowerMocksStaticInitializers {
   @Test
   public void method() throws Exception {
      DataManager.initVariables();
      PowerMockito.mockStatic(DataManager.class);
//      PowerMockito.doNothing().when(DataManager.class,"init"); // is useless because DataManager.class already caused the init() to run

      DataManager manager = new DataManager();
      manager.setData("a","1");

      System.out.println(manager.getData("a"));

   }
}


class DataManager{
   private static Map<String, String> dataStore;

   static {
      initVariables();
      System.out.println("Static init");
   }

   public static void initVariables() {
      dataStore = new HashMap<>();
   }

   public void setData(String key, String value){
      dataStore.put(key, value);
   }

   public String getData(String key){
      return dataStore.get(key);
   }
}
