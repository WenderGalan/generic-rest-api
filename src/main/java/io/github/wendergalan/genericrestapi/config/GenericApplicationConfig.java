package io.github.wendergalan.genericrestapi.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: GenericApplicationConfig.java
 * Created by : Wender Galan
 * Date created : 13/01/2019
 * *********************************************
 */
@EnableWebMvc
@EnableScheduling
public abstract class GenericApplicationConfig implements WebMvcConfigurer {

    /**
     * The constant DATE_FORMAT.
     */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /**
     * The constant TIME_FORMAT.
     */
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;
    /**
     * The constant DATE_TIME_FORMAT.
     */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * The Environment.
     */
    @Autowired
    protected Environment environment;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        builder.modules(javaTimeModule);

        ObjectMapper mapper = builder.build();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        converters.add(new MappingJackson2HttpMessageConverter(mapper));
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger configuration
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(environment.getProperty("allow.origins", "*"))
                .allowedHeaders("*")
                .allowedMethods("*");
    }

    /**
     * The type Local date serializer.
     */
    public class LocalDateSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider serializers) throws IOException, JsonProcessingException {
            generator.writeString(value.format(DATE_FORMAT));
        }
    }

    /**
     * The type Local date deserializer.
     */
    public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return LocalDate.parse(parser.getValueAsString(), DATE_FORMAT);
        }
    }

    /**
     * The type Local time serializer.
     */
    public class LocalTimeSerializer extends JsonSerializer<LocalTime> {
        @Override
        public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException, JsonProcessingException {
            generator.writeString(value.format(TIME_FORMAT));
        }
    }

    /**
     * The type Local time deserializer.
     */
    public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return LocalTime.parse(parser.getValueAsString(), TIME_FORMAT);
        }
    }

    /**
     * The type Local date time serializer.
     */
    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException, JsonProcessingException {
            generator.writeString(value.format(DATE_TIME_FORMAT));
        }
    }

    /**
     * The type Local date time deserializer.
     */
    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return LocalDateTime.parse(parser.getValueAsString(), DATE_TIME_FORMAT);
        }
    }
}
