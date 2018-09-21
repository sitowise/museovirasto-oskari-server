update oskari_maplayer
set attributes = '{"reverseXY":{"EPSG:3067":true},"unique":"kokotun","forceProxy":true,"geometryColumn":"STRING","cropping":"true","geometry":"the_geom","cropWMSLayer":"'||sub.mlid||'"}'
from (select cast ( id as varchar) as mlid from oskari_maplayer where name = '2') as sub
where name = 'WFS_Aluerajat_resurssi_WFS:kunta250';


update oskari_maplayer
set attributes = '{"reverseXY":{"EPSG:3067":true},"unique":"kokotun","forceProxy":true,"geometryColumn":"STRING","cropping":"true","geometry":"the_geom","cropWMSLayer":"'||sub.mlid||'"}'
from (select cast ( id as varchar) as mlid from oskari_maplayer where name = '1') as sub
where name = 'WFS_Aluerajat_resurssi_WFS:Maakunnat';

update oskari_maplayer set name = 'fix_maakuntamuseot' where locale ilike '%maakuntamuseot%' and type = 'wmslayer';

update oskari_maplayer
set attributes = '{"reverseXY":{"EPSG:3067":true},"unique":"kokotun","forceProxy":true,"geometryColumn":"STRING","cropping":"true","geometry":"the_geom","cropWMSLayer":"'||sub.mlid||'"}'
from (select cast ( id as varchar) as mlid from oskari_maplayer where name = 'fix_maakuntamuseot') as sub
where name = 'WFS_Aluerajat_resurssi_WFS:Maakuntamuseot';

update oskari_maplayer set name = '0' where name = 'fix_maakuntamuseot';