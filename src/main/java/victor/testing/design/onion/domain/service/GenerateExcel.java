package victor.testing.design.onion.domain.service;

import victor.testing.design.onion.domain.model.ContractForExport;

import java.util.List;

public interface GenerateExcel {
  void generateXls(List<ContractForExport> contracts);
}
