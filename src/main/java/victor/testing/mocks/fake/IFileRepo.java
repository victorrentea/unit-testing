package victor.testing.mocks.fake;

import java.util.Collection;
import java.util.stream.Stream;

public interface IFileRepo {
   Collection<String> getFileNames();

   Stream<String> openFile(String fileName);
}
