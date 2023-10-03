package victor.testing.design.onion.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.ContractForExport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static io.micrometer.observation.Observation.Event.of;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {
  private static final double WARNING_AMOUNT_THRESHOLD = 10_000;
  private static final double AMOUNT_OVER_WARNING_THRESHOLD = 10_001d;

  @Mock
  ContractExporter excelExporter;
  @InjectMocks
  ContractService contractService;
  @Captor
  ArgumentCaptor<List<ContractForExport>> captor;

@BeforeEach
final void before() {
  contractService.warningThreshold = WARNING_AMOUNT_THRESHOLD;
}
  @Test
  void normalContents() {
    Contract contract = new Contract()
        .setName("name")
        .setNumber("number");

    contractService.exportContracts(List.of(contract));

    verify(excelExporter).exportExcel(captor.capture());
    ContractForExport contractForExport = captor.getValue().get(0);
    assertThat(contractForExport.name()).isEqualTo("name");
    assertThat(contractForExport.number()).isEqualTo("number");
  }

  @Test
  void warning_overduePayments() {
    Contract contract = new Contract()
        .setStatus(Contract.Status.ACTIVE)
        .setLastPayment(LocalDateTime.now().minusDays(80))
        .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);

    contractService.exportContracts(List.of(contract));

    verify(excelExporter).exportExcel(argThat(arg -> arg.get(0).hasWarning()));
  }

  @Test
  void noWarning_paymentsLessThan60DaysAgo() throws IOException {
    Contract contract = new Contract()
        .setStatus(Contract.Status.ACTIVE)
        .setLastPayment(LocalDateTime.now().minusDays(59))
        .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);

    contractService.exportContracts(List.of(contract));

    verify(excelExporter).exportExcel(argThat(arg -> !arg.get(0).hasWarning()));

//    assertThat(getContractRow().getCell(0)
//        .getCellStyle().getFillForegroundColor())
//        .isNotEqualTo(IndexedColors.LIGHT_ORANGE.getIndex());
  }

  @Test
  void noWarning_forDraftContracts() throws IOException {
    Contract contract = new Contract()
        .setStatus(Contract.Status.CLOSED)
        .setLastPayment(LocalDateTime.now().minusDays(80))
        .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);

    contractService.exportContracts(List.of(contract));

    verify(excelExporter).exportExcel(argThat(arg -> !arg.get(0).hasWarning()));

//    assertThat(getContractRow().getCell(0)
//        .getCellStyle().getFillForegroundColor())
//        .isNotEqualTo(IndexedColors.LIGHT_ORANGE.getIndex());
  }
}