package flyway.oskari;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Checks if Resource registry data are already added and inserts it if not
 */
public class V1_35_0__Insert_data_for_resource_register implements JdbcMigration {

    public void migrate(Connection connection)
            throws Exception {
        // check existing value before inserting
        final boolean isResourceRegistryAdded = checkResourceRegistry(connection);
        if (!isResourceRegistryAdded) {
            makeInsert(connection);
        }
    }

    private boolean checkResourceRegistry(Connection connection)
            throws Exception {
        final PreparedStatement statement = connection.prepareStatement("SELECT id FROM nba_registries WHERE name = 'resource';");
        try (ResultSet rs = statement.executeQuery()) {
            return rs.next();
        } finally {
            statement.close();
        }
    }

    private void makeInsert(Connection connection)
            throws Exception {

        final PreparedStatement statement = connection.prepareStatement("INSERT INTO nba_registries (id, name, locale) VALUES (8, 'resource', '{\"fi\":{\"name\":\"Resurssi\"},\"en\":{\"name\":\"Resource\"}}');" +		
			"INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight) VALUES (8, 89, 'true', 'true');" +
			"INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight) VALUES (8, 90, 'true', 'true');" +
			"INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight) VALUES (8, 91, 'true', 'true');" +
			"INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight) VALUES (8, 92, 'true', 'true');" +
			"INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight) VALUES (8, 132, 'true', 'true');");
			
        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
