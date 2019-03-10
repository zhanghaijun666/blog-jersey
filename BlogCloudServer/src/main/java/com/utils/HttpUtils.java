package com.utils;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.http.Request;
import org.slf4j.LoggerFactory;

/**
 * @author haijun.zhang
 */
public class HttpUtils {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static String getIpAddress(Request req) {
        String ip = HttpUtils.doGetClientAddress(req);
        return StringUtils.isEmpty(ip) ? "" : ip;
    }

    private static String doGetClientAddress(Request req) {
        String forwordFor = req.getValue("X-Forwarded-For");//remote ip if we are behind haproxy/nginx
        if (StringUtils.isNoneBlank(forwordFor)) {
            if (forwordFor.contains(",")) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Forwarded by multiple proxies:" + forwordFor);
                }
                forwordFor = forwordFor.substring(0, forwordFor.indexOf(','));
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Forwarded for real ip:" + forwordFor);
            }
            return forwordFor;
        }
        return req.getClientAddress().getAddress().getHostAddress();
    }
}
