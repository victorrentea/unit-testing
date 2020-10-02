package ro.victor.unittest.spring.feed;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class FileRepoFakeForTests implements IFileRepo{
   private Map<String, String> fileContents = new HashMap<>();
   @Override
   public Collection<String> getFileNames() {
      return fileContents.keySet();
   }

   @Override
   public Reader openFile(String fileName) {
      return new StringReader(fileContents.get(fileName));
   }

   public void addFakeFile(String fileName, String contents) {
      fileContents.put(fileName, contents);
   }

   public void clearFiles() {
      fileContents.clear();
   }
}
