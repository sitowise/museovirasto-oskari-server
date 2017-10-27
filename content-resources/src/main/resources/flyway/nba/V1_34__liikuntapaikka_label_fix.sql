update portti_wfs_layer_style set sld_style='<?xml version="1.0" encoding="UTF-8"?>
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
               <VendorOption name="maxDisplacement">30</VendorOption><VendorOption name="autoWrap">70</VendorOption><VendorOption name="goodnessOfFit">0.1</VendorOption><LabelPlacement>
           <PointPlacement>
             <AnchorPoint>
               <AnchorPointX>0</AnchorPointX>
               <AnchorPointY>0</AnchorPointY>
             </AnchorPoint>
             <Displacement>
               <DisplacementX>10</DisplacementX>
               <DisplacementY>0</DisplacementY>
             </Displacement>
           </PointPlacement>
       </LabelPlacement>
       </TextSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </NamedLayer>
</StyledLayerDescriptor>'
where id in (select wfs_layer_style_id from portti_wfs_layers_styles where wfs_layer_id = (select id from portti_wfs_layer where maplayer_id = 184)) AND name = 'Näytä nimet'; 