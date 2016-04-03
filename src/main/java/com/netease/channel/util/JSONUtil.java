package com.netease.channel.util;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.Writer;

/**
 * JSON工具类
 */
public class JSONUtil {

        private static final ObjectMapper mapper = new ObjectMapper();

        private static final JsonFactory jsonFactory = new JsonFactory();

        private static final Logger log = LoggerFactory
                        .getLogger(JSONUtil.class);

        static {
                mapper.configure(
                                DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                                false);
        }

        public static ObjectMapper getMapper() {
                return mapper;
        }

        public static <T> T fromJson(String jsonAsString,
                        Class<T> pojoClass) {
                try {
                        return mapper.readValue(jsonAsString, pojoClass);
                } catch (Exception e) {
                        log.error(e.getMessage(), e);
                }
                return null;
        }
        
        public static <T> T fromJson(String jsonAsString,TypeReference<T> typeRef){
                try {
                        return mapper.readValue(jsonAsString, typeRef);
                } catch (Exception e) {
                        log.error(e.getMessage(), e);
                }
                return null;
        }
        
        public static <T> T parseResponse(String jsonAsString,TypeReference<T> typeRef){
                try {
                        JsonNode node=mapper.readTree(jsonAsString);
                        int code=node.path("success").getIntValue();
                        if(code==200){
                                return mapper.readValue(node.path("data"), typeRef);
                        }
                } catch (Exception e) {
                        log.error(e.getMessage(), e);
                }
                return null;
        }
        
        public static String toJson(Object pojo){
                return toJson(pojo,false);
        }

        public static String toJson(Object pojo, boolean prettyPrint) {
                try {
                        StringWriter sw = new StringWriter();
                        JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
                        if (prettyPrint) {
                                jg.useDefaultPrettyPrinter();
                        }
                        mapper.writeValue(jg, pojo);
                        return sw.toString();
                } catch (Exception e) {
                        log.error(e.getMessage(), e);
                }
                return null;
        }
        
        public static void writeJson(Object pojo,Writer writer){
                try {
                        JsonGenerator jg = jsonFactory.createJsonGenerator(writer);
                        mapper.writeValue(jg, pojo);
                } catch (Exception e) {
                        log.error(e.getMessage(), e);
                }
        }

}
