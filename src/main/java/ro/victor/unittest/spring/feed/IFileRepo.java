package ro.victor.unittest.spring.feed;

import java.io.Reader;
import java.util.Collection;

public interface IFileRepo {
   Collection<String> getFileNames();

   Reader openFile(String fileName);
}
