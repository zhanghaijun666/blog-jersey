package com.blog.factory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.blog.config.Configuration;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghaijun
 */
public class ServerLogs {
    private static final Logger logger = LoggerFactory.getLogger(ServerLogs.class);
    
    public static void logInit() throws JoranException {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(new File(Configuration.getInstance().getConfig().getLogback()));
    }
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void logAccess(Response response, Request request){
        try{
            RequestParser requestParser = new RequestParser(request);
            logger.info(request.getClientAddress().getAddress().getHostAddress()
                    + " " + dateFormat.format(new Date(request.getRequestTime()))
                    + " " + requestParser.getRequestUrl()
                    + " " + (response.getResponseTime() - request.getRequestTime()) + "ms " 
                    + response.getCode() 
                    + " " + request.getMethod() 
                    + " "+ request.getPath().toString() 
                    + " " + requestParser.userAgent);
        }catch(Throwable e){
        }
    }
    
    //.*不匹配换行符
    private static final Pattern HOST_REG = Pattern.compile("Host:(.*)");
    private static final Pattern USER_AGENT_REG = Pattern.compile("User-Agent:(.*)");
    private static class RequestParser{
        String protocol = "";
        String host = "";
        String path = "";
        String userAgent = "";
        
        RequestParser(Request request){
            protocol = request.getMajor() == 1 ? "http" : "https";
            path = request.getTarget();
            CharSequence header = request.getHeader();
            Matcher hostMatcher = HOST_REG.matcher(header);
            if (hostMatcher.find()) {
                host = hostMatcher.group(1);
            }
            Matcher userAgentMatcher = USER_AGENT_REG.matcher(header);
            if (userAgentMatcher.find()) {
                userAgent = userAgentMatcher.group(1);
            }
        }

        public String getRequestUrl() {
            return protocol + "://" + host + path;
        }
        
    }
}
