package df.learn.MySpringFramework.commons.properties;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.util.ReflectionUtils;


/**
 * @ClassName df.learn.MySpringFramework.commons.properties.PropertyProcessor  
 * 
 * @Version v1.0
 * @Date 2017年9月13日 下午6:05:41 
 * @Author 854154025@qq.com
 * 
 * @Description 自定义properties文件处理器
 * 
 */
public class PropertyProcessor extends PreferencesPlaceholderConfigurer implements BeanPostProcessor {
	private static java.util.Properties pros = new Properties();

	private Class<?>[] enableClassList = { String.class };

	public void setEnableClassList(Class<?>[] enableClassList) {
		this.enableClassList = enableClassList;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		Enumeration<?> enums = props.propertyNames();
		String key, value;
		while (enums.hasMoreElements()) {
			key = (String) enums.nextElement();
			value = props.getProperty(key);
			if (StringUtils.isNotEmpty(value)) {
				pros.setProperty(key, replaceSystemProperties(value.trim()));
			} else {
				pros.setProperty(key, "");
			}
		}
		super.processProperties(beanFactoryToProcess, props);
	}

	public static String replaceSystemProperties(String prop) {
		Pattern p = Pattern.compile("\\$\\{[^\\}]+}");
		Matcher m = p.matcher(prop);
		while (m.find()) {
			String group = m.group(0);
			String key = group.substring(2, group.length() - 1);
			if (StringUtils.isNotEmpty(key)) {
				String value = System.getProperty(key);
				if (StringUtils.isEmpty(value)) {
					value = System.getenv().get(key);
					if (StringUtils.isEmpty(key)) {
						value = "//";
					}
				}
				prop = prop.replace(group, value);
			}
			// map.put(group, val);
		}
		// for(Map.Entry<String, String> entry : map.entrySet()) {
		// prop = prop.replace(entry.getKey(), entry.getValue());
		// }
		return prop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 * 
	 * @Methods postProcessAfterInitialization
	 * 
	 * @param bean
	 * 
	 * @param arg1
	 * 
	 * @return
	 * 
	 * @throws BeansException
	 * 
	 * @Description Property注解的注入，只会在扫描Bean中生效，如Service Controller
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String arg1) throws BeansException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Property.class)) {
				if (filterType(field.getType().toString())) {
					Property p = field.getAnnotation(Property.class);
					try {
						ReflectionUtils.makeAccessible(field);
						field.set(bean, pros.getProperty(p.value()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bean;
	}

	private boolean filterType(String type) {
		if (type != null) {
			for (Class<?> c : enableClassList) {
				if (c.toString().equals(type)) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String arg1) throws BeansException {
		return bean;
	}

	// @Override
	// public void afterPropertiesSet() throws Exception {
	// pros = mergeProperties();
	// }
	// @Override
	// public void afterPropertiesSet() throws Exception {
	// }

	public static String getProperties(String key) {
		return pros.getProperty(key);
	}

	public static long getLong(String key) {
		return NumberUtils.toLong(getProperties(key), 0L);
	}

	public static int getInt(String key) {
		return NumberUtils.toInt(getProperties(key), 0);
	}

}