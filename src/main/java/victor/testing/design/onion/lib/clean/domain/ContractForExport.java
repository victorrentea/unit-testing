package victor.testing.design.onion.lib.clean.domain;

import lombok.Value;

@Value
public class ContractForExport {
  String contractNumber;
  boolean showWarning;
  String contractName;
}
