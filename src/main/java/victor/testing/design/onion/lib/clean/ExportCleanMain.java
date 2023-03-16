package victor.testing.design.onion.lib.clean;

import victor.testing.design.onion.lib.Contract;
import victor.testing.design.onion.lib.Contract.Status;
import victor.testing.design.onion.lib.clean.domain.CleanExportService;
import victor.testing.design.onion.lib.clean.infra.GenerateExcelAdapter;

import java.util.List;

import static java.time.LocalDateTime.now;

public class ExportCleanMain {
  public static void main(String[] args) {
    Contract contract = new Contract()
            .setNumber("CONTRACT_NUMBER")
            .setName("John Doe")
            .setLastPayment(now().minusDays(70))
            .setStatus(Status.ACTIVE)
            .setRemainingValue(14_000d);

    CleanExportService sut = new CleanExportService(new GenerateExcelAdapter());
    sut.warningAmountThreshold = 10_000;
    sut.exportContracts(List.of(contract));
  }
}
