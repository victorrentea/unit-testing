package ro.victor.unittest.spring.feed;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FeedProcessor {
   private final IFileRepo fileRepo;
   private final AltaDep altaDep;

   public FeedProcessor(IFileRepo fileRepo, AltaDep altaDep) {
      this.fileRepo = fileRepo;
      this.altaDep = altaDep;
   }

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
@Service
class AltaDep {
}

