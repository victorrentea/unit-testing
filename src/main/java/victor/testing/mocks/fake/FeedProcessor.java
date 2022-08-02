package victor.testing.mocks.fake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FeedProcessor {
   private final IFileRepo fileRepo;

   public FeedProcessor(IFileRepo fileRepo) {
      this.fileRepo = fileRepo;
   }

   // numara cate linii mai sunt de procesat in orice fisier dintr-un folder anume
   public int countPendingLines() {
      Collection<String> names = fileRepo.getFileNames();
      log.debug("Found files: " + names);
      int count = 0;
      for (String fileName : names) {
         try (Stream<String> linesStream =
                     fileRepo.openFile(fileName)) {
            List<String> lines = linesStream.collect(toList());
            lines.removeIf(line -> line.startsWith("#"));
            log.debug("Found {} lines in {}", lines.size(), fileName);
            count += lines.size();
         }
      }
      return count;
   }


}



