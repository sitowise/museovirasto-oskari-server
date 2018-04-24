package flyway.nba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;

public class V1_46_remove_registry_editor_from_published_template {

	private static final Logger LOG  = LogFactory.getLogger(V1_46_remove_registry_editor_from_published_template.class);

    public void migrate(Connection connection) throws Exception {
    	Long bundleId = getIdForRegisteryEditorBundle(connection);
    	final String sql = "DELETE FROM portti_view_bundle_seq WHERE bundle_id = ? AND view_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bundleId);
            statement.setLong(2, 2);
        } catch(Exception e) {
        	LOG.error("Error removing registry editor from published template.", e.getMessage());
        }
    }

    private Long getIdForRegisteryEditorBundle(Connection conn) throws Exception {
        String sql = "SELECT id FROM portti_bundle WHERE name = 'nba-registry-editor'";
        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getLong("id");
                }	
            }
        }
        throw new Exception("nba-registry-editor bundle not in portti_bundle");
    }
	
}
