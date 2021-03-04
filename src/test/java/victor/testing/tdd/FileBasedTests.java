package victor.testing.tdd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

@RunWith(Parameterized.class)
public class FileBasedTests {

   private final File inFile;
   private final File outFile;


   public FileBasedTests(File inFile, File outFile) {
      this.inFile = inFile;
      this.outFile = outFile;
   }

   @Parameters
   public static List<File[]> getTestFiles() {
      File rootFolder = new File("C:\\workspace\\unit-testing\\src\\test\\resources\\test-files");


      File[] inFiles = rootFolder.listFiles(new FileFilter() {
         @Override
         public boolean accept(File pathname) {
            return pathname.getName().endsWith("in.json");
         }
      });

      List<File[]> list = new ArrayList<>();

      for (File inFile : inFiles) {
         File outFile = null; // TODO
         list.add(new File[]{inFile, outFile});

      }
      return list;
   }
   @Test
   public void test() {
//       citesti din IN rulezi prod
//          verifici output = out.file
   }
}
