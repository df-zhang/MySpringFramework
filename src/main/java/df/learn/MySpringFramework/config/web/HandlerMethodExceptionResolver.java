package df.learn.MySpringFramework.config.web;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.hibernate.TypeMismatchException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import df.learn.MySpringFramework.commons.JsonException;
import df.learn.MySpringFramework.commons.utils.HttpUtils;
import df.learn.MySpringFramework.config.ApplicationConfiguration;
import df.learn.MySpringFramework.config.web.ApiResponse.ApiResponseBuilder;

/**
 * 不必在Controller中对异常进行处理，抛出即可，由此异常解析器统一控制。<br>
 * ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。<br>
 * 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。<br>
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 * 
 * @author zdf
 */
public class HandlerMethodExceptionResolver extends ExceptionHandlerExceptionResolver {
	private static Logger logger = Logger.getLogger(HandlerMethodExceptionResolver.class);

	private String defaultErrorView;

	public String getDefaultErrorView() {
		return defaultErrorView;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
		logger.error(exception.getMessage(), exception);
		// 如果抛出的异常为JsonException
		if (exception.getClass() == JsonException.class) {
			return handleResponse(request, response, exception);
		}
		// HttpMessageNotReadableException MissingServletRequestParameterException TypeMismatchException 400 (Bad Request)
		if (exception instanceof HttpMessageNotReadableException || exception instanceof MissingServletRequestParameterException || exception instanceof TypeMismatchException) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
		}

		// HttpRequestMethodNotSupportedException 405 (Method Not Allowed)
		if (exception instanceof HttpRequestMethodNotSupportedException) {
			// 方法不支持
			response.setStatus(HttpStatus.SC_METHOD_NOT_ALLOWED);
		}
		// HttpMediaTypeNotAcceptableException 406 (Not Acceptable)
		if (exception instanceof HttpMediaTypeNotAcceptableException) {
			response.setStatus(HttpStatus.SC_NOT_ACCEPTABLE);
		}

		// HttpMediaTypeNotSupportedException 415 (Unsupported Media Type)
		if (exception instanceof HttpMediaTypeNotSupportedException) {
			response.setStatus(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
		}
		// NoSuchRequestHandlingMethodException
		if (exception instanceof NoSuchRequestHandlingMethodException) {
			response.setStatus(HttpStatus.SC_NOT_FOUND);
		}
		// ConversionNotSupportedException 500 (Internal Server Error)
		if (exception instanceof ConversionNotSupportedException || exception instanceof HttpMessageNotWritableException) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}

		if (handlerMethod == null) {
			if (HttpUtils.isAjax(request)) {
				return handleResponse(request, response, exception);
			}
			return new ModelAndView(defaultErrorView);
		}
		Method method = handlerMethod.getMethod();
		if (method == null) {
			if (HttpUtils.isAjax(request)) {
				return handleResponse(request, response, exception);
			}
			return new ModelAndView(defaultErrorView);
		}

		// 如果是返回Response
		if (handlerMethod.getReturnType().getParameterType() == ApiResponse.class) {
			return handleResponse(request, response, exception);
		}
		// 如果方法上有标记ResponseBody
		ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if (responseBodyAnn != null) {
			return handleResponse(request, response, exception);
		}
		// 如果类上有标记RestController
		if (AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RestController.class) != null) {
			return handleResponse(request, response, exception);
		}
		//
		ModelAndView returnValue = super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
		if (returnValue != null && returnValue.getViewName() == null) {
			if (HttpUtils.isAjax(request)) {
				return handleResponse(request, response, exception);
			} else {
				returnValue.setViewName(defaultErrorView);
			}

		}
		if (HttpUtils.isAjax(request)) {
			return handleResponse(request, response, exception);
		}
		return new ModelAndView(defaultErrorView);

	}

	private ModelAndView handleResponse(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String accept = request.getHeader("accept");
			if (StringUtils.isNotEmpty(accept)) {
				// 判断请求数据类型
			}

			out.write(ApiResponseBuilder.create().status(ApiStatus.EXCEPTION).message(exception.getClass().getName()).data(exception.getMessage()).build().toJsonString().getBytes(ApplicationConfiguration.APP_CHARSET));
			out.flush();
			response.flushBuffer();
		} catch (IOException e) {
		} finally {
		}
		return new ModelAndView();
	}
}
