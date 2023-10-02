package victor.testing.design.onion.infra;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.Contract.Status;
import victor.testing.design.onion.domain.model.ContractForExport;
import victor.testing.design.onion.domain.service.GenerateExcel;
import victor.testing.design.onion.domain.service.ContractsExcelService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.now;

public class GenerateExcelAdapter implements GenerateExcel {

  @SneakyThrows
  public void generateXls(List<ContractForExport> contracts) {
    Workbook workbook = new XSSFWorkbook();

    Sheet sheet = workbook.createSheet("Contracts");
    sheet.setColumnWidth(0, 6000);
    sheet.setColumnWidth(1, 4000);

    createHeader(workbook, sheet);

    CellStyle warningStyle = workbook.createCellStyle();
    warningStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
    warningStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    for (int i = 0; i < contracts.size(); i++) {
      ContractForExport contract = contracts.get(i);
      Row row = sheet.createRow(1 + i);
      Cell cell = row.createCell(0);
      cell.setCellValue(contract.getContractNumber());
      if (contract.isShowWarning()) {
        cell.setCellStyle(warningStyle);
      }

      row.createCell(1).setCellValue(contract.getContractName());
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

  public static void main(String[] args) throws IOException {
    Contract contract = new Contract()
            .setNumber("CONTRACT_NUMBER")
            .setName("John Doe")
            .setLastPayment(now().minusDays(70))
            .setStatus(Status.ACTIVE)
            .setRemainingValue(14_000d);

    new ContractsExcelService().exportExcel(List.of(contract));
  }
}
