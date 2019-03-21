package com.jersey.provider;

import com.google.protobuf.Message;
import com.blog.utils.BlogMediaType;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

/**
 * @author zhanghaijun
 */
@Provider
@Consumes(value = {BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
public class ProtobufReader implements MessageBodyReader<Message> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }

    @Override
    public Message readFrom(Class<Message> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        try {
            Method newBuilder = type.getMethod("newBuilder");
            Message.Builder builder = (Message.Builder) newBuilder.invoke(type);
            return builder.mergeFrom(entityStream).build();
        } catch (IOException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            throw new WebApplicationException(ex);
        }
    }

}
