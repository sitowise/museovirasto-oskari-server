--GetCapabilities for these is broken so admin ui can't be used to add these

-- Merenpohjan maalajit 1:250000
INSERT INTO oskari_maplayer(type, name, groupId,
                            url,
                            locale,
                            version,
                            srs_name,
                            capabilities)
  VALUES('wmslayer', 'merenpohjan_maalajit_250k', (SELECT id FROM oskari_layergroup WHERE locale LIKE '%GTK%'),
         'http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer',
         '{ fi:{name:"Merenpohjan maalajit 1:250 000",subtitle:""},sv:{name:"Merenpohjan maalajit 1:250 000",subtitle:""},en:{name:"Merenpohjan maalajit 1:250 000",subtitle:""}}',
         '1.1.1',
         'EPSG:3067',
         '{"formats":{"available":["text/html","text/plain","application/vnd.ogc.wms_xml","text/xml"],"value":"text/html"},"isQueryable":true,"styles":[{"legend":"http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer?request=GetLegendGraphic%26version=1.3.0%26format=image/png%26layer=merenpohjan_maalajit_250k","name":"default","title":"merenpohjan_maalajit_250k"}],"version":"1.3.0"}');

-- link to inspire theme;
INSERT INTO oskari_maplayer_themes(maplayerid,
                                   themeid)
  VALUES((SELECT MAX(id) FROM oskari_maplayer),
         (SELECT id FROM portti_inspiretheme WHERE locale LIKE '%Geologia%'));

-- add layer as resource for mapping permissions;
INSERT INTO oskari_resource(resource_type, resource_mapping) values ('maplayer', 'wmslayer+http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer+merenpohjan_maalajit_250k');

-- Ylin ranta (10m korkeusmallista)
INSERT INTO oskari_maplayer(type, name, groupId,
                            url,
                            locale,
                            version,
                            srs_name,
                            capabilities)
  VALUES('wmslayer', 'ylin_ranta_10m_korkeusmallista', (SELECT id FROM oskari_layergroup WHERE locale LIKE '%GTK%'),
         'http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer',
         '{ fi:{name:"Ylin ranta (10m korkeusmallista)",subtitle:""},sv:{name:"Ylin ranta (10m korkeusmallista)",subtitle:""},en:{name:"Ylin ranta (10m korkeusmallista)",subtitle:""}}',
         '1.1.1',
         'EPSG:3067',
         '{"formats":{"available":["text/html","text/plain","application/vnd.ogc.wms_xml","text/xml"],"value":"text/html"},"isQueryable":true,"styles":[{"legend":"http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer?request=GetLegendGraphic%26version=1.3.0%26format=image/png%26layer=ylin_ranta_10m_korkeusmallista","name":"default","title":"ylin_ranta_10m_korkeusmallista"}],"version":"1.3.0"}');

-- link to inspire theme;
INSERT INTO oskari_maplayer_themes(maplayerid,
                                   themeid)
  VALUES((SELECT MAX(id) FROM oskari_maplayer),
         (SELECT id FROM portti_inspiretheme WHERE locale LIKE '%Geologia%'));

-- add layer as resource for mapping permissions;
INSERT INTO oskari_resource(resource_type, resource_mapping) values ('maplayer', 'wmslayer+http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer+ylin_ranta_10m_korkeusmallista');

-- Ylimmän rannan isobaasi (m mpy)
INSERT INTO oskari_maplayer(type, name, groupId,
                            url,
                            locale,
                            version,
                            srs_name,
                            capabilities)
  VALUES('wmslayer', 'ylin_ranta', (SELECT id FROM oskari_layergroup WHERE locale LIKE '%GTK%'),
         'http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer',
         '{ fi:{name:"Ylimmän rannan isobaasi (m mpy)",subtitle:""},sv:{name:"Ylimmän rannan isobaasi (m mpy)",subtitle:""},en:{name:"Ylimmän rannan isobaasi (m mpy)",subtitle:""}}',
         '1.1.1',
         'EPSG:3067',
         '{"formats":{"available":["text/html","text/plain","application/vnd.ogc.wms_xml","text/xml"],"value":"text/html"},"isQueryable":true,"styles":[{"legend":"http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer?request=GetLegendGraphic%26version=1.3.0%26format=image/png%26layer=ylin_ranta","name":"default","title":"ylin_ranta"}],"version":"1.3.0"}');

-- link to inspire theme;
INSERT INTO oskari_maplayer_themes(maplayerid,
                                   themeid)
  VALUES((SELECT MAX(id) FROM oskari_maplayer),
         (SELECT id FROM portti_inspiretheme WHERE locale LIKE '%Geologia%'));

-- add layer as resource for mapping permissions;
INSERT INTO oskari_resource(resource_type, resource_mapping) values ('maplayer', 'wmslayer+http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer+ylin_ranta');

-- Litorina (10m korkeusmallista)
INSERT INTO oskari_maplayer(type, name, groupId,
                            url,
                            locale,
                            version,
                            srs_name,
                            capabilities)
  VALUES('wmslayer', 'litorina_10m_korkeusmallista', (SELECT id FROM oskari_layergroup WHERE locale LIKE '%GTK%'),
         'http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer',
         '{ fi:{name:"Litorina (10m korkeusmallista)",subtitle:""},sv:{name:"Litorina (10m korkeusmallista)",subtitle:""},en:{name:"Litorina (10m korkeusmallista)",subtitle:""}}',
         '1.1.1',
         'EPSG:3067',
         '{"formats":{"available":["text/html","text/plain","application/vnd.ogc.wms_xml","text/xml"],"value":"text/html"},"isQueryable":true,"styles":[{"legend":"http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer?request=GetLegendGraphic%26version=1.3.0%26format=image/png%26layer=litorina_10m_korkeusmallista","name":"default","title":"litorina_10m_korkeusmallista"}],"version":"1.3.0"}');

-- link to inspire theme;
INSERT INTO oskari_maplayer_themes(maplayerid,
                                   themeid)
  VALUES((SELECT MAX(id) FROM oskari_maplayer),
         (SELECT id FROM portti_inspiretheme WHERE locale LIKE '%Geologia%'));

-- add layer as resource for mapping permissions;
INSERT INTO oskari_resource(resource_type, resource_mapping) values ('maplayer', 'wmslayer+http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer+litorina_10m_korkeusmallista');

-- Litorinan isobaasi (m mpy)
INSERT INTO oskari_maplayer(type, name, groupId,
                            url,
                            locale,
                            version,
                            srs_name,
                            capabilities)
  VALUES('wmslayer', 'litorina', (SELECT id FROM oskari_layergroup WHERE locale LIKE '%GTK%'),
         'http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer',
         '{ fi:{name:"Litorinan isobaasi (m mpy)",subtitle:""},sv:{name:"Litorinan isobaasi (m mpy)",subtitle:""},en:{name:"Litorinan isobaasi (m mpy)",subtitle:""}}',
         '1.1.1',
         'EPSG:3067',
         '{"formats":{"available":["text/html","text/plain","application/vnd.ogc.wms_xml","text/xml"],"value":"text/html"},"isQueryable":true,"styles":[{"legend":"http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer?request=GetLegendGraphic%26version=1.3.0%26format=image/png%26layer=litorina","name":"default","title":"litorina"}],"version":"1.3.0"}');

-- link to inspire theme;
INSERT INTO oskari_maplayer_themes(maplayerid,
                                   themeid)
  VALUES((SELECT MAX(id) FROM oskari_maplayer),
         (SELECT id FROM portti_inspiretheme WHERE locale LIKE '%Geologia%'));

-- add layer as resource for mapping permissions;
INSERT INTO oskari_resource(resource_type, resource_mapping) values ('maplayer', 'wmslayer+http://gtkdata.gtk.fi/arcgis/services/Rajapinnat/GTK_Maapera_WMS/MapServer/WmsServer+litorina');
