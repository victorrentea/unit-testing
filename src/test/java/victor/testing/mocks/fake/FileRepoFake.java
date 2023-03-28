package victor.testing.mocks.fake;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// BENEFIT: simpler tests esp in front of complex interaction protocol
// RISK: more code to maintain and test
public class FileRepoFake implements IFileRepo{
  private Map<String, List<String>> files = new HashMap<>();
  @Override
  public Collection<String> getFileNames() {
    return files.keySet();
  }
  @Override
  public Stream<String> openFile(String fileName) {
    return files.get(fileName).stream();
  }

  public void addTestFile(String fileName, String... linesOfContent) {
    files.put(fileName, List.of(linesOfContent));
  }

  public void reset() {
    files.clear();
  }
}
