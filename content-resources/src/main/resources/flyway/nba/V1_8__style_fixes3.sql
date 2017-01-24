--fix some attribute names
update portti_wfs_layer_style set sld_style = replace(sld_style, 'KOHDENIMI', 'Rakennusnimi') where id = 2;

update portti_wfs_layer_style set sld_style = replace(sld_style, 'KOHDENIMI', 'Kohdenimi') where id = 5;

update portti_wfs_layer_style set sld_style = replace(sld_style, 'KOHDEID', 'KOHDE_ID') where id = 6;

--suojellut and ei suojellut are using same styles, copy those to separate styles
--points
insert into portti_wfs_layer_style (id,name,sld_style) values 
((select max(id) + 1 from portti_wfs_layer_style), 'Ei tunnuksia', (select sld_style from portti_wfs_layer_style where id = 1));

update portti_wfs_layers_styles set wfs_layer_style_id = (select max(id) from portti_wfs_layer_style)
where wfs_layer_id = 111 and wfs_layer_style_id = 1;

insert into portti_wfs_layer_style (id,name,sld_style) values 
((select max(id) + 1 from portti_wfs_layer_style), 'Näytä nimet', (select sld_style from portti_wfs_layer_style where id = 2));

update portti_wfs_layers_styles set wfs_layer_style_id = (select max(id) from portti_wfs_layer_style)
where wfs_layer_id = 111 and wfs_layer_style_id = 2;

insert into portti_wfs_layer_style (id,name,sld_style) values 
((select max(id) + 1 from portti_wfs_layer_style), 'Näytä tunnukset', (select sld_style from portti_wfs_layer_style where id = 3));

update portti_wfs_layers_styles set wfs_layer_style_id = (select max(id) from portti_wfs_layer_style)
where wfs_layer_id = 111 and wfs_layer_style_id = 3;

--areas
insert into portti_wfs_layer_style (id,name,sld_style) values 
((select max(id) + 1 from portti_wfs_layer_style), 'Ei tunnuksia', (select sld_style from portti_wfs_layer_style where id = 4));

update portti_wfs_layers_styles set wfs_layer_style_id = (select max(id) from portti_wfs_layer_style)
where wfs_layer_id = 112 and wfs_layer_style_id = 4;

insert into portti_wfs_layer_style (id,name,sld_style) values 
((select max(id) + 1 from portti_wfs_layer_style), 'Näytä nimet', (select sld_style from portti_wfs_layer_style where id = 5));

update portti_wfs_layers_styles set wfs_layer_style_id = (select max(id) from portti_wfs_layer_style)
where wfs_layer_id = 112 and wfs_layer_style_id = 5;

insert into portti_wfs_layer_style (id,name,sld_style) values 
((select max(id) + 1 from portti_wfs_layer_style), 'Näytä tunnukset', (select sld_style from portti_wfs_layer_style where id = 6));

update portti_wfs_layers_styles set wfs_layer_style_id = (select max(id) from portti_wfs_layer_style)
where wfs_layer_id = 112 and wfs_layer_style_id = 6;

--update colors for ei suojellut
update portti_wfs_layer_style set sld_style = 
replace(portti_wfs_layer_style.sld_style, '#38A800', '#51F500')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layers_styles
    where
    portti_wfs_layers_styles.wfs_layer_id in (111,112)
);

update portti_wfs_layer_style set sld_style = 
replace(portti_wfs_layer_style.sld_style, '</TextSymbolizer>', '<VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption></TextSymbolizer>')
where
sld_style like '%TextSymbolizer%';