package victor.testing.mocks.fake;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Primary
@Component
public class FileRepoFake implements IFileRepo {
    Map<String, List<String>> fileContents = new HashMap<>();

    @Override
    public Collection<String> getFileNames() {
        return fileContents.keySet();
    }

    @Override
    public Stream<String> openFile(String fileName) {
        return fileContents.get(fileName).stream();
    }

    public void addFile(String fileName, List<String> contents) {
        fileContents.put(fileName, contents);
    }

    public void clearFiles() {

        fileContents.clear();
    }
}
