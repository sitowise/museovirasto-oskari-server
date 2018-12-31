package downloadbasket.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import downloadbasket.helpers.OGCServices;
import org.json.JSONException;
/*import downloadbasket.helpers.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;*/

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
import java.net.URLEncoder;

/**
 * Handles the cropping of the data before adding it to the download basket.
 * Gets layer attributes and geometry for the cropping process.
 * Added support for authorized cropping areas.
 */

@OskariActionRoute("GetFeatureForCropping")
public class GetFeatureForCropping extends ActionHandler {

	private final Logger LOGGER = LogFactory.getLogger(GetFeatureForCropping.class);

	private static final String PARAM_LAYERS = "layers";
	private static final String PARAM_X = "x";
	private static final String PARAM_Y = "y";
	private static final String PARAM_BBOX = "bbox";
	private static final String PARAM_WIDTH = "width";
	private static final String PARAM_HEIGHT = "height";
	private static final String PARAM_SRS = "srs";
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
		String data;

		LOGGER.debug("Details of the data cropping feature");
		try {
			StringBuilder s = new StringBuilder();
			s.append(OGCServices.doGetFeatureUrl(srs, oskariLayer, true, url));
			s.append("&maxFeatures=1");
			s.append("&filter=");
			s.append(URLEncoder.encode("<ogc:Filter><ogc:Contains><ogc:PropertyName>SHAPE</ogc:PropertyName><gml:Point srsName=\"urn:ogc:def:crs:EPSG::3067\"><gml:coordinates>"+params.getHttpParam(PARAM_Y)+","+params.getHttpParam(PARAM_X)+"</gml:coordinates></gml:Point></ogc:Contains></ogc:Filter>", "utf-8"));
			String featureUrl = s.toString();
			HttpURLConnection con = IOHelper.getConnection(featureUrl, oskariLayer.getUsername(), oskariLayer.getPassword());
			con.setRequestProperty("Accept-Charset", "UTF-8");
			data = IOHelper.readString(con, "UTF-8");
			ResponseHelper.writeResponse(params, data);
		} catch (JSONException e) {
			throw new ActionException("Could not get cropping:", e);
		} catch (MalformedURLException e) {
			throw new ActionException("Could not get cropping:", e);
		} catch (IOException e) {
			throw new ActionException("Could not get cropping:", e);
		}
		/* Upgrade V1.49.0 version has this below, we can check if it's an improvement
		
		    throw new ActionParamsException("Layer not found, id: " + id);
		}
		
			String url = oskariLayer.getUrl();

			String wmsUrl = Helpers.getGetFeatureInfoUrlForProxy(url, params.getHttpParam(PARAM_SRS),
					params.getHttpParam(PARAM_BBOX), params.getHttpParam(PARAM_WIDTH),
					params.getHttpParam(PARAM_HEIGHT), params.getHttpParam(PARAM_X), params.getHttpParam(PARAM_Y),
					params.getHttpParam(PARAM_LAYERS));

			LOGGER.debug("Details of the data cropping feature");
			try {

				HttpURLConnection con = IOHelper.getConnection(wmsUrl, oskariLayer.getUsername(), oskariLayer.getPassword());
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
			}*/

	}

	@Override
	public void init() {
		super.init();

		mapLayerService = new OskariLayerServiceIbatisImpl();
	}
}
