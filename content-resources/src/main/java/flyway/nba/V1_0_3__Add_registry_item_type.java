package flyway.nba;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;

/**
 * Checks if my_places already has a column attributes, adds it if not.
 */
public class V1_0_3__Add_registry_item_type implements JdbcMigration {

    public void migrate(Connection connection) throws SQLException {
        // BundleHelper checks if these bundles are already registered
        final DatabaseMetaData metadata = connection.getMetaData();
        String   catalog           = null;
        String   schemaPattern     = null;
        String   tableNamePattern  = "nba_registry_layers";
        String   columnNamePattern = "item_type";

        ResultSet result = metadata.getColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern);
        if(result.next()) {
            // already present, do nothing
            return;
        }
        addColumn(connection);
    }
    private void addColumn(Connection connection)
            throws SQLException {

        final PreparedStatement statement =
                connection.prepareStatement("ALTER TABLE nba_registry_layers ADD COLUMN item_type TEXT");
        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
