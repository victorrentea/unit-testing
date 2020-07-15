package ro.victor.unittest.spring.feed;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FeedProcessor {
   private final FileRepo fileRepo;
   private final Dep dep;

   public FeedProcessor(FileRepo fileRepo, Dep dep) {
      this.fileRepo = fileRepo;
      this.dep = dep;
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
class Dep {
   @Cacheable("x")
   public void m() {

   }
}

