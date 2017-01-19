UPDATE portti_view_bundle_seq SET config = '{
  "extraParams": [
    {
      "id": "mvtunnus",
      "name": {
        "fi": "MV rekisteritunnus",
        "en": "MV register id"
      },
      "type": "text"
    },
    {
      "id": "rekisteri",
      "name": {
        "fi": "Viittaa rekisteriin",
        "en": "Register"
      },
      "type": "text"
    },
    {
      "id": "muuTunnus",
      "name": {
        "fi": "Muu tunnus",
        "en": "Other id"
      },
      "type": "text"
    },
    {
      "id": "muuTunnusSelite",
      "name": {
        "fi": "Muun tunnuksen selite",
        "en": "Other id explanation"
      },
      "type": "text"
    },
    {
      "id": "paikannusTapa",
      "name": {
        "fi": "Paikannustapa",
        "en": "Positioning method"
      },
      "type": "dropdown",
      "values": [
        "Maastomittaus",
        "Tarkastus",
        "Muu l\u00e4hde"
      ]
    },
    {
      "id": "paikannusTarkkuus",
      "name": {
        "fi": "Paikannustarkkuus",
        "en": "Positioning accuracy"
      },
      "type": "dropdown",
      "values": [
        "Ei tiedossa",
        "Tarkka  (< 10 m)",
        "Ohjeellinen (10 - 100 m)",
        "Suuntaa antava (100 - 1000 m)",
        "> 1000 m"
      ]
    },
    {
      "id": "paikannusSelite",
      "name": {
        "fi": "Paikannuksen selite",
        "en": "Explanation of positioning"
      },
      "type": "text"
    },
    {
      "id": "kuvateksti",
      "name": {
        "fi": "Kuvateksti",
        "en": "Image caption"
      },
      "type": "text"
    }
  ]
}'
WHERE bundle_id = (SELECT id FROM portti_bundle WHERE name='myplaces2');

UPDATE public.portti_wfs_layer
	SET selected_feature_params='{"default": ["name", "place_desc","link", "image_url", "attributes"],"fi": ["name", "place_desc","link", "image_url", "attributes"]}'
	WHERE layer_name='oskari:my_places';


