package fi.sito.nba.control;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fi.nls.oskari.util.ResponseHelper;
import fi.sito.nba.model.NbaRegistry;
import fi.sito.nba.registry.services.AncientMonumentService;
import fi.sito.nba.registry.models.AncientMonument;
import fi.sito.nba.service.NbaRegistryService;
import fi.sito.nba.service.NbaRegistryServiceInterface;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

@OskariActionRoute("GetNbaRegisters")
public class GetNbaRegistersHandler extends RestActionHandler {

	private static final Logger LOG = LogFactory
			.getLogger(GetNbaRegistersHandler.class);
	private static NbaRegistryServiceInterface registryService = new NbaRegistryService();
	private static final String PARAM_LANG = "lang";

	public void preProcess(ActionParameters params) throws ActionException {
		// common method called for all request methods
		LOG.info(params.getUser(), "accessing route", getName());
	}

	@Override
	public void handleGet(ActionParameters params) throws ActionException {

		JSONArray result = new JSONArray();
		Object response;

		try {
			String langParam = params.getHttpParam(PARAM_LANG);
			
			HashMap<String, String> map = new HashMap<String, String>();
			
			List<NbaRegistry> registries = registryService.findRegistries();
			for (NbaRegistry nbaRegistry : registries) {
				
				JSONObject localeJson = new JSONObject(nbaRegistry.getLocale());
				String registryName;
				
				if (localeJson.has(langParam)) {
					registryName = localeJson.getJSONObject(langParam).getString("name");
				} else {
					registryName = nbaRegistry.getName();
				}
				map.put(nbaRegistry.getName(), registryName);
			}
			
			try {
				//TODO move adding 'All' element to frontend
				JSONObject item = new JSONObject();
				item.put("id", "");
				if (langParam.equals("fi")) {
					item.put("name", "Kaikki");
				} else {
					item.put("name", "All");
				}
				
				result.put(item);

				for (Map.Entry<String, String> entry : map.entrySet()) {
					JSONObject itemX = new JSONObject();
					itemX.put("id", entry.getKey());
					itemX.put("name", entry.getValue());
					result.put(itemX);
				}
				
				response = result;
			} catch (JSONException e) {
				throw e;
			}

		} catch (Exception e) {
			//throw new ActionException("Error during getting registries");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			response = "Message:\n" + e.getMessage() + "\n" + "Cause:\n"
					+ e.getCause() + "\n" + "Stak trace:\n" + sw.toString();
		}

		//ResponseHelper.writeResponse(params, result);
		ResponseHelper.writeResponse(params, response);
	}

}
