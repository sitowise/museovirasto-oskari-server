package flyway.nba;

import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.map.layer.OskariLayerService;
import fi.nls.oskari.map.layer.OskariLayerServiceIbatisImpl;
import fi.nls.oskari.map.view.ViewService;
import fi.nls.oskari.map.view.ViewServiceIbatisImpl;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class V1_52__add_lidar_layer_to_background_layer_selection_plugin implements JdbcMigration {
    private static final ViewService VIEW_SERVICE = new ViewServiceIbatisImpl();
    private static final OskariLayerService LAYER_SERVICE = new OskariLayerServiceIbatisImpl();
    private static final String PLUGIN_NAME = "Oskari.mapframework.bundle.mapmodule.plugin.BackgroundLayerSelectionPlugin";
    private static final String MAPFULL = "mapfull";
    private static final String LIDAR_LAYER_ID = "235";

    public void migrate(Connection connection) throws Exception {

        List<Long> list = getViewIds(connection);
        for(Long viewId : list) {
            View view = VIEW_SERVICE.getViewWithConf(viewId);
            final Bundle mapfull = view.getBundleByName(MAPFULL);
            boolean updatedPlugin = updatePlugin(mapfull);
            if(updatedPlugin) {
                VIEW_SERVICE.updateBundleSettingsForView(view.getId(), mapfull);
            }
        }
    }

    private List<Long> getViewIds(Connection conn) throws SQLException {

        List<Long> list = new ArrayList<>();
        final String sql = "SELECT distinct view_id FROM portti_view_bundle_seq WHERE bundleinstance = 'mapfull'";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("view_id");
                    list.add(id);
                }
            }
        }
        return list;
    }

    private boolean updatePlugin(final Bundle mapfull) throws JSONException {

        final JSONObject config = mapfull.getConfigJSON();
        final JSONArray plugins = config.optJSONArray("plugins");
        if(plugins == null) {
            throw new RuntimeException("No plugins" + config.toString(2));
        }

        int index = -1;
        for(int i = 0; i < plugins.length(); ++i) {
            JSONObject plugin = plugins.getJSONObject(i);
            if(PLUGIN_NAME.equals(plugin.optString("id"))) {
                index = i;
                break;
            }
        }

        if(index > -1) {
            JSONObject plugin = plugins.getJSONObject(index);
            JSONObject pluginConfig = plugin.getJSONObject("config");
            if (pluginConfig != null){
                JSONArray baseLayers = pluginConfig.getJSONArray("baseLayers");
                baseLayers.put(LIDAR_LAYER_ID);
            }
            return true;
        }

        return false;
    }
}