package ro.victor.unittest.spring.repo;

import java.io.InputStream;
import java.util.List;

public interface FileRepo {
    List<String> getFilesInInputFolder();
    InputStream openFileInInputFolder(String fileName);
}
