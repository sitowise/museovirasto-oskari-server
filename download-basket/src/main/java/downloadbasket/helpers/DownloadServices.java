package downloadbasket.helpers;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.IOHelper;
import downloadbasket.data.ErrorReportDetails;
import downloadbasket.data.LoadZipDetails;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geometry.jts.JTS;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.mail.MultiPartEmail;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import com.opencsv.CSVReader;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.geotools.data.DataStore;
import org.geotools.data.ogr.OGRDataStore;
import org.geotools.data.ogr.OGRDataStoreFactory;
import org.geotools.data.ogr.bridj.BridjOGRDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.context.MessageSource;
import fi.nls.oskari.spring.SpringContextHolder;

/**
 * Download services.
 */
public class DownloadServices {
    private final Logger LOGGER = LogFactory.getLogger(DownloadServices.class);
    private static final char CSV_FILE_DELIMITER = ',';
    private MessageSource messages;

    /**
     * Default Constructor.
     */
    public DownloadServices() {
    }

    /**
     * Load shape-ZIP from Geoserver.
     *
     * @param ldz load zip details
     * @return filename file name
     * @throws IOException Normal way download uses BBOX as the cropping method.
     *                     Otherwise, filter plugin is used.
     */
    public String loadZip(LoadZipDetails ldz, Locale locale) throws IOException {
        String zipFileName = null;
        HttpURLConnection conn = null;
        CoordinateReferenceSystem sourceCrs;
        CoordinateReferenceSystem targetCrs;
        MathTransform transform = null;
        MathTransform swap = null;
        SimpleFeatureIterator it;
        SimpleFeature feature;

        LOGGER.debug("WFS URL: " + ldz.getWFSUrl());

        if (ldz.isDownloadNormalWay()) {
            LOGGER.debug("Download normal way");
        }

        LOGGER.debug("WFS URL: " + ldz.getWFSUrl());
        LOGGER.debug("-- filter: " + ldz.getGetFeatureInfoRequest());
        final URL url = new URL(ldz.getWFSUrl() + ldz.getGetFeatureInfoRequest());

        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(600000);

        conn.connect();

        String basketId = UUID.randomUUID().toString();
        String strTempDir = ldz.getTemporaryDirectory();
        String strOutputDir = strTempDir + File.separator + basketId;

        File dir0 = new File(strTempDir);
        File outputDir = new File(strOutputDir);
        outputDir.mkdirs();

        String gmlOrigFileName = basketId + "-original.gml";
        File gmlOrigFile = new File(dir0, gmlOrigFileName);

        String gmlFileName = basketId + ".gml";
        File gmlFile = new File(dir0, gmlFileName);

        String gmlBoundaryFileName = basketId + "-boundary.gml";
        File gmlBoundaryFile = new File(dir0, gmlBoundaryFileName);


        DefaultMathTransformFactory mathFactory = new DefaultMathTransformFactory();
        try {
            double[][] matrix = {{0, 1, 0}, {1, 0, 0}, {0, 0, 1}};
            swap = mathFactory.createAffineTransform(new GeneralMatrix(matrix));
            sourceCrs = CRS.decode("EPSG:3067");
            targetCrs = CRS.decode("EPSG:4326");
            if (!targetCrs.getName().equals(sourceCrs.getName())) {
                transform = CRS.findMathTransform(sourceCrs, targetCrs, true);
            }
        } catch (FactoryException ex) {
            LOGGER.error("Factory error:", ex);
        }

        try (InputStream istream = conn.getInputStream();
             OutputStream ostream = new FileOutputStream(gmlOrigFile)) {
            IOHelper.copy(istream, ostream);
            try {
                FileReader fr = new FileReader(gmlOrigFile);
                String s;
                String totalStr = "";
                try (BufferedReader br = new BufferedReader(fr)) {
                    while ((s = br.readLine()) != null) {
                        totalStr += s;
                    }
                    String resultString = totalStr;
                    try {
                        Pattern regex = Pattern.compile("(?<before3>> {0})(?<field1>[0-9]+)(?<after3> *<)");
                        Matcher regexMatcher = regex.matcher(totalStr);
                        try {
                            resultString = regexMatcher.replaceAll("${before3}\"${field1}\"${after3}");
                        } catch (IllegalArgumentException ex) {
                            LOGGER.error("Syntax error in the replacement text (unescaped $ signs?)");
                            return null;
                        } catch (IndexOutOfBoundsException ex) {
                            LOGGER.error("Non-existent backreference used the replacement text");
                            return null;
                        }
                    } catch (PatternSyntaxException ex) {
                        LOGGER.error("Syntax error in the regular expression");
                        return null;
                    }
                    FileWriter fw = new FileWriter(gmlFile);
                    fw.write(resultString);
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, String> connectionParams = new HashMap<>();
        connectionParams.put("DriverName", "GML");
        connectionParams.put("DatasourceName", new File(dir0, gmlFileName).getAbsolutePath());
        OGRDataStoreFactory factory = new BridjOGRDataStoreFactory();
        if (!factory.isAvailable()) {
            LOGGER.error("GDAL library is not found for data export -- http://www.gdal.org/");
            return null;
        }
        DataStore store = factory.createDataStore(connectionParams);
        String[] typeNames = store.getTypeNames();

        for (int i = 0; i < typeNames.length; i++) {
            SimpleFeatureSource source = store.getFeatureSource(typeNames[i]);
            SimpleFeatureCollection features = source.getFeatures();
            DefaultFeatureCollection features3067 = new DefaultFeatureCollection();
            DefaultFeatureCollection features4326 = new DefaultFeatureCollection();
            // Coordinate transformation
            it = features.features();
            while (it.hasNext()) {
                feature = it.next();
                // Remove extra quotes
                for (Property property : feature.getProperties()) {
                    String name = property.getName().toString();
                    Object value = feature.getAttribute(name);
                    if (value.getClass().equals(java.lang.String.class)) {
                        String newValue = value.toString().trim();
                        int lastIndex = newValue.length() - 1;
                        if ((newValue.charAt(0) == '"') && (newValue.charAt(lastIndex) == '"')) {
                            newValue = newValue.substring(1, lastIndex);
                            feature.setAttribute(name, newValue);
                        }
                    }
                }
                Geometry geometry3067 = (Geometry) feature.getDefaultGeometry();
                try {
                    geometry3067 = JTS.transform(geometry3067, swap);
                    feature.setDefaultGeometry(geometry3067);
                } catch (TransformException ex) {
                    LOGGER.error("Swap transformation error:", ex);
                }
                features3067.add(feature);
            }
            it.close();

            try {
                // CSV
                String csvFileName = typeNames[i] + basketId + ".csv";
                File csvFile = new File(dir0, csvFileName);
                Map<String, String> connectionParamsCsv = new HashMap<>();
                connectionParamsCsv.put("DriverName", "CSV");
                String csvFilePath = csvFile.getAbsolutePath();
                connectionParamsCsv.put("DatasourceName", csvFilePath);
                OGRDataStoreFactory factoryCsv = new BridjOGRDataStoreFactory();
                OGRDataStore dataStoreCsv = (OGRDataStore) factoryCsv.createNewDataStore(connectionParamsCsv);
                dataStoreCsv.createSchema(features3067, true, new String[]{
                        "GEOMETRY=AS_YX"
                });

                // Excel
                File xlsFile = new File(outputDir, typeNames[i] + basketId + ".xls");
                convertCsvToXls(xlsFile.getAbsolutePath(), csvFilePath);
            } catch (Exception ex) {
                LOGGER.error("Excel conversion error: ", ex);
            }

            // Shapefile
            try {
                LOGGER.error("=============<TRY>==============");
                LOGGER.error(typeNames[i]);
                LOGGER.error(basketId);
                String shpFileName = typeNames[i] + basketId;
                LOGGER.error(shpFileName);
                LOGGER.error(outputDir);
                File shpFile = new File(outputDir, shpFileName);
                LOGGER.error(shpFile);
                Map<String, String> connectionParamsShp = new HashMap<>();
                LOGGER.error(shpFile.getAbsolutePath());
                connectionParamsShp.put("DriverName", "ESRI Shapefile");
                connectionParamsShp.put("DatasourceName", shpFile.getAbsolutePath());
                OGRDataStoreFactory factoryShp = new BridjOGRDataStoreFactory();
                LOGGER.error(features3067);
                OGRDataStore dataStoreShp = (OGRDataStore) factoryShp.createNewDataStore(connectionParamsShp);
                LOGGER.error(dataStoreShp);
                String t = dataStoreShp.getTypeNames()[0];
                SimpleFeatureSource featureSource = dataStoreShp.getFeatureSource(t);
                SimpleFeatureType schema = featureSource.getSchema();
                String geomType = schema.getGeometryDescriptor().getType().getBinding().getName();
                LOGGER.error(geomType);
                dataStoreShp.createSchema(features3067, true, new String[]{
                        "ENCODING=UTF-8"
                });
                LOGGER.error("=============</TRY>==============");
            } catch (Exception ex) {
                LOGGER.error(ex, "Shapefile conversion error: ");
            }
            LOGGER.error("Test: 1");
        }

        LOGGER.error("Test: 2");
        FileReader fr = new FileReader(gmlFile);
        String s = null;
        String totalStr = "";
        try (BufferedReader br = new BufferedReader(fr)) {
            while ((s = br.readLine()) != null) {
                totalStr += s;
            }
            String resultString = totalStr;
            resultString = resultString.replaceAll("<gml:MultiPolygon", "<gml:MultiLineString");
            resultString = resultString.replaceAll("</gml:MultiPolygon", "</gml:MultiLineString");
            resultString = resultString.replaceAll("<gml:polygonMember>", "<gml:lineStringMember>");
            resultString = resultString.replaceAll("</gml:polygonMember>", "</gml:lineStringMember>");
            resultString = resultString.replaceAll("<gml:LinearRing>", "<gml:LineString>");
            resultString = resultString.replaceAll("</gml:LinearRing>", "</gml:LineString>");
            resultString = resultString.replaceAll("<gml:outerBoundaryIs>", "");
            resultString = resultString.replaceAll("</gml:outerBoundaryIs>", "");
            resultString = resultString.replaceAll("<gml:Polygon>", "");
            resultString = resultString.replaceAll("</gml:Polygon>", "");
            FileWriter fw = new FileWriter(gmlBoundaryFile);
            fw.write(resultString);
            fw.close();
        }

        Map<String, String> connectionParamsBoundary = new HashMap<>();
        connectionParamsBoundary.put("DriverName", "GML");
        connectionParamsBoundary.put("DatasourceName", gmlBoundaryFile.getAbsolutePath());
        OGRDataStoreFactory factoryBoundary = new BridjOGRDataStoreFactory();
        if (!factoryBoundary.isAvailable()) {
            LOGGER.error("GDAL library is not found for data export -- http://www.gdal.org/");
            return null;
        }
        DataStore storeBoundary = factoryBoundary.createDataStore(connectionParamsBoundary);
        String[] typeNamesBoundary = storeBoundary.getTypeNames();

        for (int i = 0; i < typeNamesBoundary.length; i++) {
            SimpleFeatureSource source = storeBoundary.getFeatureSource(typeNamesBoundary[i]);
            SimpleFeatureCollection featuresBoundary = source.getFeatures();
            DefaultFeatureCollection features3067Boundary = new DefaultFeatureCollection();
            DefaultFeatureCollection features4326Boundary = new DefaultFeatureCollection();
            // Coordinate transformation
            SimpleFeatureIterator itBoundary = featuresBoundary.features();
            while (itBoundary.hasNext()) {
                SimpleFeature featureBoundary = itBoundary.next();
                // Remove extra quotes
                for (Property property : featureBoundary.getProperties()) {
                    String nameBoundary = property.getName().toString();
                    Object valueBoundary = featureBoundary.getAttribute(nameBoundary);
                    if (valueBoundary.getClass().equals(java.lang.String.class)) {
                        String newValueBoundary = valueBoundary.toString().trim();
                        int lastIndexBoundary = newValueBoundary.length() - 1;
                        if ((newValueBoundary.charAt(0) == '"') && (newValueBoundary.charAt(lastIndexBoundary) == '"')) {
                            newValueBoundary = newValueBoundary.substring(1, lastIndexBoundary);
                            featureBoundary.setAttribute(nameBoundary, newValueBoundary);
                        }
                    }
                }
                Geometry geometryBoundary = (Geometry) featureBoundary.getDefaultGeometry();
                if (transform != null) {
                    try {
                        geometryBoundary = JTS.transform(geometryBoundary, swap);
                        featureBoundary.setDefaultGeometry(geometryBoundary);
                        geometryBoundary = (Geometry) featureBoundary.getDefaultGeometry();
                        featureBoundary.setDefaultGeometry(JTS.transform(geometryBoundary, transform));
                        features4326Boundary.add(featureBoundary);
                    } catch (TransformException ex) {
                        LOGGER.error("Coordinate transformation error:", ex);
                    }
                }
            }
            itBoundary.close();

            // GPX
            try {
                String gpxFileName = typeNames[i] + basketId + ".gpx";
                File gpxFile = new File(outputDir, gpxFileName);
                Map<String, String> connectionParamsGpx = new HashMap<>();
                connectionParamsGpx.put("DriverName", "GPX");
                connectionParamsGpx.put("DatasourceName", gpxFile.getAbsolutePath());
                OGRDataStoreFactory factoryGpx = new BridjOGRDataStoreFactory();
                OGRDataStore dataStoreGpx = (OGRDataStore) factoryGpx.createNewDataStore(connectionParamsGpx);
                dataStoreGpx.createSchema(features4326Boundary, true, new String[]{
                        "GPX_USE_EXTENSIONS=YES"
                });
            } catch (Exception ex) {
                LOGGER.error("GPX conversion error: ", ex);
            }
        }

        File zipFile = new File(dir0, basketId + ".zip");
        zipFileName = zipFile.getAbsolutePath();
        pack(outputDir.getAbsolutePath(), zipFileName);
        LOGGER.error("Test: 3");
        return zipFileName;
    }

    public void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException ex) {
                            LOGGER.error("Error: ", ex);
                        }
                    });
        }
    }

    public String convertCsvToXls(String xlsFilePath, String csvFilePath) {
        HSSFSheet sheet = null;
        CSVReader reader = null;
        HSSFWorkbook workBook = null;
        String generatedXlsFilePath = xlsFilePath;
        FileOutputStream fileOutputStream = null;

        try {
            /**** Get the CSVReader Instance & Specify The Delimiter To Be Used ****/
            String[] nextLine;
            reader = new CSVReader(new FileReader(csvFilePath), CSV_FILE_DELIMITER);
            workBook = new HSSFWorkbook();
            sheet = (HSSFSheet) workBook.createSheet("Sheet");
            int rowNum = 0;
            LOGGER.info("Creating New .Xls File From The Already Generated .Csv File");
            while ((nextLine = reader.readNext()) != null) {
                Row currentRow = sheet.createRow(rowNum++);
                for (int i = 0; i < nextLine.length; i++) {
                    if (NumberUtils.isDigits(nextLine[i])) {
                        currentRow.createCell(i).setCellValue(Integer.parseInt(nextLine[i]));
                    } else if (NumberUtils.isNumber(nextLine[i])) {
                        currentRow.createCell(i).setCellValue(Double.parseDouble(nextLine[i]));
                    } else {
                        currentRow.createCell(i).setCellValue(nextLine[i]);
                    }
                }
            }

            LOGGER.info("The File Is Generated At The Following Location?= " + generatedXlsFilePath);

            fileOutputStream = new FileOutputStream(generatedXlsFilePath);
            workBook.write(fileOutputStream);
        } catch (Exception exObj) {
            LOGGER.error("Exception In convertCsvToXls() Method?=  " + exObj);
        } finally {
            try {

                /**** Closing The Excel Workbook Object ****/
                workBook.close();

                /**** Closing The File-Writer Object ****/
                fileOutputStream.close();

                /**** Closing The CSV File-ReaderObject ****/
                reader.close();
            } catch (IOException ioExObj) {
                LOGGER.error("Exception While Closing I/O Objects In convertCsvToXls() Method?=  " + ioExObj);
            }
        }
        return generatedXlsFilePath;
    }

    /**
     * Check if zipfile is valid.
     *
     * @param file zip file
     * @return
     */
    public boolean isValid(File file) {

        try (ZipFile zipFile = new ZipFile(file)) {
            return true;
        } catch (IOException e) {
            LOGGER.debug("Zip-file is not valid", e);
            return false;
        }
    }

    private MessageSource getMessages() {
        if (messages == null) {
            // "manual autowire"
            messages = SpringContextHolder.getBean(MessageSource.class);

        }
        return messages;
    }

    private String getMessage(String key, String language) {
        return getMessages().getMessage(key, new String[]{}, new Locale(language));
    }

    public void sendErrorReportToEmail(ErrorReportDetails errorDetails) {
        try {
            String msg = getMessage("oskari.wfs.error.message", errorDetails.getLanguage());

            // Using Multipart because HtmlEmail doesn't handle attachments very
            // well.
            MultiPartEmail email = new MultiPartEmail();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(msg, "text/html; charset=UTF-8");
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            byte[] bytes = errorDetails.getXmlRequest().getBytes();

            DataSource dataSource = new ByteArrayDataSource(bytes, "application/xml");
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setDataHandler(new DataHandler(dataSource));
            bodyPart.setFileName("wfs_request.xml");
            multipart.addBodyPart(bodyPart);

            if (errorDetails.getErrorFileLocation() != null) {
                DataSource source = new FileDataSource(errorDetails.getErrorFileLocation());
                MimeBodyPart part = new MimeBodyPart();
                part.setDataHandler(new DataHandler(source));
                part.setFileName("geoserver_wfs_response.xml");
                multipart.addBodyPart(part);
            }

            email.setSmtpPort(Integer.parseInt(PropertyUtil.getNecessary(("oskari.wfs.download.smtp.port"))));
            email.setCharset("UTF-8");

            email.setContent(multipart);
            email.setHostName(PropertyUtil.getNecessary("oskari.wfs.download.smtp.host"));
            email.setFrom(PropertyUtil.getNecessary("oskari.wfs.download.email.from"));
            email.setSubject(getMessage("oskari.wfs.download.error.report.subject", errorDetails.getLanguage()));
            email.addTo(errorDetails.getUserEmail());
            email.addBcc(PropertyUtil.getNecessary("oskari.wfs.download.error.report.support.email"));
            email.send();
        } catch (Exception ex) {
            LOGGER.error(ex, "Error: e-mail was not sent");
        }
    }
}
