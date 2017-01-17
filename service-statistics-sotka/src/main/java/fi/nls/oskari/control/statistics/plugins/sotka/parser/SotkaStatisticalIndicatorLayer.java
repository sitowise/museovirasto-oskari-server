package fi.nls.oskari.control.statistics.plugins.sotka.parser;

import java.util.Map;

import fi.nls.oskari.control.statistics.data.IndicatorValue;
import fi.nls.oskari.control.statistics.data.IndicatorValueType;
import fi.nls.oskari.control.statistics.data.StatisticalIndicatorLayer;
import fi.nls.oskari.control.statistics.data.StatisticalIndicatorDataModel;
import fi.nls.oskari.control.statistics.plugins.sotka.SotkaIndicatorValuesFetcher;

public class SotkaStatisticalIndicatorLayer extends StatisticalIndicatorLayer {
    private String sotkaId;
    private IndicatorValueType valueType;
    private SotkaIndicatorValuesFetcher indicatorValuesFetcher;
    
    public SotkaStatisticalIndicatorLayer(long oskariId, String indicatorId, String sotkaId, IndicatorValueType valueType,
            SotkaIndicatorValuesFetcher indicatorValuesFetcher) {
        super(oskariId, indicatorId);
        this.sotkaId = sotkaId;
        this.valueType = valueType;
        this.indicatorValuesFetcher = indicatorValuesFetcher;
    }
    
    @Override
    public IndicatorValueType getIndicatorValueType() {
        return valueType;
    }

    @Override
    public Map<String, IndicatorValue> getIndicatorValues(StatisticalIndicatorDataModel selectors) {
        return indicatorValuesFetcher.get(selectors, getIndicatorId(), this.sotkaId);
    }

    @Override
    public String toString() {
        return "{id: " + getOskariLayerId() + ", valueType: " + valueType + "}";
    }

}
