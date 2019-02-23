package com.service;

import com.db.User;
import com.proto.BlogStore;
import com.server.AppSession;
import com.server.BlogMediaType;
import com.server.SessionFactory;
import com.server.login.LoginAuthenticator;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author zhanghaijun
 */
@Path("/user")
public class UserService {

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @POST
    @Path("/login")
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    public BlogStore.RspInfo login(BlogStore.User requestUser) {
        BlogStore.ReturnCode checkCode = LoginAuthenticator.authenticator(requestUser, response);
        return BlogStore.RspInfo.newBuilder().setCode(checkCode).build();
    }

    @GET
    @Path("/logout")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.RspInfo logout() {
        BlogStore.RspInfo.Builder rspInfo = BlogStore.RspInfo.newBuilder();
        AppSession session = (AppSession) security.getUserPrincipal();
        SessionFactory.instance().removeSession(session);
        response.setCookie("session", "");
        return rspInfo.setCode(BlogStore.ReturnCode.OK).build();
    }

    @PUT
    @Path("/create")
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("admin")
    public BlogStore.RspInfo createNewUser(BlogStore.User user) {
        AppSession session = (AppSession) security.getUserPrincipal();
        BlogStore.RspInfo.Builder rspInfo = BlogStore.RspInfo.newBuilder();
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return rspInfo.setCode(BlogStore.ReturnCode.USERNAME_OR_PASSWORD_IS_EMPTY).build();
        }
        if (User.isExist(user.getUsername())) {
            return rspInfo.setCode(BlogStore.ReturnCode.USER_EXIST).build();
        }
        User.saveUser(user, session.getUserId(), BlogStore.Authenticator.SYSTEM_AUTHENTICATOR_VALUE);
        return rspInfo.setCode(BlogStore.ReturnCode.OK).build();
    }

    @GET
    @Path("/all/{isShowDeleted}")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.UserList getAllActiveUser(@PathParam("isShowDeleted") boolean isShowDeleted) {
        BlogStore.UserList.Builder users = BlogStore.UserList.newBuilder();
        List<User> list = User.findAll().orderBy("status, nickname, username");
        if (null == list || list.isEmpty()) {
            return users.build();
        }
        for (User user : list) {
            if (!isShowDeleted && user.getStatus() == BlogStore.Status.StatusDeleted_VALUE) {
                continue;
            }
            users.addItems(User.UserBuilder(user));
        }
        return users.build();
    }

    @GET
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.User getUser() {
        AppSession session = (AppSession) security.getUserPrincipal();
        if (null == session) {
            return BlogStore.User.getDefaultInstance();
        }
        return User.UserBuilder(User.findById(session.getUserId()));
    }

    @PUT
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("admin")
    public BlogStore.RspInfo updataUser(BlogStore.User user) {
        AppSession session = (AppSession) security.getUserPrincipal();
        BlogStore.RspInfo.Builder rspInfo = BlogStore.RspInfo.newBuilder();
        User dbUser = User.findById(user.getUserId());
        if (null == dbUser) {
            return rspInfo.setCode(BlogStore.ReturnCode.USER_EMPTY).build();
        }
        if (dbUser.getStatus() != BlogStore.Status.StatusActive_VALUE) {
            return rspInfo.setCode(BlogStore.ReturnCode.USER_EMPTY).build();
        }
        if (User.isExist(user.getUsername())) {
            return rspInfo.setCode(BlogStore.ReturnCode.USER_EXIST).build();
        }
        User.saveUser(user, dbUser, session.getUserId());
        return rspInfo.setCode(BlogStore.ReturnCode.OK).build();
    }

    @DELETE
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("admin")
    public BlogStore.RspInfoList deleteUser(BlogStore.UserList users) {
        AppSession session = (AppSession) security.getUserPrincipal();
        BlogStore.RspInfoList.Builder rspInfoList = BlogStore.RspInfoList.newBuilder();
        for (BlogStore.User user : users.getItemsList()) {
            User dbUser = User.findById(user.getUserId());
            if (null == dbUser) {
                rspInfoList.addItems(BlogStore.RspInfo.newBuilder().setCode(BlogStore.ReturnCode.USER_EMPTY).setMsg(user.getUsername()).build());
            } else if (session.getUserId() == dbUser.getUserId()) {
                rspInfoList.addItems(BlogStore.RspInfo.newBuilder().setCode(BlogStore.ReturnCode.NOT_YOURSELF).setMsg(user.getUsername()).build());
            } else {
                dbUser.setStatus(BlogStore.Status.StatusDeleted_VALUE);
                dbUser.saveIt();
            }
        }
        if (rspInfoList.getItemsCount() == 0) {
            rspInfoList.setCode(BlogStore.ReturnCode.OK);
        }
        return rspInfoList.build();
    }
}
