package fi.sito.nba.view;

import java.util.List;

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
	private NbaRegistryLayerService service = new NbaRegistryLayerService();

	@Override
	public boolean modifyBundle(ModifierParams params) throws ModifierException {
		final JSONObject config = getBundleConfig(params.getConfig());

		if (config == null) {
			return false;
		}

		// Add registry layers
		List<NbaRegistryLayer> layers = service.findRegistryLayers();
		JSONObject registries = new JSONObject();

		try {
			for (NbaRegistryLayer lyr : layers) {
				JSONObject registry = new JSONObject();
				registry.put("name", lyr.getRegistryName());
				registry.put("idAttribute", lyr.getItemIdAttribute());
				registry.put("itemType", lyr.getItemType());
				registries.put(Integer.toString(lyr.getLayerId()), registry);
			}
			config.put("registryLayers", registries);
		} catch (JSONException e) {
			log.error(
					"Unable to set registry layer ids to nba-registers config",
					e);
		}

		// Add roles with rights to edit registry items
		try {
			String[] rolesForEditor = PropertyUtil.getCommaSeparatedList("nba.registers.editroles");
			JSONArray editorRoles = new JSONArray();
			for (String role: rolesForEditor) {
				editorRoles.put(role);
			}
			config.put("editorRoles", editorRoles);
		} catch (JSONException e) {
			log.error("Unable to set editor roles to nba-registers config", e);
		}

		return false;
	}

}
