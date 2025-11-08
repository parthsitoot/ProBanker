package com.banking.proBanker.Utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface JsonUtil {
    public static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    public static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    /**
     * Convert an object into JSON string.
     * @param obj the subject to be converted into json.
     * @return JSON string of the object
     */

    public static String toJSON(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
