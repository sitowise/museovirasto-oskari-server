package fi.sito.nba.service;

import java.util.List;

import fi.nls.oskari.service.db.BaseService;
import fi.sito.nba.model.NbaRegistry;

public interface NbaRegistryServiceInterface extends BaseService<NbaRegistry>{
	public List<NbaRegistry> findRegistries();
}
