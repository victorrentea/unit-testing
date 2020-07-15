package ro.victor.unittest.spring.feed;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Profile("test")
//@Scope("prototype") NUmerge pentru ca springul cere O SINGURA DATA o instanta din asta cand creeaza acea UNICA instanta de FeedProcessor (care e@Service > singleton deci)
public class FileRepoDummyForTests implements IFileRepo{

   private final Map<String, String> fileContents = new HashMap<>();

   @Override
   public Set<String> getFileNames() {
      return fileContents.keySet();
   }

   @Override
   public InputStream openFile(String fileName) {
      return IOUtils.toInputStream(fileContents.get(fileName));
   }

   public void clearFiles() {
      fileContents.clear();
   }

   public void addDummyFile(String fileName, String contents) {
      fileContents.put(fileName, contents);
   }
}
