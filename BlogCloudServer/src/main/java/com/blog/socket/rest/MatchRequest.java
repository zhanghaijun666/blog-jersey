package com.blog.socket.rest;

public class MatchRequest {

   private final Class type;
   
   public MatchRequest(Class type) {
      this.type = type;
   }
   
   public Class getType() {
      return type;
   }
}
