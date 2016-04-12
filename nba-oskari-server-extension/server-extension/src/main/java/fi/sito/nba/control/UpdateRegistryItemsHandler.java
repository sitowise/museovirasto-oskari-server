package fi.sito.nba.control;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.RestActionHandler;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.ResponseHelper;
import fi.sito.nba.registry.infrastructure.NotImplementedException;
import fi.sito.nba.registry.models.AncientMonument;
import fi.sito.nba.registry.models.AncientMonumentArea;
import fi.sito.nba.registry.models.AncientMonumentSubItem;
import fi.sito.nba.registry.repositories.AncientMonumentRepository;
import fi.sito.nba.registry.services.AncientMonumentService;

@OskariActionRoute("UpdateRegistryItems")
public class UpdateRegistryItemsHandler extends RestActionHandler {
	private static final String PARAM_REGISTER_NAME = "registerName";
	private static final String PARAM_ITEM = "item";

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
		LOG.info(params.getUser(), "accessing route", getName());
		params.requireLoggedInUser();
		// FIXME: needs to check if user has specific role
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

			if (params.getHttpParam(PARAM_REGISTER_NAME) != null
					&& !params.getHttpParam(PARAM_REGISTER_NAME).equals("")
					&& params.getHttpParam(PARAM_ITEM) != null
					&& !params.getHttpParam(PARAM_ITEM).equals("")) {

				registryNameParam = params.getHttpParam(PARAM_REGISTER_NAME);
				itemJson = params.getHttpParam(PARAM_ITEM);

				switch (registryNameParam) {
				case "ancientMonument":
					AncientMonumentService service = new AncientMonumentService(
							connection);
					AncientMonumentRepository repo = new AncientMonumentRepository(
							connection);
					AncientMonument monument = mapper.readValue(itemJson,
							AncientMonument.class);

					response = update(monument, service, repo);

					break;
				case "ancientMaintenance":
				case "buildingHeritage":
				case "rky1993":
				case "rky2000":
					throw new NotImplementedException();
				}
			}
		} catch (IOException | SQLException | JSONException e) {
			throw new ActionException("Error during updating registry item", e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
			}
		}

		ResponseHelper.writeResponse(params, response);
	}

	private JSONObject update(AncientMonument monument,
			AncientMonumentService service, AncientMonumentRepository repo)
			throws SQLException, JSONException {

		JSONObject ret = new JSONObject();
		ret.put("updated", false);

		AncientMonument original = service
				.getAncientMonumentById(monument.getId());
		Map<Integer, AncientMonumentSubItem> originalSubItems = new HashMap<Integer, AncientMonumentSubItem>();
		for (AncientMonumentSubItem subItem : original.getSubItems()) {
			originalSubItems.put(subItem.getId(), subItem);
		}

		Map<Integer, AncientMonumentArea> originalAreas = new HashMap<Integer, AncientMonumentArea>();
		for (AncientMonumentArea areaItem : original.getAreas()) {
			originalAreas.put(areaItem.getId(), areaItem);
		}

		if (monument.getSurveyingAccuracy() != original.getSurveyingAccuracy()
				|| monument.getSurveyingType() != original.getSurveyingType()
				|| !monument.getDescription().equals(original.getDescription())
				|| !monument.getGeometry().equals(original.getGeometry())) {
			repo.update(monument.getId(), monument.getSurveyingAccuracy(),
					monument.getSurveyingType(), monument.getDescription(),
					monument.getGeometry());
			ret.put("updated", true);
			ret.put("mainItems", 1);
		}

		for (AncientMonumentSubItem subItem : monument.getSubItems()) {
			AncientMonumentSubItem originalSubItem = originalSubItems
					.get(subItem.getId());
			if (subItem.getSurveyingAccuracy() != originalSubItem
					.getSurveyingAccuracy()
					|| subItem.getSurveyingType() != originalSubItem
							.getSurveyingType()
					|| !subItem.getDescription()
							.equals(originalSubItem.getDescription())
					|| !subItem.getGeometry()
							.equals(originalSubItem.getGeometry())) {
				repo.updateSubItem(subItem.getId(),
						subItem.getSurveyingAccuracy(),
						subItem.getSurveyingType(), subItem.getDescription(),
						subItem.getGeometry());
				ret.put("updated", true);
				ret.put("subItems", ret.optInt("subItems", 0) + 1);
			}
		}

		for (AncientMonumentArea area : monument.getAreas()) {
			AncientMonumentArea originalArea = originalAreas
					.get(area.getId());
			if (!area.getName().equals(originalArea.getName())
					|| !area.getMunicipalityName()
							.equals(originalArea.getMunicipalityName())
					|| !area.getDescription()
							.equals(originalArea.getDescription())
					|| area.getClassification() != originalArea
							.getClassification()
					|| !area.getCopyright().equals(originalArea.getCopyright())
					|| !area.getDigiMk().equals(originalArea.getDigiMk())
					|| !area.getDigiMkYear().equals(originalArea.getDigiMkYear())
					|| !area.getDigitizationAuthor()
							.equals(originalArea.getDigitizationAuthor())
					|| !area.getDigitizationDate()
							.equals(originalArea.getDigitizationDate())
					|| area.getAreaSelectionType() != originalArea
							.getAreaSelectionType()
					|| area.getAreaSelectionSource() != originalArea
							.getAreaSelectionSource()
					|| area.getSurveyingAccuracy() != originalArea
							.getSurveyingAccuracy()
					|| area.getSurveyingType() != originalArea
							.getSurveyingType()
					|| !area.getGeometry().equals(originalArea.getGeometry())) {

				repo.updateArea(area.getId(), area.getName(),
						area.getMunicipalityName(), area.getDescription(),
						area.getClassification(), area.getCopyright(),
						area.getDigiMk(), area.getDigiMkYear(),
						area.getDigitizationAuthor(),
						area.getDigitizationDate(), area.getAreaSelectionType(),
						area.getAreaSelectionSource(),
						area.getSurveyingAccuracy(), area.getSurveyingType(),
						area.getGeometry());

				ret.put("updated", true);
				ret.put("areas", ret.optInt("areas", 0) + 1);
			}
		}

		return ret;
	}

}
