package fi.sito.nba.control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.mml.portti.domain.permissions.Permissions;
import fi.mml.portti.service.db.permissions.PermissionsService;
import fi.mml.portti.service.db.permissions.PermissionsServiceIbatisImpl;
import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.ActionParamsException;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;

@OskariActionRoute("SaveRegisterPermission")
public class SaveNbaRegisterPermissionsHandler extends ActionHandler {

    private static String PARAMETER_PERMISSION_DATA = "resource";

    private final static Logger log = LogFactory.getLogger(SaveNbaRegisterPermissionsHandler.class);
    private final static PermissionsService permissionsService = new PermissionsServiceIbatisImpl();

    @Override
    public void handleAction(ActionParameters params) throws ActionException {
        log.debug("PERMISSION HANDLER LAYER");
        // only accept admins
        params.requireAdminUser();

        final Permissions permissions = new Permissions();
        permissions.setExternalIdType(Permissions.EXTERNAL_TYPE_ROLE);
        permissions.getUniqueResourceName().setType("attribute");

        final JSONArray resources = parseJSONArray(params.getHttpParam(PARAMETER_PERMISSION_DATA));
        final String whoMakesThisModification = params.getUser().getScreenname();

        try {
            for (int i = 0; i < resources.length(); i++) {

                final JSONObject layerPermission = resources.getJSONObject(i);

                log.debug(layerPermission.getString("name"));
                log.debug("resource : ", layerPermission.getString("resourceName"), " namespace ",
                        layerPermission.getString("namespace"), " roleId ", layerPermission.getString("roleId"),
                        " isSelected", layerPermission.getBoolean("isSelected"));

                permissions.setExternalId(layerPermission.getString("roleId"));
                permissions.getUniqueResourceName().setNamespace(layerPermission.getString("namespace"));
                permissions.getUniqueResourceName().setName(layerPermission.getString("resourceName"));

                if (layerPermission.getBoolean("isSelected")) {
                    addPermissions(permissions, "VIEW_ATTRIBUTE");
                } else {
                    log.warn("Changing permissions (DELETE) by user '" + whoMakesThisModification + "': " + permissions);
                    deletePermissions(permissions, "VIEW_ATTRIBUTE");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray parseJSONArray(final String jsonArray) throws ActionParamsException {
        try {
            final JSONArray resources = new JSONArray(jsonArray);
            log.debug(" permissions JSON ", resources);
            return resources;
        } catch (Exception e) {
            throw new ActionParamsException("Unable to parse param JSON:\n" + jsonArray);
        }
    }

    private void addPermissions(final Permissions permissions, final String permissionType) {
        permissionsService.insertPermissions(permissions.getUniqueResourceName(),
                permissions.getExternalId(), permissions.getExternalIdType(), permissionType);
    }

    private void deletePermissions(final Permissions permissions, final String permissionType) {
        permissionsService.deletePermissions(permissions.getUniqueResourceName(), permissions.getExternalId(),
                permissions.getExternalIdType(), permissionType);
    }

}
