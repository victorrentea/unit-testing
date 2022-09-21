package victor.testing.filebased.export;

import java.io.IOException;
import java.io.Writer;
import java.util.stream.Collectors;

public class PersonExporter {
   private final PersonRepo personRepo;

   public PersonExporter(PersonRepo personRepo) {
      this.personRepo = personRepo;
   }

   // This legacy code is in production for 7 years. There are NO tests on it!
   // So, it has no bugs, 🤞 (you assume)
   //    but your task is to change it... 😱
   // Ofc, you want to make sure you don't introduce bugs.

   // So: (** CHARACTERIZATION TESTS **)
   // - You capture its current output
   // - Find inputs to go "everywhere" (Line coverage helps here)
   // - Save actual current output as tests

   // Then, when refactoring/evolving it, the tests should stay green.
   public void exportPersons(Writer writer) throws IOException {
      writer.write("full_name;phones;birth_date\n");
      for (Person person : personRepo.findAll()) {
         writer.write(person.getFirstName() + " " + person.getLastName().toUpperCase());
         writer.write(";");
         writer.write( person.getPhoneList().stream().collect(Collectors.joining(","))); // TODO fix bug when no phones
            // TODO CR: output all phones comma-separated
         writer.write(";");
         if (person.getBirthDate() != null) {
            writer.write(person.getBirthDate().toString());
         } else {
            writer.write("N/A");
         }
         writer.write("\n");
      }
   }
}
