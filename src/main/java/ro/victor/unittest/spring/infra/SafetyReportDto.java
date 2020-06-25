package ro.victor.unittest.spring.infra;

import lombok.Data;

@Data
public class SafetyReportDto {
   private String category;
   private String detailsUrl;
   private boolean safeToSell;
}
