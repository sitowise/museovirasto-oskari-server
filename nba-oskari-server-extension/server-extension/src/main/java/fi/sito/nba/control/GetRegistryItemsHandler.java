package fi.sito.nba.control;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Set;

import fi.nls.oskari.domain.Role;
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
import fi.sito.nba.registry.models.HistoricalMunicipality;
import fi.sito.nba.registry.models.KYSItem;
import fi.sito.nba.registry.models.ProvincialMuseum;
import fi.sito.nba.registry.models.Municipality250;
import fi.sito.nba.registry.models.Region;
import fi.sito.nba.registry.models.AncientMonumentArea;
import fi.sito.nba.registry.models.AncientMonumentSubItem;
import fi.sito.nba.registry.models.BuildingHeritageItemPoint;
import fi.sito.nba.registry.models.BuildingHeritageItemArea;
import fi.sito.nba.registry.models.RKY1993Geometry;
import fi.sito.nba.registry.models.RKY2000Geometry;
import fi.sito.nba.registry.models.ProjectItemPoint;
import fi.sito.nba.registry.models.ProjectItemArea;
import fi.sito.nba.registry.models.HistoricalMunicipalityArea;
import fi.sito.nba.registry.models.Municipality250Area;
import fi.sito.nba.registry.models.RegionArea;
import fi.sito.nba.registry.services.AncientMonumentMaintenanceItemService;
import fi.sito.nba.registry.services.AncientMonumentService;
import fi.sito.nba.registry.services.BuildingHeritageItemService;
import fi.sito.nba.registry.services.RKY1993Service;
import fi.sito.nba.registry.services.RKY2000Service;
import fi.sito.nba.registry.services.WorldHeritageItemService;
import fi.sito.nba.registry.services.ProjectItemService;
import fi.sito.nba.registry.services.ResourceService;
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
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}

	private boolean isGuestUser;
	private boolean hasNbaGuestLink;

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

			isGuestUser = params.getUser().isGuest();
			hasNbaGuestLink = generateGuestLink(params.getUser());

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
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", true);
						break;
					case "ancientMaintenance":
						service = new AncientMonumentMaintenanceItemService(
								connection);
						registryItem = ((AncientMonumentMaintenanceItemService) service)
								.getAncientMonumentMaintenanceItemById(itemId);
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", true);
						break;
					case "buildingHeritage":
						service = new BuildingHeritageItemService(connection);
						registryItem = ((BuildingHeritageItemService) service)
								.getBuildingHeritageItemById(itemId, params.getUser().isGuest());
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", true);
						break;
					case "rky1993":
						service = new RKY1993Service(connection);
						registryItem = ((RKY1993Service) service)
								.getRKY1993ById(itemId);
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", false);
						break;
					case "rky2000":
						service = new RKY2000Service(connection);
						registryItem = ((RKY2000Service) service)
								.getRKY2000ById(itemId);
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", true);
						break;
					case "worldHeritage":
						service = new WorldHeritageItemService(connection);
						registryItem = ((WorldHeritageItemService) service)
								.getWorldHeritageItemAreaById(itemId);
						if (registryItem == null) {
							registryItem = ((WorldHeritageItemService) service)
									.getWorldHeritageItemPointById(itemId);
						}
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", false);
						break;
					case "project":
						service = new ProjectItemService(connection);
						registryItem = ((ProjectItemService) service)
								.getProjectItemById(itemId);
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", true);
						break;
					case "resource":
						service = new ResourceService(connection);
						// try to find registry item by different methods
						registryItem = ((ResourceService) service)
								.getHistoricalMunicipalityById(itemId);
						if (registryItem == null) {
							registryItem = ((ResourceService) service)
									.getProvincialMuseumById(itemId);
							if (registryItem == null) {
								registryItem = ((ResourceService) service)
										.getMunicipality250ById(itemId);
								if (registryItem == null) {
									registryItem = ((ResourceService) service)
											.getRegionById(itemId);
									//Do not show Kulttuuriympäristöpalevluosaston vastuuhenkilöt results for guest users
									if (registryItem == null && !isGuestUser) {
										registryItem = ((ResourceService) service)
												.getKYSItemById(itemId);
									}
								}
							}
						}
						itemObj = getItemObject(registryItem,
								filteredLayerList, params.getUser());
						itemObj.put("editable", false);
						break;
					}
					itemObj.put("registryIdentifier", registryNameParam);
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
					for (String wkt : geometryStr.split("\\|")) {
						Geometry geom = reader.read(wkt).buffer(0);
						if (geometryParam == null) {
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
										connection, keywordParam,
										geometryParam, registryLayers));
								break;
							case "ancientMaintenance":
								resultArrays
										.add(getAncientMonumentMaintenanceItems(
												connection, keywordParam,
												geometryParam, registryLayers));
								break;
							case "buildingHeritage":
								resultArrays.add(getBuildingHeritageItems(
										connection, keywordParam,
										geometryParam, registryLayers, params.getUser()));
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
										connection, keywordParam,
										geometryParam, registryLayers));
								break;
							case "project":
								resultArrays.add(getProjectItems(connection,
										keywordParam, geometryParam,
										registryLayers));
								break;
							case "resource":
								resultArrays.add(getResourceItems(connection,
										keywordParam, geometryParam,
										registryLayers, params.getUser()));
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
								keywordParam, geometryParam, registryLayers, params.getUser()));
						resultArrays.add(getRKY1993Items(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getRKY2000Items(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getWorldHeritageItems(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getProjectItems(connection,
								keywordParam, geometryParam, registryLayers));
						resultArrays.add(getResourceItems(connection,
								keywordParam, geometryParam, registryLayers,
								params.getUser()));
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

			item.put("nbaUrl", registryObject.generateNbaUrl(hasNbaGuestLink));
			item.put("itemClassName", registryObject.getClass().getSimpleName());

			JSONArray mapLayersArray = new JSONArray();
			for (NbaRegistryLayer registryLayer : registryLayers) {
				if (!(registryObject instanceof AncientMonument)
						|| (((AncientMonument) registryObject).getClassification() != null
								&& ((AncientMonument) registryObject).getClassification()
										.equals(registryLayer.getClassification()))) {
					JSONObject mapLayerObject = new JSONObject();
					mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
					mapLayerObject.put("toHighlight",
							registryLayer.getToHighlight());
					mapLayerObject.put("attribute",
							registryLayer.getMainItemIdAttr());
					mapLayerObject.put("itemType", registryLayer.getItemType());

					mapLayersArray.put(mapLayerObject);
				}
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

			//change the 'point' field to 'points' array for better handling by frontend
			if (registryObject instanceof ProjectItem){
				JSONArray pointsArray = new JSONArray();
				if (item.get("point") != null && item.get("point") != JSONObject.NULL) {
					pointsArray.put(item.get("point"));
				}
				item.put("points", pointsArray);
			}

		} catch (Exception e) {
			LOG.error(e, "Error writing JSON");
		}
		return item;
	}

	private JSONObject filterAttributes(JSONObject item, String registry,
			User user) throws JSONException {
		JSONObject ret = new JSONObject();
		Set<String> permissions = permissionsService
				.getResourcesWithGrantedPermissions("attribute", user,
						"VIEW_ATTRIBUTE");
		@SuppressWarnings("unchecked")
		Iterator<String> keys = item.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if ((registry.equals("AncientMonument") && key.equals("areas"))
					|| (registry.equals("AncientMonument") && key
							.equals("subItems"))
					|| (registry.equals("AncientMonumentMaintenanceItem") && key
							.equals("subAreas"))
					|| (registry.equals("BuildingHeritageItem") && key
							.equals("points"))
					|| (registry.equals("BuildingHeritageItem") && key
							.equals("areas"))
					|| (registry.equals("RKY1993") && key.equals("areas"))
					|| (registry.equals("RKY1993") && key.equals("lines"))
					|| (registry.equals("RKY1993") && key.equals("points"))
					|| (registry.equals("RKY2000") && key.equals("areas"))
					|| (registry.equals("RKY2000") && key.equals("lines"))
					|| (registry.equals("RKY2000") && key.equals("points"))
					|| (registry.equals("ProjectItem") && key.equals("areas"))) {
				JSONArray arr = new JSONArray();
				for (int i = 0; i < item.getJSONArray(key).length(); ++i) {
					arr.put(filterAttributes(item.getJSONArray(key)
							.getJSONObject(i), registry + "_" + key, user));
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

		String registryIdentifier = "ancientMaintenance";
		JSONArray resultArray = new JSONArray();

		AncientMonumentMaintenanceItemService svc = new AncientMonumentMaintenanceItemService(
				con);

		Iterable<AncientMonumentMaintenanceItem> monuments = svc
				.findAncientMonumentMaintenanceItems(keyword, geometry);

		if (monuments != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					registryIdentifier, registryLayers);

			RegistryObjectIterator<AncientMonumentMaintenanceItem> iterator = (RegistryObjectIterator<AncientMonumentMaintenanceItem>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonumentMaintenanceItem monument = iterator.next();
					JSONObject item = new JSONObject();
					item.put("itemClassName", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());
					item.put("editable", true);
					item.put("registryIdentifier", registryIdentifier);

					JSONArray markersCoordinates = new JSONArray();
					Geometry pointGeometry = monument.getPointGeometry();
					getCoordinates(pointGeometry, markersCoordinates);
					Geometry areaGeometry = monument.getAreaGeometry();
					getCoordinates(areaGeometry, markersCoordinates);
					item.put("markersCoordinates", markersCoordinates);

					item.put("nbaUrl", monument.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getMainItemIdAttr());
						mapLayerObject.put("itemType", registryLayer.getItemType());

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

		String registryIdentifier = "ancientMonument";
		JSONArray resultArray = new JSONArray();

		AncientMonumentService svc = new AncientMonumentService(con);

		RegistryObjectCollection<AncientMonument> monuments = (RegistryObjectCollection<AncientMonument>) svc
				.findAncientMonuments(keyword, geometry);
		if (monuments != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					registryIdentifier, registryLayers);

			RegistryObjectIterator<AncientMonument> iterator = (RegistryObjectIterator<AncientMonument>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					AncientMonument monument = (AncientMonument) iterator
							.next();

					JSONObject item = new JSONObject();
					item.put("itemClassName", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());
					item.put("editable", true);
					item.put("registryIdentifier", registryIdentifier);

					JSONArray markersCoordinates = new JSONArray();
					List<AncientMonumentArea> areas = monument.getAreas();
					for(AncientMonumentArea area : areas){
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					List<AncientMonumentSubItem> subItems = monument.getSubItems();
					for(AncientMonumentSubItem subItem : subItems){
						Geometry subItemGeometry = subItem.getGeometry();
						getCoordinates(subItemGeometry, markersCoordinates);
					}
					Geometry itemGeometry = monument.getGeometry();
					getCoordinates(itemGeometry, markersCoordinates);
					item.put("markersCoordinates", markersCoordinates);

					item.put("nbaUrl", monument.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						if (monument.getClassification() != null
								&& monument.getClassification().equals(registryLayer.getClassification())) {
							JSONObject mapLayerObject = new JSONObject();
							mapLayerObject.put("mapLayerID", registryLayer.getLayerId());
							mapLayerObject.put("toHighlight", registryLayer.getToHighlight());
							mapLayerObject.put("attribute", registryLayer.getMainItemIdAttr());
							mapLayerObject.put("itemType", registryLayer.getItemType());

							mapLayersArray.put(mapLayerObject);
						}
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
			Geometry geometry, List<NbaRegistryLayer> registryLayers, User user) {

		String registryIdentifier = "buildingHeritage";
		JSONArray resultArray = new JSONArray();

		BuildingHeritageItemService svc = new BuildingHeritageItemService(con);

		RegistryObjectCollection<BuildingHeritageItem> monuments = (RegistryObjectCollection<BuildingHeritageItem>) svc
				.findBuildingHeritageItems(keyword, geometry, user.isGuest());
		if (monuments != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					registryIdentifier, registryLayers);

			RegistryObjectIterator<BuildingHeritageItem> iterator = (RegistryObjectIterator<BuildingHeritageItem>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					BuildingHeritageItem monument = (BuildingHeritageItem) iterator
							.next();

					JSONObject item = new JSONObject();
					item.put("itemClassName", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());
					item.put("editable", true);
					item.put("registryIdentifier", registryIdentifier);

					JSONArray markersCoordinates = new JSONArray();
					List<BuildingHeritageItemPoint> points = monument.getPoints();
					for(BuildingHeritageItemPoint point : points){
						Geometry pointGeometry = point.getGeometry();
						getCoordinates(pointGeometry, markersCoordinates);
					}
					List<BuildingHeritageItemArea> areas = monument.getAreas();
					for(BuildingHeritageItemArea area : areas){
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					item.put("markersCoordinates", markersCoordinates);

					item.put("nbaUrl", monument.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getMainItemIdAttr());
						mapLayerObject.put("itemType", registryLayer.getItemType());

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

		String registryIdentifier = "rky1993";
		JSONArray resultArray = new JSONArray();

		RKY1993Service svc = new RKY1993Service(con);

		RegistryObjectCollection<RKY1993> monuments = (RegistryObjectCollection<RKY1993>) svc
				.findRKY1993(keyword, geometry);
		if (monuments != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					registryIdentifier, registryLayers);

			RegistryObjectIterator<RKY1993> iterator = (RegistryObjectIterator<RKY1993>) monuments
					.iterator();
			while (iterator.hasNext()) {
				try {
					RKY1993 monument = (RKY1993) iterator.next();

					JSONObject item = new JSONObject();
					item.put("itemClassName", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);

					JSONArray markersCoordinates = new JSONArray();
					List<RKY1993Geometry> points = monument.getPoints();
					for(RKY1993Geometry point : points){
						Geometry pointGeometry = point.getGeometry();
						getCoordinates(pointGeometry, markersCoordinates);
					}
					List<RKY1993Geometry> lines = monument.getLines();
					for(RKY1993Geometry line : lines){
						Geometry lineGeometry = line.getGeometry();
						getCoordinates(lineGeometry, markersCoordinates);
					}
					List<RKY1993Geometry> areas = monument.getAreas();
					for(RKY1993Geometry area : areas){
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					item.put("markersCoordinates", markersCoordinates);

					item.put("nbaUrl", monument.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getMainItemIdAttr());
						mapLayerObject.put("itemType", registryLayer.getItemType());

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

		String registryIdentifier = "rky2000";
		JSONArray resultArray = new JSONArray();

		RKY2000Service svc = new RKY2000Service(con);

		Iterable<RKY2000> monuments = svc.findRKY2000(keyword, geometry);

		if (monuments != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					registryIdentifier, registryLayers);

			RegistryObjectIterator<RKY2000> iterator = (RegistryObjectIterator<RKY2000>) monuments
					.iterator();

			while (iterator.hasNext()) {
				try {
					RKY2000 monument = iterator.next();

					JSONObject item = new JSONObject();
					item.put("editable", true);
					item.put("registryIdentifier", registryIdentifier);
					item.put("itemClassName", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					if (!monument.getPoints().isEmpty()) {
						item.put("desc", monument.getPoints().get(0)
								.getFeatureName());
					} else if (!monument.getLines().isEmpty()) {
						item.put("desc", monument.getLines().get(0)
								.getFeatureName());
					} else if (!monument.getAreas().isEmpty()) {
						item.put("desc", monument.getAreas().get(0)
								.getFeatureName());
					}
					item.put("municipality", monument.getMunicipalityName());

					JSONArray markersCoordinates = new JSONArray();
					List<RKY2000Geometry> points = monument.getPoints();
					for(RKY2000Geometry point : points){
						Geometry pointGeometry = point.getGeometry();
						getCoordinates(pointGeometry, markersCoordinates);
					}
					List<RKY2000Geometry> lines = monument.getLines();
					for(RKY2000Geometry line : lines){
						Geometry lineGeometry = line.getGeometry();
						getCoordinates(lineGeometry, markersCoordinates);
					}
					List<RKY2000Geometry> areas = monument.getAreas();
					for(RKY2000Geometry area : areas){
						JSONObject areaCoordinates = new JSONObject();
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					item.put("markersCoordinates", markersCoordinates);

					item.put("nbaUrl", monument.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getMainItemIdAttr());
						mapLayerObject.put("itemType", registryLayer.getItemType());

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

		String registryIdentifier = "worldHeritage";
		JSONArray resultArray = new JSONArray();

		WorldHeritageItemService svc = new WorldHeritageItemService(con);

		Iterable<WorldHeritageItem> areas = svc.findWorldHeritageItemAreas(
				keyword, geometry);
		Iterable<WorldHeritageItem> points = svc.findWorldHeritageItemPoints(
				keyword, geometry);

		List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
				registryIdentifier, registryLayers);

		if (areas != null) {
			RegistryObjectIterator<WorldHeritageItem> iterator = (RegistryObjectIterator<WorldHeritageItem>) areas
					.iterator();

			while (iterator.hasNext()) {
				try {
					WorldHeritageItem monument = iterator.next();

					JSONObject item = new JSONObject();
					item.put("itemClassName", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("municipality", monument.getMunicipalityName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);

					JSONArray markersCoordinates = new JSONArray();
					Geometry itemGeometry = monument.getGeometry();
					getCoordinates(itemGeometry, markersCoordinates);
					item.put("markersCoordinates", markersCoordinates);

					item.put("nbaUrl", monument.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getMainItemIdAttr());
						mapLayerObject.put("itemType", registryLayer.getItemType());

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
					item.put("itemClassName", monument.getClass().getSimpleName());
					item.put("id", monument.getObjectId());
					item.put("desc", monument.getObjectName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);

					JSONArray markersCoordinates = new JSONArray();
					Geometry itemGeometry = monument.getGeometry();
					getCoordinates(itemGeometry, markersCoordinates);
					item.put("markersCoordinates", markersCoordinates);


					item.put("nbaUrl", monument.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getMainItemIdAttr());
						mapLayerObject.put("itemType", registryLayer.getItemType());

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

		String registryIdentifier = "project";
		JSONArray resultArray = new JSONArray();

		ProjectItemService svc = new ProjectItemService(con);

		Iterable<ProjectItem> items = svc.findProjectItems(keyword, geometry);

		if (items != null) {

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					registryIdentifier, registryLayers);

			RegistryObjectIterator<ProjectItem> iterator = (RegistryObjectIterator<ProjectItem>) items
					.iterator();

			while (iterator.hasNext()) {
				try {
					ProjectItem item = iterator.next();

					JSONObject resultItem = new JSONObject();
					resultItem.put("itemClassName", item.getClass().getSimpleName());
					resultItem.put("id", item.getObjectId());
					resultItem.put("desc", item.getObjectName());
					resultItem.put("municipality", item.getMunicipalityName());
					resultItem.put("editable", true);
					resultItem.put("registryIdentifier", registryIdentifier);

					JSONArray markersCoordinates = new JSONArray();
					List<ProjectItemArea> areas = item.getAreas();
					for(ProjectItemArea area : areas){
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					ProjectItemPoint point = item.getPoint();
					if (point != null){
						Geometry pointGeometry = point.getGeometry();
						getCoordinates(pointGeometry, markersCoordinates);
					}
					resultItem.put("markersCoordinates", markersCoordinates);

					resultItem.put("nbaUrl", item.generateNbaUrl(hasNbaGuestLink));
					JSONArray mapLayersArray = new JSONArray();
					for (NbaRegistryLayer registryLayer : filteredLayers) {
						JSONObject mapLayerObject = new JSONObject();
						mapLayerObject.put("mapLayerID",
								registryLayer.getLayerId());
						mapLayerObject.put("toHighlight",
								registryLayer.getToHighlight());
						mapLayerObject.put("attribute",
								registryLayer.getMainItemIdAttr());
						mapLayerObject.put("itemType", registryLayer.getItemType());

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

	private JSONArray getResourceItems(Connection con, String keyword,
			Geometry geometry, List<NbaRegistryLayer> registryLayers, User user) {

		String registryIdentifier = "resource";
		JSONArray resultArray = new JSONArray();

		try {
			ResourceService svc = new ResourceService(con);

			Iterable<HistoricalMunicipality> historicalMunicipalities = svc
					.findHistoricalMunicipality(keyword, geometry);
			Iterable<KYSItem> kysItems = svc.findKYSItem(keyword, geometry);
			Iterable<ProvincialMuseum> provincialMuseums = svc
					.findProvincialMuseum(keyword, geometry);
			Iterable<Municipality250> municipalities250 = svc
					.findMunicipality250(keyword, geometry);
			Iterable<Region> regions = svc.findRegion(keyword, geometry);

			List<NbaRegistryLayer> filteredLayers = getRegistryLayers(
					registryIdentifier, registryLayers);

			if (historicalMunicipalities != null) {
				RegistryObjectIterator<HistoricalMunicipality> iterator = (RegistryObjectIterator<HistoricalMunicipality>) historicalMunicipalities
						.iterator();

				while (iterator.hasNext()) {
					HistoricalMunicipality hm = iterator.next();
					JSONObject item = getItemObject(hm, filteredLayers, user);

					JSONArray markersCoordinates = new JSONArray();
					List<HistoricalMunicipalityArea> areas = hm.getAreas();
					for(HistoricalMunicipalityArea area : areas){
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					item.put("markersCoordinates", markersCoordinates);

					item.put("desc", hm.getObjectName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);
					resultArray.put(item);
				}
			}

			if (kysItems != null && !isGuestUser) {
				RegistryObjectIterator<KYSItem> iterator = (RegistryObjectIterator<KYSItem>) kysItems
						.iterator();

				while (iterator.hasNext()) {
					KYSItem kys = iterator.next();
					JSONObject item = getItemObject(kys, filteredLayers, user);

					JSONArray markersCoordinates = new JSONArray();
					Geometry itemGeometry = kys.getGeometry();
					getCoordinates(itemGeometry, markersCoordinates);
					item.put("markersCoordinates", markersCoordinates);

					item.put("desc", kys.getObjectName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);
					resultArray.put(item);
				}
			}

			if (provincialMuseums != null) {
				RegistryObjectIterator<ProvincialMuseum> iterator = (RegistryObjectIterator<ProvincialMuseum>) provincialMuseums
						.iterator();

				while (iterator.hasNext()) {
					ProvincialMuseum museum = iterator.next();
					JSONObject item = getItemObject(museum, filteredLayers,
							user);

					JSONArray markersCoordinates = new JSONArray();
					Geometry itemGeometry = museum.getGeometry();
					getCoordinates(itemGeometry, markersCoordinates);
					item.put("markersCoordinates", markersCoordinates);

					item.put("desc", museum.getObjectName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);
					resultArray.put(item);
				}
			}

			if (municipalities250 != null) {
				RegistryObjectIterator<Municipality250> iterator = (RegistryObjectIterator<Municipality250>) municipalities250
						.iterator();

				while (iterator.hasNext()) {
					Municipality250 municipality250 = iterator.next();
					JSONObject item = getItemObject(municipality250,
							filteredLayers, user);

					JSONArray markersCoordinates = new JSONArray();
					List<Municipality250Area> areas = municipality250.getAreas();
					for(Municipality250Area area : areas){
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					item.put("markersCoordinates", markersCoordinates);

					item.put("desc", municipality250.getObjectName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);
					resultArray.put(item);
				}
			}

			if (regions != null) {
				RegistryObjectIterator<Region> iterator = (RegistryObjectIterator<Region>) regions
						.iterator();

				while (iterator.hasNext()) {
					Region region = iterator.next();
					JSONObject item = getItemObject(region, filteredLayers,
							user);

					JSONArray markersCoordinates = new JSONArray();
					List<RegionArea> areas = region.getAreas();
					for(RegionArea area : areas){
						Geometry areaGeometry = area.getGeometry();
						getCoordinates(areaGeometry, markersCoordinates);
					}
					item.put("markersCoordinates", markersCoordinates);

					item.put("desc", region.getObjectName());
					item.put("editable", false);
					item.put("registryIdentifier", registryIdentifier);
					resultArray.put(item);
				}
			}

		} catch (JSONException e) {
			LOG.error(e, "Error writing JSON");
		}

		return resultArray;
	}

	private void getCoordinates(Geometry geometry, JSONArray markersCoordinates) throws JSONException {
		if(geometry == null)
			return;

		int numberOfGeometries = geometry.getNumGeometries();
		for (int i = 0; i < numberOfGeometries ; i++){
			Geometry singleGeometry = geometry.getGeometryN(i);
			if (singleGeometry == null)
				continue;
			Point centroid = singleGeometry.getCentroid();
			if (centroid == null)
				continue;

			JSONObject coordinates = new JSONObject();
			coordinates.put("coordinateX", centroid.getX());
			coordinates.put("coordinateY", centroid.getY());
			markersCoordinates.put(coordinates);
		}
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

	private boolean generateGuestLink(User user) {
		if (isGuestUser) {
			return true;
		}
		String[] rolesForGuestLink = PropertyUtil.getCommaSeparatedList("nba.guestlink.roles");

		final List<String> rolesToCheck = Arrays.asList(rolesForGuestLink);
		for (Role r : user.getRoles()) {
			if (rolesToCheck.contains(r.getName())) {
				return true;
			}
		}
		return false;
	}

}
