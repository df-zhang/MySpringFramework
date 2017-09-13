package df.learn.MySpringFramework.commons.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName df.learn.MySpringFramework.commons.properties.Property  
 * 
 * @Version v1.0
 * @Date 2017年9月13日 下午6:06:01 
 * @Author 854154025@qq.com
 * 
 * @Description 通过该注解获得config.properties中的内容，其value为key
 * 
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
	String value();
}
