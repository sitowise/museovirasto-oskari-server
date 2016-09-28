package fi.sito.nba.control;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

import fi.mml.portti.service.db.permissions.PermissionsService;
import fi.mml.portti.service.db.permissions.PermissionsServiceIbatisImpl;
import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.RestActionHandler;
import fi.nls.oskari.domain.User;
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
import fi.sito.nba.registry.models.WorldHeritageItem;
import fi.sito.nba.registry.models.ProjectItem;
import fi.sito.nba.registry.services.AncientMonumentMaintenanceItemService;
import fi.sito.nba.registry.services.AncientMonumentService;
import fi.sito.nba.registry.services.BuildingHeritageItemService;
import fi.sito.nba.registry.services.RKY1993Service;
import fi.sito.nba.registry.services.RKY2000Service;
import fi.sito.nba.registry.services.WorldHeritageItemService;
import fi.sito.nba.registry.services.ProjectItemService;
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
	private static PermissionsService permissionsService = new PermissionsServiceIbatisImpl();

	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.registerModule(new JsonOrgModule());
	}

	@Override
	public void handlePost(ActionParameters params) throws ActionException {
		handleGet(params);
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

			// get configuration of registry layers from DB
			List<NbaRegistryLayer> registryLayers = registryLayerService
					.findRegistryLayers();

			String registriesParam = "";
			String keywordParam = null;
			Geometry geometryParam = null;
			String itemIdParam = "";
			String registryNameParam = "";
			JSONArray results = new JSONArray();

			if (params.getHttpParam(PARAM_ITEM_ID) != null
					&& !params.getHttpParam(PARAM_ITEM_ID).equals("")
					&& params.getHttpParam(PARAM_REGISTER_NAME) != null
					&& !params.getHttpParam(PARAM_REGISTER_NAME).equals("")) {

				itemIdParam = params.getHttpParam(PARAM_ITEM_ID);
				String[] itemIds = itemIdParam.split(",");

				for (String id : itemIds) {

					int itemId = Integer.parseInt(id);

					registryNameParam = params
							.getHttpParam(PARAM_REGISTER_NAME);

					// filter list of layers
					List<NbaRegistryLayer> filteredLayerList = getRegistryLayers(
							registryNameParam, registryLayers);

					JSONObject itemObj = new JSONObject();
					Object service = null;
					IRegistryObject registryItem = null;

					switch (registryNameParam) {
					case "ancientMonument":
						service = new AncientMonumentService(connection);
						registryItem = ((AncientMonumentService) service)
								.getAncientMonumentById(itemId);
						itemObj = getItemObject(registryItem, filteredLayerList,
								params.getUser());
						break;
					case "ancientMaintenance":
						service = new AncientMonumentMaintenanceItemService(
								connection);
						registryItem = ((AncientMonumentMaintenanceItemService) service)
								.getAncientMonumentMaintenanceItemById(itemId);
						itemObj = getItemObject(registryItem, filteredLayerList,
								params.getUser());
						break;
					case "buildingHeritage":
						service = new BuildingHeritageItemService(connection);
						registryItem = ((BuildingHeritageItemService) service)
								.getBuildingHeritageItemById(itemId);
						itemObj = getItemObject(registryItem, filteredLayerList,
								params.getUser());
						break;
					case "rky1993":
						service = new RKY1993Service(connection);
						registryItem = ((RKY1993Service) service)
								.getRKY1993ById(itemId);
						itemObj = getItemObject(registryItem, filteredLayerList,
								params.getUser());
						break;
					case "rky2000":
						service = new RKY2000Service(connection);
						registryItem = ((RKY2000Service) service)
								.getRKY2000ById(itemId);
						itemObj = getItemObject(registryItem, filteredLayerList,
								params.getUser());
						break;
					case "worldHeritage":
						service = new WorldHeritageItemService(connection);
						registryItem = ((WorldHeritageItemService) service)
								.getWorldHeritageItemAreaById(itemId);
						if (registryItem == null) {
							registryItem = ((WorldHeritageItemService) service)
									.getWorldHeritageItemPointById(itemId);
						}
						itemObj = getItemObject(registryItem, filteredLayerList,
								params.getUser());
						break;
					case "project":
						service = new ProjectItemService(connection);
						registryItem = ((ProjectItemService) service).getProjectItemById(itemId);
						itemObj = getItemObject(registryItem, filteredLayerList, params.getUser());
						break;
					}
					results.put(itemObj);
				}

				if (itemIds.length == 1) {
					response = results.opt(0);
				} else {
					response = results;
				}
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
					GeometryFactory geomFactory = new GeometryFactory(
							new PrecisionModel(10));
					WKTReader reader = new WKTReader(geomFactory);
					for(String wkt : geometryStr.split("\\|")) {
						Geometry geom = reader.read(wkt).buffer(0);
						if(geometryParam == null) {
							geometryParam = geom;
						} else {
							geometryParam = geometryParam.union(geom);
						}
					}
				}

				List<JSONArray> resultArrays = new ArrayList<JSONArray>();

				String[] registries = registriesParam.split(",");

				if ((keywordParam != null && keywordParam.length() > 0)
						|| geometryParam != null) {

					if (registries[0].length() > 0) {

						for (String registry : registries) {
							switch (registry) {
							case "ancientMonument":
								resultArrays.add(getAncientMonumentItems(
										connection, keywordParam, geometryParam,
										registryLayers));
								break;
							case "ancientMaintenance":
								resultArrays
										.add(getAncientMonumentMaintenanceItems(
												connection, keywordParam,
												geometryParam, registryLayers));
								break;
							case "buildingHeritage":
								resultArrays.add(getBuildingHeritageItems(
										connection, keywordParam, geometryParam,
										registryLayers));
								break;
							case "rky1993":
								resultArrays.add(getRKY1993Items(connection,
										keywordParam, geometryParam,
										registryLayers));
								break;
							case "rky2000":
								resultArrays.add(getRKY2000Items(connection,
										keywordParam, geometryParam,
										registryLayers));
								break;
							case "worldHeritage":
								resultArrays.add(getWorldHeritageItems(
										connection, keywordParam, geometryParam,
										registryLayers));
								break;
							case "project":
								resultArrays.add(getProjectItems(connection, keywordParam, geometryParam, registryLayers));
								break;
							}
						}
					} else {
						resultArrays.add(getAncientMonumentItems(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getAncientMonumentMaintenanceItems(
								connection, keywordParam, geometryParam,
								registryLayers));
						resultArrays.add(getBuildingHeritageItems(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getRKY1993Items(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getRKY2000Items(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getWorldHeritageItems(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getProjectItems(connection,
								keywordParam, geometryParam, registryLayers));
					}
				}

				JSONArray resultArray = new JSONArray();

				for (JSONArray arr : resultArrays) {
					for (int i = 0; i < arr.length(); ++i) {
						resultArray.put(arr.get(i));
					}
				}

				response = resultArray;
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

	private JSONObject getItemObject(IRegistryObject registryObject,
			List<NbaRegistryLayer> registryLayers, User user) {
		JSONObject item = null;

		if (registryObject == null) {
			return item;
		}

		try {
			item = filterAttributes(
					mapper.convertValue(registryObject, JSONObject.class),
					registryObject.getClass().getSimpleName(), user);
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
				mapLayerObject.put("toHighlight",
						registryLayer.getToHighlight());
				mapLayerObject.put("attribute",
						registryLayer.getItemIdAttribute());

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

	private JSONObject filterAttributes(JSONObject item, String registry,
			User user) throws JSONException {
		JSONObject ret = new JSONObject();
		List<String> permissions = permissionsService
				.getResourcesWithGrantedPermissions("attribute", user,
						"VIEW_ATTRIBUTE");
		@SuppressWarnings("unchecked")
		Iterator<String> keys = item.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if ((registry.equals("AncientMonument") && key.equals("areas"))
					|| (registry.equals("AncientMonument")
							&& key.equals("subItems"))
					|| (registry.equals("AncientMonumentMaintenanceItem")
							&& key.equals("subAreas"))
					|| (registry.equals("BuildingHeritageItem")
							&& key.equals("points"))
					|| (registry.equals("BuildingHeritageItem")
							&& key.equals("areas"))
					|| (registry.equals("RKY1993") && key.equals("areas"))
					|| (registry.equals("RKY1993") && key.equals("lines"))
					|| (registry.equals("RKY1993") && key.equals("points"))
					|| (registry.equals("RKY2000") && key.equals("areas"))
					|| (registry.equals("RKY2000") && key.equals("lines"))
					|| (registry.equals("RKY2000") && key.equals("points"))
					|| (registry.equals("ProjectItem") && key.equals("areas"))
					|| (registry.equals("ProjectItem") && key.equals("points"))) {
				JSONArray arr = new JSONArray();
				for (int i = 0; i < item.getJSONArray(key).length(); ++i) {
					arr.put(filterAttributes(
							item.getJSONArray(key).getJSONObject(i),
							registry + "_" + key, user));
				}
				ret.put(key, arr);
			} else {
				if (permissions.contains(registry + "+" + key)) {
					ret.put(key, item.get(key));
				} else {
					ret.put("filtered", true);
					LOG.debug("Dropping", registry, "attribute", key,
							"for user", user.getScreenname());
				}
			}
		}
		return ret;
	}

	private JSONArray getAncientMonumentMaintenanceItems(Connection con,
			String keyword, Geometry geometry,
			List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		AncientMonumentMaintenanceItemService svc = new AncientMonumentMaintenanceItemService(
				con);

		Iterable<AncientMonumentMaintenanceItem> monuments = svc
				.findAncientMonumentMaintenanceItems(keyword, geometry);

		if (monuments != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					"ancientMaintenance", registryLayers);

			RegistryObjectIterator<AncientMonumentMaintenanceItem> iterator = (RegistryObjectIterator<AncientMonumentMaintenanceItem>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonumentMaintenanceItem monument = iterator.next();
					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());

					Point centroid = monument.calculateCentroid();

					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}

					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);

					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
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

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					"ancientMonument", registryLayers);

			RegistryObjectIterator<AncientMonument> iterator = (RegistryObjectIterator<AncientMonument>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonument monument = (AncientMonument) iterator
							.next();

					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());

					Point centroid = monument.calculateCentroid();

					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}

					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);

					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
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

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					"buildingHeritage", registryLayers);

			RegistryObjectIterator<BuildingHeritageItem> iterator = (RegistryObjectIterator<BuildingHeritageItem>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					BuildingHeritageItem monument = (BuildingHeritageItem) iterator
							.next();

					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());

					Point centroid = monument.calculateCentroid();

					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}

					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);

					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
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

		RegistryObjectCollection<RKY1993> monuments = (RegistryObjectCollection<RKY1993>) svc
				.findRKY1993(keyword, geometry);
		if (monuments != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("rky1993",
					registryLayers);

			RegistryObjectIterator<RKY1993> iterator = (RegistryObjectIterator<RKY1993>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					RKY1993 monument = (RKY1993) iterator.next();

					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());

					Point centroid = monument.calculateCentroid();

					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}

					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);

					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
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

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("rky2000",
					registryLayers);

			RegistryObjectIterator<RKY2000> iterator = (RegistryObjectIterator<RKY2000>) monuments
					.iterator();

			while (iterator.hasNext()) {
				try {
					RKY2000 monument = iterator.next();

					JSONObject item = new JSONObject();
					item.put("itemtype", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					if (!monument.getPoints().isEmpty()) {
						item.put("desc",
								monument.getPoints().get(0).getObjectName());
					} else if (!monument.getLines().isEmpty()) {
						item.put("desc",
								monument.getLines().get(0).getObjectName());
					} else if (!monument.getAreas().isEmpty()) {
						item.put("desc",
								monument.getAreas().get(0).getObjectName());
					}
					item.put("municipality", monument.getMunicipalityName());

					Point centroid = monument.calculateCentroid();
					if (centroid != null) {
						item.put("coordinateX", centroid.getX());
						item.put("coordinateY", centroid.getY());
					}

					item.put("nbaUrl", monument.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);

					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
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

	private JSONArray getWorldHeritageItems(Connection con, String keyword,
			Geometry geometry, List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		WorldHeritageItemService svc = new WorldHeritageItemService(con);

		Iterable<WorldHeritageItem> areas = svc
				.findWorldHeritageItemAreas(keyword, geometry);
		Iterable<WorldHeritageItem> points = svc
				.findWorldHeritageItemPoints(keyword, geometry);

		List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
				"worldHeritage", registryLayers);

		if (areas != null) {
			RegistryObjectIterator<WorldHeritageItem> iterator = (RegistryObjectIterator<WorldHeritageItem>) areas
					.iterator();

			while (iterator.hasNext()) {
				try {
					WorldHeritageItem monument = iterator.next();

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
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);

					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
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

		if (points != null) {
			RegistryObjectIterator<WorldHeritageItem> iterator = (RegistryObjectIterator<WorldHeritageItem>) points
					.iterator();

			while (iterator.hasNext()) {
				try {
					WorldHeritageItem monument = iterator.next();

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
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					item.put("mapLayers", mapLayersArray);

					Geometry envelope = monument.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
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
	
	private JSONArray getProjectItems(Connection con, String keyword,
			Geometry geometry, List<NbaRegistryLayer> registryLayers) {

		JSONArray resultArray = new JSONArray();

		ProjectItemService svc = new ProjectItemService(con);

		Iterable<ProjectItem> items = svc.findProjectItems(keyword, geometry);

		if (items != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers("project",
					registryLayers);

			RegistryObjectIterator<ProjectItem> iterator = (RegistryObjectIterator<ProjectItem>) items.iterator();

			while (iterator.hasNext()) {
				try {
					ProjectItem item = iterator.next();

					JSONObject resultItem = new JSONObject();
					resultItem.put("itemtype", item.getClass().getSimpleName());
					resultItem.put("id", item.getObjectId());
					resultItem.put("desc", item.getObjectName());
					resultItem.put("municipality", item.getMunicipalityName());

					Point centroid = item.calculateCentroid();
					if (centroid != null) {
						resultItem.put("coordinateX", centroid.getX());
						resultItem.put("coordinateY", centroid.getY());
					}

					resultItem.put("nbaUrl", item.generateNbaUrl());
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getItemIdAttribute());

						mapLayersArray.put(mapLayerObject);
					}
					resultItem.put("mapLayers", mapLayersArray);

					Geometry envelope = item.calculateEnvelope();
					if (envelope != null) {
						Envelope envelopeInternal = envelope
								.getEnvelopeInternal();
						JSONArray bounds = new JSONArray();
						bounds.put(envelopeInternal.getMinX());
						bounds.put(envelopeInternal.getMinY());
						bounds.put(envelopeInternal.getMaxX());
						bounds.put(envelopeInternal.getMaxY());
						resultItem.put("bounds", bounds);
					}

					resultArray.put(resultItem);
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return resultArray;
	}

	private List<NbaRegistryLayer> getRegistryLayers(String registryName,
			List<NbaRegistryLayer> registryLayers) {
		// filter list of layers
		List<NbaRegistryLayer> filteredLayerList = new ArrayList<>();
		for (NbaRegistryLayer nbaRegistryLayer : registryLayers) {
			if (nbaRegistryLayer.getRegistryName().equals(registryName)) {
				filteredLayerList.add(nbaRegistryLayer);
			}
		}

		return filteredLayerList;
	}

}
