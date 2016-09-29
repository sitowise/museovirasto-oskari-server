package fi.nls.oskari.printout.printing.page;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import fi.nls.oskari.printout.printing.PDFProducer.Options;
import fi.nls.oskari.printout.printing.PDFProducer.Page;
import fi.nls.oskari.printout.printing.PDFProducer.PageCounter;
import fi.nls.oskari.printout.printing.PDPageContentStream;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentGroup;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentProperties;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.pdmodel.markedcontent.PDPropertyList;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.lite.RendererUtilities;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

//import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * This class embeds layers of map images as PDF optional contents.
 * 
 * @todo fix to use transforms instead of manual cm to pixel mappings
 */
public class PDFLayeredImagesPage extends PDFAbstractPage implements PDFPage {

    private CoordinateReferenceSystem crs;
    private List<BufferedImage> images;
    private Envelope env;
    private Point centre;

    public PDFLayeredImagesPage(Page page, Options opts, PDFont font,
            CoordinateReferenceSystem coordinateReferenceSystem,
            List<BufferedImage> images, Envelope env, Point centre)
            throws IOException {
        super(page, opts, font);
        this.crs = coordinateReferenceSystem;
        this.images = images;

        this.env = env;
        this.centre = centre;
    }

    /**
     * PDXObjectImage must be created before optional content groups are created
     * 
     * @param targetDoc
     * @param ximages
     * @param images
     * @throws IOException
     */
    protected void createMapLayersImages(PDDocument targetDoc,
            List<PDXObjectImage> ximages, List<BufferedImage> images)
            throws IOException {

        for (BufferedImage image : images) {

            PDXObjectImage ximage = new PDPixelMap(targetDoc, image);

            ximages.add(ximage);
        }

    }

    /**
     * Let's create overlayers for each map layer
     * 
     * @param targetDoc
     * @param contentStream
     * @param ocprops
     * @param props
     * @param ximages
     * @param width
     * @param height
     * @throws IOException
     */

    protected void createMapLayersOverlay(PDDocument targetDoc,
            PDPage targetPage, PDPageContentStream contentStream,
            PDOptionalContentProperties ocprops, PDPropertyList props,
            List<PDXObjectImage> ximages) throws IOException {

        int r = 0;
        float f[] = { page.getWidth(), page.getHeight() };
        int width = page.getMapWidthTargetInPoints(opts);
        int height = page.getMapHeightTargetInPoints(opts);

        if (opts.getPageMapRect() != null) {
            f[0] = opts.getPageMapRect()[0];
            f[1] = opts.getPageMapRect()[1];
            page.getTransform().transform(f, 0, f, 0, 1);
        } else { // center image
            page.getTransform().transform(f, 0, f, 0, 1);
            f[0] = (f[0] - width) / 2;
            f[1] = (f[1] - height) / 2;
        }

        for (PDXObjectImage ximage : ximages) {
            r++;

            PDOptionalContentGroup layerGroup = new PDOptionalContentGroup(
                    "layer" + r);
            ocprops.addGroup(layerGroup);

            COSName mc0 = COSName.getPDFName("MC" + r);
            props.putMapping(mc0, layerGroup);

            contentStream.beginMarkedContentSequence(COSName.OC, mc0);

            contentStream.drawXObject(ximage, f[0], f[1], width, height);

            contentStream.endMarkedContentSequence();

        }
    }

    public void createPages(PDDocument targetDoc, PageCounter pageCounter)
            throws IOException, TransformException {

        PDDocumentCatalog catalog = targetDoc.getDocumentCatalog();

        PDPage targetPage = page.createNewPage(targetDoc,
                opts.getPageTemplate() != null, pageCounter);

        PDResources resources = targetPage.findResources();
        if (resources == null) {
            resources = new PDResources();
            targetPage.setResources(resources);
        }

        PDOptionalContentProperties ocprops = catalog.getOCProperties();

        if (ocprops == null) {
            ocprops = new PDOptionalContentProperties();
            catalog.setOCProperties(ocprops);
        }

        PDPropertyList props = new PDPropertyList();
        resources.setProperties(props);

        List<PDXObjectImage> ximages = new ArrayList<PDXObjectImage>(
                images.size());

        /* these MUST be created before optional overlay content */
        createMapLayersImages(targetDoc, ximages, images);
        
        // Also create logo here, otherwise it gets corrupted 
        PDXObjectImage xlogo = null;
        if (opts.isPageLogo()) {
            /* MUST create before optiona content group is created */
            /*
             * - this is a googled fix to not being able to show images in
             * overlays
             */
            InputStream inp = getClass().getResourceAsStream(
                    opts.getPageLogoResource());
            try {
                BufferedImage imageBuf = ImageIO.read(inp);

                imageBuf = doScaleWithFilters(imageBuf, imageBuf.getWidth()*4, imageBuf.getHeight()*4);

                xlogo = new PDPixelMap(targetDoc, imageBuf);
            } finally {
                inp.close();
            }
        }

        PDPageContentStream contentStream = page.createContentStreamTo(
                targetDoc, targetPage, opts.getPageTemplate() != null);

        createMapLayersOverlay(targetDoc, targetPage, contentStream, ocprops,
                props, ximages);
        createTextLayerOverlay(targetDoc, targetPage, contentStream, ocprops,
                props, env, centre, xlogo);

        createContentOverlay(targetDoc, contentStream, ocprops, props,
                opts.getContent(), pageCounter);

        contentStream.close();

    }

    /**
     * print scale line to show metric extent actual scale matches display scale
     * in openlayers which
     * 
     * 
     * @param targetDoc
     * @param contentStream
     * @param ocprops
     * @param props
     * @param font
     * @param centre
     * @param env
     * @param height
     * @param width
     * @throws IOException
     * @throws TransformException
     */
    void createScaleLine(PDDocument targetDoc,
            PDPageContentStream contentStream,
            PDOptionalContentProperties ocprops, PDPropertyList props,
            PDFont font, Envelope env, Point centre, int width, int height)
            throws IOException, TransformException {
        contentStream.setNonStrokingColor(0, 0, 0);

        contentStream.setStrokingColor(0, 0, 0);

        final ReferencedEnvelope bounds = new ReferencedEnvelope(env.getMinX(),
                env.getMaxX(), env.getMinY(), env.getMaxY(), crs);

        final Rectangle rect = new Rectangle(0, 0, width, height);

        final AffineTransform transform = RendererUtilities
                .worldToScreenTransform(bounds, rect, crs);

        /* krhm... to be fixed with some algoriddim */
        /* time restricted coding... */
        long widthInMeters = Double.valueOf(env.getWidth()).longValue();
        long scaleLength = widthInMeters / 4;
        String scaleText = ""+scaleLength;

        long firstDigit = Long.parseLong(scaleText.substring(0, 1));
        long length = scaleText.length() - 1;

        scaleLength = (long) (firstDigit * Math.pow(10, length));

        if(scaleLength < 1000) {
            scaleText = scaleLength + " m";
        } else {
            scaleText = scaleLength/1000 + " km";
        }

        double[] srcPts = new double[] { env.getMinX(), env.getMaxY(),
                env.getMaxX(), env.getMinY(), env.getMinX() + scaleLength,
                env.getMinY() };
        double[] dstPts = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        transform.transform(srcPts, 0, dstPts, 0, 3);

        createTextAt(contentStream, scaleText, 6.0f, 26f / 72f * 2.54f, opts.getFontSize(),
                0, 0, 0);

        contentStream.addLine(6.0f / 2.54f * 72f, 16f, 6.0f / 2.54f * 72f, 24f);

        contentStream.addLine(6.0f / 2.54f * 72f, 16f,
                6.0f / 2.54f * 72f + Double.valueOf(dstPts[4]).floatValue(),
                16f);

        contentStream.addLine(6.0f / 2.54f * 72f + Double.valueOf(dstPts[4])
                .floatValue(), 16f,
                6.0f / 2.54f * 72f + Double.valueOf(dstPts[4]).floatValue(),
                24f);

        contentStream.closeAndStroke();

    }

    /**
     * logo , title, etc are put on another optional overlay
     * 
     * @param targetDoc
     * @param targetPage
     * @param contentStream
     * @param ocprops
     * @param props
     * @param centre
     * @param env
     * @param height
     * @param width
     * @throws IOException
     * @throws TransformException
     */
    protected void createTextLayerOverlay(PDDocument targetDoc,
            PDPage targetPage, PDPageContentStream contentStream,
            PDOptionalContentProperties ocprops, PDPropertyList props,
            Envelope env, Point centre, PDXObjectImage xlogo) throws IOException, TransformException {

        PDOptionalContentGroup layerGroup = new PDOptionalContentGroup(
                "overlay");
        ocprops.addGroup(layerGroup);

        COSName mc0 = COSName.getPDFName("MCoverlay");
        props.putMapping(mc0, layerGroup);
        /* PDFont font = PDType1Font.HELVETICA_BOLD; */
        contentStream.beginMarkedContentSequence(COSName.OC, mc0);

        /* BEGIN overlay content */
        
        float mapImagePosition[] = { page.getWidth(), page.getHeight() };
        int mapWidth = page.getMapWidthTargetInPoints(opts);
        int mapHeight = page.getMapHeightTargetInPoints(opts);

        if (opts.getPageMapRect() != null) {
            mapImagePosition[0] = opts.getPageMapRect()[0];
            mapImagePosition[1] = opts.getPageMapRect()[1];
            page.getTransform().transform(mapImagePosition, 0, mapImagePosition, 0, 1);
        } else { // center image
            page.getTransform().transform(mapImagePosition, 0, mapImagePosition, 0, 1);
            mapImagePosition[0] = (mapImagePosition[0] - mapWidth) / 2;
            mapImagePosition[1] = (mapImagePosition[1] - mapHeight) / 2;
        }

        /* title */

        if (opts.getPageTitle() != null) {
            String pageTitle = StringEscapeUtils.unescapeHtml4(Jsoup.clean(
                    opts.getPageTitle(), Whitelist.simpleText()));

            float titleWidth = font.getStringWidth(pageTitle) / 1000 * opts.getFontSize();
            float maxWidth = page.getMapWidthTargetInPoints(opts);
            if (titleWidth < maxWidth) {
                createTextAt(contentStream, pageTitle, mapImagePosition[0] / 72f * 2.54f, page.getHeight() - 1f,
                        opts.getFontSize(), 0, 0, 0);
            } else {
                List<String> lines = new ArrayList<String>();
                String[] words = pageTitle.split(" ");
                String line = "";

                for (String word : words) {
                    String newLine = line + word;
                    float lineWidth = font.getStringWidth(newLine) / 1000 * opts.getFontSize();
                    if (lineWidth <= maxWidth || line.isEmpty()) {
                        line = newLine + " ";
                    } else {
                        lines.add(line);
                        line = word + " ";
                    }
                }

                if (!line.isEmpty()) {
                    lines.add(line);
                }

                for (int i = 0; i < 2 && i < lines.size(); ++i) {
                    createTextAt(contentStream, lines.get(i), mapImagePosition[0] / 72f * 2.54f,
                            page.getHeight() - 1f - i * opts.getFontSize() / 72f * 2.54f, opts.getFontSize(), 0, 0, 0);
                }
            }

        }

        /* pvm */
        if (opts.isPageDate()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Locale l = new Locale("fi");
            Date dte = Calendar.getInstance(l).getTime();

            String dateStr = sdf.format(dte);

            createTextAt(contentStream, dateStr, page.getWidth() - 4f, 0.56f + opts.getFontSize() / 72f * 2.54f,
                    opts.getFontSize(), 0, 0, 0);

        }

        /* mittakaava */
        if (opts.isPageScale() && crs != null) {
            int width = page.getMapWidthTargetInPoints(opts);
            int height = page.getMapHeightTargetInPoints(opts);

            createScaleLine(targetDoc, contentStream, ocprops, props, font,
                    env, centre, width, height);

        }

        /* logo */
        if (opts.isPageLogo()) {
            contentStream.setNonStrokingColor(255, 255, 255);
            contentStream.setStrokingColor(255, 255, 255);

            contentStream.drawXObject(xlogo,
                    mapImagePosition[0], mapImagePosition[1] + mapHeight - xlogo.getHeight()/4,
                    xlogo.getWidth()/4, xlogo.getHeight()/4);
        }
        
        /* coordinates */
        if(opts.isPageCoordinates()) {
            int x = (int) env.getMinX();
            int y = (int) env.getMinY();
            float widthX = font.getStringWidth("P: " + x)
                    / 1000 * opts.getFontSize();
            float widthY = font.getStringWidth("I: " + y)
                    / 1000 * opts.getFontSize();
            float width = widthX > widthY ? widthX : widthY;
            width = width / 72f * 2.54f;

            float offsetX = font.getStringWidth("I: ") / 1000
                    * opts.getFontSize();
            float offsetY = font.getStringWidth("P: ") / 1000
                    * opts.getFontSize();
            float offset = offsetX > offsetY ? offsetX : offsetY;
            offset = offset / 72f * 2.54f;
            
            createTextAt(contentStream, "P:", mapImagePosition[0]/ 72f * 2.54f,
                    0.56f + opts.getFontSize() / 72f * 2.54f,
                    opts.getFontSize(), 0, 0, 0);
            createTextAt(contentStream, String.valueOf(y),
                    mapImagePosition[0]/ 72f * 2.54f + offset,
                    0.56f + opts.getFontSize() / 72f * 2.54f,
                    opts.getFontSize(), 0, 0, 0);
            createTextAt(contentStream, "I:", mapImagePosition[0]/ 72f * 2.54f,
                    0.56f, opts.getFontSize(), 0, 0, 0);
            createTextAt(contentStream, String.valueOf(x),
                    mapImagePosition[0]/ 72f * 2.54f + offset, 0.56f,
                    opts.getFontSize(), 0, 0, 0);
            
            //create symbol to show location of coordinates
            contentStream.setStrokingColor(0, 0, 0);
            
            float markerSize = 0.25f / 2.54f * 72f;
            contentStream.addLine(mapImagePosition[0]-markerSize, mapImagePosition[1], mapImagePosition[0]+markerSize, mapImagePosition[1]);
            contentStream.addLine(mapImagePosition[0], mapImagePosition[1]-markerSize, mapImagePosition[0], mapImagePosition[1]+markerSize);

            contentStream.closeAndStroke();
            
        }
        /* END overlay content */

        contentStream.endMarkedContentSequence();

    }

    /*
     * public void createContent(PDDocument targetDoc, PDPageContentStream
     * contentStream, PDOptionalContentProperties ocprops, PDPropertyList props,
     * Page page, Options opts) throws IOException {
     * 
     * PDOptionalContentGroup tablesGroup = new PDOptionalContentGroup(
     * "tables"); ocprops.addGroup(tablesGroup);
     * 
     * COSName mc0 = COSName.getPDFName("Mtables"); props.putMapping(mc0,
     * tablesGroup);
     * 
     * contentStream.beginMarkedContentSequence(COSName.OC, mc0);
     * 
     * String[][] content = { {} }; float margin = 20; float y = 20;
     * 
     * final int rows = content.length; final int cols = content[0].length;
     * final float rowHeight = 20f; final float tableWidth = page.getWidth() /
     * 2.54f * 72f - margin - margin; final float tableHeight = rowHeight *
     * rows; final float colWidth = tableWidth / (float) cols; final float
     * cellMargin = 5f;
     * 
     * // draw the rows float nexty = y; for (int i = 0; i <= rows; i++) {
     * contentStream.drawLine(margin, nexty, margin + tableWidth, nexty); nexty
     * -= rowHeight; }
     * 
     * // draw the columns float nextx = margin; for (int i = 0; i <= cols; i++)
     * { contentStream.drawLine(nextx, y, nextx, y - tableHeight); nextx +=
     * colWidth; }
     * 
     * //float fontSize = opts.getFontSize(); // contentStream.setFont(font,
     * fontSize);
     * 
     * float textx = margin + cellMargin; float texty = y - 15; for (int i = 0;
     * i < content.length; i++) { for (int j = 0; j < content[i].length; j++) {
     * String text = content[i][j]; contentStream.beginText();
     * contentStream.moveTextPositionByAmount(textx, texty);
     * contentStream.drawString("[" + i + "," + j + "]");
     * contentStream.endText(); textx += colWidth; } texty -= rowHeight; textx =
     * margin + cellMargin; }
     * 
     * contentStream.endMarkedContentSequence();
     * 
     * }
     */

}
