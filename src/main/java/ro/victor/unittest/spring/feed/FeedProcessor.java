package ro.victor.unittest.spring.feed;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FeedProcessor {
   @Autowired
   private FileRepo fileRepo;

   public int countPendingLines() throws IOException {
      Set<String> names = fileRepo.getFileNames();
      log.debug("Found files: " + names);
      int count = 0;
      for (String fileName : names) {
         try (InputStream is = fileRepo.openFile(fileName)) {
            List<String> lines = IOUtils.readLines(is);
            log.debug("Found {} lines in {}", lines.size(), fileName);
            count += lines.size();
         }
      }
      return count;
   }
}

