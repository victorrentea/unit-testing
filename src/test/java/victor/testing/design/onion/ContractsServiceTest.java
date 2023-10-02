package victor.testing.design.onion;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.design.onion.domain.model.Contract;
import victor.testing.design.onion.domain.model.Contract.Status;
import victor.testing.design.onion.domain.service.ContractsService;

import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractsServiceTest {
  public static final int WARNING_AMOUNT_THRESHOLD = 10_000;
  public static final double AMOUNT_OVER_WARNING_THRESHOLD = 10_001d;
  @Spy ContractsService sut;
  @Mock Workbook workbookMock; // anti-pattern: mocking a complex lib
  @Mock Sheet sheetMock;
  @Mock Row rowMock;
  @Mock Cell contractNumberCell;
  @Mock Cell contractNameCell;
  private CellStyle warningStyle;

  @BeforeEach
  final void before() {
    doReturn(workbookMock).when(sut).createWorkbook();
    // anti-pattern: mocks returning mocks
    // anti-pattern: testing business logic via presentation
    when(workbookMock.createSheet(anyString())).thenReturn(sheetMock);
    when(workbookMock.createFont()).thenReturn(mock(Font.class));
    warningStyle = mock(CellStyle.class);
    when(workbookMock.createCellStyle()).thenReturn(warningStyle);
    Row headerRow = mock(Row.class);
    when(sheetMock.createRow(0)).thenReturn(headerRow);
    when(headerRow.createCell(anyInt())).thenReturn(mock(Cell.class));
    when(sheetMock.createRow(1)).thenReturn(rowMock);
    when(rowMock.createCell(1)).thenReturn(contractNameCell);
    when(rowMock.createCell(0)).thenReturn(contractNumberCell);

    sut.warningThreshold = WARNING_AMOUNT_THRESHOLD;
  }

  @Test
  void normalContents() throws IOException {
    Contract contract = new Contract()
        .setName("name")
        .setNumber("number");
    sut.exportExcel(List.of(contract));

    verify(contractNumberCell).setCellValue("number");
    verify(contractNameCell).setCellValue("name");
  }
  @Test
  void warning_overduePayments() throws IOException {
    Contract contract = new Contract()
            .setStatus(Status.ACTIVE)
            .setLastPayment(now().minusDays(80))
            .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);
    sut.exportExcel(List.of(contract));

    verify(contractNumberCell).setCellStyle(warningStyle);
  }

  @Test
  void noWarning_paymentsLessThan60DaysAgo() throws IOException {
    Contract contract = new Contract()
            .setStatus(Status.ACTIVE)
            .setLastPayment(now().minusDays(59))
            .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);
    sut.exportExcel(List.of(contract));

    verify(contractNumberCell,never()).setCellStyle(any());
  }

  @Test
  void noWarning_forDraftContracts() throws IOException {
    Contract contract = new Contract()
            .setStatus(Status.CLOSED)
            .setLastPayment(now().minusDays(80))
            .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);
    sut.exportExcel(List.of(contract));

    verify(contractNumberCell,never()).setCellStyle(any());
  }
}