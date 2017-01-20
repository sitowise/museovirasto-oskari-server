alter table nba_registry_layers add column classification text default null;

update nba_registry_layers set classification = 'kiinteä muinaisjäännös' where layer_id in (133,134,135);
update nba_registry_layers set classification = 'luonnonmuodostuma' where layer_id in (136,137,138);
update nba_registry_layers set classification = 'löytöpaikka' where layer_id in (139,140,141);
update nba_registry_layers set classification = 'mahdollinen muinaisjäännös' where layer_id in (142,143,144);
update nba_registry_layers set classification = 'muu kohde' where layer_id in (145,146,147);
update nba_registry_layers set classification = 'muu kulttuuriperintökohde' where layer_id in (161,165,167);
update nba_registry_layers set classification = 'poistettu kiinteä muinaisjäännös (ei rauhoitettu)' where layer_id in (148,149,150);