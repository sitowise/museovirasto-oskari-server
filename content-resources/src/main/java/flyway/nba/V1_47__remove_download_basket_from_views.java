package flyway.nba;

import fi.nls.oskari.util.FlywayHelper;
import fi.nls.oskari.util.PropertyUtil;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Migration is skipped by default!
 *
 * Add "flyway.nba.1_44.skip=false" in oskari-ext.properties to add
 * download-basket for all default and user views.
 *
 */
public class V1_47__remove_download_basket_from_views implements JdbcMigration {

    private static final String BUNDLE_ID = "download-basket";

    public void migrate(Connection connection) throws Exception {
        if (PropertyUtil.getOptional("flyway.nba.1_44.skip", true)) {
            return;
        }

        final List<Long> views = FlywayHelper.getUserAndDefaultViewIds(connection);
        final Long bundleId = getIdForDownloadBasketBundle(connection);
        for (Long viewId : views) {
            if (FlywayHelper.viewContainsBundle(connection, BUNDLE_ID, viewId)) {
                removeBundleFromView(bundleId, viewId, connection);
            }
        }
    }

    private Long getIdForDownloadBasketBundle(Connection conn) throws Exception {
        String sql = "SELECT id FROM portti_bundle WHERE name = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, BUNDLE_ID);
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        throw new Exception("download-basket bundle not in portti_bundle");
    }

    private void removeBundleFromView(Long bundleId, Long viewId, Connection conn) throws SQLException {
        final String sql = "DELETE FROM portti_view_bundle_seq WHERE bundle_id = ? AND view_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, bundleId);
            statement.setLong(2, viewId);
            statement.execute();
        }
    }
}
