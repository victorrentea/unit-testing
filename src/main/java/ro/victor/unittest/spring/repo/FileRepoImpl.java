package ro.victor.unittest.spring.repo;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class FileRepoImpl implements FileRepo{
    @Override
    public List<String> getFilesInInputFolder() {
        return null;
    }

    @Override
    public InputStream openFileInInputFolder(String fileName) {
        return null;
    }
}
