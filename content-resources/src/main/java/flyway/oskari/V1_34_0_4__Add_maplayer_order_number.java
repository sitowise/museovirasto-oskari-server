package flyway.oskari;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;

/**
 * Checks if my_places already has a column attributes, adds it if not.
 */
public class V1_34_0_4__Add_maplayer_order_number implements JdbcMigration {

    public void migrate(Connection connection) throws SQLException {
        // BundleHelper checks if these bundles are already registered
        final DatabaseMetaData metadata = connection.getMetaData();
        String   catalog           = null;
        String   schemaPattern     = null;
        String   tableNamePattern  = "oskari_maplayer";
        String   columnNamePattern = "order_number";

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
                connection.prepareStatement("ALTER TABLE oskari_maplayer ADD COLUMN order_number INTEGER");
        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
