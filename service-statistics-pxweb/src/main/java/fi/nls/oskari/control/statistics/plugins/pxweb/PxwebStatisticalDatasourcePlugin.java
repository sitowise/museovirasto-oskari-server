package fi.nls.oskari.control.statistics.plugins.pxweb;

import fi.nls.oskari.control.statistics.data.*;
import fi.nls.oskari.control.statistics.plugins.APIException;
import fi.nls.oskari.control.statistics.plugins.StatisticalDatasourcePlugin;
import fi.nls.oskari.control.statistics.plugins.db.DatasourceLayer;
import fi.nls.oskari.control.statistics.plugins.db.StatisticalDatasource;
import fi.nls.oskari.control.statistics.plugins.pxweb.parser.PxwebIndicatorsParser;
import fi.nls.oskari.control.statistics.plugins.pxweb.parser.ValueProcessor;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.service.ServiceRuntimeException;
import fi.nls.oskari.util.IOHelper;
import fi.nls.oskari.util.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PxwebStatisticalDatasourcePlugin extends StatisticalDatasourcePlugin {

    private static final Logger LOG = LogFactory.getLogger(PxwebStatisticalDatasourcePlugin.class);
    private PxwebIndicatorsParser indicatorsParser;
    private static final ValueProcessor DEFAULT_PROCESSOR = new ValueProcessor();
    private Map<String, ValueProcessor> processors = new HashMap<>();

    private PxwebConfig config;

    @Override
    public void update() {
        List<StatisticalIndicator> indicators = indicatorsParser.parse(getSource().getLayers());
        for (StatisticalIndicator ind : indicators) {
            onIndicatorProcessed(ind);
        }
    }

    @Override
    public void init(StatisticalDatasource source) {
        super.init(source);
        config = new PxwebConfig(source.getConfigJSON(), source.getId());
        indicatorsParser = new PxwebIndicatorsParser(config);
    }

    /*
    "query": [
   {
     "code": "Alue",
     "selection": {
       "filter": "item",
       "values": [
         "0910000000",
         "0911000000",
         "0911101000"
       ]
     }
   },
   {
     "code": "Käyttötarkoitus",
     "selection": {
       "filter": "item",
       "values": [
         "all",
         "01",
         "02"
       ]
     }
   },
   {
     "code": "Toimenpide",
     "selection": {
       "filter": "item",
       "values": [
         "all",
         "1"
       ]
     }
   },
   {
     "code": "Yksikkö",
     "selection": {
       "filter": "item",
       "values": [
         "1",
         "2"
       ]
     }
   },
   {
     "code": "Vuosi",
     "selection": {
       "filter": "item",
       "values": [
         "0",
         "1",
         "2"
       ]
     }
   }
 ],
 "response": {
   "format": "csv"
 }
}
     */
    @Override
    public Map<String, IndicatorValue> getIndicatorValues(StatisticalIndicator indicator,
                                                          StatisticalIndicatorDataModel params,
                                                          StatisticalIndicatorLayer regionset) {
        Map<String, IndicatorValue> values = new HashMap<>();
        String indicatorId = indicator.getId();
        if(config.hasIndicatorKey()) {
            // indicatorId will be something.px::[value of indicatorKey]
            int separatorIndex = indicatorId.lastIndexOf("::");
            if(separatorIndex == -1) {
                throw new ServiceRuntimeException("Unidentified indicator id: " + indicatorId);
            }
            String indicatorSelector = indicatorId.substring(separatorIndex + 2);
            indicatorId = indicatorId.substring(0, separatorIndex);
            params.addDimension(new StatisticalIndicatorDataDimension(config.getIndicatorKey(), indicatorSelector));
        }
        String url = createUrl(regionset.getParam("baseUrl"), indicatorId);
        JSONArray query = new JSONArray();
        JSONObject payload = JSONHelper.createJSONObject("query", query);
        final String regionKey = config.getRegionKey();
        for (StatisticalIndicatorDataDimension selector : params.getDimensions()) {
            if (regionKey.equalsIgnoreCase(selector.getId())) {
                // skip the region property
                continue;
            }
            JSONObject param = new JSONObject();
            JSONHelper.putValue(param, "code", selector.getId());
            JSONObject selection = new JSONObject();
            JSONHelper.putValue(selection, "filter", "item");
            JSONArray paramValues = new JSONArray();
            paramValues.put(selector.getValue());
            JSONHelper.putValue(selection, "values", paramValues);

            JSONHelper.putValue(param, "selection", selection);
            query.put(param);
        }
        JSONHelper.putValue(payload, "response", JSONHelper.createJSONObject("format", "json-stat"));

        DatasourceLayer layer = getSource().getLayers()
                .stream()
                .filter(l -> l.getMaplayerId() == regionset.getOskariLayerId())
                .findFirst()
                .orElseThrow(() -> new ServiceRuntimeException("Invalid regionset: " + regionset.getOskariLayerId()));
        ValueProcessor processor = getValueProcessor(layer);
        try {
            final HttpURLConnection con = IOHelper.getConnection(url);
            IOHelper.writeHeader(con, IOHelper.HEADER_CONTENTTYPE, IOHelper.CONTENT_TYPE_JSON + ";  charset=utf-8");
            IOHelper.writeToConnection(con, payload.toString().getBytes(IOHelper.CHARSET_UTF8));
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new APIException("Couldn't connect to API at " + url);
            }
            final String data = IOHelper.readString(con);
            JSONObject json = JSONHelper.createJSONObject(data);
            if(json == null) {
                LOG.debug("Got non-json response:", data);
                throw new APIException("Response wasn't JSON");
            }
            //dataset.dimension.Alue.category.index -> key==region id & value == index pointer to dataset.value
            JSONObject stats = json.optJSONObject("dataset").optJSONObject("dimension").optJSONObject(regionKey).optJSONObject("category").optJSONObject("index");
            JSONArray responseValues = json.optJSONObject("dataset").optJSONArray("value");
            JSONArray names = stats.names();
            for (int i = 0; i < names.length(); ++i) {
                String region = names.optString(i);
                processor.getRegionValue(responseValues, region, stats.optInt(region), layer)
                    .ifPresent(v -> values.put(v.getRegion(), v.getValue()));
            }
        } catch (IOException e) {
            throw new APIException("Couldn't get data from service/parsing failed", e);
        }

        return values;
    }

    private String createUrl(String baseUrl, String pathId) {
        if(!baseUrl.endsWith(".px")) {
            return IOHelper.fixPath(baseUrl + "/" + IOHelper.urlEncode(pathId));
        }
        return baseUrl;
    }

    private ValueProcessor getValueProcessor(DatasourceLayer layer) {
        String className = layer.getConfig("valueProcessor");
        if(className == null) {
            return DEFAULT_PROCESSOR;
        }
        ValueProcessor processor = processors.get(className);
        if(processor != null) {
            return processor;
        }
        try {
            final Class clazz = Class.forName(className);
            if(!ValueProcessor.class.isAssignableFrom(clazz)) {
                LOG.warn("Invalid ValueProcessor configured for datasource id:", config.getId(), " - class:", className);
                return DEFAULT_PROCESSOR;
            }
            processor = (ValueProcessor) clazz.newInstance();
            processors.put(className, processor);
            return processor;
        } catch (Exception e) {
            LOG.error(e, "Error creating ValueProcessor for plugin id:", config.getId(), " - class:", className);
        }
        return DEFAULT_PROCESSOR;
    }
}
