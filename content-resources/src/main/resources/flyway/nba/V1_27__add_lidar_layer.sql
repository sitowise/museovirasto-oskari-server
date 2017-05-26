INSERT INTO oskari_maplayer(type, name, groupId,
                            url,
                            locale,
                            version,
                            srs_name,
                            capabilities)
  VALUES('wmslayer', 'NBA:lidar', (SELECT id FROM oskari_layergroup WHERE locale LIKE '%Museovirasto%'),
         'http://137.116.207.73/geoserver/wms',
         '{"fi":{"name":"lidar","subtitle":""},"sv":{"name":"lidar","subtitle":""},"en":{"name":"lidar","subtitle":""}}',
         '1.1.1',
         'EPSG:3067',
         '{"styles":[{"title":"150k Limited raster","legend":"http://137.116.207.73:80/geoserver/ows?service=WMS&request=GetLegendGraphic&format=image%2Fpng&width=20&height=20&layer=NBA%3Alidar","name":"NBA:raster_limited"}],"version":"1.1.1","formats":{"value":"text/html","available":["text/html","text/plain","application/vnd.ogc.gml","text/xml"]},"isQueryable":true}');

-- link to inspire theme;
INSERT INTO oskari_maplayer_themes(maplayerid,
                                   themeid)
  VALUES((SELECT MAX(id) FROM oskari_maplayer),
         (SELECT id FROM portti_inspiretheme WHERE locale LIKE '%Korkeus%'));

-- add layer as resource for mapping permissions;
INSERT INTO oskari_resource(resource_type, resource_mapping) values ('maplayer', 'wmslayer+http://137.116.207.73/geoserver/wms+NBA:lidar');