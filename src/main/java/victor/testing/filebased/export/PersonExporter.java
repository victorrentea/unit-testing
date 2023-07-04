package victor.testing.filebased.export;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class PersonExporter {
  private final PersonRepo personRepo;

  public PersonExporter(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  // This legacy code is in production for 7 years.
  // Therefore, it has no bugs. 🤞
  // Your plan to refactor it... 😱 RISK !!
  // How can you make sure you don't break the current behavior?

  // You write CHARACTERIZATION TESTS:
  // 1. You capture the relevant output of the code
  // 2. Find proper inputs to go "everywhere" (use line coverage)
  // 3. Save current output as tests along with the inputs
  // These tests should stay green when refactoring the code.

  public void export(Writer writer) throws IOException {
    writer.write("full_name;phones;birth_date\n");
    for (Person person : personRepo.findAll()) {
      writer.write(person.getFirstName() + " " + person.getLastName().toUpperCase());
      writer.write(";");
      writer.write(person.getPhoneList().get(0));
      // TODO BUGFIX: exception when no phones
      // TODO CR: output all phones comma-separated

      writer.write(";");
      writer.write(extractBirthDate(person));
      writer.write("\n");
    }
  }

  @NotNull
  private static String extractBirthDate(Person person) {
    if (person.getBirthDate() != null) {
      return person.getBirthDate().toString(); // TODO CR: change format to "12 Nov 2021"
    } else {
      return "N/A";
    }
  }
}
