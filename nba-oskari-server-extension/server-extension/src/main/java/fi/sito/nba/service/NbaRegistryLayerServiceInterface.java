package fi.sito.nba.service;

import java.util.List;

import fi.nls.oskari.service.db.BaseService;
import fi.sito.nba.model.NbaRegistryLayer;

public interface NbaRegistryLayerServiceInterface extends BaseService<NbaRegistryLayer>{
	public List<NbaRegistryLayer> findRegistryLayers();

}
