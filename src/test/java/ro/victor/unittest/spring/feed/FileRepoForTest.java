package ro.victor.unittest.spring.feed;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//@Component
//@Primary
//@Profile("inmemrepo")
public class FileRepoForTest implements IFileRepo {
   private final Map<String, String> data = new HashMap<>();
   @Override
   public Set<String> getFileNames() {
      return data.keySet();
   }

   @Override
   public InputStream openFile(String fileName) {
      return IOUtils.toInputStream(data.get(fileName));
   }

   public void addFile(String fileName, String content) {
      data.put(fileName, content);
   }
   public void reset() {
      data.clear();
   }
}
