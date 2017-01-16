update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#828282</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layers_styles, portti_wfs_layer
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
	AND
	portti_wfs_layer.layer_name = 'WFS_MJrekisteri_kaikkitasot_WFS:mjalakohde_poistetut'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#828282</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layers_styles, portti_wfs_layer
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
	AND
	portti_wfs_layer.layer_name = 'WFS_MJrekisteri_kaikkitasot_WFS:mjpiste_poistetut'
);

update portti_wfs_layer_style set sld_style = 
regexp_replace(portti_wfs_layer_style.sld_style, '<CssParameter name="fill">#[\dA-F]+</CssParameter>', '<CssParameter name="fill">#FF00FF</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layers_styles, portti_wfs_layer
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
	AND
	portti_wfs_layer.layer_name ilike '%mahdolliset%'
);