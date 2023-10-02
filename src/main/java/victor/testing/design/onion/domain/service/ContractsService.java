package victor.testing.design.onion.domain.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.Contract.Status;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.now;

public class ContractsService {
  @Value("${warning.amount.threshold}") // from application.properties
  public double warningAmountThreshold;

  public Workbook createWorkbook() {
    return new XSSFWorkbook();
  }

  public void exportExcel(List<Contract> contracts) throws IOException {
    Workbook workbook = createWorkbook();

    Sheet sheet = workbook.createSheet("Contracts");
    sheet.setColumnWidth(0, 6000);
    sheet.setColumnWidth(1, 4000);

    createHeader(workbook, sheet);

    CellStyle warningStyle = workbook.createCellStyle();
    warningStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
    warningStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // TODO omit this

    for (int i = 0; i < contracts.size(); i++) {
      Contract contract = contracts.get(i);
      Row row = sheet.createRow(1 + i);
      Cell cell = row.createCell(0);
      cell.setCellValue(contract.getNumber());
      if (contract.getStatus() == Status.ACTIVE
          && contract.getLastPayment().isBefore(now().minusDays(60))
          && contract.getRemainingValue() > warningAmountThreshold) {

        cell.setCellStyle(warningStyle);
      }

      row.createCell(1).setCellValue(contract.getName());
    }

    workbook.write(new FileOutputStream("output.xlsx"));
    workbook.close();
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
