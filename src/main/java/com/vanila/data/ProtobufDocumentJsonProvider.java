package com.vanila.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.wink.common.annotations.Scope;

import com.google.common.base.Charsets;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

@Scope(Scope.ScopeType.SINGLETON)
@Provider
@Produces({ ProtocolBufferMediaType.APPLICATION_PROTOBUF })
@Consumes({ ProtocolBufferMediaType.APPLICATION_JSON })
public class ProtobufDocumentJsonProvider<T extends Message> implements MessageBodyReader<T>, MessageBodyWriter<T> {

    private byte[] getMessageAsJSON(Message generatedMessage) {
        return JsonFormat.printToString(generatedMessage).getBytes(Charsets.UTF_8);
    }

    @Override
    public long getSize(T message, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        long size;
        if (mediaType.isCompatible(ProtocolBufferMediaType.APPLICATION_JSON_TYPE)) {
            size = getMessageAsJSON(message).length;
        } else if (mediaType.isCompatible(ProtocolBufferMediaType.APPLICATION_PROTOBUF_TYPE)) {
            size = message.getSerializedSize();
        } else {
            size = 0;
        }

        return size;
    }

    @Override
    public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
        return Message.class.isAssignableFrom(arg0);
    }

    @Override
    public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
        return Message.class.isAssignableFrom(arg0);
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> arg4httpHeaders, InputStream entityStream) throws IOException {
        String data = IOUtils.toString(entityStream, Charsets.UTF_8.name());

        Message.Builder builder;
        try {
            Method newBuilder = type.getDeclaredMethod("newBuilder");
            builder = (Message.Builder) newBuilder.invoke(type);
        } catch (NoSuchMethodException e) {
            throw new IllegalAccessError("Failed to find builder method on Protobuf message object.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalAccessError("Failed to invoke builder method on Protobuf message object.");
        }

        if (mediaType.isCompatible(ProtocolBufferMediaType.APPLICATION_JSON_TYPE)) {
            JsonFormat.merge(data, builder);
        } else if (mediaType.isCompatible(ProtocolBufferMediaType.APPLICATION_PROTOBUF_TYPE)) {
            builder.mergeFrom(entityStream);
        }

        return (T) builder.build();
    }

    @Override
    public void writeTo(T message, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        if (mediaType.isCompatible(ProtocolBufferMediaType.APPLICATION_JSON_TYPE)) {
            entityStream.write(getMessageAsJSON(message));
        } else if (mediaType.isCompatible(ProtocolBufferMediaType.APPLICATION_PROTOBUF_TYPE)) {
            entityStream.write(message.toByteArray());
        }
    }

}
