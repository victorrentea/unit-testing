package victor.testing.mocks.fake;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileRepoFake implements IFileRepo {
   private final Map<String, List<String>> fileLines = new HashMap<>();
   @Override
   public Collection<String> getFileNames() {
      return fileLines.keySet();
   }

   @Override
   public Stream<String> openFile(String fileName) {
      return fileLines.get(fileName).stream();
   }

   public void addFile(String fileName, String... line1) {
      fileLines.put(fileName, List.of(line1));
   }
}
