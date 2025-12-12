package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;

public class V5__UppercaseSupplierCode extends BaseJavaMigration {
  @Override
  public void migrate(Context context) throws Exception {
    String sql = "UPDATE supplier SET code = UPPER(code) WHERE code IS NOT NULL AND code <> UPPER(code)";
    try (PreparedStatement ps = context.getConnection().prepareStatement(sql)) {
      ps.executeUpdate();
    }
  }
}
