package com.tools;

import com.blog.proto.BlogStore;
import com.verhas.licensor.ExtendedLicense;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghaijun
 */
public class LicenseUtil {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LicenseUtil.class);

    private static byte[] produceLicense(BlogStore.License license) {
        try {
            if (StringUtils.isEmpty(license.getCompany()) || StringUtils.isEmpty(license.getEdition())
                    || StringUtils.isEmpty(license.getProductName()) || StringUtils.isEmpty(license.getShortProductName())) {
                return null;
            }
            StringBuilder licenseStr = new StringBuilder();
            licenseStr.append("company=").append(license.getCompany()).append("\r\n");
            licenseStr.append("product_name=").append(license.getProductName()).append("\r\n");
            licenseStr.append("short_product_name=").append(license.getShortProductName()).append("\r\n");
            licenseStr.append("edition=").append(license.getEdition()).append("\r\n");
            licenseStr.append("valid-until=").append(new SimpleDateFormat("yyyy-MM-dd").format(new Date(license.getValidUntil()))).append("\r\n");
            licenseStr.append("total-user=").append(license.getTotalUser()).append("\r\n");
            licenseStr.append("total-volume=").append(license.getTotalVolume()).append("\r\n");
            ExtendedLicense extendedLicense = new ExtendedLicense();
            extendedLicense.setLicense(licenseStr.toString());
            extendedLicense.loadKey(LicenseUtil.class.getResourceAsStream("/config/secring.gpg"), "bjtxra (Bedrock Cloud License) <bjtxra@bjtxra.com>");
            extendedLicense.generateLicenseId();
            return extendedLicense.encodeLicense("bjtxra").getBytes("utf-8");
        } catch (Exception ex) {
            logger.warn("Unable to Produce License  for " + license.toString(), ex);
        }
        return null;
    }
    
    
    
    public static BlogStore.License getLicenseFromByte(byte[] licenseByte) {
        try {
            InputStream stream = new ByteArrayInputStream(licenseByte);
            ExtendedLicense license = new ExtendedLicense();
            license.loadKeyRing(LicenseUtil.class.getClassLoader().getResourceAsStream("/config/pubring.gpg"), null).setLicenseEncoded(stream);
            return buildLicense(license);
        } catch (Exception e) {
            logger.info("Starting server init license ", e);
        }
        return null;
    }
    protected static BlogStore.License buildLicense(ExtendedLicense license) throws IllegalAccessException, ParseException, InstantiationException, MalformedURLException {
        if (license.isVerified()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(license.getFeature("valid-until", java.util.Date.class));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            BlogStore.License.Builder licenseBuilder = BlogStore.License.newBuilder();
            licenseBuilder.setCompany(license.getFeature("company"))
                    .setProductName(BasicConvertUtils.toString(license.getFeature("product_name"),""))
                    .setShortProductName(BasicConvertUtils.toString(license.getFeature("short_product_name"),""))
                    .setEdition(license.getFeature("edition"))
                    .setLicenseId(license.getFeature("licenseId"))
                    .setValidUntil(cal.getTimeInMillis())
                    .setTotalUser(BasicConvertUtils.toInteger(license.getFeature("total-user"), -1))
                    .setTotalVolume(license.getFeature("total-volume") == null ? -1 : ByteSizeUtil.parseVolume(license.getFeature("total-volume")));
            return licenseBuilder.build();
        }
        return null;
    }
    
    public static BlogStore.License getLicenseFromRecource() {
        try {
            if (new File("conf/defaults.lic").canRead()) {
                ExtendedLicense license = new ExtendedLicense();
                if (license.loadKeyRing(LicenseUtil.class.getClassLoader().getResourceAsStream("pubring.gpg"), null)
                        .setLicenseEncodedFromFile("config/defaults.lic").isVerified() && !license.isExpired()) {
                    return buildLicense(license);
                }
            }
            ExtendedLicense license = new ExtendedLicense();
            if (license.loadKeyRing(LicenseUtil.class.getClassLoader().getResourceAsStream("pubring.gpg"), null)
                    .setLicenseEncodedFromResource("defaults.lic").isVerified()) {
                return buildLicense(license);
            }
        } catch (Exception e) {
            logger.warn("Starting server init license ", e);
        }
        return null;
    }
}
