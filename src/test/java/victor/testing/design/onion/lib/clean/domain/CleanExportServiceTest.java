package victor.testing.design.onion.lib.clean.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.design.onion.domain.service.CleanExportService;
import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.Contract.Status;
import victor.testing.design.onion.domain.model.ContractForExport;
import victor.testing.design.onion.domain.service.GenerateExcel;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CleanExportServiceTest {
  public static final int WARNING_AMOUNT_THRESHOLD = 10_000;
  public static final double AMOUNT_OVER_WARNING_THRESHOLD = 10_001d;

  @Mock
  GenerateExcel generateExcel;
  @Captor
  ArgumentCaptor<List<ContractForExport>> contractsForExportCaptor;
  @InjectMocks
  CleanExportService sut;
  @BeforeEach
  final void before() {
    sut.warningAmountThreshold = WARNING_AMOUNT_THRESHOLD;
  }

  @Test
  void exportContracts() {
    Contract contract = new Contract()
            .setStatus(Status.ACTIVE)
            .setLastPayment(now().minusDays(59))
            .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);
    sut.exportContracts(List.of(contract));

    verify(generateExcel).generateXls(contractsForExportCaptor.capture());
    List<ContractForExport> forExport = contractsForExportCaptor.getValue();

    assertThat(forExport.get(0).isShowWarning()).isFalse();
  }
}