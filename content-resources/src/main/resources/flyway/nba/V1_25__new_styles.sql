SELECT setval('portti_wfs_layer_style_id_seq', (SELECT MAX(id) FROM portti_wfs_layer_style));
SELECT setval('portti_wfs_layers_styles_id_seq', (SELECT MAX(id) FROM portti_wfs_layers_styles));

--- Hankeet, pisteet, ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Hankerekisteri (piste)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Hankerekisteri (piste)</Title>
            <Abstract>Hankerekisteri (piste)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Hankerekisteri (piste)</Name>
                    <Title>Hankerekisteri (piste)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>star</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#FFA500</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 152), (select max(id) from portti_wfs_layer_style));

-- Hankkeet, pisteet, nimet
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Hankerekisteri (piste)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Hankerekisteri (piste)</Title>
            <Abstract>Hankerekisteri (piste)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Hankerekisteri (piste)</Name>
                    <Title>Hankerekisteri (piste)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>star</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#FFA500</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
                <Rule>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>Kohde</ogc:PropertyName>
                        </Label>
                        <Halo>
                            <Radius>2</Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                            </Fill>
                        </Halo>
                    <VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption></TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 152), (select max(id) from portti_wfs_layer_style));

-- Hankkeet, pisteet, tunnukset
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Hankerekisteri (piste)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Hankerekisteri (piste)</Title>
            <Abstract>Hankerekisteri (piste)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Hankerekisteri (piste)</Name>
                    <Title>Hankerekisteri (piste)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>star</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#FFA500</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
                <Rule>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>hanke_id</ogc:PropertyName>
                        </Label>
                        <Halo>
                            <Radius>2</Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                            </Fill>
                        </Halo>
                    <VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption></TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 152), (select max(id) from portti_wfs_layer_style));


-- Hanke, alue, ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Hankerekisteri (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Hankerekisteri (alue)</Title>
         <Abstract>Hankerekisteri (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <Rule>
               <!--FeatureTypeName>Feature</FeatureTypeName-->
               <Name>Hankerekisteri (alue)</Name>
               <Title>Hankerekisteri (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://times</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#FFA500</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#FFA500</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 151), (select max(id) from portti_wfs_layer_style));

-- Hanke, alue, nimi
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Hankerekisteri (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Hankerekisteri (alue)</Title>
         <Abstract>Hankerekisteri (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Hankerekisteri (alue)</Name>
               <Title>Hankerekisteri (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://times</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#FFA500</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#FFA500</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>Kohde</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 151), (select max(id) from portti_wfs_layer_style));

-- Hanke, alue, tunnus
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Hankerekisteri (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Hankerekisteri (alue)</Title>
         <Abstract>Hankerekisteri (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Hankerekisteri (alue)</Name>
               <Title>Hankerekisteri (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://times</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#FFA500</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#FFA500</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>hanke_id</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 151), (select max(id) from portti_wfs_layer_style));


-- Arvokkaat maisemat, ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Arvokkaat maisemat</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Arvokkaat maisemat</Title>
         <Abstract>Arvokkaat maisemat</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <Rule>
               <!--FeatureTypeName>Feature</FeatureTypeName-->
               <Name>Arvokkaat maisemat</Name>
               <Title>Arvokkaat maisemat</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://vertline</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#228b22</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#228b22</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 179), (select max(id) from portti_wfs_layer_style));

-- Arvokkaat maisemat, nimi
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Arvokkaat maisemat</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Arvokkaat maisemat</Title>
         <Abstract>Arvokkaat maisemat</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Arvokkaat maisemat</Name>
               <Title>Arvokkaat maisemat</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://vertline</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#228b22</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#228b22</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>NIMI</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 179), (select max(id) from portti_wfs_layer_style));

-- Arvokkaat maisemat, tunnus
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Arvokkaat maisemat</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Arvokkaat maisemat</Title>
         <Abstract>Arvokkaat maisemat</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Arvokkaat maisemat</Name>
               <Title>Arvokkaat maisemat</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://vertline</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#228b22</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#228b22</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>OBJECTID</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 179), (select max(id) from portti_wfs_layer_style));


--- Pohjois-Karjalan rakennusperinto, ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Pohjois-Karjalan rakennusperinto</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Pohjois-Karjalan rakennusperinto</Title>
            <Abstract>Pohjois-Karjalan rakennusperinto</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Pohjois-Karjalan rakennusperinto</Name>
                    <Title>Pohjois-Karjalan rakennusperinto</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>circle</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#FFC0CB</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 182), (select max(id) from portti_wfs_layer_style));

-- Pohjois-Karjalan rakennusperinto, nimet
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Pohjois-Karjalan rakennusperinto</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Pohjois-Karjalan rakennusperinto</Title>
            <Abstract>Pohjois-Karjalan rakennusperinto</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Pohjois-Karjalan rakennusperinto</Name>
                    <Title>Pohjois-Karjalan rakennusperinto</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>circle</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#FFC0CB</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
                <Rule>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>Nimi</ogc:PropertyName>
                        </Label>
                        <Halo>
                            <Radius>2</Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                            </Fill>
                        </Halo>
                    <VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption></TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 182), (select max(id) from portti_wfs_layer_style));

-- Pohjois-Karjalan rakennusperinto, tunnukset
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Pohjois-Karjalan rakennusperinto</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Pohjois-Karjalan rakennusperinto</Title>
            <Abstract>Pohjois-Karjalan rakennusperinto</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Pohjois-Karjalan rakennusperinto</Name>
                    <Title>Pohjois-Karjalan rakennusperinto</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>circle</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#FFC0CB</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
                <Rule>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>Tunnus</ogc:PropertyName>
                        </Label>
                        <Halo>
                            <Radius>2</Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                            </Fill>
                        </Halo>
                    <VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption></TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 182), (select max(id) from portti_wfs_layer_style));


--- Liikuntapaikka (piste), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Liikuntapaikka (piste)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Liikuntapaikka (piste)</Title>
            <Abstract>Liikuntapaikka (piste)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Liikuntapaikka (piste)</Name>
                    <Title>Liikuntapaikka (piste)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>square</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#EE82EE</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 183), (select max(id) from portti_wfs_layer_style));

-- Liikuntapaikka (piste), nimet
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Liikuntapaikka (piste)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Liikuntapaikka (piste)</Title>
            <Abstract>Liikuntapaikka (piste)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Liikuntapaikka (piste)</Name>
                    <Title>Liikuntapaikka (piste)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>square</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#EE82EE</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
                <Rule>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>kohde_NIMI</ogc:PropertyName>
                        </Label>
                        <Halo>
                            <Radius>2</Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                            </Fill>
                        </Halo>
                    <VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption></TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 183), (select max(id) from portti_wfs_layer_style));

-- Liikuntapaikka (piste), tunnukset
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Liikuntapaikka (piste)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Liikuntapaikka (piste)</Title>
            <Abstract>Liikuntapaikka (piste)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Liikuntapaikka (piste)</Name>
                    <Title>Liikuntapaikka (piste)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>square</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#EE82EE</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
                <Rule>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>kohde_id</ogc:PropertyName>
                        </Label>
                        <Halo>
                            <Radius>2</Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                            </Fill>
                        </Halo>
                    <VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption></TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 183), (select max(id) from portti_wfs_layer_style));


-- Liikuntapaikka (alue), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Liikuntapaikka (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Liikuntapaikka (alue)</Title>
         <Abstract>Liikuntapaikka (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <Rule>
               <!--FeatureTypeName>Feature</FeatureTypeName-->
               <Name>Liikuntapaikka (alue)</Name>
               <Title>Liikuntapaikka (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://times</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#EE82EE</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#EE82EE</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 184), (select max(id) from portti_wfs_layer_style));

-- Liikuntapaikka (alue), nimi
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Liikuntapaikka (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Liikuntapaikka (alue)</Title>
         <Abstract>Liikuntapaikka (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Liikuntapaikka (alue)</Name>
               <Title>Liikuntapaikka (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://times</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#EE82EE</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#EE82EE</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>ALUE_NIMI</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 184), (select max(id) from portti_wfs_layer_style));

-- Liikuntapaikka (alue), tunnus
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Liikuntapaikka (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Liikuntapaikka (alue)</Title>
         <Abstract>Liikuntapaikka (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Liikuntapaikka (alue)</Name>
               <Title>Liikuntapaikka (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://times</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#EE82EE</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#EE82EE</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>alue_id</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 184), (select max(id) from portti_wfs_layer_style));


--Kuninkaan kartasto (alue), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Kuninkaan kartasto (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Kuninkaan kartasto (alue)</Title>
         <Abstract>Kuninkaan kartasto (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <Rule>
               <!--FeatureTypeName>Feature</FeatureTypeName-->
               <Name>Kuninkaan kartasto (alue)</Name>
               <Title>Kuninkaan kartasto (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://horline</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#8B4513</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#8B4513</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 190), (select max(id) from portti_wfs_layer_style));

--- Kuninkaan kartasto (piste), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Kuninkaan kartasto (piste)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Kuninkaan kartasto (piste)</Title>
            <Abstract>Kuninkaan kartasto (piste)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Kuninkaan kartasto (piste)</Name>
                    <Title>Kuninkaan kartasto (piste)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>star</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#8B4513</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 189), (select max(id) from portti_wfs_layer_style));

--- Kuninkaan kartasto (viiva), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>Kuninkaan kartasto (viiva)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>Kuninkaan kartasto (viiva)</Title>
            <Abstract>Kuninkaan kartasto (viiva)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Kuninkaan kartasto (viiva)</Name>
                    <Title>Kuninkaan kartasto (viiva)</Title>
                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">#8B4513</CssParameter>
                            <CssParameter name="stroke-width">3</CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 188), (select max(id) from portti_wfs_layer_style));


-- Malminetsintalupa, ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Malminetsintalupa</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Malminetsintalupa</Title>
         <Abstract>Malminetsintalupa</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <Rule>
               <!--FeatureTypeName>Feature</FeatureTypeName-->
               <Name>Malminetsintalupa</Name>
               <Title>Malminetsintalupa</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://slash</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#b7410e</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#b7410e</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 195), (select max(id) from portti_wfs_layer_style));

-- Malminetsintalupa, nimi
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Malminetsintalupa</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Malminetsintalupa</Title>
         <Abstract>Malminetsintalupa</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Malminetsintalupa</Name>
               <Title>Malminetsintalupa</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://slash</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#b7410e</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#b7410e</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>Alueennimi</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 195), (select max(id) from portti_wfs_layer_style));

-- Malminetsintalupa, tunnus
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Malminetsintalupa</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Malminetsintalupa</Title>
         <Abstract>Malminetsintalupa</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Malminetsintalupa</Name>
               <Title>Malminetsintalupa</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://slash</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#b7410e</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#b7410e</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>Aluetunnus</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 195), (select max(id) from portti_wfs_layer_style));


-- Malminetsintavaraus, ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Malminetsintavaraus</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Malminetsintavaraus</Title>
         <Abstract>Malminetsintavaraus</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <Rule>
               <!--FeatureTypeName>Feature</FeatureTypeName-->
               <Name>Malminetsintavaraus</Name>
               <Title>Malminetsintavaraus</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://slash</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#b7960e</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#b7960e</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 194), (select max(id) from portti_wfs_layer_style));

-- Malminetsintavaraus, nimi
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä nimet',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Malminetsintavaraus</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Malminetsintavaraus</Title>
         <Abstract>Malminetsintavaraus</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Malminetsintavaraus</Name>
               <Title>Malminetsintavaraus</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://slash</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#b7960e</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#b7960e</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>Alueennimi</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 194), (select max(id) from portti_wfs_layer_style));

-- Malminetsintavaraus, tunnus
insert into portti_wfs_layer_style (name, sld_style) values ('Näytä tunnukset',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>Malminetsintavaraus</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>Malminetsintavaraus</Title>
         <Abstract>Malminetsintavaraus</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <!--FeatureTypeName>Feature</FeatureTypeName-->
            <Rule>
               <Name>Malminetsintavaraus</Name>
               <Title>Malminetsintavaraus</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://slash</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#b7960e</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#b7960e</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
            <Rule>
               <TextSymbolizer>
                  <Label>
                     <ogc:PropertyName>Aluetunnus</ogc:PropertyName>
                  </Label>
                  <Halo>
                     <Radius>2</Radius>
                     <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                     </Fill>
                  </Halo>
               </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 194), (select max(id) from portti_wfs_layer_style));


--metsahallitus (alue), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
   <!-- a named layer is the basic building block of an sld document -->
   <NamedLayer>
      <Name>metsahallitus (alue)</Name>
      <UserStyle>
         <!-- they have names, titles and abstracts -->
         <Title>metsahallitus (alue)</Title>
         <Abstract>metsahallitus (alue)</Abstract>
         <!-- FeatureTypeStyles describe how to render different features -->
         <!-- a feature type for lines -->
         <FeatureTypeStyle>
            <Rule>
               <!--FeatureTypeName>Feature</FeatureTypeName-->
               <Name>metsahallitus (alue)</Name>
               <Title>metsahallitus (alue)</Title>
               <PolygonSymbolizer>
                  <Fill>
                     <GraphicFill>
                        <Graphic>
                           <Mark>
                              <WellKnownName>shape://backslash</WellKnownName>
                              <Stroke>
                                 <CssParameter name="stroke">#009900</CssParameter>
                                 <CssParameter name="stroke-width">1</CssParameter>
                              </Stroke>
                           </Mark>
                           <Size>16</Size>
                        </Graphic>
                     </GraphicFill>
                  </Fill>
                  <Stroke>
                     <CssParameter name="stroke">#009900</CssParameter>
                     <CssParameter name="stroke-width">1</CssParameter>
                  </Stroke>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 208), (select max(id) from portti_wfs_layer_style));

--- metsahallitus (viiva), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>metsahallitus (viiva)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>metsahallitus (viiva)</Title>
            <Abstract>metsahallitus (viiva)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>metsahallitus (viiva)</Name>
                    <Title>metsahallitus (viiva)</Title>
                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">#009900</CssParameter>
                            <CssParameter name="stroke-width">3</CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 209), (select max(id) from portti_wfs_layer_style));

--- metsahallitus (piste,kaikki), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>metsahallitus (piste,kaikki)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>metsahallitus (piste,kaikki)</Title>
            <Abstract>metsahallitus (piste,kaikki)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>metsahallitus (piste,kaikki)</Name>
                    <Title>metsahallitus (piste,kaikki)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>circle</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#009900</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>16</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 207), (select max(id) from portti_wfs_layer_style));

--- metsahallitus (piste, mj), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>metsahallitus (piste, mj)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>metsahallitus (piste, mj)</Title>
            <Abstract>metsahallitus (piste, mj)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>metsahallitus (piste, mj)</Name>
                    <Title>metsahallitus (piste, mj)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>cross</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#990000</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
				<Rule>
                    <Name>metsahallitus (piste, mj)</Name>
                    <Title>metsahallitus (piste, mj)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>x</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#990000</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 210), (select max(id) from portti_wfs_layer_style));

--- metsahallitus (piste, ei suoj), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>metsahallitus (piste, ei suoj)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>metsahallitus (piste, ei suoj)</Title>
            <Abstract>metsahallitus (piste, ei suoj)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>metsahallitus (piste, ei suoj)</Name>
                    <Title>metsahallitus (piste, ei suoj)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>cross</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#eeeeee</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
				<Rule>
                    <Name>metsahallitus (piste, ei suoj)</Name>
                    <Title>metsahallitus (piste, ei suoj)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>x</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#eeeeee</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 214), (select max(id) from portti_wfs_layer_style));

--- metsahallitus (piste, muu suoj), ei tunnuksia
insert into portti_wfs_layer_style (name, sld_style) values ('Ei tunnuksia',
'<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <!-- a named layer is the basic building block of an sld document -->
    <NamedLayer>
        <Name>metsahallitus (piste, muu suoj)</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->
            <Title>metsahallitus (piste, muu suoj)</Title>
            <Abstract>metsahallitus (piste, muu suoj)</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->
            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>metsahallitus (piste, muu suoj)</Name>
                    <Title>metsahallitus (piste, muu suoj)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>cross</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#009900</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
				<Rule>
                    <Name>metsahallitus (piste, muu suoj)</Name>
                    <Title>metsahallitus (piste, muu suoj)</Title>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>x</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#009900</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                    <CssParameter name="stroke-width">1</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>12</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>');

insert into portti_wfs_layers_styles (wfs_layer_id, wfs_layer_style_id) values
((select id from portti_wfs_layer where maplayer_id = 215), (select max(id) from portti_wfs_layer_style));
