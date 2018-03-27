update oskari_maplayer set attributes = '{
    "reverseXY": {
        "EPSG:3067":true
    }
}'
where url like '%kartta.nba.fi%';