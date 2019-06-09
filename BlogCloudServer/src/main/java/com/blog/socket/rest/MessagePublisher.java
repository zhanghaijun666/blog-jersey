package com.blog.socket.rest;

public interface MessagePublisher {
   void publish(Object value) throws Exception;
}
