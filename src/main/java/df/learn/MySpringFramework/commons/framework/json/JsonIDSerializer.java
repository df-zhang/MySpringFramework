package df.learn.MySpringFramework.commons.framework.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.commons.framework.json.JsonIDSerializer
 * 
 * @Version v1.0
 * @Date 2017年9月13日 下午8:50:26
 * @Author 854154025@qq.com
 * 
 * @Description 针对某些继承自BaseEntity的实体类在转换为JSON时只需要输出ID的处理。
 * 
 */
public class JsonIDSerializer extends JsonSerializer<BaseEntity> {

	@Override
	public void serialize(BaseEntity value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		if (value != null) {
			gen.writeNumber(value.getId());
		} else {
			gen.writeNumber(0);
		}
	}
}
