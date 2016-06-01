package fi.sito.nba.control;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionDeniedException;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.RestActionHandler;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.JSONHelper;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.ResponseHelper;
import fi.sito.nba.registry.infrastructure.NotImplementedException;
import fi.sito.nba.registry.infrastructure.InvalidArgumentException;
import fi.sito.nba.registry.models.AncientMonument;
import fi.sito.nba.registry.models.AncientMonumentArea;
import fi.sito.nba.registry.models.AncientMonumentSubItem;
import fi.sito.nba.registry.services.AncientMonumentService;
import fi.sito.nba.registry.services.AncientMonumentMaintenanceItemService;
import fi.sito.nba.registry.services.BuildingHeritageItemService;
import fi.sito.nba.registry.models.AncientMonumentMaintenanceItem;
import fi.sito.nba.registry.models.AncientMonumentMaintenanceItemSubArea;
import fi.sito.nba.registry.models.BuildingHeritageItemPoint;
import fi.sito.nba.registry.models.BuildingHeritageItemArea;
import fi.sito.nba.registry.models.BuildingHeritageItem;


@OskariActionRoute("UpdateRegistryItems")
public class UpdateRegistryItemsHandler extends RestActionHandler {
	private static final String PARAM_REGISTER_NAME = "registerName";
	private static final String PARAM_ITEM = "item";
	private static final String PARAM_EDITED = "edited";
	private static final String[] ALLOWED_ROLES = new String[] { "Admin",
			"Pääkäyttäjä", "Ylläpitäjä", "Viraston muokkaaja",
			"Ulk. viranomaismuokkaaja", "Ulk. muu muokkaaja" };

	private static final Logger LOG = LogFactory
			.getLogger(GetRegistryItemsHandler.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.registerModule(new JsonOrgModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	public void preProcess(ActionParameters params) throws ActionException {
		// common method called for all request methods
		User user = params.getUser();
		LOG.info(user, "accessing route", getName());
		params.requireLoggedInUser();
		if (!user.hasAnyRoleIn(ALLOWED_ROLES)) {
			throw new ActionDeniedException("Not allowed");
		}
	}

	@Override
	public void handlePost(ActionParameters params) throws ActionException {

		LOG.info("update start");

		Object response = null;
		Connection connection = null;

		try {
			// set up connection
			SQLServerDataSource ds = new SQLServerDataSource();
			ds.setUser(PropertyUtil.get("nba.db.username"));
			ds.setPassword(PropertyUtil.get("nba.db.password"));
			ds.setURL(PropertyUtil.get("nba.db.url"));
			connection = ds.getConnection();

			String registryNameParam = "";
			String itemJson = "";
			String editInfoJson = "";

			if (params.getHttpParam(PARAM_REGISTER_NAME) != null
					&& !params.getHttpParam(PARAM_REGISTER_NAME).equals("")
					&& params.getHttpParam(PARAM_ITEM) != null
					&& !params.getHttpParam(PARAM_ITEM).equals("")
					&& params.getHttpParam(PARAM_EDITED) != null
					&& !params.getHttpParam(PARAM_EDITED).equals("")) {

				registryNameParam = params.getHttpParam(PARAM_REGISTER_NAME);
				itemJson = params.getHttpParam(PARAM_ITEM);
				editInfoJson = params.getHttpParam(PARAM_EDITED);

				switch (registryNameParam) {
				case "ancientMonument":
					AncientMonumentService service = new AncientMonumentService(
							connection);
					AncientMonument monument = mapper.readValue(itemJson,
							AncientMonument.class);

					JSONObject editInfo = new JSONObject(editInfoJson);

					response = updateAncientMonument(monument, service, editInfo,
							params.getUser());

					break;
				case "ancientMaintenance":
					AncientMonumentMaintenanceItemService ammService = new AncientMonumentMaintenanceItemService(
							connection);
					AncientMonumentMaintenanceItem ammMonument = mapper.readValue(itemJson,
							AncientMonumentMaintenanceItem.class);

					JSONObject ammeditInfo = new JSONObject(editInfoJson);

					response = updateMaintenance(ammMonument, ammService, ammeditInfo,
							params.getUser());

					break;
				case "buildingHeritage":
					BuildingHeritageItemService bhService = new BuildingHeritageItemService(
							connection);
					BuildingHeritageItem bhMonument = mapper.readValue(itemJson,
							BuildingHeritageItem.class);

					JSONObject bhEditInfo = new JSONObject(editInfoJson);

					response = updateBuildingHeritage(bhMonument, bhService, bhEditInfo,
							params.getUser());

					break;
				case "rky1993":
				case "rky2000":
					throw new NotImplementedException();
				}
			}
		} catch (IOException | SQLException | JSONException | InvalidArgumentException e) {
			throw new ActionException("Error during updating registry item", e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
			}
		}

		ResponseHelper.writeResponse(params, response);
	}
	
	private JSONObject updateAncientMonument(AncientMonument monument,
			AncientMonumentService service, JSONObject editInfo, User user)
			throws SQLException, JSONException, InvalidArgumentException {

		JSONObject ret = new JSONObject();
		ret.put("updated", false);

		boolean mainItemEdited = JSONHelper.getBooleanFromJSON(editInfo,
				"edited", false);

		List<Integer> editedSubItemIds = JSONHelper
				.getArrayAsList(editInfo.getJSONArray("subItems"));
		List<Integer> editedAreaIds = JSONHelper
				.getArrayAsList(editInfo.getJSONArray("areas"));

		AncientMonument original = service
				.getAncientMonumentById(monument.getId());

		Map<Integer, AncientMonumentArea> originalAreas = new HashMap<Integer, AncientMonumentArea>();
		for (AncientMonumentArea areaItem : original.getAreas()) {
			originalAreas.put(areaItem.getId(), areaItem);
		}

		if (mainItemEdited) {
			service.updateAncientMonument(monument.getId(),
					user.getScreenname(), monument.getDescription(),
					monument.getSurveyingAccuracy(),
					monument.getSurveyingType(), monument.getGeometry());
			ret.put("updated", true);
			ret.put("mainItems", 1);
		}

		for (AncientMonumentSubItem subItem : monument.getSubItems()) {
			if (editedSubItemIds.contains(subItem.getId())) {
				service.updateAncientMonumentSubItem(subItem.getId(),
						user.getScreenname(), subItem.getDescription(),
						subItem.getSurveyingAccuracy(),
						subItem.getSurveyingType(), subItem.getGeometry());
				ret.put("updated", true);
				ret.put("subItems", ret.optInt("subItems", 0) + 1);
			}
		}

		for (AncientMonumentArea area : monument.getAreas()) {
			AncientMonumentArea originalArea = originalAreas.get(area.getId());
			if (originalArea == null) {
				service.addAncientMonumentArea(monument.getId(),
						user.getScreenname(), area.getDescription(),
						area.getSurveyingAccuracy(), area.getSurveyingType(),
						area.getAreaChangeReason(), area.getGeometry());
				ret.put("updated", true);
				ret.put("areas", ret.optInt("areas", 0) + 1);
			} else if (editedAreaIds.contains(area.getId())) {
				service.updateAncientMonumentArea(area.getId(),
						user.getScreenname(), area.getDescription(),
						area.getSurveyingAccuracy(), area.getSurveyingType(),
						area.getAreaChangeReason(), area.getGeometry());

				ret.put("updated", true);
				ret.put("areas", ret.optInt("areas", 0) + 1);
			}
		}

		return ret;
	}
	
	private JSONObject updateMaintenance(AncientMonumentMaintenanceItem monument,
			AncientMonumentMaintenanceItemService service, JSONObject editInfo, User user)
			throws SQLException, JSONException, InvalidArgumentException  {

		JSONObject ret = new JSONObject();
		ret.put("updated", false);

		boolean mainItemEdited = JSONHelper.getBooleanFromJSON(editInfo,
				"edited", false);

		if (mainItemEdited) {
			if (monument.getPointGeometry() != null) {
				service.updateAncientMonumentMaintenanceItemPoint(monument.getId(),
						user.getScreenname(), monument.getPointDescription(),
						monument.getPointSurveyingAccuracy(),
						monument.getPointSurveyingType(), monument.getPointGeometry());
			}
			
			if (monument.getAreaGeometry() != null) {
				service.updateAncientMonumentMaintenanceItemArea(monument.getId(),
						user.getScreenname(), monument.getAreaGeometry());
			}
			
			ret.put("updated", true);
			ret.put("mainItems", 1);
		}
		
		List<Integer> editedSubItemIds = JSONHelper
				.getArrayAsList(editInfo.getJSONArray("subAreas"));

		for (AncientMonumentMaintenanceItemSubArea subArea : monument.getSubAreas()) {
			if (editedSubItemIds.contains(subArea.getId())) {
				service.updateAncientMonumentMaintenanceItemSubArea(subArea.getId(),
						user.getScreenname(), subArea.getGeometry());
				ret.put("updated", true);
				ret.put("subItems", ret.optInt("subItems", 0) + 1);
			}
		}

		return ret;
	}

	private JSONObject updateBuildingHeritage(BuildingHeritageItem monument,
			BuildingHeritageItemService service, JSONObject editInfo, User user)
			throws SQLException, JSONException, InvalidArgumentException  {

		JSONObject ret = new JSONObject();
		ret.put("updated", false);

		List<Integer> editedPointIds = JSONHelper
				.getArrayAsList(editInfo.getJSONArray("points"));
		List<Integer> editedAreaIds = JSONHelper
				.getArrayAsList(editInfo.getJSONArray("areas"));

		BuildingHeritageItem original = service
				.getBuildingHeritageItemById(monument.getId());

		Map<Integer, BuildingHeritageItemArea> originalAreas = new HashMap<Integer, BuildingHeritageItemArea>();
		for (BuildingHeritageItemArea areaItem : original.getAreas()) {
			originalAreas.put(areaItem.getId(), areaItem);
		}

		for (BuildingHeritageItemPoint point : monument.getPoints()) {
			if (editedPointIds.contains(point.getId())) {
				service.updateBuildingHeritageItemPoint(point.getId(), user.getScreenname(), 
						point.getObjectName(),  point.getDescription(), point.getSurveyingAccuracy(), 
						point.getSurveyingType(), point.getConservationGroup(), point.getConservationStatus(),
						point.getGeometry());
				ret.put("updated", true);
				ret.put("subItems", ret.optInt("subItems", 0) + 1);
			}
		}

		for (BuildingHeritageItemArea area : monument.getAreas()) {
			BuildingHeritageItemArea originalArea = originalAreas.get(area.getId());
			if (originalArea == null) {
				service.addBuildingHeritageItemArea(monument.getId(),
						user.getScreenname(), area.getObjectName(), area.getDescription(),
						area.getSurveyingAccuracy(), area.getSurveyingType(),
						area.getConservationGroup(), area.getConservationStatus(),
						area.getGeometry());
				ret.put("updated", true);
				ret.put("areas", ret.optInt("areas", 0) + 1);
			} else if (editedAreaIds.contains(area.getId())) {
				service.updateBuildingHeritageItemArea(monument.getId(),
						user.getScreenname(), area.getObjectName(), area.getDescription(),
						area.getSurveyingAccuracy(), area.getSurveyingType(),
						area.getConservationGroup(), area.getConservationStatus(),
						area.getGeometry());

				ret.put("updated", true);
				ret.put("areas", ret.optInt("areas", 0) + 1);
			}
		}

		return ret;
	}

}
