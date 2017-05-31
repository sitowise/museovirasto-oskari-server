update portti_wfs_layer_style set sld_style = 
replace(portti_wfs_layer_style.sld_style, '</TextSymbolizer>', '<LabelPlacement>
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
       </TextSymbolizer>')
where
sld_style like '%TextSymbolizer%';