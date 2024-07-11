package play;

import com.google.common.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;

public class TurbineServices {
  // signleton paterns
  private static TurbineServices instance = new TurbineServices();

  public static TurbineServices getInstance() {
    return instance;
  }

  public Object getService(String serviceName) {
    if (mockedServices.containsKey(serviceName)) {
      return mockedServices.get(serviceName);
    }
    //existing kung fu
    return null;
  }


  //---- hacks for testing
  @VisibleForTesting
  public void putMockService(String serviceName, Object mockService) {
    mockedServices.put(serviceName, mockService);
  }
  private Map<String, Object> mockedServices = new HashMap<>();
  @VisibleForTesting
  public void clearMockServices() {
    mockedServices.clear();
  }

  // Hint to make this thread safe, store mocked services in a THreadLocal
  // private static ThreadLocal<Map<String, Object>> mockedServices = new ThreadLocal<>();
}
