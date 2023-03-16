package victor.testing.design.onion.lib.clean.domain;

import java.util.List;

public interface GenerateExcel {
  void generateXls(List<ContractForExport> contracts);
}
