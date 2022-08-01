package victor.testing.filebased.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.stream.Collectors;

public class PersonExporter {
   private static final Logger log = LoggerFactory.getLogger(PersonExporter.class);
   private final PersonRepo personRepo;

   public PersonExporter(PersonRepo personRepo) {
      this.personRepo = personRepo;
   }

   // This legacy code is in production for 7 years.
   // So, it has no bugs, 🤞
   //    but your task is to change it... 😱
   // Ofc, you want to make sure you don't introduce bugs.

   // So: (** CHARACTERIZATION TESTS **)
   // - You capture its current output
   // - Find inputs to go "everywhere" (Line coverage helps here)
   // - Save actual current output as tests

   // Then, when refactoring/evolving it, the tests should stay green.
   public void export(Writer writer) throws IOException {
      writer.write("full_name;phones;birth_date\n");
      for (Person person : personRepo.findAll()) {
         writer.write(person.getFirstName() + " " + person.getLastName());
         writer.write(";");
         writer.write( person.getPhoneList()
                 .stream().collect(Collectors.joining(","))
         );
            // TODO CR: output all phones comma-separated
         writer.write(";");
         if (person.getBirthDate() != null) {
            writer.write(person.getBirthDate().toString()); // TODO CR: change format to "12 Nov 2021"
         } else {
            writer.write("N/A");
         }
         writer.write("\n");
      }
   }
}
