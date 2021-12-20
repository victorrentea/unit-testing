package victor.testing.approval.export;

import java.io.IOException;
import java.io.Writer;

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
         writer.write(String.join(",", person.getPhoneList())); // TODO change delimiter
         writer.write(";");
         if (person.getBirthDate()!=null) {
            writer.write(person.getBirthDate().toString()); // TODO change format to 12 Nov 2021
         } else {
            writer.write("N/A");
         }
         writer.write("\n");
      }
   }
}
