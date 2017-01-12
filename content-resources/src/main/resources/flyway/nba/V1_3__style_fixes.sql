update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<Size>\d+</Size>', '<Size>12</Size>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'main' and nba_registry_layers.registry_id = 2
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<WellKnownName>[A-Za-z]+</WellKnownName>', '<WellKnownName>star</WellKnownName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
);


update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#01C6FF</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
	AND
	portti_wfs_layer.layer_name = 'WFS_MJ_supistettu_sisalto:Luonnonmuodostumat_alakohteet'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#FF7F01</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
	AND
	portti_wfs_layer.layer_name = 'WFS_MJ_supistettu_sisalto:Loytopaikat_alakohteet'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#E60000</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
	AND
	portti_wfs_layer.layer_name = 'WFS_MJ_supistettu_sisalto:Kiinteat_muinaisjaannokset_alakohteet'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#B67F4A</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
	AND
	portti_wfs_layer.layer_name = 'WFS_MJ_supistettu_sisalto:Muut_kulttuuriperintokohteet_alakohteet'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#F30503</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
	AND
	portti_wfs_layer.layer_name = 'WFS_MJ_supistettu_sisalto:Poistetut_kiinteat_muinaisjaannokset_alakohteet'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#FF00FF</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
	AND
	portti_wfs_layer.layer_name = 'WFS_MJ_supistettu_sisalto:Mahdolliset_muinaisjaannokset_alakohteet'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#FFFFFF</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'sub' and nba_registry_layers.registry_id = 2
	AND
	portti_wfs_layer.layer_name = 'WFS_MJ_supistettu_sisalto:Muut_kohteet_alakohteet'
);