package com.jersey.provider;

import com.google.protobuf.Message;
import com.blog.utils.BlogMediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.WeakHashMap;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * @author zhanghaijun
 */
@Provider
@Produces(value = {BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
public class ProtobufWriter implements MessageBodyWriter<Message> {

    private final Map<Message, byte[]> buffer = new WeakHashMap<>();

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Message t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            t.writeTo(baos);
        } catch (IOException e) {
            return -1;
        }
        byte[] bytes = baos.toByteArray();
        buffer.put(t, bytes);
        return bytes.length;
    }

    @Override
    public void writeTo(Message t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
//        entityStream.write(buffer.remove(t));
        byte[] b = buffer.remove(t);
        if (b == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                t.writeTo(baos);
                b = baos.toByteArray();
            } catch (IOException e) {
                b = new byte[0];
            }
        }
        entityStream.write(b);
    }

}
