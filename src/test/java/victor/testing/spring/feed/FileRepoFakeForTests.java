package victor.testing.spring.feed;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Primary
@Component // 1 = singleton
public class FileRepoFakeForTests implements FileRepo {

	private final Map<String, List<String>> fileLines = new HashMap<>();
	
	@Override
	public Collection<String> getFileNames() {
		return fileLines.keySet();
	}

	@Override
	public Stream<String> openFile(String fileName) {
		return fileLines.get(fileName).stream();
	}

	public void addFile(String fileName, String... lines) {
		fileLines.put(fileName, Arrays.asList(lines));
	}
	public void clearFiles() {
		fileLines.clear();
	}
}
