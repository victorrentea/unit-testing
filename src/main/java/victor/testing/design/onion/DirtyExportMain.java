package victor.testing.design.onion;

import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.Contract.Status;
import victor.testing.design.onion.domain.service.ContractsExcelService;

import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.now;

public class DirtyExportMain {
  public static void main(String[] args) throws IOException {
    Contract contract = new Contract()
            .setNumber("CONTRACT_NUMBER")
            .setName("John Doe")
            .setLastPayment(now().minusDays(70))
            .setStatus(Status.ACTIVE)
            .setRemainingValue(14_000d);

    ContractsExcelService sut = new ContractsExcelService();
    sut.warningAmountThreshold = 10_000;
    sut.exportExcel(List.of(contract));
  }
}
