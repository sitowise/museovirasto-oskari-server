package fi.sito.nba.view;

import java.util.List;

import fi.sito.nba.model.NbaRegistry;
import fi.sito.nba.service.NbaRegistryService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.nls.oskari.annotation.OskariViewModifier;
import fi.nls.oskari.control.view.modifier.bundle.BundleHandler;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.view.modifier.ModifierException;
import fi.nls.oskari.view.modifier.ModifierParams;
import fi.sito.nba.model.NbaRegistryLayer;
import fi.sito.nba.service.NbaRegistryLayerService;

@OskariViewModifier("nba-registers")
public class NBARegistersModifier extends BundleHandler {

	private static final Logger log = LogFactory
			.getLogger(NBARegistersModifier.class);
	private NbaRegistryLayerService registryLayerService = new NbaRegistryLayerService();
	private NbaRegistryService registryService = new NbaRegistryService();

	@Override
	public boolean modifyBundle(ModifierParams params) throws ModifierException {
		final JSONObject config = getBundleConfig(params.getConfig());

		if (config == null) {
			return false;
		}

		// Add registries and their locales
		List<NbaRegistry> registries = registryService.findRegistries();
		JSONObject registriesJson = new JSONObject();

		try {
			for (NbaRegistry reg : registries) {
				registriesJson.put(reg.getName(), new JSONObject(reg.getLocale()));
			}
			config.put("registries", registriesJson);
		} catch (JSONException e) {
			log.error("Unable to set registries to nba-registers config", e);
		}

		// Add registry layers
		List<NbaRegistryLayer> layers = registryLayerService.findRegistryLayers();
		JSONObject registryLayersJson = new JSONObject();

		try {
			for (NbaRegistryLayer lyr : layers) {
				JSONObject registryLayerJson = new JSONObject();
				registryLayerJson.put("name", lyr.getRegistryName());
				registryLayerJson.put("mainIdAttribute", lyr.getMainItemIdAttr());
				registryLayerJson.put("subIdAttribute", lyr.getSubItemIdAttr());
				registryLayerJson.put("itemType", lyr.getItemType());
				registryLayerJson.put("gfiAttributes",
						(lyr.getGfiAttributes() != null && !lyr.getGfiAttributes().isEmpty())
						? new JSONArray(lyr.getGfiAttributes())
						: null);
				registryLayersJson.put(Integer.toString(lyr.getLayerId()), registryLayerJson);
			}
			config.put("registryLayers", registryLayersJson);
		} catch (JSONException e) {
			log.error(
					"Unable to set registry layer ids to nba-registers config",
					e);
		}

		// Add roles with rights to edit registry items
		try {
			JSONObject editorRolesJson = new JSONObject();
			JSONArray generalRolesJson = new JSONArray();

			String[] rolesForEditor = PropertyUtil.getCommaSeparatedList("nba.registers.editroles");
			for (String role: rolesForEditor) {
				generalRolesJson.put(role);
			}
			editorRolesJson.put("general", generalRolesJson);

			//find configurations for specific registries
			List<String> propertyNames = PropertyUtil.getPropertyNamesStartingWith("nba.registers.editroles.");
			for (String propertyName: propertyNames) {
				rolesForEditor = PropertyUtil.getCommaSeparatedList(propertyName);

				JSONArray registryRolesJson = new JSONArray();
				for (String role: rolesForEditor) {
					registryRolesJson.put(role);
				}

				String registryName = propertyName.substring(propertyName.lastIndexOf(".") + 1);
				editorRolesJson.put(registryName, registryRolesJson);
			}

			config.put("editorRoles", editorRolesJson);
		} catch (JSONException e) {
			log.error("Unable to set editor roles to nba-registers config", e);
		}

		return false;
	}

}
