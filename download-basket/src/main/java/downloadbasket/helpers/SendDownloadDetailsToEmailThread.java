package downloadbasket.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import downloadbasket.data.ErrorReportDetails;
import fi.nls.oskari.domain.Role;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.domain.map.OskariLayer;
import fi.nls.oskari.geoserver.LayerHelper;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.map.layer.OskariLayerService;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.IOHelper;
import downloadbasket.data.LoadZipDetails;
import downloadbasket.data.NormalWayDownloads;
import downloadbasket.data.ZipDownloadDetails;
import org.apache.commons.mail.HtmlEmail;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Send download details email service (thread).
 */
public class SendDownloadDetailsToEmailThread implements Runnable {
	private final OskariLayerService mapLayerService;
	private final JSONArray downloadDetails;
	private final JSONObject userDetails;
	private final Locale locale;
	private final User user;

	private final Logger LOGGER = LogFactory.getLogger(SendDownloadDetailsToEmailThread.class);
	private final String PARAM_CROPPING_MODE = "croppingMode";
	private final String PARAM_CROPPING_LAYER = "croppingLayer";
	private final String PARAM_LAYER = "layer";
	private final String PARAM_LAYER_ID = "id";
	private final String FULL_DETAIL_LAYER = "fullDetailLayer";
	private final String DOWNLOAD_BASKET_LEVEL = "downloadBasketLevel";
	private final int ADMIN_LEVEL = 4;
	private final int HIGH_PRIORITY_LEVEL = 3;
	private final int MEDIUM_PRIORITY_LEVEL = 2;
	private final int LOW_PRIORITY_LEVEL = 1;
	private final int DEFAULT_LEVEL = 0;

	/**
	 * Constructor.
	 *
	 * @param mapLayerService
	 *            map layer service
	 * @param downloadDetails
	 *            download details
	 * @param userDetails
	 *            user details
	 * @param locale locale
	 * @param user user
	 */

	public SendDownloadDetailsToEmailThread(OskariLayerService mapLayerService, JSONArray downloadDetails,
											JSONObject userDetails, Locale locale, User user) {
		this.downloadDetails = downloadDetails;
		this.userDetails = userDetails;
		this.locale = locale;
		this.mapLayerService = mapLayerService;
		this.user = user;
	}

	private ArrayList<ZipDownloadDetails> downloadFromService() {
		ArrayList<ZipDownloadDetails> mergeThese = new ArrayList<ZipDownloadDetails>();
		final String strTempDir = PropertyUtil.get("oskari.wfs.download.folder.name");
		NormalWayDownloads normalDownloads = new NormalWayDownloads();
		for (String download : PropertyUtil.getCommaSeparatedList("oskari.wfs.download.normal.way.downloads")) {
			normalDownloads.addDownload(download);
		}

		String[] lowPriorityRoles = PropertyUtil.getCommaSeparatedList("oskari.wfs.download.low_priority_roles");
		String[] mediumPriorityRoles = PropertyUtil.getCommaSeparatedList("oskari.wfs.download.medium_priority_roles");
		String[] highPriorityRoles = PropertyUtil.getCommaSeparatedList("oskari.wfs.download.high_priority_roles");
		int numLowPriorityRoles = lowPriorityRoles.length;
		int numMediumPriorityRoles = mediumPriorityRoles.length;
		int numHighPriorityRoles = highPriorityRoles.length;
		int downloadBasketRoleLevel = DEFAULT_LEVEL;
		if (this.user.isAdmin()) {
			downloadBasketRoleLevel = ADMIN_LEVEL;
		} else {
			Set<Role> roles = this.user.getRoles();
			iterateRoles: for (Role role : roles) {
				String roleName = role.getName();
				for (int i = 0; i < numHighPriorityRoles; i++) {
					if (roleName.equals(highPriorityRoles[i])) {
						if (downloadBasketRoleLevel < HIGH_PRIORITY_LEVEL) {
							downloadBasketRoleLevel = HIGH_PRIORITY_LEVEL;
						}
						continue iterateRoles;
					}
				}
				for (int i = 0; i < numMediumPriorityRoles; i++) {
					if (roleName.equals(mediumPriorityRoles[i])) {
						if (downloadBasketRoleLevel < MEDIUM_PRIORITY_LEVEL) {
							downloadBasketRoleLevel = MEDIUM_PRIORITY_LEVEL;
						}
						continue iterateRoles;
					}
				}
				for (int i = 0; i < numLowPriorityRoles; i++) {
					if (roleName.equals(lowPriorityRoles[i])) {
						if (downloadBasketRoleLevel < LOW_PRIORITY_LEVEL) {
							downloadBasketRoleLevel = LOW_PRIORITY_LEVEL;
						}
						continue iterateRoles;
					}
				}
			}
		}

		try {
			DownloadServices ds = new DownloadServices();
			for (int i = 0; i < downloadDetails.length(); i++) {
				JSONObject download = downloadDetails.getJSONObject(i);
				final String croppingMode = download.getString(PARAM_CROPPING_MODE);
				String croppingLayer = "";
				if (download.has(PARAM_CROPPING_LAYER)) {
					croppingLayer = download.getString(PARAM_CROPPING_LAYER);
				}

				LoadZipDetails ldz = new LoadZipDetails();
				ldz.setTemporaryDirectory(strTempDir);
				ldz.setUserEmail(userDetails.getString("email"));
				ldz.setLanguage(this.locale.getLanguage());
				ldz.setDownloadNormalWay(normalDownloads.isBboxCropping(croppingMode, croppingLayer));
				OskariLayer oskariLayer = mapLayerService.find(download.getString(PARAM_LAYER_ID));

				JSONObject attributes = oskariLayer.getAttributes();
				if ((attributes == null) || (!attributes.has(DOWNLOAD_BASKET_LEVEL))) {
					continue;
				}
				int downloadBasketLevel = attributes.getInt(DOWNLOAD_BASKET_LEVEL);
				if (downloadBasketRoleLevel < downloadBasketLevel) {
					continue;
				}

				OskariLayer dataLayer;
				if ((downloadBasketRoleLevel == HIGH_PRIORITY_LEVEL) && (attributes.has(FULL_DETAIL_LAYER))) {
					String fullDetailLayerName = attributes.getString(FULL_DETAIL_LAYER);
					dataLayer = LayerHelper.getLayerWithName(fullDetailLayerName);
				} else {
					dataLayer = oskariLayer;
				}

				String url = dataLayer.getUrl();
				String srs = "EPSG:4326";
				if (dataLayer != null) {
					srs = dataLayer.getSrs_name();
				}

				ldz.setGetFeatureInfoRequest(OGCServices.getFilter(download, true, dataLayer));
				ldz.setWFSUrl(OGCServices.doGetFeatureUrl(srs, dataLayer, false, url));

				final String fileLocation = ds.loadZip(ldz, this.locale);

				if (fileLocation != null && ds.isValid(new File(fileLocation))) {
					ZipDownloadDetails zdd = new ZipDownloadDetails();
					zdd.setFileName(fileLocation);
					final String sLayer = Helpers.getLayerNameWithoutNameSpace(dataLayer.getName());
					zdd.setLayerName(sLayer);
					mergeThese.add(zdd);
				} else {
					ErrorReportDetails erd = new ErrorReportDetails();
					erd.setErrorFileLocation(fileLocation);
					erd.setWfsUrl(ldz.getWFSUrl());
					erd.setXmlRequest(ldz.getGetFeatureInfoRequest());
					erd.setUserEmail(ldz.getUserEmail());
					erd.setLanguage(locale.getLanguage());
					ds.sendErrorReportToEmail(erd);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Cannot download shape zip.", ex);
		}

		return mergeThese;
	}

	private String mergeZipsToOne(ArrayList<ZipDownloadDetails> mergeTheseFiles) {
		final String strTempDir = PropertyUtil.get("oskari.wfs.download.folder.name");
		ZipOutputStream out = null;
		String strZipFileName = UUID.randomUUID().toString() + ".zip";
		try {
			File f = new File(strTempDir);
			f.mkdirs();
			out = new ZipOutputStream(new FileOutputStream(new File(strTempDir, strZipFileName)));

			Hashtable<String, Integer> indexes = new Hashtable<String, Integer>();
			byte[] buffer = new byte[1024];

			for (int i = 0; i < mergeTheseFiles.size(); i++) {
				ZipInputStream in = null;

				try {
					ZipDownloadDetails zdd = mergeTheseFiles.get(i);
					String strTempFile = zdd.getFileName();

					Integer index = indexes.get(zdd.getLayerName());
					if (index == null) {
						index = 0;
					} else {
						index++;
						indexes.remove(zdd.getLayerName());
					}

					indexes.put(zdd.getLayerName(), index);

					String folderName = zdd.getLayerName() + "_" + index + "/";
					out.putNextEntry(new ZipEntry(folderName));

					in = new ZipInputStream(new FileInputStream(strTempFile));
					ZipEntry ze = in.getNextEntry();
					while (ze != null) {
						String fileName = ze.getName();
						out.putNextEntry(new ZipEntry(folderName + fileName));
						int len;
						while ((len = in.read(buffer)) > 0) {
							out.write(buffer, 0, len);
						}
						ze = in.getNextEntry();
					}

					out.closeEntry();
					deleteFile(strTempFile);

				} catch (Exception ex) {
					LOGGER.error("Cannot parse JSON download details", ex);
				} finally {
					if (in != null)
						IOHelper.close(in);
				}
			}

		} catch (FileNotFoundException fe) {
			LOGGER.error("File not found", fe);
		} catch (Exception ex) {
			LOGGER.error("Error", ex);
		} finally {
			IOHelper.close(out);
		}

		return strZipFileName;
	}

	/**
	 *
	 * Overrides the run method. Collects the download materials and sends them
	 * using the variables given in constructor.
	 *
	 */
	@Override
	public void run() {

		try {

			// Downlaod all shapes from service
			ArrayList<ZipDownloadDetails> mergeThese = downloadFromService();

			// Merge all zip files to one
			String mergedZipFileName = mergeZipsToOne(mergeThese);

			// Send zipped file to email
			sendZipFile(mergedZipFileName);

		} catch (Exception ex) {
			LOGGER.error("Cannot download shape zip.", ex);
		}
	}

	/**
	 *
	 * Sends the zip file to current user's email address.
	 *
	 * @param strZipFileName
	 *            zip file name
	 */
	public void sendZipFile(final String strZipFileName) {

		try {
			HtmlEmail email = new HtmlEmail();

			int smtpPort = Integer.parseInt(PropertyUtil.getNecessary("oskari.wfs.download.smtp.port"));
			email.setSmtpPort(smtpPort);
			email.setHostName(PropertyUtil.getNecessary("oskari.wfs.download.smtp.host"));
			email.setFrom(PropertyUtil.getNecessary("oskari.wfs.download.email.from"));
			email.setSubject(PropertyUtil.getNecessary("oskari.wfs.download.email.subject"));
			email.setCharset("UTF-8");

			StringBuilder htmlHeader = new StringBuilder();
			StringBuilder htmlMsg = new StringBuilder();
			StringBuilder htmlFooter = new StringBuilder();

			StringBuilder txtHeader = new StringBuilder();
			StringBuilder txtMsg = new StringBuilder();
			StringBuilder txtFooter = new StringBuilder();

			htmlHeader.append(PropertyUtil.getNecessary("oskari.wfs.download.email.header"));
			txtHeader.append(PropertyUtil.getNecessary("oskari.wfs.download.email.header"));

			htmlHeader.append("<br/><br/>");
			txtHeader.append("\n\n");
			htmlMsg.append(PropertyUtil.getNecessary("oskari.wfs.download.email.message"));
			txtMsg.append(PropertyUtil.getNecessary("oskari.wfs.download.email.message"));

			htmlMsg.append("<br/>");
			txtMsg.append("\n");

			String url = PropertyUtil.getNecessary("oskari.wfs.download.link.url.prefix") + strZipFileName;
			htmlMsg.append("<a href=\"" + url + "\">" + url + "</a>");
			txtMsg.append(url);

			htmlFooter.append("<br/><br/>");
			txtFooter.append("\n\n");
			String f = PropertyUtil.get("oskari.wfs.download.email.footer", "");
			String ff = f.replaceAll("\\{LINEBREAK\\}", "\n");
			f = f.replaceAll("\\{LINEBREAK\\}", "<br/>");
			htmlFooter.append(f);
			txtFooter.append(ff);
			String d = PropertyUtil.get("oskari.wfs.download.email.message.datadescription", "");
			String dd = d.replaceAll("\\{LINEBREAK\\}", "\n");
			d = d.replaceAll("\\{LINEBREAK\\}", "<br/>");
			htmlFooter.append(d);
			txtFooter.append(dd);
			htmlFooter.append(PropertyUtil.get("oskari.wfs.download.email.datadescription_link", ""));
			txtFooter.append(PropertyUtil.get("oskari.wfs.download.email.datadescription_link", ""));

			String htmlFullMessage = "<html>" + htmlHeader.toString() + htmlMsg.toString() + htmlFooter.toString()
					+ "</html>";

			String txtFullMessage = txtHeader.toString() + txtMsg.toString() + txtFooter.toString();

			email.setHtmlMsg(htmlFullMessage);
			email.setTextMsg(txtFullMessage);
			email.addTo(userDetails.getString("email"));
			email.send();
		} catch (Exception ex) {
			LOGGER.error("HTML email error", ex);
		}
	}

	/**
	 * Delete temp files.
	 *
	 * @param strFilePath
	 *            temp path
	 */
	private void deleteFile(String strFilePath) {

		File f = new File(strFilePath);
		if (f.exists() && f.canWrite()) {
			f.delete();
		}
	}
}
