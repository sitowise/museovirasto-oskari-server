update portti_wfs_layer set feature_params_locales = '{"fi": ["Attribuutit","Luotu","Päivitetty","Tunnus"]}'
where layer_name = 'oskari:vuser_layer_data';

update portti_wfs_layer set feature_params_locales = '{ "fi": ["nimi", "kuvaus","linkki", "kuva-linkki", "Attribuutit"]}'
where layer_name = 'oskari:my_places';