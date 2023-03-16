package victor.testing.design.onion.lib.dirty;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.design.onion.lib.Contract;
import victor.testing.design.onion.lib.Contract.Status;

import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirtyTestCoupledToLib {
  public static final int WARNING_AMOUNT_THRESHOLD = 10_000;
  public static final double AMOUNT_OVER_WARNING_THRESHOLD = 10_001d;
  @Spy // antiparttern
  DirtyExport sut;
  @Mock
  Workbook workbookMock; // anti-pattern: mocking a complex lib
  @Mock
  Sheet sheetMock;
  @Mock
  Row rowMock;
  @Mock
  Cell cell0Mock;
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
    when(rowMock.createCell(anyInt())).thenReturn(mock(Cell.class));
    when(rowMock.createCell(0)).thenReturn(cell0Mock);

    sut.warningAmountThreshold = WARNING_AMOUNT_THRESHOLD;
  }

  @Test
  void warningForOverduePayments() throws IOException {

    Contract contract = new Contract()
            .setStatus(Status.ACTIVE)
            .setLastPayment(now().minusDays(80))
            .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);
    sut.exportExcel(List.of(contract));

    verify(cell0Mock).setCellStyle(warningStyle);
  }

  @Test
  void noWarningForPaymentsLessThan60DaysAgo() throws IOException {
    Contract contract = new Contract()
            .setStatus(Status.ACTIVE)
            .setLastPayment(now().minusDays(59))
            .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);
    sut.exportExcel(List.of(contract));

    verify(cell0Mock,never()).setCellStyle(any());
  }

  @Test
  void noWarning_forDraftContracts() throws IOException {
    Contract contract = new Contract()
            .setStatus(Status.CLOSED)
            .setLastPayment(now().minusDays(80))
            .setRemainingValue(AMOUNT_OVER_WARNING_THRESHOLD);
    sut.exportExcel(List.of(contract));

    verify(cell0Mock,never()).setCellStyle(any());
  }
}