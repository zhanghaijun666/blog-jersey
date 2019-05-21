package com.server.filter;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanghaijun
 */
@PreMatching
@Priority(Priorities.HEADER_DECORATOR + 50)
public class BlogFilter implements ContainerResponseFilter, ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (!StringUtils.isBlank(path) && !path.startsWith("static")) {
//            Base.close();
//            Base.commitTransaction();
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (!StringUtils.isBlank(path) && !path.startsWith("static")) {
//            ConfigStore.DB db = Configuration.getInstance().getConfig().getDb();
//            Base.open(db.getDriver(), db.getUrl(), db.getUser(), db.getPassword());
//            Base.openTransaction();
        }
    }
}
