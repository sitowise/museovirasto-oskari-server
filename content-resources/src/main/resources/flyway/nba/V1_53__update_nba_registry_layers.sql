UPDATE public.nba_registry_layers
	SET gfi_attributes= '["objectName", "projectCategorySpecification", "year", "projectCategoryCode", "projectOrganization", "stageCode", "nbaUrl", "createDate", "modifyDate"]'
	WHERE registry_id = (SELECT id FROM public.nba_registries WHERE name = 'project')
	AND item_type = 'points';
	
UPDATE public.nba_registry_layers
	SET gfi_attributes= '["objectName", "description", "projectCategorySpecification", "year", "projectCategoryCode", "projectOrganization", "stageCode", "nbaUrl", "createDate", "modifyDate", "type"]'
	WHERE registry_id = (SELECT id FROM public.nba_registries WHERE name = 'project')
	AND item_type = 'areas';