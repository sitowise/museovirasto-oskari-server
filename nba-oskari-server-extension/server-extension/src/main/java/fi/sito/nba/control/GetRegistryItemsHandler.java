package fi.sito.nba.control;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.event.ListSelectionEvent;

import org.deegree.io.DBConnectionPool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionDeniedException;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.ActionParamsException;
import fi.nls.oskari.control.RestActionHandler;
import fi.nls.oskari.db.DatasourceHelper;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.ResponseHelper;
import fi.sito.nba.registry.models.*;
import fi.sito.nba.registry.services.*;
import fi.sito.nba.registry.infrastructure.*;
import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

@OskariActionRoute("GetRegistryItems")
public class GetRegistryItemsHandler extends RestActionHandler {
	private static final String PARAM_REGISTER_NAME = "registerName";
	private static final String PARAM_ITEM_ID = "id";
	private static final String PARAM_REGISTRIES = "registries";
	private static final String PARAM_KEYWORD = "keyword";
	private static final String PARAM_GEOMETRY = "geometry";

	private static final Logger LOG = LogFactory
			.getLogger(GetRegistryItemsHandler.class);

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

				itemIdParam = Integer.parseInt(params.getHttpParam(PARAM_ITEM_ID));
				registryNameParam = params.getHttpParam(PARAM_REGISTER_NAME);
				JSONObject itemObj = new JSONObject();

				Object service = null;
				IRegistryObject registryItem = null;
				
				switch (registryNameParam) {
				case "ancientMonument":
					service = new AncientMonumentService(connection);
					registryItem = ((AncientMonumentService)service).getAncientMonumentById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "ancientMaintenance":
					service = new AncientMonumentMaintenanceItemService(
							connection);
					registryItem = ((AncientMonumentMaintenanceItemService)service).getAncientMonumentMaintenanceItemById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "buildingHeritage":
					service = new BuildingHeritageItemService(
							connection);
					registryItem = ((BuildingHeritageItemService)service).getBuildingHeritageItemById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "rky1993":
					service = new RKY1993Service(connection);
					registryItem = ((RKY1993Service)service).getRKY1993ById(itemIdParam);
					itemObj = getItemObject(registryItem);
					break;
				case "rky2000":
					service = new RKY2000Service(connection);
					registryItem = ((RKY2000Service)service).getRKY2000ById(itemIdParam);
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
							registerResultArray = getAncientMonumentItems(connection,
									keywordParam, geometryParam);
							break;
						case "ancientMaintenance":
							registerResultArray = getAncientMonumentMaintenanceItems(
									connection, keywordParam, geometryParam);
							break;
						case "buildingHeritage":
							registerResultArray = getBuildingHeritageItems(connection,
									keywordParam, geometryParam);
							break;
						case "rky1993":
							registerResultArray = getRKY1993Items(connection, keywordParam,
									geometryParam);
							break;
						case "rky2000":
							registerResultArray = getRKY2000Items(connection, keywordParam,
									geometryParam);
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
			throw new ActionException("Error during geting registry item");
		} finally {
			try {
			connection.close();
			} catch (Exception e) {
			}
		}

		ResponseHelper.writeResponse(params, response);
	}

	private JSONObject getItemObject(IRegistryObject registryObject) {
		JSONObject item = new JSONObject();
		try {

			item.put("id", registryObject.getObjectId());
			// item.put("desc", registryObject.getObjectName());
			item.put("coordinateX", registryObject.getCentroid().getX());
			item.put("coordinateY", registryObject.getCentroid().getY());
			item.put("nbaUrl", registryObject.getNbaUrl());

			if (registryObject instanceof AncientMonumentMaintenanceItem) {
				item.put("mapLayerID", "69"); // "Oskari:MJHOITO"
			} else if (registryObject instanceof AncientMonument) {
				item.put("mapLayerID", "64"); // "Oskari:MJOHDE"
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
			e.printStackTrace();
		}
		return item;
	}
	
	private JSONArray getAncientMonumentMaintenanceItems(Connection con,
			String keyword, Geometry geometry) {

		JSONArray resultArray = new JSONArray();

		String results = "";

		AncientMonumentMaintenanceItemService svc = new AncientMonumentMaintenanceItemService(
				con);

		RegistryObjectCollection<AncientMonumentMaintenanceItem> monuments = (RegistryObjectCollection<AncientMonumentMaintenanceItem>) svc
				.findAncientMonumentMaintenanceItems(keyword, geometry);

		if (monuments != null) {
			RegistryObjectIterator iterator = (RegistryObjectIterator) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonumentMaintenanceItem monument = (AncientMonumentMaintenanceItem) iterator
							.next();
					JSONObject item = new JSONObject();
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("coordinateX", monument.getCentroid().getX());
					item.put("coordinateY", monument.getCentroid().getY());
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

		String results = "";

		AncientMonumentService svc = new AncientMonumentService(con);

		List<AncientMonument> monuments = (List<AncientMonument>) svc
				.findAncientMonuments(keyword, geometry);
		if (monuments != null) {
			for (AncientMonument monument : monuments) {
				try {
					JSONObject item = new JSONObject();
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("coordinateX", monument.getCentroid().getX());
					item.put("coordinateY", monument.getCentroid().getY());
					item.put("nbaUrl", monument.getNbaUrl());
					item.put("mapLayerID", "64"); // "Oskari:MJOHDE"
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

		String results = "";

		BuildingHeritageItemService svc = new BuildingHeritageItemService(con);

		List<BuildingHeritageItem> monuments = (List<BuildingHeritageItem>) svc
				.findBuildingHeritageItems(keyword, geometry);
		if (monuments != null) {
			for (BuildingHeritageItem monument : monuments) {
				try {
					JSONObject item = new JSONObject();
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("coordinateX", monument.getCentroid().getX());
					item.put("coordinateY", monument.getCentroid().getY());
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

		String results = "";

		RKY1993Service svc = new RKY1993Service(con);

		List<RKY1993> monuments = (List<RKY1993>) svc.findRKY1993(keyword,
				geometry);
		if (monuments != null) {
			for (RKY1993 monument : monuments) {
				try {
					JSONObject item = new JSONObject();
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("coordinateX", monument.getCentroid().getX());
					item.put("coordinateY", monument.getCentroid().getY());
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

		String results = "";

		RKY2000Service svc = new RKY2000Service(con);

		RegistryObjectCollection<RKY2000> monuments = (RegistryObjectCollection<RKY2000>) svc
				.findRKY2000(keyword, geometry);

		if (monuments != null) {
			RegistryObjectIterator iterator = (RegistryObjectIterator) monuments
					.iterator();

			// List<RKY2000> monuments = (List<RKY2000>) svc
			// .findRKY2000(keyword, null);
			// for (RKY2000 monument : monuments) {
			while (iterator.hasNext()) {
				try {
					RKY2000 monument = (RKY2000) iterator.next();

					JSONObject item = new JSONObject();
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("coordinateX", monument.getCentroid().getX());
					item.put("coordinateY", monument.getCentroid().getY());
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
