package victor.testing.design.onion.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.Contract.Status;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ContractsService {
  @Value("${warning.threshold}") // from application.properties
  public double warningThreshold;

  public void exportExcel(List<Contract> contracts)  {
    try (Workbook workbook = createWorkbook()) {
      Sheet sheet = workbook.createSheet("Contracts");
      sheet.setColumnWidth(0, 6000);
      sheet.setColumnWidth(1, 4000);

      createHeader(workbook, sheet);

      CellStyle warningStyle = workbook.createCellStyle();
      warningStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
      warningStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

      for (int i = 0; i < contracts.size(); i++) {
        Contract contract = contracts.get(i);
        Row row = sheet.createRow(1 + i);
        Cell cell = row.createCell(0);
        cell.setCellValue(contract.getNumber()); // ⭐️
        if (contract.getStatus() == Status.ACTIVE
            && contract.getLastPayment().isBefore(now().minusDays(60))
            && contract.getRemainingValue() > warningThreshold) { // ⭐️

          cell.setCellStyle(warningStyle);
        }

        row.createCell(1).setCellValue(contract.getName()); // ⭐️
      }

      workbook.write(new FileOutputStream("output.xlsx"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // to allow @Spy from unit tests
  public Workbook createWorkbook() {
    return new XSSFWorkbook();
  }

  //<editor-fold desc="Excel Header">
  static void createHeader(Workbook workbook, Sheet sheet) {
    Row header = sheet.createRow(0);

    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    Font font = workbook.createFont();
    font.setBold(true);
    headerStyle.setFont(font);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("ContractNumber");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue("Customer Name");
    headerCell.setCellStyle(headerStyle);
  }
  //</editor-fold>


}
