package victor.testing.spring.feed;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Primary
@Profile("dummyFileRepo")
@Component
//@Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
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
   public void addTestFile(String fileName, String... lines) {
      fileContents.put(fileName, Arrays.asList(lines));
   }
   public void clearFiles() {
      fileContents.clear();
   }
}