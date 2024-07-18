package victor.testing.filebased.export;

import java.io.IOException;
import java.io.Writer;

public class PersonExporter {
  private final PersonRepo personRepo;

  public PersonExporter(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  // This legacy code is in production for 7 years.
  // Therefore, it has no bugs. 🤞
  // Your plan to refactor it... 😱
  // How can you make sure you don't break the current behavior?

  // You write CHARACTERIZATION TESTS:
  // 1. You capture the relevant output of the code
  // 2. Find proper inputs to go "everywhere" (use line coverage)
  // 3. Save current output as tests along with the inputs
  // These tests should stay green when refactoring the code.

  public void export(Writer writer) throws IOException {
    writer.write("full_name;phones;birth_date\n");
    for (Person person : personRepo.findAll()) {
      writer.write(person.getLastName().toUpperCase() + " " + person.getFirstName());
      writer.write(";");
      writer.write(person.getPhoneList().get(0));
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
