package victor.testing.mocks.fake;

import java.util.*;
import java.util.stream.Stream;

// doar pt testare facuta
public class FileRepoFake implements IFileRepo{
    private final Map<String, List<String>> fileContents = new HashMap<>();
    @Override
    public Collection<String> getFileNames() {
        return fileContents.keySet();
    }

    @Override
    public Stream<String> openFile(String fileName) {
        return fileContents.get(fileName).stream();
    }

    public void addFile(String fileName, String... contents) {
        fileContents.put(fileName, Arrays.asList(contents));
    }
}
