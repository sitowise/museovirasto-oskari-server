package downloadbasket.helpers;

import fi.nls.oskari.log.LogFactory;

import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.IOHelper;
import downloadbasket.data.ErrorReportDetails;
import downloadbasket.data.LoadZipDetails;
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
import java.util.UUID;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.springframework.context.MessageSource;
import fi.nls.oskari.spring.SpringContextHolder;

/**
 * Download services.
 *
 */
public class DownloadServices {
	private final Logger LOGGER = LogFactory.getLogger(DownloadServices.class);

	private MessageSource messages;

	/**
	 * Default Constructor.
	 */
	public DownloadServices() {
	}

	/**
	 * Load shape-ZIP from Geoserver.
	 *
	 * @param ldz
	 *            load zip details
	 * @return filename file name
	 * @throws IOException
	 *
	 *             Normal way download uses BBOX as the cropping method.
	 *             Otherwise, filter plugin is used.
	 *
	 */
	public String loadZip(LoadZipDetails ldz, Locale locale) throws IOException {
		String realFileName = null;
		HttpURLConnection conn = null;

		try {
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

			String filename = UUID.randomUUID().toString();
			String strTempDir = ldz.getTemporaryDirectory();
			File dir0 = new File(strTempDir);
			dir0.mkdirs();
			String gmlFileName = filename + ".gml";
			String zipFileName = filename + ".zip";
			File gmlFile = new File(dir0, gmlFileName);
			File zipFile = new File(dir0, zipFileName);

			try (InputStream istream = conn.getInputStream();
					OutputStream ostream = new FileOutputStream(gmlFile)) {
				IOHelper.copy(istream, ostream);
				FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zipOut = new ZipOutputStream(fos);
				FileInputStream fis = new FileInputStream(gmlFile);
				ZipEntry zipEntry = new ZipEntry(gmlFile.getName());
				zipOut.putNextEntry(zipEntry);
				final byte[] bytes = new byte[1024];
				int length;
				while((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
				zipOut.close();
				fis.close();
				fos.close();
				Files.deleteIfExists(gmlFile.toPath());
				realFileName = new File(dir0, zipFileName).getAbsolutePath();
			}

		} catch (Exception ex) {
			LOGGER.error("Error: ", ex);
		}
		return realFileName;
	}

	/**
	 * Check if zipfile is valid.
	 *
	 * @param file
	 *            zip file
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
		return getMessages().getMessage(key, new String[] {}, new Locale(language));
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
