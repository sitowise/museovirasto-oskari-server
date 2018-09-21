UPDATE oskari_maplayer
SET attributes = '{"reverseXY":{"EPSG:3067":true},"unique":"kokotun","forceProxy":true,"geometryColumn":"STRING","cropping":"true","geometry":"the_geom","cropWMSLayer":"2"}'
WHERE name ='WFS_Aluerajat_resurssi_WFS:kunta250';

UPDATE oskari_maplayer
SET attributes = '{"reverseXY":{"EPSG:3067":true},"unique":"kokotun","forceProxy":true,"geometryColumn":"STRING","cropping":"true","geometry":"the_geom","cropWMSLayer":"0"}'
WHERE name ='WFS_Aluerajat_resurssi_WFS:Maakuntamuseot';

UPDATE oskari_maplayer
SET attributes = '{"reverseXY":{"EPSG:3067":true},"unique":"kokotun","forceProxy":true,"geometryColumn":"STRING","cropping":"true","geometry":"the_geom","cropWMSLayer":"1"}'
WHERE name ='WFS_Aluerajat_resurssi_WFS:Maakunnat';