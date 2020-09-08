package ro.victor.unittest.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.mocks.telemetry.Phone.Type;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
   @Mock
   private TelemetryClient client;
   @Mock
   private UUIDGenerator uuidGenerator;
   private ConfigFactory configFactory;
   private TelemetryDiagnosticControls controls;

   @Before
   public void initialize() {
      configFactory = new ConfigFactory(uuidGenerator);
      controls = new TelemetryDiagnosticControls(client, configFactory);
      when(client.getOnlineStatus()).thenReturn(true);
      when(client.getVersion()).thenReturn("UNUSED");
      when(uuidGenerator.generateRandom()).thenReturn("UUID");
   }

   @Test
   public void disconnects() throws Exception {
      controls.checkTransmission();
      verify(client).disconnect();
   }

   @Test(expected = IllegalStateException.class)
//    @DisplayName("Expect ceva")
   public void throwsWhenNotOnline() throws Exception {
      when(client.getOnlineStatus()).thenReturn(false); // !!!!!!!
      controls.checkTransmission();
//        int actual = 1;
//        assertEquals(/*"Expect ceva", */1, actual);
   }

   @Test
   public void sendsDiagnosticInfo() throws Exception {
      controls.checkTransmission();
      verify(client).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
//      verifyNoMoreInteractions(client);
   }

   @Test
   public void receivesDiagnosticInfo() throws Exception {
//        Random r = new Random(1);
//        System.out.println(r.nextInt());
//        System.out.println(r.nextInt());
      when(client.receive()).thenReturn("received value");
      controls.checkTransmission();

      verify(client, times(1)).receive();  //
      // veriy pe query doar daca vrei sa te
      // asiguri ca nu face repetat o operatie SCUMPA (100ms)

      assertThat(controls.getDiagnosticInfo()).isEqualTo("received value");
   }

   @Test
   public void configuresWithAckNormal() {
      when(client.getVersion()).thenReturn("VER");
      controls.checkTransmission();
      // TODO subred
//      verify(client).configure(captor);
//      captor.getValue()`
   }



   {

      for (Field declaredField : Customer.class.getDeclaredFields()) {
         // in fct de tipul fieldului completezi cu date random

      }
//      mapezi Java 1-> PB
//      mapezi PB->Java2
      for (Field declaredField : Customer.class.getDeclaredFields()) {
         // in fct de tipul fieldului completezi cu date random

      }
      // O metoda cu un enum
      // pt fiecare valoare se face ceva/

   }


   // prod

   @Test
   public void test() {
      Phone phone1 = aPhone().setType(Type.WORK);
      Phone phone2 = aPhone().setType(Type.HOME);


      Customer customer = aCustomer()
          .setPhone(aPhone().setType(Type.WORK));
   }

   public static Customer aCustomer() {
      return new Customer()
//          .set
//          .set
//          .set
//          .set
          .setPhone(aPhone());
   }
   // poate chiar dintr-o alta clasa TestData
   public static Phone aPhone() {
      return new Phone("value", Type.HOME);
   }
}

class Customer {
   private Phone phone;

   public Phone getPhone() {
      return phone;
   }

   public Customer setPhone(Phone phone) {
      this.phone = phone;
      return this;
   }
}

class Phone {
   enum Type {
      WORK, HOME
   }
   private String value;
   private Type type;
   public Phone() {}

   public Phone(String value, Type type) {
      this.value = value;
      this.type = type;
   }

   public Phone setValue(String value) {
      this.value = value;
      return this;
   }

   public Phone setType(Type type) {
      this.type = type;
      return this;
   }
}
