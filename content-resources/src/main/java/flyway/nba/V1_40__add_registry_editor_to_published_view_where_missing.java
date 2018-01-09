package flyway.nba;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.JSONHelper;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class V1_40__add_registry_editor_to_published_view_where_missing implements JdbcMigration {

    private static final Logger LOG  = LogFactory.getLogger(V1_40__add_registry_editor_to_published_view_where_missing.class);

    class Bundle {
        long view;
        long id;
        JSONObject startup;
        JSONObject config;
    }

    public void migrate(Connection connection) throws Exception {
        // find all published maps without toolbar bundle
        List<Long> viewsNotUpdated = new ArrayList<>();
        List<Long> viewsToUpdate = getPublishedViewsWithoutRegisteryEditorBundle(connection);
        long toolbarId = getIdForRegisteryEditorBundle(connection);
        for(long id : viewsToUpdate) {
            try {
                Bundle bundle = getBundle();
                bundle.id = toolbarId;
                bundle.view = id;
                insertBundle(bundle, connection);
            } catch (Exception ex) {
                LOG.error("Error updating view with id", id, ": ", ex.getMessage());
                viewsNotUpdated.add(id);
            }
        }
        if(viewsToUpdate.size() > 0 && viewsNotUpdated.size() == viewsToUpdate.size()) {
            throw new Exception("Views to update, but didn't update any. Tried " + viewsToUpdate.size());
        }
        if(viewsNotUpdated.size() > 0) {
            LOG.warn("Error updating toolbar to", viewsNotUpdated.size(), "views:", viewsNotUpdated);
        }
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.config = JSONHelper.createJSONObject("{}");
        bundle.startup = JSONHelper.createJSONObject("{\n" +
            "        \"bundlename\" : \"nba-registry-editor\",\n" +
            "        \"metadata\" : {\n" +
            "            \"Import-Bundle\" : {\n" +
            "                \"nba-registry-editor\": {\n" +
            "                    \"bundlePath\": \"/Oskari/packages/nba/bundle/\"\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }");
        return bundle;
    }

    private List<Long> getPublishedViewsWithoutRegisteryEditorBundle(Connection conn) throws SQLException {
        List<Long> list = new ArrayList<>();
        String sql = "select id from portti_view where (type = 'PUBLISHED' OR type = 'PUBLISH') and id NOT IN (select view_id from portti_view_bundle_seq\n" +
                " where view_id in (SELECT id FROM portti_view where (type = 'PUBLISHED' OR type = 'PUBLISH'))\n" +
                " and bundle_id = (select id from portti_bundle where name = 'nba-registry-editor'))";
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
        String sql = "select id from portti_bundle where name = 'nba-registry-editor'";
        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        throw new Exception("nba-registry-editor bundle not in portti_bundle");
    }

    private void insertBundle(Bundle bundle, Connection conn) throws SQLException {
        final String sql = "INSERT INTO portti_view_bundle_seq (view_id, bundle_id, config, startup, seqno) VALUES (?, ?, ?, ?, (SELECT (max(seqno) + 1) FROM portti_view_bundle_seq WHERE view_id = ?))";
        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, bundle.view);
            statement.setLong(2, bundle.id);
            statement.setString(3, bundle.config.toString());
            statement.setString(4, bundle.startup.toString());
            statement.setLong(5, bundle.view);
            statement.execute();
        }
    }

}
