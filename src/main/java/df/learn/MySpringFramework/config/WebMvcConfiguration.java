package df.learn.MySpringFramework.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;

import df.learn.MySpringFramework.commons.utils.JsonUtils;
import df.learn.MySpringFramework.config.web.HandlerMethodExceptionResolver;
import df.learn.MySpringFramework.config.web.RESTfulMultipartResolver;
import df.learn.MySpringFramework.config.web.interceptors.GlobalInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ApplicationConfiguration.PACKAGE_CONTROLLERS, includeFilters = { @Filter(type = FilterType.ANNOTATION, value = Controller.class), @Filter(type = FilterType.ANNOTATION, value = ControllerAdvice.class), })
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
	static final Map<String, MediaType> MEDIA_TYPE_MAP = new HashMap<String, MediaType>(3);
	static final List<MediaType> MEDIA_TYPES = new ArrayList<MediaType>(3);

	static {
		MEDIA_TYPE_MAP.put("json", new MediaType("application", "json"));
		MEDIA_TYPE_MAP.put("plain", new MediaType("text", "plain"));
		MEDIA_TYPE_MAP.put("html", new MediaType("text", "html"));
		MEDIA_TYPES.addAll(MEDIA_TYPE_MAP.values());
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 全局日志
		registry.addInterceptor(new GlobalInterceptor());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// HttpMessage转换器
		converters.add(byteArrayHttpMessageConverter());
		converters.add(mappingJacksonHttpMessageConverter());
		converters.add(stringHttpMessageConverter());
	}

	@Bean
	public ContentNegotiationManagerFactoryBean contentNegotiationManager() {
		ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean = new ContentNegotiationManagerFactoryBean();
		contentNegotiationManagerFactoryBean.addMediaTypes(MEDIA_TYPE_MAP);
		contentNegotiationManagerFactoryBean.setFavorPathExtension(false);
		contentNegotiationManagerFactoryBean.setFavorParameter(false);
		contentNegotiationManagerFactoryBean.setIgnoreAcceptHeader(false);
		return contentNegotiationManagerFactoryBean;
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		// 异常处理器
		HandlerMethodExceptionResolver resolver = new HandlerMethodExceptionResolver();
		resolver.setDefaultErrorView("common/404");
		exceptionResolvers.add(resolver);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "index");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 资源文件
		registry.addResourceHandler("css/**", "fonts/**", "images/**", "js/**", "vendors/**", "assets/**", "app/**", "mobileRec/**").addResourceLocations("/sources/css/", "/sources/fonts/", "/sources/images/", "/sources/js/", "/sources/vendors/",
				"/sources/assets/", "/app/", "/sources/mobileRec/");
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/jsp/", ".jsp");
	}

	/**
	 * 支持文件上传
	 * 
	 * @return
	 */
	@Bean
	@Description("for file upload")
	public RESTfulMultipartResolver multipartResolver() {
		RESTfulMultipartResolver resolver = new RESTfulMultipartResolver();
		resolver.setMaxUploadSize(1024L * 1024L * 200L);
		resolver.setResolveLazily(true);
		return resolver;
	}

	// @Bean
	// @Description("for request")
	// public RequestMappingHandlerMapping requestMappingHandlerMapping() {
	// RequestMappingHandlerMapping mapping = new
	// RequestMappingHandlerMapping();
	// mapping.setInterceptors(new Object[] { new GlobalInterceptor()
	// });//openEntityManagerInViewInterceptor
	// return mapping;
	// }

	@Bean
	public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
		return new SimpleControllerHandlerAdapter();
	}

	@Bean
	public SimpleServletHandlerAdapter simpleServletHandlerAdapter() {
		return new SimpleServletHandlerAdapter();
	}

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		converter.setSupportedMediaTypes(MEDIA_TYPES);
		return converter;
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		return new ByteArrayHttpMessageConverter();
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MEDIA_TYPES);
		converter.setObjectMapper(JsonUtils.getJacksonMapper());
		return converter;
	}

	// @Bean
	// public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
	// List<HttpMessageConverter<?>> messageConverters = new
	// ArrayList<HttpMessageConverter<?>>();
	// messageConverters.add(byteArrayHttpMessageConverter());
	// messageConverters.add(mappingJacksonHttpMessageConverter());
	// RequestMappingHandlerAdapter adapter = new
	// RequestMappingHandlerAdapter();
	// adapter.setMessageConverters(messageConverters);
	// return adapter;
	// }
}
