package fi.nls.oskari.control.view;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.*;
import fi.nls.oskari.control.view.modifier.bundle.BundleHandler;
import fi.nls.oskari.control.view.modifier.param.ParamControl;
import fi.nls.oskari.domain.Role;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.domain.map.view.ViewTypes;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.map.data.service.PublishedMapRestrictionService;
import fi.nls.oskari.map.data.service.PublishedMapRestrictionServiceImpl;
import fi.nls.oskari.map.view.*;
import fi.nls.oskari.map.view.util.ViewHelper;
import fi.nls.oskari.util.*;
import fi.nls.oskari.view.modifier.ModifierException;
import fi.nls.oskari.view.modifier.ModifierParams;
import fi.nls.oskari.view.modifier.ViewModifier;
import fi.nls.oskari.view.modifier.ViewModifierManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.*;

@OskariActionRoute("GetAppSetup")
public class GetAppSetupHandler extends ActionHandler {

    private ViewService viewService = null;
    private BundleService bundleService = null;
    private PublishedMapRestrictionService restrictionService = null;

    private static final Logger log = LogFactory.getLogger(GetAppSetupHandler.class);

    public static final String PROPERTY_AJAXURL = "oskari.ajax.url.prefix";

    public static final String PARAM_OLD_ID = "oldId";
    public static final String PARAM_NO_SAVED_STATE = "noSavedState";
    public static final String VIEW_DATA = "viewData";
    public static final String STATE = "state";

    private static final String KEY_ENV = "env";
    private static final String KEY_STARTUP = "startupSequence";
    private static final String KEY_CONFIGURATION = "configuration";

    public static final String COOKIE_SAVED_STATE = "oskaristate";

    // for adding extra bundle(s) for users with specific roles
    private Map<String, List<Bundle>> bundlesForRole = new HashMap<String, List<Bundle>>();

    // params need to be in a list that keeps order since there is a priority order
    private final List<String> paramHandlers = new ArrayList<>();
    private final Map<String, BundleHandler> bundleHandlers = new HashMap<String, BundleHandler>();

    public void setViewService(final ViewService service) {
        viewService = service;
    }
    public void setBundleService(final BundleService service) {
        bundleService = service;
    }
    public void setPublishedMapRestrictionService(final PublishedMapRestrictionService service) {
        restrictionService = service;
    }

    public void init() {
        // setup services if they haven't been initialized
        if(viewService == null) {
            setViewService(new ViewServiceIbatisImpl());
        }
        if(bundleService == null) {
            setBundleService(new BundleServiceIbatisImpl());
        }
        if(restrictionService == null) {
            setPublishedMapRestrictionService(new PublishedMapRestrictionServiceImpl());
        }
        // Returns names of @OskariViewModifier annotated classes of type ParamHandler from classpath
        paramHandlers.addAll(ParamControl.getHandlerKeys());

        // Loads @OskariViewModifier annotated classes of type BundleHandler from classpath
        final Map<String, BundleHandler> handlers = ViewModifierManager.getModifiersOfType(BundleHandler.class);
        for(Map.Entry<String, BundleHandler> entry : handlers.entrySet()) {
            bundleHandlers.put(entry.getKey(), entry.getValue());
        }


       //Get dynamic bundles from properties
       final String[] dynamicBundles = PropertyUtil.getCommaSeparatedList("actionhandler.GetAppSetup.dynamic.bundles");

        // Get roles for each dynamic bundle and retrieve bundles from db. Store bundles in <role,bundle> map.
        Map <String, Bundle> requestedBundles = new HashMap<String, Bundle>();
        for(String bundleId : dynamicBundles) {
            final String[] rolesForBundle = PropertyUtil.getCommaSeparatedList("actionhandler.GetAppSetup.dynamic.bundle."
                                            + bundleId + ".roles");

            for(String roleName : rolesForBundle) {
                if(!bundlesForRole.containsKey(roleName)) {
                    bundlesForRole.put(roleName, new ArrayList<Bundle>());
                }

                List<Bundle> list = bundlesForRole.get(roleName);
                if( requestedBundles.containsKey(bundleId)) {
                     list.add(requestedBundles.get(bundleId)) ;
                } else {
                    Bundle bundle = bundleService.getBundleTemplateByName(bundleId);
                    if (bundle != null) {
                        requestedBundles.put(bundleId, bundle);
                        list.add(bundle);
                    } else {
                        log.error("Could not retrieve configured bundle by name " + bundleId);
                    }
                }
            }
        }
        
        // setup default views for appsetup.env
        try {
            List<View> views = new ArrayList<>();
            for (long viewId : viewService.getSystemDefaultViewIds()) {
                views.add(viewService.getViewWithConf(viewId));
            }
            EnvHelper.setupViews(views);
        } catch (Exception ex) {
            log.warn(ex, "Couldn't setup default views for appsetup! Default views are not working properly in appsetup.env");
        }
    }

    public void handleAction(final ActionParameters params) throws ActionException {
        // oldId => support for migrated published maps
        final long oldId = params.getHttpParam(PARAM_OLD_ID, -1);
        final User user = params.getUser();
        final long defaultViewId = viewService.getDefaultViewId(user);
        final View view = getView(params, defaultViewId, oldId);

        if (view == null) {
            throw new ActionParamsException("Could not load View");
        }
        // Strictly necessary only if oldId used
        final long viewId = view.getId();
        final String referer = RequestHelper.getDomainFromReferer(params
                .getHttpHeader(IOHelper.HEADER_REFERER));


        // ignore saved state when loading:
        //   - views that are not system default views
        //   - when explicitly told with parameter
        //   - when the cookie's srs is different from the view's
        boolean ignoreSavedState = !viewService.isSystemDefaultView(viewId)
                || params.getHttpParam(PARAM_NO_SAVED_STATE, false)
                || !srsNamesMatch(params, view);
        // restore state from cookie if not
        if (!ignoreSavedState) {
            log.debug("Modifying map view if saved state is available");
            modifyView(view, getStateFromCookie(params
                    .getCookie(COOKIE_SAVED_STATE)));
        }

        // Check user/permission
        final long creator = view.getCreator();
        final long userId = params.getUser().getId();
        if (view.isPublic()) {
            log.info("View ID:", viewId, "created by user", creator,
                    "is public, access granted for user with id", userId);
        } else if (creator == userId) {
            log.info("Creator", creator, "granted access to view with ID:",
                    viewId);
        } else {
            throw new ActionDeniedException("Denied access to view with ID: "
                    + viewId + " for user with id " + userId
                    + " - View created by user " + creator);
        }

        if (view.getType().equals(ViewTypes.PUBLISHED)) {
            // Check referrer
            final String pubDomain = view.getPubDomain();
            if(ViewHelper.isRefererDomain(referer, pubDomain)) {
                log.info("Granted access to published view in domain:",
                        pubDomain, "for referer", referer);
            } else {
                log.error("Referer: ", params.getHttpHeader("Referer"), " -> ",
                        referer);
                throw new ActionDeniedException(
                        "Denied access to published view in domain: "
                                + pubDomain + " for referer " + referer);
            }

            // Check View lock
            if (restrictionService.isPublishedMapLocked((int) viewId)) {
                throw new ActionDeniedException("View with id" + viewId
                        + "is locked!");
            }

            // Check usage count -
            // // FIXME: we cannot use the current user -> the publisher is the
            // one we are interested in!!!!
            /*
private String UNRESTRICTED_USAGE_ROLE = "";
UNRESTRICTED_USAGE_ROLE = PropertyUtil.get("view.published.usage.unrestrictedRoles");
            if (!params.getUser().hasRole(UNRESTRICTED_USAGE_ROLE)) {
                final List<Integer> viewIdList = new ArrayList<Integer>();
                // get all view for view creator
                final List<View> viewList = viewService.getViewsForUser(view
                        .getCreator());
                for (View v : viewList) {
                    viewIdList.add((int) v.getId());
                }
                if (restrictionService.isServiceCountExceeded(viewIdList)) {
                    throw new ActionDeniedException(
                            "Denied access to published view" + viewId
                                    + " - service count for user" + userId
                                    + "exceeded!");
                }
            }
            */
        }

        // Update view for latest usage timestamp and opened count number
        updateUsageData(view);

        // JSON presentation of view
        final JSONObject configuration = getConfiguration(view);
        final JSONArray startupSequence = getStartupSequence(view);

        // modify the loaded view before serving it if there are any control
        // parameters
        final ModifierParams modifierParams = new ModifierParams();
        modifierParams.setBaseAjaxUrl(EnvHelper.getAPIurl(params));
        modifierParams.setConfig(configuration);
        modifierParams.setActionParams(params);

        modifierParams.setReferer(referer);
        modifierParams.setView(view);
        modifierParams.setStartupSequence(startupSequence);
        modifierParams.setOldPublishedMap(oldId != -1);
        modifierParams.setModifyURLs(EnvHelper.isSecure(params));
        modifierParams.setAjaxRouteParamName(ActionControl.PARAM_ROUTE);

        // Add admin-layerselector/layer-rights bundle, if admin role and default view
        // TODO: check if we can assume ViewTypes.DEFAULT || ViewTypes.USER for this.
        //add bundles according to role/rights
        if (ViewTypes.DEFAULT.equals(view.getType()) ||
                ViewTypes.USER.equals(view.getType())) {
            log.debug("Adding bundles for user", params.getUser());

            for(Role r : params.getUser().getRoles()) {
                List<Bundle> bundles = bundlesForRole.get(r.getName());
                if(bundles != null) {
                    for(Bundle b : bundles) {
                        addBundle(modifierParams, b.getName(), b);
                    }
                }
            }
        }

        int locationModified = 0;
        for (String paramKey : paramHandlers) {
            final String value = params.getHttpParam(paramKey);
            log.debug("Handling parameter", paramKey);
            modifierParams.setParamValue(value);
            try {
                if (value != null
                        && ParamControl.handleParam(paramKey, modifierParams)) {
                    locationModified++;
                    log.debug("Parameter", paramKey, "with value", value,
                            "modified map location");
                }
            } catch (ModifierException ex) {
                log.warn(ex, "Parameter couldn't be handled:", paramKey, "=",
                        value);
            }
            modifierParams.setLocationModified(locationModified > 0);
        }

        // rewrite bundle configurations f.ex. mapfull only lists layer ids ->
        // replace with full layer JSONs etc
        // mapfull modifier needs to know if location has been modified ->
        // disables geolocation bundle
        modifierParams.setLocationModified(locationModified > 0);
        // TODO: if we have modified location more than once, user gave
        // conflicting params, maybe notify about it?
        for (int i = 0; i < startupSequence.length(); i++) {
            final JSONObject bundle = (JSONObject) startupSequence.opt(i);
            final String bundleid = bundle.optString("bundlename");
            if (bundleHandlers.containsKey(bundleid)) {
                log.debug("Modifying bundle", bundleid);
                try {
                    bundleHandlers.get(bundleid).modifyBundle(modifierParams);
                } catch (ModifierException e) {
                    log.error(e, "Unable to modify bundle:", bundle);
                }
            }
        }

        // write response
        try {
            JSONObject appSetup = new JSONObject();
            appSetup.put(KEY_ENV, EnvHelper.getEnvironmentJSON(params, view));
            appSetup.put(KEY_STARTUP, startupSequence);
            appSetup.put(KEY_CONFIGURATION, configuration);
            ResponseHelper.writeResponse(params, appSetup);
        } catch (JSONException jsonex) {
            throw new ActionException("Malformed startup sequence/config!", jsonex);
        }
    }

    /**
     * Check whether cookie srs matches the view's native srs
     * @return
     */
    private boolean srsNamesMatch(ActionParameters params, View view) {
        try {
            JSONObject cookieState = getStateFromCookie(params.getCookie(COOKIE_SAVED_STATE));
            String srs = JSONHelper.getStringFromJSON(cookieState, "srs", null);
            return getConfiguration(view).getJSONObject("mapfull").getJSONObject("conf").getJSONObject("mapOptions").get("srsName").equals(srs);
        } catch(Exception e) {
            log.error("Srs parsing failed. ", e);
            return true;
        }
    }
    private boolean updateUsageData(final View view)  {
        try {
            viewService.updateViewUsage(view);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private JSONObject getConfiguration(final View view) throws ActionException {
        try {
            return ViewHelper.getConfiguration(view);
        } catch (ViewException e) {
            throw new ActionException("Couldn't get configuration", e);
        }
    }

    private JSONArray getStartupSequence(final View view)
            throws ActionException {
        try {
            return ViewHelper.getStartupSequence(view);
        } catch (ViewException e) {
            throw new ActionException("Couldn't get startup sequence", e);
        }
    }


    private View getView(final ActionParameters params, final long defaultViewId, final long oldId) throws ActionException {

        long viewId = ConversionHelper.getLong(params.getHttpParam(ActionConstants.PARAM_VIEW_ID), defaultViewId);
        final String uuId = params.getHttpParam(ActionConstants.PARAM_UUID);
        //final long viewId, final long oldId, final String uuId
        if (uuId != null) {
            log.debug("Requested UUID :" + uuId);
            return viewService.getViewWithConfByUuId(uuId);
        } else if (oldId > 0){
            log.debug("Requested old View ID :" + oldId);
            return viewService.getViewWithConfByOldId(oldId);
        }
        log.debug("Requested View ID:" + viewId);
        View view = viewService.getViewWithConf(viewId);
        if(viewId != defaultViewId && view.isOnlyForUuId()) {
            log.warn("View can only be loaded by uuid. ViewId:", viewId);
            return null;
        }
        return view;
    }


    private JSONObject getStateFromCookie(javax.servlet.http.Cookie cookie) {
        if (cookie == null) {
            log.debug("Cookie state was <null>");
            return null;
        }
        // cookie view data
        try {
            String value = URLDecoder.decode(cookie.getValue(),"UTF-8");
            if (!value.isEmpty()) {
                log.debug("Using cookie state:", value);
                return new JSONObject(value);
            }
        } catch (Exception je) {
            log.info("Got cookie but couldn't transform to JSON", cookie);
        }
        return null;
    }

    private void modifyView(final View view, JSONObject myview) {
        if (myview == null) {
            return;
        }
        log.info("[GetAppSetupHandler] Fetching View from cookie", myview);
        // merge cookie state for mapfull
        try {
            JSONObject viewdata = myview.optJSONObject(VIEW_DATA);
            if(viewdata == null) {
                return;
            }
            for ( Iterator<String> bundleIterator = viewdata.keys(); bundleIterator.hasNext(); ) {
                final String bundleName = bundleIterator.next();
                //final String bundle = viewdata.getString(bundleName);
                final JSONObject bundle = viewdata.optJSONObject(bundleName);
                if(bundle == null) {
                    continue;
                }
                final JSONObject state = bundle.optJSONObject(STATE);
                if(state == null || state.names() == null) {
                    continue;
                }
                log.debug("Got state for bundle", bundleName, "- state:", state);
                if(ViewModifier.BUNDLE_MAPFULL.equals(bundleName) &&
                        state.optJSONArray("selectedLayers").length() == 0) {
                    // Do not use mapfull state if there's no selected layers
                    continue;
                }
                Bundle b = view.getBundleByName(bundleName);
                if(b != null) {
                    b.setState(state.toString());
                }
            }
        } catch (Exception ex) {
            log.info(ex, "Error parsing cookie JSON:", myview);
        }
    }

    private void addBundle(final ModifierParams params, final String id, final Bundle bundle) {

        if(bundle == null) {
            // admin bundle init failed. See init().
            log.warn("Tried to insert bundle but it isn't initialized. Id:", id);
            return;
        }
        try {
            // add to startup sequence
            params.getStartupSequence().put(JSONHelper.createJSONObject(bundle.getStartup()));
            // add initial config/state
            final JSONObject bundleConfig = new JSONObject();
            bundleConfig.put(ViewModifier.KEY_CONF,
                    JSONHelper.createJSONObject(bundle.getConfig()));
            bundleConfig.put(ViewModifier.KEY_STATE,
                    JSONHelper.createJSONObject(bundle.getState()));
            params.getConfig().put(id, bundleConfig);
        } catch (Exception e) {
            log.error(e, "Failed to add", id, "bundle to startup sequence:",
                    bundle);
        }
    }
}
