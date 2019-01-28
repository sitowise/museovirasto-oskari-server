package downloadbasket.helpers;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.Normalizer;
import java.util.HashSet;

public class XMLHelperServices {

    private static final int MAX_NAME_LENGTH = 10;
    private static final String FEATURE_COLLECTION_STRING = "featurecollection";
    private static final String FEATURE_MEMBER_STRING = "featuremember";
    private static final String SHAPE_STRING= "shape";


    public void NormalizeGmlForShp(File inputFile, File outputFile) throws Exception {

        //Read the content of inputFile
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputFile);

        //Get document nodes
        NodeList nodeList = doc.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE ||
                    !node.getLocalName().toLowerCase().equals(FEATURE_COLLECTION_STRING))
                continue;

            //Get featueCollection element's children
            NodeList nodeChildren = node.getChildNodes();
            for (int j = 0; j < nodeChildren.getLength(); j++) {

                Node nodeChild = nodeChildren.item(j);
                if (nodeChild.getNodeType() != Node.ELEMENT_NODE ||
                        !nodeChild.getLocalName().toLowerCase().equals(FEATURE_MEMBER_STRING))
                    continue;

                //Get featureMember element's children
                NodeList nodeChildChildren = nodeChild.getChildNodes();
                for (int k = 0; k < nodeChildChildren.getLength(); k++) {

                    Node featureMemberNode = nodeChildChildren.item(k);
                    if (featureMemberNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;

                    HashSet<String> localNames = new HashSet<>();

                    //There's only one child of element type
                    NodeList featureMemberNodeChildren = featureMemberNode.getChildNodes();
                    for (int m = 0; m < featureMemberNodeChildren.getLength(); m++) {

                        Node featureMemberNodeChild = featureMemberNodeChildren.item(m);
                        if (featureMemberNodeChild.getNodeType() != Node.ELEMENT_NODE
                                || featureMemberNodeChild.getLocalName().toLowerCase().equals(SHAPE_STRING))
                            continue;

                        //Normalize and shorten local name if necessary
                        NormalizeAndShortenLocalName(doc, featureMemberNode, localNames, featureMemberNodeChild);
                    }
                }
            }
        }

        // Write the content into outputFile
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outputFile);
        transformer.transform(source, result);
    }

    public void NormalizeGmlForGpx(File inputGmlFile, File outputGmlFile) throws IOException {
        FileReader fr = new FileReader(inputGmlFile);
        String s;
        String totalStr = "";
        try (BufferedReader br = new BufferedReader(fr)) {
            while ((s = br.readLine()) != null) {
                totalStr += s;
            }
            String resultString = totalStr;

            resultString = resultString.replaceAll("<gml:LinearRing>", "<gml:LineString>");
            resultString = resultString.replaceAll("</gml:LinearRing>", "</gml:LineString>");

            resultString = resultString.replaceAll("<gml:exterior>", "");
            resultString = resultString.replaceAll("</gml:exterior>", "");
            resultString = resultString.replaceAll("<gml:outerBoundaryIs>", "");
            resultString = resultString.replaceAll("</gml:outerBoundaryIs>", "");

            resultString = resultString.replaceAll("<gml:Polygon>", "");
            resultString = resultString.replaceAll("</gml:Polygon>", "");
            resultString = resultString.replaceAll("<gml:MultiPolygon", "<gml:MultiLineString>");
            resultString = resultString.replaceAll("</gml:MultiPolygon", "</gml:MultiLineString>");
            resultString = resultString.replaceAll("<gml:polygonMember>", "<gml:lineStringMember>");
            resultString = resultString.replaceAll("</gml:polygonMember>", "</gml:lineStringMember>");


            resultString = resultString.replaceAll("<gml:MultiSurface>", "<gml:MultiLineString>");
            resultString = resultString.replaceAll("</gml:MultiSurface>", "</gml:MultiLineString>");
            resultString = resultString.replaceAll("<gml:surfaceMember>", "<gml:lineStringMember>");
            resultString = resultString.replaceAll("</gml:surfaceMember>", "</gml:lineStringMember>");

            resultString = resultString.replaceAll("<gml:MultiCurve>", "<gml:MultiLineString>");
            resultString = resultString.replaceAll("</gml:MultiCurve>", "</gml:MultiLineString>");
            resultString = resultString.replaceAll("<gml:curveMember>", "<gml:lineStringMember>");
            resultString = resultString.replaceAll("</gml:curveMember>", "</gml:lineStringMember>");

            FileWriter fw = new FileWriter(outputGmlFile);
            fw.write(resultString);
            fw.close();
        }
    }

    private void NormalizeAndShortenLocalName(Document doc, Node node, HashSet<String> localNames, Node nodeChild) {

        String localName = nodeChild.getLocalName();

        String newLocalName = Normalizer
                .normalize(localName, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");

        if (newLocalName.length() > MAX_NAME_LENGTH){
            newLocalName = newLocalName.substring(0, MAX_NAME_LENGTH);

            int index = 1;
            while (localNames.contains(newLocalName)) {
                newLocalName = newLocalName.substring(0, MAX_NAME_LENGTH - 1) + index;
                index++;
            }
        }
        localNames.add(newLocalName);

        String tagName = String.format("%s:%s", nodeChild.getPrefix(), newLocalName);
        Element newChild = doc.createElement(tagName);

        newChild.setTextContent(nodeChild.getTextContent());
        NamedNodeMap attributes = nodeChild.getAttributes();
        int numberOfAttribiutes = attributes.getLength();

        for (int a = 0; a < numberOfAttribiutes; a++) {

            Node attribiute = attributes.item(a);
            newChild.setAttribute(attribiute.getNodeName(), attribiute.getNodeValue());
        }

        node.replaceChild(newChild, nodeChild);
    }
}
