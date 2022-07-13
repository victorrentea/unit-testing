package victor.testing.mocks.fake;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileRepoFake implements  FileRepo{
//    boolean chmodCalled;
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
        fileContents.put(fileName, List.of(contents));

    }
}
