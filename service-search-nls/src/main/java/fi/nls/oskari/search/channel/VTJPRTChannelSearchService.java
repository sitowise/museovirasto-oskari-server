package fi.nls.oskari.search.channel;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlObject;

import fi.mml.nameregister.FeatureCollectionDocument;
import fi.mml.nameregister.FeaturePropertyType;
import fi.mml.portti.service.search.ChannelSearchResult;
import fi.mml.portti.service.search.SearchCriteria;
import fi.mml.portti.service.search.SearchResultItem;
import fi.nls.oskari.annotation.Oskari;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.search.util.SearchUtil;
import fi.nls.oskari.util.IOHelper;
import fi.nls.oskari.util.PropertyUtil;

@Oskari(VTJPRTChannelSearchService.ID)
public class VTJPRTChannelSearchService extends SearchChannel {
    /** logger */
    private Logger log = LogFactory.getLogger(this.getClass());

    public static final String ID = "VTJPRT_CHANNEL";
    //url is https://ws.nls.fi/rahu/wfs
    private static final String PROPERTY_SERVICE_URL = "search.channel.VTJPRT_CHANNEL.service.url";

    private String serviceURL = null;

    @Override
    public void init() {
        super.init();
        serviceURL = PropertyUtil.getOptional(PROPERTY_SERVICE_URL);
        log.debug("ServiceURL set to " + serviceURL);
    }

    private String getLocaleCode(String locale) {
        final String currentLocaleCode =  SearchUtil.getLocaleCode(locale);
        if("eng".equals(currentLocaleCode)) {
            return "fin";
        }
        return currentLocaleCode;
    }

    public ChannelSearchResult doSearch(SearchCriteria searchCriteria) {
        if (serviceURL == null) {
            log.warn("ServiceURL not configured. Add property with key", PROPERTY_SERVICE_URL);
            return null;
        }

        ChannelSearchResult searchResultList = new ChannelSearchResult();

        String searchString = searchCriteria.getSearchString();

        Pattern p = Pattern.compile("\\p{Digit}{9}\\p{Alnum}");
        Matcher m = p.matcher(searchString);
        if (!m.matches()) {
            // invalid search string
            log.debug("VTJ-PRT Id not found for query: " + searchCriteria.getSearchString());
            return searchResultList;
        }

        try {
            final String url = getWFSUrl(searchString);
            final String data = IOHelper.readString(getConnection(url));

            final String currentLocaleCode = getLocaleCode(searchCriteria.getLocale());
            final FeatureCollectionDocument fDoc = FeatureCollectionDocument.Factory.parse(data);
            final FeaturePropertyType[] fMembersArray = fDoc.getFeatureCollection().getFeatureMemberArray();

            for (FeaturePropertyType fpt : fMembersArray) {
                XmlObject[] tiedot = fpt.selectChildren(SearchUtil.rhrRakennuksenTunnistetiedot);
                XmlObject[] sijainti = tiedot[0].selectChildren(SearchUtil.rhrSijainti);
                XmlObject[] point = sijainti[0].selectChildren(SearchUtil.gmlPoint);
                XmlObject[] pos = point[0].selectChildren(SearchUtil.gmlPos);

                String[] lonLat = pos[0].newCursor().getTextValue().split("\\s");

                SearchResultItem item = new SearchResultItem();
                item.setRank(SearchUtil.getRank("Rakennus"));
                item.setType("Rakennus");
                item.setTitle(searchString);
                item.setContentURL(lonLat[0] + "_" + lonLat[1]);
                item.setLocationName(searchString);
                if (currentLocaleCode.equals("swe")) {
                    item.setVillage(tiedot[0].selectChildren(SearchUtil.rhrKuntanimiSwe)[0].newCursor().getTextValue());
                } else {
                    item.setVillage(tiedot[0].selectChildren(SearchUtil.rhrKuntanimiFin)[0].newCursor().getTextValue());
                }

                item.setLon(lonLat[0]);
                item.setLat(lonLat[1]);
                item.setMapURL(SearchUtil.getMapURL(searchCriteria.getLocale()));

                searchResultList.addItem(item);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to search Locations from rahu", e);
        }

        return searchResultList;
    }
    
    /**
     * Returns the searchcriterial String. 
     *
     * @param filter
     * @return
     * @throws Exception
     */
    private String getWFSUrl(String filter) throws Exception {


        String filterXml = "<Filter>" +
                            "<PropertyIsEqualTo>" +
                            "<PropertyName>rakennustunnus</PropertyName>" +
                            "<Literal>" + filter + "</Literal>" +
                            "</PropertyIsEqualTo>" +
                            "</Filter>";
        filterXml = URLEncoder.encode(filterXml, "UTF-8");

        String wfsUrl = serviceURL +
                "?SERVICE=WFS&VERSION=1.1.0" +
                "&maxFeatures=" +  (SearchUtil.maxCount+1) +  // added 1 to maxCount because need to know if there are more then maxCount
                "&REQUEST=GetFeature&TYPENAME=rhr:RakennuksenTunnistetiedot" +
                "&NAMESPACE=xmlns%28rhr=http://xml.nls.fi/Rakennustiedot/VTJRaHu/2009/02%29" +
                "&filter=" + filterXml;

        // log.debug("Our search url is: '" + wfsUrl + "'");
        return wfsUrl;
    }
}
