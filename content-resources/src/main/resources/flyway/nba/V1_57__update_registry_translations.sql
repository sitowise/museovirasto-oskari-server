UPDATE nba_registries
	SET locale=
	'{"fi":{"name":"Muinaisjäännösrekisteri"},"en":{"name":"Ancient Monument"},"sv":{"name":"Fornminnesregistret"}}'
    WHERE name = 'ancientMonument' ;

UPDATE nba_registries
	SET locale=
	'{"fi":{"name":"RKY1993"},"en":{"name":"RKY1993"},"sv":{"name":"RKY1993"}}'
    WHERE name = 'rky1993' ;

UPDATE nba_registries
	SET locale=
	'{"fi":{"name":"RKY2000"},"en":{"name":"RKY2000"},"sv":{"name":"RKY2000"}}'
    WHERE name = 'rky2000' ;

UPDATE nba_registries
	SET locale=
	'{"fi":{"name":"Maailmanperintökohteet"},"en":{"name":"World Heritage"},"sv":{"name":"Världsarv"}}'
    WHERE name = 'worldHeritage' ;

UPDATE nba_registries
	SET locale=
	'{"fi":{"name":"Hankerekisteri"},"en":{"name":"Project"},"sv":{"name":"Arkeologiska projekt"}}'
    WHERE name = 'project' ;


UPDATE nba_registries
	SET locale=
	'{"fi":{"name":"Muinaisjäänn. hoitorekisteri"},"en":{"name":"Ancient Monument Maintenance"},"sv":{"name":"Kulturmiljöns vård"}}'
    WHERE name = 'ancientMaintenance' ;

UPDATE nba_registries
	SET locale=
	'{"fi":{"name":"Rakennusperintörekisteri"},"en":{"name":"Building Heritage"},"sv":{"name":"Byggnadsarvregistret"}}'
    WHERE name = 'buildingHeritage' ;

UPDATE nba_registries
	SET locale=
	'{
        "fi":{
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
        },
        "sv":{
                "name":"Administrativa enheter",
                "HistoricalMunicipality":"Tidigare kommun",
                "KYSItem":"KYS",
                "Municipality250":"Kommun",
                "ProvincialMuseum":"Landskapsmuseum",
                "Region":"Landskap"
        }
    }'
    WHERE name = 'resource' ;