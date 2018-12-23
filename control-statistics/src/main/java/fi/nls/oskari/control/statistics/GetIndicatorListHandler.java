package fi.nls.oskari.control.statistics;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.ActionParamsException;
import fi.nls.oskari.control.statistics.data.IndicatorSet;
import fi.nls.oskari.control.statistics.data.StatisticalIndicatorLayer;
import fi.nls.oskari.control.statistics.plugins.StatisticalDatasourcePlugin;
import fi.nls.oskari.control.statistics.plugins.StatisticalDatasourcePluginManager;
import fi.nls.oskari.control.statistics.data.StatisticalIndicator;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.util.JSONHelper;
import fi.nls.oskari.util.ResponseHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static fi.nls.oskari.control.ActionConstants.*;

/**
 * This interface gives the relevant information for all the indicators to the frontend.
 * This information can be subsequently used to query the indicator selector metadata.
 *
 * - action_route=GetIndicatorsMetadata
 *
 * eg.
 * OSKARI_URL?action_route=GetIndicatorsMetadata
 * Response is in JSON, and contains the indicator metadata for each plugin separately.
 */
@OskariActionRoute("GetIndicatorList")
public class GetIndicatorListHandler extends ActionHandler {

    private static final String KEY_COMPLETE = "complete";
    private static final String KEY_INDICATORS = "indicators";
    private static final String KEY_REGIONSETS = "regionsets";
    /**
     * For now, this uses pretty much static global store for the plugins.
     * In the future it might make sense to inject the pluginManager references to different controllers using DI.
     */
    private static final StatisticalDatasourcePluginManager pluginManager = StatisticalDatasourcePluginManager.getInstance();

    @Override
    public void handleAction(ActionParameters ap) throws ActionException {
        final int srcId = ap.getRequiredParamInt(StatisticsHelper.PARAM_DATASOURCE_ID);
        JSONObject response = getIndicatorsListJSON(srcId, ap.getUser(), ap.getLocale().getLanguage());
        ResponseHelper.writeResponse(ap, response);
    }

    /**
     * Requests new data skipping the cache. Used for cache refresh before expiration.
     * @return
     * @throws ActionException
     */
    JSONObject getIndicatorsListJSON(long datasourceId, User user, String language) throws ActionException {
        final StatisticalDatasourcePlugin plugin = pluginManager.getPlugin(datasourceId);
        if(plugin == null) {
            throw new ActionParamsException("No such datasource (id=" + datasourceId + ").");
        }

        JSONObject response = new JSONObject();
        IndicatorSet set = plugin.getIndicatorSet(user);
        JSONHelper.putValue(response, KEY_COMPLETE, set.isComplete());
        List<JSONObject> indicators = set.getIndicators().stream()
                .map(i -> toJSON(i, language))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        JSONHelper.putValue(response, KEY_INDICATORS, new JSONArray(indicators));
        return response;
    }

    private Optional<JSONObject> toJSON(StatisticalIndicator indicator, String language) {
        if(indicator == null || indicator.getLayers() == null || indicator.getLayers().isEmpty()) {
            return Optional.empty();
        }
        try {
            final JSONObject json = new JSONObject();
            JSONHelper.putValue(json, KEY_ID, indicator.getId());
            JSONHelper.putValue(json, KEY_NAME, indicator.getName(language));
            // add layer ids as available regionsets for the indicator
            JSONHelper.putValue(json, KEY_REGIONSETS, new JSONArray(indicator
                    .getLayers()
                    .stream()
                    .map(StatisticalIndicatorLayer::getOskariLayerId)
                    .collect(Collectors.toSet())));
            return Optional.of(json);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
