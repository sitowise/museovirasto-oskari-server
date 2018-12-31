/*
package fi.nls.oskari.map.userlayer.domain;

import com.vividsolutions.jts.geom.Geometry;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.map.userlayer.service.GeoJsonWorker;
import fi.nls.oskari.util.JSONHelper;
import org.apache.commons.io.IOUtils;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

public class SHPGeoJsonCollection extends GeoJsonCollection implements GeoJsonWorker {

    static final int GJSON_DECIMALS = 10;
    private static final Logger log = LogFactory
            .getLogger(SHPGeoJsonCollection.class);
    private static final Map<String, Charset> AVAILABLE_CHARSETS = Charset.availableCharsets(); //Returns the default charset of this Java virtual machine
    GeometryJSON gjson = new GeometryJSON(GJSON_DECIMALS);
    final FeatureJSON io = new FeatureJSON(gjson);

    */
/**
     * Parse ESRI shape file set to geojson features
     * Coordinate transformation is executed, if shape .prj file is within
     *
     * @param file        .shp import file
     * @param source_epsg source CRS (is used, if crs in not available in source data)
     * @param target_epsg target CRS
     * @return null --> ok   error message --> import failed
     *//*

    public String parseGeoJSON(File file, String source_epsg, String target_epsg) {
        ShapefileDataStore dataStore = null;
        try {
            dataStore = new ShapefileDataStore(file.toURI().toURL());
            dataStore.setCharset(getCharset(file));


            File cpg = new File(file.toString().replaceFirst("\\.shp$", ".cpg"));
            if(cpg.exists() && !cpg.isDirectory()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(cpg));
                    String charset = reader.readLine();
                    dataStore.setCharset(Charset.forName(charset));
                } catch (IOException | IllegalArgumentException e) {
                    //use default charset
                    log.warn(e, "Failed to read CPG file, using default charset");
                } finally {
                    if(reader != null) {
                        reader.close();
                    }
                }
            }

            String typeName = dataStore.getTypeNames()[0];
            MathTransform transform = null;
            FeatureSource source = dataStore.getFeatureSource(typeName);
            FeatureCollection collection = source.getFeatures();

            FeatureType schema = collection.getSchema();

            //Coordinate transformation support
            CoordinateReferenceSystem sourceCrs = source.getBounds().getCoordinateReferenceSystem();
            if (sourceCrs == null) {
                sourceCrs = schema.getCoordinateReferenceSystem();
            }
            //TODO check axis orientation

            //Geojson axis orientation is always  lon,lat (decode(....true)
            CoordinateReferenceSystem target = CRS.decode(target_epsg, true);

            if (sourceCrs == null && source_epsg == null) {
                // Unknown CRS in source data - better to stop - result could be chaos
                log.error("Uknown projection data in the source import file", file.getName());
                return "unknown_projection";
            }

            // Source epsg not found in source data, use epsg given by the user
            if (sourceCrs == null) {
                sourceCrs = CRS.decode(source_epsg, true);
            }

            // Source and target are identical no transform ?  --> no transform
            if (sourceCrs != null && !target.getName().equals(sourceCrs.getName())) {
                transform = CRS.findMathTransform(sourceCrs, target, true);
            }

            JSONArray feas = new JSONArray();
            FeatureIterator iterator = collection.features();
            while (iterator.hasNext()) {

                SimpleFeature feature = (SimpleFeature) iterator.next();
                if (transform != null) {
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    feature.setDefaultGeometry(JTS.transform(geometry, transform));

                }
                JSONObject geojs = JSONHelper.createJSONObject(io.toString(feature));
                if (geojs != null) {
                    feas.put(geojs);
                }
            }
            iterator.close();


            setGeoJson(JSONHelper.createJSONObject("features", feas));
            setFeatureType(schema);
            setTypeName(typeName);
            return null;

        } catch (Exception e) {
            log.error("Couldn't create geoJSON from the shapefile.", file.getName(), e);
            return "shp";
        } finally {
            dataStore.dispose();
        }
    }

    private Charset getCharset(File file) {
        //try to find cpg file for identifying the character encoding to be used
        try {
            String baseFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.'));
            File cpgFile = new File(baseFilePath + ".cpg");
            if (cpgFile != null) {
                try (FileInputStream inputStream = new FileInputStream(cpgFile)) {
                    String content = IOUtils.toString(inputStream); //cpg file should have only charsets name
                    if (AVAILABLE_CHARSETS.containsKey(content.trim())) {
                        Charset charset = AVAILABLE_CHARSETS.get(content.trim());
                        log.debug("Found charset from cpg file. Using charset:", charset.name());
                        return charset;
                    }
                }
            }
        } catch (IOException e) {
            log.debug("Couldn't find cpg file", e.getMessage(), "Using default ISO-8859-1 charset");
        }
        return (Charset) ShapefileDataStoreFactory.DBFCHARSET.getDefaultValue();
    }
}
*/
