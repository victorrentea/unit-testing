package victor.testing.spring.export;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.repo.ProductRepo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
@RequiredArgsConstructor
public class ProductExporter {
  private final ProductRepo personRepo;

  public String export() throws IOException {
    String fileName = "export-%s.csv".formatted(UUID.randomUUID());
    try (FileWriter fileWriter = new FileWriter(fileName)) {
      writeContent(fileWriter);
    }
    return fileName;
  }

  @VisibleForTesting
  void writeContent(Writer writer) throws IOException {
    writer.write("name;barcode;category2;created_by;created_at\n");
    for (Product product : personRepo.findAll()) {
      writer.write(product.getName().toUpperCase());
      writer.write(";");
      writer.write(product.getBarcode());
      writer.write(";");
      writer.write(toExportCode(product.getCategory()));
      writer.write(";");
      if (product.getCreatedBy() != null) {
        writer.write(product.getCreatedBy());
      } else {
        writer.write("SYSTEM");
      }
      writer.write(";");
      writer.write(product.getCreatedDate().format(ofPattern("dd MMM yyyy")));
      writer.write("\n");
    }
  }

  private String toExportCode(ProductCategory string) {
    return switch (string) {
      case ELECTRONICS -> "E";
      case KIDS -> "K";
      case HOME -> "H";
      default -> "-";
    };
  }

}
