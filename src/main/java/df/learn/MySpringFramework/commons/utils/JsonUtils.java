package df.learn.MySpringFramework.commons.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class JsonUtils {
	protected static final ObjectMapper jacksonMapper = Jackson2ObjectMapperBuilder.json().build();
	private static final Logger logger = Logger.getLogger(JsonUtils.class);

	static {
		// jacksonMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,
		// true);
		// jacksonMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,
		// true);

		jacksonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		jacksonMapper.setSerializationInclusion(Include.NON_NULL);
//		jacksonMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//			@Override
//			public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
//				jgen.writeString("");
//			}
//		});
		jacksonMapper.registerModule(new Hibernate4Module().disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION));
	}

	public static ObjectMapper getJacksonMapper() {
		return jacksonMapper;
	}

	public static String toJsonString(Object obj) {
		try {
			return getJacksonMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2Map(String jsonStr) {
		try {
			return getJacksonMapper().readValue(jsonStr, Map.class);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static boolean isJson(String str) {
		try {
			getJacksonMapper().readValue(str, Map.class);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public static String map2Json(Map<String, Object> map) {
		try {
			return getJacksonMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return "{}";
	}

	public static <T> T json2Obj(String jsonStr, Class<T> cls) {
		try {
			return getJacksonMapper().readValue(jsonStr, cls);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> T json2Obj(String jsonStr, TypeReference<T> typeReference) {
		try {
			return getJacksonMapper().readValue(jsonStr, typeReference);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
