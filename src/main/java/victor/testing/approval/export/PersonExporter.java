package victor.testing.approval.export;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
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
         writer.write( person.getPhoneList().get(0)); // TODO fix bug: what if no phone?
//         writer.write(String.join(",", person.getPhoneList())); // TODO CR: output all phones comma-separated
         writer.write(";");
         writer.write(person.getBirthDate().toString()); // TODO CR: change format to "12 Nov 2021"
         writer.write("\n");
      }
   }
}

//@ExtendWith(MockitoExtension.class)
class SaNeJucam  {
   // practic pe sub cand mockuiesti o metoda, Mockito subclaseaza clasa data si apoi te lasa sa ii inveti metodele ce sa faca, aprox ca mai jos:
//      PersonRepo personRepoMock = new PersonRepo() {
//         @Override
//         public List<Person> findAll() {
//            return List.of(person);
//         }
//      }
//   @Mock
//   PersonRepo personRepoMock /*= Mockito.mock(PersonRepo.class)*/;
//
//   @InjectMocks
//   PersonExporter personExporter /*= new PersonExporter(personRepoMock)*/;

   @Test
   void explore() throws IOException {
      // given
      // Plan a (Ionut): inseram in DB date, rulam codul, apoi stergem datele. > test de integration : mai mult de munca
      // Plan b (Victor): sa stabuim metoda #findAll sa intoarca ceea ce noi vrem > Mockuri
      Person person = new Person(1L, "John", "Doe", LocalDate.now(), List.of("0720019564"));
      PersonRepo personRepoMock = Mockito.mock(PersonRepo.class);
      Mockito.when(personRepoMock.findAll()).thenReturn(List.of(person));
      PersonExporter personExporter = new PersonExporter(personRepoMock);
      StringWriter writer = new StringWriter();

      // when (prod call)
      personExporter.export(writer);

      // then (asserts)
      String output = writer.toString();
      assertThat(output).isEqualTo("full_name;phones;birth_date\n" +
                                   "John DOE;0720019564;2022-06-14\n"); // habar n-am ce produce,
      // dar sigur e corect, ca e in prod de 4ani si nimeni nu s-a plans
      // atunci vrei sa "CAPTUREZI" behavior curent al codului ca-n prod, ca sa te asiguri ca TU nu-l strici.
      // >> Characterization Testing :
   }
}