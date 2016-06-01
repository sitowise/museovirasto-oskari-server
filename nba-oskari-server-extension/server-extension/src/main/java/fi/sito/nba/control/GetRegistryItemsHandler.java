package fi.sito.nba.control;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.RestActionHandler;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.ResponseHelper;
import fi.sito.nba.model.NbaRegistryLayer;
import fi.sito.nba.registry.infrastructure.RegistryObjectCollection;
import fi.sito.nba.registry.infrastructure.RegistryObjectIterator;
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
import fi.sito.nba.service.NbaRegistryLayerService;
import fi.sito.nba.service.NbaRegistryLayerServiceInterface;

@OskariActionRoute("GetRegistryItems")
public class GetRegistryItemsHandler extends RestActionHandler {
	private static final String PARAM_REGISTER_NAME = "registerName";
	private static final String PARAM_ITEM_ID = "id";
	private static final String PARAM_REGISTRIES = "registries";
	private static final String PARAM_KEYWORD = "keyword";
	private static final String PARAM_GEOMETRY = "geometry";

	private static final Logger LOG = LogFactory
			.getLogger(GetRegistryItemsHandler.class);
	private static NbaRegistryLayerServiceInterface registryLayerService = new NbaRegistryLayerService();

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

			//get configuration of registry layers from DB
			List<NbaRegistryLayer> registryLayers = registryLayerService.findRegistryLayers();
			
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
				
				//filter list of layers
				List<NbaRegistryLayer> filteredLayerList = getRegistryLayers(registryNameParam, registryLayers);
				
				JSONObject itemObj = new JSONObject();
				Object service = null;
				IRegistryObject registryItem = null;

				switch (registryNameParam) {
				case "ancientMonument":
					service = new AncientMonumentService(connection);
					registryItem = ((AncientMonumentService) service)
							.getAncientMonumentById(itemIdParam);
					itemObj = getItemObject(registryItem, filteredLayerList);
					break;
				case "ancientMaintenance":
					service = new AncientMonumentMaintenanceItemService(
							connection);
					registryItem = ((AncientMonumentMaintenanceItemService) service)
							.getAncientMonumentMaintenanceItemById(itemIdParam);
					itemObj = getItemObject(registryItem, filteredLayerList);
					break;
				case "buildingHeritage":
					service = new BuildingHeritageItemService(connection);
					registryItem = ((BuildingHeritageItemService) service)
							.getBuildingHeritageItemById(itemIdParam);
					itemObj = getItemObject(registryItem, filteredLayerList);
					break;
				case "rky1993":
					service = new RKY1993Service(connection);
					registryItem = ((RKY1993Service) service)
							.getRKY1993ById(itemIdParam);
					itemObj = getItemObject(registryItem, filteredLayerList);
					break;
				case "rky2000":
					service = new RKY2000Service(connection);
					registryItem = ((RKY2000Service) service)
							.getRKY2000ById(itemIdParam);
					itemObj = getItemObject(registryItem, filteredLayerList);
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
									keywordParam, geometryParam, registryLayers);
							break;
						case "ancientMaintenance":
							registerResultArray = getAncientMonumentMaintenanceItems(
									connection, keywordParam, geometryParam, registryLayers);
							break;
						case "buildingHeritage":
							registerResultArray = getBuildingHeritageItems(connection,
									keywordParam, geometryParam, registryLayers);
							break;
						case "rky1993":
							registerResultArray = getRKY1993Items(connection, keywordParam,
									geometryParam, registryLayers);
							break;
						case "rky2000":
							registerResultArray = getRKY2000Items(connection, keywordParam,
									geometryParam, registryLayers);
							break;
						}

						for (int i = 0; i < registerResultArray.length(); i++) {
							generalResultArray.put(registerResultArray.get(i));
						}

					}
				} else {
					
					JSONArray registerResultArray1 = getAncientMonumentItems(
							connection, keywordParam, geometryParam, registryLayers);
					JSONArray registerResultArray2 = getAncientMonumentMaintenanceItems(
							connection, keywordParam, geometryParam, registryLayers);
					JSONArray registerResultArray3 = getBuildingHeritageItems(
							connection, keywordParam, geometryParam, registryLayers);
					JSONArray registerResultArray4 = getRKY1993Items(connection,
							keywordParam, geometryParam, registryLayers);
					JSONArray registerResultArray5 = getRKY2000Items(connection,
							keywordParam, geometryParam, registryLayers);

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

	private JSONObject getItemObject(IRegistryObject registryObject, List<NbaRegistryLayer> registryLayers) {
		JSONObject item = null;
		try {
			item = mapper.convertValue(registryObject, JSONObject.class);
			item.put("id", registryObject.getObjectId());
			
			Point centroid = registryObject.calculateCentroid(); 
			if (centroid != null) {
				item.put("coordinateX", centroid.getX());
				item.put("coordinateY", centroid.getY());
			}
			
			item.put("nbaUrl", registryObject.generateNbaUrl());
			item.put("itemtype", registryObject.getClass().getSimpleName());
			
			JSONArray mapLayersArray = new JSONArray(); 
			for (NbaRegistryLayer registryLayer : registryLayers) {
				JSONObject mapLayerObject = new JSONObject();
				mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
				mapLayerObject.put("toHighlight", registryLayer.getToHighlight());
				mapLayerObject.put("attribute", registryLayer.getItemIdAttribute());
				
				mapLayersArray.put(mapLayerObject);
			}
			item.put("mapLayers", mapLayersArray);
			
			Geometry envelope = registryObject.calculateEnvelope();
			if (envelope != null) {
				Envelope envelopeInternal = envelope.getEnvelopeInternal();
				JSONArray bounds = new JSONArray();
				bounds.put(envelopeInternal.getMinX());
				bounds.put(envelopeInternal.getMinY());
				bounds.put(envelopeInternal.getMaxX());
				bounds.put(envelopeInternal.getMaxY());
				item.put("bounds", bounds);
			}

		} catch (Exception e) {
			LOG.error(e, "Error writing JSON");
		}
		return item;
	}

	private JSONArray getAncientMonumentMaintenanceItems(Connection con,
			String keyword, Geometry geometry, List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		AncientMonumentMaintenanceItemService svc = new AncientMonumentMaintenanceItemService(
				con);

		Iterable<AncientMonumentMaintenanceItem> monuments = svc
				.findAncientMonumentMaintenanceItems(keyword, geometry);

		if (monuments != null) {
			
			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("ancientMaintenance", registryLayers);
			
			RegistryObjectIterator<AncientMonumentMaintenanceItem> iterator = (RegistryObjectIterator<AncientMonumentMaintenanceItem>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonumentMaintenanceItem monument = iterator.next();
					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());

					Point centroid = monument.calculateCentroid(); 
					
					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}
					
					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray(); 
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
						mapLayerObject.put("toHighlight", registryLayer.getToHighlight());
						mapLayerObject.put("attribute", registryLayer.getItemIdAttribute());
						
						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);
					
					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope.getEnvelopeInternal();
						JSONArray bounds = new JSONArray();
						bounds.put(envelopeInternal.getMinX());
						bounds.put(envelopeInternal.getMinY());
						bounds.put(envelopeInternal.getMaxX());
						bounds.put(envelopeInternal.getMaxY());
						item.put("bounds", bounds);
					}
					
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getAncientMonumentItems(Connection con, String keyword,
			Geometry geometry, List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		AncientMonumentService svc = new AncientMonumentService(con);

		RegistryObjectCollection<AncientMonument> monuments = (RegistryObjectCollection<AncientMonument>) svc
				.findAncientMonuments(keyword, geometry);
		if (monuments != null) {
			
			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("ancientMonument", registryLayers);
			
			RegistryObjectIterator<AncientMonument> iterator = (RegistryObjectIterator<AncientMonument>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonument monument = (AncientMonument) iterator.next();
					
					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					
					Point centroid = monument.calculateCentroid(); 
					
					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}
					
					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray(); 
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
						mapLayerObject.put("toHighlight", registryLayer.getToHighlight());
						mapLayerObject.put("attribute", registryLayer.getItemIdAttribute());
						
						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);
					
					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope.getEnvelopeInternal();
						JSONArray bounds = new JSONArray();
						bounds.put(envelopeInternal.getMinX());
						bounds.put(envelopeInternal.getMinY());
						bounds.put(envelopeInternal.getMaxX());
						bounds.put(envelopeInternal.getMaxY());
						item.put("bounds", bounds);
					}
					
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getBuildingHeritageItems(Connection con, String keyword,
			Geometry geometry, List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		BuildingHeritageItemService svc = new BuildingHeritageItemService(con);

		RegistryObjectCollection<BuildingHeritageItem> monuments = (RegistryObjectCollection<BuildingHeritageItem>) svc
				.findBuildingHeritageItems(keyword, geometry);
		if (monuments != null) {
			
			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("buildingHeritage", registryLayers);
			
			RegistryObjectIterator<BuildingHeritageItem> iterator = (RegistryObjectIterator<BuildingHeritageItem>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					BuildingHeritageItem monument = (BuildingHeritageItem) iterator.next();
					
					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());

					Point centroid = monument.calculateCentroid(); 
					
					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}
					
					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray(); 
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
						mapLayerObject.put("toHighlight", registryLayer.getToHighlight());
						mapLayerObject.put("attribute", registryLayer.getItemIdAttribute());
						
						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);
					
					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope.getEnvelopeInternal();
						JSONArray bounds = new JSONArray();
						bounds.put(envelopeInternal.getMinX());
						bounds.put(envelopeInternal.getMinY());
						bounds.put(envelopeInternal.getMaxX());
						bounds.put(envelopeInternal.getMaxY());
						item.put("bounds", bounds);
					}
					
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getRKY1993Items(Connection con, String keyword,
			Geometry geometry, List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		RKY1993Service svc = new RKY1993Service(con);

		RegistryObjectCollection<RKY1993> monuments = (RegistryObjectCollection<RKY1993>) svc.findRKY1993(keyword,
				geometry);
		if (monuments != null) {
			
			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("rky1993", registryLayers);
			
			RegistryObjectIterator<RKY1993> iterator = (RegistryObjectIterator<RKY1993>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					RKY1993 monument = (RKY1993) iterator.next();
					
					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());

					Point centroid = monument.calculateCentroid(); 
					
					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}
					
					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray(); 
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
						mapLayerObject.put("toHighlight", registryLayer.getToHighlight());
						mapLayerObject.put("attribute", registryLayer.getItemIdAttribute());
						
						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);
					
					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope.getEnvelopeInternal();
						JSONArray bounds = new JSONArray();
						bounds.put(envelopeInternal.getMinX());
						bounds.put(envelopeInternal.getMinY());
						bounds.put(envelopeInternal.getMaxX());
						bounds.put(envelopeInternal.getMaxY());
						item.put("bounds", bounds);
					}
					
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private JSONArray getRKY2000Items(Connection con, String keyword,
			Geometry geometry, List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		RKY2000Service svc = new RKY2000Service(con);

		Iterable<RKY2000> monuments = svc.findRKY2000(keyword, geometry);

		if (monuments != null) {
			
			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("rky2000", registryLayers);
			
			RegistryObjectIterator<RKY2000> iterator = (RegistryObjectIterator<RKY2000>) monuments
					.iterator();

			while (iterator.hasNext()) {
				try {
					RKY2000 monument = iterator.next();

					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					//item.put("desc", monument.getObjectName());

					Point centroid = monument.calculateCentroid();
					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}
					
					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray(); 
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
						mapLayerObject.put("toHighlight", registryLayer.getToHighlight());
						mapLayerObject.put("attribute", registryLayer.getItemIdAttribute());
						
						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);
					
					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope.getEnvelopeInternal();
						JSONArray bounds = new JSONArray();
						bounds.put(envelopeInternal.getMinX());
						bounds.put(envelopeInternal.getMinY());
						bounds.put(envelopeInternal.getMaxX());
						bounds.put(envelopeInternal.getMaxY());
						item.put("bounds", bounds);
					}
					
					resultArray.put(item);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}
	
	private List<NbaRegistryLayer> getRegistryLayers(String registryName, List<NbaRegistryLayer> registryLayers) {
		//filter list of layers
		List<NbaRegistryLayer> filteredLayerList = new ArrayList<>();
		for (NbaRegistryLayer nbaRegistryLayer : registryLayers) {
			if (nbaRegistryLayer.getRegistryName().equals(registryName)) {
				filteredLayerList.add(nbaRegistryLayer);
			}
		}
		
		return filteredLayerList;
	}

}
