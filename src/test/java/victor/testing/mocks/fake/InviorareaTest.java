package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InviorareaTest {
    @Mock
    private IFileRepo fileRepo;
    @InjectMocks
    private FeedProcessor feedProcessor;

    @Test
    void explore() {
        when(fileRepo.getFileNames())
                .thenReturn(List.of("file1.txt"));
        when(fileRepo.openFile("file1.txt"))
                .thenReturn(Stream.of("line1"));

        int n = feedProcessor.countPendingLines();

        assertThat(n).isEqualTo(1);
    }
    @Test
    void twoFiles() {
        when(fileRepo.getFileNames()).thenReturn(List.of("file1.txt", "file2.txt"));
        when(fileRepo.openFile("file1.txt")).thenReturn(Stream.of("line1"));
        when(fileRepo.openFile("file2.txt")).thenReturn(Stream.of("line2"));

        int n = feedProcessor.countPendingLines();

        assertThat(n).isEqualTo(2);
    }

    @Test
    void ignoresHashLines() {
        when(fileRepo.getFileNames())
                .thenReturn(List.of("file1.txt"));
        when(fileRepo.openFile("file1.txt"))
                .thenReturn(Stream.of("#line2"));

        int n = feedProcessor.countPendingLines();

        assertThat(n).isEqualTo(0);
    }
}
