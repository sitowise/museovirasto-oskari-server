UPDATE nba_registries SET 
locale = '{"fi":{
                "name":"Aluejaot",
                "HistoricalMunicipality":"Vanha kunta",
                "KYSItem":"KYS",
                "Municipality250":"Kunta",
                "ProvincialMuseum":"Maakuntamuseo",
                "Region":"Maakunta"
            },
            "en":{
                "name":"Resource",
                "HistoricalMunicipality":"Historical Municipality",
                "KYSItem":"KYS",
                "Municipality250":"Municipality",
                "ProvincialMuseum":"Provincial Museum",
                "Region":"Region"
            }}'
WHERE name = 'resource';