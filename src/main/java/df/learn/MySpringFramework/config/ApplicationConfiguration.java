package df.learn.MySpringFramework.config;

import java.nio.charset.Charset;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import df.learn.MySpringFramework.commons.properties.PropertyProcessor;
import df.learn.MySpringFramework.config.web.SpringContextInitializeListener;

/**
 * @ClassName df.learn.MySpringFramework.config.ApplicationConfiguration
 * 
 * @Version v1.0
 * @Date 2017年9月13日 下午5:37:22
 * @Author 854154025@qq.com
 * 
 * @Description TODO
 * 
 */
@Configuration // 标注为Spring配置文件，等同于spring-xxx.xml
@EnableScheduling // 启用Spring-Schedule
@EnableCaching // 同理 启用Spring-Cache
// 扫描包，包路径指定后，会将包内的所有类实例化并缓存到Spring容器中，供Ioc注入
@ComponentScan(basePackages = { ApplicationConfiguration.PACKAGE_SERVICES, ApplicationConfiguration.PACKAGES_QUARTZ })
public class ApplicationConfiguration {
	/**
	 * 日志是否输出Debug
	 */
	public static final boolean IS_LOGGER_DEBUG = true;
	/**
	 * 上下文字符集自定
	 */
	public static final String APP_ENCODING = "UTF-8";
	public static final Charset APP_CHARSET = Charset.forName(ApplicationConfiguration.APP_ENCODING);
	/**
	 * 定义包名路径常量，通常不会有太大变化
	 */
	private static final String BASE_PACKAGE = "df.learn.MySpringFramework.modules.";
	// 实体层包名
	public static final String PACKAGE_BEANS = BASE_PACKAGE + "entities";
	// 业务层包名
	public static final String PACKAGE_SERVICES = BASE_PACKAGE + "services";
	// 视图层包名
	public static final String PACKAGE_CONTROLLERS = BASE_PACKAGE + "controllers";
	// 数据访问层包名
	public static final String PACKAGES_REPOSITORIES = BASE_PACKAGE + "repositories";
	/**
	 * 定时任务类包名，使用Spring-Schedule需要添加注解@EnableScheduling
	 */
	public static final String PACKAGES_QUARTZ = BASE_PACKAGE + "quartz";
	public static final String CONFIG_PROPERTIES_NAME = "config.properties";
	public static final String APP_NAME = "DF";
	public static final String THUMBNAIL_ = "thumbnail_";

	/**
	 * 用于读取配置文件的方法，{@link PropertyProcessor} 类用于扩展和存储自己的配置文件
	 * @Methods propertyProcessor
	 * 
	 * @return
	 * 
	 * @Description TODO
	 */
	@Bean
	public static PropertyPlaceholderConfigurer propertyProcessor() {
		PropertyPlaceholderConfigurer configurer = new PropertyProcessor();
		configurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
		configurer.setIgnoreResourceNotFound(true);
		configurer.setSearchSystemEnvironment(true);
		configurer.setFileEncoding(APP_ENCODING);
		configurer.setLocation(new ClassPathResource(CONFIG_PROPERTIES_NAME));
		return configurer;
	}

	/**
	 * 
	 * @Methods applicationListener
	 * 
	 * @return
	 * 
	 * @Description TODO
	 */
	@Bean
	public ApplicationListener<ContextRefreshedEvent> applicationListener() {
		return new SpringContextInitializeListener();
	}
}
