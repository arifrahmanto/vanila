package com.vanila.service.app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.apache.wink.common.internal.providers.entity.xml.JAXBCollectionXmlProvider;
import org.apache.wink.providers.protobuf.WinkProtobufProvider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.vanila.service.OrderService;
import com.vanila.service.provider.ProtobufXmlJsonProvider;

/**
 *
 * User: purwol Date: Feb 2, 2016 Time: 11:38:22 PM
 */

public class RestApplication extends Application {

    public static ObjectMapper getJacksonJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                .with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).with(MapperFeature.AUTO_DETECT_CREATORS)
                .with(MapperFeature.AUTO_DETECT_SETTERS).with(MapperFeature.AUTO_DETECT_FIELDS);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.getSerializationConfig().without(SerializationFeature.INDENT_OUTPUT)
                .without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .without(SerializationFeature.FAIL_ON_EMPTY_BEANS)

                .with(SerializationFeature.WRITE_ENUMS_USING_TO_STRING).with(MapperFeature.AUTO_DETECT_GETTERS)
                .with(MapperFeature.AUTO_DETECT_IS_GETTERS).with(MapperFeature.AUTO_DETECT_FIELDS);

        AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();
        mapper.getDeserializationConfig().with(jacksonIntrospector);
        mapper.getSerializationConfig().with(jacksonIntrospector);
        return mapper;
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(OrderService.class);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();
        // XML
        singletons.add(new JAXBCollectionXmlProvider());

        // JSON
        ObjectMapper mapper = getJacksonJsonMapper();
        JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
        jaxbProvider.setMapper(mapper);
        singletons.add(jaxbProvider);

        // Protobuf
        WinkProtobufProvider protobufProvider = new WinkProtobufProvider();
        singletons.add(protobufProvider);

        // Protobuf as XML or JSON
        singletons.add(new ProtobufXmlJsonProvider());

        return singletons;
    }

}
