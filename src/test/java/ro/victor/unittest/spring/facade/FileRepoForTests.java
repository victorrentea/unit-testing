package ro.victor.unittest.spring.facade;

import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.victor.unittest.spring.repo.FileRepo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@Primary
@Profile("dummyFileRepo")// daca vrei ca doar in anumite teste sa replaceuiesti implementarea reala
public class FileRepoForTests implements FileRepo {
    private Map<String, String> fileContents = new HashMap<>();
    @Override
    public List<String> getFilesInInputFolder() {
        return new ArrayList<>(fileContents.keySet());
    }

    @Override
    public InputStream openFileInInputFolder(String fileName) {
        return new ByteArrayInputStream(fileContents.get(fileName).getBytes());
    }
}
