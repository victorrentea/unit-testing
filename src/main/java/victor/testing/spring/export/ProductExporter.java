package victor.testing.spring.export;

import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.repo.ProductRepo;

import java.io.IOException;
import java.io.Writer;

import static java.time.format.DateTimeFormatter.ofPattern;

public class ProductExporter {
  private final ProductRepo personRepo;

  public ProductExporter(ProductRepo personRepo) {
    this.personRepo = personRepo;
  }

  public void export(Writer writer) throws IOException {
    writer.write("name;barcode;category;created_by;created_at\n");
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
