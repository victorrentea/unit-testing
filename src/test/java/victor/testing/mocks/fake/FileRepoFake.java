package victor.testing.mocks.fake;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Primary
@Component
public class FileRepoFake implements IFileRepo{

   private final Map<String, List<String>> data = new HashMap<>();
   @Override
   public Collection<String> getFileNames() {
      return data.keySet();
   }
   @Override
   public Stream<String> openFile(String fileName) {
      return data.get(fileName).stream();
   }

   public void addFile(String fileName, String... lines) {
      data.put(fileName, List.of(lines));
   }
}
