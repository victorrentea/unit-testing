package victor.testing.spring.feed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FeedProcessor {
   @Autowired
   private FileRepo fileRepo;
   @Autowired
   private FeedScanner scanner;

   public int countPendingLines() {
      Collection<String> names = fileRepo.getFileNames();
      log.debug("Found files: " + names);
      int count = 0;
      for (String fileName : names) {
         try (Stream<String> linesStream = fileRepo.openFile(fileName)) {
            List<String> lines = linesStream.collect(toList());
            scanner.removeComments(lines);
            log.debug("Found {} lines in {}", lines.size(), fileName);
            count += lines.size();
         }
      }
      return count;
   }
}

