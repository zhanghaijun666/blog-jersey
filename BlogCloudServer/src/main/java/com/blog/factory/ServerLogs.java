package com.blog.factory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.blog.config.Configuration;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghaijun
 */
public class ServerLogs {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ServerLogs.class);

    public static void logInit() throws JoranException {
        Path logbackPath = Paths.get(Configuration.getInstance().getConfig().getLogback());
        if (logbackPath.toFile().isFile()) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            loggerContext.reset();
            configurator.doConfigure(logbackPath.toFile());
        }
    }

    public static void logAccess(Response response, Request request) {
        logger.info(response.getCode() + " " + getRequestUrl(request)
                + " ip:" + request.getClientAddress().getAddress().getHostAddress()
                + " 耗时：" + (response.getResponseTime() - request.getRequestTime()) + "ms "
                + "[" + request.getValue("User-Agent") + "]");

    }

    private static String getRequestUrl(Request request) {
        return (request.getMajor() == 1 ? "http" : "https") + "://" + request.getValue("Host") + request.getTarget();
    }

//    //.*不匹配换行符
//    private static final Pattern HOST_REG = Pattern.compile("Host:(.*)");
//    private static final Pattern USER_AGENT_REG = Pattern.compile("User-Agent:(.*)");
//
//    private static class RequestParser {
//
//        String protocol = "";
//        String host = "";
//        String path = "";
//        String userAgent = "";
//
//        RequestParser(Request request) {
//            protocol = request.getMajor() == 1 ? "http" : "https";
//            path = request.getTarget();
//            CharSequence header = request.getHeader();
//            Matcher hostMatcher = HOST_REG.matcher(header);
//            if (hostMatcher.find()) {
//                host = hostMatcher.group(1);
//            }
//            Matcher userAgentMatcher = USER_AGENT_REG.matcher(header);
//            if (userAgentMatcher.find()) {
//                userAgent = userAgentMatcher.group(1);
//            }
//        }
//
//        public String getRequestUrl() {
//            return protocol + "://" + host + path;
//        }
//
//    }
}
