package victor.testing.approval.export;

import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;

public class PersonExporter {
   private final PersonRepo personRepo;

   public PersonExporter(PersonRepo personRepo) {
      this.personRepo = personRepo;
   }

   public void export(Writer writer) throws IOException {
      writer.write("full_name;phones;birth_date\n");
      for (Person person : personRepo.findAll()) {
         writer.write(person.getFirstName() + " " + person.getLastName().toUpperCase());
         writer.write(";");
         writer.write(String.join(" ", person.getPhoneList())); // TODO change delimiter
         writer.write(";");
         writer.write(person.getBirthDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))); // TODO change format to 12 Nov 2021
         writer.write("\n");
      }
   }
}
