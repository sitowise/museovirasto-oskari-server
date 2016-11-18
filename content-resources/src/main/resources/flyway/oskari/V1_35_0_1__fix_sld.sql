update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>RAPEAKOHDE</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Rakennusperintörekisteri</Title>
      <Abstract>Rakennusperintörekisteri</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
       	
       <Rule>
	   <Name>Piste</Name>
       <Title>Piste</Title>
       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>square</WellKnownName>
             <Fill>
               <CssParameter name="fill">#38A800</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
' where id = 1;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>RAPEAKOHDE</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Rakennusperintörekisteri</Title>
      <Abstract>Rakennusperintörekisteri</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
       	
       <Rule>
	   <Name>Piste</Name>
       <Title>Piste</Title>
       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>square</WellKnownName>
             <Fill>
               <CssParameter name="fill">#38A800</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
                <TextSymbolizer>
         <Label>
           <ogc:PropertyName>KOHDENIMI</ogc:PropertyName>
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
</StyledLayerDescriptor>
' where id = 2;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>RAPEAKOHDE</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Rakennusperintörekisteri</Title>
      <Abstract>Rakennusperintörekisteri</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
       	
       <Rule>
	   <Name>Piste</Name>
       <Title>Piste</Title>
       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>square</WellKnownName>
             <Fill>
               <CssParameter name="fill">#38A800</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
                <TextSymbolizer>
         <Label>
           <ogc:PropertyName>KOHDEID</ogc:PropertyName>
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
</StyledLayerDescriptor>
' where id = 3;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>	 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 49;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 50;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
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
       </TextSymbolizer>
</Rule>		 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 51;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>	 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 58;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 59;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
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
       </TextSymbolizer>
</Rule>		 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 60;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>	 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 67;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 68;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
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
       </TextSymbolizer>
</Rule>		 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 69;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
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
     <Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 73;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
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
        <!--FeatureTypeName>Feature</FeatureTypeName-->    
       <Rule>
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
     <Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 74;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
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
        <!--FeatureTypeName>Feature</FeatureTypeName-->    
       <Rule>
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
       </TextSymbolizer>
</Rule>		 
		 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 75;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>	 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 76;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 77;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
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
       </TextSymbolizer>
</Rule>		 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 78;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#B5915F</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>	 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 85;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#B5915F</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 86;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#B5915F</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
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
       </TextSymbolizer>
</Rule>		 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 87;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>	 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 94;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
         </Graphic>
       </PointSymbolizer>
     </Rule>
<Rule>
     <TextSymbolizer>
         <Label>
           <ogc:PropertyName>Kohdenimi</ogc:PropertyName>
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
</StyledLayerDescriptor>' where id = 95;

update portti_wfs_layer_style set sld_style = '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Alakohde (piste)</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
      
      <Title>Alakohde (piste)</Title>
      <Abstract>Alakohde (piste)</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for lines -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
     
       <Rule>
	   <Name>Alakohde (piste)</Name>
       <Title>Alakohde (piste)</Title>

       <PointSymbolizer>
         <Graphic>
           <Mark>
             <WellKnownName>circle</WellKnownName>
             <Fill>
               <CssParameter name="fill">#CDCDCD</CssParameter>
             </Fill>
			 <Stroke>
			  <CssParameter name="stroke">#000000</CssParameter>
			  <CssParameter name="stroke-width">1</CssParameter>
         </Stroke>	
           </Mark>
           <Size>7</Size>
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
       </TextSymbolizer>
</Rule>		 
        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>' where id = 96;