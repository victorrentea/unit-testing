//package ro.victor.unittest.spring.feed;
//
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//@Primary
//@Component
////@Profile("dummy-file-repo")
//public class FileRepoDummy implements IFileRepo {
//   private final Map<String, String> fileContents = new HashMap<>();
//
//   public void addFileContents(String fileName, String contents) {
//      this.fileContents.put(fileName, contents);
//   }
//
//   @Override
//   public Set<String> getFileNames() {
//      return fileContents.keySet();
//   }
//
//   @Override
//   public InputStream openFile(String fileName) {
//      byte[] bytes = fileContents.get(fileName).getBytes();
//      return new ByteArrayInputStream(bytes);
//   }
//}
