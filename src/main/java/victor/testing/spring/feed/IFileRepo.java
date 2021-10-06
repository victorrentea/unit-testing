package victor.testing.spring.feed;

import java.util.Collection;
import java.util.stream.Stream;

public interface IFileRepo {
   Collection<String> getFileNames();

   Stream<String> openFile(String fileName);

//   List<String> search(String pattern);
}
