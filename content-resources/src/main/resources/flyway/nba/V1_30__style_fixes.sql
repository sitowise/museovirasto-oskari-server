update portti_wfs_layer_style set sld_style = 
'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Mahdollinen muinaisjäännös (alue)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Mahdollinen muinaisjäännös (alue)</Title>
      <Abstract>Mahdollinen muinaisjäännös (alue)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
       <Rule>
        <!--FeatureTypeName>Feature</FeatureTypeName-->    
       <Name>Mahdollinen muinaisjäännös (alue)</Name>
       <Title>Mahdollinen muinaisjäännös (alue)</Title>	
       <PolygonSymbolizer>
		<Fill>
           <GraphicFill>
             <Graphic>
               <Mark>
                 <WellKnownName>shape://times</WellKnownName>
                 <Stroke>
                   <CssParameter name="stroke">#FF00FF</CssParameter>
                   <CssParameter name="stroke-width">1</CssParameter>
                 </Stroke>
               </Mark>
               <Size>16</Size>
             </Graphic>
           </GraphicFill>
	   </Fill>
         <Stroke>
           <CssParameter name="stroke">#FF00FF</CssParameter>
           <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	   
       </PolygonSymbolizer>
     </Rule>	
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>'
where
name = 'Ei tunnuksia' AND
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'areas' and nba_registry_layers.registry_id = 2
    AND
    portti_wfs_layer.layer_name ilike '%mahdolliset%'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
);

update portti_wfs_layer_style set sld_style = replace(sld_style, '<CssParameter name="fill">#FF00FF</CssParameter>', '<CssParameter name="fill">#FFFFFF</CssParameter>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'areas' and nba_registry_layers.registry_id = 2
    AND
    portti_wfs_layer.layer_name ilike '%mahdolliset%'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
);


update portti_wfs_layer_style set sld_style = 
'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Muu kulttuuriperintökohde (alue)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Muu kulttuuriperintökohde (alue)</Title>
      <Abstract>Muu kulttuuriperintökohde (alue)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->    
     <Rule>
       <Name>Muu kulttuuriperintökohde (alue)</Name>
       <Title>Muu kulttuuriperintökohde (alue)</Title>
       <PolygonSymbolizer>

		<Fill>
           <GraphicFill>
             <Graphic>
               <Mark>
                 <WellKnownName>shape://times</WellKnownName>
                 <Stroke>
                   <CssParameter name="stroke">#B67F4A</CssParameter>
                   <CssParameter name="stroke-width">1</CssParameter>
                 </Stroke>
               </Mark>
               <Size>16</Size>
             </Graphic>
           </GraphicFill>
	   </Fill>
         <Stroke>
           <CssParameter name="stroke">#B67F4A</CssParameter>
           <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	   
       </PolygonSymbolizer>
     </Rule>	 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>'
where
name = 'Ei tunnuksia' AND
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'areas' and nba_registry_layers.registry_id = 2
    AND
    portti_wfs_layer.layer_name ilike '%muut_kulttuuriperinto%'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
);


update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>KOHDE_ID</ogc:PropertyName>', '<ogc:PropertyName>id</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'areas'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky2000'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä tunnukset'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>KOHDENIMI</ogc:PropertyName>', '<ogc:PropertyName>KOHDE_NIMI</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'lines'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky2000'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä nimet'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>ID</ogc:PropertyName>', '<ogc:PropertyName>id</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'lines'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky2000'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä tunnukset'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>KOHDENIMI</ogc:PropertyName>', '<ogc:PropertyName>KOHDE_NIMI</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'points'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky2000'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä nimet'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>ID</ogc:PropertyName>', '<ogc:PropertyName>id</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'points'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky2000'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä tunnukset'
);


update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>Kohdenimi</ogc:PropertyName>', '<ogc:PropertyName>KOHDE_NIMI</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'lines'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky1993'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä nimet'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>Tunnus</ogc:PropertyName>', '<ogc:PropertyName>KOHDE_ID</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'lines'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky1993'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä tunnukset'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>Kohdenimi</ogc:PropertyName>', '<ogc:PropertyName>KOHDE_NIMI</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'points'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky1993'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä nimet'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>Tunnus</ogc:PropertyName>', '<ogc:PropertyName>KOHDE_ID</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.item_type = 'points'
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'rky1993'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä tunnukset'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>ALUE_NIMI</ogc:PropertyName>', '<ogc:PropertyName>Nimi</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'worldHeritage'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä nimet'
);

update portti_wfs_layer_style set sld_style = 
replace(sld_style, '<ogc:PropertyName>ALUE_ID</ogc:PropertyName>', '<ogc:PropertyName>ID</ogc:PropertyName>')
where
portti_wfs_layer_style.id 
in (
    select portti_wfs_layers_styles.wfs_layer_style_id from portti_wfs_layer_style, portti_wfs_layers_styles, portti_wfs_layer, nba_registry_layers, nba_registries
    where
    portti_wfs_layers_styles.wfs_layer_id = portti_wfs_layer.id
    AND
    portti_wfs_layer.maplayer_id = nba_registry_layers.layer_id
    AND
    nba_registry_layers.registry_id = nba_registries.id
    AND
    nba_registries.name = 'worldHeritage'
    AND
    portti_wfs_layers_styles.wfs_layer_style_id = portti_wfs_layer_style.id
    AND
    portti_wfs_layer_style.name = 'Näytä tunnukset'
);

