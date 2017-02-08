update portti_wfs_layer_style set sld_style = 
replace(portti_wfs_layer_style.sld_style, 'Kohdenimi', 'ALAKOHDE_NIMI')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'subItems' and nba_registry_layers.registry_id = 2
    AND
    portti_wfs_layer_style.name = 'Näytä nimet'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
);

update portti_wfs_layer_style set sld_style = 
replace(portti_wfs_layer_style.sld_style, 'Tunnus', 'Alakohdetunnus')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'subItems' and nba_registry_layers.registry_id = 2
    AND
    portti_wfs_layer_style.name = 'Näytä tunnukset'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
);