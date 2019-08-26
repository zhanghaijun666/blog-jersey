package com.blog.controller;

import com.blog.db.Menu;
import com.blog.config.BlogStore;
import com.blog.utils.BlogMediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * @author haijun.zhang
 */
@Path("menu")
public class MenuContorller {

    @GET
    @Path("/hash/{hash}")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    public BlogStore.MenuList getHashMenus(@PathParam("hash") String hash) {
        return Menu.getHashMenus(hash);
    }
}
