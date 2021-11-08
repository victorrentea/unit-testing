package victor.testing.approval.export;

import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class PersonExporter {
   private final PersonRepo personRepo;

   public PersonExporter(PersonRepo personRepo) {
      this.personRepo = personRepo;
   }

   public void export(Writer writer) throws IOException {
      writer.write("full_name;phones;birth_date\n");
      for (Person person : personRepo.findAll()) {
         List<String> phoneList = person.getPhoneList();
         if (phoneList.isEmpty()) phoneList = Collections.singletonList("");
         for (String phone : phoneList) {
            writer.write(person.getFirstName() + " " + person.getLastName().trim().toUpperCase());
            writer.write(";");
            writer.write(phone);
            writer.write(";");
            writer.write(person.getBirthDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))); // TODO change format to 12 Nov 2021
            writer.write("\n");
         }
      }
   }
}
