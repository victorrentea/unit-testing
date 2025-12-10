package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;

public class V6__insert_supplier extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        // Insert a default Supplier row using a fixed ID to be portable across DBs
        try (PreparedStatement ps = context.getConnection().prepareStatement(
                "insert into supplier (id, code, name, active) values (?, ?, ?, ?)")) {
            ps.setLong(1, 100L);
            ps.setString(2, "JAVAMIG");
            ps.setString(3, "Java Migration Supplier");
            ps.setBoolean(4, true);
            ps.executeUpdate();
        }
    }
}
