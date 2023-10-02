package victor.testing.design.onion.domain.model;

import lombok.Value;

@Value
public class ContractForExport {
  String contractNumber;
  boolean showWarning;
  String contractName;
}
