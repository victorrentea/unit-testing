package ro.victor.unittest.spring.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedProcessor {
   private final IFileRepo repo;
   private final AltaClasa altaClasa;

   public int countPendingLines() throws IOException {
      Set<String> names = repo.getFileNames();
      log.debug("Found files: " + names);
      int count = altaClasa.m();
      for (String fileName : names) {
         try (InputStream is = repo.openFile(fileName)) {
            List<String> lines = IOUtils.readLines(is);
            log.debug("Found {} lines in {}", lines.size(), fileName);
            count += lines.size();
         }
      }
      return count;
   }
}

@Service
class AltaClasa {
   public int m() {
       return 0;
   }
}

