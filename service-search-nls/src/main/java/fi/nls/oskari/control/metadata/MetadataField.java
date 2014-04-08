package fi.nls.oskari.control.metadata;

import fi.nls.oskari.util.JSONHelper;
import org.json.JSONArray;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a CSW query field:
 * - name is used as key in JSON API
 * - property is as key used for internal operations (defaults to name)
 * - isMulti means multiple values can be selected (comma separated as param and String[] for search criteria)
 * - filter is the property name in CSW query filter (example inspire name can be used as keyword property filter)
 * - filterOp is an internal key for filter operation (defaults to LIKE)
 * - mustMatch means that this is a top level inclusion filter (AND-operation)
 * - dependencies maps a possible field value to another field for combining filter (example type=service can be further filtered with serviceType field ->
 * these need to be wrapped in Logical AND operation)
 * - shownIf is a dumb JSON presentation for frontend form (example serviceType should only be shown if value 'service' is selected in field 'type')
 */
public class MetadataField {
/*
    TYPE("type", true), // type
    SERVICE_TYPE("serviceType", true), // serviceType
    SERVICE_NAME("serviceName", "Title"), // Title
    ORGANIZATION("organization", "orgName"), // orgName
    COVERAGE("coverage", new CoverageHandler()),
    INSPIRE_THEME("inspireTheme", new InspireThemeHandler()),
    KEYWORD("keyword"), // keyword
    TOPIC("topic", "topicCategory"); // topicCategory
*/
    private String name = null;
    private boolean multi = false;
    private String property = null;
    private String filter = null;
    private String filterOp = null;
    private boolean mustMatch = false;
    private Map<String,String> dependencies = Collections.emptyMap();
    private MetadataFieldHandler handler = null;
    private JSONArray shownIf = null;

    public static final String RESULT_KEY_ORGANIZATION = "organization";


    private MetadataField(String name, final String property) {
        this(name, new MetadataFieldHandler());
        this.property = property;
    }

    private MetadataField(String name) {
        this(name, false);
    }

    public MetadataField(String name, boolean isMulti) {
        this(name, isMulti, null);
    }

    private MetadataField(String name, final MetadataFieldHandler handler) {
        this(name, false, handler);
    }
    private MetadataField(String name, boolean isMulti, MetadataFieldHandler handler) {
        this.name = name;
        this.multi = isMulti;

        if(handler == null) {
            handler = new MetadataFieldHandler();
        }
        handler.setMetadataField(this);
        this.handler = handler;
    }

    /**
     * Single or multiple values
     * @return
     */
    public boolean isMulti() {
        return multi;
    }

    /**
     * Field name used by client
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * The property name used when getting options from CSW getDomain
     * @return
     */
    public String getProperty() {
        if(property == null) {
            return name;
        }
        return property;
    }

    /**
     * The name used when constructing CSW query filter
     * @return
     */
    public String getFilter() {
        if(filter == null) {
            return name;
        }
        return filter;
    }
    public void setFilter(String param) {
        filter = param;
    }

    public MetadataFieldHandler getHandler() {
        return handler;
    }

    public JSONArray getShownIf() {
        return shownIf;
    }

    public void setShownIf(String shownIf) {
        if(shownIf != null) {
            // TODO: maybe try catch errors or make JSONHelper create empty array on errors...
            this.shownIf = JSONHelper.createJSONArray(shownIf);
        }
    }

    /**
     * If null -> should default to  like operation
     * @return
     */
    public String getFilterOp() {
        return filterOp;
    }

    public void setFilterOp(String filterOp) {
        this.filterOp = filterOp;
    }

    public boolean isMustMatch() {
        return mustMatch;
    }

    public void setMustMatch(boolean mustMatch) {
        this.mustMatch = mustMatch;
    }

    public void setDependencies(final Map<String,String> map) {
        dependencies = map;
    }

    public Map<String, String> getDependencies() {
        return Collections.unmodifiableMap(dependencies);
    }
}
