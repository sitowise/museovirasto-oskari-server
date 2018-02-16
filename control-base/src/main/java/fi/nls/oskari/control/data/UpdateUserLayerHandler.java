package fi.nls.oskari.control.data;

import java.io.File;

import org.deegree.portal.cataloguemanager.model.UseLimitation;
import org.json.JSONObject;

import fi.mml.map.mapwindow.util.OskariLayerWorker;
import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.domain.map.userlayer.UserLayer;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.map.userlayer.service.UserLayerDataService;
import fi.nls.oskari.map.userlayer.service.UserLayerDbService;
import fi.nls.oskari.map.userlayer.service.UserLayerDbServiceMybatisImpl;
import fi.nls.oskari.util.ConversionHelper;
import fi.nls.oskari.util.JSONHelper;
import fi.nls.oskari.util.ResponseHelper;

import javax.servlet.http.HttpServletResponse;

@OskariActionRoute("UpdateUserLayer")
public class UpdateUserLayerHandler extends ActionHandler {
	
	private static final Logger log = LogFactory
            .getLogger(CreateUserLayerHandler.class);
    private final UserLayerDbService userLayerDbService = new UserLayerDbServiceMybatisImpl();
    private final UserLayerDataService userlayerDataService = new UserLayerDataService();
    private final static String PARAM_ID = "id";
    private final static String PARAM_DESCRIPTION = "description";
    private final static String PARAM_DATASOURCE = "datasource";
	
	@Override
    public void handleAction(ActionParameters params) throws ActionException {

        // stop here if user isn't logged in
        params.requireLoggedInUser();

        try {
        	
            String idParam = params.getHttpParam(PARAM_ID);
            Long id = ConversionHelper.getLong(idParam.replace("userlayer_", ""), -1);
            
            String description = params.getHttpParam(PARAM_DESCRIPTION);
            String datasource = params.getHttpParam(PARAM_DATASOURCE);

            // Get user layer from DB and update it
            UserLayer uLayer = userLayerDbService.getUserLayerById(id);
            uLayer.setLayer_desc(description);
            uLayer.setLayer_source(datasource);
            
            userLayerDbService.updateUserLayerCols(uLayer);
            
            String bounds = userLayerDbService.getUserLayerBounds(id);
            uLayer.setBounds(bounds);

            JSONObject response = userlayerDataService.parseUserLayer2JSON(uLayer);
            JSONObject permissions = OskariLayerWorker.getAllowedPermissions();
            JSONHelper.putValue(response, "permissions", permissions);
            
            ResponseHelper.writeResponse(params, response);

        } catch (Exception e) {
            throw new ActionException("Couldn't update the user layer",
                    e);
        }

    }
}
