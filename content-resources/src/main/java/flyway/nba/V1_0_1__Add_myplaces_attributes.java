package flyway.nba;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;

/**
 * Checks if my_places already has a column attributes, adds it if not.
 */
public class V1_0_1__Add_myplaces_attributes implements JdbcMigration {

    public void migrate(Connection connection) throws SQLException {
        // BundleHelper checks if these bundles are already registered
        final DatabaseMetaData metadata = connection.getMetaData();
        String   catalog           = null;
        String   schemaPattern     = null;
        String   tableNamePattern  = "my_places";
        String   columnNamePattern = "attributes";

        ResultSet result = metadata.getColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern);
        if(result.next()) {
            // already present, do nothing
            return;
        }
        addColumn(connection);
        modifyView(connection);
    }
    private void addColumn(Connection connection)
            throws SQLException {

        final PreparedStatement statement =
                connection.prepareStatement("ALTER TABLE my_places ADD COLUMN attributes TEXT DEFAULT '{}'::TEXT");
        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
    private void modifyView(Connection connection)
            throws SQLException {

        final PreparedStatement statement =
                connection.prepareStatement("CREATE OR REPLACE VIEW my_places_categories AS SELECT mp.id, mp.uuid, mp.category_id, mp.name, mp.attention_text, mp.place_desc, mp.created, mp.updated, mp.geometry, c.category_name, c.\"default\", c.stroke_width, c.stroke_color, c.fill_color, c.dot_color, c.dot_size, c.dot_shape, c.border_width, c.border_color, c.publisher_name, mp.link, mp.image_url, c.fill_pattern, c.stroke_linejoin, c.stroke_linecap, c.stroke_dasharray, c.border_linejoin, c.border_dasharray, mp.attributes FROM my_places mp, categories c WHERE mp.category_id = c.id");
        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
