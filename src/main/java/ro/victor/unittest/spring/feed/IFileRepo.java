package ro.victor.unittest.spring.feed;

import java.io.InputStream;
import java.util.Set;

public interface IFileRepo {
   Set<String> getFileNames();

   InputStream openFile(String fileName);
}
