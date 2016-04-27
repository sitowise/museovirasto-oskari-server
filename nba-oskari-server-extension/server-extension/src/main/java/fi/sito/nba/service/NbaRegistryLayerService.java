package fi.sito.nba.service;

import java.io.Reader;
import java.util.List;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.sqlmap.client.SqlMapSession;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.service.db.BaseIbatisService;
import fi.sito.nba.model.NbaRegistryLayer;
import fi.sito.nba.service.NbaRegistryLayerServiceInterface;

public class NbaRegistryLayerService extends BaseIbatisService<NbaRegistryLayer> implements NbaRegistryLayerServiceInterface{

	private final static Logger log = LogFactory.getLogger(NbaRegistryService.class);
	private SqlMapClient client = null;
	
	@Override
	public List<NbaRegistryLayer> findRegistryLayers() {
		List<NbaRegistryLayer> registries = queryForList(getNameSpace() + ".findRegistryLayers");
		return registries;
	}
	
	@Override
	protected String getNameSpace() {
		return "NbaRegistries";
	}
	
	protected String getSqlMapLocation() {
		return "META-INF/SqlMapConfig-nba.xml";
	}
	
	@Override
	protected SqlMapClient getSqlMapClient() {
		if (client != null) {
            return client;
        }

        Reader reader = null;
        try {
            String sqlMapLocation = getSqlMapLocation();
            reader = Resources.getResourceAsReader(sqlMapLocation);
            client = SqlMapClientBuilder.buildSqlMapClient(reader);
            return client;
        } catch (Exception e) {
        	log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Failed to retrieve SQL client", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
	}
}
