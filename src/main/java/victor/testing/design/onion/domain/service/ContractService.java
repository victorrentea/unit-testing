package victor.testing.design.onion.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.Contract.Status;
import victor.testing.design.onion.domain.model.ContractForExport;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ContractService {
  private final ContractExporter excelExporter;
  @Value("${warning.threshold}") // from application.properties
  public Double warningThreshold;


  private boolean hasWarning(Contract contract) {
    return contract.getStatus() == Status.ACTIVE
        && contract.getLastPayment().isBefore(now().minusDays(60))
        && contract.getRemainingValue() > warningThreshold;
  }
  public void exportContracts(List<Contract> contracts) {
    List<ContractForExport> contractForExports = contracts.stream()
        .map(contract -> new ContractForExport(contract.getNumber(), contract.getName(), hasWarning(contract)))
        .toList();

    excelExporter.exportExcel(contractForExports);
  }
}

