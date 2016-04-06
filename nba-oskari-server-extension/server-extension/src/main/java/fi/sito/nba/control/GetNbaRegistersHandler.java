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
import fi.sito.nba.registry.services.AncientMonumentService;
import fi.sito.nba.registry.models.AncientMonument;
import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

@OskariActionRoute("GetNbaRegisters")
public class GetNbaRegistersHandler extends RestActionHandler {

	private static final Logger LOG = LogFactory
			.getLogger(GetNbaRegistersHandler.class);

	public void preProcess(ActionParameters params) throws ActionException {
		// common method called for all request methods
		LOG.info(params.getUser(), "accessing route", getName());
	}

	@Override
	public void handleGet(ActionParameters params) throws ActionException {

		JSONArray result = new JSONArray();

		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ancientMaintenance", "Muinaisjaann. hoitorekisteri"); //Ancient Monument Maintenance
			map.put("ancientMonument", "Muinaisjäännösrekisteri"); //Ancient Monument
			map.put("buildingHeritage", "Rakennusperintorekisteri"); //Building Heritage
			map.put("rky1993", "RKY1993"); //RKY1993
			map.put("rky2000", "RKY2000"); //RKY2000

			try {

				JSONObject item = new JSONObject();
				item.put("id", "");
				item.put("name", "Kaikki"); //All
				result.put(item);

				for (Map.Entry<String, String> entry : map.entrySet()) {
					JSONObject itemX = new JSONObject();
					itemX.put("id", entry.getKey());
					itemX.put("name", entry.getValue());
					result.put(itemX);

				}
			} catch (JSONException e) {
				throw e;
			}

		} catch (Exception e) {
			throw new ActionException("Error during getting registries");
		}

		ResponseHelper.writeResponse(params, result);
	}

}
