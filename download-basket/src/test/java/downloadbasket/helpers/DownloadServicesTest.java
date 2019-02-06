package downloadbasket.helpers;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@Ignore
public class DownloadServicesTest {

    @Test
    //Test checking if geometry normalization in NormalizeGmlForGpx method gives a valid result
    public void TestGmlGeometryNormalizationForGpx() throws IOException {

        //Gml file containing NOT EMPTY set of features
        String inputFileName = "testFile.gml";
        //Location of input file
        String inputDirectory = "C:\\gpxTest\\input";
        //Location of output files
        String outputDirectory = "C:\\gpxTest\\output";

        File inputGmlFile = new File(inputDirectory + "\\" + inputFileName);
        File gmlBoundaryFile = new File(outputDirectory, inputFileName + "-temp");

        DownloadServices downladServices = new DownloadServices();
        XMLHelperServices xmlHelperServices = new XMLHelperServices();

        xmlHelperServices.NormalizeGmlForGpx(inputGmlFile, gmlBoundaryFile);

        DataStore storeBoundary = downladServices.GetGmlDataStoreForGpx(gmlBoundaryFile);
        String[] typeNamesBoundary = storeBoundary.getTypeNames();

        for (int i = 0; i < typeNamesBoundary.length; i++) {
            SimpleFeatureSource source = storeBoundary.getFeatureSource(typeNamesBoundary[i]);
            SimpleFeatureCollection featuresBoundary = source.getFeatures();
            SimpleFeatureIterator itBoundary = featuresBoundary.features();

            Assert.assertTrue("Should have features", itBoundary.hasNext());
        }
    }

    @Test
    public void TestGmlNormalizationForShp() throws Exception {

        String inputDirectory = "C:\\shpTest\\input";
        String inputFileName = "testFile.gml";
        File inputFile= new File(inputDirectory, inputFileName);

        String outputDirectory = "C:\\shpTest\\output";
        String outputFileName = inputFileName + "-temp";
        File outputFile = new File(outputDirectory, outputFileName);

        XMLHelperServices xmlHelperServices = new XMLHelperServices();
        xmlHelperServices.NormalizeGmlForShp(inputFile, outputFile);
    }
}