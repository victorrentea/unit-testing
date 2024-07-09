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

  // You write CHARACTERIZATION TESTS for the entire existing behavior:
  // 1. Identify and capture the relevant input/output of the existing code: eg: INSERT, return value, mq.send(message)
  // 2. Find proper inputs to go "everywhere" (use line coverage)
  // 3. Save current output as tests along with the inputs=>saveInFile(input,recordedOutput) x 100

  // These tests should stay green when refactoring the code.

  public void export(Writer writer) throws IOException {
    writer.write("full_name;phones;birth_date\n");
    for (Person person : personRepo.findAll()) {
      writer.write(person.getFirstName() + " " + person.getLastName().toUpperCase());
      writer.write(";");
      if (person.getPhoneList().isEmpty()) {
        writer.write("-");
      } else {
        writer.write(person.getPhoneList().get(0));
      }
      // TODO BUGFIX: exception when no phones
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
