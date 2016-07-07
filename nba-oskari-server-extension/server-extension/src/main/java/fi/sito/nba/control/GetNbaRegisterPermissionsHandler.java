package fi.sito.nba.control;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import fi.mml.portti.service.db.permissions.PermissionsService;
import fi.mml.portti.service.db.permissions.PermissionsServiceIbatisImpl;
import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.permission.domain.Resource;
import fi.nls.oskari.util.ResponseHelper;

@OskariActionRoute("GetRegistryPermissions")
public class GetNbaRegisterPermissionsHandler extends ActionHandler {
	private static PermissionsService permissionsService = new PermissionsServiceIbatisImpl();
	private static String JSON_ID = "id";
	private static String JSON_NAME = "name";
	private static String JSON_NAMES_SPACE = "namespace";
	private static String JSON_RESOURCE_NAME = "resourceName";
	private static String JSON_RESOURCE = "resource";
	private static String JSON_IS_SELECTED = "isSelected";

	@Override
	public void handleAction(ActionParameters params) throws ActionException {

		// require admin user
		//params.requireAdminUser();

		String externalId = params.getHttpParam("externalId", "");
		String externalType = params.getHttpParam("externalType", "");

		List<Resource> resources = permissionsService.findResourcesWithType("attribute");

		List<String> permissions = permissionsService
				.getResourcesWithGrantedPermissions("attribute", externalId,
						externalType, "VIEW_ATTRIBUTE");

		JSONObject root = new JSONObject();

		for (Resource resource : resources) {
			try {
				JSONObject realJson = new JSONObject();
				realJson.put(JSON_ID, resource.getId());
				realJson.put(JSON_NAME,
						resource.getMapping());
				realJson.put(JSON_NAMES_SPACE, resource.getMapping().split("\\+")[0]);
				realJson.put(JSON_RESOURCE_NAME, resource.getMapping().split("\\+")[1]);
				final String permissionKey = resource.getMapping();

				if (permissions.contains(permissionKey)) {
					realJson.put(JSON_IS_SELECTED, true);
				} else {
					realJson.put(JSON_IS_SELECTED, false);
				}

				root.append(JSON_RESOURCE, realJson);
			} catch (JSONException e) {
				throw new ActionException(
						"Something is wrong with doPermissionResourcesJson ajax reguest",
						e);
			}

		}

		ResponseHelper.writeResponse(params, root.toString());
	}
}
