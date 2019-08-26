package com.blog.controller;

import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
@Path("/demo")
public class ADemo {

    @GET
    @Path("/images/{image}")
    @Produces("image/*")
    public Response getImage(@PathParam("image") String image) {
        if (StringUtils.isBlank(image)) {
            Response.status(Response.Status.NOT_FOUND).entity("Item, " + image + ", is not found").type("text/plain").build();
        }
        File file = new File(image);
        if (!file.exists()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(file, new MimetypesFileTypeMap().getContentType(file)).build();
//        return Response.created(createdUri).build();
    }
}
