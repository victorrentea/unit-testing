package victor.testing.approval.export;

import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class PersonExporter {
   private static final DateTimeFormatter BIRTH_DATE_FORMAT = ofPattern("dd MMM yyyy");
   private final PersonRepo personRepo;

   public PersonExporter(PersonRepo personRepo) {
      this.personRepo = personRepo;
   }

   public void export(Writer writer) throws IOException {
      writer.write("full_name;phones;birth_date\n");
      for (Person person : personRepo.findAll()) {
         writer.write(person.getFirstName() + " " + person.getLastName().toUpperCase());
         writer.write(";");
         writer.write(String.join(",", person.getPhoneList()));
         writer.write(";");
         writer.write(person.getBirthDate().format(BIRTH_DATE_FORMAT));
         writer.write("\n");
      }
   }
}
