package df.learn.MySpringFramework.commons.framework.json;

import java.io.IOException;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * JSON 日期序列化，数据库存放1970年到现在的毫秒数，输出JSON时转换为指定格式日期字符串
 * 
 * @author zdf
 * 
 */
public class JsonDateSerializer extends JsonSerializer<Long> {
	@Override
	public void serialize(Long date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		if (date != null) {
			jsonGenerator.writeString(DateFormatUtils.ISO_DATETIME_FORMAT.format(date));
		}
	}
}
