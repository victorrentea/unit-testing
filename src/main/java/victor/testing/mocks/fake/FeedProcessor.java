package victor.testing.mocks.fake;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class FeedProcessor {
   private final IFileRepo fileRepo;

   public int countPendingLines() {
      Collection<String> names = fileRepo.getFileNames();
      log.debug("Found files: " + names);
      int count = 0;
      for (String fileName : names) {
         try (Stream<String> linesStream = fileRepo.openFile(fileName)) {
            long newCount = linesStream
                .filter(line -> !line.startsWith("#"))
                .count();
            log.debug("Found {} lines in {}", newCount, fileName);
            count+= newCount;
         }
      }
      return count;
   }
}

