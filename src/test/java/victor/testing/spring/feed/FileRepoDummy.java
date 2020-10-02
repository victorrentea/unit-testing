package victor.testing.spring.feed;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Primary
@Component
//@Profile("dummy-file-repo")
public class FileRepoDummy implements IFileRepo {
   private final Map<String, String> fileContents = new HashMap<>();

   public void addFileContents(String fileName, String contents) {
      this.fileContents.put(fileName, contents);
   }

   @Override
   public Collection<String> getFileNames() {
      return fileContents.keySet();
   }

   @Override
   public Reader openFile(String fileName) {
      return new StringReader(fileContents.get(fileName));
   }
}
