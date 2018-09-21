update oskari_maplayer set name = 'maakuntamuseot_wms' where locale ilike '%maakuntamuseot%' and type = 'wmslayer';
UPDATE oskari_maplayer
SET attributes = '{"reverseXY":{"EPSG:3067":true},"unique":"kokotun","forceProxy":true,"geometryColumn":"STRING","cropping":"true","geometry":"the_geom","cropWMSLayer":"maakuntamuseot_wms"}'
WHERE name ='WFS_Aluerajat_resurssi_WFS:Maakuntamuseot';