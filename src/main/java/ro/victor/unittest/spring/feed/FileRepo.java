package ro.victor.unittest.spring.feed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Component
public class FileRepo implements IFileRepo {
   @Value("${feed.in.folder}")
   private File inFolder;

   @Override
   public Set<String> getFileNames() {
      File[] files = inFolder.listFiles();
      if (files == null) {
         return emptySet();
      }
      return Stream.of(files)
          .filter(File::isFile)
          .map(File::getName)
          .collect(toSet());
   }

   @Override
   public InputStream openFile(String fileName) {
      File file = new File(inFolder, fileName);
      if (!file.isFile()) {
         throw new IllegalArgumentException("Not a file name: " + fileName);
      }
      try {
         return new FileInputStream(file);
      } catch (FileNotFoundException e) {
         throw new IllegalArgumentException(e);
      }
   }

}
