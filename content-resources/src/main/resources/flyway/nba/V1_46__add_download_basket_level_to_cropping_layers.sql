UPDATE oskari_maplayer SET attributes = '{
    "reverseXY": {
        "EPSG:3067":true
    },
    "downloadBasketLevel": 1
}' WHERE name IN ('WFS_Aluerajat_resurssi_WFS:Maakuntamuseot','WFS_Aluerajat_resurssi_WFS:Vanhat_kunnat','WFS_MJ_supistettu_sisalto:Kiinteat_muinaisjaannokset','WFS_MJ_supistettu_sisalto:Kiinteat_muinaisjaannokset_alakohteet','WFS_MJ_supistettu_sisalto:Kiinteat_muinaisjaannokset_alueet','WFS_MJ_supistettu_sisalto:Loytopaikat','WFS_MJ_supistettu_sisalto:Loytopaikat_alakohteet','WFS_MJ_supistettu_sisalto:Loytopaikat_alueet','WFS_MJ_supistettu_sisalto:Luonnonmuodostumat','WFS_MJ_supistettu_sisalto:Luonnonmuodostumat_alakohteet','WFS_MJ_supistettu_sisalto:Luonnonmuodostumat_alueet','WFS_MJ_supistettu_sisalto:Muut_kohteet','WFS_MJ_supistettu_sisalto:Muut_kohteet_alakohteet','WFS_MJ_supistettu_sisalto:Muut_kohteet_alueet','WFS_MJ_supistettu_sisalto:Muut_kulttuuriperintokohteet','WFS_MJ_supistettu_sisalto:Muut_kulttuuriperintokohteet_alakohteet','WFS_MJ_supistettu_sisalto:Muut_kulttuuriperintokohteet_alueet','WFS_Rakper_rekisteri:Suojellut_rakennukset','WFS_Rakper_rekisteri:Suojelurajaukset','WFS_RKY_ja_maailmanper_WFS:maailmanperintokohde_alue','WFS_RKY_ja_maailmanper_WFS:maailmanperintokohde_piste','WFS_RKY_ja_maailmanper_WFS:RKY_alueet','WFS_RKY_ja_maailmanper_WFS:RKY_pisteet','WFS_RKY_ja_maailmanper_WFS:RKY_viivat');

UPDATE oskari_maplayer SET attributes = '{
    "reverseXY": {
        "EPSG:3067":true
    },
    "downloadBasketLevel": 2
}' WHERE name IN ('WFS_MJ_supistettu_sisalto:Mahdolliset_muinaisjaannokset_alueet','WFS_MJ_supistettu_sisalto:Mahdolliset_muinaisjäännökset','WFS_MJ_supistettu_sisalto:Mahdolliset_muinaisjäännökset_alakohteet','WFS_MJ_supistettu_sisalto:Poistetut_kiinteat_muinaisjaannokset','WFS_MJ_supistettu_sisalto:Poistetut_kiinteat_muinaisjaannokset_alakohteet','WFS_MJ_supistettu_sisalto:Poistetut_kiinteat_muinaisjaannokset_alueet');