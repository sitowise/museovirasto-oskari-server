package fi.sito.nba.control;

import java.sql.Connection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.RestActionHandler;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.ResponseHelper;
import fi.sito.nba.registry.models.AncientMonument;
import fi.sito.nba.registry.models.AncientMonumentMaintenanceItem;
import fi.sito.nba.registry.models.BuildingHeritageItem;
import fi.sito.nba.registry.models.IRegistryObject;
import fi.sito.nba.registry.models.RKY1993;
import fi.sito.nba.registry.models.RKY2000;
import fi.sito.nba.registry.services.AncientMonumentMaintenanceItemService;
import fi.sito.nba.registry.services.AncientMonumentService;
import fi.sito.nba.registry.services.BuildingHeritageItemService;
import fi.sito.nba.registry.services.RKY1993Service;
import fi.sito.nba.registry.services.RKY2000Service;

@OskariActionRoute("GetRegistryItems")
public class GetRegistryItemsHandler extends RestActionHandler {
	private static final String PARAM_REGISTER_NAME = "registerName";
	private static final String PARAM_ITEM_ID = "id";
	private static final String PARAM_REGISTRIES = "registries";
	private static final String PARAM_KEYWORD = "keyword";
	private static final String PARAM_GEOMETRY = "geometry";

	private static final Logger LOG = LogFactory
			.getLogger(GetRegistryItemsHandler.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.registerModule(new JsonOrgModule());
	}

	public void preProcess(ActionParameters params) throws ActionException {
		// common method called for all request methods
		LOG.info(params.getUser(), "accessing route", getName());
	}

	@Override
	public void handleGet(ActionParameters params) throws ActionException {

		Object response = null;
		Connection connection = null;

		try {
			// set up connection
			SQLServerDataSource ds = new SQLServerDataSource();
			ds.setUser(PropertyUtil.get("nba.db.username"));
			ds.setPassword(PropertyUtil.get("nba.db.password"));
			ds.setURL(PropertyUtil.get("nba.db.url"));
			connection = ds.getConnection();

			String registriesParam = "";
			String keywordParam = null;
			Geometry geometryParam = null;
			int itemIdParam = 0;
			String registryNameParam = "";

			if (params.getHttpParam(PARAM_ITEM_ID) != null
					&& !params.getHttpParam(PARAM_ITEM_ID).equals("")
					&& params.getHttpParam(PARAM_REGISTER_NAME) != null
					&& !params.getHttpParam(PARAM_REGISTER_NAME).equals("")) {

				itemIdParam = Integer
						.parseInt(params.getHttpParam(PARAM_ITEM_ID));
				registryNameParam = params.getHttpParam(PARAM_REGISTER_NAME);
				JSONObject itemObj = new JSONObject();

				Object service = null;
				IRegistryObject registryItem = null;

				switch (registryNameParam) {
				case "ancientMonument":
					service = new AncientMonumentService(connection);
					registryItem = ((AncientMonumentService) service)
							.getAncientMonumentById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "ancientMaintenance":
					service = new AncientMonumentMaintenanceItemService(
							connection);
					registryItem = ((AncientMonumentMaintenanceItemService) service)
							.getAncientMonumentMaintenanceItemById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "buildingHeritage":
					service = new BuildingHeritageItemService(connection);
					registryItem = ((BuildingHeritageItemService) service)
							.getBuildingHeritageItemById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "rky1993":
					service = new RKY1993Service(connection);
					registryItem = ((RKY1993Service) service)
							.getRKY1993ById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "rky2000":
					service = new RKY2000Service(connection);
					registryItem = ((RKY2000Service) service)
							.getRKY2000ById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				}

				response = itemObj;
			} else {

				// read parameters
				if (params.getHttpParam(PARAM_REGISTRIES) != null
						&& !params.getHttpParam(PARAM_REGISTRIES).equals("")) {
					registriesParam = params.getHttpParam(PARAM_REGISTRIES);
				}

				if (params.getHttpParam(PARAM_KEYWORD) != null
						&& !params.getHttpParam(PARAM_KEYWORD).equals("")) {
					keywordParam = params.getHttpParam(PARAM_KEYWORD);
				}

				if (params.getHttpParam(PARAM_GEOMETRY) != null
						&& !params.getHttpParam(PARAM_GEOMETRY).equals("")) {
					String geometryStr = params.getHttpParam(PARAM_GEOMETRY);
					geometryParam = (new WKTReader()).read(geometryStr);
				}

				JSONArray generalResultArray = new JSONArray();

				String[] registries = registriesParam.split(",");
				if (registries[0].length() > 0) {
					for (String registry : registries) {

						JSONArray registerResultArray = new JSONArray();

						switch (registry) {
						case "ancientMonument":
							registerResultArray = getAncientMonumentItems(
									connection, keywordParam, geometryParam);
							break;
						case "ancientMaintenance":
							registerResultArray = getAncientMonumentMaintenanceItems(
									connection, keywordParam, geometryParam);
							break;
						case "buildingHeritage":
							registerResultArray = getBuildingHeritageItems(
									connection, keywordParam, geometryParam);
							break;
						case "rky1993":
							registerResultArray = getRKY1993Items(connection,
									keywordParam, geometryParam);
							break;
						case "rky2000":
							registerResultArray = getRKY2000Items(connection,
									keywordParam, geometryParam);
							break;
						}

						for (int i = 0; i < registerResultArray.length(); i++) {
							generalResultArray.put(registerResultArray.get(i));
						}

					}
				} else {
					JSONArray registerResultArray1 = getAncientMonumentItems(
							connection, keywordParam, geometryParam);
					JSONArray registerResultArray2 = getAncientMonumentMaintenanceItems(
							connection, keywordParam, geometryParam);
					JSONArray registerResultArray3 = getBuildingHeritageItems(
							connection, keywordParam, geometryParam);
					JSONArray registerResultArray4 = getRKY1993Items(connection,
							keywordParam, geometryParam);
					JSONArray registerResultArray5 = getRKY2000Items(connection,
							keywordParam, geometryParam);

					for (int i = 0; i < registerResultArray1.length(); i++) {
						generalResultArray.put(registerResultArray1.get(i));
					}

					for (int i = 0; i < registerResultArray2.length(); i++) {
						generalResultArray.put(registerResultArray2.get(i));
					}

					for (int i = 0; i < registerResultArray3.length(); i++) {
						generalResultArray.put(registerResultArray3.get(i));
					}

					for (int i = 0; i < registerResultArray4.length(); i++) {
						generalResultArray.put(registerResultArray4.get(i));
					}

					for (int i = 0; i < registerResultArray5.length(); i++) {
						generalResultArray.put(registerResultArray5.get(i));
					}
				}

				response = generalResultArray;
			}
		} catch (Exception e) {
			throw new ActionException("Error during geting registry item", e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
			}
		}

		ResponseHelper.writeResponse(params, response);
	}

	private JSONObject getItemObject(IRegistryObject registryObject) {
		JSONObject item = null;
		try {
			item = mapper.convertValue(registryObject, JSONObject.class);
			item.put("id", registryObject.getObjectId());
			// item.put("desc", registryObject.getObjectName());
			if (registryObject.getCentroid() != null) {
				item.put("coordinateX", registryObject.getCentroid().getX());
				item.put("coordinateY", registryObject.getCentroid().getY());
			}
			item.put("nbaUrl", registryObject.getNbaUrl());
			item.put("type", registryObject.getClass().getSimpleName());

			if (registryObject instanceof AncientMonumentMaintenanceItem) {
				item.put("mapLayerID", "69"); // "Oskari:MJHOITO"
			} else if (registryObject instanceof AncientMonument) {
				// item.put("mapLayerID", "64"); // "Oskari:MJOHDE"
				item.put("mapLayerID", "84"); // "WFS_MJrekisteri_WFS:mjpiste_kiintea_ja_muukp"
				item.put("mapLayerID2", "85"); // "WFS_MJrekisteri_WFS:mjpiste_muut"
			} else if (registryObject instanceof BuildingHeritageItem) {
				item.put("mapLayerID", "70"); // "Oskari:RAPEAKOHDE"
			} else if (registryObject instanceof RKY1993) {
				item.put("mapLayerID", "72"); // "Oskari:RKY1993"
			} else if (registryObject instanceof RKY2000) {
				item.put("mapLayerID", "71"); // "Oskari:RKY2000"
			} else {
				item.put("mapLayerID", "NO_LAYER");
			}

		} catch (Exception e) {
			LOG.error(e, "Error writing JSON");
		}
		return item;
	}

	private JSONArray getAncientMonumentMaintenanceItems(Connection con,
			String keyword, Geometry geometry) {

		JSONArray resultArray = new JSONArray();

		AncientMonumentMaintenanceItemService svc = new AncientMonumentMaintenanceItemService(
				con);

		Iterable<AncientMonumentMaintenanceItem> monuments = svc
				.findAncientMonumentMaintenanceItems(keyword, geometry);

		if (monuments != null) {
			Iterator<AncientMonumentMaintenanceItem> iterator = monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonumentMaintenanceItem monument = iterator.next();
					JSONObject item = new JSONObject();
					item.put("type", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					if (monument.getCentroid() != null) {
						item.put("coordinateX", monument.getCentroid().getX());
						item.put("coordinateY", monument.getCentroid().getY());
					}
					item.put("nbaUrl", monument.getNbaUrl());
					item.put("mapLayerID", "69"); // "Oskari:MJHOITO"
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getAncientMonumentItems(Connection con, String keyword,
			Geometry geometry) {

		JSONArray resultArray = new JSONArray();

		AncientMonumentService svc = new AncientMonumentService(con);

		Iterable<AncientMonument> monuments = svc.findAncientMonuments(keyword,
				geometry);
		if (monuments != null) {
			for (AncientMonument monument : monuments) {
				try {
					JSONObject item = new JSONObject();
					item.put("type", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					if (monument.getCentroid() != null) {
						item.put("coordinateX", monument.getCentroid().getX());
						item.put("coordinateY", monument.getCentroid().getY());
					}
					item.put("nbaUrl", monument.getNbaUrl());
					// item.put("mapLayerID", "64"); // "Oskari:MJOHDE"
					item.put("mapLayerID", "84"); // "WFS_MJrekisteri_WFS:mjpiste_kiintea_ja_muukp"
					item.put("mapLayerID2", "85"); // "WFS_MJrekisteri_WFS:mjpiste_muut"
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getBuildingHeritageItems(Connection con, String keyword,
			Geometry geometry) {

		JSONArray resultArray = new JSONArray();

		BuildingHeritageItemService svc = new BuildingHeritageItemService(con);

		Iterable<BuildingHeritageItem> monuments = svc
				.findBuildingHeritageItems(keyword, geometry);
		if (monuments != null) {
			for (BuildingHeritageItem monument : monuments) {
				try {
					JSONObject item = new JSONObject();
					item.put("type", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					if (monument.getCentroid() != null) {
						item.put("coordinateX", monument.getCentroid().getX());
						item.put("coordinateY", monument.getCentroid().getY());
					}
					item.put("nbaUrl", monument.getNbaUrl());
					item.put("mapLayerID", "70"); // "Oskari:RAPEAKOHDE"
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getRKY1993Items(Connection con, String keyword,
			Geometry geometry) {

		JSONArray resultArray = new JSONArray();

		RKY1993Service svc = new RKY1993Service(con);

		Iterable<RKY1993> monuments = svc.findRKY1993(keyword, geometry);
		if (monuments != null) {
			for (RKY1993 monument : monuments) {
				try {
					JSONObject item = new JSONObject();
					item.put("type", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					if (monument.getCentroid() != null) {
						item.put("coordinateX", monument.getCentroid().getX());
						item.put("coordinateY", monument.getCentroid().getY());
					}
					item.put("nbaUrl", monument.getNbaUrl());
					item.put("mapLayerID", "72"); // "Oskari:RKY1993"
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getRKY2000Items(Connection con, String keyword,
			Geometry geometry) {

		JSONArray resultArray = new JSONArray();

		RKY2000Service svc = new RKY2000Service(con);

		Iterable<RKY2000> monuments = svc.findRKY2000(keyword, geometry);

		if (monuments != null) {
			Iterator<RKY2000> iterator = monuments.iterator();

			while (iterator.hasNext()) {
				try {
					RKY2000 monument = iterator.next();

					JSONObject item = new JSONObject();
					item.put("type", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					if (monument.getCentroid() != null) {
						item.put("coordinateX", monument.getCentroid().getX());
						item.put("coordinateY", monument.getCentroid().getY());
					}
					item.put("nbaUrl", monument.getNbaUrl());
					item.put("mapLayerID", "71"); // "Oskari:RKY2000"
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

}
