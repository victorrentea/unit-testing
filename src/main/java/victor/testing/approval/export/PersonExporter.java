package victor.testing.approval.export;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonExporter {
//   @Autowired
   private final PersonRepo personRepo;

   public PersonExporter(PersonRepo personRepo) {
      this.personRepo = personRepo;
   }

   public void export(Writer writer) throws IOException {
      writer.write("full_name;phones;birth_date\n"); // header
      for (Person person : personRepo.findAll()) {
         writer.write(person.getFirstName() + " " + person.getLastName().toUpperCase());
         writer.write(";");
         if (person.getPhoneList().isEmpty()) {
            writer.write("N/A");
         } else {
            writer.write( person.getPhoneList().get(0)); // TODO fix bug: what if no phone?
         }
//         writer.write(String.join(",", person.getPhoneList())); // TODO CR: output all phones comma-separated
         writer.write(";");
         writer.write(person.getBirthDate().toString()); // TODO CR: change format to "12 Nov 2021"
         writer.write("\n");
      }
   }
}

