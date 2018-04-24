package flyway.nba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;

public class V1_45_remove_registry_editor_from_published_views implements JdbcMigration {

    private static final Logger LOG  = LogFactory.getLogger(V1_45_remove_registry_editor_from_published_views.class);

    public void migrate(Connection connection) throws Exception {
        // find all published maps
        List<Long> viewsNotUpdated = new ArrayList<>();
        List<Long> viewsToUpdate = getPublishedViews(connection);
        long bundleId = getIdForRegisteryEditorBundle(connection);
        for(long id : viewsToUpdate) {
            try {
                removeBundle(bundleId, connection);
            } catch (Exception ex) {
                LOG.error("Error updating view with id", id, ": ", ex.getMessage());
                viewsNotUpdated.add(id);
            }
        }
        if(!viewsToUpdate.isEmpty() && viewsNotUpdated.size() == viewsToUpdate.size()) {
            throw new Exception("Views to update, but didn't update any. Tried " + viewsToUpdate.size());
        }
        if(!viewsNotUpdated.isEmpty()) {
            LOG.warn("Error updating toolbar to", viewsNotUpdated.size(), "views:", viewsNotUpdated);
        }
    }

    private List<Long> getPublishedViews(Connection conn) throws SQLException {
        List<Long> list = new ArrayList<>();
        String sql = "SELECT id FROM portti_view WHERE (type = 'PUBLISHED' OR type = 'PUBLISH')";
        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    list.add(rs.getLong("id"));
                }
            }
        }
        return list;
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

    private void removeBundle(Long bundleId, Connection conn) throws SQLException {
        final String sql = "DELETE FROM portti_view_bundle_seq WHERE bundle_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, bundleId);
        }
    }

}
