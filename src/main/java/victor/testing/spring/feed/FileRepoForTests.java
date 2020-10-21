package victor.testing.spring.feed;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Profile("test")
@Component
public class FileRepoForTests implements FileRepo{
   private final Map<String, List<String>> fileContents = new HashMap<>();
   @Override
   public Collection<String> getFileNames() {
      return fileContents.keySet();
   }
   @Override
   public Stream<String> openFile(String fileName) {
      return fileContents.get(fileName).stream();
   }
   public void addFile(String fileName, String... lines) {
      fileContents.put(fileName, Arrays.asList(lines));
   }
}
