package com.blog.socket.rest;

public interface RequestRouter {
   <T> void register(RequestHandler<T> handler, Class<T> type, String prefix);
}
