package victor.testing.mocks.inheritance;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@Slf4j
public abstract class AbstractFileExport {
   private final EmailSender emailSender;
   private Writer writer;

   protected AbstractFileExport(EmailSender emailSender) {
      this.emailSender = emailSender;
   }

   public void exportOrder() throws IOException {
      File file = new File("target/orders.csv");
      log.info("Starting export into {} ...", file.getAbsolutePath());
      long t0 = System.currentTimeMillis();
      writer = new FileWriter(file);
      try {
         writeContent();
         log.info("Export completed in {} seconds ", (System.currentTimeMillis() - t0) / 1000);
      } catch (Exception e) {
         emailSender.sendErrorEmail("Export orders", e);
         log.debug("Gotcha!", e); // TERROR-Driven Development
         throw e;
      } finally {
         writer.close();
      }
   }

   protected void writeLine(List<Object> cells) {
      try {
         String line = cells.stream().map(Objects::toString).map(this::escapeNewLine).collect(joining(";")) + "\n";
         writer.write(line);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private String escapeNewLine(String s) {
      if (!s.contains("\n")) {
         return s;
      }
      s = s.replace("\"", "\"\"");
      s = "\"" + s + "\"";
      return s;
   }

   protected abstract void writeContent() throws IOException;


}
