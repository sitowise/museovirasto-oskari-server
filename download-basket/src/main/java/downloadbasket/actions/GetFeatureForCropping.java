package downloadbasket.actions;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;

import downloadbasket.helpers.OGCServices;
import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.ActionParamsException;
import fi.nls.oskari.domain.map.OskariLayer;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.map.layer.OskariLayerService;
import fi.nls.oskari.map.layer.OskariLayerServiceIbatisImpl;
import fi.nls.oskari.util.ResponseHelper;
import fi.nls.oskari.util.IOHelper;
import java.net.HttpURLConnection;

/**
 * Handles the cropping of the data before adding it to the download basket.
 * Gets layer attributes and geometry for the cropping process.
 * Added support for authorized cropping areas.
 */

@OskariActionRoute("GetFeatureForCropping")
public class GetFeatureForCropping extends ActionHandler {

	private final Logger LOGGER = LogFactory.getLogger(GetFeatureForCropping.class);

	private static final String PARAM_X = "x";
	private static final String PARAM_Y = "y";
	private static final String PARAM_ID = "id";

	private OskariLayerService mapLayerService;

	@Override
	public void handleAction(final ActionParameters params) throws ActionException {
		final int id = params.getRequiredParamInt(PARAM_ID);
		final OskariLayer oskariLayer = mapLayerService.find(id);
		if (oskariLayer == null) {
			throw new ActionParamsException("Layer not found, id: " + id);
		}

		String url = oskariLayer.getUrl();
		String srs = oskariLayer.getSrs_name();

		LOGGER.debug("Details of the data cropping feature");
		try {
			StringBuilder s = new StringBuilder();
			s.append(OGCServices.doGetFeatureUrl(srs, oskariLayer, true, url, "2.0.0"));
			s.append("&outputFormat=GEOJSON");
			s.append("&maxFeatures=1");
			s.append("&filter=");
			s.append(URLEncoder.encode("<ogc:Filter><ogc:Contains><ogc:PropertyName>SHAPE</ogc:PropertyName><gml:Point srsName=\"urn:ogc:def:crs:EPSG::3067\"><gml:coordinates>"+params.getHttpParam(PARAM_Y)+","+params.getHttpParam(PARAM_X)+"</gml:coordinates></gml:Point></ogc:Contains></ogc:Filter>", "utf-8"));
			String featureUrl = s.toString();
			HttpURLConnection con = IOHelper.getConnection(featureUrl, oskariLayer.getUsername(), oskariLayer.getPassword());
			con.setRequestProperty("Accept-Charset", "UTF-8");
			final String data = IOHelper.readString(con, "UTF-8");

			JSONObject json = new JSONObject(data);

			ResponseHelper.writeResponse(params, json);

		} catch (JSONException e) {
			throw new ActionException("Could not get cropping:", e);
		} catch (MalformedURLException e) {
			throw new ActionException("Could not get cropping:", e);
		} catch (IOException e) {
			throw new ActionException("Could not get cropping:", e);
		}

	}

	@Override
	public void init() {
		super.init();

		mapLayerService = new OskariLayerServiceIbatisImpl();
	}
}