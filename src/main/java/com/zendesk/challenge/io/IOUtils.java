package com.zendesk.challenge.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.zendesk.challenge.domain.commons.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zendesk.challenge.App.DATETIME_FORMAT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IOUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setDateFormat(new SimpleDateFormat(DATETIME_FORMAT));


    public static <T> List<T> getDataJackson(String filePath, Class<T> clazz) {
        try (Stream<String> lines = Files.lines(new File(filePath).toPath(), Charset.defaultCharset())) {
            String data = lines.collect(Collectors.joining());
            CollectionType typeReference =
                    TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
            return OBJECT_MAPPER.readValue(data, typeReference);
        } catch (IOException e) {
            System.err.println("Expected data missing or malformed...");
            System.err.println("Please check the data source and try again - " + filePath);
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    @SneakyThrows
    public static String prettyFormat(Set<? extends Entity> entities) {
        return OBJECT_MAPPER.writeValueAsString(entities);
    }

}
