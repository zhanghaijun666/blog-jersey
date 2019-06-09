package com.blog.socket.rest;

public interface MessageListener<T> {
   void onMessage(T message) throws Exception;
}
