
INSERT INTO portti_bundle (name, startup) 
       VALUES ('admin-layerselector','{}');


update portti_bundle set startup='{
    "title" : "Admin layerselector",
    "fi" : "admin-layerselector",
    "sv" : "admin-layerselector",
    "en" : "admin-layerselector",
    "bundlename" : "admin-layerselector",
    "bundleinstancename" : "admin-layerselector",
    "metadata" : {
        "Import-Bundle" : {
            "bb" : {
                "bundlePath" : "/Oskari/packages/integration/bundle/"
            },
            "admin-layerselector" : {
                "bundlePath" : "/Oskari/packages/integration/bundle/"
            }
        },
        "Require-Bundle-Instance" : []
    },
    "instanceProps" : {}
}' where name = 'admin-layerselector';